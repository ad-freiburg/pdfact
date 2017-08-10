package pdfact.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfElementType;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfWord;
import pdfact.models.PdfWordList;
import pdfact.models.PdfWordList.PdfWordListFactory;
import pdfact.utils.geometric.Line;

/**
 * A plain implementation of {@link PdfTextLine}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLine extends PlainPdfSinglePositionElement implements PdfTextLine {
  /**
   * The words of this text line.
   */
  protected PdfWordList words;

  /**
   * The text of this text line.
   */
  protected String text;

  /**
   * The baseline of this text line.
   */
  protected Line baseLine;

  /**
   * The statistics about the characters in this text line.
   */
  protected PdfCharacterStatistic characterStatistic;

  // ==========================================================================

  /**
   * Creates a new text line.
   * 
   * @param wordListFactory
   *        The factory to create instances of {@link PdfWordList}.
   */
  @AssistedInject
  public PlainPdfTextLine(PdfWordListFactory wordListFactory) {
    this.words = wordListFactory.create();
  }
  
  // ==========================================================================

  @Override
  public PdfElementType getType() {
    return PdfElementType.TEXT_LINE;
  }

  // ==========================================================================

  @Override
  public PdfWordList getWords() {
    return this.words;
  }

  @Override
  public PdfWord getFirstWord() {
    if (this.words == null || this.words.isEmpty()) {
      return null;
    }
    return this.words.get(0);
  }

  @Override
  public PdfWord getLastWord() {
    if (this.words == null || this.words.isEmpty()) {
      return null;
    }
    return this.words.get(this.words.size() - 1);
  }

  @Override
  public void setWords(PdfWordList words) {
    this.words = words;
  }

  @Override
  public void addWords(PdfWordList words) {
    this.words.addAll(words);
  }

  @Override
  public void addWord(PdfWord word) {
    this.words.add(word);
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
  public Line getBaseline() {
    return this.baseLine;
  }

  @Override
  public void setBaseline(Line baseLine) {
    this.baseLine = baseLine;
  }

  // ==========================================================================

  @Override
  public PdfCharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(PdfCharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfTextLine(" + this.text + ")";
  }

  // ==========================================================================
  
  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfTextLine) {
      PdfTextLine otherTextLine = (PdfTextLine) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getText(), otherTextLine.getText());
      builder.append(getPosition(), otherTextLine.getPosition());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getText());
    builder.append(getPosition());
    return builder.hashCode();
  }
}
