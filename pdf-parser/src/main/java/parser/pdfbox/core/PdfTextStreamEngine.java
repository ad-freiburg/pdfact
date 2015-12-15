package parser.pdfbox.core;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.fontbox.afm.CharMetric;
import org.apache.fontbox.cff.CFFType1Font;
import org.apache.fontbox.cff.Type1CharString;
import org.apache.fontbox.type1.Type1Font;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

import de.freiburg.iif.model.Point;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimplePoint;
import de.freiburg.iif.model.simple.SimpleRectangle;
import parser.pdfbox.core.operator.color.SetNonStrokingColor;
import parser.pdfbox.core.operator.color.SetNonStrokingColorN;
import parser.pdfbox.core.operator.color.SetNonStrokingColorSpace;
import parser.pdfbox.core.operator.color.SetNonStrokingDeviceCMYKColor;
import parser.pdfbox.core.operator.color.SetNonStrokingDeviceGrayColor;
import parser.pdfbox.core.operator.color.SetNonStrokingDeviceRGBColor;
import parser.pdfbox.core.operator.graphics.AppendRectangleToPath;
import parser.pdfbox.core.operator.graphics.BeginInlineImage;
import parser.pdfbox.core.operator.graphics.ClipEvenOddRule;
import parser.pdfbox.core.operator.graphics.ClipNonZeroRule;
import parser.pdfbox.core.operator.graphics.ClosePath;
import parser.pdfbox.core.operator.graphics.CurveTo;
import parser.pdfbox.core.operator.graphics.CurveToReplicateFinalPoint;
import parser.pdfbox.core.operator.graphics.CurveToReplicateInitialPoint;
import parser.pdfbox.core.operator.graphics.EndPath;
import parser.pdfbox.core.operator.graphics.FillEvenOddAndStrokePath;
import parser.pdfbox.core.operator.graphics.FillEvenOddRule;
import parser.pdfbox.core.operator.graphics.FillNonZeroAndStrokePath;
import parser.pdfbox.core.operator.graphics.FillNonZeroRule;
import parser.pdfbox.core.operator.graphics.Invoke;
import parser.pdfbox.core.operator.graphics.LineTo;
import parser.pdfbox.core.operator.graphics.ModifyCurrentTransformationMatrix;
import parser.pdfbox.core.operator.graphics.MoveTo;
import parser.pdfbox.core.operator.graphics.RestoreGraphicsState;
import parser.pdfbox.core.operator.graphics.SaveGraphicsState;
import parser.pdfbox.core.operator.graphics.SetGraphicsStateParameters;
import parser.pdfbox.core.operator.graphics.StrokePath;
import parser.pdfbox.core.operator.text.BeginText;
import parser.pdfbox.core.operator.text.EndText;
import parser.pdfbox.core.operator.text.MoveText;
import parser.pdfbox.core.operator.text.MoveTextSetLeading;
import parser.pdfbox.core.operator.text.MoveToNextLine;
import parser.pdfbox.core.operator.text.MoveToNextLineAndShowText;
import parser.pdfbox.core.operator.text.MoveToNextLineAndShowTextWithSpacing;
import parser.pdfbox.core.operator.text.SetCharacterSpacing;
import parser.pdfbox.core.operator.text.SetFontAndSize;
import parser.pdfbox.core.operator.text.SetTextHorizontalScaling;
import parser.pdfbox.core.operator.text.SetTextLeading;
import parser.pdfbox.core.operator.text.SetTextMatrix;
import parser.pdfbox.core.operator.text.SetTextRenderingMode;
import parser.pdfbox.core.operator.text.SetTextRise;
import parser.pdfbox.core.operator.text.SetType3GlyphWidthAndBoundingBox;
import parser.pdfbox.core.operator.text.SetWordSpacing;
import parser.pdfbox.core.operator.text.ShowText;
import parser.pdfbox.core.operator.text.ShowTextWithIndividualGlyphPositioning;
import parser.pdfbox.model.PdfBoxCharacter;
import parser.pdfbox.model.PdfBoxColor;
import parser.pdfbox.model.PdfBoxFont;

/**
 * Extension of PDFStreamEngine for advanced processing of text, images, lines
 * and shapes.
 * 
 * @author Claudius Korzen
 */
public class PdfTextStreamEngine extends PdfStreamEngine {
  /** The custom glyph list. */
  protected GlyphList glyphList;
  /** The extraction order number. */
  protected int extractionOrderNumber = 0;

  /**
   * The default constructor.
   * 
   * @throws IOException
   *           if reading the glyph list fails.
   */
  public PdfTextStreamEngine() {
    super();

    // Text operators.
    addOperator(new BeginText()); // BT
    addOperator(new EndText()); // ET
    addOperator(new MoveText()); // Td
    addOperator(new MoveTextSetLeading()); // TD
    addOperator(new MoveToNextLineAndShowText()); // '
    addOperator(new MoveToNextLineAndShowTextWithSpacing()); // "
    addOperator(new SetCharacterSpacing()); // Tc
    addOperator(new SetFontAndSize()); // Tf
    addOperator(new MoveToNextLine()); // T*
    addOperator(new SetTextHorizontalScaling()); // Tz
    addOperator(new SetTextLeading()); // TL
    addOperator(new SetTextMatrix()); // Tm
    addOperator(new SetTextRenderingMode()); // Tr
    addOperator(new SetTextRise()); // Ts
    addOperator(new SetType3GlyphWidthAndBoundingBox()); // d1
    addOperator(new SetWordSpacing()); // Tw
    addOperator(new ShowText()); // Tj
    addOperator(new ShowTextWithIndividualGlyphPositioning()); // TJ

    // Graphics operators.
    addOperator(new AppendRectangleToPath()); // re
    addOperator(new BeginInlineImage()); // BI
    addOperator(new ClipEvenOddRule()); // W*
    addOperator(new ClipNonZeroRule()); // W
    addOperator(new ClosePath()); // h
    addOperator(new CurveTo()); // c
    addOperator(new CurveToReplicateFinalPoint()); // y
    addOperator(new CurveToReplicateInitialPoint()); // v
    addOperator(new EndPath()); // n
    addOperator(new FillEvenOddAndStrokePath()); // B*
    addOperator(new FillEvenOddRule()); // f*
    addOperator(new FillNonZeroAndStrokePath()); // B
    addOperator(new FillNonZeroRule()); // f
    addOperator(new Invoke()); // Do
    addOperator(new LineTo()); // l
    addOperator(new ModifyCurrentTransformationMatrix()); // cm
    addOperator(new MoveTo()); // m
    addOperator(new RestoreGraphicsState()); // Q
    addOperator(new SaveGraphicsState()); // q
    addOperator(new SetGraphicsStateParameters()); // gs
    addOperator(new StrokePath()); // S

    // Color operators.
    addOperator(new SetNonStrokingColor());
    addOperator(new SetNonStrokingColorN());
    addOperator(new SetNonStrokingColorSpace());
    addOperator(new SetNonStrokingDeviceCMYKColor());
    addOperator(new SetNonStrokingDeviceGrayColor());
    addOperator(new SetNonStrokingDeviceRGBColor());

    // load additional glyph list for Unicode mapping
    String path = "org/apache/pdfbox/resources/glyphlist/additional.txt";
    ClassLoader classLoader = GlyphList.class.getClassLoader();
    InputStream input = classLoader.getResourceAsStream(path);
    try {
      this.glyphList = new GlyphList(GlyphList.getAdobeGlyphList(), input);
    } catch (IOException e) {
      System.out.println("Couldn't instantiate glyph list.");
    }
  }

  @Override
  public void showGlyph(String unicode, int code, PDFont font, Matrix trm)
    throws IOException {
    super.showGlyph(unicode, code, font, trm);

    Rectangle boundingBox = null;
    if (font instanceof PDType3Font) {
      processType3Stream(((PDType3Font) font).getCharProc(code), trm);
      boundingBox = getCurrentType3GlyphBoundingBox();
    } else {
      boundingBox = getFontGlyphBoundingBox(code, font, trm);
    }

    Rectangle defaultBoundingBox = getDefaultBoundingBox(code, font, trm);
    if (boundingBox == null) {
      boundingBox = getDefaultBoundingBox(code, font, trm);
    } else {
      boundingBox.setMinX(defaultBoundingBox.getMinX());
      boundingBox.setMaxX(defaultBoundingBox.getMaxX());
    }

    float fontsize = getGraphicsState().getTextState().getFontSize();
    // fontsize = fontsize * trm.getScalingFactorX();

    // Use our additional glyph list for Unicode mapping
    unicode = font.toUnicode(code, glyphList);

    // When there is no Unicode mapping available, Acrobat simply coerces the
    // character code into Unicode, so we do the same. Subclasses of
    // PDFStreamEngine don't necessarily want this, which is why we leave it
    // until this point in PDFTextStreamEngine.
    if (unicode == null) {
      if (font instanceof PDSimpleFont) {
        char c = (char) code;
        unicode = new String(new char[] { c });
      } else {
        // Acrobat doesn't seem to coerce composite font's character codes,
        // instead it skips them. See the "allah2.pdf" TestTextStripper file.
        return;
      }
    }

    PDColor nonStrokingColor = getGraphicsState().getNonStrokingColor();
    PdfBoxColor color = PdfBoxColor.create(nonStrokingColor);
    PdfBoxFont pdfFont = PdfBoxFont.create(font);

    PdfBoxCharacter character = new PdfBoxCharacter(currentPage, unicode);
    character.setCharCode(code);
    character.setRectangle(boundingBox);
    character.setTextRenderingMatrix(trm);
    character.setFont(pdfFont);
    character.setFontsize(fontsize);
    character.setColor(color);
    character.setExtractionOrderNumber(this.extractionOrderNumber++);

    showPdfTextCharacter(character);
  }

  @Override
  public void showLigature(String[] unicodes, int code, PDFont font, 
      Matrix trm) throws IOException {
    super.showLigature(unicodes, code, font, trm);

    float size = getGraphicsState().getTextState().getFontSize();
    PDColor nonStrokingColor = getGraphicsState().getNonStrokingColor();
    PdfBoxColor color = PdfBoxColor.create(nonStrokingColor);
    PdfBoxFont pdfFont = PdfBoxFont.create(font);

    Rectangle boundingBox = null;
    if (font instanceof PDType3Font) {
      processType3Stream(((PDType3Font) font).getCharProc(code), trm);
      boundingBox = getCurrentType3GlyphBoundingBox();
    } else {
      boundingBox = getFontGlyphBoundingBox(code, font, trm);
    }

    if (boundingBox == null) {
      boundingBox = getDefaultBoundingBox(code, font, trm);
    } else {
      Rectangle defaultBoundingBox = getDefaultBoundingBox(code, font, trm);
      boundingBox.setMinX(defaultBoundingBox.getMinX());
      boundingBox.setMaxX(defaultBoundingBox.getMaxX());
    }

    float widthPerUnicode = boundingBox.getWidth() / (float) unicodes.length;

    for (int i = 0; i < unicodes.length; i++) {
      float minX = boundingBox.getMinX() + i * widthPerUnicode;
      float maxX = minX + widthPerUnicode;
      float minY = boundingBox.getMinY();
      float maxY = boundingBox.getMaxY();
      Rectangle newBoundBox = new SimpleRectangle(minX, minY, maxX, maxY);

      PdfBoxCharacter character = new PdfBoxCharacter(currentPage, unicodes[i]);
      character.setCharCode(code);
      character.setRectangle(newBoundBox);
      character.setTextRenderingMatrix(trm);
      character.setFont(pdfFont);
      character.setFontsize(size);
      character.setColor(color);
      character.setExtractionOrderNumber(this.extractionOrderNumber++);

      showPdfTextCharacter(character);
    }
  }

  /**
   * Returns the glyph's bounding box of given character in glyph space units.
   * 
   * @param charCode
   *          the code of given char.
   * @param font
   *          the font.
   * @param trm
   *          the current text rendering matrix.
   * 
   * @return the bounding box of the given character.
   * @throws IOException
   *           if obtaining the bounding box fails.
   */
  protected Rectangle getFontGlyphBoundingBox(int charCode, PDFont font,
      Matrix trm) throws IOException {
    if (font == null) {
      return null;
    }

    // Ensure, that the font is a type1 font.
    if (!(font instanceof PDSimpleFont)) {
      return null;
    }

    PDSimpleFont simpleFont = (PDSimpleFont) font;

    // Obtain the associated glyph name.
    Encoding encoding = simpleFont.getEncoding();
    if (encoding != null) {
      String glyphName = encoding.getName(charCode);

      if (glyphName != null) {
        // Check, if the font is a type1 font.
        if (simpleFont instanceof PDType1Font) {
          PDType1Font type1Font = (PDType1Font) simpleFont;

          // Check, if the font contains an embedded FontFile.
          Type1Font afmFont = type1Font.getType1Font();
          if (afmFont != null) {
            Type1CharString charString = afmFont.getType1CharString(glyphName);
            if (charString != null) {
              return computeBoundingBox(charString.getBounds(), font, trm);
            }
          }

          // Check, if the additional AFM map contains an entry for the font.
          CharMetric charMetric = getCharMetric(glyphName, type1Font);
          if (charMetric != null) {
            return computeBoundingBox(charMetric.getBoundingBox(), font, trm);
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
              return computeBoundingBox(charString.getBounds(), font, trm);
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Computes the bounding box from given Rectangle2D in device space units.
   * 
   * @param rect
   *          the chars string.
   * @param font
   *          the font.
   * @param trm
   *          the current text rendering matrix.
   * @return the bounding box from given char string.
   */
  protected Rectangle computeBoundingBox(Rectangle2D rect, PDFont font,
      Matrix trm) {
    if (rect != null && font != null) {
      Matrix fontMatrix = font.getFontMatrix();

      Point lowerLeft = new SimplePoint(rect.getMinX(), rect.getMinY());
      Point upperRight = new SimplePoint(rect.getMaxX(), rect.getMaxY());

      // glyph space -> text space
      transform(lowerLeft, fontMatrix);
      transform(upperRight, fontMatrix);

      // text space -> device space
      transform(lowerLeft, trm);
      transform(upperRight, trm);

      return new SimpleRectangle(lowerLeft, upperRight);
    }
    return null;
  }

  /**
   * Computes the bounding box from given Rectangle2D in device space units.
   * 
   * @param rect
   *          the chars string.
   * @param font
   *          the current font.
   * @param trm
   *          the current text rendering matrix.
   * @return the bounding box from given char string.
   */
  protected Rectangle computeBoundingBox(BoundingBox rect, PDFont font,
      Matrix trm) {
    if (rect != null && font != null) {
      Matrix fontMatrix = font.getFontMatrix();

      Point ll = new SimplePoint(rect.getLowerLeftX(), rect.getLowerLeftY());
      Point ur = new SimplePoint(rect.getUpperRightX(), rect.getUpperRightY());

      // glyph space -> text space
      transform(ll, fontMatrix);
      transform(ur, fontMatrix);

      // text space -> device space
      transform(ll, trm);
      transform(ur, trm);

      return new SimpleRectangle(ll, ur);
    }
    return null;
  }

  /**
   * Returns an approximate bounding box for a glyph with a font, for which no
   * glyph metrics exist.
   * 
   * @param code
   *          the character
   * @param font
   *          the font.
   * @param trm
   *          the current text rendering matrix.
   * @return the bounding box.
   * @throws IOException
   *           if obtaining the defualt bounding box fails.
   */
  protected Rectangle getDefaultBoundingBox(int code, PDFont font, Matrix trm)
    throws IOException {
    PDGraphicsState state = getGraphicsState();
    Matrix ctm = state.getCurrentTransformationMatrix();
    Matrix textMatrix = getTextMatrix();

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

    return new SimpleRectangle(minX, minY, maxX, maxY);
  }

  /**
   * A method provided as an event interface to allow a subclass to perform some
   * specific functionality when text needs to be processed.
   * 
   * @param character
   *          The character to be processed.
   */
  protected void showPdfTextCharacter(PdfBoxCharacter character) {
    // subclasses can override to provide specific functionality
  }

  /**
   * A method provided as an event interface to allow a subclass to perform some
   * specific functionality when text needs to be processed.
   * 
   * @param character
   *          The character to be processed.
   */
  protected void showPdfTextCharacters(List<PdfBoxCharacter> characters) {
    for (PdfBoxCharacter character : characters) {
      showPdfTextCharacter(character);
    }
  }
}
