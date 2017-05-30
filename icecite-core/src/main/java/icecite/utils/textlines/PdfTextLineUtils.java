package icecite.utils.textlines;

import icecite.models.PdfCharacterList;
import icecite.models.PdfFont;
import icecite.models.PdfTextLine;
import icecite.utils.geometric.Line;

/**
 * A collection of utility methods that deal with PDF text lines.
 * 
 * @author Claudius Korzen
 */
public class PdfTextLineUtils {
  /**
   * Computes the line pitch between the given lines. Both lines must share the
   * same page.
   * 
   * @param firstLine
   *        The first text line.
   * @param secondLine
   *        The second text line.
   * @return The line pitch between the given lines or Float.NaN if the lines
   *         do not share the same page.
   */
  public static float computeLinePitch(PdfTextLine firstLine,
      PdfTextLine secondLine) {
    if (firstLine == null || secondLine == null) {
      return Float.NaN;
    }

    if (firstLine.getPage() != secondLine.getPage()) {
      return Float.NaN;
    }

    Line firstBaseLine = firstLine.getBaseline();
    Line secondBaseLine = secondLine.getBaseline();
    if (firstBaseLine == null || secondBaseLine == null) {
      return Float.NaN;
    }

    return Math.abs(firstBaseLine.getStartY() - secondBaseLine.getStartY());
  }

  /**
   * Computes the font face of the given line, that is a string containing the
   * font name and the font size.
   * 
   * @param line
   *        The text line to process.
   * 
   * @return The font face of the given line.
   */
  public static String computeFontFace(PdfTextLine line) {
    if (line == null) {
      return null;
    }

    PdfCharacterList characters = line.getCharacters();
    if (characters == null) {
      return null;
    }

    PdfFont font = characters.getMostCommonFont();
    if (font == null) {
      return null;
    }

    String fontName = font.getBaseName();
    if (fontName == null) {
      return null;
    }

    float fontsize = characters.getMostCommonFontsize();

    return fontName + "" + fontsize;
  }
}
