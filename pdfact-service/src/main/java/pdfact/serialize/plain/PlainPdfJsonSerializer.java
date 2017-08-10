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
import static pdfact.serialize.plain.PlainPdfSerializerConstants.POSITIONS;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.R;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.TEXT;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.TEXT_BLOCK;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.TEXT_LINE;
import static pdfact.serialize.plain.PlainPdfSerializerConstants.WORD;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

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
import pdfact.serialize.PdfJsonSerializer;
import pdfact.serialize.PdfSerializer;
import pdfact.utils.geometric.Rectangle;

/**
 * An implementation of {@link PdfSerializer} that serializes a PDF document in
 * JSON format.
 *
 * @author Claudius Korzen
 */
public class PlainPdfJsonSerializer implements PdfJsonSerializer {
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

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new serializer that serializes a PDF document in JSON format.
   */
  @AssistedInject
  public PlainPdfJsonSerializer() {
    this.usedFonts = new HashSet<>();
    this.usedColors = new HashSet<>();
  }

  /**
   * Creates a new serializer that serializes a PDF document in JSON format.
   * 
   * @param typesFilter
   *        The element types filter.
   * @param rolesFilter
   *        The semantic roles filter.
   */
  @AssistedInject
  public PlainPdfJsonSerializer(@Assisted Set<PdfElementType> typesFilter,
      @Assisted Set<PdfRole> rolesFilter) {
    this();
    this.typesFilter = typesFilter;
    this.rolesFilter = rolesFilter;
  }

  // ==========================================================================

  @Override
  public byte[] serialize(PdfDocument pdf) {
    // The serialization to return.
    String result = "";

    if (pdf != null) {
      // The JSON object to fill.
      JSONObject json = new JSONObject();

      // Create the section that contains all serialized PDF elements.
      JSONArray elementsJson = serializePdfElements(pdf);
      if (elementsJson != null && elementsJson.length() > 0) {
        json.put(DOCUMENT, elementsJson);
      }

      // Create the section that contains the used fonts.
      JSONArray fontsJson = serializeFonts(this.usedFonts);
      if (fontsJson != null && fontsJson.length() > 0) {
        json.put(FONTS, fontsJson);
      }

      // Create the section that contains the used colors.
      JSONArray colorsJson = serializeColors(this.usedColors);
      if (colorsJson != null && colorsJson.length() > 0) {
        json.put(COLORS, colorsJson);
      }

      // Serialize the JSON object.
      result = json.toString(INDENT_LENGTH);
    }
    return result.getBytes(DEFAULT_ENCODING);
  }

  // ==========================================================================

  /**
   * Serializes the elements of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   *
   * @return A JSON array that represent the serialized elements.
   */
  protected JSONArray serializePdfElements(PdfDocument pdf) {
    JSONArray result = new JSONArray();

    if (pdf != null) {
      for (PdfParagraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        // Serialize the paragraph if paragraphs match the types filter.
        if (hasRelevantElementType(paragraph)) {
          JSONObject paragraphJson = serializeParagraph(paragraph);
          if (paragraphJson != null && paragraphJson.length() > 0) {
            result.put(paragraphJson);
          }
        }

        for (PdfTextBlock block : paragraph.getTextBlocks()) {
          // Serialize the text block if text blocks match the types filter.
          if (hasRelevantElementType(block)) {
            JSONObject blockJson = serializeTextBlock(block);
            if (blockJson != null && blockJson.length() > 0) {
              result.put(blockJson);
            }
          }
          for (PdfTextLine line : block.getTextLines()) {
            // Serialize the text line if text lines match the types filter.
            if (hasRelevantElementType(line)) {
              JSONObject lineJson = serializeTextLine(line);
              if (lineJson != null && lineJson.length() > 0) {
                result.put(lineJson);
              }
            }
            for (PdfWord word : line.getWords()) {
              // Serialize the word if words match the types filter.
              if (hasRelevantElementType(word)) {
                JSONObject wordJson = serializeWord(word);
                if (wordJson != null && wordJson.length() > 0) {
                  result.put(wordJson);
                }
              }
              for (PdfCharacter character : word.getCharacters()) {
                // Serialize the character if characters match the type filter.
                if (hasRelevantElementType(character)) {
                  JSONObject characterJson = serializeCharacter(character);
                  if (characterJson != null && characterJson.length() > 0) {
                    result.put(characterJson);
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

  // ==========================================================================

  /**
   * Serializes the given paragraph.
   * 
   * @param paragraph
   *        The paragraph to serialize.
   * 
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeParagraph(PdfParagraph paragraph) {
    JSONObject paragraphJson = serializePdfElement(paragraph);
    JSONObject result = new JSONObject();
    // Wrap the JSON object with a JSON object that describes paragraphs.
    if (paragraphJson != null && paragraphJson.length() > 0) {
      result.put(PARAGRAPH, paragraphJson);
    }
    return result;
  }

  /**
   * Serializes the given text block.
   * 
   * @param block
   *        The text block to serialize.
   * 
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeTextBlock(PdfTextBlock block) {
    JSONObject blockJson = serializePdfElement(block);
    JSONObject result = new JSONObject();
    // Wrap the JSON object with a JSON object that describes text blocks.
    if (blockJson != null && blockJson.length() > 0) {
      result.put(TEXT_BLOCK, blockJson);
    }
    return result;
  }

  /**
   * Serializes the given text line.
   * 
   * @param line
   *        The text line to serialize.
   * 
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeTextLine(PdfTextLine line) {
    JSONObject lineJson = serializePdfElement(line);
    JSONObject result = new JSONObject();
    // Wrap the JSON object with a JSON object that describes text lines.
    if (lineJson != null && lineJson.length() > 0) {
      result.put(TEXT_LINE, lineJson);
    }
    return result;
  }

  /**
   * Serializes the given word.
   * 
   * @param word
   *        The word to serialize.
   * 
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeWord(PdfWord word) {
    JSONObject wordJson = serializePdfElement(word);
    JSONObject result = new JSONObject();
    // Wrap the JSON object with a JSON object that describes words.
    if (wordJson != null && wordJson.length() > 0) {
      result.put(WORD, wordJson);
    }
    return result;
  }

  /**
   * Serializes the given character.
   * 
   * @param character
   *        The character to serialize.
   *
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeCharacter(PdfCharacter character) {
    JSONObject characterJson = serializePdfElement(character);
    JSONObject result = new JSONObject();
    // Wrap the JSON object with a JSON object that describes characters.
    if (characterJson != null && characterJson.length() > 0) {
      result.put(CHARACTER, characterJson);
    }
    return result;
  }

  /**
   * Serializes the given PDF element.
   * 
   * @param element
   *        The element to serialize.
   * 
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializePdfElement(PdfElement element) {
    JSONObject result = new JSONObject();

    if (element != null) {
      // Serialize the list of positions of the element, if there is any.
      if (element instanceof HasPositions) {
        HasPositions hasPositions = (HasPositions) element;
        List<PdfPosition> positions = hasPositions.getPositions();

        JSONArray serialized = serializePositions(positions);
        if (serialized != null && serialized.length() > 0) {
          result.put(POSITIONS, serialized);
        }
      }

      // Serialize the single position of the element, if there is any.
      if (element instanceof HasPosition) {
        HasPosition hasPosition = (HasPosition) element;
        PdfPosition position = hasPosition.getPosition();

        JSONArray serialized = serializePositions(position);
        if (serialized != null && serialized.length() > 0) {
          result.put(POSITIONS, serialized);
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
              JSONObject fontJson = new JSONObject();
              fontJson.put(ID, fontId);
              fontJson.put(FONTSIZE, size);
              result.put(FONT, fontJson);
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
            JSONObject colorJson = new JSONObject();
            colorJson.put(ID, colorId);
            result.put(COLOR, colorJson);
            this.usedColors.add(color);
          }
        }
      }

      // Serialize the text of the element, if there is any.
      if (element instanceof HasText) {
        HasText hasText = (HasText) element;
        String text = hasText.getText();

        if (text != null) {
          result.put(TEXT, text);
        }
      }
    }

    return result;
  }

  // ==========================================================================

  /**
   * Serializes the given PDF positions.
   * 
   * @param positions
   *        The positions to serialize.
   * 
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializePositions(PdfPosition... positions) {
    return serializePositions(Arrays.asList(positions));
  }

  /**
   * Serializes the given PDF positions.
   * 
   * @param positions
   *        The list of positions to serialize.
   * 
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializePositions(List<PdfPosition> positions) {
    JSONArray result = new JSONArray();
    if (positions != null) {
      for (PdfPosition position : positions) {
        JSONObject positionJson = serializePosition(position);
        if (positionJson != null && positionJson.length() > 0) {
          result.put(positionJson);
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given PDF position.
   * 
   * @param position
   *        The position to serialize.
   * 
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializePosition(PdfPosition position) {
    JSONObject positionJson = new JSONObject();
    if (position != null) {
      PdfPage page = position.getPage();
      Rectangle rect = position.getRectangle();

      if (page != null && rect != null) {
        positionJson.put(PAGE, page.getPageNumber());
        positionJson.put(MIN_X, rect.getMinX());
        positionJson.put(MIN_Y, rect.getMinY());
        positionJson.put(MAX_X, rect.getMaxX());
        positionJson.put(MAX_Y, rect.getMaxY());
      }
    }
    return positionJson;
  }

  // ==========================================================================
  // Methods to serialize fonts.

  /**
   * Serializes the given fonts.
   * 
   * @param fonts
   *        The fonts to serialize.
   * 
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializeFonts(PdfFont... fonts) {
    return serializeFonts(new HashSet<>(Arrays.asList(fonts)));
  }

  /**
   * Serializes the given fonts.
   * 
   * @param fonts
   *        The fonts to serialize.
   * 
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializeFonts(Set<PdfFont> fonts) {
    JSONArray result = new JSONArray();
    if (fonts != null) {
      for (PdfFont font : fonts) {
        JSONObject fontJson = serializeFont(font);
        if (fontJson != null && fontJson.length() > 0) {
          result.put(fontJson);
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given font.
   * 
   * @param font
   *        The font to serialize.
   * 
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeFont(PdfFont font) {
    JSONObject fontJson = new JSONObject();
    if (font != null) {
      String fontId = font.getId();
      String fontName = font.getNormalizedName();

      if (fontId != null && fontName != null) {
        fontJson.put(ID, fontId);
        fontJson.put(NAME, fontName);
      }
    }
    return fontJson;
  }

  // ==========================================================================
  // Methods to serialize colors.

  /**
   * Serializes the given colors.
   * 
   * @param colors
   *        The color to serialize.
   * 
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializeColors(PdfColor... colors) {
    return serializeColors(new HashSet<>(Arrays.asList(colors)));
  }

  /**
   * Serializes the given colors.
   * 
   * @param colors
   *        The colors to serialize.
   * 
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializeColors(Set<PdfColor> colors) {
    JSONArray result = new JSONArray();

    if (colors != null) {
      for (PdfColor color : colors) {
        if (color != null) {
          JSONObject colorJson = serializeColor(color);
          if (colorJson != null && colorJson.length() > 0) {
            result.put(colorJson);
          }
        }
      }
    }
    
    return result;
  }

  /**
   * Serializes the given color.
   * 
   * @param color
   *        The color to serialize.
   * 
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeColor(PdfColor color) {
    JSONObject colorJson = new JSONObject();
    if (color != null) {
      String colorId = color.getId();
      float[] rgb = color.getRGB();

      if (colorId != null && rgb != null && rgb.length == 3) {
        colorJson.put(ID, colorId);
        colorJson.put(R, rgb[0]);
        colorJson.put(G, rgb[1]);
        colorJson.put(B, rgb[2]);
      }
    }
    return colorJson;
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
