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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import de.freiburg.iif.model.Line;
import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimplePoint;
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
  protected List<PDPageContentStream> streams = new ArrayList<>();

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
    this.streams.add(null); // Add dummy, because pageNumbers are 1-based.
    // Preallocate the list of streams.
    for (PDPage page : pages) {
      streams.add(new PDPageContentStream(pdDocument, page,
          PDPageContentStream.AppendMode.APPEND, true));
    }
  }

  // ___________________________________________________________________________

  @Override
  public void drawLine(Line line, int pageNum) throws IOException {
    this.drawLine(line, pageNum, DEFAULT_COLOR);
  }

  @Override
  public void drawLine(Line line, int pageNum, Color color)
    throws IOException {
    this.drawLine(line, pageNum, color, DEFAULT_LINE_THICKNESS);
  }

  @Override
  public void drawLine(Line line, int pageNum, Color color, float thickness)
    throws IOException {
    PDPageContentStream stream = getPdPageContentStream(pageNum);

    stream.setStrokingColor(color);
    stream.setLineWidth(thickness);
    stream.moveTo(line.getStartX(), line.getStartY());
    stream.lineTo(line.getEndX(), line.getEndY());
    stream.stroke();
  }

  // ___________________________________________________________________________

  @Override
  public void drawRectangle(Rectangle rect, int pageNum) throws IOException {
    this.drawRectangle(rect, pageNum, DEFAULT_COLOR);
  }

  @Override
  public void drawRectangle(Rectangle rect, int pageNum, Color color)
    throws IOException {
    this.drawRectangle(rect, pageNum, color, DEFAULT_LINE_THICKNESS);
  }

  @Override
  public void drawRectangle(Rectangle rect,
      int pageNum,
      Color color,
      float thickness) throws IOException {
    PDPageContentStream stream = getPdPageContentStream(pageNum);

    stream.setStrokingColor(color);
    stream.setNonStrokingColor(color);
    stream.setLineWidth(thickness);
    stream.moveTo(rect.getMinX(), rect.getMinY());
    stream.lineTo(rect.getMaxX(), rect.getMinY());
    stream.lineTo(rect.getMaxX(), rect.getMaxY());
    stream.lineTo(rect.getMinX(), rect.getMaxY());
    stream.lineTo(rect.getMinX(), rect.getMinY());
    stream.stroke();
  }

  // ___________________________________________________________________________

  @Override
  public void drawText(String text, int pageNum) throws IOException {
    this.drawText(text, pageNum, new SimplePoint(0, 0));
  }

  @Override
  public void drawText(String text, int pageNum, Point point)
    throws IOException {
    this.drawText(text, pageNum, point, DEFAULT_COLOR);
  }

  @Override
  public void drawText(String text, int pageNum, Point point, Color color)
    throws IOException {
    this.drawText(text, pageNum, point, color, DEFAULT_FONT_SIZE);
  }

  @Override
  public void drawText(String text,
      int pageNum,
      Point point,
      Color color,
      float fontsize) throws IOException {
    PDPageContentStream stream = getPdPageContentStream(pageNum);

    stream.setNonStrokingColor(color);
    stream.beginText();
    stream.setFont(DEFAULT_FONT, fontsize);
    stream.newLineAtOffset(point.getX(), point.getY());
    stream.showText(text);
    stream.endText();
  }

  @Override
  public void writeTo(OutputStream os) throws IOException {
    try {
      // Close all the open PDPageContentStream objects. Start at 1 because of
      // the dummy at the start.
      for (int i = 1; i < streams.size(); i++) {
        try {
          streams.get(i).close();
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
    if (pageNum < 1 || pageNum >= streams.size()) {
      throw new IllegalArgumentException("The given page number is invalid.");
    }
    return this.streams.get(pageNum);
  }

  /**
   * Returns the pd document.
   */
  public PDDocument getPdDocument() {
    return pdDocument;
  }
}
