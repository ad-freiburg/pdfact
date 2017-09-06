package pdfact.core.pipes;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that wires up all necessary steps to identify the document structure
 * of PDF documents.
 * 
 * @author Claudius Korzen
 */
public interface PdfActCorePipe extends Pipe {
  /**
   * The factory to create instances of {@link PdfActCorePipe}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfActCorePipeFactory {
    /**
     * Creates a new PdfActCorePipe.
     * 
     * @return An instance of {@link PdfActCorePipe}.
     */
    PdfActCorePipe create();
  }
}
