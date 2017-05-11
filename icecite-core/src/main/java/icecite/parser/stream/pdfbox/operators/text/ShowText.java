package icecite.parser.stream.pdfbox.operators.text;

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

import com.google.inject.Inject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacter.PdfCharacterFactory;
import icecite.models.PdfColor;
import icecite.models.PdfColor.PdfColorFactory;
import icecite.models.PdfFont;
import icecite.models.PdfFont.PdfFontFactory;
import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.parser.stream.pdfbox.utils.PdfBoxAFMUtils;
import icecite.parser.stream.pdfbox.utils.PdfBoxGlyphUtils;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.PointFactory;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.RectangleFactory;
import icecite.utils.math.MathUtils;

/**
 * Tj: Show a text string.
 * 
 * @author Claudius Korzen
 */
public class ShowText extends OperatorProcessor {
  /**
   * The factory to create instances of {@link PdfCharacterFactory}.
   */
  @Inject
  protected PdfCharacterFactory pdfCharacterFactory;

  /**
   * The factory to create instances of {@link PdfFont}.
   */
  @Inject
  // TODO: Create cache of PdfFonts.
  protected PdfFontFactory pdfFontFactory;

  /**
   * The factory to create instances of {@link PdfColor}.
   */
  @Inject
  protected PdfColorFactory pdfColorFactory;

  /**
   * The factory to create instances of {@link Rectangle}.
   */
  @Inject
  protected RectangleFactory rectangleFactory;

  /**
   * The factory to create instances of {@link Point}.
   */
  @Inject
  protected PointFactory pointFactory;

  // ==========================================================================

  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
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
        showGlyph(unicode, code, font, trm);
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
    }
  }

  // ==========================================================================
  // Methods to process a glyph.

  /**
   * Processes a single glyph.
   *
   * @param glyph
   *        The Unicode text for this glyph.
   * @param code
   *        The internal PDF character code for the glyph
   * @param font
   *        The font of the glyph.
   * @param trm
   *        The current text rendering matrix
   * @throws IOException
   *         if something went wrong on processing the glyph.
   */
  public void showGlyph(String glyph, int code, PDFont font, Matrix trm)
      throws IOException {
    // Compute a bounding box that indeed surrounds the whole glyph, even in
    // case of ascenders (e.g., "l") and descenders (e.g., "g").
    Rectangle boundBox = computeGlyphBoundingBox(code, font, trm);

    // Compute the bounding box of the glyph by the method of PdfBox, where all
    // bounding boxes in a text line share the same baseline, even in case of
    // ascenders and descenders.
    Rectangle pdfBoxBoundBox = computePdfBoxGlyphBoundingBox(code, font, trm);

    if (boundBox != null) {
      // Bounding boxes need some adjustments.
      if (MathUtils.isEqual(pdfBoxBoundBox.getWidth(), 0, 0.1f)) {
        // Don't adjust bounding box if the width is 0.
        boundBox.setMinX(pdfBoxBoundBox.getMinX());
        boundBox.setMaxX(pdfBoxBoundBox.getMaxX());
      } else if (MathUtils.isLarger(pdfBoxBoundBox.getWidth(), 0, 0.1f)) {
        if (pdfBoxBoundBox.getMinX() < boundBox.getMinX()) {
          boundBox.setMinX(pdfBoxBoundBox.getMinX());
        }
        if (pdfBoxBoundBox.getMaxX() > boundBox.getMaxX()) {
          boundBox.setMaxX(pdfBoxBoundBox.getMaxX());
        }
      }
    } else {
      // Use the bounding box of PdfBox.
      boundBox = pdfBoxBoundBox;
    }

    // In some pdfs, fontsize is equal to '1.0' for every character.
    // In this case, multiplying it with the scaling factor gives the correct
    // fontsize. In other pdfs, the fontsizes are correct and multiplying it
    // with the scaling factor would result in too large fontsizes.
    // Observation: In the latter case, fontsize and scale factor are equal,
    // so multiply the fontsize and the scale factor only when they aren't
    // equal.
    // TODO: Verify, that this is a correct observation.
    PDGraphicsState graphicsState = this.engine.getGraphicsState();
    float fontsize = graphicsState.getTextState().getFontSize();
    float scaleFactorX = trm.getScalingFactorX();
    if (fontsize != scaleFactorX) {
      fontsize *= scaleFactorX;
    }

    // Use our additional glyph list for Unicode mapping
    GlyphList additionalGlyphs = PdfBoxGlyphUtils.getAdditionalGlyphs();
    String unicode = font.toUnicode(code, additionalGlyphs);

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
      if (font instanceof PDSimpleFont) {
        char c = (char) code;
        unicode = new String(new char[] { c });

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

    PDColor nonStrokingColor = graphicsState.getNonStrokingColor();
    PDColorSpace cs = graphicsState.getNonStrokingColorSpace();
    float[] rgb = cs.toRGB(nonStrokingColor.getComponents());

    // TODO set the properties of the font.
    PdfFont pdfFont = this.pdfFontFactory.create();
    PdfColor color = this.pdfColorFactory.create(rgb);

    PdfCharacter character = this.pdfCharacterFactory.create();
    character.setText(unicode);
    character.setFontSize(fontsize);
    character.setColor(color);
    character.setFont(pdfFont);
    character.setBoundingBox(boundBox);

    this.engine.handlePdfCharacter(character);
  }

  // ==========================================================================

  /**
   * Computes the bounding box for the given glyph in any font.
   * 
   * @param code
   *        The internal PDF character code for the glyph
   * @param font
   *        The font of the glyph.
   * @param trm
   *        The current text rendering matrix
   * 
   * @return The bounding box of the glyph or null, if the bounding box could
   *         not be computed.
   * @throws IOException
   *         if something went wrong on computing the bounding box.
   */
  protected Rectangle computeGlyphBoundingBox(int code, PDFont font, Matrix trm)
      throws IOException {
    if (font instanceof PDType3Font) {
      // The font is a Type3 font. We have to compute the bounding box by
      // parsing the Type3 stream.
      return computeType3GlyphBoundingBox(code, font, trm);
    } else {
      // The font is *not* a Type3 font. We can compute the bounding box on the
      // "default" way.
      return computeNonType3GlyphBoundingBox(code, font, trm);
    }
  }

  /**
   * Computes the bounding box for the given glyph in a Type3 font.
   * 
   * @param code
   *        The internal PDF character code for the glyph
   * @param font
   *        The Type3 font of the glyph.
   * @param trm
   *        The current text rendering matrix
   * 
   * @return The bounding box of the glyph or null, if the bounding box could
   *         not be computed.
   * @throws IOException
   *         if something went wrong on computing the bounding box.
   */
  protected Rectangle computeType3GlyphBoundingBox(int code, PDFont font,
      Matrix trm) throws IOException {
    PDType3Font type3Font = (PDType3Font) font;
    this.engine.processType3Stream(type3Font.getCharProc(code), trm);
    return this.engine.getCurrentType3GlyphBoundingBox();
  }

  /**
   * Computes the bounding box for the given glyph, given in any font,
   * different from a Type3 font.
   * 
   * @param code
   *        The internal PDF character code for the glyph
   * @param font
   *        The font of the glyph.
   * @param trm
   *        The current text rendering matrix
   * 
   * @return The bounding box of the glyph or null, if the bounding box could
   *         not be computed.
   * @throws IOException
   *         if something went wrong on computing the bounding box.
   */
  protected Rectangle computeNonType3GlyphBoundingBox(int code, PDFont font,
      Matrix trm) throws IOException {
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
   * @param minX
   *        The minX value of the bounding box.
   * @param minY
   *        The minY value of the bounding box.
   * @param maxX
   *        The maxX value of the bounding box.
   * @param maxY
   *        The maxY value of the bounding box.
   * @param font
   *        The current font.
   * @param trm
   *        The current text rendering matrix.
   * 
   * @return The transformed bounding box.
   */
  protected Rectangle transformBoundingBox(float minX, float minY, float maxX,
      float maxY, PDFont font, Matrix trm) {
    if (font == null) {
      return null;
    }

    Point lowerLeft = this.pointFactory.create(minX, minY);
    Point upperRight = this.pointFactory.create(maxX, maxY);

    Matrix fontMatrix = font.getFontMatrix();

    // glyph space -> text space
    this.engine.transform(lowerLeft, fontMatrix);
    this.engine.transform(upperRight, fontMatrix);

    // text space -> device space
    this.engine.transform(lowerLeft, trm);
    this.engine.transform(upperRight, trm);

    return this.rectangleFactory.create(lowerLeft, upperRight);
  }

  /**
   * Computes the bounding box for the given glyph by the method of PdfBox,
   * that is computing an approximate bounding box, without respecting
   * ascenders (like "l") or descenders (like "g").
   * 
   * @param code
   *        The character
   * @param font
   *        The font.
   * @param trm
   *        The current text rendering matrix.
   * @return The bounding box.
   * @throws IOException
   *         if obtaining the defualt bounding box fails.
   */
  protected Rectangle computePdfBoxGlyphBoundingBox(int code, PDFont font,
      Matrix trm) throws IOException {
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

    return this.rectangleFactory.create(minX, minY, maxX, maxY);
  }

  @Override
  public String getName() {
    return "Tj";
  }
}