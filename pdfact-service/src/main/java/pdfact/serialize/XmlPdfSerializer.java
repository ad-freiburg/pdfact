package pdfact.serialize;

import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_CHARACTER;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLORS;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_B;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_G;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_ID;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_COLOR_R;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_DOCUMENT;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_FONT_SIZE;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_X;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_Y;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_X;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_Y;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_FONT;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_FONTS;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_FONT_ID;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_FONT_NAME;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_PARAGRAPH;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_TEXTLINE;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_TEXT_BLOCK;
import static pdfact.serialize.PdfSerializerConstants.CONTEXT_NAME_WORD;
import static pdfact.serialize.PdfSerializerConstants.INDENT_LENGTH;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

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
import pdfact.models.PdfPage;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfTextUnit;
import pdfact.models.PdfWord;
import pdfact.utils.collection.CollectionUtils;
import pdfact.utils.geometric.Rectangle;
import pdfact.utils.text.StringUtils;

/**
 * An implementation of PdfSerializer that serializes a PDF document to XML
 * format.
 *
 * @author Claudius Korzen
 */
public class XmlPdfSerializer implements PdfSerializer {
  /**
   * The text units to include on serialization.
   */
  protected Set<PdfTextUnit> units;

  /**
   * The roles of text units to include on serialization.
   */
  protected Set<PdfRole> roles;

  /**
   * The fonts which were utilized on serializing a PDF document.
   */
  protected Set<PdfFont> usedFonts;

  /**
   * The colors which were utilized on serializing a PDF document.
   */
  protected Set<PdfColor> usedColors;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new serializer that serializes PDF documents to XML format.
   */
  @AssistedInject
  public XmlPdfSerializer() {

  }

  /**
   * Creates a new serializer that serializes PDF documents to XML format.
   * 
   * @param units
   *        The text units to include on serialization.
   * @param roles
   *        The roles of text units to include on serialization.
   */
  @AssistedInject
  public XmlPdfSerializer(Set<PdfTextUnit> units, Set<PdfRole> roles) {
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
    return PdfActSerializationFormat.XML;
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
    // The lines representing the serialized PDF document.
    List<String> lines = new ArrayList<>();

    // Start an XML tag to introduce the document: <document>
    lines.add(indent("<" + CONTEXT_NAME_DOCUMENT + ">", 0));
    // Serialize each single page.
    List<String> serializedPages = serializePdfDocument(pdf, 1);
    if (serializedPages != null && !serializedPages.isEmpty()) {
      lines.addAll(serializedPages);
    }

    // Serialize the fonts: Serialize *not* all fonts, but only the fonts of
    // the serialized elements.
    if (!this.usedFonts.isEmpty()) {
      // Start an XML tag to introduce the fonts: <fonts>
      lines.add(indent("<" + CONTEXT_NAME_FONTS + ">", 1));
      // Serialize each single font.
      List<String> serializedFonts = serializeFonts(2);
      if (serializedFonts != null && !serializedFonts.isEmpty()) {
        lines.addAll(serializedFonts);
      }
      // End the XML tag for the fonts: </fonts>
      lines.add(indent("</" + CONTEXT_NAME_FONTS + ">", 1));
    }

    // Serialize the colors: Serialize *not* all fonts, but only the fonts of
    // the serialized elements.
    if (!this.usedColors.isEmpty()) {
      // Start an XML tag to introduce the colors: <colors>
      lines.add(indent("<" + CONTEXT_NAME_COLORS + ">", 1));
      // Serialize each single color.
      List<String> serializedColors = serializeColors(2);
      if (serializedColors != null && !serializedColors.isEmpty()) {
        lines.addAll(serializedColors);
      }
      // End the XML tag for the fonts: </colors>
      lines.add(indent("</" + CONTEXT_NAME_COLORS + ">", 1));
    }

    // End the XML tag for the document </document>
    lines.add(indent("</" + CONTEXT_NAME_DOCUMENT + ">", 0));

    // Write the lines to the stream.
    String text = CollectionUtils.join(lines, System.lineSeparator());
    return text.getBytes(StandardCharsets.UTF_8);
  }

  // ==========================================================================

  /**
   * Serializes the pages of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * @param level
   *        The current indentation level.
   * @return The text lines that represent the serialized pages of the PDF
   *         document.
   */
  protected List<String> serializePdfDocument(PdfDocument pdf, int level) {
    if (pdf == null) {
      return null;
    }

    List<PdfPage> pages = pdf.getPages();
    if (pages == null || pages.isEmpty()) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfParagraph paragraph : pdf.getParagraphs()) {
      // Ignore the paragraph if its role doesn't match the roles filter.
      if (!hasRelevantRole(paragraph)) {
        continue;
      }

      // Serialize the paragraph if paragraphs should be included.
      if (isRelevantTextUnit(paragraph)) {
        lines.add(serializeElement(paragraph, CONTEXT_NAME_PARAGRAPH, level));
      }

      for (PdfTextBlock block : paragraph.getTextBlocks()) {
        if (isRelevantTextUnit(block)) {
          lines.add(serializeElement(block, CONTEXT_NAME_TEXT_BLOCK, level));
        }
        for (PdfTextLine line : block.getTextLines()) {
          if (isRelevantTextUnit(line)) {
            lines.add(serializeElement(line, CONTEXT_NAME_TEXTLINE, level));
          }
          for (PdfWord word : line.getWords()) {
            if (isRelevantTextUnit(word)) {
              lines.add(serializeElement(word, CONTEXT_NAME_WORD, level));
            }
            for (PdfCharacter c : word.getCharacters()) {
              if (isRelevantTextUnit(c)) {
                lines.add(serializeElement(c, CONTEXT_NAME_CHARACTER, level));
              }
            }
          }
        }
      }
    }
    return lines;
  }

  /**
   * Serializes the given PDF element.
   * 
   * @param element
   *        The PDF element to serialize.
   * @param tagName
   *        The name for the tag to wrap around the serialization.
   * @param level
   *        The current indentation level.
   * @return The serialization string representing the given PDF element.
   */
  protected String serializeElement(PdfElement element, String tagName,
      int level) {
    if (element == null) {
      return null;
    }

    StringBuilder xml = new StringBuilder();

    String text = null;
    if (element instanceof HasText) {
      HasText hasText = (HasText) element;
      text = hasText.getText();
    }

    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);

    // Introduce the starting tag, for example: <character
    xml.append(indent);
    xml.append("<");
    xml.append(tagName);

    // Append the page number to the tag: <character page="1"
    // TODO
//    PdfPage page = element.getPosition().getPage();
//    if (page != null) {
//      int pageNum = page.getPageNumber();
//      xml.append(" " + CONTEXT_NAME_ELEMENT_PAGE + "=\"" + pageNum + "\"");
//    }

    Rectangle r = element.getRectangle();
    if (r != null) {
      // Append the coordinates of the bounding box:
      // <character ... minX="1" minY="2" maxX="3" maxY="4"
      xml.append(" " + CONTEXT_NAME_ELEMENT_MIN_X + "=\"" + r.getMinX() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MIN_Y + "=\"" + r.getMinY() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MAX_X + "=\"" + r.getMaxX() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MAX_Y + "=\"" + r.getMaxY() + "\"");
    }

    if (element instanceof HasFontFace) {
      PdfFontFace fontFace = ((HasFontFace) element).getFontFace();
      PdfFont font = fontFace.getFont();
      float fs = fontFace.getFontSize();

      if (font != null) {
        String fontId = font.getId();
        // Append the id of the font: <character ... font="foo"
        xml.append(" " + CONTEXT_NAME_FONT_ID + "=\"" + fontId + "\"");
        // Register the font as a used font.
        this.usedFonts.add(font);
      }
      // Append the font size: <character ... font-size="12"
      xml.append(" " + CONTEXT_NAME_ELEMENT_FONT_SIZE + "=\"" + fs + "\"");
    }

    if (element instanceof HasColor) {
      HasColor hasColor = (HasColor) element;
      PdfColor color = hasColor.getColor();

      if (color != null) {
        String colorId = color.getId();
        // Append the id of the color: <character ... color="foo"
        xml.append(" " + CONTEXT_NAME_COLOR_ID + "=\"" + colorId + "\"");
        // Register the color as a utilized color.
        this.usedColors.add(color);
      }
    }

    // TODO
    // PdfRole role = element.getRole();
    // if (role != null) {
    // String roleName = role.getIdentifier();
    // // Append the role: <character ... role="foo"
    // xml.append(" " + CONTEXT_NAME_ELEMENT_ROLE + "=\"" + roleName + "\"");
    // }

    // Check if there is text to display.
    if (text == null || text.isEmpty()) {
      // Close the tag: <shape page=1 ... role="foo"/>
      xml.append("/>");
    } else {
      // Add the text as inner content: <character page="1" ...>x</character>
      xml.append(">");
      xml.append(StringEscapeUtils.escapeXml11(text));
      xml.append("</" + tagName + ">");
    }

    return xml.toString();
  }

  // ==========================================================================

  /**
   * Serializes the used fonts.
   * 
   * @param level
   *        The current indentation level.
   * @return The serialization strings representing the used fonts.
   */
  protected List<String> serializeFonts(int level) {
    List<String> lines = new ArrayList<>();
    for (PdfFont font : this.usedFonts) {
      String serialized = serializeFont(font, level);
      if (serialized != null && !serialized.isEmpty()) {
        lines.add(serialized);
      }
    }
    return lines;
  }

  /**
   * Serializes the given font.
   * 
   * @param font
   *        The font to serialize.
   * @param level
   *        The current indentation level.
   * @return The serialization string representing the given color.
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

    // Introduce the starting tag, for example: <font
    xml.append(indent);
    xml.append("<");
    xml.append(CONTEXT_NAME_FONT);

    // Append the id of the font: <font id="foo"
    xml.append(" " + CONTEXT_NAME_FONT_ID + "=\"" + font.getId() + "\"");

    // Append the name of the font: <font id="foo" name="bar"
    String name = font.getNormalizedName();
    if (name != null) {
      xml.append(" " + CONTEXT_NAME_FONT_NAME + "=\"" + name + "\"");
    }

    // TODO: Add some more attributes to the fonts.
    // boolean isBold = font.isBold();
    // xml.append(" " + CONTEXT_NAME_FONT_IS_BOLD + "=\"" + isBold + "\"");
    // boolean isItalic = font.isItalic();
    // xml.append(" " + CONTEXT_NAME_FONT_IS_ITALIC + "=\"" + isItalic + "\"");
    // boolean isType3 = font.isType3Font();
    // xml.append(" " + CONTEXT_NAME_FONT_IS_TYPE3 + "=\"" + isType3 + "\"");

    // Close the tag: <font id="foo" name="bar" ... />
    xml.append("/>");

    return xml.toString();
  }

  // ==========================================================================
  // Methods to serialize colors.

  /**
   * Serializes the used colors.
   * 
   * @param level
   *        The current indentation level.
   * @return The serialization strings representing the used colors.
   */
  protected List<String> serializeColors(int level) {
    List<String> lines = new ArrayList<>();
    for (PdfColor color : this.usedColors) {
      String serialized = serializeColor(color, level);
      if (serialized != null && !serialized.isEmpty()) {
        lines.add(serialized);
      }
    }
    return lines;
  }

  /**
   * Serializes the given color.
   * 
   * @param color
   *        The color to serialize.
   * @param level
   *        The current indentation level.
   * @return The serialization string representing the given color.
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

    // Introduce the starting tag: <color
    xml.append(indent);
    xml.append("<");
    xml.append(CONTEXT_NAME_COLOR);

    // Append the id of the color: <color id="foo"
    xml.append(" " + CONTEXT_NAME_COLOR_ID + "=\"" + color.getId() + "\"");

    float[] rgb = color.getRGB();
    if (rgb != null && rgb.length == 3) {
      // Append the RGB values: <color id="foo" r="0" g="1" b="1"
      xml.append(" " + CONTEXT_NAME_COLOR_R + "=\"" + rgb[0] + "\"");
      xml.append(" " + CONTEXT_NAME_COLOR_G + "=\"" + rgb[1] + "\"");
      xml.append(" " + CONTEXT_NAME_COLOR_B + "=\"" + rgb[2] + "\"");
    }

    // Close the tag: <color id="foo" r="0" g="1" b="1">
    xml.append("/>");

    return xml.toString();
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
   * Indents the given string.
   * 
   * @param string
   *        The string to indent.
   * @param level
   *        The indentation level.
   * @return The indented string.
   */
  protected String indent(String string, int level) {
    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);
    return indent + string;
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
