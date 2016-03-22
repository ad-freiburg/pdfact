package serializer;

import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLORS;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLOR_B;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLOR_G;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLOR_ID;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLOR_R;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_COLOR;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FONT;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FONT_SIZE;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_X;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_Y;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_X;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_Y;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_PAGE;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_ROLE;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_TEXT;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONTS;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_ID;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_IS_BOLD;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_IS_ITALIC;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_IS_TYPE3;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_NAME;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_PAGES;
import static serializer.PdfSerializerConstants.INDENT_LENGTH;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.freiburg.iif.model.Rectangle;
import model.PdfColor;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfFont;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextElement;

/**
 * Serializes a PdfDocument to json format.
 *
 * @author Claudius Korzen
 */
public class JsonPdfSerializer implements PdfSerializer {
  /** The features to serialize (all features per default). */
  protected List<PdfFeature> features = PdfFeature.valuesAsList();
  /** The roles to serialize (all roles per default). */
  protected Set<PdfRole> roles = PdfRole.valuesAsSet();

  /** The flag indicating whether to serialize punctuation marks. */
  protected boolean serializePunctuationMarks = true;
  /** The flag indicating whether to serialize subscripts. */
  protected boolean serializeSubscripts = true;
  /** The flag indicating whether to serialize superscripts. */
  protected boolean serializeSuperscripts = true;

  // ___________________________________________________________________________

  @Override
  public String getOutputFormat() {
    return "json";
  }

  @Override
  public void serialize(PdfDocument doc, OutputStream os) throws IOException {
    JSONObject json = new JSONObject();

    // Serialize the pages.
    JSONArray serializedPages = serializePages(doc);
    if (serializedPages != null && serializedPages.length() > 0) {
      json.put(CONTEXT_NAME_PAGES, serializedPages);
    }

    // Serialize the fonts.
    JSONArray serializedFonts = serializeFonts(doc);
    if (serializedFonts != null && serializedFonts.length() > 0) {
      json.put(CONTEXT_NAME_FONTS, serializedFonts);
    }

    // Serialize the colors.
    JSONArray serializedColors = serializeColors(doc);
    if (serializedColors != null && serializedColors.length() > 0) {
      json.put(CONTEXT_NAME_COLORS, serializedColors);
    }

    // Write the serialization to given stream.
    writeTo(json, os);
  }

  // ___________________________________________________________________________

  /**
   * Writes the given json object to the given output stream.
   */
  protected void writeTo(JSONObject json, OutputStream os) throws IOException {
    os.write(json.toString(INDENT_LENGTH).getBytes(StandardCharsets.UTF_8));
  }

  // ___________________________________________________________________________

  /**
   * Serializes the pages of the given document.
   */
  protected JSONArray serializePages(PdfDocument doc) {
    if (doc == null) {
      return null;
    }

    List<PdfPage> pages = doc.getPages();

    if (pages == null || pages.isEmpty()) {
      return null;
    }

    JSONArray json = new JSONArray();
    for (PdfPage page : pages) {
      JSONObject serialized = serializePage(page);
      if (serialized != null && serialized.length() > 0) {
        json.put(serialized);
      }
    }
    return json;
  }

  /**
   * Serializes the given page.
   */
  protected JSONObject serializePage(PdfPage page) {
    if (page == null) {
      return null;
    }

    JSONObject json = new JSONObject();

    // Obtain the features to serialize.
    List<PdfFeature> features = getFeaturesToSerialize();

    if (features != null) {
      for (PdfFeature f : features) {
        // Obtain the elements by feature.
        List<? extends PdfElement> elements = page.getElementsByFeature(f);

        JSONArray serialized = serializeElementsWithRespectToRoles(elements);
        if (serialized != null && serialized.length() > 0) {
          json.put(f.getFieldName(), serialized);
        }
      }
    }

    if (json.length() > 0) {
      json.put("id", page.getPageNumber());
      return json;
    }

    return null;
  }

  /**
   * Serializes the given elements with respect to the given roles.
   */
  protected JSONArray serializeElementsWithRespectToRoles(
      List<? extends PdfElement> elements) {
    if (elements == null) {
      return null;
    }

    JSONArray json = new JSONArray();
    for (PdfElement element : elements) {
      // Serialize the element only if its role is included in the given roles.
      if (getRolesToSerialize().contains(element.getRole())) {
        JSONObject serialized = serializeElement(element);
        if (serialized != null && serialized.length() > 0) {
          json.put(serialized);
        }
      }
    }
    return json;
  }

  /**
   * Serializes the given element.
   */
  protected JSONObject serializeElement(PdfElement element) {
    if (element == null) {
      return null;
    }

    // TODO: Get rid of ignore method. Is still needed for dehyphenation.
    if (element.ignore()) {
      return null;
    }

    if (element instanceof PdfTextElement) {
      return serializeTextElement((PdfTextElement) element);
    } else {
      return serializeNonTextElement(element);
    }
  }

  /**
   * Serializes the fonts of the given document.
   */
  protected JSONArray serializeFonts(PdfDocument document) {
    if (document == null) {
      return null;
    }

    JSONArray json = new JSONArray();
    for (PdfFont font : document.getFonts()) {
      JSONObject serialized = serializeFont(font);
      if (serialized != null && serialized.length() > 0) {
        json.put(serialized);
      }
    }
    return json;
  }

  /**
   * Serializes the colors of the given document.
   */
  protected JSONArray serializeColors(PdfDocument document) {
    if (document == null) {
      return null;
    }

    JSONArray json = new JSONArray();
    for (PdfColor color : document.getColors()) {
      JSONObject serialized = serializeColor(color);
      if (serialized != null && serialized.length() > 0) {
        json.put(serialized);
      }
    }
    return json;
  }

  /**
   * Serializes the given text element.
   */
  protected JSONObject serializeTextElement(PdfTextElement element) {
    if (element == null) {
      return null;
    }

    String text = element.getText(getSerializePunctuationMarks(),
        getSerializeSubscripts(), getSerializeSuperscripts());

    if (text == null) {
      return null;
    }

    JSONObject json = new JSONObject();
    json.put(CONTEXT_NAME_ELEMENT_TEXT, StringEscapeUtils.escapeJson(text));

    PdfPage page = element.getPage();
    if (page != null) {
      json.put(CONTEXT_NAME_ELEMENT_PAGE, page.getPageNumber());
    }

    Rectangle rect = element.getRectangle();
    if (rect != null) {
      json.put(CONTEXT_NAME_ELEMENT_MIN_X, rect.getMinX());
      json.put(CONTEXT_NAME_ELEMENT_MIN_Y, rect.getMinY());
      json.put(CONTEXT_NAME_ELEMENT_MAX_X, rect.getMaxX());
      json.put(CONTEXT_NAME_ELEMENT_MAX_Y, rect.getMaxY());
    }

    PdfFont font = element.getFont();
    if (font != null) {
      json.put(CONTEXT_NAME_ELEMENT_FONT, font.getId());
      json.put(CONTEXT_NAME_ELEMENT_FONT_SIZE, element.getFontsize());
    }

    PdfColor color = element.getColor();
    if (color != null) {
      json.put(CONTEXT_NAME_ELEMENT_COLOR, color.getId());
    }

    PdfRole role = element.getRole();
    if (role != null) {
      json.put(CONTEXT_NAME_ELEMENT_ROLE, role.name);
    }

    return json;
  }

  /**
   * Serializes the given (non-text) element.
   */
  protected JSONObject serializeNonTextElement(PdfElement element) {
    if (element == null) {
      return null;
    }

    JSONObject json = new JSONObject();

    Rectangle rect = element.getRectangle();
    if (rect != null) {
      json.put(CONTEXT_NAME_ELEMENT_MIN_X, rect.getMinX());
      json.put(CONTEXT_NAME_ELEMENT_MIN_Y, rect.getMinY());
      json.put(CONTEXT_NAME_ELEMENT_MAX_X, rect.getMaxX());
      json.put(CONTEXT_NAME_ELEMENT_MAX_Y, rect.getMaxY());
    }

    PdfColor color = element.getColor();
    if (color != null) {
      json.put(CONTEXT_NAME_ELEMENT_COLOR, color.getId());
    }

    PdfRole role = element.getRole();
    if (role != null) {
      json.put(CONTEXT_NAME_ELEMENT_ROLE, role.name);
    }

    return json;
  }

  /**
   * Serializes the given font.
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
    json.put(CONTEXT_NAME_FONT_ID, id);

    String basename = font.getBasename();
    if (basename != null) {
      json.put(CONTEXT_NAME_FONT_NAME, basename);
    }

    json.put(CONTEXT_NAME_FONT_IS_BOLD, font.isBold());
    json.put(CONTEXT_NAME_FONT_IS_ITALIC, font.isItalic());
    json.put(CONTEXT_NAME_FONT_IS_TYPE3, font.isType3Font());

    return json;
  }

  /**
   * Serializes the given color.
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
    json.put(CONTEXT_NAME_COLOR_ID, id);
    json.put(CONTEXT_NAME_COLOR_R, color.getR());
    json.put(CONTEXT_NAME_COLOR_G, color.getG());
    json.put(CONTEXT_NAME_COLOR_B, color.getB());

    return json;
  }

  @Override
  public void setFeatures(List<PdfFeature> features) {
    this.features = features;
  }

  /**
   * Returns the features to serialize.
   */
  public List<PdfFeature> getFeaturesToSerialize() {
    return this.features;
  }

  @Override
  public void setRoles(List<PdfRole> roles) {
    this.roles = new HashSet<>(roles);
  }

  /**
   * Returns the features to serialize.
   */
  public Set<PdfRole> getRolesToSerialize() {
    return this.roles;
  }

  @Override
  public void setSerializePunctuationMarks(boolean serialize) {
    this.serializePunctuationMarks = serialize;
  }

  /**
   * Returns true if punctuation marks should be serialized.
   */
  public boolean getSerializePunctuationMarks() {
    return this.serializePunctuationMarks;
  }

  @Override
  public void setSerializeSubscripts(boolean serialize) {
    this.serializeSubscripts = serialize;
  }

  /**
   * Returns true if subscripts should be serialized.
   */
  public boolean getSerializeSubscripts() {
    return this.serializeSubscripts;
  }

  @Override
  public void setSerializeSuperscripts(boolean serialize) {
    this.serializeSuperscripts = serialize;
  }

  /**
   * Returns true if superscripts should be serialized.
   */
  public boolean getSerializeSuperscripts() {
    return this.serializeSuperscripts;
  }
}
