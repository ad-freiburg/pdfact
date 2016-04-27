package serializer;

import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLORS;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLOR_B;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLOR_G;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_COLOR_R;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_DOCUMENT;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MOST_COMMON_COLOR;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_COLOR;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_LAST_CHARACTER_COLOR;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT_SIZE;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_X;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_Y;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_X;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_Y;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_PAGE;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_ROLE;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_FONT;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_FONTSIZE;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONTS;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_IS_BOLD;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_IS_ITALIC;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_IS_TYPE3;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_FONT_NAME;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_LAST_CHARACTER_FONT;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_LAST_CHARACTER_FONTSIZE;
import static serializer.PdfSerializerConstants.CONTEXT_NAME_PAGES;
import static serializer.PdfSerializerConstants.INDENT_LENGTH;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import de.freiburg.iif.collection.CollectionUtils;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.text.StringUtils;
import model.Comparators;
import model.PdfCharacter;
import model.PdfColor;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfFont;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextElement;

/**
 * Serializes a PdfDocument to tsv format.
 *
 * @author Claudius Korzen
 */
public class XmlPdfSerializer implements PdfSerializer {
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
    return "xml";
  }

  @Override
  public void serialize(PdfDocument doc, OutputStream os) throws IOException {
    List<String> lines = new ArrayList<>();
    int level = 0;

    lines.add(indent("<" + CONTEXT_NAME_DOCUMENT + ">", level));

    // Serialize the pages.
    lines.add(indent("<" + CONTEXT_NAME_PAGES + ">", ++level));
    List<String> serializedPages = serializePages(doc, ++level);
    if (serializedPages != null && !serializedPages.isEmpty()) {
      lines.addAll(serializedPages);
    }
    lines.add(indent("</" + CONTEXT_NAME_PAGES + ">", --level));

    // Serialize the fonts
    lines.add(indent("<" + CONTEXT_NAME_FONTS + ">", level++));
    List<String> serializedFonts = serializeFonts(doc, level);
    if (serializedFonts != null && !serializedFonts.isEmpty()) {
      lines.addAll(serializedFonts);
    }
    lines.add(indent("</" + CONTEXT_NAME_FONTS + ">", --level));

    // Serialize the colors.
    lines.add(indent("<" + CONTEXT_NAME_COLORS + ">", level++));
    List<String> serializedColors = serializeColors(doc, level);
    if (serializedColors != null && !serializedColors.isEmpty()) {
      lines.addAll(serializedColors);
    }
    lines.add(indent("</" + CONTEXT_NAME_COLORS + ">", --level));

    lines.add(indent("</" + CONTEXT_NAME_DOCUMENT + ">", --level));

    writeTo(lines, os);
  }

  // ___________________________________________________________________________

  /**
   * Writes the given json object to the given output stream.
   */
  protected void writeTo(List<String> lines, OutputStream os)
    throws IOException {
    String serialized = CollectionUtils.join(lines, System.lineSeparator());
    os.write(serialized.getBytes(StandardCharsets.UTF_8));
  }

  // ___________________________________________________________________________

  /**
   * Serializes the pages of the given document.
   */
  protected List<String> serializePages(PdfDocument doc, int level) {
    if (doc == null) {
      return null;
    }

    List<PdfPage> pages = doc.getPages();

    if (pages == null || pages.isEmpty()) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfPage page : pages) {
      int pageNum = page.getPageNumber();
      lines.add(indent("<" + CONTEXT_NAME_ELEMENT_PAGE
          + " id=\"" + pageNum + "\">", level++));
      lines.addAll(serializePage(page, level));
      lines.add(indent("</" + CONTEXT_NAME_ELEMENT_PAGE + ">", --level));
    }
    return lines;
  }

  /**
   * Serializes the given page.
   */
  protected List<String> serializePage(PdfPage page, int level) {
    if (page == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();

    // Obtain the features to serialize.
    List<PdfFeature> features = getFeaturesToSerialize();

    if (features != null) {
      for (PdfFeature f : features) {
        // Obtain the elements by feature.
        List<? extends PdfElement> elements = page.getElementsByFeature(f);

        List<String> serialized =
            serializeElementsWithRespectToRoles(elements, level);
        if (serialized != null && !serialized.isEmpty()) {
          lines.addAll(serialized);
        }
      }
    }

    return lines;
  }

  /**
   * Serializes the given elements with respect to the given roles.
   */
  protected List<String> serializeElementsWithRespectToRoles(
      List<? extends PdfElement> elements, int level) {
    if (elements == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfElement element : elements) {
      // Serialize the element only if its role is included in the given roles.
      if (getRolesToSerialize().contains(element.getRole())) {
        String serialized = serializeElement(element, level);
        if (serialized != null && !serialized.isEmpty()) {
          lines.add(serialized);
        }
      }
    }
    return lines;
  }

  /**
   * Serializes the given element.
   */
  protected String serializeElement(PdfElement element, int level) {
    if (element == null) {
      return null;
    }

    // TODO: Get rid of ignore method. Is still needed for dehyphenation.
    if (element.ignore()) {
      return null;
    }

    if (element instanceof PdfTextElement) {
      return serializeTextElement((PdfTextElement) element, level);
    } else {
      return serializeNonTextElement(element, level);
    }
  }

  /**
   * Serializes the fonts of the given document.
   */
  protected List<String> serializeFonts(PdfDocument document, int level) {
    if (document == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfFont font : document.getFonts()) {
      String serialized = serializeFont(font, level);
      if (serialized != null && !serialized.isEmpty()) {
        lines.add(serialized);
      }
    }
    return lines;
  }

  /**
   * Serializes the colors of the given document.
   */
  protected List<String> serializeColors(PdfDocument document, int level) {
    if (document == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfColor color : document.getColors()) {
      String serialized = serializeColor(color, level);
      if (serialized != null && !serialized.isEmpty()) {
        lines.add(serialized);
      }
    }
    return lines;
  }

  /**
   * Serializes the given text element.
   */
  protected String serializeTextElement(PdfTextElement element, int level) {
    if (element == null) {
      return null;
    }

    String text = element.getText(getSerializePunctuationMarks(),
        getSerializeSubscripts(), getSerializeSuperscripts());

    if (text == null || text.trim().isEmpty()) {
      return null;
    }

    StringBuilder xml = new StringBuilder();

    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);

    xml.append(indent);
    xml.append("<");

    // Append the feature name.
    PdfFeature feature = element.getFeature();
    xml.append(feature != null ? feature.getFieldName() : "");

    // Append the page.
    PdfPage page = element.getPage();
    if (page != null) {
      int pageNum = page.getPageNumber();
      xml.append(" " + CONTEXT_NAME_ELEMENT_PAGE + "=\"" + pageNum + "\"");
    }

    // Append the bounding box.
    Rectangle r = element.getRectangle();
    if (r != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_MIN_X + "=\"" + r.getMinX() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MIN_Y + "=\"" + r.getMinY() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MAX_X + "=\"" + r.getMaxX() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MAX_Y + "=\"" + r.getMaxY() + "\"");
    }

    // Append the font.
    PdfFont font = element.getFont();
    if (font != null) {
      String fontId = font.getId();
      float fs = element.getFontsize();
      xml.append(
          " " + CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT + "=\"" + fontId + "\"");
      xml.append(
          " " + CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT_SIZE + "=\"" + fs + "\"");
    }

    // Add font of first and last character.
    PdfFont firstCharacterFont = null;
    PdfFont lastCharacterFont = null;
    PdfColor firstCharacterColor = null;
    PdfColor lastCharacterColor = null;
    float firstCharacterFontsize = 0;
    float lastCharacterFontsize = 0;
    List<PdfCharacter> characters = element.getTextCharacters();
    if (characters != null && !characters.isEmpty()) {
      Collections.sort(characters, new Comparators.MinXComparator());

      PdfCharacter firstCharacter = characters.get(0);
      PdfCharacter lastCharacter = characters.get(characters.size() - 1);

      if (firstCharacter != null) {
        firstCharacterFont = firstCharacter.getFont();
        firstCharacterFontsize = firstCharacter.getFontsize();
        firstCharacterColor = firstCharacter.getColor();
      }
      if (lastCharacter != null) {
        lastCharacterFont = lastCharacter.getFont();
        lastCharacterFontsize = lastCharacter.getFontsize();
        lastCharacterColor = lastCharacter.getColor();
      }
    }
    if (firstCharacterFont != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_FONT + "=\""
          + firstCharacterFont.getId() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_FONTSIZE + "=\""
          + firstCharacterFontsize + "\"");
    }
    if (lastCharacterFont != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_LAST_CHARACTER_FONT + "=\""
          + lastCharacterFont.getId() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_LAST_CHARACTER_FONTSIZE + "=\""
          + lastCharacterFontsize + "\"");
    }

    // Append the most common color.
    PdfColor color = element.getColor();
    if (color != null) {
      String colorId = color.getId();
      xml.append(" " + CONTEXT_NAME_ELEMENT_MOST_COMMON_COLOR + "=\"" + colorId
          + "\"");
    }
    // Append the color of first character.
    if (firstCharacterColor != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_FIRST_CHARACTER_COLOR + "=\""
          + firstCharacterColor.getId() + "\"");
    }
    // Append the color of last character.
    if (lastCharacterColor != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_LAST_CHARACTER_COLOR + "=\""
          + lastCharacterColor.getId() + "\"");
    }

    // Append the role.
    PdfRole role = element.getRole();
    if (role != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_ROLE + "=\"" + role.name + "\"");
    }
    xml.append(">");

    // Append the text.
    xml.append(StringEscapeUtils.escapeXml11(text));

    xml.append("</" + (feature != null ? feature.getFieldName() : "") + ">");

    return xml.toString();
  }

  /**
   * Serializes the given (non-text) element.
   */
  protected String serializeNonTextElement(PdfElement element, int level) {
    if (element == null) {
      return null;
    }

    StringBuilder xml = new StringBuilder();

    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);

    xml.append(indent);
    xml.append("<");

    // Append the feature name.
    PdfFeature feature = element.getFeature();
    xml.append(feature != null ? feature.getFieldName() : "");

    // Append the page.
    PdfPage page = element.getPage();
    if (page != null) {
      int pageNum = page.getPageNumber();
      xml.append(" " + CONTEXT_NAME_ELEMENT_PAGE + "=\"" + pageNum + "\"");
    }

    // Append the bounding box.
    Rectangle r = element.getRectangle();
    if (r != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_MIN_X + "=\"" + r.getMinX() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MIN_Y + "=\"" + r.getMinY() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MAX_X + "=\"" + r.getMaxX() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MAX_Y + "=\"" + r.getMaxY() + "\"");
    }

    // Append the color.
    PdfColor color = element.getColor();
    if (color != null) {
      String colorId = color.getId();
      xml.append(" " + CONTEXT_NAME_ELEMENT_MOST_COMMON_COLOR + "=\"" + colorId
          + "\"");
    }

    // Append the role.
    PdfRole role = element.getRole();
    if (role != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_ROLE + "=\"" + role.name + "\"");
    }
    xml.append("/>");

    return xml.toString();
  }

  /**
   * Serializes the given font.
   */
  protected String serializeFont(PdfFont font, int level) {
    if (font == null) {
      return null;
    }

    String id = font.getId();
    if (id == null) {
      return null;
    }

    StringBuilder xml = new StringBuilder();

    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);

    xml.append(indent);
    xml.append("<font id=\"" + id + "\"");

    String basename = font.getBasename();
    xml.append(" " + CONTEXT_NAME_FONT_NAME + "=\"" + basename + "\"");
    boolean isBold = font.isBold();
    xml.append(" " + CONTEXT_NAME_FONT_IS_BOLD + "=\"" + isBold + "\"");
    boolean isItalic = font.isItalic();
    xml.append(" " + CONTEXT_NAME_FONT_IS_ITALIC + "=\"" + isItalic + "\"");
    boolean isType3 = font.isType3Font();
    xml.append(" " + CONTEXT_NAME_FONT_IS_TYPE3 + "=\"" + isType3 + "\"");
    xml.append(" />");

    return xml.toString();
  }

  /**
   * Serializes the given color.
   */
  protected String serializeColor(PdfColor color, int level) {
    if (color == null) {
      return null;
    }

    String id = color.getId();

    if (id == null) {
      return null;
    }

    StringBuilder xml = new StringBuilder();

    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);

    xml.append(indent);
    xml.append("<color id=\"" + id + "\"");

    xml.append(" " + CONTEXT_NAME_COLOR_R + "=\"" + color.getR() + "\"");
    xml.append(" " + CONTEXT_NAME_COLOR_G + "=\"" + color.getG() + "\"");
    xml.append(" " + CONTEXT_NAME_COLOR_B + "=\"" + color.getB() + "\"");
    xml.append(" />");

    return xml.toString();
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

  /**
   * Indents the given string.
   */
  protected String indent(String string, int level) {
    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);
    return indent + string;
  }
}
