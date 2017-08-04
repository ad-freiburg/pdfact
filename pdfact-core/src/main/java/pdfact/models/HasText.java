package pdfact.models;

/**
 * An interface that is implemented by PDF elements that contain text.
 *
 * @author Claudius Korzen
 */
public interface HasText {
  /**
   * Returns the text of the element.
   * 
   * @return The text.
   */
  String getText();

  /**
   * Sets the text of the element.
   * 
   * @param text
   *        The text.
   */
  void setText(String text);
}
