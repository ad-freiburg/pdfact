package icecite.visualize;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfFeature;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.utils.collection.CollectionUtils;

/**
 * A plain implementation of PdfVisualizer.
 *
 * @author Claudius Korzen
 */
public class PlainPdfVisualizer implements PdfVisualizer {
  /**
   * The factory to create instance of {@link PdfDrawer}.
   */
  protected PdfDrawerFactory pdfDrawerFactory;

  /**
   * The random generator needed to create random colors.
   */
  protected Random r;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF visualizer.
   * 
   * @param pdfDrawerFactory
   *        The factory to create instances of {@link PdfDrawerFactory}.
   */
  @AssistedInject
  public PlainPdfVisualizer(PdfDrawerFactory pdfDrawerFactory) {
    this.pdfDrawerFactory = pdfDrawerFactory;
    this.r = new Random();
  }

  // ==========================================================================

  @Override
  public void visualize(PdfDocument pdf, OutputStream stream)
      throws IOException {
    visualize(pdf, stream, null, null);
  }

  @Override
  public void visualize(PdfDocument pdf, OutputStream stream,
      Set<PdfFeature> features, Set<PdfRole> roles) throws IOException {
    PdfDrawer drawer = this.pdfDrawerFactory.create(pdf.getFile());

    for (PdfPage page : pdf.getPages()) {
      visualizePage(page, drawer, features, roles);
    }

    drawer.writeTo(stream);
  }

  // ==========================================================================

  /**
   * Visualizes the given features of the given document using the given
   * drawer.
   * 
   * @param page
   *        The page to process.
   * @param drawer
   *        The drawer to use.
   * @param features
   *        The features to visualize.
   * @param roles
   *        The roles to consider on visualization.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePage(PdfPage page, PdfDrawer drawer,
      Set<PdfFeature> features, Set<PdfRole> roles) throws IOException {
    if (page == null) {
      return;
    }
    
    // Visualize the text elements.
    for (PdfTextBlock block : page.getTextBlocks()) {
       if (isVisualizePdfElement(block, features, roles)) {
        visualizePdfElement(page, block, drawer, Color.RED);
      }
      for (PdfTextLine line : block.getTextLines()) {
        if (isVisualizePdfElement(line, features, roles)) {
          visualizePdfElement(page, line, drawer, Color.BLUE);
        }
        for (PdfWord word : line.getWords()) {
          if (isVisualizePdfElement(word, features, roles)) {
            visualizePdfElement(page, word, drawer, Color.ORANGE);
          }
          for (PdfCharacter character : word.getCharacters()) {
            if (isVisualizePdfElement(character, features, roles)) {
              visualizePdfElement(page, character, drawer, Color.BLUE);
            }
          }
        }
      }
    }

    // Visualize the graphical elements.
    for (PdfFigure figure : page.getFigures()) {
      if (isVisualizePdfElement(figure, features, roles)) {
        visualizePdfElement(page, figure, drawer, Color.CYAN);
      }
    }
    for (PdfShape shape : page.getShapes()) {
      if (isVisualizePdfElement(shape, features, roles)) {
        visualizePdfElement(page, shape, drawer, Color.MAGENTA);
      }
    }
  }

  /**
   * Visualizes the given list of rectangles using the given drawer.
   * 
   * @param page
   *        The page in which the element is located.
   * @param element
   *        The element to visualize.
   * @param drawer
   *        The drawer to use.
   * @param strokingColor
   *        The color to use.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePdfElement(PdfPage page, PdfElement element,
      PdfDrawer drawer, Color strokingColor) throws IOException {
    visualizePdfElement(page, element, drawer, strokingColor, null);
  }

  /**
   * Visualizes the given list of rectangles using the given drawer.
   * 
   * @param page
   *        The page in which the element is located.
   * @param element
   *        The element to visualize.
   * @param drawer
   *        The drawer to use.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePdfElement(PdfPage page, PdfElement element,
      PdfDrawer drawer, Color strokingColor, Color nonStrokingColor)
      throws IOException {
    if (element != null) {
      drawer.drawBoundingBox(element, page.getPageNumber(), strokingColor,
          nonStrokingColor);
    }
  }

  // ==========================================================================

  /**
   * Checks if the given PDF element should be visualized or not, dependent on
   * the type and the role of the element.
   * 
   * @param element
   *        The PDF element to check.
   * @param features
   *        The features to serialize.
   * @param roles
   *        The roles to consider on visualization.
   * @return True, if the given PDF element should be visalized, false
   *         otherwise.
   */
  protected boolean isVisualizePdfElement(PdfElement element,
      Set<PdfFeature> features, Set<PdfRole> roles) {
    // Check if the type of the given element was registered to be serialized.
    boolean isFeatureGiven = !CollectionUtils.isNullOrEmpty(features);
    if (isFeatureGiven && !features.contains(element.getFeature())) {
      return false;
    }

    // Check if the role of the given element was registered to be serialized.
    // TODO
//    boolean isRoleGiven = !CollectionUtils.isNullOrEmpty(roles);
//    if (isRoleGiven && !roles.contains(element.getRole())) {
//      return false;
//    }
    return true;
  }
}
