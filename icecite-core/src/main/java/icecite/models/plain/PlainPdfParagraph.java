package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterStatistics;
import icecite.models.PdfFeature;
import icecite.models.PdfParagraph;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLineStatistics;

/**
 * A plain implementation of {@link PdfParagraph}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraph extends PlainPdfElement implements PdfParagraph {
  /**
   * The text blocks of this paragraph.
   */
  protected List<PdfTextBlock> textBlocks;

  /**
   * The text of this paragraph.
   */
  protected String text;

  /**
   * The role of this paragraph.
   */
  protected PdfRole role;

  /**
   * The secondary role of this paragraph.
   */
  protected PdfRole secondaryRole;

  /**
   * The statistics about the characters.
   */
  protected PdfCharacterStatistics characterStatistics;

  /**
   * The statistics about the text lines.
   */
  protected PdfTextLineStatistics textLineStatistics;

  // ==========================================================================
  // Constructors.

  /**
   * Creates an empty paragraph.
   */
  @AssistedInject
  public PlainPdfParagraph() {
    this.textBlocks = new ArrayList<>();
  }

  // ==========================================================================

  @Override
  public List<PdfTextBlock> getTextBlocks() {
    return this.textBlocks;
  }

  @Override
  public void setTextBlocks(List<PdfTextBlock> blocks) {
    this.textBlocks = blocks;
  }

  @Override
  public void addTextBlocks(List<PdfTextBlock> blocks) {
    this.textBlocks.addAll(blocks);
  }

  @Override
  public void addTextBlock(PdfTextBlock block) {
    this.textBlocks.add(block);
  }

  // ==========================================================================

  @Override
  public PdfTextBlock getFirstTextBlock() {
    if (this.textBlocks == null || this.textBlocks.isEmpty()) {
      return null;
    }
    return this.textBlocks.get(0);
  }

  @Override
  public PdfTextBlock getLastTextBlock() {
    if (this.textBlocks == null || this.textBlocks.isEmpty()) {
      return null;
    }
    return this.textBlocks.get(this.textBlocks.size() - 1);
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
  public PdfTextLineStatistics getTextLineStatistics() {
    return this.textLineStatistics;
  }

  @Override
  public void setPdfTextLineStatistics(PdfTextLineStatistics statistics) {
    this.textLineStatistics = statistics;
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

  // ==========================================================================

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.PARAGRAPH;
  }

  // ==========================================================================

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
      builder.append(getFeature(), otherParagraph.getFeature());
      builder.append(getPosition(), otherParagraph.getPosition());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getText());
    builder.append(getFeature());
    builder.append(getPosition());
    return builder.hashCode();
  }
}
