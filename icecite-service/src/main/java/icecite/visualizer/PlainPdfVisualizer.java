package icecite.visualizer;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import icecite.drawer.PdfDrawer;
import icecite.drawer.PdfDrawerFactory;
import icecite.models.PdfCharacter;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfShape;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;

// TODO: Implement roles.

/**
 * The default implementation of a PdfVisualizer.
 *
 * @author Claudius Korzen
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
    Set<Class<? extends PdfElement>> types = new HashSet<>();
    
//    types.add(PdfParagraph.class);
    types.add(PdfTextLine.class);
//    types.add(PdfWord.class);
//    types.add(PdfCharacter.class);
//    types.add(PdfFigure.class);
//    types.add(PdfShape.class);
    
    visualize(pdf, types, stream);
  }

  /**
   * Visualizes the given PDF elements of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param types
   *        The types of PDF elements to visualize.
   * @param stream
   *        The stream to write to.
   * @throws IOException
   *         if something went wrong while visualizing.
   */
  public void visualize(PdfDocument pdf, Set<Class<? extends PdfElement>> types,
      OutputStream stream) throws IOException {
    PdfDrawer drawer = this.pdfDrawerFactory.create(pdf.getFile());

    for (PdfPage page : pdf.getPages()) {
      visualizePage(page, types, drawer);
    }

    drawer.writeTo(stream);
  }

  // ___________________________________________________________________________

  /**
   * Visualizes the given features of the given document using the given
   * drawer.
   * 
   * @param page
   *        The page to process.
   * @param types
   *        The types of the PDF elements to visualize.
   * @param drawer
   *        The drawer to use.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePage(PdfPage page,
      Set<Class<? extends PdfElement>> types, PdfDrawer drawer)
      throws IOException {
    if (page == null) {
      return;
    }

    if (types == null) {
      return;
    }
    
    // Visualize the text elements.
    for (PdfParagraph paragraph : page.getParagraphs()) {
      if (types.isEmpty() || types.contains(PdfParagraph.class)) {
        visualizePdfElement(paragraph, drawer, Color.GREEN);
      }
      for (PdfTextLine line : paragraph.getTextLines()) {
        if (types.isEmpty() || types.contains(PdfTextLine.class)) {
          visualizePdfElement(line, drawer, Color.BLUE);
        }
        for (PdfWord word : line.getWords()) {
          if (types.isEmpty() || types.contains(PdfWord.class)) {
            visualizePdfElement(word, drawer, Color.RED);
          }
          for (PdfCharacter character : word.getCharacters()) {
            if (types.isEmpty() || types.contains(PdfCharacter.class)) {
              visualizePdfElement(character, drawer, Color.GRAY);
            }
          }
        }
      }
    }

    // Visualize the graphical elements.
    if (types.isEmpty() || types.contains(PdfFigure.class)) {
      for (PdfFigure figure : page.getFigures()) {
        visualizePdfElement(figure, drawer, Color.CYAN);
      }
    }
    if (types.isEmpty() || types.contains(PdfShape.class)) {
      for (PdfShape shape : page.getShapes()) {
        visualizePdfElement(shape, drawer, Color.MAGENTA);
      }
    }
  }

  /**
   * Visualizes the given list of rectangles using the given drawer.
   * 
   * @param element
   *        The element to visualize.
   * @param drawer
   *        The drawer to use.
   * @param color
   *        The color to use.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePdfElement(PdfElement element, PdfDrawer drawer,
      Color color) throws IOException {
    if (element != null) {
      PdfPage page = element.getPage();
      drawer.drawBoundingBox(element, page.getPageNumber(), color);
    }
  }
}
