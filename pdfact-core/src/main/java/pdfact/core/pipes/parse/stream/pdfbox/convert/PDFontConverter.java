package pdfact.core.pipes.parse.stream.pdfbox.convert;

import static pdfact.core.PdfActCoreSettings.AFM_FILE_FIELD_DELIMITER;
import static pdfact.core.PdfActCoreSettings.AFM_FILE_PATH;
import static pdfact.core.PdfActCoreSettings.DEFAULT_ENCODING;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import pdfact.core.model.Font;

/**
 * A converter that converts PDFont objects to {@link Font} objects.
 * 
 * @author Claudius Korzen
 */
public class PDFontConverter {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PDFontConverter.class);

  /**
   * A map of the already known fonts per name.
   */
  protected Map<String, Font> knownFonts;

  /**
   * Creates a new font converter.
   */
  public PDFontConverter() {
    this.knownFonts = readWellKnownFontsFromFile();
  }

  // ==============================================================================================

  /**
   * Converts the given PDFont object to a related {@link Font} object.
   * 
   * @param font
   *        The font to convert.
   * 
   * @return The converted font.
   */
  public Font convert(PDFont font) {

    if (font == null) {
      return null;
    }

    // Check if the font is already known.
    Font knownFont = getKnownFont(font);
    if (knownFont != null) {
      return knownFont;
    }

    // The font is not known. Create a new font.
    Font newFont = new Font();
    newFont.setId("font-" + this.knownFonts.size());
    newFont.setNormalizedName(computeNormalizedName(font));
    newFont.setBasename(computeBasename(newFont));
    newFont.setIsBold(computeIsBold(newFont));
    newFont.setIsItalic(computeIsItalic(newFont));
    newFont.setIsType3Font(computeIsType3Font(font));

    // Add the new font to the map of known fonts.
    this.knownFonts.put(newFont.getNormalizedName(), newFont);
    log.debug("A new font was registered: " + newFont);

    return newFont;
  }

  // ==============================================================================================

  /**
   * Reads some font specifications from file. This method was introduced to get
   * meta data about fonts like "cmr9", from which we can't derive from the font
   * name, if the font is bold (or italic).
   * 
   * @return The well-known fonts per name.
   */
  protected Map<String, Font> readWellKnownFontsFromFile() {
    Map<String, Font> knownFonts = new HashMap<>();

    // Read the AFM file that contains some metadata about common fonts.
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream afm = classLoader.getResourceAsStream(AFM_FILE_PATH);

    log.debug("Reading the AFM file '" + AFM_FILE_PATH + "'.");

    try (BufferedReader br =
        new BufferedReader(new InputStreamReader(afm, DEFAULT_ENCODING))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty()) {
          continue;
        }

        String[] fields = line.split(AFM_FILE_FIELD_DELIMITER);
        if (fields.length != 5) {
          continue;
        }

        // Create the font from the line.
        Font font = new Font();
        font.setId("font-" + knownFonts.size());
        font.setNormalizedName(fields[0]);
        font.setBasename(computeBasename(font));
        font.setFontFamilyName(fields[2].trim());
        font.setIsBold(fields[3].trim().equals("1"));
        font.setIsItalic(fields[4].trim().equals("1"));
        font.setIsType3Font(false);

        knownFonts.put(font.getNormalizedName(), font);
        log.trace("Read font: " + font);
      }
    } catch (IOException e) {
      log.warn("An error occurred on reading the AFM file.", e);
    }

    log.debug("Reading the AFM file done.");
    log.debug("# read fonts: " + knownFonts.size());
    return knownFonts;
  }

  /**
   * Checks if the given font is an already known font.
   * 
   * @param font
   *        The font to check.
   * 
   * @return True, if the given font is a known font; false otherwise.
   */
  protected boolean isKnownFont(PDFont font) {
    return getKnownFont(font) != null;
  }

  /**
   * Returns a {@link Font} object related to the given font if the font is
   * already known; null otherwise.
   * 
   * @param font
   *        The font to process.
   * 
   * @return A {@link Font} object related to the given font if the font is
   *         already known; null otherwise.
   */
  protected Font getKnownFont(PDFont font) {
    return this.knownFonts.get(computeNormalizedName(font));
  }

  // ==============================================================================================

  /**
   * Computes the normalized name of the given font, that is the lower cased
   * font name without the prefix (the part before "+"). For example, the
   * normalized name of "LTSLOS+NimbusSanL-Bold" is "nimbussanl-bold".
   * 
   * @param font
   *        The font to process.
   * 
   * @return The normalized name of the font.
   */
  protected String computeNormalizedName(PDFont font) {
    boolean isType3Font = computeIsType3Font(font);
    if (isType3Font) {
      return "type3";
    }

    String normalized = font.getName().toLowerCase();

    // Eliminate the prefix up to the "+".
    int indexPlus = normalized.indexOf("+");
    if (indexPlus > -1) {
      normalized = normalized.substring(indexPlus + 1);
    }

    return normalized;
  }

  // ==============================================================================================

  /**
   * Computes the basename of the given font, that is the normalized name
   * without the part after the "-" symbol. For example, the basename of
   * "LTSLOS+NimbusSanL-Bold" is "nimbussanl".
   * 
   * @param font
   *        The font to process.
   *
   * @return The basename of the given font.
   */
  public String computeBasename(Font font) {
    // Compute the basename from the name: "LTSLOS+NimbusSanL-Bold"
    String basename = font.getNormalizedName();

    // Eliminate trailing characters starting at the "-": nimbussanl
    int indexMinus = basename.indexOf("-");
    if (indexMinus > -1) {
      basename = basename.substring(0, indexMinus);
    }

    // Replace all other special characters and digits.
    return basename.replaceAll("[0-9\\-, ]", "");
  }

  /**
   * Checks if the given PDFont is a Type3 font.
   * 
   * @param font
   *        The font to check.
   * 
   * @return True, if the given font is a Type3 font, false otherwise.
   */
  public boolean computeIsType3Font(PDFont font) {
    return font instanceof PDType3Font;
  }

  /**
   * Checks if the given PDFont represents a bold-faced font.
   * 
   * @param font
   *        The font to check.
   * 
   * @return True, if the given font represents a bold-faced font, false
   *         otherwise.
   */
  public boolean computeIsBold(Font font) {
    String fontName = font.getNormalizedName();

    if (fontName != null) {
      int lengthBefore = fontName.length();
      fontName = fontName.replaceAll("bold", "").replaceAll("black", "");
      int lengthAfter = fontName.length();
      return lengthAfter < lengthBefore;
    }

    return false;
  }

  /**
   * Checks if the given PDFont represents an italic-faced font.
   * 
   * @param font
   *        The font to check.
   * @return True, if the given font represents an italic-faced font.
   */
  public boolean computeIsItalic(Font font) {
    String fontName = font.getNormalizedName();

    if (fontName != null) {
      int lengthBefore = fontName.length();
      fontName = fontName.replaceAll("ital", "").replaceAll("oblique", "");
      int lengthAfter = fontName.length();
      return lengthAfter < lengthBefore;
    }

    return false;
  }
}
