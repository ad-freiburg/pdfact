package model;

/**
 * Interface for a single pdf paragraph.
 *
 * @author Claudius Korzen
 */
public interface PdfNonTextParagraph extends PdfElement {  
  /**
   * Adds the given non text element to this paragraph.
   */
  public void addNonTextElement(PdfElement element);
  
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