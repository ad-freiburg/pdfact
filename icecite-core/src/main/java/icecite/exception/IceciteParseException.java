package icecite.exception;

/**
 * The standard exception to throw on any errors on parsing a PDF.
 * 
 * @author Claudius Korzen
 */
public class IceciteParseException extends IceciteException {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 3552957003556580621L;

  /**
   * The default status code of this exception.
   */
  protected static final int STATUS_CODE = 20;

  /**
   * The default constructor.
   * 
   * @param message
   *        The message of this exception.
   */
  public IceciteParseException(String message) {
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
  public IceciteParseException(String message, Throwable cause) {
    super(STATUS_CODE, message, cause);
  }
}
