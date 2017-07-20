package icecite.models.plain;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfFeature;
import icecite.models.PdfWord;
import icecite.utils.geometric.Rectangle;

// TODO: Do not derive the bounding box in the model.

/**
 * A plain implementation of {@link PdfWord}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfWord extends PlainPdfElement implements PdfWord {
  /**
   * The characters of this word.
   */
  protected PdfCharacterList characters;

  /**
   * The text of this word.
   */
  protected String text;

  /**
   * The boolean flag that indicates if this word is hyphenated.
   */
  protected boolean isHyphenated;

  /**
   * The boolean flag that indicates if this word is dehyphenated.
   */
  protected boolean isDehyphenated;

  /**
   * The list of rectangles of the hyphenated syllables.
   */
  protected List<Rectangle> syllableRectangles;

  // ==========================================================================

  /**
   * Creates a new word.
   * 
   * @param characters
   *        The characters of this word.
   */
  @AssistedInject
  public PlainPdfWord(@Assisted PdfCharacterList characters) {
    this.characters = characters;
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
  public PdfCharacterList getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(PdfCharacterList characters) {
    this.characters = characters;
  }

  @Override
  public void addCharacters(PdfCharacterList characters) {
    for (PdfCharacter character : characters) {
      addCharacter(character);
    }
  }

  @Override
  public void addCharacter(PdfCharacter character) {
    this.characters.add(character);
  }

  // ==========================================================================

  @Override
  public boolean isHyphenated() {
    return this.isHyphenated;
  }

  @Override
  public void setIsHyphenated(boolean isHyphenated) {
    this.isHyphenated = isHyphenated;
  }

  // ==========================================================================

  @Override
  public boolean isDehyphenated() {
    return this.isDehyphenated;
  }

  @Override
  public void setIsDehyphenated(boolean isDehyphenated) {
    this.isDehyphenated = isDehyphenated;
  }

  // ==========================================================================

  @Override
  public List<Rectangle> getSyllableRectangles() {
    return this.syllableRectangles;
  }

  @Override
  public void setSyllableRectangles(List<Rectangle> rectangles) {
    this.syllableRectangles = rectangles;
  }

  // ==========================================================================

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.WORD;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfWord(" + this.text + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfWord) {
      PdfWord otherWord = (PdfWord) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getCharacters(), otherWord.getCharacters());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getCharacters());
    return builder.hashCode();
  }
}
