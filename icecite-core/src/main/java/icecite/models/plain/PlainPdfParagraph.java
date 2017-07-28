package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfFeature;
import icecite.models.PdfParagraph;
import icecite.models.PdfRole;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;

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

  /**
   * The role of this paragraph.
   */
  protected PdfRole role;

  /**
   * The secondary role of this paragraph.
   */
  protected PdfRole secondaryRole;

  // ========================================================================
  // Constructors.

  /**
   * Creates an empty paragraph.
   * 
   * @param wordListFactory
   *        The factory to create instances of PdfWordListFactory.
   */
  @AssistedInject
  public PlainPdfParagraph(PdfWordListFactory wordListFactory) {
    this.words = wordListFactory.create();
  }

  // ========================================================================

  @Override
  public PdfWordList getWords() {
    return this.words;
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

  // ==========================================================================

  @Override
  public PdfCharacter getFirstCharacter() {
    PdfWord firstWord = getFirstWord();
    if (firstWord == null) {
      return null;
    }
    return firstWord.getFirstCharacter();
  }

  @Override
  public PdfCharacter getLastCharacter() {
    PdfWord lastWord = getLastWord();
    if (lastWord == null) {
      return null;
    }
    return lastWord.getLastCharacter();
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
