package icecite.exception;

/**
 * The standard exception to throw on any serialization error.
 * 
 * @author Claudius Korzen
 */
public class IceciteSerializeException extends IceciteException {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -2555647293109903637L;

  /**
   * The default status code of this exception.
   */
  protected static final int STATUS_CODE = 30;

  /**
   * The default constructor.
   * 
   * @param message
   *        The message of this exception.
   */
  public IceciteSerializeException(String message) {
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
  public IceciteSerializeException(String message, Throwable cause) {
    super(STATUS_CODE, message, cause);
  }
}