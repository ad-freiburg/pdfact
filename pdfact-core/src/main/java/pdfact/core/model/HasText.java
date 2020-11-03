package pdfact.core.model;

/**
 * An interface to implement by elements that includes text (in form of a
 * string).
 *
 * @author Claudius Korzen
 */
public interface HasText {
  /**
   * Returns the text of this element.
   * 
   * @return The text of this element.
   */
  String getText();

  /**
   * Sets the text of this element.
   * 
   * @param text The text of this element.
   */
  void setText(String text);
}
