package parser.pdfbox.util;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;

/**
 * Collection of util methods regarding fonts. 
 *
 * @author Claudius Korzen
 *
 */
public class PdfFontUtil {
  /**
   * Computes the normalized fontname of the font. This is a string with only
   * the fontname and no additional style informations (like "bold" or
   * "italic").
   * 
   * @param font
   *          the font to analyze.
   * @return the derived font name.
   */
  public static String computeBasename(PDFont font) {
    // Terminate all whitespaces, commas and hyphens
    String fontname = null;
    
    if (font != null) {
      if (font instanceof PDType3Font) {
        fontname = "type3";
      } else {
        fontname = font.getName().toLowerCase();
        
        // Terminate trailing characters up to the "+".
        // As far as I know, these characters are used in names of embedded
        // fonts. If the embedded font can't be read, we'll try to find it here.
        if (fontname.indexOf("+") > -1) {
          fontname = fontname.substring(fontname.indexOf("+") + 1);
        }
      }
    }

    return fontname;
  }
}
