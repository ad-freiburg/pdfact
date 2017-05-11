package icecite.models.plain;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfColor;
import icecite.models.PdfFont;
import icecite.models.PdfParagraph;
import icecite.models.PdfTextLine;

/**
 * A plain implementation of {@link PdfParagraph}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraph extends PlainPdfElement implements PdfParagraph {
  /**
   * The characters of this page.
   */
  protected PdfCharacterSet characters;

  /**
   * The text lines of this paragraph.
   */
  protected List<PdfTextLine> textLines;

  /**
   * The text of this paragraph.
   */
  protected String text;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new paragraph.
   * 
   * @param textLines
   *        The text lines of this paragraph.
   */
  @AssistedInject
  public PlainPdfParagraph(@Assisted List<PdfTextLine> textLines) {
    this.textLines = textLines;
  }

  // ==========================================================================

  @Override
  public PdfCharacterSet getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(PdfCharacterSet characters) {
    this.characters = characters;
  }

  // ==========================================================================

  @Override
  public List<PdfTextLine> getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(List<PdfTextLine> textLines) {
    this.textLines = textLines;
  }

  @Override
  public void addTextLine(PdfTextLine textLine) {
    this.textLines.add(textLine);
  }

  // ==========================================================================

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

  // ==========================================================================

  @Override
  public PdfFont getMostCommonFont() {
    return this.characters.getMostCommonFont();
  }

  @Override
  public PdfColor getMostCommonColor() {
    return this.characters.getMostCommonColor();
  }

  @Override
  public float getMostCommonFontsize() {
    return this.characters.getMostCommonFontsize();
  }

  @Override
  public float getAverageFontsize() {
    return this.characters.getAverageFontsize();
  }

  @Override
  public float getMostCommonHeight() {
    return this.characters.getMostCommonHeight();
  }

  @Override
  public float getAverageHeight() {
    return this.characters.getAverageHeight();
  }

  @Override
  public float getMostCommonWidth() {
    return this.characters.getMostCommonWidth();
  }

  @Override
  public float getAverageWidth() {
    return this.characters.getAverageWidth();
  }
}
