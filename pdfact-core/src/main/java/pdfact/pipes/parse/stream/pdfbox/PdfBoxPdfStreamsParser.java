package pdfact.pipes.parse.stream.pdfbox;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.util.Matrix;

import com.google.inject.Inject;

import pdfact.model.Character;
import pdfact.model.Figure;
import pdfact.model.Page;
import pdfact.model.Page.PageFactory;
import pdfact.model.PdfDocument;
import pdfact.model.Point;
import pdfact.model.Rectangle;
import pdfact.model.Shape;
import pdfact.pipes.parse.stream.PdfStreamsParser;
import pdfact.pipes.parse.stream.pdfbox.operators.OperatorProcessor;
import pdfact.util.exception.PdfActException;
import pdfact.util.exception.PdfActParseException;
import pdfact.util.statistic.CharacterStatistician;

/**
 * Parses PDF content streams and interprets the related operations. Provides a
 * callback interface for clients to handle specific operations.
 * 
 * @author Claudius Korzen
 */
public class PdfBoxPdfStreamsParser implements PdfStreamsParser {
  /**
   * The factory to create instances of PdfPage.
   */
  protected PageFactory pageFactory;

  /**
   * The map of operator processors.
   */
  protected Map<String, OperatorProcessor> operatorProcessors;

  /**
   * The statistician to compute statistics about characters.
   */
  protected CharacterStatistician statistician;

  /**
   * The current page in the PDF file.
   */
  protected PDPage page;

  /**
   * The resources of the current page.
   */
  protected PDResources resources;

  /**
   * The graphics stack of the current page.
   */
  protected Stack<PDGraphicsState> graphicsStack;

  /**
   * The current geometric path constructed from straight lines, quadratic and
   * cubic (Bézier) curves.
   */
  protected GeneralPath linePath;

  /**
   * The current position of the line path.
   */
  protected float[] linePathPosition;

  /**
   * The position of last MOVETO operation.
   */
  protected float[] linePathLastMoveToPosition;

  /**
   * The clipping winding rule used for the clipping path.
   */
  protected int clippingWindingRule = -1;

  /**
   * The initial matrix of the page.
   */
  protected Matrix initialMatrix;

  /**
   * The current text matrix.
   */
  protected Matrix textMatrix;

  /**
   * The current text line matrix.
   */
  protected Matrix textLineMatrix;

  /**
   * The current type3 glyph bounding box.
   */
  protected Rectangle currentType3GlyphBoundingBox;

  /**
   * Flag to indicate, whether the current stream is a type3 stream.
   */
  protected boolean isType3Stream;

  /**
   * The log.
   */
  protected static final Log LOG = LogFactory
      .getLog(PdfBoxPdfStreamsParser.class);

  /**
   * Creates a new stream engine.
   * 
   * @param pageFactory
   *        The factory to create instance of {@link Page}.
   * @param operators
   *        The operator processors to investigate on parsing.
   * @param statistician
   *        The statistician to compute statistics about characters.
   */
  @Inject
  public PdfBoxPdfStreamsParser(PageFactory pageFactory,
      Set<OperatorProcessor> operators, CharacterStatistician statistician) {
    this.pageFactory = pageFactory;
    this.operatorProcessors = new HashMap<>();
    for (OperatorProcessor operator : operators) {
      this.operatorProcessors.put(operator.getName(), operator);
    }
    this.statistician = statistician;
    this.graphicsStack = new Stack<PDGraphicsState>();
    this.linePath = new GeneralPath();
  }

  // ==========================================================================
  // Methods to process the file.

  @Override
  public void parse(PdfDocument pdf) throws PdfActException {
    try (PDDocument doc = PDDocument.load(pdf.getFile())) {
      handlePdfFileStart(pdf);
      for (int i = 0; i < doc.getPages().getCount(); i++) {
        processPage(pdf, doc.getPages().get(i), i + 1);
      }
      handlePdfFileEnd(pdf);
    } catch (IOException e) {
      throw new PdfActParseException("Couldn't parse the PDF.", e);
    }
  }

  /**
   * Processes the given page.
   * 
   * @param pdf
   *        The PDF document to which the given page belongs to.
   * @param page
   *        The page to process
   * @param pageNum
   *        The number of the page in the PDF document.
   * @throws IOException
   *         If something went wrong while parsing the page.
   */
  protected void processPage(PdfDocument pdf, PDPage page, int pageNum)
      throws IOException {
    this.page = page;
    this.graphicsStack.clear();
    this.graphicsStack.push(new PDGraphicsState(page.getCropBox()));
    this.resources = null;
    this.textMatrix = null;
    this.textLineMatrix = null;
    this.initialMatrix = page.getMatrix();
    this.linePath = new GeneralPath();
    this.linePathPosition = null;
    this.linePathLastMoveToPosition = null;
    this.clippingWindingRule = -1;
    this.currentType3GlyphBoundingBox = null;
    this.isType3Stream = false;

    Page pdfPage = this.pageFactory.create(pageNum);

    handlePdfPageStart(pdf, pdfPage);
    processStream(pdf, pdfPage, page);
    handlePdfPageEnd(pdf, pdfPage);
  }

  /**
   * Processes the page content stream.
   * 
   * @param pdf
   *        The PDF document to which the stream belongs to.
   * @param page
   *        The PDF page to which the stream belongs to.
   * @param stream
   *        The content stream
   * @throws IOException
   *         if there is an exception while processing the stream
   */
  public void processStream(PdfDocument pdf, Page page, PDContentStream stream)
      throws IOException {
    if (stream != null) {
      PDResources parent = pushResources(stream);
      Stack<PDGraphicsState> savedStack = saveGraphicsStack();
      Matrix parentMatrix = this.initialMatrix;

      // Transform the CTM using the stream's matrix.
      getCurrentTransformationMatrix().concatenate(stream.getMatrix());

      // The stream's initial matrix includes the parent CTM, e.g. this
      // allows a scaled form.
      this.initialMatrix = getCurrentTransformationMatrix().clone();

      // TODO: Do we need this snippet?
      // clip to bounding box
      // PDRectangle bbox = stream.getBBox();
      // clipToRect(bbox);

      processStreamOperators(pdf, page, stream);

      // Restore the initialMatrix, the graphics stack and the resources.
      this.initialMatrix = parentMatrix;
      restoreGraphicsStack(savedStack);
      popResources(parent);
    }
  }

  /**
   * Processes a type 3 character stream.
   * 
   * @param pdf
   *        The PDF document to which the stream belongs to.
   * @param page
   *        The PDF page to which the stream belongs to.
   * @param proc
   *        Type 3 character procedure
   * @param trm
   *        The text Rendering Matrix
   * @throws IOException
   *         if processing the type stream fails.
   */
  public void processType3Stream(PdfDocument pdf, Page page,
      PDType3CharProc proc, Matrix trm) throws IOException {
    PDResources parent = pushResources(proc);
    Stack<PDGraphicsState> savedStack = saveGraphicsStack();

    // Replace the CTM with the TRM
    setCurrentTransformationMatrix(trm);

    // Transform the CTM using the stream's matrix (this is the FontMatrix)
    getCurrentTransformationMatrix().concatenate(proc.getMatrix());

    // Save text matrices (Type 3 stream may contain BT/ET, see PDFBOX-2137)
    Matrix oldTextMatrix = getTextMatrix();
    setTextMatrix(new Matrix());
    Matrix oldTextLineMatrix = getTextLineMatrix();
    setTextLineMatrix(new Matrix());

    setIsType3Stream(true);
    processStreamOperators(pdf, page, proc);
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
   * @param pdf
   *        The PDF document to which the stream belongs to.
   * @param page
   *        The PDF page to which the stream belongs to.
   * @param stream
   *        The stream.
   * @throws IOException
   *         if parsing the stream fails.
   */
  protected void processStreamOperators(PdfDocument pdf, Page page,
      PDContentStream stream) throws IOException {
    List<COSBase> arguments = new ArrayList<COSBase>();

    PDFStreamParser parser = new PDFStreamParser(stream);
    parser.parse();

    for (Object token : parser.getTokens()) {
      if (token instanceof COSObject) {
        arguments.add(((COSObject) token).getObject());
      } else
        if (token instanceof Operator) {
          processOperator(pdf, page, (Operator) token, arguments);
          arguments = new ArrayList<COSBase>();
        } else {
          arguments.add((COSBase) token);
        }
    }
  }

  /**
   * This is used to handle an operator.
   * 
   * @param pdf
   *        The PDF document to which the operator belongs to.
   * @param page
   *        The PDF page to which the operator belongs to.
   * @param operation
   *        The operation to perform.
   * @param arguments
   *        The list of arguments.
   * @throws IOException
   *         If there is an error processing the operation.
   */
  public void processOperator(PdfDocument pdf, Page page, String operation,
      List<COSBase> arguments) throws IOException {
    Operator operator = Operator.getOperator(operation);
    processOperator(pdf, page, operator, arguments);
  }

  /**
   * This is used to handle an operator.
   * 
   * @param pdf
   *        The PDF document to which the operator belongs to.
   * @param page
   *        The PDF page to which the operator belongs to.
   * @param op
   *        The operation to perform.
   * @param args
   *        The list of arguments.
   * @throws IOException
   *         If there is an error processing the operation.
   */
  protected void processOperator(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    OperatorProcessor processor = this.operatorProcessors.get(op.getName());

    // System.out.println(pageNum + " " + operator + " " + operands);

    if (processor != null) {
      try {
        processor.setStreamEngine(this);
        processor.process(pdf, page, op, args);
      } catch (IOException e) {
        LOG.warn("Error on processing operator '" + op + "'. ", e);
      }
    } else {
      LOG.trace("Unsupported operator: " + op);
    }
  }

  // ========================================================================
  // Methods related to resources.

  /**
   * Pushes the given stream's resources, returning the previous resources.
   * 
   * @param stream
   *        The stream.
   * @return The resources.
   */
  protected PDResources pushResources(PDContentStream stream) {
    // Lookup resources: first look for stream resources, then fallback to
    // the current page
    PDResources parentResources = this.resources;
    PDResources streamResources = stream.getResources();
    if (streamResources != null) {
      this.resources = streamResources;
    } else {
      // else if (resources != null) {
      // inherit directly from parent stream, this is not in the PDF spec,
      // but the file from PDFBOX-1359 does this and works in Acrobat
      // }
      this.resources = this.page.getResources();
    }

    // resources are required in PDF
    if (this.resources == null) {
      this.resources = new PDResources();
    }
    return parentResources;
  }

  /**
   * Pops the current resources and replaces them with the given resources.
   * 
   * @param parentResources
   *        The resources.
   */
  protected void popResources(PDResources parentResources) {
    this.resources = parentResources;
  }

  /**
   * Returns the resources of the page.
   * 
   * @return The resources.
   */
  public PDResources getResources() {
    return this.resources;
  }

  // ========================================================================
  // Methods related to the graphics stack.

  /**
   * Saves the entire graphics stack.
   * 
   * @return The saved graphics stack.
   */
  public final Stack<PDGraphicsState> saveGraphicsStack() {
    Stack<PDGraphicsState> savedStack = this.graphicsStack;
    this.graphicsStack = new Stack<PDGraphicsState>();
    this.graphicsStack.add(savedStack.peek().clone());
    return savedStack;
  }

  /**
   * Restores the entire graphics stack.
   * 
   * @param snapshot
   *        The graphics stack to restore.
   */
  public void restoreGraphicsStack(Stack<PDGraphicsState> snapshot) {
    this.graphicsStack = snapshot;
  }

  /**
   * Returns the size of the graphics stack.
   * 
   * @return The size of the graphics stack.
   */
  public int getGraphicsStackSize() {
    return this.graphicsStack.size();
  }

  /**
   * Returns the graphics state.
   * 
   * @return The graphics state.
   */
  public PDGraphicsState getGraphicsState() {
    return this.graphicsStack.peek();
  }

  /**
   * Pushes the current graphics state to the stack.
   */
  public void saveGraphicsState() {
    this.graphicsStack.push(this.graphicsStack.peek().clone());
  }

  /**
   * Pops the current graphics state from the stack.
   */
  public void restoreGraphicsState() {
    this.graphicsStack.pop();
  }

  // ========================================================================
  // Methods related to the current transformation matrix.

  /**
   * Returns the current transformation matrix.
   * 
   * @return The current transformation matrix.
   */
  public Matrix getCurrentTransformationMatrix() {
    return getGraphicsState().getCurrentTransformationMatrix();
  }

  /**
   * Sets the current transformation matrix.
   * 
   * @param matrix
   *        The current transformation matrix.
   */
  public void setCurrentTransformationMatrix(Matrix matrix) {
    getGraphicsState().setCurrentTransformationMatrix(matrix);
  }

  /**
   * Transforms the given coordinates by applying the current transformation
   * matrix.
   * 
   * @param p
   *        The point to transform.
   */
  // TODO: Maybe its a better idea to make the transformation *not* in place.
  public void transform(Point p) {
    transform(p, getCurrentTransformationMatrix());
  }

  /**
   * Transforms the given coordinates by applying the given matrix.
   * 
   * @param p
   *        The point to transform.
   * @param m
   *        The matrix to apply.
   */
  // TODO: Maybe its a better idea to make the transformation *not* in place.
  public void transform(Point p, Matrix m) {
    if (p != null && m != null) {
      p.setX(p.getX() * m.getScaleX() + p.getY() * m.getShearX()
          + m.getTranslateX());
      p.setY(p.getX() * m.getShearY() + p.getY() * m.getScaleY()
          + m.getTranslateY());
    }
  }

  // ========================================================================
  // Methods related to text matrices.

  /**
   * Returns the text line matrix.
   * 
   * @return The text line matrix.
   */
  public Matrix getTextLineMatrix() {
    return this.textLineMatrix;
  }

  /**
   * Sets the text line matrix.
   * 
   * @param value
   *        The text line matrix.
   */
  public void setTextLineMatrix(Matrix value) {
    this.textLineMatrix = value;
  }

  /**
   * Returns the text matrix.
   * 
   * @return The text matrix.
   */
  public Matrix getTextMatrix() {
    return this.textMatrix;
  }

  /**
   * Sets the text matrix.
   * 
   * @param value
   *        The text matrix to set.
   */
  public void setTextMatrix(Matrix value) {
    this.textMatrix = value;
  }

  // ========================================================================
  // Methods related to Type 3 fonts.

  /**
   * Returns true, if the current stream to parse is a type3 stream.
   * 
   * @return True, if the current stream to parse is a type3 stream.
   */
  public boolean isType3Stream() {
    return this.isType3Stream;
  }

  /**
   * Sets the isType3Stream flag.
   * 
   * @param isType3Stream
   *        The flag to set.
   */
  public void setIsType3Stream(boolean isType3Stream) {
    this.isType3Stream = isType3Stream;
  }

  /**
   * Sets the current type3 glyph bounding box.
   * 
   * @param boundingBox
   *        The bounding box.
   */
  public void setCurrentType3GlyphBoundingBox(Rectangle boundingBox) {
    this.currentType3GlyphBoundingBox = boundingBox;
  }

  /**
   * Returns the current type3 glyph bounding box.
   * 
   * @return The current type3 glyph bounding box.
   */
  public Rectangle getCurrentType3GlyphBoundingBox() {
    return this.currentType3GlyphBoundingBox;
  }

  // ==========================================================================
  // Methods related to the line path.

  /**
   * Returns the current line path of this stripper.
   * 
   * @return The line path.
   */
  public GeneralPath getLinePath() {
    return this.linePath;
  }

  /**
   * Sets the line path of this stripper.
   * 
   * @param path
   *        The line path to set.
   */
  public void setLinePath(GeneralPath path) {
    if (this.linePath == null || this.linePath.getCurrentPoint() == null) {
      this.linePath = path;
    } else {
      this.linePath.append(path, false);
    }
  }

  /**
   * Returns the current position of the line path.
   * 
   * @return The current position of the line path.
   */
  public float[] getLinePathPosition() {
    return this.linePathPosition;
  }

  /**
   * Sets the current position of the line path.
   * 
   * @param linePathPosition
   *        The position to set.
   */
  public void setLinePathPosition(float[] linePathPosition) {
    this.linePathPosition = linePathPosition;
  }

  /**
   * Returns the position of the last moveto operation in line path.
   * 
   * @return The position of the last moveto operation in line path.
   */
  public float[] getLinePathLastMoveToPosition() {
    return this.linePathLastMoveToPosition;
  }

  /**
   * Sets the position of the last moveto operation in line path.
   * 
   * @param position
   *        The position to set.
   */
  public void setLinePathLastMoveToPosition(float[] position) {
    this.linePathLastMoveToPosition = position;
  }

  /**
   * Returns the current clipping path.
   * 
   * @return The current clipping path.
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
   *        The winding rule which will be used for clipping.
   */
  public void setClippingWindingRule(int rule) {
    this.clippingWindingRule = rule;
  }

  // ==========================================================================
  // Handler methods.

  /**
   * A callback to handle the start of parsing the PDF file.
   * 
   * @param pdf
   *        The PDF document.
   */
  public void handlePdfFileStart(PdfDocument pdf) {
    // Nothing to do so far.
  }

  /**
   * A callback to handle the end of parsing the PDF file.
   * 
   * @param pdf
   *        The PDF document.
   */
  public void handlePdfFileEnd(PdfDocument pdf) {
    // Compute the character statistics for the whole PDF document.
    pdf.setCharacterStatistic(this.statistician.aggregate(pdf.getPages()));
  }

  /**
   * A callback to handle the start of the processing of a PDF page.
   * 
   * @param pdf
   *        The PDF document to which the given PDF page belongs to.
   * @param page
   *        The page to process.
   */
  public void handlePdfPageStart(PdfDocument pdf, Page page) {
    pdf.addPage(page);
  }

  /**
   * A callback to handle the end of the processing of a PDF page.
   * 
   * @param pdf
   *        The PDF document to which the given PDF page belongs to.
   * @param page
   *        The page to process.
   */
  public void handlePdfPageEnd(PdfDocument pdf, Page page) {
    // Compute the character statistics for the page.
    page.setCharacterStatistic(this.statistician.compute(page));
  }

  /**
   * A callback to handle a character.
   * 
   * @param pdf
   *        The PDF document to which the given character belongs to.
   * @param page
   *        The PDF page to which the given character belongs to.
   * @param c
   *        The character to process.
   */
  public void handlePdfCharacter(PdfDocument pdf, Page page, Character c) {
    page.addCharacter(c);
  }

  /**
   * A callback to handle a PdfFigure.
   * 
   * @param pdf
   *        The PDF document to which the given figure belongs to.
   * @param page
   *        The PDF page to which the given figure belongs to.
   * @param figure
   *        The figure to process.
   */
  public void handlePdfFigure(PdfDocument pdf, Page page, Figure figure) {
    page.addFigure(figure);
  }

  /**
   * A callback to handle a PdfShape.
   * 
   * @param pdf
   *        The PDF document to which the given shape belongs to.
   * @param page
   *        The PDF page to which the given shape belongs to.
   * @param shape
   *        The shape to process.
   */
  public void handlePdfShape(PdfDocument pdf, Page page, Shape shape) {
    page.addShape(shape);
  }
}