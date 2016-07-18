package parser.pdfbox.model;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.util.Matrix;

import de.freiburg.iif.model.Rectangle;
import model.DimensionStatistics;
import model.PdfCharacter;
import model.PdfFeature;
import model.PdfPage;
import model.PdfRole;
import model.TextStatistics;
import statistics.DimensionStatistician;
import statistics.TextStatistician;

/**
 * Concrete implementation of a PdfTextCharacter using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxCharacter extends PdfBoxArea implements PdfCharacter {
  /**
   * Adds non-decomposing diacritics to the hash with their related combining
   * character. These are values that the unicode spec claims are equivalent but
   * are not mapped in the form NFKC normalization method. Determined by going
   * through the Combining Diacritical Marks section of the Unicode spec and
   * identifying which characters are not mapped to by the normalization. For
   * example, maps "ACUTE ACCENT" to "COMBINING ACUTE ACCENT".
   */
  protected static final Map<Integer, String> DIACRITICS;

  /**
   * The rectangle.
   */
  protected Rectangle rectangle;

  /**
   * The textual content of this character in unicode.
   */
  protected String unicode;

  /**
   * The code of the character.
   */
  protected int code;

  /**
   * The color of this character.
   */
  protected PdfBoxColor color;

  /**
   * The font of this character.
   */
  protected PdfBoxFont font;

  /**
   * The font size in pt of this character.
   */
  protected float fontsize;

  /**
   * The extraction order number of this character.
   */
  protected int extractionOrderNumber;

  /**
   * The text rendering matrix. TODO: Do we really need this?
   */
  protected Matrix textRenderingMatrix;

  /**
   * The orientation.
   */
  protected float orientation = Float.MAX_VALUE;

  /**
   * This character wrapped in a list.
   */
  protected List<PdfCharacter> characterInList;

  /**
   * The role of this character.
   */
  protected PdfRole role = PdfRole.UNKNOWN;
  
  /**
   * The flag indicating if this character is a subscript.
   */
  protected boolean isSubScript;

  /**
   * The flag indicating if this character is a superscript.
   */
  protected boolean isSuperScript;

  /**
   * The flag indicating if this character is a punctuation mark.
   */
  protected boolean isPunctuationMark;

  /**
   * The flag indicating if this character is a diacritic.
   */
  protected boolean isDiacritic;
  
  /**
   * The flag to indicate whether this character has an encoding. 
   */
  protected boolean hasEncoding;
  
  // ___________________________________________________________________________
  // Constructor.

  /**
   * Creates an new PdfBoxTextCharacter from given unicode.
   */
  public PdfBoxCharacter(PdfPage page, String unicode) {
    super(page);
    this.unicode = unicode;
    this.characterInList = new ArrayList<>();
    this.characterInList.add(this);
    this.isDiacritic = computeIsDiacritic();
  }

  // ___________________________________________________________________________
  // Getters and setters.

  @Override
  public List<PdfCharacter> getTextCharacters() {
    return this.characterInList;
  }

  @Override
  public Rectangle getRectangle() {
    return this.rectangle;
  }

  /**
   * Sets the rectangle of this character.
   */
  public void setRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  @Override
  public String toString() {
    return getUnicode();
  }

  @Override
  public String getUnicode() {
    return unicode;
  }

  @Override
  public PdfBoxColor getColor() {
    return this.color;
  }

  /**
   * Sets the color of this shape.
   */
  public void setColor(PdfBoxColor color) {
    this.color = color;
  }

  @Override
  public PdfBoxFont getFont() {
    return this.font;
  }

  /**
   * Sets the font of this shape.
   */
  public void setFont(PdfBoxFont font) {
    this.font = font;
  }

  /**
   * Returns the character code.
   */
  public int getCharCode() {
    return code;
  }

  /**
   * Sets the character code.
   */
  public void setCharCode(int code) {
    this.code = code;
  }

  /**
   * Returns the font size in pt of this character.
   */
  public float getFontsize() {
    return fontsize;
  }

  /**
   * Sets the font size of this character (in pt).
   */
  public void setFontsize(float fontsize) {
    this.fontsize = fontsize;
  }

  /**
   * Returns the extraction order number of this character.
   */
  public int getExtractionOrderNumber() {
    return extractionOrderNumber;
  }

  /**
   * Sets the extraction order number of this character.
   */
  public void setExtractionOrderNumber(int extractionOrderNumber) {
    this.extractionOrderNumber = extractionOrderNumber;
  }

  /**
   * Returns the text rendering matrix of this character.
   */
  public Matrix getTextRenderingMatrix() {
    return textRenderingMatrix;
  }

  /**
   * Sets the text rendering matrix of this character.
   */
  public void setTextRenderingMatrix(Matrix textRenderingMatrix) {
    this.textRenderingMatrix = textRenderingMatrix;
  }

  @Override
  public float getOrientation() {
    if (orientation == Float.MAX_VALUE) {
      orientation = computeOrientation();
    }
    return orientation;
  }

  /**
   * Computes the orientation of this character.
   */
  protected float computeOrientation() {
    Matrix trm = getTextRenderingMatrix();
    float xScale = trm.getScaleX();
    float rotation = (float) Math.acos(trm.getValue(0, 0) / xScale);
    if (trm.getValue(0, 1) < 0 && trm.getValue(1, 0) > 0) {
      rotation = (-1) * rotation;
    }
    return (float) (rotation * 180 / Math.PI);
  }

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.characters;
  }

  @Override
  public DimensionStatistics computeDimensionStatistics() {
    return DimensionStatistician.compute(getTextCharacters());
  }

  @Override
  public TextStatistics computeTextStatistics() {
    TextStatistics s = TextStatistician.compute(getTextCharacters());

    return s;
  }

  @Override
  public int getCodePoint() {
    return code;
  }

  @Override
  public boolean isAscii() {
    for (PdfCharacter character : getTextCharacters()) {
      for (char chaar : character.getUnicode().toCharArray()) {
        if (chaar < 32 || chaar > 126) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean isDigit() {
    if (getTextCharacters() == null) {
      return false;
    }

    if (getTextCharacters().isEmpty()) {
      return false;
    }

    for (PdfCharacter character : getTextCharacters()) {
      if (character.getCodePoint() < '0' || character.getCodePoint() > '9') {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns true if the given text is a diacritic char.
   */
  public void setIsDiacritic(boolean diacritic) {
    this.isDiacritic = diacritic;
  }
  
  /**
   * Returns true if the given text is a diacritic char.
   */
  public boolean isDiacritic() {
    return this.isDiacritic;
  }
  
  /**
   * Returns true if the given text is a diacritic char.
   */
  protected boolean computeIsDiacritic() {
    String text = getUnicode();

    if (text == null || text.length() != 1) {
      return false;
    }

    int type = Character.getType(text.charAt(0));
    return type == Character.NON_SPACING_MARK
        || type == Character.MODIFIER_SYMBOL
        || type == Character.MODIFIER_LETTER;
  }

  @Override
  public void mergeDiacritic(PdfCharacter diacritic) {
    if (diacritic == null || !diacritic.isDiacritic()) {
      return;
    }

    this.unicode += resolveDiacritic(diacritic);
    
    // TODO: There is an issue where the characters may be located in different
    // columns / pages. So merging the rectangles would result in rectangle
    // spanning most of the page (and in bad paragraphs identification).
    // Example: 76.pdf. So for now, don't merge the rectangles.
//    this.rectangle = this.rectangle.union(diacritic.getRectangle());
  }

  protected String resolveDiacritic(PdfCharacter diacritic) {
    if (diacritic == null || diacritic.getUnicode() == null) {
      return "";
    }

    String str = diacritic.getUnicode();
    int codePoint = str.codePointAt(0);

    // convert the characters not defined in the Unicode spec
    if (DIACRITICS.containsKey(codePoint)) {
      return DIACRITICS.get(codePoint);
    } else {
      return Normalizer.normalize(str, Normalizer.Form.NFKC).trim();
    }
  }

  static {
    DIACRITICS = new HashMap<Integer, String>(31);
    DIACRITICS.put(0x0060, "\u0300");
    DIACRITICS.put(0x02CB, "\u0300");
    DIACRITICS.put(0x0027, "\u0301");
    DIACRITICS.put(0x02B9, "\u0301");
    DIACRITICS.put(0x02CA, "\u0301");
    DIACRITICS.put(0x005e, "\u0302");
    DIACRITICS.put(0x02C6, "\u0302");
    DIACRITICS.put(0x007E, "\u0303");
    DIACRITICS.put(0x02C9, "\u0304");
    DIACRITICS.put(0x00B0, "\u030A");
    DIACRITICS.put(0x02BA, "\u030B");
    DIACRITICS.put(0x02C7, "\u030C");
    DIACRITICS.put(0x02C8, "\u030D");
    DIACRITICS.put(0x0022, "\u030E");
    DIACRITICS.put(0x02BB, "\u0312");
    DIACRITICS.put(0x02BC, "\u0313");
    DIACRITICS.put(0x0486, "\u0313");
    DIACRITICS.put(0x055A, "\u0313");
    DIACRITICS.put(0x02BD, "\u0314");
    DIACRITICS.put(0x0485, "\u0314");
    DIACRITICS.put(0x0559, "\u0314");
    DIACRITICS.put(0x02D4, "\u031D");
    DIACRITICS.put(0x02D5, "\u031E");
    DIACRITICS.put(0x02D6, "\u031F");
    DIACRITICS.put(0x02D7, "\u0320");
    DIACRITICS.put(0x02B2, "\u0321");
    DIACRITICS.put(0x02CC, "\u0329");
    DIACRITICS.put(0x02B7, "\u032B");
    DIACRITICS.put(0x02CD, "\u0331");
    DIACRITICS.put(0x005F, "\u0332");
    DIACRITICS.put(0x204E, "\u0359");
  }

  @Override
  public boolean isSubScript() {
    return isSubScript;
  }

  @Override
  public void setIsSubScript(boolean isSubscript) {
    this.isSubScript = isSubscript;
  }

  @Override
  public boolean isSuperScript() {
    return isSuperScript;
  }

  @Override
  public void setIsSuperScript(boolean isSuperscript) {
    this.isSuperScript = isSuperscript;
  }

  @Override
  public boolean isPunctuationMark() {
    return isPunctuationMark;
  }

  @Override
  public void setIsPunctuationMark(boolean isPuncutationMark) {
    this.isPunctuationMark = isPuncutationMark;
  }

  @Override
  public String getText(boolean includePunctuationMarks,
      boolean includeSubscripts, boolean includeSuperscripts) {
    if ((!includePunctuationMarks && isPunctuationMark())
        || (!includeSubscripts && isSubScript())
        || (!includeSuperscripts && isSuperScript())) {
      return null;
    }

    return getUnicode();
  }
  
  @Override
  public void setRole(PdfRole role) {
    this.role = role;
  }
  
  @Override
  public PdfRole getRole() {
    return role;
  }

  @Override
  public boolean hasEncoding() {
    return hasEncoding;
  }
  
  /**
   * Sets the has encoding flag.
   */
  public void setHasEncoding(boolean hasEncoding) {
    this.hasEncoding = hasEncoding;
  }
}