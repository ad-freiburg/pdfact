package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterStatistics;
import icecite.models.PdfParagraph;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.models.PdfTextLineStatistics;

/**
 * A plain implementation of {@link PdfTextBlock}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextBlock extends PlainPdfElement implements PdfTextBlock {
  /**
   * The text lines of this text block.
   */
  protected PdfTextLineList textLines;

  /**
   * The text of this text block.
   */
  protected String text;

  /**
   * The parent paragraph.
   */
  protected PdfParagraph parentParagraph;

  /**
   * The role of this text block.
   */
  protected PdfRole role;

  /**
   * The secondary role of this text block.
   */
  protected PdfRole secondaryRole;

  /**
   * The statistics about the characters.
   */
  protected PdfCharacterStatistics characterStatistics;

  /**
   * The statistics about text lines.
   */
  protected PdfTextLineStatistics textLineStatistics;

  // ==========================================================================

  /**
   * Creates a new text block.
   * 
   * @param textLineListFactory
   *        The factory to create instances of {@link PdfTextLine}.
   */
  @AssistedInject
  public PlainPdfTextBlock(PdfTextLineListFactory textLineListFactory) {
    this.textLines = textLineListFactory.create();
  }

  // ==========================================================================

  @Override
  public PdfTextLineList getTextLines() {
    return this.textLines;
  }

  @Override
  public PdfTextLine getFirstTextLine() {
    if (this.textLines == null || this.textLines.isEmpty()) {
      return null;
    }
    return this.textLines.get(0);
  }

  @Override
  public PdfTextLine getLastTextLine() {
    if (this.textLines == null || this.textLines.isEmpty()) {
      return null;
    }
    return this.textLines.get(this.textLines.size() - 1);
  }

  @Override
  public void setTextLines(PdfTextLineList lines) {
    this.textLines = lines;
  }

  @Override
  public void addTextLines(PdfTextLineList lines) {
    this.textLines.addAll(lines);
  }

  @Override
  public void addTextLine(PdfTextLine line) {
    this.textLines.add(line);
  }

  // ==========================================================================

  @Override
  public PdfParagraph getParentPdfParagraph() {
    return this.parentParagraph;
  }

  @Override
  public void setParentPdfParagraph(PdfParagraph paragraph) {
    this.parentParagraph = paragraph;
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
  public PdfCharacterStatistics getCharacterStatistics() {
    return this.characterStatistics;
  }

  @Override
  public void setCharacterStatistics(PdfCharacterStatistics statistics) {
    this.characterStatistics = statistics;
  }

  @Override
  public PdfTextLineStatistics getTextLineStatistics() {
    return this.textLineStatistics;
  }

  @Override
  public void setPdfTextLineStatistics(PdfTextLineStatistics statistics) {
    this.textLineStatistics = statistics;
  }

  // ==========================================================================

  @Override
  public PdfRole getRole() {
    return this.role;
  }

  @Override
  public void setRole(PdfRole role) {
    this.role = role;
  }

  // ==========================================================================

  @Override
  public PdfRole getSecondaryRole() {
    return this.secondaryRole;
  }

  @Override
  public void setSecondaryRole(PdfRole secondaryRole) {
    this.secondaryRole = secondaryRole;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfTextBlock(pos: " + getPosition() + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfTextBlock) {
      PdfTextBlock otherTextBlock = (PdfTextBlock) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherTextBlock.getPosition());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPosition());
    return builder.hashCode();
  }
}
