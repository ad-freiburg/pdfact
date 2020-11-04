package pdfact.core.util.normalize;

import gnu.trove.set.TCharSet;
import gnu.trove.set.hash.TCharHashSet;
import pdfact.core.model.Word;

/**
 * A word normalizer.
 * 
 * @author Claudius Korzen
 */
public class WordNormalizer {
  /**
   * The boolean flag that indicates whether words should be transformed to lower
   * cases or not.
   */
  protected boolean isToLowerCase;

  /**
   * The boolean flag that indicates whether words should be trimmed or not.
   */
  protected boolean isTrim;

  /**
   * The characters to keep on removing leading characters.
   */
  protected TCharSet leadingCharactersToKeep;

  /**
   * The characters to keep on removing trailing characters.
   */
  protected TCharSet trailingCharactersToKeep;

  /**
   * Creates a new word normalizer.
   */
  public WordNormalizer() {
    this.leadingCharactersToKeep = new TCharHashSet();
    this.trailingCharactersToKeep = new TCharHashSet();
  }

  /**
   * Normalizes the given word.
   * 
   * @param word The word to normalize.
   * 
   * @return The normalized string.
   */
  public String normalize(Word word) {
    if (word == null) {
      return null;
    }

    String text = word.getText();
    if (text == null) {
      return null;
    }

    char[] array = text.toCharArray();

    int startIndex = 0;
    int endIndex = array.length - 1;

    if (isToLowerCase()) {
      array = toLowerCase(array);
    }

    if (hasLeadingCharactersToKeep()) {
      startIndex = getIndexOfFirstLeadingCharacterToKeep(array);
    }

    if (hasTrailingCharactersToKeep()) {
      endIndex = getIndexOfFirstTrailingCharacterToKeep(array);
    }

    if (startIndex < 0 || endIndex < 0) {
      return "";
    }

    if (startIndex >= array.length || endIndex >= array.length) {
      return "";
    }

    return new String(array, startIndex, endIndex - startIndex + 1);

    // // Make sure that word contains only characters or hyphens.
    // Pattern p = Pattern.compile("[a-zA-z\\-]+");
    // Matcher m = p.matcher(wordStr);
    // if (!m.matches()) {
    //
    // }
    // return null;
  }

  // ==============================================================================================

  /**
   * Transforms all characters in the given char array to lower cases.
   * 
   * @param array The char array to process.
   * 
   * @return The char array after transforming the characters to lower cases.
   */
  protected char[] toLowerCase(char[] array) {
    for (int i = 0; i < array.length; i++) {
      array[i] = Character.toLowerCase(array[i]);
    }
    return array;
  }

  // ==============================================================================================

  /**
   * Iterates through the given array, beginning at position 0, and returns the
   * index of the first character which is covered by the set of characters
   * defined by setLeadingCharactersToKeep().
   * 
   * @param array The char array to process.
   * 
   * @return The index of the first character which is covered by the set of
   *         characters defined by setLeadingCharactersToKeep()
   */
  protected int getIndexOfFirstLeadingCharacterToKeep(char[] array) {
    int i = 0;
    for (; i < array.length; i++) {
      if (this.leadingCharactersToKeep.contains(array[i])) {
        break;
      }
    }
    return i;
  }

  /**
   * Iterates through the given array, beginning at the end of the array, and
   * returns the index of the first character which is covered by the set of
   * characters defined by setTrailingCharactersToKeep().
   * 
   * @param array The char array to process.
   * 
   * @return The index of the first character which is covered by the set of
   *         characters defined by setTrailingCharactersToKeep()
   */
  protected int getIndexOfFirstTrailingCharacterToKeep(char[] array) {
    int i = array.length - 1;
    for (; i >= 0; i--) {
      if (this.trailingCharactersToKeep.contains(array[i])) {
        break;
      }
    }
    return i;
  }

  // ==============================================================================================

  /**
   * Sets the flag that indicates whether words should be transformed to lower
   * cases or not.
   * 
   * @param toLowerCase The flag to set.
   */
  public void setIsToLowerCase(boolean isToLowerCase) {
    this.isToLowerCase = isToLowerCase;
  }

  /**
   * Returns the flag that indicates whether words should be transformed to lower
   * cases or not.
   * 
   * @return True if words should be transformed to lower cases; False otherwise.
   */
  public boolean isToLowerCase() {
    return this.isToLowerCase;
  }

  // ==============================================================================================

  /**
   * Sets the characters to keep on removing leading characters. All leading
   * characters that are not covered by the given characters will be removed.
   * 
   * @param characters The characters to keep.
   */
  public void setLeadingCharactersToKeep(TCharSet... characters) {
    for (TCharSet c : characters) {
      this.leadingCharactersToKeep.addAll(c);
    }
  }

  /**
   * Sets the characters to keep on removing leading characters. All leading
   * characters that are not covered by the given characters will be removed.
   * 
   * @param characters The characters to keep.
   */
  public void setLeadingCharactersToKeep(char... characters) {
    this.leadingCharactersToKeep.addAll(characters);
  }

  /**
   * Returns the characters to keep on removing leading characters.
   * 
   * @return The set of characters to keep on removing leading characters.
   */
  public TCharSet getLeadingCharactersToKeep() {
    return this.leadingCharactersToKeep;
  }

  /**
   * Returns true if there are leading characters defined.
   * 
   * @return True if there are leading characters defined; False otherwise.
   */
  protected boolean hasLeadingCharactersToKeep() {
    return this.leadingCharactersToKeep != null
        && !this.leadingCharactersToKeep.isEmpty();
  }

  // ==============================================================================================

  /**
   * Sets the characters to keep on removing trailing characters. All trailing
   * characters that are not covered by the given characters will be removed.
   * 
   * @param characters The characters to keep.
   */
  public void setTrailingCharactersToKeep(TCharSet... characters) {
    for (TCharSet c : characters) {
      this.trailingCharactersToKeep.addAll(c);
    }
  }

  /**
   * Sets the characters to keep on removing trailing characters. All trailing
   * characters that are not covered by the given characters will be removed.
   * 
   * @param characters The characters to keep.
   */
  public void setTrailingCharactersToKeep(char... characters) {
    this.trailingCharactersToKeep.addAll(characters);
  }

  /**
   * Returns the characters to keep on removing trailing characters.
   * 
   * @return The set of characters to keep on removing trailing characters.
   */
  public TCharSet getTrailingCharactersToKeep() {
    return this.trailingCharactersToKeep;
  }

  /**
   * Returns true if there are trailing characters defined.
   * 
   * @return True if there are trailing characters defined; False otherwise.
   */
  protected boolean hasTrailingCharactersToKeep() {
    return this.trailingCharactersToKeep != null
        && !this.trailingCharactersToKeep.isEmpty();
  }
}
