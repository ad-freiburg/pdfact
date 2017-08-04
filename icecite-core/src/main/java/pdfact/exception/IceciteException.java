package pdfact.exception;

/**
 * The standard exception to throw on any errors while executing Icecite.
 * 
 * @author Claudius Korzen
 */
public class IceciteException extends Exception {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -7396761485778372279L;

  /**
   * The exit code to return of this exception.
   */
  protected int exitCode;

  /**
   * Creates a new exception.
   * 
   * @param exitCode
   *        The exit code of this exception.
   * @param message
   *        The message of this exception.
   */
  public IceciteException(int exitCode, String message) {
    this(exitCode, message, null);
  }

  /**
   * Creates a new exception.
   * 
   * @param exitCode
   *        The exit code of this exception.
   * @param message
   *        The message of this exception.
   * @param cause
   *        The cause of this exception.
   */
  public IceciteException(int exitCode, String message, Throwable cause) {
    super(message, cause);
    this.exitCode = exitCode;
  }

  // ===========================================================================

  /**
   * Returns the exit code of this exception.
   * 
   * @return The exit code of this exception.
   */
  public int getExitCode() {
    return this.exitCode;
  }
}
