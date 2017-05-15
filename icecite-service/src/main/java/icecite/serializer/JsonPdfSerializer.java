package icecite.serializer;

import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_X;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MAX_Y;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_X;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_MIN_Y;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_PAGE;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_ELEMENT_TEXT;
import static icecite.serializer.PdfSerializerConstants.CONTEXT_NAME_PAGES;
import static icecite.serializer.PdfSerializerConstants.INDENT_LENGTH;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

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
import icecite.utils.geometric.Rectangle;

// TODO: Implement the JSON serializer.

/**
 * An implementation of PdfSerializer that serializes a PDF document to JSON
 * format.
 *
 * @author Claudius Korzen
 */
public class JsonPdfSerializer implements PdfSerializer {
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
    JSONObject json = new JSONObject();

    // Serialize the pages.
    JSONArray serializedPages = serializePages(pdf, types);
    if (serializedPages != null && serializedPages.length() > 0) {
      json.put(CONTEXT_NAME_PAGES, serializedPages);
    }

    // TODO: Serialize the fonts of a PDF document.
    // // Serialize the fonts.
    // JSONArray serializedFonts = serializeFonts(pdf);
    // if (serializedFonts != null && serializedFonts.length() > 0) {
    // json.put(CONTEXT_NAME_FONTS, serializedFonts);
    // }

    // TODO: Serialize the colors of a PDF document.
    // // Serialize the colors.
    // JSONArray serializedColors = serializeColors(pdf);
    // if (serializedColors != null && serializedColors.length() > 0) {
    // json.put(CONTEXT_NAME_COLORS, serializedColors);
    // }

    // Write the serialization to given stream.
    writeTo(json, os);
  }

  // ==========================================================================

  /**
   * Serializes the pages of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param types
   *        The types of the PDF elements to serialize.
   * @return An JSON array representing the serialized PDF document.
   */
  protected JSONArray serializePages(PdfDocument pdf,
      Set<Class<? extends PdfElement>> types) {
    if (pdf == null) {
      return null;
    }

    List<PdfPage> pages = pdf.getPages();

    if (pages == null || pages.isEmpty()) {
      return null;
    }

    JSONArray json = new JSONArray();
    for (PdfPage page : pages) {
      JSONObject serialized = serializePage(page, types);
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
   * @param types
   *        The types of PDF elements to serialize.
   * @return A JSON object representing the serialized page.
   */
  protected JSONObject serializePage(PdfPage page,
      Set<Class<? extends PdfElement>> types) {
    if (page == null) {
      return null;
    }

    JSONObject json = new JSONObject();

    // TODO: Use constants.
    
    // Serialize the text elements.
    if (types.isEmpty() || types.contains(PdfParagraph.class)) {
      json.put("paragraphs", serializePdfElements(page.getParagraphs()));
    }
    for (PdfParagraph paragraph : page.getParagraphs()) {
      if (types.isEmpty() || types.contains(PdfTextLine.class)) {
        json.put("textlines", serializePdfElements(paragraph.getTextLines()));
      }
      for (PdfTextLine line : paragraph.getTextLines()) {
        if (types.isEmpty() || types.contains(PdfWord.class)) {
          json.put("words", serializePdfElements(line.getWords()));
        }
        for (PdfWord word : line.getWords()) {
          if (types.isEmpty() || types.contains(PdfCharacter.class)) {
            json.put("words", serializePdfElements(word.getCharacters()));
          }
        }
      }
    }

    // Serialize the graphical elements.
    if (types.isEmpty() || types.contains(PdfFigure.class)) {
      json.put("figures", serializePdfElements(page.getFigures()));
    }
    if (types.isEmpty() || types.contains(PdfShape.class)) {
      json.put("shapes", serializePdfElements(page.getShapes()));
    }

    if (json.length() > 0) {
      json.put("id", page.getPageNumber());
      return json;
    }

    return null;
  }

  /**
   * Serializes the given elements.
   * 
   * @param elements
   *        The elements to serialize.
   * @return A JSON array representing the serialized PDF elements.
   */
  protected JSONArray serializePdfElements(
      Collection<? extends PdfElement> elements) {
    if (elements == null) {
      return null;
    }

    JSONArray json = new JSONArray();
    for (PdfElement element : elements) {
      JSONObject serialized = serializePdfElement(element);
      if (serialized != null && serialized.length() > 0) {
        json.put(serialized);
      }
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
        json.put(CONTEXT_NAME_ELEMENT_TEXT, JSONObject.quote(text));
      }
    }

    // Serialize font.
//    if (element instanceof HasFont) {
//      HasFont hasFont = (HasFont) element;
//      PdfFont font = hasFont.getFont();
//
//      if (font != null) {
//        // TODO: Serialize font.
//        json.put(CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT, font.getId());
//        // TODO: Serialize font size.
//        json.put(CONTEXT_NAME_ELEMENT_MOST_COMMON_FONT_SIZE,
//        element.getFontsize());
//      }
//    }
    
    // TODO: Serialize color.
//    if (element instanceof HasColor) {
//      HasColor hasColor = (HasColor) element;
//      PdfColor color = hasColor.getColor();
//
//      if (color != null) {
//        json.put(CONTEXT_NAME_ELEMENT_MOST_COMMON_COLOR, color.getId());
//      }
//    }
    
    PdfPage page = element.getPage();
    if (page != null) {
      json.put(CONTEXT_NAME_ELEMENT_PAGE, page.getPageNumber());
    }

    Rectangle rect = element.getBoundingBox();
    if (rect != null) {
      json.put(CONTEXT_NAME_ELEMENT_MIN_X, rect.getMinX());
      json.put(CONTEXT_NAME_ELEMENT_MIN_Y, rect.getMinY());
      json.put(CONTEXT_NAME_ELEMENT_MAX_X, rect.getMaxX());
      json.put(CONTEXT_NAME_ELEMENT_MAX_Y, rect.getMaxY());
    }

    return json;
  }

  // ==========================================================================
  // Methods to serialize fonts.

  // /**
  // * Serializes the fonts of the given PDF document.
  // *
  // * @param document
  // * The PDF document to process.
  // * @return A JSON array representing the serialized fonts of the PDF
  // * document.
  // */
  // protected JSONArray serializeFonts(PdfDocument document) {
  // if (document == null) {
  // return null;
  // }
  //
  // JSONArray json = new JSONArray();
  // for (PdfFont font : document.getFonts()) {
  // JSONObject serialized = serializeFont(font);
  // if (serialized != null && serialized.length() > 0) {
  // json.put(serialized);
  // }
  // }
  // return json;
  // }

  // /**
  // * Serializes the given font.
  // *
  // * @param font
  // * The font to serialize.
  // * @return A JSON object representing the serialized font.
  // */
  // protected JSONObject serializeFont(PdfFont font) {
  // if (font == null) {
  // return null;
  // }
  //
  // String id = font.getId();
  // if (id == null) {
  // return null;
  // }
  //
  // JSONObject json = new JSONObject();
  // json.put(CONTEXT_NAME_FONT_ID, id);
  //
  // String basename = font.getBasename();
  // if (basename != null) {
  // json.put(CONTEXT_NAME_FONT_NAME, basename);
  // }
  //
  // json.put(CONTEXT_NAME_FONT_IS_BOLD, font.isBold());
  // json.put(CONTEXT_NAME_FONT_IS_ITALIC, font.isItalic());
  // json.put(CONTEXT_NAME_FONT_IS_TYPE3, font.isType3Font());
  //
  // return json;
  // }

  // ==========================================================================
  // Methods to serialize colors.

  // /**
  // * Serializes the colors of the given PDF document.
  // *
  // * @param document
  // * The PDF document to process.
  // * @return A JSON array representing the serialized colors of the PDF.
  // */
  // protected JSONArray serializeColors(PdfDocument document) {
  // if (document == null) {
  // return null;
  // }
  //
  // JSONArray json = new JSONArray();
  // for (PdfColor color : document.getColors()) {
  // JSONObject serialized = serializeColor(color);
  // if (serialized != null && serialized.length() > 0) {
  // json.put(serialized);
  // }
  // }
  // return json;
  // }

  // /**
  // * Serializes the given color.
  // *
  // * @param color
  // * The color to serialize.
  // * @return A JSON object representing the serialized color.
  // */
  // protected JSONObject serializeColor(PdfColor color) {
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
  // JSONObject json = new JSONObject();
  // json.put(CONTEXT_NAME_COLOR_ID, id);
  // json.put(CONTEXT_NAME_COLOR_R, color.getR());
  // json.put(CONTEXT_NAME_COLOR_G, color.getG());
  // json.put(CONTEXT_NAME_COLOR_B, color.getB());
  //
  // return json;
  // }

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
}
