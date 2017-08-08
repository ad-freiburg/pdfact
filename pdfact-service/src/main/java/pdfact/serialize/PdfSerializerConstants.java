package pdfact.serialize;

/**
 * Collection of some constants that are needed on serialization.
 * 
 * @author Claudius Korzen
 *
 */
public class PdfSerializerConstants {
  /** The number of spaces to add to each level of indentation. */
  static final int INDENT_LENGTH = 4;

  /** The name of context where to put the serialized document. */
  static final String CONTEXT_NAME_DOCUMENT = "document";
  /** The name of context where to put the serialized pages. */
  static final String CONTEXT_NAME_PAGES = "pages";
  /** The name of context where to put the feature. */
  static final String CONTEXT_NAME_FEATURE = "feature";
  /** The name of context where to put the paragraphs. */
  static final String CONTEXT_NAME_PARAGRAPHS = "paragraphs";
  /** The name of context where to put a single paragraph. */
  static final String CONTEXT_NAME_PARAGRAPH = "paragraph";
  /** The name of context where to put the text blocks. */
  static final String CONTEXT_NAME_TEXT_BLOCKS = "blocks";
  /** The name of context where to put a single text block. */
  static final String CONTEXT_NAME_TEXT_BLOCK = "block";
  /** The name of context where to put the text lines. */
  static final String CONTEXT_NAME_TEXTLINES = "textlines";
  /** The name of context where to put a single text line. */
  static final String CONTEXT_NAME_TEXTLINE = "textline";
  /** The name of context where to put the words. */
  static final String CONTEXT_NAME_WORDS = "words";
  /** The name of context where to put a single word. */
  static final String CONTEXT_NAME_WORD = "word";
  /** The name of context where to put the characters. */
  static final String CONTEXT_NAME_CHARACTERS = "characters";
  /** The name of context where to put a single character. */
  static final String CONTEXT_NAME_CHARACTER = "character";
  /** The name of context where to put the figures. */
  static final String CONTEXT_NAME_FIGURES = "figures";
  /** The name of context where to put a single figure. */
  static final String CONTEXT_NAME_FIGURE = "figure";
  /** The name of context where to put the shapes. */
  static final String CONTEXT_NAME_SHAPES = "shapes";
  /** The name of context where to put a single shape. */
  static final String CONTEXT_NAME_SHAPE = "shape";
  /** The name of context where to put the serialized fonts. */
  static final String CONTEXT_NAME_FONTS = "fonts";
  /** The name of context where to put a single serialized font. */
  static final String CONTEXT_NAME_FONT = "font";
  /** The name of context where to put the serialized colors. */
  static final String CONTEXT_NAME_COLORS = "colors";
  /** The name of context where to put a single serialized color. */
  static final String CONTEXT_NAME_COLOR = "color";
  /**
   * The name of context where to put the text of a serialized text element.
   */
  static final String CONTEXT_NAME_ELEMENT_TEXT = "text";
  /** The name of context where to put the page of a serialized element. */
  static final String CONTEXT_NAME_ELEMENT_PAGE = "page";
  /** The name of context where to put the role of a serialized element. */
  static final String CONTEXT_NAME_ELEMENT_ROLE = "role";
  /**
   * The name of context where to put the bound box of a serialized element.
   */
  static final String CONTEXT_NAME_ELEMENT_BOUNDING_BOX = "bounding-box";
  /** The name of context where to put the minX of a serialized element. */
  static final String CONTEXT_NAME_ELEMENT_MIN_X = "minX";
  /** The name of context where to put the maxX of a serialized element. */
  static final String CONTEXT_NAME_ELEMENT_MAX_X = "maxX";
  /** The name of context where to put the minY of a serialized element. */
  static final String CONTEXT_NAME_ELEMENT_MIN_Y = "minY";
  /** The name of context where to put the maxY of a serialized element. */
  static final String CONTEXT_NAME_ELEMENT_MAX_Y = "maxY";
  /**
   * The name of context where to put the font of a serialized text element.
   */
  static final String CONTEXT_NAME_ELEMENT_FONT = "font";
  /** The name of context where to put the font size of a text element. */
  static final String CONTEXT_NAME_ELEMENT_FONT_SIZE = "font-size";
  /** The name of context where to put the id of a serialized font. */
  static final String CONTEXT_NAME_FONT_ID = "font-id";
  /** The name of context where to put the name of a serialized font. */
  static final String CONTEXT_NAME_FONT_NAME = "font-name";
  /** The name of context where to put the bold flag of a serialized font. */
  static final String CONTEXT_NAME_FONT_IS_BOLD = "bold";
  /** The name of context where to put the italic flag of a serialized font. */
  static final String CONTEXT_NAME_FONT_IS_ITALIC = "italic";
  /** The name of context where to put the type3 flag of a serialized font. */
  static final String CONTEXT_NAME_FONT_IS_TYPE3 = "type3";

  /** The name of context where to put the color of a serialized element. */
  static final String CONTEXT_NAME_ELEMENT_COLOR = "color";
  /** The name of context where to put the id of a serialized color. */
  static final String CONTEXT_NAME_COLOR_ID = "color-id";
  /** The name of context where to put the r-value of a serialized color. */
  static final String CONTEXT_NAME_COLOR_R = "r";
  /** The name of context where to put the g-value of a serialized color. */
  static final String CONTEXT_NAME_COLOR_G = "g";
  /** The name of context where to put the b-value of a serialized color. */
  static final String CONTEXT_NAME_COLOR_B = "b";
}