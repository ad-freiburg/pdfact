package pdfact.core.util.comparator;

import java.io.Serializable;
import java.util.Comparator;
import pdfact.core.model.Font;
import pdfact.core.model.FontFace;

/**
 * A comparator that compares two font faces A and B as follows: 
 *   - A and B are equal when
 *     (1) they have the exact same font size, and
 *     (2) they are equally emphasized (e.g., both have italic and/or bold fonts).
 *   - A is larger than B when
 *     (1) the font size of A is larger than the fontsize of B.
 *     (2) A is more emphasized than B (where normal < italic < bold).
 * 
 * @author Claudius Korzen
 */
public class FontFaceComparator implements Comparator<FontFace>, Serializable {
  /**
   * The serial id.
   */
  private static final long serialVersionUID = 9023567620988519833L;

  @Override
  public int compare(FontFace element1, FontFace element2) {
    if (element1 == null && element2 == null) {
      return 0;
    }
    if (element1 == null) {
      return 1;
    }
    if (element2 == null) {
      return -1;
    }

    float fontsize1 = element1.getFontSize();
    float fontsize2 = element2.getFontSize();
    if (fontsize1 < fontsize2) {
      return -1;
    }
    if (fontsize1 > fontsize2) {
      return 1;
    }
    
    Font font1 = element1.getFont();
    Font font2 = element2.getFont();
    if (font1 == null && font2 == null) {
      return 0;
    }
    if (font1 == null) {
      return 1;
    }
    if (font2 == null) {
      return -1;
    }

    int emph1 = 0;
    if (font1.isItalic()) { emph1 += 1; }
    if (font1.isBold()) { emph1 += 2; }
    
    int emph2 = 0;
    if (font2.isItalic()) { emph2 += 1; }
    if (font2.isBold()) { emph2 += 2; }

    return emph1 - emph2;
  }
}