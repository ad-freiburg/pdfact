package icecite.utils.textlines;

import icecite.models.PdfCharacterList;
import icecite.models.PdfFont;
import icecite.models.PdfTextLine;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.Rectangle;

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

  /**
   * Checks if the given line is indented, compared to the given reference
   * line.
   * 
   * @param line
   *        The text line to process.
   * @param refLine
   *        The reference text line.
   * 
   * @return True, if the given line is indented compared to the given
   *         reference line.
   */
  public static boolean isIndented(PdfTextLine line, PdfTextLine refLine) {
    if (line == null || refLine == null) {
      return false;
    }

    Rectangle rectangle = line.getRectangle();
    Rectangle referenceRectangle = refLine.getRectangle();
    if (rectangle == null || referenceRectangle == null) {
      return false;
    }

    // TODO
    return rectangle.getMinX() - referenceRectangle.getMinX() > 1;
  }

  /**
   * Checks if the line pitches between the line / previous line and the line /
   * next line are equal.
   * 
   * @param prevLine
   *        The previous text line.
   * @param line
   *        The text line to process.
   * @param nextLine
   *        The next text line.
   * 
   * @return True, if the line pitches between the line / previous line and the
   *         line / next line are equal, false otherwise.
   */
  public static boolean isLinepitchesEqual(PdfTextLine prevLine,
      PdfTextLine line, PdfTextLine nextLine) {
    float prevLinePitch = computeLinePitch(prevLine, line);
    float nextLinePitch = computeLinePitch(line, nextLine);
    // TODO
    return Math.abs(prevLinePitch - nextLinePitch) < 1;
  }

  /**
   * Checks, if the minX values for the given lines are equal.
   * 
   * @param line1
   *        The first text line.
   * @param line2
   *        The second text line-
   * @return True, if the minX values for the given lines are equal, false
   *         otherwise.
   */
  public static boolean isMinXEqual(PdfTextLine line1, PdfTextLine line2) {
    if (line1 == null || line2 == null) {
      return false;
    }
    
    Rectangle rectangle1 = line1.getRectangle();
    Rectangle rectangle2 = line2.getRectangle();
    if (rectangle1 == null || rectangle2 == null) {
      return false;
    }
    
    // TODO
    return Math.abs(rectangle1.getMinX() - rectangle2.getMinX()) < 1;
  }
}
