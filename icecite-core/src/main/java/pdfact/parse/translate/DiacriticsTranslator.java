package pdfact.parse.translate;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import pdfact.models.PdfCharacter;
import pdfact.utils.geometric.Rectangle;

// FIXME: Adjust the bounding box of an character on merging diacritics.

/**
 * A class that merges diacritic marks with the related base characters.
 * 
 * @author Claudius Korzen
 */
public class DiacriticsTranslator {
  /**
   * Checks if the given character is a diacritic, chooses the belonging base
   * character (either the given character to the left or the given character
   * to the right of the diacritic) and merges the diacritic with this base
   * character.
   * 
   * @param diacritic
   *        The diacritic in question.
   * @param leftChar
   *        The character to the left of the diacritic.
   * @param rightChar
   *        The character to the right of the diacritic.
   */
  public static void translate(PdfCharacter diacritic,
      PdfCharacter leftChar, PdfCharacter rightChar) {
    // Don't proceed if the character in question is not a diacritic.
    if (!isDiacritic(diacritic)) {
      return;
    }

    // Get the bounding box of the diacritic.
    Rectangle diacriticBoundingBox = diacritic.getRectangle();
    if (diacriticBoundingBox == null) {
      return;
    }

    // Choose the belonging base character:
    // (1) Compute the horizontal overlap with the left character.
    float leftOverlap = 0;
    if (leftChar != null) {
      leftOverlap = diacriticBoundingBox
          .computeHorizontalOverlapLength(leftChar.getRectangle());
    }

    // (2) Compute the horizontal overlap with the right character.
    float rightOverlap = 0;
    if (rightChar != null) {
      rightOverlap = diacriticBoundingBox
          .computeHorizontalOverlapLength(rightChar.getRectangle());
    }

    // Merge the diacritic to the base character with the largest overlap.
    if (leftOverlap > 0 && leftOverlap >= rightOverlap) {
      mergeDiacritic(diacritic, leftChar);
    } else
      if (rightOverlap > 0 && rightOverlap > leftOverlap) {
        mergeDiacritic(diacritic, rightChar);
      }
  }

  /**
   * Checks if the given characters is a diacritic.
   * 
   * @param character
   *        The character to check.
   * @return True, if the given character is a diacritic; false otherwise.
   */
  public static boolean isDiacritic(PdfCharacter character) {
    if (character == null) {
      return false;
    }

    String text = character.getText();
    if (text == null || text.length() != 1) {
      return false;
    }

    int type = Character.getType(text.charAt(0));
    return type == Character.NON_SPACING_MARK
        || type == Character.MODIFIER_SYMBOL
        || type == Character.MODIFIER_LETTER;
  }

  /**
   * Merges the given diacritic with the given base character.
   * 
   * @param diacritic
   *        The diacritic to merge with the base character.
   * @param baseCharacter
   *        The base character to merge with the diacritic.
   */
  protected static void mergeDiacritic(PdfCharacter diacritic,
      PdfCharacter baseCharacter) {
    if (diacritic == null || baseCharacter == null) {
      return;
    }

    String diacriticText = diacritic.getText();
    String baseText = baseCharacter.getText();
    if (diacriticText == null || baseText == null) {
      return;
    }

    // TODO
    if (DIACRITICS.containsKey(diacriticText.codePointAt(0))) {
      diacriticText = DIACRITICS.get(diacriticText.codePointAt(0));
    }

    // Merge the diacritic with the base character.
    String mergedText = Normalizer
        .normalize(baseText + diacriticText, Normalizer.Form.NFC).trim();

    // Adjust the text of the base character.
    baseCharacter.setText(mergedText);
  }

  // ==========================================================================

  /**
   * Adds non-decomposing diacritics to the hash with their related combining
   * character. These are values that the unicode spec claims are equivalent
   * but are not mapped in the form NFKC normalization method. Determined by
   * going through the Combining Diacritical Marks section of the Unicode spec
   * and identifying which characters are not mapped to by the normalization.
   * For example, maps "ACUTE ACCENT" to "COMBINING ACUTE ACCENT".
   */
  static final Map<Integer, String> DIACRITICS;

  static {
    DIACRITICS = new HashMap<Integer, String>(31);
    DIACRITICS.put(0x0060, "\u0300");
    DIACRITICS.put(0x02CB, "\u0300");
    DIACRITICS.put(0x0027, "\u0301");
    DIACRITICS.put(0x00B4, "\u0301");
    DIACRITICS.put(0x02B9, "\u0301");
    DIACRITICS.put(0x02CA, "\u0301");
    DIACRITICS.put(0x0384, "\u0301");
    DIACRITICS.put(0x005E, "\u0302");
    DIACRITICS.put(0x02C6, "\u0302");
    DIACRITICS.put(0x007E, "\u0303");
    DIACRITICS.put(0x02DC, "\u0303");
    DIACRITICS.put(0x00AF, "\u0304");
    DIACRITICS.put(0x02C9, "\u0304");
    DIACRITICS.put(0x00A8, "\u0308");
    DIACRITICS.put(0x00B0, "\u030A");
    DIACRITICS.put(0x02DA, "\u030A");
    DIACRITICS.put(0x0022, "\u030B");
    DIACRITICS.put(0x02BA, "\u030B");
    DIACRITICS.put(0x02DD, "\u030B");
    DIACRITICS.put(0x02C7, "\u030C");
    DIACRITICS.put(0x02C8, "\u030D");
    DIACRITICS.put(0x02BB, "\u0312");
    DIACRITICS.put(0x02BC, "\u0313");
    DIACRITICS.put(0x0486, "\u0313");
    DIACRITICS.put(0x055A, "\u0313");
    DIACRITICS.put(0x02BD, "\u0314");
    DIACRITICS.put(0x0485, "\u0314");
    DIACRITICS.put(0x0559, "\u0314");
    DIACRITICS.put(0x02D4, "\u031D");
    DIACRITICS.put(0x02D5, "\u031E");
    DIACRITICS.put(0x02D6, "\u031F");
    DIACRITICS.put(0x02D7, "\u0320");
    DIACRITICS.put(0x02B2, "\u0321");
    DIACRITICS.put(0x02CC, "\u0329");
    DIACRITICS.put(0x02B7, "\u032B");
    DIACRITICS.put(0x02CD, "\u0331");
    DIACRITICS.put(0x005F, "\u0332");
    DIACRITICS.put(0x204E, "\u0359");
  }
}
