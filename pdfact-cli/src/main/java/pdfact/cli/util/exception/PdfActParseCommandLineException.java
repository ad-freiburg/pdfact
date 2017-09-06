package pdfact.cli.util.exception;

import pdfact.core.util.exception.PdfActException;

/**
 * The standard exception to throw on any errors on parsing the command line
 * arguments.
 * 
 * @author Claudius Korzen
 */
public class PdfActParseCommandLineException extends PdfActException {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -852706872435413153L;

  /**
   * The default status code of this exception.
   */
  protected static final int STATUS_CODE = 50;

  /**
   * The default constructor.
   * 
   * @param message
   *        The message of this exception.
   */
  public PdfActParseCommandLineException(String message) {
    super(STATUS_CODE, message);
  }

  /**
   * The default constructor.
   * 
   * @param message
   *        The message of this exception.
   * @param cause
   *        The cause of this exception.
   */
  public PdfActParseCommandLineException(String message, Throwable cause) {
    super(STATUS_CODE, message, cause);
  }

  /**
   * The default constructor.
   * 
   * @param message
   *        The message of this exception.
   * @param statusCode
   *        The status code.
   * @param cause
   *        The cause of this exception.
   */
  public PdfActParseCommandLineException(String message, int statusCode,
      Throwable cause) {
    super(statusCode, message, cause);
  }
}
