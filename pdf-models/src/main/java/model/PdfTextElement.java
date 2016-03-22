package model;

/**
 * Any text element.
 *
 * @author Claudius Korzen
 */
public interface PdfTextElement extends PdfElement, PdfArea, HasFeature,
    HasText {

  /**
   * Returns the text that may contain punctuation marks, sub- and superscripts 
   * (depending on the given flags).
   */
  public String getText(boolean includePunctuationMarks, 
      boolean includeSubscripts, boolean includeSuperscripts);
}
