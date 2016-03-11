package parser.pdfbox.core;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fontbox.afm.AFMParser;
import org.apache.fontbox.afm.CharMetric;
import org.apache.fontbox.afm.FontMetrics;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.util.Matrix;

import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.path.PathUtils;
import parser.pdfbox.core.operator.OperatorProcessor;
import parser.pdfbox.model.PdfBoxDocument;
import parser.pdfbox.model.PdfBoxFigure;
import parser.pdfbox.model.PdfBoxPage;
import parser.pdfbox.model.PdfBoxShape;
import parser.pdfbox.util.PdfFontUtil;

/**
 * Processes a PDF content stream and executes certain operations. Provides a
 * callback interface for clients that want to do things with the stream.
 * 
 * @author Claudius Korzen
 */
public class PdfStreamEngine {
  /** The log. */
  protected static final Log LOG = LogFactory.getLog(PdfStreamEngine.class);
  /** The path to the AFM dir. */
  protected static final String AFM_DIR_PATH = "afm/";
  
  /** The map of operator processors. */
  protected Map<String, OperatorProcessor> operators;
  /** The graphics stack. */
  protected Stack<PDGraphicsState> graphicsStack;

  /** The document to process. */
  protected PDDocument document;
  /** The current page. */
  protected PdfBoxPage currentPage;
  /** The current page number. */
  protected int currentPageNumber;
  /** The rectangle of the page. */
  protected PDRectangle pageSize;
  /** The resources. */
  protected PDResources resources;
  /** The initial matrix. */
  protected Matrix initialMatrix;
  /** The text matrix. */
  protected Matrix textMatrix;
  /** The text line matrix. */
  protected Matrix textLineMatrix;
  
  /** This object represents a geometric path constructed from straight lines,
      and quadratic and cubic (Bezier) curves. */
  protected GeneralPath linePath;
  /** The current position of linePath. */
  protected float[] linePathPosition;
  /** The position of last MOVETO operator. */
  protected float[] linePathLastMoveToPosition;
  /** The clipping winding rule used for the clipping path. */
  protected int clippingWindingRule = -1;
  
  /** Extended font metrics in addition to the 14 standard fonts. */
  protected Map<String, FontMetricsWrapper> additionalAFMs;
  /** The current type3 glyph bounding box. */
  protected Rectangle currentType3GlyphBoundingBox;
  /** Flag to indicate, whether the current stream is a type3 stream. */
  protected boolean isType3Stream;
  
  /**
   * Creates a new PdfStreamEngine.
   * 
   * @throws IOException
   *           if reading the additional AFM files fails.
   */
  public PdfStreamEngine() {
    this.operators = new HashMap<String, OperatorProcessor>();
    this.graphicsStack = new Stack<PDGraphicsState>();
    this.linePath = new GeneralPath();
  }

  /**
   * This will process the given file.
   * 
   * @param file
   *          the file to process.
   * @throws IOException
   *           if there is an error on processing the document.
   */
  public void processFile(Path pdfFile) throws IOException {    
    if (pdfFile != null) {
      PDDocument doc = PDDocument.load(pdfFile.toFile());
      PdfBoxDocument textDocument = new PdfBoxDocument(doc);
      textDocument.setPdfFile(pdfFile);
      
      reset(doc);
      startDocument(textDocument);
      processPages(doc.getDocumentCatalog().getPages());
      endDocument(textDocument);
      
      doc.close();
    }
  }

  /**
   * This will process the given pages.
   * 
   * @param pages
   *          The pages object in the document.
   * 
   * @throws IOException
   *           If there is an error parsing the text.
   */
  protected void processPages(PDPageTree pages) throws IOException {
    for (PDPage page : pages) {
      processPage(page);
    }
  }

  /**
   * This will process the given page.
   * 
   * @param page
   *          the page to process
   * @throws IOException
   *           if there is an error accessing the stream
   */
  public void processPage(PDPage page) throws IOException {         
    reset(page);
    startPage(this.currentPage);
    processStream(page);
    endPage(this.currentPage);
  }

  /**
   * Processes a content stream.
   * 
   * @param stream
   *          the content stream
   * @throws IOException
   *           if there is an exception while processing the stream
   */
  public void processStream(PDContentStream stream) throws IOException {
    if (stream != null) {
      PDResources parent = pushResources(stream);
      Stack<PDGraphicsState> savedStack = saveGraphicsStack();
      Matrix parentMatrix = initialMatrix;
  
      // Transform the CTM using the stream's matrix.
      getCurrentTransformationMatrix().concatenate(stream.getMatrix());
  
      // The stream's initial matrix includes the parent CTM, e.g. this allows 
      // a scaled form.
      this.initialMatrix = getCurrentTransformationMatrix().clone();
  
      // TODO: Do we need this snippet?
      // clip to bounding box
      // PDRectangle bbox = stream.getBBox();
      // clipToRect(bbox);
  
      processStreamOperators(stream);
      
      // Restore the initialMatrix, the graphics stack and the resources.
      this.initialMatrix = parentMatrix;
      restoreGraphicsStack(savedStack);
      popResources(parent);
    }
  }

  /**
   * Processes a type 3 character stream.
   * 
   * @param charProc
   *          Type 3 character procedure
   * @param trm
   *          the text Rendering Matrix
   * @throws IOException
   *           if processing the type stream fails.
   */
  public void processType3Stream(PDType3CharProc charProc, Matrix trm) 
      throws IOException {
    PDResources parent = pushResources(charProc);
    Stack<PDGraphicsState> savedStack = saveGraphicsStack();

    // Replace the CTM with the TRM
    setCurrentTransformationMatrix(trm);
    
    // Rransform the CTM using the stream's matrix (this is the FontMatrix)
    getCurrentTransformationMatrix().concatenate(charProc.getMatrix());
    
    // Save text matrices (Type 3 stream may contain BT/ET, see PDFBOX-2137)
    Matrix oldTextMatrix = getTextMatrix();
    setTextMatrix(new Matrix());
    Matrix oldTextLineMatrix = getTextLineMatrix();
    setTextLineMatrix(new Matrix());

    setIsType3Stream(true);
    processStreamOperators(charProc);
    setIsType3Stream(false);    
    
    // Restore text matrices
    setTextMatrix(oldTextMatrix);
    setTextLineMatrix(oldTextLineMatrix);

    restoreGraphicsStack(savedStack);
    popResources(parent);
  }
  
  /**
   * Processes the operators of the given content stream.
   * 
   * @param stream
   *          the stream.
   * @throws IOException
   *           if parsing the stream fails.
   */
  protected void processStreamOperators(PDContentStream stream)
    throws IOException {
    List<COSBase> arguments = new ArrayList<COSBase>();
    PDFStreamParser parser = new PDFStreamParser(stream);
    
    parser.parse();
    for (Object token : parser.getTokens()) {
      if (token instanceof COSObject) {
        arguments.add(((COSObject) token).getObject());
      } else if (token instanceof Operator) {
        processOperator((Operator) token, arguments);
        arguments = new ArrayList<COSBase>();
      } else {
        arguments.add((COSBase) token);
      }
    }
  }

  /**
   * This is used to handle an operation.
   * 
   * @param operation
   *          The operation to perform.
   * @param arguments
   *          The list of arguments.
   * @throws IOException
   *           If there is an error processing the operation.
   */
  public void processOperator(String operation, List<COSBase> arguments)
    throws IOException {
    Operator operator = Operator.getOperator(operation);
    processOperator(operator, arguments);
  }

  /**
   * This is used to handle an operation.
   * 
   * @param operator
   *          The operation to perform.
   * @param operands
   *          The list of arguments.
   * @throws IOException
   *           If there is an error processing the operation.
   */
  protected void processOperator(Operator operator, List<COSBase> operands)
    throws IOException {
    String name = operator.getName();
    OperatorProcessor processor = this.operators.get(name);
        
    // System.out.println(currentPageNumber + " " + operator + " " + operands);
    
    if (processor != null) {
      processor.setContext(this);
      try {
        processor.process(operator, operands);
      } catch (IOException e) {
        LOG.error("Error on processing operator " + operator.getName(), e);
      }
    } else {
      LOG.warn("Unsupported operator: " + operator);
    }
  }

  // ___________________________________________________________________________
  
  /**
   * Registers an operator processor to the engine.
   * 
   * @param operator
   *          the operator processor to register
   */
  protected void addOperator(OperatorProcessor operator) {
    if (operator != null) {
      operator.setContext(this);
      this.operators.put(operator.getName(), operator);  
    }
  }
  
  /**
   * Initializes the stream engine for the given document.
   * 
   * @param document
   *          the document.
   */
  protected void reset(PDDocument document) {
    this.document = document;
    this.currentPage = null;
    this.currentPageNumber = 0;
    this.pageSize = null;
  }

  /**
   * Initializes the stream engine for the given page.
   * 
   * @param page
   *          the page.
   */
  protected void reset(PDPage page) {
    this.currentPage = new PdfBoxPage(page, ++this.currentPageNumber);
    this.pageSize = page.getCropBox();
    this.graphicsStack.clear();
    this.graphicsStack.push(new PDGraphicsState(page.getCropBox()));
    this.resources = null;
    this.textMatrix = null;
    this.textLineMatrix = null;
    this.initialMatrix = page.getMatrix();
  }

  // ___________________________________________________________________________
  // Callback methods.

  /**
   * Called when the processing of the current document starts.
   * 
   * @param doc
   *          the document.
   * 
   * @throws IOException
   *           if there is an error on handling this event.
   */
  public void startDocument(PdfBoxDocument doc) throws IOException {
    // Override in subclasses.
  }

  /**
   * Called when the processing of the current document has finished.
   * 
   * @param doc
   *          the document.
   * 
   * @throws IOException
   *           if there is an error on handling this event.
   */
  public void endDocument(PdfBoxDocument doc) throws IOException {
    // Override in subclasses
  }

  /**
   * Called when the processing of the current page starts.
   * 
   * @param page
   *          the page.
   * 
   * @throws IOException
   *           if there is an error on handling this event.
   */
  public void startPage(PdfBoxPage page) throws IOException {
    // Override in subclasses.
  }

  /**
   * Called when the processing of the current page has finished.
   * 
   * @param page
   *          the page.
   * 
   * @throws IOException
   *           if there is an error on handling this event.
   */
  public void endPage(PdfBoxPage page) throws IOException {
    // Override in subclasses.
  }
  
  /**
   * Called when a glyph is to be processed.This method is intended for
   * overriding in subclasses, the default implementation does nothing.
   * 
   * @param trm
   *          the current text rendering matrix, T<sub>rm</sub>
   * @param font
   *          the current font
   * @param code
   *          internal PDF character code for the glyph
   * @param unicode
   *          the Unicode text for this glyph, or null if the PDF does provide
   *          it
   * @throws IOException
   *           if the glyph cannot be processed
   */
  public void showGlyph(String unicode, int code, PDFont font, Matrix trm)
    throws IOException {
    // Override in subclasses.
  }

  /**
   * Called when a ligature is to be processed. This method is intended for
   * overriding in subclasses, the default implementation does nothing.
   */
  public void showLigature(String[] unicodes, int code, PDFont font, 
      Matrix trm) throws IOException {
    // Override in subclasses.
  }
  
  /**
   * Called when the BI operator is encountered. This method is for overriding
   * in subclasses, the default implementation does nothing.
   * 
   * @param figure
   *          the figure to show.
   */
  public void showFigure(PdfBoxFigure figure) {
    // Override in subclasses
  }

  /**
   * This will process a shape.
   */
  public void showShape(PdfBoxShape shape) {
    // Override in subclasses.
  }
   
  // ___________________________________________________________________________
  // Resources methods.

  /**
   * Pushes the given stream's resources, returning the previous resources.
   * 
   * @param stream
   *          the stream.
   * @return the resources.
   */
  protected PDResources pushResources(PDContentStream stream) {
    // Lookup resources: first look for stream resources, then fallback to the
    // current page
    PDResources parentResources = resources;
    PDResources streamResources = stream.getResources();
    if (streamResources != null) {
      this.resources = streamResources;
    } else {
    // else if (resources != null) {
      // inherit directly from parent stream, this is not in the PDF spec, but
      // the file from PDFBOX-1359 does this and works in Acrobat
    // } 
      this.resources = currentPage.getResources();
    }

    // resources are required in PDF
    if (this.resources == null) {
      this.resources = new PDResources();
    }
    return parentResources;
  }

  /**
   * Pops the current resources, replacing them with the given resources.
   * 
   * @param parentResources
   *          the resources.
   */
  protected void popResources(PDResources parentResources) {
    this.resources = parentResources;
  }

  /**
   * Returns the stream' resources.
   * 
   * @return the resources.
   */
  public PDResources getResources() {
    return resources;
  }
  
  // ___________________________________________________________________________
  // Graphics stack methods.

  /**
   * Saves the entire graphics stack.
   * 
   * @return the saved graphics stack.
   */
  public final Stack<PDGraphicsState> saveGraphicsStack() {
    Stack<PDGraphicsState> savedStack = graphicsStack;
    graphicsStack = new Stack<PDGraphicsState>();
    graphicsStack.add(savedStack.peek().clone());
    return savedStack;
  }

  /**
   * Restores the entire graphics stack.
   * 
   * @param snapshot
   *          the graphics stack to restore.
   */
  public void restoreGraphicsStack(Stack<PDGraphicsState> snapshot) {
    this.graphicsStack = snapshot;
  }

  /**
   * Returns the size of the graphics stack.
   * 
   * @return the size of the graphics stack.
   */
  public int getGraphicsStackSize() {
    return graphicsStack.size();
  }

  /**
   * @return Returns the graphicsState.
   */
  public PDGraphicsState getGraphicsState() {
    return graphicsStack.peek();
  }

  /**
   * Pushes the current graphics state to the stack.
   */
  public void saveGraphicsState() {
    this.graphicsStack.push(graphicsStack.peek().clone());
  }

  /**
   * Pops the current graphics state from the stack.
   */
  public void restoreGraphicsState() {
    this.graphicsStack.pop();
  }

  /**
   * Returns the current transformation matrix.
   * 
   * @return the current transformation matrix.
   */
  public Matrix getCurrentTransformationMatrix() {
    return getGraphicsState().getCurrentTransformationMatrix();
  }
  
  /**
   * Sets the current transformation matrix.
   * 
   * @param matrix the current transformation matrix.
   */
  public void setCurrentTransformationMatrix(Matrix matrix) {
    getGraphicsState().setCurrentTransformationMatrix(matrix);
  }
  
  // ___________________________________________________________________________
  // Text matrix methods.

  /**
   * Returns the text line matrix.
   * 
   * @return the text line matrix.
   */
  public Matrix getTextLineMatrix() {
    return textLineMatrix;
  }

  /**
   * Sets the text line matrix.
   * 
   * @param value
   *          The text line matrix.
   */
  public void setTextLineMatrix(Matrix value) {
    this.textLineMatrix = value;
  }

  /**
   * Returns the text matrix.
   * 
   * @return the text matrix.
   */
  public Matrix getTextMatrix() {
    return textMatrix;
  }

  /**
   * Sets the text matrix.
   * 
   * @param value
   *          the text matrix to set.
   */
  public void setTextMatrix(Matrix value) {        
    this.textMatrix = value;
  }
 
  // ___________________________________________________________________________
  // Type 3 methods.
  
  /**
   * Returns true, if the current stream to parse is a type3 stream.
   *  
   * @return true, if the current stream to parse is a type3 stream.
   */
  public boolean isType3Stream() {
    return this.isType3Stream;
  }
  
  /**
   * Sets the isType3Stream flag.
   *  
   * @param isType3Stream the flag to set.
   */
  public void setIsType3Stream(boolean isType3Stream) {
    this.isType3Stream = isType3Stream;
  }
  
  /**
   * Sets the current type3 glyph bounding box.
   * 
   * @param boundingBox the bounding box.
   */
  public void setCurrentType3GlyphBoundingBox(Rectangle boundingBox) {    
    this.currentType3GlyphBoundingBox = boundingBox;
  }
  
  /**
   * Returns the current type3 glyph bounding box.
   * 
   * @return the current type3 glyph bounding box.
   */
  public Rectangle getCurrentType3GlyphBoundingBox() {
    return this.currentType3GlyphBoundingBox;
  }
  
  // ___________________________________________________________________________
  // Linepath methods. 
  
  /**
   * Returns the current line path of this stripper.
   * 
   * @return the line path.
   */
  public GeneralPath getLinePath() {
    return this.linePath;
  }
  
  /**
   * Sets the line path of this stripper.
   * 
   * @param path
   *          the line path to set.
   */
  public void setLinePath(GeneralPath path) {
    if (linePath == null || linePath.getCurrentPoint() == null) {
      linePath = path;
    } else {
      linePath.append(path, false);
    }
  }
          
  /**
   * Returns the current position of the line path.
   * 
   * @return the current position of the line path.
   */
  public float[] getLinePathPosition() {
    return linePathPosition;
  }

  /**
   * Sets the current position of the line path.
   * 
   * @param linePathPosition the position to set.
   */
  public void setLinePathPosition(float[] linePathPosition) {
    this.linePathPosition = linePathPosition;
  }

  /**
   * Returns the position of the last moveto operation in line path.
   * 
   * @return the position of the last moveto operation in line path.
   */
  public float[] getLinePathLastMoveToPosition() {
    return linePathLastMoveToPosition;
  }

  /**
   * Sets the position of the last moveto operation in line path.
   * 
   * @param position the psoition to set.
   */
  public void setLinePathLastMoveToPosition(float[] position) {
    this.linePathLastMoveToPosition = position;
  }
  
  /**
   * Returns the current clipping path.
   * 
   * @return the current clipping path.
   */
  public int getClippingWindingRule() {
    return this.clippingWindingRule;
  }
  
  /**
   * Modify the current clipping path by intersecting it with the current path.
   * The clipping path will not be updated until the succeeding painting
   * operator is called.
   * 
   * @param rule
   *          The winding rule which will be used for clipping.
   */
  public void setClippingWindingRule(int rule) {
    this.clippingWindingRule = rule;
  }
  
  // ___________________________________________________________________________
  // Helper methods.

  /**
   * Returns the CharMetric for the glyph given by glyphName. 
   * 
   * @param glyphName the name of glyph.
   * @param type1Font the font.
   * @return the CharMetric.
   */
  protected CharMetric getCharMetric(String glyphName, PDType1Font type1Font) 
      throws IOException {
    if (type1Font != null) {
      String basename = PdfFontUtil.computeBasename(type1Font);
      
      if (this.additionalAFMs == null) {
        this.additionalAFMs = readAdditionalAFMFiles("afm/");
      }
      
      FontMetricsWrapper fontMetrics = this.additionalAFMs.get(basename);
      if (fontMetrics != null) {
        return fontMetrics.getCharMetricsMap().get(glyphName);
      }
    }
    return null;
  }
  
  /**
   * Reads further AFM files in addition to the 14 standard fonts.
   * 
   * @param path
   *          the path to the directory of AFM files.
   * 
   * @return map containing the read font metrics.
   * @throws IOException
   *           if reading the files fails.
   */
  protected Map<String, FontMetricsWrapper> readAdditionalAFMFiles(
      String path) throws IOException {
    Map<String, InputStream> files = PathUtils.readDirectory(path);
    Map<String, FontMetricsWrapper> result = new HashMap<>();
    
    for (Entry<String, InputStream> file : files.entrySet()) {
      String name = file.getKey();
      InputStream stream = file.getValue();
      
      String basename = PathUtils.getBasename(name);
            
      try {
        AFMParser parser = new AFMParser(stream);
        FontMetrics fontMetrics = parser.parse();

        // Put the result into map.
        result.put(basename, new FontMetricsWrapper(fontMetrics));
      } catch (IOException e) {
        continue;
      } finally {
        stream.close();  
      }
    }
    
    return result;
  }

//  /**
//   * Transforms the given rectangle using the CTM and then intersects it with
//   * the current clipping area.
//   * 
//   * @param rectangle
//   *          the rectangle.
//   */
//  protected void clipToRect(PDRectangle rectangle) {
//    if (rectangle != null) {
//      Matrix ctm = getGraphicsState().getCurrentTransformationMatrix();
//      GeneralPath clip = rectangle.transform(ctm);
//      getGraphicsState().intersectClippingPath(clip);
//    }
//  }

//  /**
//   * Transforms a point using the CTM.
//   * 
//   * @param x
//   *          the x value
//   * @param y
//   *          the y value
//   * @return the transformed point.
//   */
//  public Point transformedPoint(float x, float y) {
//    float[] position = { x, y };
//    AffineTransform at = getCurrentTransformationMatrix()
//      .createAffineTransform();
//    at.transform(position, 0, position, 0, 1);
//    return new SimplePoint(position[0], position[1]);
//  }

  /**
   * Transforms the given coordinates by applying the current transformation 
   * matrix.
   * 
   * @param p
   *          the point to transform.
   */
  public void transform(Point p) {
    transform(p, getCurrentTransformationMatrix());
  }
  
  /**
   * Transforms the given coordinates by applying the given matrix.
   * 
   * @param p
   *          the point to transform.
   * @param m
   *          the matrix to apply.
   */
  public void transform(Point p, Matrix m) {
    if (p != null && m != null) {
      p.setX(p.getX() * m.getScaleX() + p.getY() * m.getShearX() 
          + m.getTranslateX());
      p.setY(p.getX() * m.getShearY() + p.getY() * m.getScaleY() 
          + m.getTranslateY());
    }
  }
  
  /**
   * Returns the current page.
   */
  public PdfBoxPage getCurrentPage() {
    return this.currentPage;
  }
}
