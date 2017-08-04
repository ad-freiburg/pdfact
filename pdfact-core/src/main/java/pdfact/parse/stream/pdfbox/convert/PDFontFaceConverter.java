package pdfact.parse.stream.pdfbox.convert;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import pdfact.models.PdfFont;
import pdfact.models.PdfFontFace;
import pdfact.models.PdfFontFace.PdfFontFaceFactory;

/**
 * A converter that converts PDFont objects and font size to PdfFontFace
 * objects.
 * 
 * @author Claudius Korzen
 */
public class PDFontFaceConverter {
  /**
   * The factory to create instances of {@link PdfFontFace}.
   */
  protected PdfFontFaceFactory fontFaceFactory;

  /**
   * The already known PdfFont objects per name.
   */
  protected Map<String, PdfFontFace> knownFontFaces;

  // ==========================================================================
  // The constructors.

  /**
   * Creates a new PDFontFaceConverter.
   * 
   * @param fontFaceFactory
   *        The factory to create instances of {@link PdfFontFace}.
   */
  @Inject
  public PDFontFaceConverter(PdfFontFaceFactory fontFaceFactory) {
    this.knownFontFaces = new HashMap<>();
    this.fontFaceFactory = fontFaceFactory;
  }

  // ==========================================================================

  /**
   * Converts the given PdfFont object and font size to an PdfFontFace object.
   * 
   * @param font
   *        The font.
   * @param fontSize
   *        The font size.
   * 
   * @return An instance of {@link PdfFontFace}.
   */
  public PdfFontFace convert(PdfFont font, float fontSize) {
    if (font == null) {
      return null;
    }

    String key = font.getId() + ":" + fontSize;
    if (this.knownFontFaces.containsKey(key)) {
      return this.knownFontFaces.get(key);
    }

    PdfFontFace fontFace = this.fontFaceFactory.create(font, fontSize);
    this.knownFontFaces.put(key, fontFace);

    return fontFace;
  }
}
