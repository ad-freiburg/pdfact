package pdfact.cli.pipes.validate;

import java.nio.file.Files;
import java.nio.file.Path;

import pdfact.core.model.PdfDocument;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.exception.PdfActValidateException;

/**
 * A plain implementation of {@link ValidatePathToWritePipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainValidatePathToWritePipe implements ValidatePathToWritePipe {
  /**
   * The path to validate.
   */
  protected Path path;

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
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

    return pdf;
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
