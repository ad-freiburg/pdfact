package pdfact.serialize;

import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_CHARACTERS;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLORS;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_B;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_G;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_ID;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_R;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_COLOR;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FONT;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FONT_SIZE;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_X;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_Y;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_X;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_Y;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_TEXT;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_FIGURES;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_FONTS;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_FONT_ID;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_FONT_NAME;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_PAGES;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_PARAGRAPHS;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_SHAPES;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_TEXTLINES;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_TEXT_BLOCKS;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_WORDS;
import static pdfact.serialize.PdfSerializerConstants.INDENT_LENGTH;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.HasColor;
import pdfact.models.HasFontFace;
import pdfact.models.HasText;
import pdfact.models.PdfCharacter;
import pdfact.models.PdfColor;
import pdfact.models.PdfDocument;
import pdfact.models.PdfElement;
import pdfact.models.PdfFont;
import pdfact.models.PdfFontFace;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfTextUnit;
import pdfact.models.PdfWord;
import pdfact.utils.geometric.Rectangle;

/**
 * An implementation of PdfSerializer that serializes a PDF document to JSON
 * format.
 *
 * @author Claudius Korzen
 */
public class JsonPdfSerializer implements PdfSerializer {
  /**
   * The text units to include on serialization.
   */
  protected Set<PdfTextUnit> units;

  /**
   * The roles of text units to include on serialization.
   */
  protected Set<PdfRole> roles;

  /**
   * The set of utilized fonts while serializing a PDF document.
   */
  protected Set<PdfFont> usedFonts;

  /**
   * The set of utilized colors while serializing a PDF document.
   */
  protected Set<PdfColor> usedColors;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new serializer that serializes PDF documents to JSON format.
   */
  @AssistedInject
  public JsonPdfSerializer() {

  }

  /**
   * Creates a new serializer that serializes PDF documents to JSON format.
   * 
   * @param units
   *        The text units to include on serialization.
   * @param roles
   *        The roles of text units to include on serialization.
   */
  @AssistedInject
  public JsonPdfSerializer(Set<PdfTextUnit> units, Set<PdfRole> roles) {
    this.units = units;
    this.roles = roles;
  }

  // ==========================================================================

  /**
   * Returns the output format of this serializer.
   * 
   * @return The output format of this serializer.
   */
  public static PdfActSerializationFormat getSerializationFormat() {
    return PdfActSerializationFormat.JSON;
  }

  // ==========================================================================

  @Override
  public byte[] serialize(PdfDocument pdf) {
    if (pdf == null) {
      return "".getBytes(StandardCharsets.UTF_8);
    }

    // The used fonts on serializing the PDF document.
    this.usedFonts = new HashSet<>();
    // The used colors on serializing the PDF document.
    this.usedColors = new HashSet<>();

    // The JSON object to fill.
    JSONObject json = new JSONObject();

    // Serialize the pages.
    JSONObject serializedPdf = serializePdfDocument(pdf);
    if (serializedPdf != null && serializedPdf.length() > 0) {
      json.put(CONTEXT_NAME_PAGES, serializedPdf);
    }

    // Serialize the fonts.
    JSONArray serializedFonts = serializeFonts();
    if (serializedFonts != null && serializedFonts.length() > 0) {
      json.put(CONTEXT_NAME_FONTS, serializedFonts);
    }

    // // Serialize the colors.
    JSONArray serializedColors = serializeColors();
    if (serializedColors != null && serializedColors.length() > 0) {
      json.put(CONTEXT_NAME_COLORS, serializedColors);
    }

    // Write the serialization to given stream.
    return json.toString(INDENT_LENGTH).getBytes(StandardCharsets.UTF_8);
  }

  // ==========================================================================

  /**
   * Serializes the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return An JSON array representing the serialized pages of the PDF
   *         document.
   */
  protected JSONObject serializePdfDocument(PdfDocument pdf) {
    if (pdf == null) {
      return null;
    }

    // Define a JSON object for the whole pages.
    JSONObject json = new JSONObject();

    // Define JSON arrays for each PDF element type.
    JSONArray paragraphs = new JSONArray();
    JSONArray blocks = new JSONArray();
    JSONArray textLines = new JSONArray();
    JSONArray words = new JSONArray();
    JSONArray characters = new JSONArray();
    JSONArray figures = new JSONArray();
    JSONArray shapes = new JSONArray();

    for (PdfParagraph paragraph : pdf.getParagraphs()) {
      // Ignore the paragraph if its role doesn't match the roles filter.
      if (!hasRelevantRole(paragraph)) {
        continue;
      }

      // Serialize the paragraph if paragraphs should be included.
      if (isRelevantTextUnit(paragraph)) {
        JSONObject serializedParagraph = serializePdfElement(paragraph);
        if (serializedParagraph != null && serializedParagraph.length() > 0) {
          paragraphs.put(serializedParagraph);
        }
      }

      for (PdfTextBlock block : paragraph.getTextBlocks()) {
        // Serialize the text block if text blocks should be included.
        if (isRelevantTextUnit(block)) {
          JSONObject serializedBlock = serializePdfElement(block);
          if (serializedBlock != null && serializedBlock.length() > 0) {
            blocks.put(serializedBlock);
          }
        }
        for (PdfTextLine line : block.getTextLines()) {
          // Serialize the text line if text lines should be included.
          if (isRelevantTextUnit(line)) {
            JSONObject serializedLine = serializePdfElement(line);
            if (serializedLine != null && serializedLine.length() > 0) {
              textLines.put(serializedLine);
            }
          }
          for (PdfWord word : line.getWords()) {
            // Serialize the text word if words should be included.
            if (isRelevantTextUnit(word)) {
              JSONObject serializedWord = serializePdfElement(word);
              if (serializedWord != null && serializedWord.length() > 0) {
                words.put(serializedWord);
              }
            }
            for (PdfCharacter character : word.getCharacters()) {
              // Serialize the characters if characters should be included.
              if (isRelevantTextUnit(character)) {
                JSONObject serializedChar = serializePdfElement(character);
                if (serializedChar != null && serializedChar.length() > 0) {
                  characters.put(serializedChar);
                }
              }
            }
          }
        }
      }
    }

    // Add the JSON arrays to the overall JSON object.
    if (paragraphs.length() > 0) {
      json.put(CONTEXT_NAME_PARAGRAPHS, paragraphs);
    }
    if (blocks.length() > 0) {
      json.put(CONTEXT_NAME_TEXT_BLOCKS, blocks);
    }
    if (textLines.length() > 0) {
      json.put(CONTEXT_NAME_TEXTLINES, textLines);
    }
    if (words.length() > 0) {
      json.put(CONTEXT_NAME_WORDS, words);
    }
    if (characters.length() > 0) {
      json.put(CONTEXT_NAME_CHARACTERS, characters);
    }
    if (figures.length() > 0) {
      json.put(CONTEXT_NAME_FIGURES, figures);
    }
    if (shapes.length() > 0) {
      json.put(CONTEXT_NAME_SHAPES, shapes);
    }

    return json;
  }

  /**
   * Serializes the given PDF element.
   * 
   * @param element
   *        The element to serialize.
   * @return A JSON object representing the serialized PDF element.
   */
  protected JSONObject serializePdfElement(PdfElement element) {
    if (element == null) {
      return null;
    }

    JSONObject json = new JSONObject();

    if (element instanceof HasText) {
      HasText hasText = (HasText) element;
      String text = hasText.getText();

      if (text != null) {
        // Append the text.
        json.put(CONTEXT_NAME_ELEMENT_TEXT, StringEscapeUtils.escapeJson(text));
      }
    }

    if (element instanceof HasFontFace) {
      PdfFontFace fontFace = ((HasFontFace) element).getFontFace();
      PdfFont font = fontFace.getFont();

      if (font != null) {
        String fontId = font.getId();
        // Append the id of the font.
        json.put(CONTEXT_NAME_ELEMENT_FONT, fontId);
        // Register the font as a utilized font.
        this.usedFonts.add(font);
      }
      // Append the font size.
      json.put(CONTEXT_NAME_ELEMENT_FONT_SIZE, fontFace.getFontSize());
    }

    if (element instanceof HasColor) {
      HasColor hasColor = (HasColor) element;
      PdfColor color = hasColor.getColor();

      if (color != null) {
        // Append the id of the color.
        json.put(CONTEXT_NAME_ELEMENT_COLOR, color.getId());
        // Register the color as a utilized color.
        this.usedColors.add(color);
      }
    }

    // TODO:
//    PdfPage page = element.getPosition().getPage();
//    if (page != null) {
//      // Append the page number.
//      json.put(CONTEXT_NAME_ELEMENT_PAGE, page.getPageNumber());
//    }

    Rectangle rect = element.getRectangle();
    if (rect != null) {
      // Append the coordinates of the bounding box.
      json.put(CONTEXT_NAME_ELEMENT_MIN_X, rect.getMinX());
      json.put(CONTEXT_NAME_ELEMENT_MIN_Y, rect.getMinY());
      json.put(CONTEXT_NAME_ELEMENT_MAX_X, rect.getMaxX());
      json.put(CONTEXT_NAME_ELEMENT_MAX_Y, rect.getMaxY());
    }

    return json;
  }

  // ==========================================================================
  // Methods to serialize fonts.

  /**
   * Serializes the utilized fonts on serializing the PDF document.
   *
   * @return A JSON array representing the serialized fonts of the PDF
   *         document.
   */
  protected JSONArray serializeFonts() {
    JSONArray json = new JSONArray();
    for (PdfFont font : this.usedFonts) {
      JSONObject serialized = serializeFont(font);
      if (serialized != null && serialized.length() > 0) {
        json.put(serialized);
      }
    }
    return json;
  }

  /**
   * Serializes the given font.
   *
   * @param font
   *        The font to serialize.
   * @return A JSON object representing the serialized font.
   */
  protected JSONObject serializeFont(PdfFont font) {
    if (font == null) {
      return null;
    }

    String id = font.getId();
    if (id == null) {
      return null;
    }

    JSONObject json = new JSONObject();
    // Append the id of the font.
    json.put(CONTEXT_NAME_FONT_ID, id);

    // Append the name of the font.
    String name = font.getNormalizedName();
    if (name != null) {
      json.put(CONTEXT_NAME_FONT_NAME, name);
    }

    // TODO: Add more attributes to fonts.
    // json.put(CONTEXT_NAME_FONT_IS_BOLD, font.isBold());
    // json.put(CONTEXT_NAME_FONT_IS_ITALIC, font.isItalic());
    // json.put(CONTEXT_NAME_FONT_IS_TYPE3, font.isType3Font());

    return json;
  }

  // ==========================================================================
  // Methods to serialize colors.

  /**
   * Serializes the utilized colors on serializing the PDF document.
   *
   * @return A JSON array representing the serialized colors of the PDF.
   */
  protected JSONArray serializeColors() {
    JSONArray json = new JSONArray();
    for (PdfColor color : this.usedColors) {
      JSONObject serialized = serializeColor(color);
      if (serialized != null && serialized.length() > 0) {
        json.put(serialized);
      }
    }
    return json;
  }

  /**
   * Serializes the given color.
   *
   * @param color
   *        The color to serialize.
   * @return A JSON object representing the serialized color.
   */
  protected JSONObject serializeColor(PdfColor color) {
    if (color == null) {
      return null;
    }

    String id = color.getId();
    if (id == null) {
      return null;
    }

    JSONObject json = new JSONObject();
    // Append the id of the color.
    json.put(CONTEXT_NAME_COLOR_ID, id);

    float[] rgb = color.getRGB();
    if (rgb != null && rgb.length == 3) {
      // Append the RGB values of the color.
      json.put(CONTEXT_NAME_COLOR_R, rgb[0]);
      json.put(CONTEXT_NAME_COLOR_G, rgb[1]);
      json.put(CONTEXT_NAME_COLOR_B, rgb[2]);
    }

    return json;
  }

  // ==========================================================================

  @Override
  public Set<PdfTextUnit> getTextUnits() {
    return this.units;
  }

  @Override
  public void setTextUnits(Set<PdfTextUnit> units) {
    this.units = units;
  }

  // ==========================================================================

  @Override
  public Set<PdfRole> getRoles() {
    return this.roles;
  }

  @Override
  public void setRoles(Set<PdfRole> roles) {
    this.roles = roles;
  }

  // ==========================================================================

  /**
   * Checks if the semantic role of the given text paragraph matches the
   * semantic roles filter of this serializer.
   * 
   * @param paragraph
   *        The text paragraph to check.
   * @return True, if the given paragraph matches the semantic roles filter of
   *         this serializer, false otherwise.
   */
  protected boolean hasRelevantRole(PdfParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }

    if (this.roles == null || this.roles.isEmpty()) {
      return true;
    }

    PdfRole role = paragraph.getRole();
    if (role == null) {
      return false;
    }

    return this.roles.contains(role);
  }

  /**
   * Checks if the text unit of the given PDF element matches the text unit
   * filter of this serializer.
   * 
   * @param element
   *        The PDF element to check.
   *
   * @return True, if the text unit of the given PDF element matches the text
   *         unit filter of this serializer, false otherwise.
   */
  protected boolean isRelevantTextUnit(PdfElement element) {
    if (element == null) {
      return false;
    }

    if (this.units == null || this.units.isEmpty()) {
      return true;
    }

    return this.units.contains(element.getTextUnit());
  }
}
