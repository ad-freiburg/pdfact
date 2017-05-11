package icecite.parser.stream.pdfbox.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;

// TODO: Check if this works as suggested.

/**
 * A collection of some utility methods to manage and process additional
 * glyphs.
 * 
 * @author Claudius Korzen
 */
public class PdfBoxGlyphUtils {
  /**
   * The additional glyphs.
   */
  protected static GlyphList additionalGlyphs;

  static {
    // TODO: Don't hardcode the paths.
    additionalGlyphs = readAdditionalGlyphs(
        "org/apache/pdfbox/resources/glyphlist/additional.txt");
  }

  /**
   * Returns a list of additional glyphs.
   * 
   * @return A list of additional glyphs.
   */
  public static GlyphList getAdditionalGlyphs() {
    return additionalGlyphs;
  }

  /**
   * Reads additional glyphs from file.
   * 
   * @param path
   *        The path to the file to read from.
   * 
   * @return An instance of GlyphList that included the additional glyphs.
   * 
   * @TODO: Further investigation what this method actually does.
   * @TODO: Check, if this method works properly.
   */
  protected static GlyphList readAdditionalGlyphs(String path) {
    ClassLoader classLoader = GlyphList.class.getClassLoader();
    try (InputStream input = classLoader.getResourceAsStream(path)) {
      try {
        return new GlyphList(GlyphList.getAdobeGlyphList(), input);
      } catch (IOException e) {
        System.out.println("Couldn't instantiate glyph list.");
      }
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return null;
  }
}
