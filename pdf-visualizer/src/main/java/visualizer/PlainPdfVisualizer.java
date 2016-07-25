package visualizer;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import de.freiburg.iif.model.HasRectangle;
import drawer.PdfDrawer;
import drawer.pdfbox.PdfBoxDrawer;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfPage;
import model.PdfTextLine;

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

    for (PdfTextLine line : page.getTextLines()) {
//      if (line.getMeanLine() != null) {
//        drawer.drawLine(line.getMeanLine(), line.getPage().getPageNumber(), Color.BLUE);
//      }
//      if (line.getBaseLine() != null) {
//        drawer.drawLine(line.getBaseLine(), line.getPage().getPageNumber(), Color.RED);
//      }
      drawer.drawLine(line.getColumnXRange(), line.getPage().getPageNumber());
    }
    
    //    if (page.getRects() != null) {
    //      for (Rectangle rect : page.getRects()) {
    //        drawer.drawRectangle(rect, page.getPageNumber(), Color.BLACK);
    //      }
    //    }

//        for (PdfArea block : page.getBlocks()) {
//          drawer.drawRectangle(block.getRectangle(), page.getPageNumber(),
//              Color.BLACK);     
//        }   

//    drawer.drawRectangle(new SimpleRectangle(285.49875f,367.94885f,306.49875f,398.34326f), 7, Color.MAGENTA);
//    drawer.drawRectangle(new SimpleRectangle(172.908f,667.54803f,444.46802f,668.54803f), 18);
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
