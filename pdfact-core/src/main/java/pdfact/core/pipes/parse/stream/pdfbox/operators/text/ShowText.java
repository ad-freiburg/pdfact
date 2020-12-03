package pdfact.core.pipes.parse.stream.pdfbox.operators.text;

import static pdfact.core.PdfActCoreSettings.FLOATING_NUMBER_PRECISION;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.fontbox.afm.CharMetric;
import org.apache.fontbox.cff.CFFType1Font;
import org.apache.fontbox.cff.Type1CharString;
import org.apache.fontbox.type1.Type1Font;
import org.apache.fontbox.util.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontFactory;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import pdfact.core.model.Character;
import pdfact.core.model.Color;
import pdfact.core.model.Document;
import pdfact.core.model.Font;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.Point;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.pipes.parse.stream.pdfbox.convert.PDFontConverter;
import pdfact.core.pipes.parse.stream.pdfbox.convert.PDFontFaceConverter;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;
import pdfact.core.pipes.parse.stream.pdfbox.utils.PdfBoxAFMUtils;
import pdfact.core.pipes.parse.stream.pdfbox.utils.PdfBoxGlyphUtils;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.color.ColorManager;

/**
 * Tj: Show a text string.
 * 
 * @author Claudius Korzen
 */
public class ShowText extends OperatorProcessor {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(ShowText.class);

  /**
   * The translator to translate PDFont objects to PdfFont objects.
   */
  protected PDFontConverter fontTranslator;

  /**
   * The converter to create instances of {@link FontFace}.
   */
  protected PDFontFaceConverter fontFaceConverter;

  /**
   * The util to read the specifications of special glyphs.
   */
  protected PdfBoxGlyphUtils glyphUtils;

  /**
   * The number of already processed characters (needed to define the sequence number of a
   * character).
   */
  protected int sequenceNumber;

  /**
   * Creates a new OperatorProcessor to process the operation "ShowText".
   */
  public ShowText() {
    this.fontTranslator = new PDFontConverter();
    this.fontFaceConverter = new PDFontFaceConverter();
    this.glyphUtils = new PdfBoxGlyphUtils();
  }

  // ==============================================================================================

  @Override
  public void process(Document pdf, Page page, Operator op, List<COSBase> args) throws IOException {
    if (args.size() < 1) {
      // ignore ( )Tj
      return;
    }
    // Get the graphics state from the engine.
    PDGraphicsState state = this.engine.getGraphicsState();

    // Get the text state from the engine.
    PDTextState textState = state.getTextState();

    // Get some text state parameters.
    float fontSize = textState.getFontSize();
    float horizScaling = textState.getHorizontalScaling() / 100f;
    float charSpacing = textState.getCharacterSpacing();

    // Put the text state parameters into matrix form.
    Matrix params = new Matrix(fontSize * horizScaling, 0, // 0
            0, fontSize, // 0
            0, textState.getRise()); // 1

    // Get the current font from the text state.
    PDFont font = textState.getFont();
    if (font == null) {
      // No current font available, use a default one.
      font = PDFontFactory.createDefaultFont();
    }

    // Get the text to show.
    COSString text = (COSString) args.get(0);
    byte[] bytes = text.getBytes();

    // Create an stream from given bytes and read it.
    try (InputStream in = new ByteArrayInputStream(bytes)) {
      while (in.available() > 0) {
        // Decode a single character
        int before = in.available();
        int code = font.readCode(in);
        int codeLength = before - in.available();
        String unicode = font.toUnicode(code);

        // Word spacing shall be applied to every occurrence of the single-byte
        // character code 32 in a string when using a simple font or a
        // composite font that defines code 32 as a single-byte code.
        float wordSpacing = 0;
        if (codeLength == 1 && code == 32) {
          wordSpacing += textState.getWordSpacing();
        }

        // Define the text rendering matrix (text space -> device space)
        Matrix ctm = state.getCurrentTransformationMatrix();
        Matrix trm = params.multiply(this.engine.getTextMatrix()).multiply(ctm);

        // get glyph's position vector if this is vertical text
        // changes to vertical text should be tested with PDFBOX-2294 and
        // PDFBOX-1422
        if (font.isVertical()) {
          // position vector, in text space
          Vector v = font.getPositionVector(code);

          // apply the position vector to the horizontal origin to get the
          // vertical origin
          trm.translate(v);
        }

        // Process the glyph.
        this.engine.saveGraphicsState();
        showGlyph(pdf, page, unicode, code, font, trm);
        this.engine.restoreGraphicsState();

        // Get glyph's horizontal and vertical displacements, in text space
        Vector w = font.getDisplacement(code);

        // Calculate the displacements.
        float tx, ty;
        if (font.isVertical()) {
          tx = 0;
          ty = w.getY() * fontSize + charSpacing + wordSpacing;
        } else {
          tx = (w.getX() * fontSize + charSpacing + wordSpacing) * horizScaling;
          ty = 0;
        }

        // Update the text matrix.
        Matrix translate = Matrix.getTranslateInstance(tx, ty);
        this.engine.getTextMatrix().concatenate(translate);
      }
    } catch (Exception e) {
      log.warn("An error occurred on processing the character " + text, e);
    }
  }

  // ==============================================================================================
  // Methods to process a glyph.

  /**
   * Processes a single glyph.
   * 
   * @param pdf    The PDF document to which the glyph belongs to.
   * @param page   The PDF page to which the glyph belongs to.
   * @param glyph  The unicode text for this glyph.
   * @param code   The internal PDF character code for the glyph
   * @param pdFont The font of the glyph.
   * @param trm    The current text rendering matrix
   * @throws IOException if something went wrong on processing the glyph.
   */
  public void showGlyph(Document pdf, Page page, String glyph, int code, PDFont pdFont, Matrix trm)
          throws IOException {
    // Compute a bounding box that indeed surrounds the whole glyph, even in
    // case of ascenders (e.g., "l") and descenders (e.g., "g").
    // TODO: Make it faster.
    Rectangle box = computeGlyphBoundingBox(pdf, page, code, pdFont, trm);

    // Compute the bounding box of the glyph by the method of PdfBox, where all
    // bounding boxes in a text line share the same baseline, even in case of
    // ascenders and descenders.
    // TODO: Make it faster.
    Rectangle pdfBoxBoundBox = computePdfBoxGlyphBoundingBox(code, pdFont, trm);

    if (box != null) {
      // Bounding boxes need some adjustments.
      if (PdfActUtils.isEqual(pdfBoxBoundBox.getWidth(), 0, 0.1f)) {
        // Don't adjust bounding box if the width is 0.
        box.setMinX(pdfBoxBoundBox.getMinX());
        box.setMaxX(pdfBoxBoundBox.getMaxX());
      } else if (PdfActUtils.isLarger(pdfBoxBoundBox.getWidth(), 0, 0.1f)) {
        if (pdfBoxBoundBox.getMinX() < box.getMinX()) {
          box.setMinX(pdfBoxBoundBox.getMinX());
        }
        if (pdfBoxBoundBox.getMaxX() > box.getMaxX()) {
          box.setMaxX(pdfBoxBoundBox.getMaxX());
        }
      }
    } else {
      // Use the bounding box of PdfBox.
      box = pdfBoxBoundBox;
    }

    // Compute the fontsize. 
    // See https://stackoverflow.com/questions/48010235/pdf-specification-get-font-size-in-points
    // for an explanation why we can't use engine.getGraphicsState().getTextState().getFontSize().
    float fontSize = trm.getScalingFactorX();

    // Use our additional glyph list for Unicode mapping
    GlyphList additionalGlyphs = this.glyphUtils.getAdditionalGlyphs();
    String unicode = pdFont.toUnicode(code, additionalGlyphs);

    // TODO: If we need the hasEncoding flag, uncomment the following:
    // boolean hasEncoding = unicode != null;
    //
    // // Obtain the glyph name.
    // String glyphName = ".notdef";
    // if (font instanceof PDType1CFont) {
    // PDType1CFont cFont = (PDType1CFont) font;
    // glyphName = cFont.codeToName(code);
    // } else if (font instanceof PDSimpleFont) {
    // PDSimpleFont simpleFont = (PDSimpleFont) font;
    // glyphName = simpleFont.getGlyphList().codePointToName(code);
    // }
    //
    // // From time to time (if font is embedded), there could be glyphs that
    // were
    // // redefined by a type3 font file (a custom path to draw to print the
    // // glyph). In such cases, we don't know the semantic meaning of the
    // glpyh.
    // // Try to derive it from the glyph name.
    // if (PdfGlyphDictionary.hasGlyphForName(glyphName)) {
    // unicode = PdfGlyphDictionary.getGlyphForName(glyphName);
    // }

    // When there is no Unicode mapping available, Acrobat simply coerces the
    // character code into Unicode, so we do the same. Subclasses of
    // PDFStreamEngine don't necessarily want this, which is why we leave it
    // until this point in PDFTextStreamEngine.
    if (unicode == null) {
      if (pdFont instanceof PDSimpleFont) {
        char c = (char) code;
        unicode = new String(new char[] {c});

        // TODO: If we need the hasEncoding flag, uncomment the following:
        // // Obtain if the font has an encoding for the given code.
        // PDSimpleFont simpleFont = (PDSimpleFont) font;
        // if (font instanceof PDType1Font) {
        // PDType1Font type1Font = (PDType1Font) font;
        // hasEncoding = !".notdef".equals(type1Font.codeToName(code));
        // } else {
        // String name = simpleFont.getGlyphList().codePointToName(code);
        // String glyphUnicode = simpleFont.getGlyphList().toUnicode(name);
        // hasEncoding = glyphUnicode != null;
        // }
      } else {
        // Acrobat doesn't seem to coerce composite font's character codes,
        // instead it skips them. See the "allah2.pdf" TestTextStripper file.
        return;
      }
    }

    PDGraphicsState graphicsState = this.engine.getGraphicsState();
    PDColor pdColor = graphicsState.getNonStrokingColor();
    PDColorSpace pdColorSpace = graphicsState.getNonStrokingColorSpace();

    // Convert the color.
    Color color = ColorManager.getColor(pdColor, pdColorSpace);

    // Convert the font.
    Font font = this.fontTranslator.convert(pdFont);
    // Round the font size.
    fontSize = PdfActUtils.round(fontSize, FLOATING_NUMBER_PRECISION);
    FontFace fontFace = this.fontFaceConverter.convert(font, fontSize);

    // Round the values of boundingbox.
    box.setMinX(PdfActUtils.round(box.getMinX(), FLOATING_NUMBER_PRECISION));
    box.setMinY(PdfActUtils.round(box.getMinY(), FLOATING_NUMBER_PRECISION));
    box.setMaxX(PdfActUtils.round(box.getMaxX(), FLOATING_NUMBER_PRECISION));
    box.setMaxY(PdfActUtils.round(box.getMaxY(), FLOATING_NUMBER_PRECISION));

    Position position = new Position(page, box);

    Character character = new Character();
    character.setText(unicode);
    character.setFontFace(fontFace);
    character.setColor(color);
    character.setPosition(position);
    character.setExtractionRank(this.sequenceNumber++);

    this.engine.handlePdfCharacter(pdf, page, character);
  }

  // ==============================================================================================

  /**
   * Computes the bounding box for the given glyph in any font.
   * 
   * @param pdf  The PDF document to which the stream belongs to.
   * @param page The PDF page to which the stream belongs to.
   * @param code The internal PDF character code for the glyph
   * @param font The font of the glyph.
   * @param trm  The current text rendering matrix
   * 
   * @return The bounding box of the glyph or null, if the bounding box could not be computed.
   * @throws IOException if something went wrong on computing the bounding box.
   */
  protected Rectangle computeGlyphBoundingBox(Document pdf, Page page, int code, PDFont font,
          Matrix trm) throws IOException {
    if (font instanceof PDType3Font) {
      // The font is a Type3 font. We have to compute the bounding box by
      // parsing the Type3 stream.
      return computeType3GlyphBoundingBox(pdf, page, code, font, trm);
    }
    // The font is *not* a Type3 font. We can compute the bounding box on the
    // "default" way.
    return computeNonType3GlyphBoundingBox(code, font, trm);
  }

  /**
   * Computes the bounding box for the given glyph in a Type3 font.
   * 
   * @param pdf  The PDF document to which the glyph belongs to.
   * @param page The PDF page to which the glyph belongs to.
   * @param code The internal PDF character code for the glyph
   * @param font The Type3 font of the glyph.
   * @param trm  The current text rendering matrix
   * 
   * @return The bounding box of the glyph or null, if the bounding box could not be computed.
   * @throws IOException if something went wrong on computing the bounding box.
   */
  protected Rectangle computeType3GlyphBoundingBox(Document pdf, Page page, int code, PDFont font,
          Matrix trm) throws IOException {
    PDType3Font type3Font = (PDType3Font) font;
    this.engine.processType3Stream(pdf, page, type3Font.getCharProc(code), trm);
    return this.engine.getCurrentType3GlyphBoundingBox();
  }

  /**
   * Computes the bounding box for the given glyph, given in any font, different from a Type3 font.
   * 
   * @param code The internal PDF character code for the glyph
   * @param font The font of the glyph.
   * @param trm  The current text rendering matrix
   * 
   * @return The bounding box of the glyph or null, if the bounding box could not be computed.
   * @throws IOException if something went wrong on computing the bounding box.
   */
  protected Rectangle computeNonType3GlyphBoundingBox(int code, PDFont font, Matrix trm)
          throws IOException {
    if (font == null) {
      return null;
    }

    // Ensure, that the font is not a Type3 font.
    if (!(font instanceof PDSimpleFont)) {
      return null;
    }

    PDSimpleFont simpleFont = (PDSimpleFont) font;

    // Obtain the associated glyph name.
    Encoding encoding = simpleFont.getEncoding();
    if (encoding != null) {
      String glyphName = encoding.getName(code);
      if (glyphName != null) {
        // Check, if the font is a type1 font.
        if (simpleFont instanceof PDType1Font) {
          PDType1Font t1Font = (PDType1Font) simpleFont;

          // Check, if the font contains an embedded FontFile.
          Type1Font afmFont = t1Font.getType1Font();
          if (afmFont != null) {
            Type1CharString charString = afmFont.getType1CharString(glyphName);
            if (charString != null) {
              Rectangle2D boundingBox = charString.getBounds();
              float minX = (float) boundingBox.getMinX();
              float minY = (float) boundingBox.getMinY();
              float maxX = (float) boundingBox.getMaxX();
              float maxY = (float) boundingBox.getMaxY();
              return transformBoundingBox(minX, minY, maxX, maxY, font, trm);
            }
          }

          // Check, if the additional AFM map contains an entry for the font.
          CharMetric metric = PdfBoxAFMUtils.getCharMetric(glyphName, t1Font);
          if (metric != null) {
            BoundingBox boundingBox = metric.getBoundingBox();
            float minX = boundingBox.getLowerLeftX();
            float minY = boundingBox.getLowerLeftY();
            float maxX = boundingBox.getUpperRightX();
            float maxY = boundingBox.getUpperRightY();
            return transformBoundingBox(minX, minY, maxX, maxY, font, trm);
          }
        }

        // Check, if the font contains an embedded FontFile3.
        if (simpleFont instanceof PDType1CFont) {
          // This font has an embedded font program represented in the Compact
          // Font Format (CFF).
          PDType1CFont type1CFont = (PDType1CFont) simpleFont;
          CFFType1Font cffFont = type1CFont.getCFFType1Font();
          if (cffFont != null) {
            Type1CharString charString = cffFont.getType1CharString(glyphName);

            if (charString != null) {
              Rectangle2D boundingBox = charString.getBounds();
              float minX = (float) boundingBox.getMinX();
              float minY = (float) boundingBox.getMinY();
              float maxX = (float) boundingBox.getMaxX();
              float maxY = (float) boundingBox.getMaxY();
              return transformBoundingBox(minX, minY, maxX, maxY, font, trm);
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Transforms the given bounding box into the device space.
   * 
   * @param minX The minX value of the bounding box.
   * @param minY The minY value of the bounding box.
   * @param maxX The maxX value of the bounding box.
   * @param maxY The maxY value of the bounding box.
   * @param font The current font.
   * @param trm  The current text rendering matrix.
   * 
   * @return The transformed bounding box.
   */
  protected Rectangle transformBoundingBox(float minX, float minY, float maxX, float maxY,
          PDFont font, Matrix trm) {
    if (font == null) {
      return null;
    }

    Point lowerLeft = new Point(minX, minY);
    Point upperRight = new Point(maxX, maxY);

    Matrix fontMatrix = font.getFontMatrix();

    // glyph space -> text space
    this.engine.transform(lowerLeft, fontMatrix);
    this.engine.transform(upperRight, fontMatrix);

    // text space -> device space
    this.engine.transform(lowerLeft, trm);
    this.engine.transform(upperRight, trm);

    return new Rectangle(lowerLeft, upperRight);
  }

  /**
   * Computes the bounding box for the given glyph by the method of PdfBox, that is computing an
   * approximate bounding box, without respecting ascenders (like "l") or descenders (like "g").
   * 
   * @param code The character
   * @param font The font.
   * @param trm  The current text rendering matrix.
   * @return The bounding box.
   * @throws IOException if obtaining the default bounding box fails.
   */
  protected Rectangle computePdfBoxGlyphBoundingBox(int code, PDFont font, Matrix trm)
          throws IOException {
    PDGraphicsState state = this.engine.getGraphicsState();
    Matrix ctm = state.getCurrentTransformationMatrix();
    Matrix textMatrix = this.engine.getTextMatrix();

    Vector displacement = font.getDisplacement(code);
    float fontSize = state.getTextState().getFontSize();
    float horizScaling = state.getTextState().getHorizontalScaling() / 100f;

    float tx = displacement.getX() * fontSize * horizScaling;
    float ty = 0;

    // (modified) combined displacement matrix
    Matrix td = Matrix.getTranslateInstance(tx, ty);

    // (modified) text rendering matrix
    Matrix nextTrm = td.multiply(textMatrix).multiply(ctm);

    // 1/2 the bbox is used as the height todo: why?
    float glyphHeight = font.getBoundingBox().getHeight() / 2;
    // transformPoint from glyph space -> text space
    float height = font.getFontMatrix().transformPoint(0, glyphHeight).y;
    float dyDisplay = height * trm.getScalingFactorY();

    float minX = trm.getTranslateX();
    float minY = trm.getTranslateY();
    float maxX = nextTrm.getTranslateX();
    float maxY = minY + dyDisplay;

    return new Rectangle(minX, minY, maxX, maxY);
  }

  @Override
  public String getName() {
    return "Tj";
  }
}
