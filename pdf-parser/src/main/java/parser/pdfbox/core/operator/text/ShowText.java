package parser.pdfbox.core.operator.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontFactory;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * Tj: Show a text string.
 * 
 * @author Claudius Korzen
 */
public class ShowText extends OperatorProcessor {
  /** Maps the unicodes of ligatures to its individual characters. */
  protected static final Map<String, String[]> LIGATURES_MAP;

  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    if (args.size() < 1) {
      // ignore ( )Tj
      return;
    }
    COSString string = (COSString) args.get(0);
    byte[] bytes = string.getBytes();

    PDGraphicsState state = context.getGraphicsState();
    PDTextState textState = state.getTextState();
    float fontSize = textState.getFontSize();
    float horizScaling = textState.getHorizontalScaling() / 100f;
    float charSpacing = textState.getCharacterSpacing();
    PDFont font = textState.getFont();
    if (font == null) {
      font = PDFontFactory.createDefaultFont();
    }

    // put the text state parameters into matrix form
    Matrix params = new Matrix(fontSize * horizScaling, 0, // 0
        0, fontSize, // 0
        0, textState.getRise()); // 1

    // read the stream until it is empty
    InputStream in = new ByteArrayInputStream(bytes);
    while (in.available() > 0) {
      // Decode a single character
      int before = in.available();
      int code = font.readCode(in);
      int codeLength = before - in.available();
      String unicode = font.toUnicode(code);

      // Word spacing shall be applied to every occurrence of the single-byte
      // character code 32 in a string when using a simple font or a composite
      // font that defines code 32 as a single-byte code.
      float wordSpacing = 0;
      if (codeLength == 1 && code == 32) {
        wordSpacing += textState.getWordSpacing();
      }

      // Define the text rendering matrix (text space -> device space)
      Matrix ctm = state.getCurrentTransformationMatrix();
      Matrix trm = params.multiply(context.getTextMatrix()).multiply(ctm);

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

      // process the decoded glyph
      context.saveGraphicsState();

      // Check, if the unicode is a ligature.
      String[] resolvedLigature = resolveLigature(unicode);
      // resolvedLigature is null, if the unicode string isn't a ligature.
      if (resolvedLigature == null) {
        context.showGlyph(unicode, code, font, trm);
      } else {
        context.showLigature(resolvedLigature, code, font, trm);
      }

      context.restoreGraphicsState();

      // get glyph's horizontal and vertical displacements, in text space
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

      // Update the text matrix
      context.getTextMatrix().concatenate(Matrix.getTranslateInstance(tx, ty));
    }
  }

  /**
   * Resolves the given unicode string to its individual characters if the
   * string is a ligature. Returns null, if the string isn't a ligature.
   * 
   * @param unicode
   *          the unicode string to check.
   * 
   * @return the resolved characters in a string array if the input is a
   *         ligature; null otherwise.
   */
  protected String[] resolveLigature(String unicode) {
    return LIGATURES_MAP.get(unicode);
  }

  @Override
  public String getName() {
    return "Tj";
  }
  
  static {
    // Fill the ligatures map.
    LIGATURES_MAP = new HashMap<String, String[]>();
    LIGATURES_MAP.put("\u00C6", new String[] { "A", "E" }); // AE
    LIGATURES_MAP.put("\u00E6", new String[] { "a", "e" }); // ae
    LIGATURES_MAP.put("\u0152", new String[] { "O", "E" }); // OE
    LIGATURES_MAP.put("\u0153", new String[] { "o", "e" }); // oe
    LIGATURES_MAP.put("\u0132", new String[] { "I", "J" }); // IJ
    LIGATURES_MAP.put("\u0133", new String[] { "i", "j" }); // ij
    LIGATURES_MAP.put("\u1D6B", new String[] { "u", "e" }); // ue
    LIGATURES_MAP.put("\uA728", new String[] { "T", "Z" }); // TZ
    LIGATURES_MAP.put("\uA729", new String[] { "t", "z" }); // tz
    LIGATURES_MAP.put("\uA732", new String[] { "A", "A" }); // AA
    LIGATURES_MAP.put("\uA733", new String[] { "a", "a" }); // aa
    LIGATURES_MAP.put("\uA734", new String[] { "A", "O" }); // AO
    LIGATURES_MAP.put("\uA735", new String[] { "a", "o" }); // ao
    LIGATURES_MAP.put("\uA736", new String[] { "A", "U" }); // AU
    LIGATURES_MAP.put("\uA737", new String[] { "a", "u" }); // au
    LIGATURES_MAP.put("\uA738", new String[] { "A", "V" }); // AV
    LIGATURES_MAP.put("\uA739", new String[] { "a", "v" }); // av
    LIGATURES_MAP.put("\uA73C", new String[] { "A", "Y" }); // AY
    LIGATURES_MAP.put("\uA73D", new String[] { "a", "y" }); // ay
    LIGATURES_MAP.put("\uA74E", new String[] { "O", "O" }); // OO
    LIGATURES_MAP.put("\uA74F", new String[] { "o", "o" }); // oo
    LIGATURES_MAP.put("\uAB50", new String[] { "u", "i" }); // ui
    LIGATURES_MAP.put("\uFB00", new String[] { "f", "f" }); // ff
    LIGATURES_MAP.put("\uFB01", new String[] { "f", "i" }); // fi
    LIGATURES_MAP.put("\uFB02", new String[] { "f", "l" }); // fl
    LIGATURES_MAP.put("\uFB03", new String[] { "f", "f", "i" }); // ffi
    LIGATURES_MAP.put("\uFB04", new String[] { "f", "f", "l" }); // ffl
    LIGATURES_MAP.put("\uFB06", new String[] { "s", "t" }); // st
  }
}
