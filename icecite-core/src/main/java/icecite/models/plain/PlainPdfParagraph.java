package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfFeature;
import icecite.models.PdfParagraph;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.utils.geometric.Rectangle.RectangleFactory;

// TODO: Do not extend the bounding box in the model.

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

  // ========================================================================
  // Constructors.

  /**
   * Creates an empty paragraph.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @AssistedInject
  public PlainPdfParagraph(RectangleFactory rectangleFactory) {
    this.textBlocks = new ArrayList<>();
  }

  // ========================================================================

  @Override
  public List<PdfTextBlock> getTextBlocks() {
    return this.textBlocks;
  }

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
  public PdfTextLine getFirstTextLine() {
    PdfTextBlock firstTextBlock = getFirstTextBlock();
    if (firstTextBlock == null) {
      return null;
    }
    return firstTextBlock.getFirstTextLine();
  }

  @Override
  public PdfTextLine getLastTextLine() {
    PdfTextBlock lastTextBlock = getLastTextBlock();
    if (lastTextBlock == null) {
      return null;
    }
    return lastTextBlock.getLastTextLine();
  }
  
  // ==========================================================================
  
  @Override
  public PdfWord getFirstWord() {
    PdfTextBlock firstTextBlock = getFirstTextBlock();
    if (firstTextBlock == null) {
      return null;
    }
    return firstTextBlock.getFirstWord();
  }

  @Override
  public PdfWord getLastWord() {
    PdfTextBlock lastTextBlock = getLastTextBlock();
    if (lastTextBlock == null) {
      return null;
    }
    return lastTextBlock.getLastWord();
  }

  // ==========================================================================

  @Override
  public PdfCharacter getFirstCharacter() {
    PdfTextBlock firstTextBlock = getFirstTextBlock();
    if (firstTextBlock == null) {
      return null;
    }
    return firstTextBlock.getFirstCharacter();
  }

  @Override
  public PdfCharacter getLastCharacter() {
    PdfTextBlock lastTextBlock = getLastTextBlock();
    if (lastTextBlock == null) {
      return null;
    }
    return lastTextBlock.getLastCharacter();
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

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.PARAGRAPH;
  }

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
