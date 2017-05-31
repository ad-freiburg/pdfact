package icecite.utils.font;

import java.util.HashMap;
import java.util.Map;

/**
 * A dictionary that maps glyph names to its unicode string. This dictionary
 * was introduced because in case of embedded fonts, there could be glyphs
 * (codes) that were redefined with a custom shape. For example, in paper
 * "cond-mat0001220", the glyph "ǫ" is redefined by "ϵ". But we actually know
 * the path to draw the "ϵ", but we don't now its semantic meaning.
 * Nevertheless, in most of the cases we have a name for the glyph, given from
 * font descriptor, from which we may derive the glyph to show (with the help
 * of this dictionary).
 * 
 * @author Claudius Korzen
 *
 */
public class PdfGlyphDictionary {
  /** The dictionary. */
  static final Map<String, String> DICT = new HashMap<>();

  static {
    DICT.put("Alpha", "Α");
    DICT.put("Beta", "Β");
    DICT.put("Gamma", "Γ");
    DICT.put("Delta", "Δ");
    DICT.put("Epsilon", "Ε");
    DICT.put("Zeta", "Ζ");
    DICT.put("Eta", "Η");
    DICT.put("Theta", "Θ");
    DICT.put("Iota", "Ι");
    DICT.put("Kappa", "Κ");
    DICT.put("Lambda", "Λ");
    DICT.put("Mu", "Μ");
    DICT.put("Nu", "Ν");
    DICT.put("Xi", "Ξ");
    DICT.put("Omicron", "Ο");
    DICT.put("Pi", "Π");
    DICT.put("Rho", "Ρ");
    DICT.put("Sigma", "Σ");
    DICT.put("Tau", "Τ");
    DICT.put("Upsilon", "Υ");
    DICT.put("Phi", "Φ");
    DICT.put("Chi", "Χ");
    DICT.put("Psi", "Ψ");
    DICT.put("Omega", "Ω");
    DICT.put("alpha", "α");
    DICT.put("beta", "β");
    DICT.put("gamma", "γ");
    DICT.put("delta", "δ");
    DICT.put("epsilon", "ε");
    DICT.put("zeta", "ζ");
    DICT.put("eta", "η");
    DICT.put("theta", "θ");
    DICT.put("iota", "ι");
    DICT.put("kappa", "κ");
    DICT.put("lambda", "λ");
    DICT.put("mu", "μ");
    DICT.put("nu", "ν");
    DICT.put("xi", "ξ");
    DICT.put("omicron", "ο");
    DICT.put("pi", "π");
    DICT.put("rho", "ρ");
    DICT.put("sigma", "σ");
    DICT.put("tau", "τ");
    DICT.put("upsilon", "υ");
    DICT.put("phi", "φ");
    DICT.put("chi", "χ");
    DICT.put("psi", "ψ");
    DICT.put("omega", "ω");
    DICT.put("partial", "∂");
    DICT.put("infty", "∞");
  }

  /**
   * Returns true, if this dictionary contains a glyph for the given name.
   * 
   * @param name
   *        The name of the glyph.
   * @return True, if this dictionary contains a glyph for the given name,
   *         false otherwise.
   */
  public static boolean hasGlyphForName(String name) {
    return getGlyphForName(name) != null;
  }

  /**
   * Returns the glyph associated with the given name or null if there is no
   * such glyph.
   * 
   * @param name
   *        The name of the glyph.
   * @return True, if this dictionary contains a glyph for the given name,
   *         false otherwise.
   */
  public static String getGlyphForName(String name) {
    if (DICT.containsKey(name)) {
      return DICT.get(name);
    }

    // From time to time, names are followed by "1", e.g. "epsilon1".
    // Remove all numbers from name and check, if the dict contains this name.
    String normalized = name.replaceAll("[0-9]", "");
    return DICT.get(normalized);
  }
}
