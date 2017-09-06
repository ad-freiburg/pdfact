package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.util.list.TextLineList;
import pdfact.core.util.list.TextLineList.TextLineListFactory;

/**
 * A plain implementation of {@link TextBlock}.
 * 
 * @author Claudius Korzen
 */
public class PlainTextBlock extends PlainElement implements TextBlock {
  /**
   * The text lines of this text block.
   */
  protected TextLineList textLines;

  /**
   * The text of this text block.
   */
  protected String text;

  /**
   * The position of this text block.
   */
  protected Position position;

  /**
   * The semantic role of this text block.
   */
  protected SemanticRole semanticRole;

  /**
   * The secondary role of this text block.
   */
  protected SemanticRole secondaryRole;

  /**
   * The statistics about the characters in this text block.
   */
  protected CharacterStatistic characterStatistic;
  
  /**
   * The statistics about the text lines in this text block.
   */
  protected TextLineStatistic textLineStatistic;

  /**
   * Creates a new text block.
   * 
   * @param textLineListFactory
   *        The factory to create instances of {@link TextLineList}.
   */
  @AssistedInject
  public PlainTextBlock(TextLineListFactory textLineListFactory) {
    this.textLines = textLineListFactory.create();
  }

  // ==========================================================================

  @Override
  public TextLineList getTextLines() {
    return this.textLines;
  }

  @Override
  public TextLine getFirstTextLine() {
    if (this.textLines == null || this.textLines.isEmpty()) {
      return null;
    }
    return this.textLines.get(0);
  }

  @Override
  public TextLine getLastTextLine() {
    if (this.textLines == null || this.textLines.isEmpty()) {
      return null;
    }
    return this.textLines.get(this.textLines.size() - 1);
  }
  
  @Override
  public void setTextLines(TextLineList textLines) {
    this.textLines = textLines;
  }

  @Override
  public void addTextLines(TextLineList textLines) {
    this.textLines.addAll(textLines);
  }

  @Override
  public void addTextLine(TextLine word) {
    this.textLines.add(word);
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
  public Position getPosition() {
    return this.position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
  }

  // ==========================================================================

  @Override
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==========================================================================
  
  @Override
  public TextLineStatistic getTextLineStatistic() {
    return this.textLineStatistic;
  }

  @Override
  public void setTextLineStatistic(TextLineStatistic statistic) {
    this.textLineStatistic = statistic;
  }
  
  // ==========================================================================

  @Override
  public SemanticRole getSemanticRole() {
    return this.semanticRole;
  }

  @Override
  public void setSemanticRole(SemanticRole semanticRole) {
    this.semanticRole = semanticRole;
  }

  // ==========================================================================

  @Override
  public SemanticRole getSecondaryRole() {
    return this.secondaryRole;
  }

  @Override
  public void setSecondaryRole(SemanticRole secondaryRole) {
    this.secondaryRole = secondaryRole;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainTextBlock(pos: " + getPosition() + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof TextBlock) {
      TextBlock otherTextBlock = (TextBlock) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getTextLines(), otherTextBlock.getTextLines());
      builder.append(getText(), otherTextBlock.getText());
      builder.append(getPosition(), otherTextBlock.getPosition());
      builder.append(getSemanticRole(), otherTextBlock.getSemanticRole());
      
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getTextLines());
    builder.append(getText());
    builder.append(getPosition());
    builder.append(getSemanticRole());
    return builder.hashCode();
  }
}
