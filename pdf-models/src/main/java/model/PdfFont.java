package model;

/**
 * The interface for a single pdf font.
 *
 * @author Claudius Korzen
 */
public interface PdfFont extends HasPdfDocument, HasId {
  /**
   * Returns the pdf name of this font.
   * 
   * @return the pdf name.
   */
  public String getName();

  /**
   * Returns the name of this font.
   * 
   * @return the name of this font.
   */
  public String getBasename();

  /**
   * Returns the full name of this font.
   * 
   * @return the full name of this font.
   */
  public String getFullName();

  /**
   * Returns the name of the font family.
   * 
   * @return the name of the font family.
   */
  public String getFamilyName();

  /**
   * Returns true, if the weight of this font is italic.
   * 
   * @return true, if the weight of this font is italic.
   */
  public boolean isItalic();

  /**
   * Returns true, if the weight of this font is bold.
   * 
   * @return true, if the weight of this font is bold.
   */
  public boolean isBold();

  /**
   * Returns true, if this font is a type3 font.
   * 
   * @return true, if this font is a type3 font.
   */
  public boolean isType3Font();
}
