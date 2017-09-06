package pdfact.core.util.pipeline;

import pdfact.core.model.PdfDocument;
import pdfact.core.util.exception.PdfActException;

/**
 * An element (task) in a pipeline (= chain of tasks to be executed on starting
 * the pipeline).
 * 
 * @author Claudius Korzen
 */
public interface Pipe {
  /**
   * Executes this pipe.
   * 
   * @param pdf
   *        The input PDF document for this pipe.
   * 
   * @return The state of the PDF document after executing this pipe.
   * 
   * @throws PdfActException
   *         If something went wrong while executing this pipe.
   */
  PdfDocument execute(PdfDocument pdf) throws PdfActException;
}
