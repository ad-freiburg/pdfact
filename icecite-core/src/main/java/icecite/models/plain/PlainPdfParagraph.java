package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfParagraph;
import icecite.models.PdfType;
import icecite.models.PdfWord;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;

/**
 * A plain implementation of {@link PdfParagraph}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraph extends PlainPdfElement implements PdfParagraph {
  /**
   * The words of this paragraph.
   */
  protected List<PdfWord> words;

  /**
   * The text of this paragraph.
   */
  protected String text;

  // ==========================================================================
  // Constructors.

  /**
   * Creates an empty paragraph.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @AssistedInject
  public PlainPdfParagraph(RectangleFactory rectangleFactory) {
    this.boundingBox = rectangleFactory.create();
    this.words = new ArrayList<>();
  }

  // ==========================================================================

  @Override
  public List<PdfWord> getWords() {
    return this.words;
  }

  @Override
  public void setWords(List<PdfWord> words) {
    this.words = words;
    for (PdfWord word : words) {
      this.boundingBox.extend(word);
    }
  }

  @Override
  public void addWords(List<PdfWord> words) {
    for (PdfWord word : words) {
      addWord(word);
    }
  }

  @Override
  public void addWord(PdfWord word) {
    this.words.add(word);
    this.boundingBox.extend(word);
  }

  // ==========================================================================

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }
 

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    return this.boundingBox;
  }

  @Override
  public void setRectangle(Rectangle boundingBox) {
    // The bounding box results from the text lines of this paragraph.
    throw new UnsupportedOperationException();
  }

  // ==========================================================================

  @Override
  public PdfType getType() {
    return PdfType.PARAGRAPHS;
  }
}
