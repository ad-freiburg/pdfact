package pdfact.pipes.validate;

import java.nio.file.Files;
import java.nio.file.Path;

import pdfact.model.PdfDocument;
import pdfact.util.exception.PdfActException;
import pdfact.util.exception.PdfActValidateException;

/**
 * A plain implementation of {@link ValidatePdfPathPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainValidatePdfPathPipe implements ValidatePdfPathPipe {
  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    if (pdf == null) {
      String message = "No PDF document given.";
      throw new PdfActValidateException(message);
    }

    // Validate the path to the PDF file.
    validatePdfPath(pdf.getPath());

    return pdf;
  }

  /**
   * Validates the given path to a PDF file.
   * 
   * @param pdfPath
   *        The path to validate.
   * 
   * @throws PdfActException
   *         If the path to the PDF file is not valid.
   */
  protected void validatePdfPath(Path pdfPath) throws PdfActException {
    // Check if a path is given.
    if (pdfPath == null) {
      String message = "No path to a PDF file given.";
      throw new PdfActValidateException(message);
    }

    // Make sure that the file exists.
    if (!Files.exists(pdfPath)) {
      String message = String.format("The file '%s' doesn't exist.", pdfPath);
      throw new PdfActValidateException(message);
    }

    // Make sure that the path points to a file (and not to a directory).
    if (!Files.isRegularFile(pdfPath)) {
      String message = String.format("'%s' is not a regular file.", pdfPath);
      throw new PdfActValidateException(message);
    }

    // Make sure that the file is readable.
    if (!Files.isReadable(pdfPath)) {
      String message = String.format("The file '%s' can't be read.", pdfPath);
      throw new PdfActValidateException(message);
    }
  }
}
