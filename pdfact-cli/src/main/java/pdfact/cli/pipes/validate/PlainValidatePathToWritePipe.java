package pdfact.cli.pipes.validate;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;

import pdfact.core.model.PdfDocument;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.exception.PdfActValidateException;
import pdfact.core.util.log.InjectLogger;

/**
 * A plain implementation of {@link ValidatePathToWritePipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainValidatePathToWritePipe implements ValidatePathToWritePipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The path to validate.
   */
  protected Path path;

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Validating the path.");
    validatePath(pdf);

    log.debug("Validating the path done.");
    log.debug("Validated path: " + this.path);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  // ==========================================================================

  /**
   * Validates the given path.
   * 
   * @param pdf
   *        The PDF document to process.
   * @throws PdfActValidateException
   *         If the given path is not valid.
   */
  protected void validatePath(PdfDocument pdf) throws PdfActValidateException {
    // Check if a path is given.
    if (this.path == null) {
      throw new PdfActValidateException("No file given.");
    }

    // Check if the visualization file already exists.
    if (Files.exists(this.path)) {
      // Make sure that the existing visualization file is a regular file.
      if (!Files.isRegularFile(this.path)) {
        throw new PdfActValidateException("The file '" + this.path
            + "' already exists, but is no regular file.");
      }

      // Make sure that the existing visualization file is writable.
      if (!Files.isWritable(this.path)) {
        throw new PdfActValidateException("The file '" + this.path
            + "' already exists, but isn't writable.");
      }
    }
  }

  // ==========================================================================

  @Override
  public void setPath(Path path) {
    this.path = path;
  }

  @Override
  public Path getPath() {
    return this.path;
  }
}
