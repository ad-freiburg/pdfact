package serializer;

/**
 * Collection of some constants that are needed on serialization.
 * 
 * @author Claudius Korzen
 *
 */
public class PdfSerializerConstants {
  /** The number of spaces to add to each level of indentation. */
  protected static final int INDENT_LENGTH = 4;

  /** The name of context where to put the serialized document. */
  protected static final String CONTEXT_NAME_DOCUMENT = "document";
  /** The name of context where to put the serialized pages. */
  protected static final String CONTEXT_NAME_PAGES = "pages";
  /** The name of context where to put the feature. */
  protected static final String CONTEXT_NAME_FEATURE = "feature";
  /** The name of context where to put the serialized fonts. */
  protected static final String CONTEXT_NAME_FONTS = "fonts";
  /** The name of context where to put the serialized colors. */
  protected static final String CONTEXT_NAME_COLORS = "colors";
  /** The name of context where to put the text of a serialized text element. */
  protected static final String CONTEXT_NAME_ELEMENT_TEXT = "text";
  /** The name of context where to put the page of a serialized element. */
  protected static final String CONTEXT_NAME_ELEMENT_PAGE = "page";
  /** The name of context where to put the role of a serialized element. */
  protected static final String CONTEXT_NAME_ELEMENT_ROLE = "role";
  /** The name of context where to put the bound box of a serialized element. */
  protected static final String CONTEXT_NAME_ELEMENT_BOUNDING_BOX =
      "boundingBox";
  /** The name of context where to put the minX of a serialized element. */
  protected static final String CONTEXT_NAME_ELEMENT_MIN_X = "minX";
  /** The name of context where to put the maxX of a serialized element. */
  protected static final String CONTEXT_NAME_ELEMENT_MAX_X = "maxX";
  /** The name of context where to put the minY of a serialized element. */
  protected static final String CONTEXT_NAME_ELEMENT_MIN_Y = "minY";
  /** The name of context where to put the maxY of a serialized element. */
  protected static final String CONTEXT_NAME_ELEMENT_MAX_Y = "maxY";
  /** The name of context where to put the font of a serialized text element. */
  protected static final String CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT =
      "mostCommonFont";
  /** The name of context where to put the fontsize of a text element. */
  protected static final String CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT_SIZE =
      "mostCommonFontsize";
  /** The name of context where to put the font of the first character. */
  protected static final String CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_FONT =
      "startFont";
  /** The name of context where to put the fontsize of the first character. */
  protected static final String CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_FONTSIZE =
      "startFontsize";
  /** The name of context where to put the font of the last character. */
  protected static final String CONTEXT_NAME_ELEMENT_LAST_CHARACTER_FONT =
      "endFont";
  /** The name of context where to put the fontsize of the last character. */
  protected static final String CONTEXT_NAME_ELEMENT_LAST_CHARACTER_FONTSIZE =
      "endFontsize";
  /** The name of context where to put the color of a serialized element. */
  protected static final String CONTEXT_NAME_ELEMENT_MOST_COMMON_COLOR =
      "mostCommonColor";
  /** The name of context where to put the color of the first character. */
  protected static final String CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_COLOR =
      "startColor";
  /** The name of context where to put the color of the last character. */
  protected static final String CONTEXT_NAME_ELEMENT_LAST_CHARACTER_COLOR =
      "endColor";
  /** The name of context where to put the id of a serialized font. */
  protected static final String CONTEXT_NAME_FONT_ID = "id";
  /** The name of context where to put the name of a serialized font. */
  protected static final String CONTEXT_NAME_FONT_NAME = "name";
  /** The name of context where to put the bold flag of a serialized font. */
  protected static final String CONTEXT_NAME_FONT_IS_BOLD = "bold";
  /** The name of context where to put the italic flag of a serialized font. */
  protected static final String CONTEXT_NAME_FONT_IS_ITALIC = "italic";
  /** The name of context where to put the type3 flag of a serialized font. */
  protected static final String CONTEXT_NAME_FONT_IS_TYPE3 = "type3";
  /** The name of context where to put the id of a serialized color. */
  protected static final String CONTEXT_NAME_COLOR_ID = "id";
  /** The name of context where to put the r-value of a serialized color. */
  protected static final String CONTEXT_NAME_COLOR_R = "r";
  /** The name of context where to put the g-value of a serialized color. */
  protected static final String CONTEXT_NAME_COLOR_G = "g";
  /** The name of context where to put the b-value of a serialized color. */
  protected static final String CONTEXT_NAME_COLOR_B = "b";
}
