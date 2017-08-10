package pdfact.serialize.plain;

import static pdfact.PdfActCoreSettings.DEFAULT_ENCODING;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.B;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.CHARACTER;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.COLOR;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.COLORS;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.DOCUMENT;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.FONT;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.FONTS;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.FONTSIZE;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.G;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.ID;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.MAX_X;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.MAX_Y;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.MIN_X;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.MIN_Y;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.NAME;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.PAGE;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.PARAGRAPH;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.PDF;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.POSITION;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.POSITIONS;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.R;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.TEXT;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.TEXT_BLOCK;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.TEXT_LINE;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.WORD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.exception.PdfActSerializeException;
import pdfact.models.HasColor;
import pdfact.models.HasElementType;
import pdfact.models.HasFontFace;
import pdfact.models.HasPosition;
import pdfact.models.HasPositions;
import pdfact.models.HasRole;
import pdfact.models.HasText;
import pdfact.models.PdfCharacter;
import pdfact.models.PdfColor;
import pdfact.models.PdfDocument;
import pdfact.models.PdfElement;
import pdfact.models.PdfElementType;
import pdfact.models.PdfFont;
import pdfact.models.PdfFontFace;
import pdfact.models.PdfPage;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfPosition;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfWord;
import pdfact.serialize.PdfSerializer;
import pdfact.serialize.PdfXmlSerializer;
import pdfact.utils.collection.CollectionUtils;
import pdfact.utils.geometric.Rectangle;
import pdfact.utils.text.StringUtils;

/**
 * An implementation of {@link PdfSerializer} that serializes a PDF document in
 * XML format.
 *
 * @author Claudius Korzen
 */
public class PlainPdfXmlSerializer implements PdfXmlSerializer {
  /**
   * The element types filter.
   */
  protected Set<PdfElementType> typesFilter;

  /**
   * The semantic roles filter.
   */
  protected Set<PdfRole> rolesFilter;

  /**
   * The fonts of the PDF elements which were in fact serialized.
   */
  protected Set<PdfFont> usedFonts;

  /**
   * The colors of the PDF elements which were in fact serialized.
   */
  protected Set<PdfColor> usedColors;

  /**
   * The indentation length.
   */
  protected static final int INDENT_LENGTH = 2;

  /**
   * The line delimiter to use on joining the individual lines.
   */
  protected static String LINE_DELIMITER = System.lineSeparator();

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new serializer that serializes a PDF document in XML format.
   */
  @AssistedInject
  public PlainPdfXmlSerializer() {
    this.usedFonts = new HashSet<>();
    this.usedColors = new HashSet<>();
  }

  /**
   * Creates a new serializer that serializes a PDF document in XML format.
   * 
   * @param typesFilter
   *        The element types filter.
   * @param rolesFilter
   *        The semantic roles filter.
   */
  @AssistedInject
  public PlainPdfXmlSerializer(@Assisted Set<PdfElementType> typesFilter,
      @Assisted Set<PdfRole> rolesFilter) {
    this();
    this.typesFilter = typesFilter;
    this.rolesFilter = rolesFilter;
  }

  // ==========================================================================

  @Override
  public byte[] serialize(PdfDocument pdf) throws PdfActSerializeException {
    // The current indentation level.
    int level = 0;
    // The serialization to return.
    String result = "";

    if (pdf != null) {
      // The individual lines of the serialization.
      List<String> resultLines = new ArrayList<>();

      // Start the XML document.
      resultLines.add(start(PDF, level++));

      // Create the section that contains all serialized PDF elements.
      List<String> pdfElementsLines = serializePdfElements(level + 1, pdf);
      if (pdfElementsLines != null && !pdfElementsLines.isEmpty()) {
        resultLines.add(start(DOCUMENT, level));
        resultLines.addAll(pdfElementsLines);
        resultLines.add(end(DOCUMENT, level));
      }

      // Create the section that contains the used fonts.
      List<String> fontsLines = serializeFonts(level + 1, this.usedFonts);
      if (fontsLines != null && !fontsLines.isEmpty()) {
        resultLines.add(start(FONTS, level));
        resultLines.addAll(fontsLines);
        resultLines.add(end(FONTS, level));
      }

      // Create the section that contains the used colors.
      List<String> colorsLines = serializeColors(level + 1, this.usedColors);
      if (colorsLines != null && !colorsLines.isEmpty()) {
        resultLines.add(start(COLORS, level));
        resultLines.addAll(colorsLines);
        resultLines.add(end(COLORS, level));
      }

      // End the XML document.
      resultLines.add(end(PDF, --level));

      // Join the lines to a single string.
      result = CollectionUtils.join(resultLines, LINE_DELIMITER);
    }

    return result.getBytes(DEFAULT_ENCODING);
  }

  // ==========================================================================

  /**
   * Serializes the elements of the given PDF document.
   * 
   * @param level
   *        The current indentation level.
   * @param pdf
   *        The PDF document to process.
   *
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializePdfElements(int level, PdfDocument pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      for (PdfParagraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        // Serialize the paragraph if paragraphs match the types filter.
        if (hasRelevantElementType(paragraph)) {
          List<String> paragraphLines = serializeParagraph(level, paragraph);
          if (paragraphLines != null) {
            result.addAll(paragraphLines);
          }
        }

        for (PdfTextBlock block : paragraph.getTextBlocks()) {
          // Serialize the text block if text blocks match the types filter.
          if (hasRelevantElementType(block)) {
            List<String> blockLines = serializeTextBlock(level, block);
            if (blockLines != null) {
              result.addAll(blockLines);
            }
          }
          for (PdfTextLine line : block.getTextLines()) {
            // Serialize the text line if text lines match the types filter.
            if (hasRelevantElementType(line)) {
              List<String> lineLines = serializeTextLine(level, line);
              if (lineLines != null) {
                result.addAll(lineLines);
              }
            }
            for (PdfWord word : line.getWords()) {
              // Serialize the word if words match the types filter.
              if (hasRelevantElementType(word)) {
                List<String> wordLines = serializeWord(level, word);
                if (wordLines != null) {
                  result.addAll(wordLines);
                }
              }
              for (PdfCharacter c : word.getCharacters()) {
                // Serialize the character if characters match the type filter.
                if (hasRelevantElementType(c)) {
                  List<String> characterLines = serializeCharacter(level, c);
                  if (characterLines != null) {
                    result.addAll(characterLines);
                  }
                }
              }
            }
          }
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given paragraph.
   * 
   * @param level
   *        The current indentation level.
   * @param paragraph
   *        The paragraph to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeParagraph(int level, PdfParagraph paragraph) {
    List<String> serialized = serializePdfElement(level + 1, paragraph);
    List<String> result = new ArrayList<>();
    // Wrap the serialized lines with a tag that describes paragraphs.
    if (serialized != null && !serialized.isEmpty()) {
      result.add(start(PARAGRAPH, level));
      result.addAll(serialized);
      result.add(end(PARAGRAPH, level));
    }
    return result;
  }

  /**
   * Serializes the given text block.
   * 
   * @param level
   *        The current indentation level.
   * @param block
   *        The text block to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeTextBlock(int level, PdfTextBlock block) {
    List<String> serialized = serializePdfElement(level + 1, block);
    List<String> result = new ArrayList<>();
    // Wrap the serialized lines with a tag that describes text blocks.
    if (serialized != null && !serialized.isEmpty()) {
      result.add(start(TEXT_BLOCK, level));
      result.addAll(serialized);
      result.add(end(TEXT_BLOCK, level));
    }
    return result;
  }

  /**
   * Serializes the given text line.
   * 
   * @param level
   *        The current indentation level.
   * @param line
   *        The text line to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeTextLine(int level, PdfTextLine line) {
    List<String> serialized = serializePdfElement(level + 1, line);
    List<String> result = new ArrayList<>();
    // Wrap the serialized lines with a tag that describes text lines.
    if (serialized != null && !serialized.isEmpty()) {
      result.add(start(TEXT_LINE, level));
      result.addAll(serialized);
      result.add(end(TEXT_LINE, level));
    }
    return result;
  }

  /**
   * Serializes the given word.
   * 
   * @param level
   *        The current indentation level.
   * @param word
   *        The word to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeWord(int level, PdfWord word) {
    List<String> serialized = serializePdfElement(level + 1, word);
    List<String> result = new ArrayList<>();
    // Wrap the serialized lines with a tag that describes words.
    if (serialized != null && !serialized.isEmpty()) {
      result.add(start(WORD, level));
      result.addAll(serialized);
      result.add(end(WORD, level));
    }
    return result;
  }

  /**
   * Serializes the given character.
   * 
   * @param level
   *        The current indentation level.
   * @param character
   *        The character to serialize.
   *
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeCharacter(int level, PdfCharacter character) {
    List<String> serialized = serializePdfElement(level + 1, character);
    List<String> result = new ArrayList<>();
    // Wrap the serialized lines with a tag that describes characters.
    if (serialized != null && !serialized.isEmpty()) {
      result.add(start(CHARACTER, level));
      result.addAll(serialized);
      result.add(end(CHARACTER, level));
    }
    return result;
  }

  /**
   * Serializes the given PDF element.
   * 
   * @param level
   *        The current indentation level.
   * @param element
   *        The PDF element to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializePdfElement(int level, PdfElement element) {
    List<String> result = new ArrayList<>();

    if (element != null) {
      // Serialize the list of positions of the element, if there is any.
      if (element instanceof HasPositions) {
        HasPositions hasPositions = (HasPositions) element;
        List<PdfPosition> positions = hasPositions.getPositions();

        List<String> serialized = serializePositions(level + 1, positions);
        if (serialized != null && !serialized.isEmpty()) {
          result.add(start(POSITIONS, level));
          result.addAll(serialized);
          result.add(end(POSITIONS, level));
        }
      }

      // Serialize the single position of the element, if there is any.
      if (element instanceof HasPosition) {
        HasPosition hasPosition = (HasPosition) element;
        PdfPosition position = hasPosition.getPosition();

        List<String> serialized = serializePositions(level + 1, position);
        if (serialized != null) {
          result.add(start(POSITIONS, level));
          result.addAll(serialized);
          result.add(end(POSITIONS, level));
        }
      }

      // Serialize the font face of the element, if there is any.
      if (element instanceof HasFontFace) {
        HasFontFace hasFontFace = (HasFontFace) element;
        PdfFontFace fontFace = hasFontFace.getFontFace();

        if (fontFace != null) {
          PdfFont font = fontFace.getFont();
          if (font != null) {
            String fontId = font.getId();
            float size = fontFace.getFontSize();
            if (fontId != null && size > 0) {
              result.add(start(FONT, level++));
              result.add(start(ID, level) + text(fontId) + end(ID));
              result.add(start(FONTSIZE, level) + text(size) + end(FONTSIZE));
              result.add(end(FONT, --level));
              this.usedFonts.add(font);
            }
          }
        }
      }

      // Serialize the color of the element, if there is any.
      if (element instanceof HasColor) {
        HasColor hasColor = (HasColor) element;
        PdfColor color = hasColor.getColor();

        if (color != null) {
          String colorId = color.getId();
          if (colorId != null) {
            result.add(start(COLOR, level++));
            result.add(start(ID, level) + text(colorId) + end(ID));
            result.add(end(COLOR, --level));
            this.usedColors.add(color);
          }
        }
      }

      // Serialize the text of the element, if there is any.
      if (element instanceof HasText) {
        HasText hasText = (HasText) element;
        String text = hasText.getText();

        if (text != null) {
          result.add(start(TEXT, level) + text(text) + end(TEXT));
        }
      }
    }

    return result;
  }

  // ==========================================================================

  /**
   * Serializes the given list of PDF positions.
   * 
   * @param level
   *        The current indentation level.
   * @param pos
   *        The list of positions to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializePositions(int level, PdfPosition... pos) {
    return serializePositions(level, Arrays.asList(pos));
  }

  /**
   * Serializes the given list of PDF positions.
   * 
   * @param level
   *        The current indentation level.
   * @param pos
   *        The list of positions to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializePositions(int level, List<PdfPosition> pos) {
    List<String> result = new ArrayList<>();
    if (pos != null) {
      for (PdfPosition position : pos) {
        List<String> positionLines = serializePosition(level, position);
        if (positionLines != null) {
          result.addAll(positionLines);
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given PDF position.
   * 
   * @param level
   *        The current indentation level.
   * @param position
   *        The position to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializePosition(int level, PdfPosition position) {
    List<String> result = new ArrayList<>();

    if (position != null) {
      PdfPage page = position.getPage();
      int pageNumber = page.getPageNumber();
      Rectangle rect = position.getRectangle();

      if (pageNumber > 0 && rect != null) {
        result.add(start(POSITION, level++));

        result.add(start(PAGE, level) + text(pageNumber) + end(PAGE));
        result.add(start(MIN_X, level) + text(rect.getMinX()) + end(MIN_X));
        result.add(start(MIN_Y, level) + text(rect.getMinY()) + end(MIN_Y));
        result.add(start(MAX_X, level) + text(rect.getMaxX()) + end(MAX_X));
        result.add(start(MAX_Y, level) + text(rect.getMaxY()) + end(MAX_Y));

        result.add(end(POSITION, --level));
      }
    }

    return result;
  }

  // ==========================================================================

  /**
   * Serializes the given fonts.
   * 
   * @param level
   *        The current indentation level.
   * @param fonts
   *        The fonts to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeFonts(int level, PdfFont... fonts) {
    return serializeFonts(level, new HashSet<>(Arrays.asList(fonts)));
  }

  /**
   * Serializes the given fonts.
   *
   * @param level
   *        The current indentation level.
   * @param fonts
   *        The fonts to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeFonts(int level, Set<PdfFont> fonts) {
    List<String> result = new ArrayList<>();
    if (fonts != null) {
      for (PdfFont font : fonts) {
        List<String> fontLines = serializeFont(level, font);
        if (fontLines != null) {
          result.addAll(fontLines);
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given font.
   * 
   * @param level
   *        The current indentation level.
   * @param font
   *        The font to serialize. 
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeFont(int level, PdfFont font) {
    List<String> result = new ArrayList<>();
    if (font != null) {
      String fontId = font.getId();
      String fontName = font.getNormalizedName();

      if (fontId != null && fontName != null) {
        result.add(start(FONT, level++));
        result.add(start(ID, level) + text(fontId) + end(ID));
        result.add(start(NAME, level) + text(fontName) + end(NAME));
        result.add(end(FONT, --level));
      }
    }
    return result;
  }

  // ==========================================================================
  // Methods to serialize colors.

  /**
   * Serializes the given colors.
   * 
   * @param level
   *        The current indentation level.
   * @param colors
   *        The colors to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeColors(int level, PdfColor... colors) {
    return serializeColors(level, new HashSet<>(Arrays.asList(colors)));
  }

  /**
   * Serializes the given colors.
   * 
   * @param level
   *        The current indentation level.
   * @param colors
   *        The colors to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeColors(int level, Set<PdfColor> colors) {
    List<String> result = new ArrayList<>();
    for (PdfColor color : colors) {
      List<String> colorLines = serializeColor(level, color);
      if (colorLines != null) {
        result.addAll(colorLines);
      }
    }
    return result;
  }

  /**
   * Serializes the given color.
   * 
   * @param level
   *        The current indentation level.
   * @param color
   *        The color to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeColor(int level, PdfColor color) {
    List<String> result = new ArrayList<>();
    if (color != null) {
      float[] rgb = color.getRGB();
      if (rgb != null && rgb.length == 3) {
        result.add(start(COLOR, level++));
        result.add(start(ID, level) + text(color.getId()) + end(ID));
        result.add(start(R, level) + text(rgb[0]) + end(R));
        result.add(start(G, level) + text(rgb[1]) + end(G));
        result.add(start(B, level) + text(rgb[2]) + end(B));
        result.add(end(COLOR, --level));
      }
    }
    return result;
  }

  // ==========================================================================

  /**
   * Wraps the given text in an XML start tag.
   * 
   * @param text
   *        The text to wrap.
   * 
   * @return The given text wrapped in an XML start tag.
   */
  protected String startTag(String text) {
    return start(text, 0);
  }

  /**
   * Wraps the given text in an XML start tag and indents it by the given
   * indentation level.
   * 
   * @param text
   *        The text to wrap.
   * @param level
   *        The indentation level.
   * 
   * @return The given text wrapped in an XML start tag, indented by the given
   *         indentation level.
   */
  protected String start(String text, int level) {
    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);
    return indent + "<" + text + ">";
  }

  /**
   * Wraps the given text in an XML end tag.
   * 
   * @param text
   *        The text to wrap.
   * 
   * @return The given text wrapped in an XML end tag.
   */
  protected String end(String text) {
    return end(text, 0);
  }

  /**
   * Wraps the given text in an XML end tag and indents it by the given
   * indentation level.
   * 
   * @param text
   *        The text to wrap.
   * @param level
   *        The indentation level.
   * 
   * @return The given text wrapped in an XML end tag, indented by the given
   *         indentation level.
   */
  protected String end(String text, int level) {
    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);
    return indent + "</" + text + ">";
  }

  /**
   * Transform the given object to an XML escaped string.
   * 
   * @param obj
   *        The object to process.
   * 
   * @return The XML escaped string.
   */
  protected String text(Object obj) {
    return text(obj, 0);
  }

  /**
   * Transform the given object to an XML escaped string and indents it by the
   * given indentation level.
   * 
   * @param obj
   *        The object to process.
   * @param level
   *        The indentation level.
   *
   * @return The XML escaped string, indented by the given indentation level.
   */
  protected String text(Object obj, int level) {
    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);
    String text = StringEscapeUtils.escapeXml11(obj.toString());
    return indent + text;
  }

  // ==========================================================================

  @Override
  public Set<PdfElementType> getElementTypeFilters() {
    return this.typesFilter;
  }

  @Override
  public void setElementTypeFilters(Set<PdfElementType> types) {
    this.typesFilter = types;
  }

  // ==========================================================================

  @Override
  public Set<PdfRole> getElementRoleFilters() {
    return this.rolesFilter;
  }

  @Override
  public void setElementRoleFilters(Set<PdfRole> roles) {
    this.rolesFilter = roles;
  }

  // ==========================================================================

  /**
   * Checks if the semantic role of the given element matches the semantic
   * roles filter of this serializer.
   * 
   * @param element
   *        The element to check.
   * 
   * @return True, if the role of the given element matches the semantic roles
   *         filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantRole(HasRole element) {
    if (element == null) {
      return false;
    }

    if (this.rolesFilter == null || this.rolesFilter.isEmpty()) {
      // No filter is given -> The paragraph is relevant.
      return true;
    }

    PdfRole role = element.getRole();
    if (role == null) {
      return false;
    }

    return this.rolesFilter.contains(role);
  }

  /**
   * Checks if the type of the given PDF element matches the element type
   * filter of this serializer.
   * 
   * @param element
   *        The PDF element to check.
   *
   * @return True, if the type of the given PDF element matches the element
   *         type filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantElementType(HasElementType element) {
    if (element == null) {
      return false;
    }

    if (this.typesFilter == null || this.typesFilter.isEmpty()) {
      // No filter is given -> The element is relevant.
      return true;
    }

    PdfElementType elementType = element.getType();
    if (elementType == null) {
      return false;
    }

    return this.typesFilter.contains(elementType);
  }
}
