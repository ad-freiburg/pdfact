package icecite.models;

/**
 * An interface that declares that the implementing object has a text.
 *
 * @author Claudius Korzen
 */
public interface HasText {
  /**
   * Returns the text of the implementing object.
   * 
   * @return The text of the implementing object.
   */
  String getText();

  /**
   * Sets the text of the implementing object.
   * 
   * @param text
   *        The text of the implementing object.
   */
  void setText(String text);
}
