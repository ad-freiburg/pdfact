package icecite.parser.translators;

import java.util.HashMap;
import java.util.Map;

import icecite.models.PdfCharacter;

/**
 * A class that translates ligatures into the individual characters.
 * 
 * @author Claudius Korzen
 */
public class LigaturesTranslator {
  /**
   * A map with the unicodes of ligatures and its individual characters.
   */
  protected static final Map<String, String> LIGATURES;

  static {
    // Fill the ligatures DIACRITICS.
    LIGATURES = new HashMap<String, String>();
    LIGATURES.put("\u00C6", "AE");
    LIGATURES.put("\u00E6", "ae");
    LIGATURES.put("\u0152", "OE");
    LIGATURES.put("\u0153", "oe");
    LIGATURES.put("\u0132", "IJ");
    LIGATURES.put("\u0133", "ij");
    LIGATURES.put("\u1D6B", "ue");
    LIGATURES.put("\uA728", "TZ");
    LIGATURES.put("\uA729", "tz");
    LIGATURES.put("\uA732", "AA");
    LIGATURES.put("\uA733", "aa");
    LIGATURES.put("\uA734", "AO");
    LIGATURES.put("\uA735", "ao");
    LIGATURES.put("\uA736", "AU");
    LIGATURES.put("\uA737", "au");
    LIGATURES.put("\uA738", "AV");
    LIGATURES.put("\uA739", "av");
    LIGATURES.put("\uA73C", "AY");
    LIGATURES.put("\uA73D", "ay");
    LIGATURES.put("\uA74E", "OO");
    LIGATURES.put("\uA74F", "oo");
    LIGATURES.put("\uAB50", "ui");
    LIGATURES.put("\uFB00", "ff");
    LIGATURES.put("\uFB01", "fi");
    LIGATURES.put("\uFB02", "fl");
    LIGATURES.put("\uFB03", "ffi");
    LIGATURES.put("\uFB04", "ffl");
    LIGATURES.put("\uFB06", "st");
  }

  /**
   * Checks if the given character is a ligature and if so, translates it into
   * its individual characters and changes the textual content of the given
   * character accordingly. Does nothing if the characters is not a ligature.
   * 
   * @param character
   *        The PDF character to process.
   */
  public static void resolveLigature(PdfCharacter character) {
    if (isLigature(character)) {
      character.setText(getResolvedLigatureText(character));
    }
  }

  /**
   * Returns true if the given character is a ligature; false otherwise.
   * 
   * @param character
   *        The PDF character to check.
   * @return true if the given character is a ligature; false otherwise.
   */
  public static boolean isLigature(PdfCharacter character) {
    return character != null && LIGATURES.containsKey(character.getText());
  }

  /**
   * Returns the text consisting of the individual characters of the given
   * ligature. Returns null if the given character is not a ligature.
   * 
   * @param character
   *        The PDF character to process.
   * @return the text consisting of the individual characters of the given
   *         ligature or null if the given character is not a ligature.
   */
  public static String getResolvedLigatureText(PdfCharacter character) {
    return LIGATURES.get(character.getText());
  }
}
