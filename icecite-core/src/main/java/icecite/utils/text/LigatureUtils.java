package icecite.utils.text;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of utility methods that deal with ligatures.
 * 
 * @author Claudius Korzen
 */
public class LigatureUtils {
  /**
   * Maps the unicodes of ligatures to its individual characters.
   */
  protected static final Map<String, String[]> LIGATURES;

  static {
    // Fill the ligatures DIACRITICS.
    LIGATURES = new HashMap<String, String[]>();
    LIGATURES.put("\u00C6", new String[] { "A", "E" }); // AE
    LIGATURES.put("\u00E6", new String[] { "a", "e" }); // ae
    LIGATURES.put("\u0152", new String[] { "O", "E" }); // OE
    LIGATURES.put("\u0153", new String[] { "o", "e" }); // oe
    LIGATURES.put("\u0132", new String[] { "I", "J" }); // IJ
    LIGATURES.put("\u0133", new String[] { "i", "j" }); // ij
    LIGATURES.put("\u1D6B", new String[] { "u", "e" }); // ue
    LIGATURES.put("\uA728", new String[] { "T", "Z" }); // TZ
    LIGATURES.put("\uA729", new String[] { "t", "z" }); // tz
    LIGATURES.put("\uA732", new String[] { "A", "A" }); // AA
    LIGATURES.put("\uA733", new String[] { "a", "a" }); // aa
    LIGATURES.put("\uA734", new String[] { "A", "O" }); // AO
    LIGATURES.put("\uA735", new String[] { "a", "o" }); // ao
    LIGATURES.put("\uA736", new String[] { "A", "U" }); // AU
    LIGATURES.put("\uA737", new String[] { "a", "u" }); // au
    LIGATURES.put("\uA738", new String[] { "A", "V" }); // AV
    LIGATURES.put("\uA739", new String[] { "a", "v" }); // av
    LIGATURES.put("\uA73C", new String[] { "A", "Y" }); // AY
    LIGATURES.put("\uA73D", new String[] { "a", "y" }); // ay
    LIGATURES.put("\uA74E", new String[] { "O", "O" }); // OO
    LIGATURES.put("\uA74F", new String[] { "o", "o" }); // oo
    LIGATURES.put("\uAB50", new String[] { "u", "i" }); // ui
    LIGATURES.put("\uFB00", new String[] { "f", "f" }); // ff
    LIGATURES.put("\uFB01", new String[] { "f", "i" }); // fi
    LIGATURES.put("\uFB02", new String[] { "f", "l" }); // fl
    LIGATURES.put("\uFB03", new String[] { "f", "f", "i" }); // ffi
    LIGATURES.put("\uFB04", new String[] { "f", "f", "l" }); // ffl
    LIGATURES.put("\uFB06", new String[] { "s", "t" }); // st
  }

  /**
   * Resolves the given unicode string to its individual characters if the
   * string is a ligature. Returns null, if the string isn't a ligature.
   * 
   * @param unicode
   *        The unicode string to check.
   * 
   * @return The resolved characters in a string array if the input is a
   *         ligature; null otherwise.
   */
  public static String[] resolveLigature(String unicode) {
    return LIGATURES.get(unicode);
  }
}
