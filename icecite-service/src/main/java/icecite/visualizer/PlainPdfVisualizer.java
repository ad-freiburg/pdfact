package icecite.visualizer;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import com.google.inject.Inject;

import icecite.drawer.PdfDrawer;
import icecite.drawer.PdfDrawerFactory;
import icecite.models.PdfCharacter;
import icecite.models.PdfDocument;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfShape;

/**
 * The default implementation of a PdfVisualizer.
 *
 * @author Claudius Korzen
 *
 */
public class PlainPdfVisualizer implements PdfVisualizer {
  /**
   * The factory to create instance of {@link PdfDrawer}.
   */
  protected PdfDrawerFactory pdfDrawerFactory;

  /**
   * Creates a new PDF visualizer.
   * 
   * @param pdfDrawerFactory
   *        The factory to create instances of {@link PdfDrawerFactory}.
   */
  @Inject
  public PlainPdfVisualizer(PdfDrawerFactory pdfDrawerFactory) {
    this.pdfDrawerFactory = pdfDrawerFactory;
  }

  @Override
  public void visualize(PdfDocument pdf, OutputStream stream)
      throws IOException {
    PdfDrawer drawer = this.pdfDrawerFactory.create(pdf.getFile());

    for (PdfPage page : pdf.getPages()) {
      visualizePage(page, drawer);
    }

    drawer.writeTo(stream);
  }

  // public void visualize(PdfDocument document, List<Class<PdfElement>>
  // features,
  // OutputStream stream)
  // throws IOException {
  // PdfDrawer drawer = new PdfBoxDrawer(document.getPdfFile());
  //
  // // If the list of features is empty, take all features.
  // if (features == null || features.isEmpty()) {
  // features = Arrays.asList(PdfFeature.values());
  // }
  //
  // for (PdfPage page : document.getPages()) {
  // visualizePage(page, features, drawer);
  // }
  //
  // drawer.writeTo(stream);
  // }

  // ___________________________________________________________________________

  /**
   * Visualizes the given features of the given document using the given
   * drawer.
   * 
   * @param page
   *        The page to process.
   * @param drawer
   *        The drawer to use.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePage(PdfPage page, PdfDrawer drawer)
      throws IOException {

    // for (PdfFeature feature : features) {
    // visualizeFeature(page, feature, drawer);
    // }
    visualizeCharacters(page, drawer);
  }

  // ___________________________________________________________________________

  // /**
  // * Visualizes the paragraphs of the given document using the given drawer.
  // */
  // protected void visualizeFeature(PdfPage page, PdfFeature feature,
  // PdfDrawer drawer)
  // throws IOException {
  // if (feature == null) {
  // return;
  // }
  //
  // if (page == null) {
  // return;
  // }
  //
  // for (PdfElement element : page.getElementsByFeature(feature)) {
  // visualizeElement(element, feature.getColor(), drawer);
  // }
  // }

  // ___________________________________________________________________________

  // /**
  // * Visualizes the given list of rectangles using the given drawer.
  // */
  // protected void visualizeElement(PdfElement element,
  // Color color, PdfDrawer drawer)
  // throws IOException {
  // visualizeElement(element, element.getPage(), color, drawer);
  // }
  //
  // /**
  // * Visualizes the given list of rectangles using the given drawer.
  // */
  // protected void visualizeElement(HasBoundingBox element, PdfPage page,
  // Color color, PdfDrawer drawer)
  // throws IOException {
  // if (element == null) {
  // return;
  // }
  //
  // if (page != null) {
  // int pageNumber = page.getPageNumber();
  // drawer.drawRectangle(element.getBoundingBox(), pageNumber, color);
  // }
  // }

  /**
   * Visualizes the given list of rectangles using the given drawer.
   * 
   * @param page
   *        The page to process.
   * @param drawer
   *        The drawer to use.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizeCharacters(PdfPage page, PdfDrawer drawer)
      throws IOException {
    if (page != null) {
      int pageNumber = page.getPageNumber();
      for (PdfCharacter character : page.getCharacters()) {
        drawer.drawBoundingBox(character, pageNumber, Color.BLUE);
      }
      for (PdfFigure figure : page.getFigures()) {
        drawer.drawBoundingBox(figure, pageNumber, Color.RED);
      }
      for (PdfShape shape : page.getShapes()) {
        drawer.drawBoundingBox(shape, pageNumber, Color.GREEN);
      }
    }
  }
}
