package icecite.models.plain;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfParagraph;
import icecite.models.PdfType;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;

// TODO: Do not extend the bounding box in the model.

/**
 * A plain implementation of {@link PdfParagraph}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraph extends PlainPdfElement implements PdfParagraph {
  /**
   * The words of this paragraph.
   */
  protected PdfWordList words;

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
   * @param wordListFactory
   *        The factory to create instances of PdfWordList. 
   */
  @AssistedInject
  public PlainPdfParagraph(RectangleFactory rectangleFactory,
      PdfWordListFactory wordListFactory) {
    this.boundingBox = rectangleFactory.create();
    this.words = wordListFactory.create();
  }

  // ==========================================================================

  @Override
  public PdfWordList getWords() {
    return this.words;
  }

  @Override
  public PdfWord getFirstWord() {
    if (this.words != null && !this.words.isEmpty()) {
      return this.words.get(0);
    }
    return null;
  }

  @Override
  public PdfWord getLastWord() {
    if (this.words != null && !this.words.isEmpty()) {
      return this.words.get(this.words.size() - 1);
    }
    return null;
  }

  @Override
  public void setWords(PdfWordList words) {
    this.words = words;
    for (PdfWord word : words) {
      this.boundingBox.extend(word);
    }
  }

  @Override
  public void addWords(PdfWordList words) {
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
