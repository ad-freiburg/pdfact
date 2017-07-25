package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfParagraph;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;
import icecite.utils.geometric.Rectangle.RectangleFactory;

// TODO: Do not extend the bounding box in the model.

/**
 * A plain implementation of {@link PdfParagraph}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraph implements PdfParagraph {
  /**
   * The words of this paragraph.
   */
  protected PdfWordList words;

  /**
   * The text of this paragraph.
   */
  protected String text;

  // ========================================================================
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
    this.words = wordListFactory.create();
  }

  // ========================================================================

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
  }

  // ========================================================================

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

  // ========================================================================

//  @Override
//  public PdfType getType() {
//    return PdfType.PARAGRAPHS;
//  }

  // ========================================================================

  @Override
  public String toString() {
    return "PlainPdfParagraph(" + this.getText() + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfParagraph) {
      PdfParagraph otherParagraph = (PdfParagraph) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getText(), otherParagraph.getText());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getText());
    return builder.hashCode();
  }
}
