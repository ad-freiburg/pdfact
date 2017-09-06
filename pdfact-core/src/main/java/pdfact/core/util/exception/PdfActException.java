package pdfact.core.util.exception;

// TODO: Create more exceptions that are thrown in the individual pipes.

/**
 * The base exception for any exceptions to throw on errors while executing 
 * PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActException extends Exception {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -7396761485778372279L;

  /**
   * The code to return on exiting PdfAct.
   */
  protected int exitCode;

  /**
   * Creates a new exception with the given exit code and message.
   * 
   * @param exitCode
   *        The code to return on exiting PdfAct.
   * @param message
   *        The error message to show when the exception was caught.
   */
  public PdfActException(int exitCode, String message) {
    this(exitCode, message, null);
  }

  /**
   * Creates a new exception with the given exit code and message.
   * 
   * @param exitCode
   *        The code to return on exiting PdfAct.
   * @param message
   *        The error message to show when the exception was caught.
   * @param cause
   *        The cause of this exception (can be used to trace the error).
   */
  public PdfActException(int exitCode, String message, Throwable cause) {
    super(message, cause);
    this.exitCode = exitCode;
  }

  // ===========================================================================

  /**
   * Returns the code to return on exiting PdfAct.
   * 
   * @return The code to return on exiting PdfAct.
   */
  public int getExitCode() {
    return this.exitCode;
  }
}
