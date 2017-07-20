package icecite.exception;

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
   * The status code of this exception.
   */
  protected int statusCode;

  /**
   * The default constructor.
   * 
   * @param statusCode
   *        The status code of this exception.
   * @param message
   *        The message of this exception.
   */
  public IceciteException(int statusCode, String message) {
    this(statusCode, message, null);
  }

  /**
   * The default constructor.
   * 
   * @param statusCode
   *        The status code of this exception.
   * @param message
   *        The message of this exception.
   * @param cause The cause of this exception.
   */
  public IceciteException(int statusCode, String message, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
  }
  
  // ===========================================================================
  
  /**
   * Returns the status code of this exception.
   * 
   * @return The status code of this exception.
   */
  public int getStatusCode() {
    return this.statusCode;
  }
}
