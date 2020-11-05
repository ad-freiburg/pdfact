package pdfact.core.pipes.parse.stream.pdfbox;

import static pdfact.core.PdfActCoreSettings.FLOATING_NUMBER_PRECISION;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.util.Matrix;

import pdfact.core.model.Character;
import pdfact.core.model.Figure;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Point;
import pdfact.core.model.Rectangle;
import pdfact.core.model.Shape;
import pdfact.core.pipes.parse.stream.PdfStreamsParser;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingColorN;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingColorSpace;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceCMYKColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceGrayColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceRGBColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingColorN;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingColorSpace;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingDeviceCMYKColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingDeviceGrayColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingDeviceRGBColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.AppendRectangleToPath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.BeginInlineImage;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.ClipEvenOddRule;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.ClipNonZeroRule;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.ClosePath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.CurveTo;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.CurveToReplicateFinalPoint;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.CurveToReplicateInitialPoint;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.EndPath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.FillEvenOddAndStrokePath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.FillEvenOddRule;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.FillNonZeroAndStrokePath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.FillNonZeroRule;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.Invoke;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.LineTo;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.ModifyCurrentTransformationMatrix;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.MoveTo;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.RestoreGraphicsState;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.SaveGraphicsState;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.SetGraphicsStateParameters;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.StrokePath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.BeginText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.EndText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveTextSetLeading;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveToNextLine;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveToNextLineAndShowText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveToNextLineAndShowTextWithSpacing;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetCharacterSpacing;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetFontAndSize;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextHorizontalScaling;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextLeading;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextMatrix;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextRenderingMode;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextRise;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetType3GlyphWidthAndBoundingBox;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetWordSpacing;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.ShowText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.ShowTextWithIndividualGlyphPositioning;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.exception.PdfActParseException;
import pdfact.core.util.statistician.CharacterStatistician;

// TODO: Refactor all the PDFBox utils (remove unnecessary stuff).

/**
 * Parses PDF content streams and interprets the related operations. Provides a
 * callback interface for clients to handle specific operations.
 * 
 * @author Claudius Korzen
 */
public class PdfBoxPdfStreamsParser implements PdfStreamsParser {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PdfBoxPdfStreamsParser.class);

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
   * The number of extracted pages.
   */
  protected int numPages;

  /**
   * The number of extracted characters.
   */
  protected int numCharacters;

  /**
   * The number of extracted figures.
   */
  protected int numFigures;

  /**
   * The number of extracted shapes.
   */
  protected int numShapes;

  /**
   * Creates a new stream engine.
   */
  public PdfBoxPdfStreamsParser() {
    this.operatorProcessors = new HashMap<>();

    // Install the text operator modules.
    registerOperatorProcessor(new BeginText()); // BT
    registerOperatorProcessor(new EndText()); // ET
    registerOperatorProcessor(new MoveText()); // Td
    registerOperatorProcessor(new MoveTextSetLeading()); // TD
    registerOperatorProcessor(new MoveToNextLineAndShowText()); // '
    registerOperatorProcessor(new MoveToNextLineAndShowTextWithSpacing()); // "
    registerOperatorProcessor(new SetCharacterSpacing()); // Tc
    registerOperatorProcessor(new SetFontAndSize()); // Tf
    registerOperatorProcessor(new MoveToNextLine()); // T*
    registerOperatorProcessor(new SetTextHorizontalScaling()); // Tz
    registerOperatorProcessor(new SetTextLeading()); // TL
    registerOperatorProcessor(new SetTextMatrix()); // Tm
    registerOperatorProcessor(new SetTextRenderingMode()); // Tr
    registerOperatorProcessor(new SetTextRise()); // Ts
    registerOperatorProcessor(new SetType3GlyphWidthAndBoundingBox()); // d1
    registerOperatorProcessor(new SetWordSpacing()); // Tw
    registerOperatorProcessor(new ShowText()); // Tj
    registerOperatorProcessor(new ShowTextWithIndividualGlyphPositioning()); // TJ

    // Install the graphics operator modules.
    registerOperatorProcessor(new AppendRectangleToPath()); // re
    registerOperatorProcessor(new BeginInlineImage()); // BI
    registerOperatorProcessor(new ClipEvenOddRule()); // W*
    registerOperatorProcessor(new ClipNonZeroRule()); // W
    registerOperatorProcessor(new ClosePath()); // h
    registerOperatorProcessor(new CurveTo()); // c
    registerOperatorProcessor(new CurveToReplicateFinalPoint()); // y
    registerOperatorProcessor(new CurveToReplicateInitialPoint()); // v
    registerOperatorProcessor(new EndPath()); // n
    registerOperatorProcessor(new FillEvenOddAndStrokePath()); // B*
    registerOperatorProcessor(new FillEvenOddRule()); // f*
    registerOperatorProcessor(new FillNonZeroAndStrokePath()); // B
    registerOperatorProcessor(new FillNonZeroRule()); // f
    registerOperatorProcessor(new Invoke()); // Do
    registerOperatorProcessor(new LineTo()); // l
    registerOperatorProcessor(new ModifyCurrentTransformationMatrix()); // cm
    registerOperatorProcessor(new MoveTo()); // m
    registerOperatorProcessor(new RestoreGraphicsState()); // Q
    registerOperatorProcessor(new SaveGraphicsState()); // q
    registerOperatorProcessor(new SetGraphicsStateParameters()); // gs
    registerOperatorProcessor(new StrokePath()); // S

    // Install the color operator modules.
    registerOperatorProcessor(new SetNonStrokingColor()); // sc
    registerOperatorProcessor(new SetNonStrokingColorN()); // scn
    registerOperatorProcessor(new SetNonStrokingColorSpace()); // cs
    registerOperatorProcessor(new SetNonStrokingDeviceCMYKColor()); // k
    registerOperatorProcessor(new SetNonStrokingDeviceGrayColor()); // g
    registerOperatorProcessor(new SetNonStrokingDeviceRGBColor()); // rg
    registerOperatorProcessor(new SetStrokingColor()); // SC
    registerOperatorProcessor(new SetStrokingColorN()); // SCN
    registerOperatorProcessor(new SetStrokingColorSpace()); // CS
    registerOperatorProcessor(new SetStrokingDeviceCMYKColor()); // K
    registerOperatorProcessor(new SetStrokingDeviceGrayColor()); // G
    registerOperatorProcessor(new SetStrokingDeviceRGBColor()); // RG

    this.statistician = new CharacterStatistician();
    this.graphicsStack = new Stack<PDGraphicsState>();
    this.linePath = new GeneralPath();
  }

  protected void registerOperatorProcessor(OperatorProcessor processor) {
    this.operatorProcessors.put(processor.getName(), processor);
  }

  // ==============================================================================================
  // Methods to process the file.

  @Override
  public void parse(PdfDocument pdf) throws PdfActException {
    log.debug("Parsing the streams of the PDF file.");
    try (PDDocument doc = PDDocument.load(pdf.getFile())) {
      int numProcessors = this.operatorProcessors.size();
      log.debug("# registered PDF operator processors: " + numProcessors);

      handlePdfFileStart(pdf);
      for (int i = 0; i < doc.getPages().getCount(); i++) {
        processPage(pdf, doc.getPages().get(i), i + 1);
      }
      handlePdfFileEnd(pdf);

      log.debug("Parsing the streams of the PDF file done.");
      log.debug("# extracted pages: " + this.numPages);
      log.debug("# extracted characters: " + this.numCharacters);
      log.debug("# extracted figures: " + this.numFigures);
      log.debug("# extracted shapes: " + this.numShapes);
    } catch (IOException e) {
      throw new PdfActParseException("Couldn't parse the PDF.", e);
    }
  }

  /**
   * Processes the given page.
   * 
   * @param pdf     The PDF document to which the given page belongs to.
   * @param page    The page to process
   * @param pageNum The number of the page in the PDF document.
   * @throws IOException If something went wrong while parsing the page.
   */
  protected void processPage(PdfDocument pdf, PDPage page, int pageNum) throws IOException {
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

    Page pdfPage = new Page(pageNum);
    PDRectangle rect = page.getMediaBox();
    if (rect == null) {
      rect = page.getCropBox();
    }
    if (rect == null) {
      rect = page.getTrimBox();
    }
    if (rect != null) {
      pdfPage.setHeight(PdfActUtils.round(rect.getHeight(), FLOATING_NUMBER_PRECISION));
      pdfPage.setWidth(PdfActUtils.round(rect.getWidth(), FLOATING_NUMBER_PRECISION));
    }

    handlePdfPageStart(pdf, pdfPage);
    processStream(pdf, pdfPage, page);
    handlePdfPageEnd(pdf, pdfPage);
  }

  /**
   * Processes the page content stream.
   * 
   * @param pdf    The PDF document to which the stream belongs to.
   * @param page   The PDF page to which the stream belongs to.
   * @param stream The content stream
   * @throws IOException if there is an exception while processing the stream
   */
  public void processStream(PdfDocument pdf, Page page, PDContentStream stream) throws IOException {
    if (stream != null) {
      PDResources parent = pushResources(stream);
      Stack<PDGraphicsState> savedStack = saveGraphicsStack();
      Matrix parentMatrix = this.initialMatrix;

      // Transform the CTM using the stream's matrix.
      getCurrentTransformationMatrix().concatenate(stream.getMatrix());

      // The stream's initial matrix includes the parent CTM, e.g. this
      // allows a scaled form.
      this.initialMatrix = getCurrentTransformationMatrix().clone();

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
   * @param pdf  The PDF document to which the stream belongs to.
   * @param page The PDF page to which the stream belongs to.
   * @param proc Type 3 character procedure
   * @param trm  The text Rendering Matrix
   * @throws IOException if processing the type stream fails.
   */
  public void processType3Stream(PdfDocument pdf, Page page, PDType3CharProc proc, Matrix trm) throws IOException {
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
   * @param pdf    The PDF document to which the stream belongs to.
   * @param page   The PDF page to which the stream belongs to.
   * @param stream The stream.
   * @throws IOException if parsing the stream fails.
   */
  protected void processStreamOperators(PdfDocument pdf, Page page, PDContentStream stream) throws IOException {
    List<COSBase> arguments = new ArrayList<COSBase>();

    PDFStreamParser parser = new PDFStreamParser(stream);
    parser.parse();

    for (Object token : parser.getTokens()) {
      if (token instanceof COSObject) {
        arguments.add(((COSObject) token).getObject());
      } else if (token instanceof Operator) {
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
   * @param pdf       The PDF document to which the operator belongs to.
   * @param page      The PDF page to which the operator belongs to.
   * @param operation The operation to perform.
   * @param arguments The list of arguments.
   * @throws IOException If there is an error processing the operation.
   */
  public void processOperator(PdfDocument pdf, Page page, String operation, List<COSBase> arguments)
      throws IOException {
    Operator operator = Operator.getOperator(operation);
    processOperator(pdf, page, operator, arguments);
  }

  /**
   * This is used to handle an operator.
   * 
   * @param pdf  The PDF document to which the operator belongs to.
   * @param page The PDF page to which the operator belongs to.
   * @param op   The operation to perform.
   * @param args The list of arguments.
   * @throws IOException If there is an error processing the operation.
   */
  protected void processOperator(PdfDocument pdf, Page page, Operator op, List<COSBase> args) throws IOException {
    OperatorProcessor processor = this.operatorProcessors.get(op.getName());

    log.trace("Processing PDF operator: " + op + "; args: " + args);

    if (processor != null) {
      try {
        processor.setStreamEngine(this);
        processor.process(pdf, page, op, args);
      } catch (IOException e) {
        log.warn("Error on processing operator '" + op + "'. ", e);
      }
    } else {
      log.trace("Unsupported operator: " + op + "; args: " + args);
    }
  }

  // ==============================================================================================
  // Methods related to resources.

  /**
   * use the current transformation matrix to transform a single point.
   *
   * @param x x-coordinate of the point to be transform
   * @param y y-coordinate of the point to be transform
   * @return the transformed coordinates as Point2D.Double
   */
  public Point2D.Double transformedPoint(double x, double y) {
    double[] position = { x, y };
    getGraphicsState().getCurrentTransformationMatrix().createAffineTransform()
            .transform(position, 0, position, 0, 1);
    return new Point2D.Double(position[0], position[1]);
  }

  /**
   * use the current transformation matrix to transform a PDRectangle.
   * 
   * @param rect the PDRectangle to transform
   * @return the transformed coordinates as a GeneralPath
   */
  public GeneralPath transformedPDRectanglePath(PDRectangle rect) {
    float x1 = rect.getLowerLeftX();
    float y1 = rect.getLowerLeftY();
    float x2 = rect.getUpperRightX();
    float y2 = rect.getUpperRightY();
    Point2D p0 = transformedPoint(x1, y1);
    Point2D p1 = transformedPoint(x2, y1);
    Point2D p2 = transformedPoint(x2, y2);
    Point2D p3 = transformedPoint(x1, y2);
    GeneralPath path = new GeneralPath();
    path.moveTo((float) p0.getX(), (float) p0.getY());
    path.lineTo((float) p1.getX(), (float) p1.getY());
    path.lineTo((float) p2.getX(), (float) p2.getY());
    path.lineTo((float) p3.getX(), (float) p3.getY());
    path.closePath();
    return path;
  }

  /**
   * Pushes the given stream's resources, returning the previous resources.
   * 
   * @param stream The stream.
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
   * @param parentResources The resources.
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

  // ==============================================================================================
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
   * @param snapshot The graphics stack to restore.
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

  // ==============================================================================================
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
   * @param matrix The current transformation matrix.
   */
  public void setCurrentTransformationMatrix(Matrix matrix) {
    getGraphicsState().setCurrentTransformationMatrix(matrix);
  }

  /**
   * Transforms the given coordinates by applying the current transformation
   * matrix.
   * 
   * @param p The point to transform.
   */
  // TODO: Maybe its a better idea to make the transformation *not* in place.
  public void transform(Point p) {
    transform(p, getCurrentTransformationMatrix());
  }

  /**
   * Transforms the given coordinates by applying the given matrix.
   * 
   * @param p The point to transform.
   * @param m The matrix to apply.
   */
  // TODO: Maybe its a better idea to make the transformation *not* in place.
  public void transform(Point p, Matrix m) {
    if (p != null && m != null) {
      p.setX(p.getX() * m.getScaleX() + p.getY() * m.getShearX() + m.getTranslateX());
      p.setY(p.getX() * m.getShearY() + p.getY() * m.getScaleY() + m.getTranslateY());
    }
  }

  // ==============================================================================================
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
   * @param value The text line matrix.
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
   * @param value The text matrix to set.
   */
  public void setTextMatrix(Matrix value) {
    this.textMatrix = value;
  }

  // ==============================================================================================
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
   * @param isType3Stream The flag to set.
   */
  public void setIsType3Stream(boolean isType3Stream) {
    this.isType3Stream = isType3Stream;
  }

  /**
   * Sets the current type3 glyph bounding box.
   * 
   * @param boundingBox The bounding box.
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

  // ==============================================================================================
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
   * @param path The line path to set.
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
   * @param linePathPosition The position to set.
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
   * @param position The position to set.
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
   * The clipping path will not be updated until the succeeding painting operator
   * is called.
   * 
   * @param rule The winding rule which will be used for clipping.
   */
  public void setClippingWindingRule(int rule) {
    this.clippingWindingRule = rule;
  }

  // ==============================================================================================
  // Handler methods.

  /**
   * A callback to handle the start of parsing the PDF file.
   * 
   * @param pdf The PDF document.
   */
  public void handlePdfFileStart(PdfDocument pdf) {
    // Nothing to do so far.
  }

  /**
   * A callback to handle the end of parsing the PDF file.
   * 
   * @param pdf The PDF document.
   */
  public void handlePdfFileEnd(PdfDocument pdf) {
    // Compute the character statistics for the whole PDF document.
    pdf.setCharacterStatistic(this.statistician.aggregate(pdf.getPages()));
  }

  /**
   * A callback to handle the start of the processing of a PDF page.
   * 
   * @param pdf  The PDF document to which the given PDF page belongs to.
   * @param page The page to process.
   */
  public void handlePdfPageStart(PdfDocument pdf, Page page) {
    pdf.addPage(page);
    this.numPages++;
  }

  /**
   * A callback to handle the end of the processing of a PDF page.
   * 
   * @param pdf  The PDF document to which the given PDF page belongs to.
   * @param page The page to process.
   */
  public void handlePdfPageEnd(PdfDocument pdf, Page page) {
    // Compute the character statistics for the page.
    page.setCharacterStatistic(this.statistician.compute(page));
  }

  /**
   * A callback to handle a character.
   * 
   * @param pdf  The PDF document to which the given character belongs to.
   * @param page The PDF page to which the given character belongs to.
   * @param c    The character to process.
   */
  public void handlePdfCharacter(PdfDocument pdf, Page page, Character c) {
    page.addCharacter(c);
    this.numCharacters++;
  }

  /**
   * A callback to handle a PdfFigure.
   * 
   * @param pdf    The PDF document to which the given figure belongs to.
   * @param page   The PDF page to which the given figure belongs to.
   * @param figure The figure to process.
   */
  public void handlePdfFigure(PdfDocument pdf, Page page, Figure figure) {
    page.addFigure(figure);
    this.numFigures++;
  }

  /**
   * A callback to handle a PdfShape.
   * 
   * @param pdf   The PDF document to which the given shape belongs to.
   * @param page  The PDF page to which the given shape belongs to.
   * @param shape The shape to process.
   */
  public void handlePdfShape(PdfDocument pdf, Page page, Shape shape) {
    page.addShape(shape);
    this.numShapes++;
  }
}