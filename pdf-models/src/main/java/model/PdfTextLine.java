package model;

import java.util.List;

import de.freiburg.iif.model.Line;

/**
 * The interface for a single text line.
 *
 * @author Claudius Korzen
 */
public interface PdfTextLine extends PdfTextElement {
  /**
   * Sets the words in this line.
   */
  public void setWords(List<? extends PdfWord> words);
  
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
   * Returns the meanline of this text line.
   */
  public Line getMeanLine();
  
  /**
   * Sets the meanline of this text line.
   */
  public void setMeanLine(Line meanLine);
  
  /**
   * Returns the baseline of this text line.
   */
  public Line getBaseLine();
  
  /**
   * Sets the baseline of this text line.
   */
  public void setBaseLine(Line baseLine);
}
