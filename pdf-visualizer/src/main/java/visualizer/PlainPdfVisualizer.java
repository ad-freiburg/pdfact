package visualizer;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import drawer.PdfDrawer;
import drawer.pdfbox.PdfBoxDrawer;
import model.PdfArea;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfPage;
import model.PdfTextLine;
import model.PdfWord;

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
      OutputStream stream) throws IOException {
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
      PdfDrawer drawer) throws IOException {

    if (features == null) {
      return;
    }

    for (PdfFeature feature : features) {
      visualizeFeature(page, feature, drawer);
    }

//    if (page.getRects() != null) {
//      for (Rectangle rect : page.getRects()) {
//        drawer.drawRectangle(rect, page.getPageNumber(), Color.BLACK);
//      }
//    }
    
    for (PdfArea block : page.getBlocks()) {
//      drawer.drawRectangle(block.getRectangle(), page.getPageNumber(),
//          Color.BLACK);
      if (block.getRects() != null) {
        for (Rectangle rect : block.getRects()) {
          drawer.drawRectangle(rect, page.getPageNumber(), Color.BLUE);
        }
      }
//      for (PdfWord word : block.getWords()) {
//        drawer.drawRectangle(word.getRectangle(), page.getPageNumber(),
//            Color.BLUE);
//      } 
      Random r = new Random();
      for (PdfTextLine line : block.getTextLines()) {
        Color color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        for (PdfCharacter character : line.getTextCharacters()) {
          drawer.drawRectangle(character.getRectangle(), page.getPageNumber(), color);
        }
        drawer.drawRectangle(line.getRectangle(), page.getPageNumber(),
            Color.BLUE);
      } 
      
    }
    
//    drawer.drawRectangle(new SimpleRectangle(316.81195f,541.1039f,556.87244f,550.3539f), page.getPageNumber());
//    drawer.drawRectangle(new SimpleRectangle(316.81195f,541.1039f,556.87244f,550.3539f), page.getPageNumber());
    drawer.drawRectangle(new SimpleRectangle(320.39597f,548.1713f,555.92334f,549.4213f), page.getPageNumber());
//    if (page.getRects() != null) {
//      for (Rectangle rect : page.getRects()) {
//        drawer.drawRectangle(rect.getRectangle(), page.getPageNumber(),
//            Color.BLACK);
//      }
//    }
    
  }

  // ___________________________________________________________________________

  /**
   * Visualizes the paragraphs of the given document using the given drawer.
   */
  protected void visualizeFeature(PdfPage page, PdfFeature feature,
      PdfDrawer drawer) throws IOException {
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
      Color color, PdfDrawer drawer) throws IOException {
    visualizeElement(element, element.getPage(), color, drawer);
  }

  /**
   * Visualizes the given list of rectangles using the given drawer.
   */
  protected void visualizeElement(HasRectangle element, PdfPage page,
      Color color, PdfDrawer drawer) throws IOException {
    if (element == null) {
      return;
    }

    if (page != null) {
      int pageNumber = page.getPageNumber();
      drawer.drawRectangle(element.getRectangle(), pageNumber, color);
    }
  }
}
