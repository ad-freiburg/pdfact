package model;

import java.util.Collection;
import java.util.List;

import de.freiburg.iif.model.Rectangle;

/**
 * The interface for a pdf page, containing the list of characters, figures
 * and shapes.
 * 
 * @author Claudius Korzen
 */
public interface PdfPage extends PdfTextArea, HasTextLineStatistics {
  /**
   * Returns the media box of this page. TODO: Explain media box.
   */
  public Rectangle getMediaBox();

  /**
   * Returns the crop box of this page.  TODO: Explain crop box. 
   */
  public Rectangle getCropBox();

  /**
   * Returns the art box of this page.  TODO: Explain art box.
   */
  public Rectangle getArtBox();

  /**
   * Returns the trim box of this page.  TODO: Explain trim box.
   */
  public Rectangle getTrimBox();

  /**
   * Returns the x-offset between cropBox and mediaBox.
   * 
   * @return the x-offset between cropBox and mediaBox.
   */
  public abstract float getCropXOffset();

  /**
   * Returns the y-offset between cropBox and mediaBox.
   * 
   * @return the y-offset between cropBox and mediaBox.
   */
  public abstract float getCropYOffset();
  
  /**
   * Returns the number of this page in the document.
   */
  public int getPageNumber();

  /**
   * Returns the list of text characters.
   */
  public List<PdfCharacter> getTextCharacters();
  
  /**
   * Sets the text characters of this page.
   */
  public void setTextCharacters(List< ? extends PdfCharacter> characters);
  
  /**
   * Returns the list of words.
   */
  public List<PdfWord> getWords();
  
  /**
   * Sets the words of this page.
   */
  public void setWords(List< ? extends PdfWord> words);
  
  /**
   * Returns the list of text lines.
   */
  public List<PdfTextLine> getTextLines();
  
  /**
   * Sets the text lines of this page.
   */
  public void setTextLines(List< ? extends PdfTextLine> lines);
  
  /**
   * Returns the list of paragraphs.
   */
  public List<PdfParagraph> getParagraphs();
  
  /**
   * Sets the paragraphs of this page.
   */
  public void setParagraphs(List< ? extends PdfParagraph> paragraphs);

  /**
   * Returns the list of figures.
   */
  public List<PdfFigure> getFigures();
  
  /**
   * Returns the list of shapes (lines, curves, etc.).
   */
  public List<PdfShape> getShapes();
  
  /**
   * Returns all elements of this page that are related to the given feature.
   */
  public List<? extends PdfElement> getElementsByFeature(PdfFeature feature);
    
  /**
   * Returns all fonts used in this page.
   */
  public Collection<PdfFont> getFonts();
  
  /**
   * Returns all colors used in this page.
   */
  public Collection<PdfColor> getColors();
}

