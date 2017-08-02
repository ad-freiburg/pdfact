package icecite.serialize;

import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_CHARACTERS;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_COLORS;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_B;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_G;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_ID;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_R;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_COLOR;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FONT;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FONT_SIZE;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_X;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_Y;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_X;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_Y;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_PAGE;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_TEXT;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_FIGURES;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_FONTS;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_FONT_ID;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_FONT_NAME;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_PAGES;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_PARAGRAPHS;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_SHAPES;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_TEXTLINES;
import static icecite.serialize.PdfSerializerConstants.CONTEXT_NAME_WORDS;
import static icecite.serialize.PdfSerializerConstants.INDENT_LENGTH;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.HasColor;
import icecite.models.HasFontFace;
import icecite.models.HasText;
import icecite.models.PdfCharacter;
import icecite.models.PdfColor;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfFeature;
import icecite.models.PdfFigure;
import icecite.models.PdfFont;
import icecite.models.PdfFontFace;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.utils.collection.CollectionUtils;
import icecite.utils.geometric.Rectangle;

// TODO: Fix the serializers.
// TODO: Serialize the paragraphs.

/**
 * An implementation of PdfSerializer that serializes a PDF document to JSON
 * format.
 *
 * @author Claudius Korzen
 */
public class JsonPdfSerializer implements PdfSerializer {
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

  // ==========================================================================

  @Override
  public void serialize(PdfDocument pdf, OutputStream os) throws IOException {
    serialize(pdf, os, null, null);
  }

  @Override
  public void serialize(PdfDocument pdf, OutputStream os,
      Set<PdfFeature> features, Set<PdfRole> roles) throws IOException {
    if (pdf == null) {
      return;
    }
    if (os == null) {
      return;
    }

    // The used fonts on serializing the PDF document.
    this.usedFonts = new HashSet<>();
    // The used colors on serializing the PDF document.
    this.usedColors = new HashSet<>();

    // The JSON object to fill.
    JSONObject json = new JSONObject();

    // Serialize the pages.
    JSONArray serializedPages = serializePages(pdf, features, roles);
    if (serializedPages != null && serializedPages.length() > 0) {
      json.put(CONTEXT_NAME_PAGES, serializedPages);
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
    writeTo(json, os);
  }

  // ==========================================================================

  /**
   * Serializes the pages of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param features
   *        The featurs to serialize.
   * @param roles
   *        The roles to consider on serialization.
   * @return An JSON array representing the serialized pages of the PDF
   *         document.
   */
  protected JSONArray serializePages(PdfDocument pdf, Set<PdfFeature> features,
      Set<PdfRole> roles) {
    if (pdf == null) {
      return null;
    }

    List<PdfPage> pages = pdf.getPages();
    if (pages == null || pages.isEmpty()) {
      return null;
    }

    // Serialize each single page.
    JSONArray json = new JSONArray();
    for (PdfPage page : pages) {
      JSONObject serialized = serializePage(page, features, roles);
      if (serialized != null && serialized.length() > 0) {
        json.put(serialized);
      }
    }
    return json;
  }

  /**
   * Serializes the given page.
   * 
   * @param page
   *        The page to serialize.
   * @param features
   *        The features to serialize.
   * @param roles
   *        The roles to consider on serialization.
   * @return A JSON object representing the serialized page.
   */
  protected JSONObject serializePage(PdfPage page, Set<PdfFeature> features,
      Set<PdfRole> roles) {
    if (page == null) {
      return null;
    }

    // Define a JSON object for the whole pages.
    JSONObject json = new JSONObject();

    // Define JSON arrays for each PDF element type.
    JSONArray paragraphs = new JSONArray();
    JSONArray textLines = new JSONArray();
    JSONArray words = new JSONArray();
    JSONArray characters = new JSONArray();
    JSONArray figures = new JSONArray();
    JSONArray shapes = new JSONArray();

    // Serialize the text elements.
    for (PdfTextBlock block : page.getTextBlocks()) {
      if (isSerializePdfElement(block, features, roles)) {
        JSONObject serializedParagraph = serializePdfElement(page, block);
        if (serializedParagraph != null && serializedParagraph.length() > 0) {
          paragraphs.put(serializedParagraph);
        }
      }
      for (PdfTextLine line : block.getTextLines()) {
        if (isSerializePdfElement(line, features, roles)) {
          JSONObject serializedTextLine = serializePdfElement(page, line);
          if (serializedTextLine != null && serializedTextLine.length() > 0) {
            textLines.put(serializedTextLine);
          }
        }
        for (PdfWord word : line.getWords()) {
          if (isSerializePdfElement(word, features, roles)) {
            JSONObject serializedTextWord = serializePdfElement(page, word);
            if (serializedTextWord != null && serializedTextWord.length() > 0) {
              words.put(serializedTextWord);
            }
          }
          for (PdfCharacter character : word.getCharacters()) {
            if (isSerializePdfElement(character, features, roles)) {
              JSONObject serializedChar = serializePdfElement(page, character);
              if (serializedChar != null && serializedChar.length() > 0) {
                characters.put(serializedChar);
              }
            }
          }
        }
      }
    }

    // Serialize the graphical elements.
    for (PdfFigure figure : page.getFigures()) {
      if (isSerializePdfElement(figure, features, roles)) {
        JSONObject serializedFigure = serializePdfElement(page, figure);
        if (serializedFigure != null && serializedFigure.length() > 0) {
          figures.put(serializedFigure);
        }
      }
    }
    for (PdfShape shape : page.getShapes()) {
      if (isSerializePdfElement(shape, features, roles)) {
        JSONObject serializedShapes = serializePdfElement(page, shape);
        if (serializedShapes != null && serializedShapes.length() > 0) {
          shapes.put(serializedShapes);
        }
      }
    }

    // Add the JSON arrays to the overall JSON object.
    if (paragraphs.length() > 0) {
      json.put(CONTEXT_NAME_PARAGRAPHS, paragraphs);
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

    // Add the page number.
    if (json.length() > 0) {
      json.put("id", page.getPageNumber());
      return json;
    }

    return null;
  }

  /**
   * Serializes the given PDF element.
   * 
   * @param page
   *        The page in which the element is located.
   * @param element
   *        The element to serialize.
   * @return A JSON object representing the serialized PDF element.
   */
  protected JSONObject serializePdfElement(PdfPage page, PdfElement element) {
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

    if (page != null) {
      // Append the page number.
      json.put(CONTEXT_NAME_ELEMENT_PAGE, page.getPageNumber());
    }

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

  /**
   * Writes the given JSON object to the given output stream.
   * 
   * @param json
   *        The JSON object to write.
   * @param os
   *        The stream to write to.
   * @throws IOException
   *         If writing to the stream failed.
   */
  protected void writeTo(JSONObject json, OutputStream os) throws IOException {
    os.write(json.toString(INDENT_LENGTH).getBytes(StandardCharsets.UTF_8));
  }

  // ==========================================================================

  /**
   * Checks if the given PDF element should be serialized or not, dependent on
   * the type and the role of the element.
   * 
   * @param element
   *        The PDF element to check.
   * @param features
   *        The features to serialize.
   * @param roles
   *        The roles to consider on serialization.
   * @return True, if the given PDF element should be serialized, false
   *         otherwise.
   */
  protected boolean isSerializePdfElement(PdfElement element,
      Set<PdfFeature> features, Set<PdfRole> roles) {
    // Check if the type of the given element was registered to be serialized.
    boolean isFeatureGiven = !CollectionUtils.isNullOrEmpty(features);
    if (isFeatureGiven && !features.contains(element.getFeature())) {
      return false;
    }

    // Check if the role of the given element was registered to be serialized.
    // TODO
    // boolean isRoleGiven = !CollectionUtils.isNullOrEmpty(roles);
    // if (isRoleGiven && !roles.contains(element.getRole())) {
    // return false;
    // }
    return true;
  }

  // ==========================================================================

  /**
   * Returns the output format of this serializer.
   * 
   * @return The output format of this serializer.
   */
  public static String getOutputFormat() {
    return "json";
  }
}
