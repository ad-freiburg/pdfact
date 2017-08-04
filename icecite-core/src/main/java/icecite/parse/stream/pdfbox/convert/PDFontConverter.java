package icecite.parse.stream.pdfbox.convert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;

import com.google.inject.Inject;

import icecite.models.PdfFont;
import icecite.models.PdfFont.PdfFontFactory;

/**
 * A converter that converts PDFont objects to PdfFont objects.
 * 
 * @author Claudius Korzen
 */
public class PDFontConverter {
  /**
   * The factory to create instances of {@link PdfFont}.
   */
  protected PdfFontFactory fontFactory;

  /**
   * The already known fonts per name.
   */
  protected Map<String, PdfFont> knownFonts;

  // ==========================================================================
  // The constructors.

  /**
   * Creates a new PDFontConverter.
   * 
   * @param fontFactory
   *        The factory to create instances of {@link PdfFont}.
   */
  @Inject
  public PDFontConverter(PdfFontFactory fontFactory) {
    this.fontFactory = fontFactory;
    this.knownFonts = readWellKnownFontsFromFile();
  }

  // ==========================================================================

  /**
   * Converts the given PDFont object to a related PdfFont object.
   * 
   * @param font
   *        The font to convert.
   * 
   * @return The converted font.
   */
  public PdfFont convert(PDFont font) {
    // Check if the font is already known.
    PdfFont wellKnownFont = getKnownFont(font);
    if (wellKnownFont != null) {
      return wellKnownFont;
    }

    // The font is not known. Create a new font.
    PdfFont newFont = this.fontFactory.create();
    newFont.setId("font" + this.knownFonts.size());
    newFont.setNormalizedName(computeNormalizedFontName(font));
    newFont.setBaseName(computeBasename(newFont));
    newFont.setIsBold(computeIsBold(newFont));
    newFont.setIsItalic(computeIsItalic(newFont));
    newFont.setIsType3Font(computeIsType3Font(font));

    this.knownFonts.put(newFont.getNormalizedName(), newFont);

    return newFont;
  }

  // ==========================================================================

  /**
   * Reads some font specifications from file. This method was introduced to
   * get meta data about fonts like "cmr9", from which we can't derive from the
   * font name, if the font is bold (or italic).
   * 
   * @return The well-known fonts per name.
   */
  protected Map<String, PdfFont> readWellKnownFontsFromFile() {
    Map<String, PdfFont> wellKnownFonts = new HashMap<>();

    // Obtain the path to the afm.map file.
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream is = classLoader.getResourceAsStream("afm.map");

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(is, "UTF-8"))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty()) {
          continue;
        }

        String[] fields = line.split("\t");
        if (fields.length != 5) {
          continue;
        }

        // Create the font from the line.
        PdfFont font = this.fontFactory.create();
        font.setId("font" + wellKnownFonts.size());
        font.setNormalizedName(fields[0]);
        font.setBaseName(computeBasename(font));
        font.setFontFamilyName(fields[2].trim());
        font.setIsBold(fields[3].trim().equals("1"));
        font.setIsItalic(fields[4].trim().equals("1"));
        font.setIsType3Font(false);

        wellKnownFonts.put(font.getNormalizedName(), font);
      }
    } catch (IOException e) {
      // Nothing to do so far. TODO: Log message.
    }
    return wellKnownFonts;
  }

  /**
   * Returns true if the given font is a known font.
   * 
   * @param font
   *        The font to check.
   * @return True, if the given font is a known font, false otherwise.
   */
  protected boolean isKnownFont(PDFont font) {
    return getKnownFont(font) != null;
  }

  /**
   * Returns true if the given font is a known font.
   * 
   * @param font
   *        The font to process.
   * @return True, if the given font is a known font, false otherwise.
   */
  protected PdfFont getKnownFont(PDFont font) {
    return this.knownFonts.get(computeNormalizedFontName(font));
  }

  // ==========================================================================

  /**
   * Computes the normalized name of the given font, that is the lower cased
   * font name without the prefix (the part before "+"). For example, the
   * normalized name of "LTSLOS+NimbusSanL-Bold" is "nimbussanl-bold".
   * 
   * @param font
   *        The font to convert.
   * @return The converted font.
   */
  protected String computeNormalizedFontName(PDFont font) {
    if (computeIsType3Font(font)) {
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

  // ==========================================================================

  /**
   * Computes the basename of the given PDFont. For example, for the font with
   * the name "LTSLOS+NimbusSanL-Bold", this method returns the string
   * "nimbussanl".
   * 
   * @param font
   *        The font to process.
   *
   * @return The basename of the given font.
   */
  public String computeBasename(PdfFont font) {
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
   * @return True, if the given font is a Type3 font, false otherwise.
   */
  public boolean computeIsType3Font(PDFont font) {
    return font instanceof PDType3Font;
  }

  /**
   * Checks if the given PDFont represents a font with a bold font face.
   * 
   * @param font
   *        The font to check.
   * @return True, if the given font represents a font with a bold font face,
   *         false otherwise.
   */
  public boolean computeIsBold(PdfFont font) {
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
   * Checks if the given PDFont represents a font with an italic font face.
   * 
   * @param font
   *        The font to check.
   * @return True, if the given font represents a font with an italic font
   *         face, false otherwise.
   */
  public boolean computeIsItalic(PdfFont font) {
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
