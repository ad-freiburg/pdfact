package model;

import java.util.Collection;
import java.util.List;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;

/**
 * Interface that represents an arbitrary area of a page.
 *
 * @author Claudius Korzen
 */
public interface PdfArea extends HasPdfDocument, HasPdfPage, HasRectangle,
    HasDimensionStatistics, HasTextStatistics, HasPositionStatistics, 
    HasPdfColor, HasPdfFont {
  // ___________________________________________________________________________

  /**
   * Returns the list of pdf elements.
   */
  public List<PdfElement> getElements();

  /**
   * Returns the list of pdf elements within the given rectangle.
   */
  public List<PdfElement> getElementsWithin(HasRectangle object);

  /**
   * Returns the list of all elements which surround the given rectangle.
   */
  public List<PdfElement> getElementsSurrounding(HasRectangle object);

  /**
   * Returns the list of all elements which overlap the given rectangle.
   */
  public List<PdfElement> getElementsOverlapping(HasRectangle object);

  /**
   * Adds any elements to this area.
   */
  public void addAnyElements(List<? extends PdfElement> elements);
  
  /**
   * Adds any element to this area.
   */
  public void addAnyElement(PdfElement element);
  
  // ___________________________________________________________________________

  /**
   * Returns the list of text characters.
   */
  public List<PdfCharacter> getTextCharacters();

  /**
   * Returns the list of text characters within the given rectangle.
   */
  public List<PdfCharacter> getTextCharactersWithin(HasRectangle object);

  /**
   * Returns the list of text characters which surround the given rectangle.
   */
  public List<PdfCharacter> getTextCharactersSurrounding(HasRectangle object);

  /**
   * Returns the list of text characters which overlap the given rectangle.
   */
  public List<PdfCharacter> getTextCharactersOverlapping(HasRectangle object);

  /**
   * Sets the text characters of this area.
   */
  public void setTextCharacters(List<? extends PdfCharacter> characters);
  
  /**
   * Adds the text characters to this area.
   */
  public void addTextCharacters(List<? extends PdfCharacter> characters);
  
  /**
   * Adds a text characters to this area.
   */
  public void addTextCharacter(PdfCharacter character);

  // ___________________________________________________________________________

  /**
   * Returns the list of words.
   */
  public List<PdfWord> getWords();

  /**
   * Returns the list of words within the given rectangle.
   */
  public List<PdfWord> getWordsWithin(HasRectangle object);

  /**
   * Returns the list of words which surround the given rectangle.
   */
  public List<PdfWord> getWordsSurrounding(HasRectangle object);

  /**
   * Returns the list of words which overlap the given rectangle.
   */
  public List<PdfWord> getWordsOverlapping(HasRectangle object);

  /**
   * Sets the text words of this area.
   */
  public void setWords(List<? extends PdfWord> words);
  
  /**
   * Adds the words to this area.
   */
  public void addWords(List<? extends PdfWord> words);
  
  /**
   * Adds a word to this area.
   */
  public void addWord(PdfWord word);

  // ___________________________________________________________________________

  /**
   * Returns the list of text lines.
   */
  public List<PdfTextLine> getTextLines();

  /**
   * Returns the list of text lines within the given rectangle.
   */
  public List<PdfTextLine> getTextLinesWithin(HasRectangle object);

  /**
   * Returns the list of text lines which surround the given rectangle.
   */
  public List<PdfTextLine> getTextLinesSurrounding(HasRectangle obj);

  /**
   * Returns the list of text lines which overlap the given rectangle.
   */
  public List<PdfTextLine> getTextLinesOverlapping(HasRectangle obj);

  /**
   * Sets the text lines of this area.
   */
  public void setTextLines(List<? extends PdfTextLine> lines);
  
  /**
   * Adds the text lines to this area.
   */
  public void addTextLines(List<? extends PdfTextLine> lines);
  
  /**
   * Adds a text lines to this area.
   */
  public void addTextLine(PdfTextLine line);

  // ___________________________________________________________________________

  /**
   * Returns the list of paragraphs.
   */
  public List<PdfTextParagraph> getParagraphs();

  /**
   * Returns the list of text paragraphs within the given rectangle.
   */
  public List<PdfTextParagraph> getParagraphsWithin(HasRectangle obj);

  /**
   * Returns the list of text paragraphs within the given rectangle.
   */
  public List<PdfTextParagraph> getParagraphsSurrounding(
      HasRectangle object);

  /**
   * Returns the list of text paragraphs which overlap the given rectangle.
   */
  public List<PdfTextParagraph> getParagraphsOverlapping(
      HasRectangle object);

  /**
   * Sets the text paragraphs of this area.
   */
  public void setParagraphs(List<? extends PdfTextParagraph> paragraphs);
  
  /**
   * Adds the text paragraphs to this area.
   */
  public void addParagraphs(List<? extends PdfTextParagraph> paragraphs);
  
  /**
   * Adds a paragraph to this area.
   */
  public void addParagraph(PdfTextParagraph paragraphs);

  // ___________________________________________________________________________

  /**
   * Returns the list of all text elements.
   */
  public List<? extends PdfTextElement> getTextElements();

  /**
   * Returns the list of text elements within the given rectangle.
   */
  public List<? extends PdfTextElement> getTextElementsWithin(
      HasRectangle object);

  /**
   * Returns the list of text elements within the given rectangle.
   */
  public List<? extends PdfTextElement> getTextElementsSurrounding(
      HasRectangle object);

  /**
   * Returns the list of text elements which overlap the given rectangle.
   */
  public List<? extends PdfTextElement> getTextElementsOverlapping(
      HasRectangle object);

  // ___________________________________________________________________________

  /**
   * Returns the list of figures.
   */
  public List<PdfFigure> getFigures();

  /**
   * Returns the list of figures within the given rectangle.
   */
  public List<PdfFigure> getFiguresWithin(HasRectangle object);

  /**
   * Returns the list of figures within the given rectangle.
   */
  public List<PdfFigure> getFiguresSurrounding(HasRectangle object);

  /**
   * Returns the list of figures which overlap the given rectangle.
   */
  public List<PdfFigure> getFiguresOverlapping(HasRectangle object);

  // ___________________________________________________________________________

  /**
   * Returns the list of shapes (lines, curves, etc.).
   */
  public List<PdfShape> getShapes();

  /**
   * Returns the list of shapes within the given rectangle.
   */
  public List<PdfShape> getShapesWithin(HasRectangle object);

  /**
   * Returns the list of shapes within the given rectangle.
   */
  public List<PdfShape> getShapesSurrounding(HasRectangle object);

  /**
   * Returns the list of shapes which overlap the given rectangle.
   */
  public List<PdfShape> getShapesOverlapping(HasRectangle object);

  // ___________________________________________________________________________

  /**
   * Returns the list of all non text paragraphs.
   */
  public List<PdfNonTextParagraph> getNonTextParagraphs();

  /**
   * Returns the list of non text paragraphs within the given rectangle.
   */
  public List<PdfNonTextParagraph> getNonTextParagraphsWithin(
      HasRectangle object);

  /**
   * Returns the list of non text paragraphs within the given rectangle.
   */
  public List<PdfNonTextParagraph> getNonTextParagraphsSurrounding(
      HasRectangle object);

  /**
   * Returns the list of non text elements which overlap the given rectangle.
   */
  public List<PdfNonTextParagraph> getNonTextParagraphsOverlapping(
      HasRectangle object);

  /**
   * Sets the non text paragraphs of this area.
   */
  public void setNonTextParagraphs(
      List<? extends PdfNonTextParagraph> paragraphs);
  
  /**
   * Adds the non text paragrpahs to this area.
   */
  public void addNonTextParagraphs(
      List<? extends PdfNonTextParagraph> paragraphs);
  
  /**
   * Adds a non text paragraph to this area.
   */
  public void addNonTextParagraph(PdfNonTextParagraph paragraph);

  // ___________________________________________________________________________

  /**
   * Returns the list of all non text elements.
   */
  public List<? extends PdfElement> getNonTextElements();

  /**
   * Returns the list of non text elements within the given rectangle.
   */
  public List<? extends PdfElement> getNonTextElementsWithin(
      HasRectangle object);

  /**
   * Returns the list of non text elements within the given rectangle.
   */
  public List<? extends PdfElement> getNonTextElementsSurrounding(
      HasRectangle object);

  /**
   * Returns the list of non text elements which overlap the given rectangle.
   */
  public List<? extends PdfElement> getNonTextElementsOverlapping(
      HasRectangle object);

  // ___________________________________________________________________________

  /**
   * Returns all elements of this page that are related to the given feature.
   */
  public List<? extends PdfElement> getElementsByFeature(PdfFeature feature);

  // ___________________________________________________________________________

  /**
   * Returns all fonts used in this page.
   */
  public Collection<PdfFont> getFonts();

  /**
   * Returns all colors used in this page.
   */
  public Collection<PdfColor> getColors();

  // ___________________________________________________________________________

  /**
   * Returns true, if we have to ignore this area from further computations.
   */
  public boolean ignore();
  
  /**
   * Sets the ignore flag of this area.
   */
  public void setIgnore(boolean ignore);
  
  public Rectangle getRawRectangle();
  
  public PdfTextAlignment getTextLineAlignment();
  
  /**
   * A string that identifies the textual markup, i.e. a string containing the 
   * font and fontsize.
   */
  public String getMarkup();
  
  public PdfArea getBlock();
  public void setBlock(PdfArea block);
}
