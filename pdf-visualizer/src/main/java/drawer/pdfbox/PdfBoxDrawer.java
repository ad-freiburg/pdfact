package drawer.pdfbox;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import de.freiburg.iif.model.Line;
import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleLine;
import de.freiburg.iif.model.simple.SimplePoint;
import de.freiburg.iif.model.simple.SimpleRectangle;
import drawer.PdfDrawer;

/**
 * The implementation of PdfVisualizer using PdfBox.
 * 
 * @author Claudius Korzen.
 */
public class PdfBoxDrawer implements PdfDrawer {
  /**
   * The default color.
   */
  protected static final Color DEFAULT_COLOR = Color.BLACK;

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
  protected static final float DEFAULT_LINE_THICKNESS = 1;

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
   */
  public PdfBoxDrawer(Path pdfFile) throws IOException {
    this(pdfFile != null ? pdfFile.toFile() : null);
  }

  /**
   * Creates a new visualizer from the given file.
   */
  public PdfBoxDrawer(File pdfFile) throws IOException {
    this(pdfFile != null ? PDDocument.load(pdfFile) : null);
  }

  /**
   * Creates a new visualizer from the given PDDocument.
   */
  public PdfBoxDrawer(PDDocument pdDocument) throws IOException {
    this.pdDocument = pdDocument;

    PDPageTree pages = pdDocument.getDocumentCatalog().getPages();
    this.pageStreams.add(null); // Add dummy, because pageNumbers are 1-based.
    this.pageBoundingBoxes.add(null);
    // Preallocate the list of streams.
    for (PDPage page : pages) {
      pageStreams.add(new PDPageContentStream(pdDocument, page,
          PDPageContentStream.AppendMode.APPEND, true));
      Rectangle boundingBox = new SimpleRectangle();

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

      pageBoundingBoxes.add(boundingBox);
    }
  }

  // ___________________________________________________________________________

  @Override
  public void drawLine(Line line, int pageNum) throws IOException {
    drawLine(line, pageNum, false, false);
  }

  @Override
  public void drawLine(Line line, int pageNum, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException {
    this.drawLine(line, pageNum, DEFAULT_COLOR, relativeToUpperLeft,
        originInUpperLeft);
  }

  @Override
  public void drawLine(Line line, int pageNum, Color color)
    throws IOException {
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

    Line adapted =
        adaptLine(line, pageNum, relativeToUpperLeft, originInUpperLeft);

    stream.setStrokingColor(color);
    stream.setLineWidth(thickness);
    stream.moveTo(adapted.getStartX(), adapted.getStartY());
    stream.lineTo(adapted.getEndX(), adapted.getEndY());
    stream.stroke();
  }

  // ___________________________________________________________________________

  @Override
  public void drawRectangle(Rectangle rect, int pageNum) throws IOException {
    this.drawRectangle(rect, pageNum, false, false);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
        throws IOException {
    this.drawRectangle(rect, pageNum, DEFAULT_COLOR, relativeToUpperLeft,
        originInUpperLeft);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color color)
    throws IOException {
    this.drawRectangle(rect, pageNum, color, false, false);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color color,
      boolean relativeToUpperLeft, boolean originInUpperLeft)
        throws IOException {
    this.drawRectangle(rect, pageNum, color, DEFAULT_LINE_THICKNESS,
        relativeToUpperLeft, originInUpperLeft);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color color,
      float thickness) throws IOException {
    drawRectangle(rect, pageNum, color, thickness, false, false);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color color,
      float thickness, boolean relativeToUpperLeft, boolean originInUpperLeft)
        throws IOException {
    PDPageContentStream stream = getPdPageContentStream(pageNum);

    if (rect == null) {
      return;
    }

    Rectangle adapted =
        adaptRectangle(rect, pageNum, relativeToUpperLeft, originInUpperLeft);

    stream.setStrokingColor(color);
    stream.setNonStrokingColor(color);
    stream.setLineWidth(thickness);
    stream.moveTo(adapted.getMinX(), adapted.getMinY());
    stream.lineTo(adapted.getMaxX(), adapted.getMinY());
    stream.lineTo(adapted.getMaxX(), adapted.getMaxY());
    stream.lineTo(adapted.getMinX(), adapted.getMaxY());
    stream.lineTo(adapted.getMinX(), adapted.getMinY());
    stream.stroke();
  }

  // ___________________________________________________________________________

  @Override
  public void drawText(String text, int pageNum) throws IOException {
    this.drawText(text, pageNum, false, false);
  }

  @Override
  public void drawText(String text, int pageNum, boolean relativeToUpperLeft,
      boolean originInUpperLeft) throws IOException {
    this.drawText(text, pageNum, new SimplePoint(0, 0), relativeToUpperLeft,
        originInUpperLeft);
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
    this.drawText(text, pageNum, point, DEFAULT_COLOR, relativeToUpperLeft,
        originInUpperLeft);
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

    Point adapted =
        adaptPoint(point, pageNum, relativeToUpperLeft, originInUpperLeft);

    stream.setNonStrokingColor(color);
    stream.beginText();
    stream.setFont(DEFAULT_FONT, fontsize);
    stream.newLineAtOffset(adapted.getX(), adapted.getY());
    stream.showText(text);
    stream.endText();
  }

  @Override
  public void writeTo(OutputStream os) throws IOException {
    try {
      // Close all the open PDPageContentStream objects. Start at 1 because of
      // the dummy at the start.
      for (int i = 1; i < pageStreams.size(); i++) {
        try {
          pageStreams.get(i).close();
        } catch (IOException e) {
          continue;
        }
      }
      // Try to save the pdf document to the given file.
      pdDocument.save(os);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("Error on saving visualization: " + e.getMessage());
    } finally {
      try {
        // Try to close the pdf document.
        this.pdDocument.close();
      } catch (IOException e) {
        throw new IOException("Error on closing the pdf: " + e.getMessage());
      }
    }
  }

  /**
   * Returns the content stream for the given page.
   */
  protected PDPageContentStream getPdPageContentStream(int pageNum) {
    if (pageNum < 1 || pageNum >= pageStreams.size()) {
      throw new IllegalArgumentException("The given page number is invalid.");
    }
    return this.pageStreams.get(pageNum);
  }

  /**
   * Returns the pd document.
   */
  public PDDocument getPdDocument() {
    return pdDocument;
  }

  /**
   * Adapts the given point dependent on whether the coordinates are relative to
   * upper left and whether the origin is in upper left.
   */
  public Point adaptPoint(Point point, int pageNum,
      boolean isRelativeToUpperLeft, boolean isOriginInUpperLeft) {
    Point copy = new SimplePoint(point.getX(), point.getY());
    if (isRelativeToUpperLeft) {
      Rectangle pageBoundingBox = pageBoundingBoxes.get(pageNum);
      copy.setY(pageBoundingBox.getMaxY() - point.getY());
    }
    return copy;
  }

  /**
   * Adapts the given line dependent on whether the coordinates are relative to
   * upper left and whether the origin is in upper left.
   */
  public Line adaptLine(Line line, int pageNum,
      boolean isRelativeToUpperLeft, boolean isOriginInUpperLeft) {
    Line copy = new SimpleLine(line.getStartPoint(), line.getEndPoint());
    if (isRelativeToUpperLeft) {
      Rectangle pageBoundingBox = pageBoundingBoxes.get(pageNum);
      copy.setStartY(pageBoundingBox.getMaxY() - line.getStartY());
      copy.setEndY(pageBoundingBox.getMaxY() - line.getEndY());
    }
    return copy;
  }

  /**
   * Adapts the given line dependent on whether the coordinates are relative to
   * upper left and whether the origin is in upper left.
   */
  public Rectangle adaptRectangle(Rectangle rect, int pageNum,
      boolean isRelativeToUpperLeft, boolean isOriginInUpperLeft) {
    Rectangle copy = new SimpleRectangle(rect);
    if (isRelativeToUpperLeft) {
      Rectangle pageBoundingBox = pageBoundingBoxes.get(pageNum);
      copy.setMinY(pageBoundingBox.getMaxY() - rect.getMinY());
      copy.setMaxY(pageBoundingBox.getMaxY() - rect.getMaxY());
    }
    if (!isOriginInUpperLeft) {
      copy.setMaxY(copy.getMinY() + rect.getHeight());
    }
    return copy;
  }
}
