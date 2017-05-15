package icecite.serializer;

import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_DOCUMENT;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_X;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_Y;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_X;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_Y;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_PAGE;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_PAGES;
import static icecite.serializer.PdfSerializerConstants.INDENT_LENGTH;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import icecite.models.HasText;
import icecite.models.PdfCharacter;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfShape;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.utils.collection.CollectionUtils;
import icecite.utils.geometric.Rectangle;
import icecite.utils.text.StringUtils;

// TODO: Implement the XML serializer.

/**
 * An implementation of PdfSerializer that serializes a PDF document to JSON
 * format.
 *
 * @author Claudius Korzen
 */
public class XmlPdfSerializer implements PdfSerializer {
  @Override
  public void serialize(PdfDocument pdf, OutputStream os) throws IOException {
    Set<Class<? extends PdfElement>> types = new HashSet<>();

    // types.add(PdfParagraph.class);
    types.add(PdfTextLine.class);
    // types.add(PdfWord.class);
    // types.add(PdfCharacter.class);
    // types.add(PdfFigure.class);
    // types.add(PdfShape.class);

    serialize(pdf, types, os);
  }

  @Override
  public void serialize(PdfDocument pdf, Set<Class<? extends PdfElement>> types,
      OutputStream os) throws IOException {
    List<String> lines = new ArrayList<>();
    int level = 0;

    lines.add(indent("<" + CONTEXT_NAME_DOCUMENT + ">", level));

    // Serialize the pages.
    lines.add(indent("<" + CONTEXT_NAME_PAGES + ">", ++level));
    List<String> serializedPages = serializePages(pdf, types, ++level);
    if (serializedPages != null && !serializedPages.isEmpty()) {
      lines.addAll(serializedPages);
    }
    lines.add(indent("</" + CONTEXT_NAME_PAGES + ">", --level));

    // TODO: Serialize the fonts
    // lines.add(indent("<" + CONTEXT_NAME_FONTS + ">", level++));
    // List<String> serializedFonts = serializeFonts(doc, level);
    // if (serializedFonts != null && !serializedFonts.isEmpty()) {
    // lines.addAll(serializedFonts);
    // }
    // lines.add(indent("</" + CONTEXT_NAME_FONTS + ">", --level));

    // TODO: Serialize the colors.
    // lines.add(indent("<" + CONTEXT_NAME_COLORS + ">", level++));
    // List<String> serializedColors = serializeColors(doc, level);
    // if (serializedColors != null && !serializedColors.isEmpty()) {
    // lines.addAll(serializedColors);
    // }
    // lines.add(indent("</" + CONTEXT_NAME_COLORS + ">", --level));
    //
    // lines.add(indent("</" + CONTEXT_NAME_DOCUMENT + ">", --level));

    writeTo(lines, os);
  }

  /**
   * Serializes the pages of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * @param types
   *        The types of PDF elements to serialize.
   * @param level
   *        The current indentation level.
   * @return The serialization string representing the PDF pages.
   */
  protected List<String> serializePages(PdfDocument pdf,
      Set<Class<? extends PdfElement>> types, int level) {
    if (pdf == null) {
      return null;
    }

    List<PdfPage> pages = pdf.getPages();

    if (pages == null || pages.isEmpty()) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfPage page : pages) {
      int pageNum = page.getPageNumber();
      String in = "<" + CONTEXT_NAME_ELEMENT_PAGE + " id=\"" + pageNum + "\">";
      lines.add(indent(in, level++));
      lines.addAll(serializePage(page, types, level));
      lines.add(indent("</" + CONTEXT_NAME_ELEMENT_PAGE + ">", --level));
    }
    return lines;
  }

  /**
   * Serializes the given page.
   * 
   * @param page
   *        The page to serialize.
   * @param types
   *        The types of the PDF elements to serialize.
   * @param level
   *        The current indentation level.
   * @return The serialization string representing the given page.
   */
  protected List<String> serializePage(PdfPage page,
      Set<Class<? extends PdfElement>> types, int level) {
    if (page == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    List<String> serialized;

    // Serialize the text elements.
    if (types.isEmpty() || types.contains(PdfParagraph.class)) {
      serialized = serializePdfElements(page.getParagraphs(), level);
      if (serialized != null) {
        lines.addAll(serialized);
      }
    }
    for (PdfParagraph paragraph : page.getParagraphs()) {
      if (types.isEmpty() || types.contains(PdfTextLine.class)) {
        serialized = serializePdfElements(paragraph.getTextLines(), level);
        if (serialized != null) {
          lines.addAll(serialized);
        }
      }
      for (PdfTextLine line : paragraph.getTextLines()) {
        if (types.isEmpty() || types.contains(PdfWord.class)) {
          serialized = serializePdfElements(line.getWords(), level);
          if (serialized != null) {
            lines.addAll(serialized);
          }
        }
        for (PdfWord word : line.getWords()) {
          if (types.isEmpty() || types.contains(PdfCharacter.class)) {
            serialized = serializePdfElements(word.getCharacters(), level);
            if (serialized != null) {
              lines.addAll(serialized);
            }
          }
        }
      }
    }

    // Serialize the graphical elements.
    if (types.isEmpty() || types.contains(PdfFigure.class)) {
      serialized = serializePdfElements(page.getFigures(), level);
      if (serialized != null) {
        lines.addAll(serialized);
      }
    }
    if (types.isEmpty() || types.contains(PdfShape.class)) {
      serialized = serializePdfElements(page.getShapes(), level);
      if (serialized != null) {
        lines.addAll(serialized);
      }
    }

    return lines;
  }

  /**
   * Serializes the given PDF elements.
   * 
   * @param elements
   *        The PDF elements to serialize.
   * @param level
   *        The current indentation level.
   * @return The serialization strings representing the given PDF elements.
   */
  protected List<String> serializePdfElements(
      Collection<? extends PdfElement> elements, int level) {
    if (elements == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfElement element : elements) {
      String serialized = serializePdfElement(element, level);
      if (serialized != null && !serialized.isEmpty()) {
        lines.add(serialized);
      }
    }
    return lines;
  }

  /**
   * Serializes the given PDF element.
   * 
   * @param element
   *        The PDF element to serialize.
   * @param level
   *        The current indentation level.
   * @return The serialization string representing the given PDF element.
   */
  protected String serializePdfElement(PdfElement element, int level) {
    if (element == null) {
      return null;
    }

    StringBuilder xml = new StringBuilder();

    String text = null;
    if (element instanceof HasText) {
      HasText hasText = (HasText) element;
      text = hasText.getText();

      if (text == null || text.trim().isEmpty()) {
        return null;
      }
    }

    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);

    xml.append(indent);
    xml.append("<");

    // TODO: Append the feature name.
    // PdfFeature feature = element.getFeature();
    // xml.append(feature != null ? feature.getFieldName() : "");

    // Append the page.
    PdfPage page = element.getPage();
    if (page != null) {
      int pageNum = page.getPageNumber();
      xml.append(" " + CONTEXT_NAME_ELEMENT_PAGE + "=\"" + pageNum + "\"");
    }

    // Append the bounding box.
    Rectangle r = element.getBoundingBox();
    if (r != null) {
      xml.append(" " + CONTEXT_NAME_ELEMENT_MIN_X + "=\"" + r.getMinX() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MIN_Y + "=\"" + r.getMinY() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MAX_X + "=\"" + r.getMaxX() + "\"");
      xml.append(" " + CONTEXT_NAME_ELEMENT_MAX_Y + "=\"" + r.getMaxY() + "\"");
    }

    // TODO: Append the font.
    // PdfFont font = element.getFont();
    // if (font != null) {
    // String fontId = font.getId();
    // float fs = element.getFontsize();
    // xml.append(
    // " " + CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT + "=\"" + fontId + "\"");
    // xml.append(
    // " " + CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT_SIZE + "=\"" + fs + "\"");
    // }

    // TODO: Append the most common color.
    // PdfColor color = element.getColor();
    // if (color != null) {
    // String colorId = color.getId();
    // xml.append(" " + CONTEXT_NAME_ELEMENT_MOST_COMMON_COLOR + "=\"" +
    // colorId
    // + "\"");
    // }

    // TODO: Append the role.
    // PdfRole role = element.getRole();
    // if (role != null) {
    // xml.append(" " + CONTEXT_NAME_ELEMENT_ROLE + "=\"" + role.name + "\"");
    // }
    xml.append(">");

    // Append the text.
    xml.append(StringEscapeUtils.escapeXml11(text));

    // TODO
    // xml.append("</" + (feature != null ? feature.getFieldName() : "") +
    // ">");

    return xml.toString();
  }

  // ==========================================================================

  /**
   * Serializes the fonts of the given document.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param level
   *        The current indentation level.
   * @return The serialization strings representing the given colors.
   */
  // protected List<String> serializeFonts(PdfDocument pdf, int level) {
  // if (pdf == null) {
  // return null;
  // }
  //
  // List<String> lines = new ArrayList<>();
  // for (PdfFont font : pdf.getFonts()) {
  // String serialized = serializeFont(font, level);
  // if (serialized != null && !serialized.isEmpty()) {
  // lines.add(serialized);
  // }
  // }
  // return lines;
  // }

  /**
   * Serializes the given font.
   * 
   * @param font
   *        The font to serialize.
   * @param level
   *        The current indentation level.
   * @return The serialization string representing the given color.
   */
  // protected String serializeFont(PdfFont font, int level) {
  // if (font == null) {
  // return null;
  // }
  //
  // String id = font.getId();
  // if (id == null) {
  // return null;
  // }
  //
  // StringBuilder xml = new StringBuilder();
  //
  // String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);
  //
  // xml.append(indent);
  // xml.append("<font id=\"" + id + "\"");
  //
  // String basename = font.getBasename();
  // xml.append(" " + CONTEXT_NAME_FONT_NAME + "=\"" + basename + "\"");
  // boolean isBold = font.isBold();
  // xml.append(" " + CONTEXT_NAME_FONT_IS_BOLD + "=\"" + isBold + "\"");
  // boolean isItalic = font.isItalic();
  // xml.append(" " + CONTEXT_NAME_FONT_IS_ITALIC + "=\"" + isItalic + "\"");
  // boolean isType3 = font.isType3Font();
  // xml.append(" " + CONTEXT_NAME_FONT_IS_TYPE3 + "=\"" + isType3 + "\"");
  // xml.append(" />");
  //
  // return xml.toString();
  // }

  // ==========================================================================
  // Methods to serialize colors.

  /**
   * Serializes the colors of the given document.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param level
   *        The current indentation level.
   * @return The serialization strings representing the given colors.
   */
  // protected List<String> serializeColors(PdfDocument pdf, int level) {
  // if (pdf == null) {
  // return null;
  // }
  //
  // List<String> lines = new ArrayList<>();
  // for (PdfColor color : pdf.getColors()) {
  // String serialized = serializeColor(color, level);
  // if (serialized != null && !serialized.isEmpty()) {
  // lines.add(serialized);
  // }
  // }
  // return lines;
  // }

  /**
   * Serializes the given color.
   * 
   * @param color
   *        The color to serialize.
   * @param level
   *        The current indentation level.
   * @return The serialization string representing the given color.
   */
  // protected String serializeColor(PdfColor color, int level) {
  // if (color == null) {
  // return null;
  // }
  //
  // String id = color.getId();
  //
  // if (id == null) {
  // return null;
  // }
  //
  // StringBuilder xml = new StringBuilder();
  //
  // String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);
  //
  // xml.append(indent);
  // xml.append("<color id=\"" + id + "\"");
  //
  // xml.append(" " + CONTEXT_NAME_COLOR_R + "=\"" + color.getR() + "\"");
  // xml.append(" " + CONTEXT_NAME_COLOR_G + "=\"" + color.getG() + "\"");
  // xml.append(" " + CONTEXT_NAME_COLOR_B + "=\"" + color.getB() + "\"");
  // xml.append(" />");
  //
  // return xml.toString();
  // }

  // ==========================================================================

  /**
   * Writes the given JSON object to the given output stream.
   * 
   * @param lines
   *        The lines representing the serialization of the PDF document.
   * @param os
   *        The stream to write to.
   * @throws IOException
   *         If something went wrong while writing to the stream.
   */
  protected void writeTo(List<String> lines, OutputStream os)
      throws IOException {
    String serialized = CollectionUtils.join(lines, System.lineSeparator());
    os.write(serialized.getBytes(StandardCharsets.UTF_8));
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
}
