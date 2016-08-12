package visualizer;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.simple.SimpleLine;
import de.freiburg.iif.model.simple.SimpleRectangle;
import drawer.PdfDrawer;
import drawer.pdfbox.PdfBoxDrawer;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfPage;
import model.PdfTextLine;
import model.PdfTextParagraph;

/**
 * The default implmentation of a PdfVisualizer.
 *
 * @author Claudius Korzen
 *
 */
public class PlainPdfVisualizer implements PdfVisualizer {

  @Override
  public void visualize(PdfDocument document, OutputStream stream)
    throws IOException {
    visualize(document, null, stream);
  }

  @Override
  public void visualize(PdfDocument document, List<PdfFeature> features,
      OutputStream stream)
    throws IOException {
    PdfDrawer drawer = new PdfBoxDrawer(document.getPdfFile());

    // If the list of features is empty, take all features.
    if (features == null || features.isEmpty()) {
      features = Arrays.asList(PdfFeature.values());
    }

    for (PdfPage page : document.getPages()) {
      visualizePage(page, features, drawer);
    }

    drawer.writeTo(stream);
  }

  // ___________________________________________________________________________

  /**
   * Visualizes the given features of the given document using the given drawer.
   */
  protected void visualizePage(PdfPage page, List<PdfFeature> features,
      PdfDrawer drawer)
    throws IOException {

    if (features == null) {
      return;
    }

    for (PdfFeature feature : features) {
      /**
       * This feature was added for David. He needs the paragraphs with the 
       * associated lines. Remove it if it not needed anymore.
       */
      if (feature == PdfFeature.paragraphs_with_lines) {
        visualizeFeature(page, PdfFeature.lines, drawer);
        visualizeFeature(page, PdfFeature.paragraphs, drawer);
        continue;
      }

      visualizeFeature(page, feature, drawer);
    }

    for (int i = 0; i < page.getTextLines().size(); i++) {
      PdfTextLine line = page.getTextLines().get(i);

//      if (line.getMeanLine() != null) {
//        drawer.drawLine(line.getMeanLine(), line.getPage().getPageNumber(), Color.BLUE);
//      }
//      if (line.getBaseLine() != null) {
//        drawer.drawLine(line.getBaseLine(), line.getPage().getPageNumber(), Color.RED);
//      }
      drawer.drawLine(line.getColumnXRange(), line.getPage().getPageNumber());
//      drawer.drawRectangle(line.getRectangle(), line.getPage().getPageNumber());
      drawer.drawText(" " + line.getRole() + " " + line.getAlignment(), line.getPage().getPageNumber(), line.getRectangle().getLowerRight(), Color.BLACK, 5);
    }
    
    //    if (page.getRects() != null) {
    //      for (Rectangle rect : page.getRects()) {
    //        drawer.drawRectangle(rect, page.getPageNumber(), Color.BLACK);
    //      }
    //    }

//        for (PdfTextParagraph block : page.getParagraphs()) {
//          drawer.drawText("" + block.getRole(), page.getPageNumber(), block.getRectangle().getLowerRight(),
//              Color.BLACK);     
//        }   

//    drawer.drawRectangle(new SimpleRectangle(99.719666f,231.2512f,483.85175f,261.44653f), page.getPageNumber(), Color.MAGENTA);
//    drawer.drawRectangle(new SimpleRectangle(100f,87f,150f,97f), page.getPageNumber(), Color.MAGENTA);
//    drawer.drawLine(new SimpleLine(54.000046f,621.81165f,299.3169f,621.9116f), 9);
  }

  // ___________________________________________________________________________

  /**
   * Visualizes the paragraphs of the given document using the given drawer.
   */
  protected void visualizeFeature(PdfPage page, PdfFeature feature,
      PdfDrawer drawer)
    throws IOException {
    if (feature == null) {
      return;
    }

    if (page == null) {
      return;
    }

    for (PdfElement element : page.getElementsByFeature(feature)) {
      visualizeElement(element, feature.getColor(), drawer);
    }
  }

  // ___________________________________________________________________________

  /**
   * Visualizes the given list of rectangles using the given drawer.
   */
  protected void visualizeElement(PdfElement element,
      Color color, PdfDrawer drawer)
    throws IOException {
    visualizeElement(element, element.getPage(), color, drawer);
  }

  /**
   * Visualizes the given list of rectangles using the given drawer.
   */
  protected void visualizeElement(HasRectangle element, PdfPage page,
      Color color, PdfDrawer drawer)
    throws IOException {
    if (element == null) {
      return;
    }

    if (page != null) {
      int pageNumber = page.getPageNumber();
      drawer.drawRectangle(element.getRectangle(), pageNumber, color);
    }
  }
}
