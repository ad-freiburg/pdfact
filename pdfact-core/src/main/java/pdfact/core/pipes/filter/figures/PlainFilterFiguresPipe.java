package pdfact.core.pipes.filter.figures;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pdfact.core.model.Figure;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.ElementList;

/**
 * A plain implementation of {@link FilterFiguresPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainFilterFiguresPipe implements FilterFiguresPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PlainFilterFiguresPipe.class);

  /**
   * The number of processed figures.
   */
  protected int numProcessedFigures;

  /**
   * The number of filtered figures.
   */
  protected int numFilteredFigures;

  // ==============================================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Filtering figures.");
    filterFigures(pdf);

    log.debug("Filtering figures done.");
    log.debug("# processed figures: " + this.numProcessedFigures);
    log.debug("# filtered figures : " + this.numFilteredFigures);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  // ==============================================================================================

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
        ElementList<Figure> before = page.getFigures();
        // Create a new list of figures which should not be filtered.
        ElementList<Figure> after = new ElementList<>(before.size());
        for (Figure figure : before) {
          this.numProcessedFigures++;

          if (isFilterFigure(figure)) {
            this.numFilteredFigures++;
            continue;
          }

          after.add(figure);
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