package pdfact.core.pipes.parse.stream.pdfbox.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;

import pdfact.core.util.log.InjectLogger;

/**
 * A collection of some utility methods to manage and process additional glyphs.
 * 
 * @author Claudius Korzen
 */
public class PdfBoxGlyphUtils {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The additional glyphs.
   */
  protected final GlyphList additionalGlyphs;

  /**
   * A utility class to read the specifications of special characters.
   */
  public PdfBoxGlyphUtils() {
    this.additionalGlyphs = readAdditionalGlyphs(
        "org/apache/pdfbox/resources/glyphlist/additional.txt");
  }

  // ==========================================================================

  /**
   * Returns a list of additional glyphs.
   * 
   * @return A list of additional glyphs.
   */
  public GlyphList getAdditionalGlyphs() {
    return this.additionalGlyphs;
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
    log.debug("Reading additional glyphs from path '" + path + "'.");

    GlyphList glyphList = null;
    ClassLoader classLoader = GlyphList.class.getClassLoader();
    try (InputStream in = classLoader.getResourceAsStream(path)) {
      glyphList = new GlyphList(GlyphList.getAdobeGlyphList(), in);
    } catch (IOException e) {
      log.warn("An error occurred while reading additional glyphs.", e);
    }

    log.debug("Reading additional glyphs from path done.");
    return glyphList;
  }
}
