package pdfact.pipes.filter.figures;

import java.util.ArrayList;
import java.util.List;

import pdfact.model.Figure;
import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.util.exception.PdfActException;

/**
 * A plain implementation of {@link FilterFiguresPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainFilterFiguresPipe implements FilterFiguresPipe {
  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    filterFigures(pdf);
    return pdf;
  }

  // ==========================================================================

  /**
   * Filters those figures of a PDF document that should not be considered.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void filterFigures(PdfDocument pdf) {
    if (pdf != null) {
      List<Page> pages = pdf.getPages();
      for (Page page : pages) {
        List<Figure> before = page.getFigures();
        // Create a new list of figures which should not be filtered.
        List<Figure> after = new ArrayList<>(before.size());
        for (Figure figure : before) {
          if (!isFilterFigure(figure)) {
            after.add(figure);
          }
        }
        page.setFigures(after);
      }
    }
  }

  /**
   * Checks if the given figure should be filtered out.
   * 
   * @param figure
   *        The figure to check.
   * 
   * @return True if the given figure should be filtered out; False otherwise.
   */
  public static boolean isFilterFigure(Figure figure) {
    return false;
  }
}