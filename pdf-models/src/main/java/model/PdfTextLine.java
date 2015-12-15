package model;

import java.util.List;

/**
 * The interface for a single text line.
 *
 * @author Claudius Korzen
 */
public interface PdfTextLine extends PdfTextElement {
  /**
   * Returns the words in this line.
   */
  public List<PdfWord> getWords(); 
  
  /**
   * Returns the first word in this line.
   */
  public PdfWord getFirstWord();
  
  /**
   * Returns the last word in this font.
   */
  public PdfWord getLastWord();
  
  /**
   * Sets the words in this line.
   */
  void setWords(List<? extends PdfWord> words);
}
