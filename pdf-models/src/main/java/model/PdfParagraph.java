package model;

import java.util.List;

/**
 * Interface for a single pdf paragraph.
 *
 * @author Claudius Korzen
 */
public interface PdfParagraph extends PdfTextElement, HasTextLineStatistics {
  /**
   * Returns the lines in this paragraph.
   */
  public List<PdfTextLine> getTextLines();
  
  /**
   * Adds the given line to this paragraph.
   */
  public void addTextLine(PdfTextLine line);
  
  /**
   * Sets the context of this paragraph.
   */
  public void setContext(String context);
  
  /**
   * Returns the context of this paragraph.
   */
  public String getContext();
  
  /**
   * Sets the role of this paragraph.
   */
  public void setRole(String role);
  
  /**
   * Returns the role of this paragraph.
   */
  public String getRole();
}
