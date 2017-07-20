package icecite.exception;

/**
 * The standard exception to throw on any visualization error.
 * 
 * @author Claudius Korzen
 */
public class IceciteVisualizeException extends IceciteException {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -2555647293109903637L;
  
  /**
   * The default status code of this exception.
   */
  protected static final int statusCode = 40;

  /**
   * The default constructor.
   * 
   * @param message
   *        The message of this exception.
   */
  public IceciteVisualizeException(String message) {
    super(statusCode, message);
  }
  
  /**
   * The default constructor.
   * 
   * @param message
   *        The message of this exception.
   * @param cause
   *        The cause of this exception.
   */
  public IceciteVisualizeException(String message, Throwable cause) {
    super(statusCode, message, cause);
  }
}
