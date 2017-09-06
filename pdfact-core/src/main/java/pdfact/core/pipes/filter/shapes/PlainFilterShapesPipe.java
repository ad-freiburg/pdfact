package pdfact.core.pipes.filter.shapes;

import java.util.ArrayList;
import java.util.List;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Shape;
import pdfact.core.util.exception.PdfActException;

/**
 * A plain implementation of {@link FilterShapesPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainFilterShapesPipe implements FilterShapesPipe {
  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    filterShapes(pdf);
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
        List<Shape> before = page.getShapes();
        // Create a new list of shapes which should not be filtered.
        List<Shape> after = new ArrayList<>(before.size());
        for (Shape shape : before) {
          if (!isFilterShape(shape)) {
            after.add(shape);
          }
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