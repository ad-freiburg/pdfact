package pdfact.core.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.core.util.list.ElementList;

/**
 * A text paragraph in a document.
 * 
 * @author Claudius Korzen
 */
public class Paragraph extends Element implements HasWords, HasText, HasPositions, HasSemanticRole {
  /**
   * The words of this paragraph.
   */
  protected ElementList<Word> words;

  /**
   * The text of this paragraph.
   */
  protected String text;

  /**
   * The positions of this paragraph.
   */
  protected List<Position> positions;

  /**
   * The semantic role of this paragraph.
   */
  protected SemanticRole role;

  /**
   * The secondary semantic role of this paragraph.
   */
  protected SemanticRole secondaryRole;

  /**
   * The statistics about the characters in this paragraph.
   */
  protected CharacterStatistic characterStatistic;

  // ==============================================================================================

  /**
   * Creates a new paragraph.
   */
  public Paragraph() {
    this.words = new ElementList<>();
  }

  // ==============================================================================================

  @Override
  public ElementList<Word> getWords() {
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
  public void setWords(ElementList<Word> words) {
    this.words = words;
  }

  @Override
  public void addWords(ElementList<Word> words) {
    this.words.addAll(words);
  }

  @Override
  public void addWord(Word word) {
    this.words.add(word);
  }

  // ==============================================================================================

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

  // ==============================================================================================

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

  // ==============================================================================================

  @Override
  public SemanticRole getSemanticRole() {
    return this.role;
  }

  @Override
  public void setSemanticRole(SemanticRole role) {
    this.role = role;
  }

  @Override
  public SemanticRole getSecondarySemanticRole() {
    return this.secondaryRole;
  }

  @Override
  public void setSecondarySemanticRole(SemanticRole secondaryRole) {
    this.secondaryRole = secondaryRole;
  }

  // ==============================================================================================

  @Override
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Paragraph(" + this.getText() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Paragraph) {
      Paragraph otherParagraph = (Paragraph) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getWords(), otherParagraph.getWords());
      builder.append(getText(), otherParagraph.getText());
      builder.append(getPositions(), otherParagraph.getPositions());
      builder.append(getSemanticRole(), otherParagraph.getSemanticRole());
      builder.append(getSecondarySemanticRole(),
          otherParagraph.getSecondarySemanticRole());

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
    builder.append(getSecondarySemanticRole());
    return builder.hashCode();
  }
}
