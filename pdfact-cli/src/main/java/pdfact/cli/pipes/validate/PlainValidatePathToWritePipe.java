package pdfact.cli.pipes.validate;

import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.exception.PdfActValidateException;

/**
 * A plain implementation of {@link ValidatePathToWritePipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainValidatePathToWritePipe implements ValidatePathToWritePipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PlainValidatePathToWritePipe.class);

  /**
   * The path to validate.
   */
  protected Path path;

  // ==============================================================================================

  @Override
  public Document execute(Document pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Validating the path.");
    validatePath(pdf);

    log.debug("Validating the path done.");
    log.debug("Validated path: " + this.path);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  // ==============================================================================================

  /**
   * Validates the given path.
   * 
   * @param pdf
   *        The PDF document to process.
   * @throws PdfActValidateException
   *         If the given path is not valid.
   */
  protected void validatePath(Document pdf) throws PdfActValidateException {
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

  // ==============================================================================================

  @Override
  public void setPath(Path path) {
    this.path = path;
  }

  @Override
  public Path getPath() {
    return this.path;
  }
}
