package pdfact.core.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.util.list.WordList;
import pdfact.core.util.list.WordList.WordListFactory;

/**
 * A plain implementation of {@link Paragraph}.
 * 
 * @author Claudius Korzen
 */
public class PlainParagraph extends PlainElement implements Paragraph {
  /**
   * The words of this paragraph.
   */
  protected WordList words;

  /**
   * The text of this paragraph.
   */
  protected String text;

  /**
   * The positions of this paragraph.
   */
  protected List<Position> positions;

  /**
   * The role of this paragraph.
   */
  protected SemanticRole role;

  /**
   * The secondary role of this paragraph.
   */
  protected SemanticRole secondaryRole;

  /**
   * The statistics about the characters in this paragraph.
   */
  protected CharacterStatistic characterStatistic;

  /**
   * Creates an empty paragraph.
   * 
   * @param wordListFactory
   *        The factory to create instances of {@link WordList}.
   */
  @AssistedInject
  public PlainParagraph(WordListFactory wordListFactory) {
    this.words = wordListFactory.create();
  }

  // ==========================================================================

  @Override
  public WordList getWords() {
    return this.words;
  }

  @Override
  public Word getFirstWord() {
    if (this.words == null || this.words.isEmpty()) {
      return null;
    }
    return this.words.get(0);
  }

  @Override
  public Word getLastWord() {
    if (this.words == null || this.words.isEmpty()) {
      return null;
    }
    return this.words.get(this.words.size() - 1);
  }

  @Override
  public void setWords(WordList words) {
    this.words = words;
  }

  @Override
  public void addWords(WordList words) {
    this.words.addAll(words);
  }

  @Override
  public void addWord(Word word) {
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
  public List<Position> getPositions() {
    return this.positions;
  }

  @Override
  public Position getFirstPosition() {
    if (this.positions == null || this.positions.isEmpty()) {
      return null;
    }
    return this.positions.get(0);
  }

  @Override
  public Position getLastPosition() {
    if (this.positions == null || this.positions.isEmpty()) {
      return null;
    }
    return this.positions.get(this.positions.size() - 1);
  }

  @Override
  public void setPositions(List<Position> positions) {
    this.positions = positions;
  }

  @Override
  public void addPositions(List<Position> positions) {
    this.positions.addAll(positions);
  }

  @Override
  public void addPosition(Position position) {
    this.positions.add(position);
  }

  // ==========================================================================

  @Override
  public SemanticRole getSemanticRole() {
    return this.role;
  }

  @Override
  public void setSemanticRole(SemanticRole role) {
    this.role = role;
  }

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
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainParagraph(" + this.getText() + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Paragraph) {
      Paragraph otherParagraph = (Paragraph) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getWords(), otherParagraph.getWords());
      builder.append(getText(), otherParagraph.getText());
      builder.append(getPositions(), otherParagraph.getPositions());
      builder.append(getSemanticRole(), otherParagraph.getSemanticRole());
      
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getWords());
    builder.append(getText());
    builder.append(getPositions());
    builder.append(getSemanticRole());
    return builder.hashCode();
  }
}
