package parser.pdfbox.core;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
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
import parser.pdfbox.model.PdfBoxFontDictionary;

/**
 * Extension of PDFStreamEngine for advanced processing of text, images, lines
 * and shapes.
 * 
 * @author Claudius Korzen
 */
public class PdfTextStreamEngine extends PdfStreamEngine {
  /** The custom glyph list. */
  protected GlyphList glyphList;

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

  PdfBoxCharacter prevCharacter = null;
  PdfBoxCharacter prePrevCharacter = null;
  
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

    // In some pdfs, fontsize is equal to '1.0' for every character. 
    // In this case, multiplying it with the scaling factor gives the correct 
    // fontsize. In other pdfs, the fontsizes are correct and multiplying it 
    // with the scaling factor would result in too large fontsizes.
    // Observation: In the latter case, fontsize and scale factor are equal, 
    // so multiply the fontsize and the scale factor only when they aren't 
    // equal.
    // TODO: Verify, that this is a correct observation.
    float fontsize = getGraphicsState().getTextState().getFontSize();
    float scaleFactorX = trm.getScalingFactorX();
    if (fontsize != scaleFactorX) {
      fontsize *= scaleFactorX;
    }
        
    // Use our additional glyph list for Unicode mapping
    unicode = font.toUnicode(code, glyphList);

    boolean hasEncoding = unicode != null;
         
    // Obtain the glyph name.
    // TODO: Move to PdfBoxFont.
    String glyphName = ".notdef";
    if (font instanceof PDType1CFont) {
      PDType1CFont cFont = (PDType1CFont) font;
      glyphName = cFont.codeToName(code);
    } else if (font instanceof PDSimpleFont) { 
      PDSimpleFont simpleFont = (PDSimpleFont) font;
      glyphName = simpleFont.getGlyphList().codePointToName(code);
    }
    
    // From time to time (if font is embedded), there could be glyphs that were
    // redefined by a type3 font file (a custom path to draw to print the glyph)
    // In such cases, we don't know the semantic meaning of the glpyh. Try to
    // derive it from the glyph name.
    if (PdfBoxFontDictionary.hasGlyphForName(glyphName)) {
      unicode = PdfBoxFontDictionary.getGlyphForName(glyphName);
    }
           
    // When there is no Unicode mapping available, Acrobat simply coerces the
    // character code into Unicode, so we do the same. Subclasses of
    // PDFStreamEngine don't necessarily want this, which is why we leave it
    // until this point in PDFTextStreamEngine.
    if (unicode == null) {
      if (font instanceof PDSimpleFont) { 
        PDSimpleFont simpleFont = (PDSimpleFont) font;
                
        char c = (char) code;
        unicode = new String(new char[] { c });

        // Obtain if the font has an encoding for the given code.
        if (font instanceof PDType1Font) {          
          PDType1Font type1Font = (PDType1Font) font;
          hasEncoding = !".notdef".equals(type1Font.codeToName(code));
        } else {          
          String name = simpleFont.getGlyphList().codePointToName(code);
          String glyphUnicode = simpleFont.getGlyphList().toUnicode(name);
          hasEncoding = glyphUnicode != null;
        }
      } else {
        // Acrobat doesn't seem to coerce composite font's character codes,
        // instead it skips them. See the "allah2.pdf" TestTextStripper file.
        return;
      }
    }
    

    
    PDColor nonStrokingColor = getGraphicsState().getNonStrokingColor();
    PdfBoxColor color = PdfBoxColor.create(nonStrokingColor);
    PdfBoxFont pdfFont = PdfBoxFont.create(font);
    
    // In case of an italic font, the maxX of the bounding box is usually too
    // small, so extend it by a given amount.
    if (pdfFont.isItalic()) {
      char l = unicode.charAt(0);
      
      // List all characters whose upper right is equal to the maxX of 
      // characters bounding box. TODO
      if (l == 'B'
          || l == 'V' 
          || l == 'W' 
          || l == 'v' 
          || l == 'w'
          || l == 'T'
          || l == 'U'
          || l == 'τ'
          || l == 'M'
          || l == 'N'
          || l == 'f'
          || l == 'P') {
        boundingBox.setMaxX(boundingBox.getMaxX() + 0.25f * boundingBox.getWidth());
      }
    }
    
    PdfBoxCharacter character = new PdfBoxCharacter(currentPage, unicode);
    character.setCharCode(code);
    character.setRectangle(boundingBox);
    character.setTextRenderingMatrix(trm);
    character.setFont(pdfFont);
    character.setFontsize(fontsize);
    character.setColor(color);
    character.setHasEncoding(hasEncoding);
       
    // Handle diacritic characters:
    // In most cases, diacritic characters are represented in its decomposed 
    // form. For example, "è" may be represented as the two characters "'e" or 
    // "e'". To merge such characters the base character must be followed by 
    // the diacritic: "e'" implicitly. To maintain this order, decide to which 
    // base character a diacritic belongs and merge them together.
    
    // To decide to which character the diacritic belongs, we have to wait for
    // the character after the diacritic, so we check if the previous character
    // is a diacritic and compare the horizontal overlap between (a) the 
    // diacritic and the character "in front" of the character 
    // (prePreviousCharacter) and (b) the diacritic and the character "behind"
    // the character (the current character).
    
    // The character could be a diacritic but not merged with any character.
    // So obtain, if the diacritic is merged with any character.    
    if (prevCharacter != null && prevCharacter.isDiacritic()) {
      Rectangle prevRectangle = prevCharacter.getRectangle();
      Rectangle prePrevRectangle = null;
      if (prePrevCharacter != null) {
        prePrevRectangle = prePrevCharacter.getRectangle();
      }
      
      // Compare the horizontal overlaps with the diacritic.
      float overlap1 = prevRectangle.computeHorizontalOverlap(prePrevRectangle);
      float overlap2 = prevRectangle.computeHorizontalOverlap(boundingBox);
            
      if (overlap1 < 0.01f && overlap2 < 0.01) {
        prevCharacter.setIsDiacritic(false);
      } else if (overlap1 >= overlap2) {
        prePrevCharacter.mergeDiacritic(prevCharacter);
        prevCharacter.setIsDiacritic(true);
      } else {
        character.mergeDiacritic(prevCharacter);
        prevCharacter.setIsDiacritic(true);
      }
    }
    
    // Return the character only if it is no diacritic.
    // TODO: There could be characters that are actually diacritics but have
    // a special encoding (and hence a different meaning). That's the case for
    // pdf 76 (page 5) for example.
    showPdfTextCharacter(character);
    
    prePrevCharacter = prevCharacter;
    prevCharacter = character;
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
