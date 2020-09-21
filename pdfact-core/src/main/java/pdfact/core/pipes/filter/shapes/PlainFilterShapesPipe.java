package pdfact.core.pipes.filter.shapes;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Shape;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.list.ElementList.ElementListFactory;
import pdfact.core.util.log.InjectLogger;

/**
 * A plain implementation of {@link FilterShapesPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainFilterShapesPipe implements FilterShapesPipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The factory to create lists of shapes.
   */
  protected ElementListFactory<Shape> shapeListFactory;

  /**
   * The number of processed shapes.
   */
  protected int numProcessedShapes;

  /**
   * The number of filtered shapes.
   */
  protected int numFilteredShapes;

  /**
   * Creates a pipe that filters those shapes of a PDF document that should not
   * be considered.
   * 
   * @param shapeListFactory
   *        The factory to create lists of shapes.
   */
  @Inject
  public PlainFilterShapesPipe(ElementListFactory<Shape> shapeListFactory) {
    this.shapeListFactory = shapeListFactory;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Filtering shapes.");
    filterShapes(pdf);

    log.debug("Filtering shapes done.");
    log.debug("# processed shapes: " + this.numProcessedShapes);
    log.debug("# filtered shapes : " + this.numFilteredShapes);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");

    return pdf;
  }

  // ==========================================================================

  /**
   * Filters those shapes of a PDF document that should not be considered.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void filterShapes(PdfDocument pdf) {
    if (pdf != null) {
      List<Page> pages = pdf.getPages();
      for (Page page : pages) {
        ElementList<Shape> before = page.getShapes();
        // Create a new list of shapes which should not be filtered.
        ElementList<Shape> after = this.shapeListFactory.create(before.size());
        for (Shape shape : before) {
          this.numProcessedShapes++;

          if (isFilterShape(shape)) {
            this.numFilteredShapes++;
            continue;
          }

          after.add(shape);
        }
        page.setShapes(after);
      }
    }
  }

  /**
   * Checks if the given shape should be filtered out.
   * 
   * @param shape
   *        The shape to check.
   * 
   * @return True if the given shape should be filtered out; False otherwise.
   */
  public static boolean isFilterShape(Shape shape) {
    return false;
  }
}