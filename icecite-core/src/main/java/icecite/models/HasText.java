package icecite.models;

/**
 * An interface that declares that the implementing object has text.
 *
 * @author Claudius Korzen
 */
public interface HasText {
  /**
   * Returns the text.
   * 
   * @return The text.
   */
  String getText();

  /**
   * Sets the given text.
   * 
   * @param text
   *        The text.
   */
  void setText(String text);
}
