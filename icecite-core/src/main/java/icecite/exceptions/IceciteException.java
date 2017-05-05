package icecite.exceptions;

/**
 * The basic exception to throw on any error/exception.
 * 
 * @author Claudius Korzen
 */
public class IceciteException extends Exception {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 1L;

  /**
   * Creates a new exception object.
   * 
   * @param msg
   *        The message of this exception.
   */
  public IceciteException(String msg) {
    super(msg);
  }
}
