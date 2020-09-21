package pdfact.cli.pipes.visualize.pdfbox;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.cli.pipes.visualize.PdfDrawer;
import pdfact.core.model.HasRectangle;
import pdfact.core.model.Line;
import pdfact.core.model.Page;
import pdfact.core.model.Point;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.Line.LineFactory;
import pdfact.core.model.Point.PointFactory;
import pdfact.core.model.Rectangle.RectangleFactory;

/**
 * An implementation of PdfVisualizer using PdfBox.
 * 
 * @author Claudius Korzen.
 */
public class PdfBoxDrawer implements PdfDrawer {
  /**
   * The rectangle to create instances of Rectangle.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The rectangle to create instances of Line.
   */
  protected LineFactory lineFactory;

  /**
   * The rectangle to create instances of Point.
   */
  protected PointFactory pointFactory;

  /**
   * The default stroking color.
   */
  protected static final Color DEFAULT_STROKING_COLOR = Color.BLACK;

  /**
   * The default non-stroking color.
   */
  protected static final Color DEFAULT_NON_STROKING_COLOR = null;

  /**
   * The default font.
   */
  protected static final PDFont DEFAULT_FONT = PDType1Font.HELVETICA;

  /**
   * The default fontsize.
   */
  protected static final float DEFAULT_FONT_SIZE = 12;

  /**
   * The default line thickness.
   */
  protected static final float DEFAULT_LINE_THICKNESS = 0.1f;

  /**
   * The pdf file in fashion of PdfBox.
   */
  protected PDDocument pdDocument;

  /**
   * The cache of PDPageContentStream objects.
   */
  protected List<PDPageContentStream> pageStreams = new ArrayList<>();

  /**
   * The cache of page bounding boxes.
   */
  protected List<Rectangle> pageBoundingBoxes = new ArrayList<>();

  /**
   * Creates a new visualizer from the given file.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   * @param pointFactory
   *        The factory to create instances of Point.
   * @param lineFactory
   *        The factory to create instances of Line.
   * @param pdfFile
   *        The PDF file to process.
   * @throws IOException
   *         If reading the PDF file failed.
   */
  @AssistedInject
  public PdfBoxDrawer(RectangleFactory rectangleFactory,
      PointFactory pointFactory, LineFactory lineFactory,
      @Assisted Path pdfFile) throws IOException {
    this(rectangleFactory, pointFactory, lineFactory,
        pdfFile != null ? pdfFile.toFile() : null);
  }

  /**
   * Creates a new visualizer from the given file.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   * @param pointFactory
   *        The factory to create instances of Point.
   * @param lineFactory
   *        The factory to create instances of Line.
   * @param pdfFile
   *        The PDF file to process.
   * @throws IOException
   *         If reading the PDF file failed.
   */
  @AssistedInject
  public PdfBoxDrawer(RectangleFactory rectangleFactory,
      PointFactory pointFactory, LineFactory lineFactory,
      @Assisted File pdfFile) throws IOException {
    this(rectangleFactory, pointFactory, lineFactory,
        pdfFile != null ? PDDocument.load(pdfFile) : null);
  }

  /**
   * Creates a new visualizer from the given PDDocument.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   * @param pointFactory
   *        The factory to create instances of Point.
   * @param lineFactory
   *        The factory to create instances of Line.
   * @param pdDocument
   *        The PDDocument.
   * @throws IOException
   *         If parsing the PDDocument failed.
   */
  @AssistedInject
  public PdfBoxDrawer(RectangleFactory rectangleFactory,
      PointFactory pointFactory, LineFactory lineFactory, PDDocument pdDocument)
      throws IOException {
    this.rectangleFactory = rectangleFactory;
    this.pointFactory = pointFactory;
    this.lineFactory = lineFactory;
    this.pdDocument = pdDocument;

    if (pdDocument == null) {
      throw new IllegalArgumentException("No PD document given");
    }

    PDDocumentCatalog catalog = pdDocument.getDocumentCatalog();

    if (catalog == null) {
      throw new IllegalArgumentException("No document catalog given.");
    }

    PDPageTree pages = catalog.getPages();

    if (pages == null) {
      throw new IllegalArgumentException("No pages given.");
    }

    this.pageStreams.add(null); // Add dummy, because pageNumbers are 1-based.
    this.pageBoundingBoxes.add(null);
    // Preallocate the list of streams.
    for (PDPage page : pages) {
      this.pageStreams.add(new PDPageContentStream(pdDocument, page,
          PDPageContentStream.AppendMode.APPEND, true));

      Rectangle boundingBox = this.rectangleFactory.create();

      PDRectangle box = page.getCropBox();
      if (box == null) {
        box = page.getMediaBox();
      }
      if (box != null) {
        boundingBox.setMinX(box.getLowerLeftX());
        boundingBox.setMinY(box.getLowerLeftY());
        boundingBox.setMaxX(box.getUpperRightX());
        boundingBox.setMaxY(box.getUpperRightY());
      }

      this.pageBoundingBoxes.add(boundingBox);
    }
  }

  // ==========================================================================

  @Override
  public void drawLine(Line line, int pageNum) throws IOException {
    drawLine(line, pageNum, false, false);
  }

  @Override
  public void drawLine(Line line, int pageNum, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException {
    this.drawLine(line, pageNum, DEFAULT_STROKING_COLOR, relativeToUpperLeft,
        originInUpperLeft);
  }

  @Override
  public void drawLine(Line line, int pageNum, Color color) throws IOException {
    this.drawLine(line, pageNum, color, false, false);
  }

  @Override
  public void drawLine(Line line, int pageNum, Color color,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException {
    this.drawLine(line, pageNum, color, DEFAULT_LINE_THICKNESS,
        relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawLine(Line line, int pageNum, Color color, float thickness)
      throws IOException {
    drawLine(line, pageNum, color, thickness, false, false);
  }

  @Override
  public void drawLine(Line line, int pageNum, Color color, float thickness,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException {
    PDPageContentStream stream = getPdPageContentStream(pageNum);
    Line adapted = adaptLine(line, pageNum, relativeToUpperLeft,
        originInUpperLeft);

    float minX = adapted.getStartX();
    float minY = adapted.getStartY();
    float maxX = adapted.getEndX();
    float maxY = adapted.getEndY();

    // If width and/or height is 0, the lines are not visible. Set minimum
    // values for both values.
//    if (minX == maxX) {
//      maxX += thickness;
//    }
//    if (minY == maxY) {
//      maxY += thickness;
//    }

    stream.setStrokingColor(color);
    stream.setLineWidth(thickness);
    stream.moveTo(minX, minY);
    stream.lineTo(maxX, maxY);
    stream.stroke();
  }

  // ==========================================================================

  @Override
  public void drawRectangle(Rectangle rect, int pageNum) throws IOException {
    this.drawRectangle(rect, pageNum, false, false);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException {
    this.drawRectangle(rect, pageNum, DEFAULT_STROKING_COLOR,
        DEFAULT_NON_STROKING_COLOR, relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor) throws IOException {
    this.drawRectangle(rect, pageNum, strokingColor, nonStrokingColor, false,
        false);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException {
    this.drawRectangle(rect, pageNum, strokingColor, nonStrokingColor,
        DEFAULT_LINE_THICKNESS, relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, float thickness) throws IOException {
    drawRectangle(rect, pageNum, strokingColor, nonStrokingColor, thickness,
        false, false);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color strokingColor,
      Color nonStrokingColor, float thickness, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException {
    if (rect == null) {
      return;
    }
    PDPageContentStream stream = getPdPageContentStream(pageNum);

    Rectangle adapted = adaptRectangle(rect, pageNum, relativeToUpperLeft,
        originInUpperLeft);

    float minX = adapted.getMinX();
    float minY = adapted.getMinY();
    float width = adapted.getWidth();
    float height = adapted.getHeight();

    // If width and/or height is 0, the lines are not visible. Set minimum
    // values for both values.
    width = Math.max(thickness, width);
    height = Math.max(thickness, height);

    stream.setStrokingColor(strokingColor);
    stream.setLineWidth(thickness);
    stream.addRect(minX, minY, width, height);

    if (nonStrokingColor != null) {
      PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
      // graphicsState.setNonStrokingAlphaConstant(0.5f);
      stream.setGraphicsStateParameters(graphicsState);

      stream.setNonStrokingColor(nonStrokingColor);
      stream.fill();
    }

    stream.stroke();
  }

  // ==========================================================================

  @Override
  public void drawBoundingBox(HasRectangle box, int pageNum)
      throws IOException {
    this.drawRectangle(box.getRectangle(), pageNum);
  }

  @Override
  public void drawBoundingBox(HasRectangle box, int pageNum,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException {
    this.drawRectangle(box.getRectangle(), pageNum, relativeToUpperLeft,
        originInUpperLeft);
  }

  @Override
  public void drawBoundingBox(HasRectangle box, int pageNum,
      Color strokingColor, Color nonStrokingColor) throws IOException {
    this.drawRectangle(box.getRectangle(), pageNum, strokingColor,
        nonStrokingColor);
  }

  @Override
  public void drawBoundingBox(HasRectangle box, int pageNum,
      Color strokingColor, Color nonStrokingColor, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException {
    this.drawRectangle(box.getRectangle(), pageNum, strokingColor,
        nonStrokingColor, relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawBoundingBox(HasRectangle box, int pageNum,
      Color strokingColor, Color nonStrokingColor, float thickness)
      throws IOException {
    drawRectangle(box.getRectangle(), pageNum, strokingColor, nonStrokingColor,
        thickness);
  }

  @Override
  public void drawBoundingBox(HasRectangle box, int pageNum,
      Color strokingColor, Color nonStrokingColor, float thickness,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException {
    drawRectangle(box.getRectangle(), pageNum, strokingColor, nonStrokingColor,
        thickness, relativeToUpperLeft, originInUpperLeft);
  }

  // ==========================================================================
  
  @Override
  public void drawPosition(Position position) throws IOException {
    Rectangle rect = position.getRectangle();
    Page page = position.getPage();
    drawRectangle(rect, page.getPageNumber());
  }

  @Override
  public void drawPosition(Position position, boolean relativeToUpperLeft, 
      boolean originInUpperLeft) throws IOException {
    Rectangle rect = position.getRectangle();
    Page page = position.getPage();
    this.drawRectangle(rect, page.getPageNumber(), relativeToUpperLeft,
        originInUpperLeft);
  }

  @Override
  public void drawPosition(Position position, Color strokingColor, 
      Color nonStrokingColor) throws IOException {
    Rectangle rect = position.getRectangle();
    Page page = position.getPage();
    this.drawRectangle(rect, page.getPageNumber(), strokingColor,
        nonStrokingColor);
  }

  @Override
  public void drawPosition(Position position, Color strokingColor, 
      Color nonStrokingColor, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException {
    Rectangle rect = position.getRectangle();
    Page page = position.getPage();
    this.drawRectangle(rect, page.getPageNumber(), strokingColor,
        nonStrokingColor, relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawPosition(Position position, Color strokingColor, 
      Color nonStrokingColor, float thickness) throws IOException {
    Rectangle rect = position.getRectangle();
    Page page = position.getPage();
    drawRectangle(rect, page.getPageNumber(), strokingColor, nonStrokingColor,
        thickness);
  }

  @Override
  public void drawPosition(Position position, Color strokingColor, 
      Color nonStrokingColor, float thickness, boolean relativeToUpperLeft, 
      boolean originInUpperLeft) throws IOException {
    Rectangle rect = position.getRectangle();
    Page page = position.getPage();
    drawRectangle(rect, page.getPageNumber(), strokingColor, nonStrokingColor,
        thickness, relativeToUpperLeft, originInUpperLeft);
  }
  
  // ==========================================================================

  @Override
  public void drawText(String text, int pageNum) throws IOException {
    this.drawText(text, pageNum, false, false);
  }

  @Override
  public void drawText(String text, int pageNum, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException {
    this.drawText(text, pageNum, this.pointFactory.create(0, 0),
        relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawText(String text, int pageNum, Point point)
      throws IOException {
    this.drawText(text, pageNum, point, false, false);
  }

  @Override
  public void drawText(String text, int pageNum, Point point,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException {
    this.drawText(text, pageNum, point, DEFAULT_STROKING_COLOR,
        relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawText(String text, int pageNum, Point point, Color color)
      throws IOException {
    this.drawText(text, pageNum, point, color, false, false);
  }

  @Override
  public void drawText(String text, int pageNum, Point point, Color color,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException {
    this.drawText(text, pageNum, point, color, DEFAULT_FONT_SIZE,
        relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawText(String text, int pageNum, Point point, Color color,
      float fontsize) throws IOException {
    drawText(text, pageNum, point, color, fontsize, false, false);
  }

  @Override
  public void drawText(String text, int pageNum, Point point, Color color,
      float fontsize, boolean relativeToUpperLeft, boolean originInUpperLeft)
      throws IOException {
    PDPageContentStream stream = getPdPageContentStream(pageNum);

    Point adapted = adaptPoint(point, pageNum, relativeToUpperLeft,
        originInUpperLeft);

    stream.setNonStrokingColor(color);
    stream.beginText();
    stream.setFont(DEFAULT_FONT, fontsize);
    stream.newLineAtOffset(adapted.getX(), adapted.getY());
    stream.showText(text);
    stream.endText();
  }

  @Override
  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      // Close all the open PDPageContentStream objects. Start at 1 because of
      // the dummy at the start.
      for (int i = 1; i < this.pageStreams.size(); i++) {
        try {
          this.pageStreams.get(i).close();
        } catch (IOException e) {
          continue;
        }
      }
      // Try to save the pdf document to the given file.
      this.pdDocument.save(baos);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("Error on visualization: " + e.getMessage());
    } finally {
      try {
        // Try to close the pdf document.
        this.pdDocument.close();
      } catch (IOException e) {
        throw new IOException("Error on closing the pdf: " + e.getMessage());
      }
    }
    return baos.toByteArray();
  }

  /**
   * Returns the content stream for the given page.
   * 
   * @param pageNum
   *        The number of page to process.
   * @return The content stream of the given page.
   */
  protected PDPageContentStream getPdPageContentStream(int pageNum) {
    if (pageNum < 1 || pageNum >= this.pageStreams.size()) {
      throw new IllegalArgumentException("The given page number is invalid.");
    }
    return this.pageStreams.get(pageNum);
  }

  /**
   * Returns the PDDocument.
   * 
   * @return An instance of PDDocument.
   */
  public PDDocument getPdDocument() {
    return this.pdDocument;
  }

  /**
   * Adapts the given point dependent on whether the coordinates are relative to
   * upper left and whether the origin is in upper left.
   * 
   * @param point
   *        The point to adapt.
   * @param pageNum
   *        The number of the page in which the point is located.
   * @param isRelativeToUpperLeft
   *        A flag to indicate whether the coordinates of the point are relative
   *        to the upper left.
   * @param isOriginInUpperLeft
   *        A flag to indicate whether the origin of the page is in the upper
   *        left.
   * @return a new instance of Point with adapted coordinates.
   */
  public Point adaptPoint(Point point, int pageNum,
      boolean isRelativeToUpperLeft, boolean isOriginInUpperLeft) {
    Point copy = this.pointFactory.create(point.getX(), point.getY());
    if (isRelativeToUpperLeft) {
      Rectangle pageBoundingBox = this.pageBoundingBoxes.get(pageNum);
      copy.setY(pageBoundingBox.getMaxY() - point.getY());
    }
    return copy;
  }

  /**
   * Adapts the given line dependent on whether the coordinates are relative to
   * upper left and whether the origin is in upper left.
   * 
   * @param l
   *        The line to adapt.
   * @param pageNum
   *        The number of the page in which the line is located.
   * @param isRelativeToUpperLeft
   *        A flag to indicate whether the coordinates of the line are relative
   *        to the upper left.
   * @param isOriginInUpperLeft
   *        A flag to indicate whether the origin of the page is in the upper
   *        left.
   * @return A new instance of Line with adapted coordinates.
   */
  public Line adaptLine(Line l, int pageNum, boolean isRelativeToUpperLeft,
      boolean isOriginInUpperLeft) {
    Line copy = this.lineFactory.create(l.getStartPoint(), l.getEndPoint());
    if (isRelativeToUpperLeft) {
      Rectangle pageBoundingBox = this.pageBoundingBoxes.get(pageNum);
      copy.setStartY(pageBoundingBox.getMaxY() - l.getStartY());
      copy.setEndY(pageBoundingBox.getMaxY() - l.getEndY());
    }
    return copy;
  }

  /**
   * Adapts the given line dependent on whether the coordinates are relative to
   * upper left and whether the origin is in upper left.
   * 
   * @param rect
   *        The rectangle to adapt.
   * @param pageNum
   *        The number of the page in which the rectangle is located.
   * @param isRelativeToUpperLeft
   *        A flag to indicate whether the coordinates of the rectangle are
   *        relative to the upper left.
   * @param isOriginInUpperLeft
   *        A flag to indicate whether the origin of the page is in the upper
   *        left.
   * @return A new instance of Rectangle with adapted coordinates.
   */
  public Rectangle adaptRectangle(Rectangle rect, int pageNum,
      boolean isRelativeToUpperLeft, boolean isOriginInUpperLeft) {
    Rectangle copy = this.rectangleFactory.create(rect);
    if (isRelativeToUpperLeft) {
      Rectangle pageBoundingBox = this.pageBoundingBoxes.get(pageNum);
      copy.setMinY(pageBoundingBox.getMaxY() - rect.getMinY());
      copy.setMaxY(pageBoundingBox.getMaxY() - rect.getMaxY());
    }
    if (!isOriginInUpperLeft) {
      copy.setMaxY(copy.getMinY() + rect.getHeight());
    }
    return copy;
  }
}
