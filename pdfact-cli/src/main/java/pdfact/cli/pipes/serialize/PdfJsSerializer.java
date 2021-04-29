package pdfact.cli.pipes.serialize;

import static pdfact.cli.pipes.serialize.PdfSerializerConstants.B;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.COLOR;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.COLORS;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.FONT;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.FONTS;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.FONTSIZE;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.G;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.ID;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.IS_BOLD;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.IS_ITALIC;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.IS_TYPE3;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.MAX_X;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.MAX_Y;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.MIN_X;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.MIN_Y;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.NAME;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.PAGE;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.POSITIONS;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.R;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.ROLE;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.TEXT;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.TEXT_BLOCK;
import static pdfact.cli.pipes.serialize.PdfSerializerConstants.TEXT_BLOCKS;
import static pdfact.core.PdfActCoreSettings.DEFAULT_ENCODING;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import pdfact.cli.model.ExtractionUnit;
import pdfact.core.model.Color;
import pdfact.core.model.Document;
import pdfact.core.model.Element;
import pdfact.core.model.Font;
import pdfact.core.model.FontFace;
import pdfact.core.model.HasColor;
import pdfact.core.model.HasFontFace;
import pdfact.core.model.HasPosition;
import pdfact.core.model.HasPositions;
import pdfact.core.model.HasSemanticRole;
import pdfact.core.model.HasText;
import pdfact.core.model.Page;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;

/**
 * A serializer that outputs a document in the format as required by Robin's tool that improves
 * the search of pdf.js. This format equals to the normal JSON format of Pdfact, broken down by
 * text blocks. The text of the text blocks is composed by joining the text lines of a text blocks
 * using the following line endings:
 * (1) If a line does not end with a hyphenated word, add the ending " \n". This is a space
 *     followed by "\n". This identifies both a word ending and a line ending.
 *  (2) If the line ends with a hyphenated word, add the ending "\n". This is a single "\n".
 *      This identifies a line ending but not a word ending (since the word continues in the
 *      next line). Remove the hyphen if it is not mandatory.
 *
 *  Here are some examples that illustrate how this format joins two consecutive lines:
 *  (a) The lines "We introduce an" and "efficient algorithm" are joined to
 *      "We introduce an \nefficient algorithm".
 *  (b) The lines "We introduce an effi-" and "cient algorithm" are joined to
 *      "We introduce an effi\ncient algorithm" (without space before \n and without hyphen).
 *  (c) The lines "We introduce an high-" and "efficient algorithm" are joined to
 *      "We introduce a high-\nefficient algorithm" (without space before \n and with hyphen).
 *
 * @author Claudius Korzen
 */
public class PdfJsSerializer implements PdfSerializer {
  /**
   * The indentation length.
   */
  protected static final int INDENT_LENGTH = 2;

  // ==============================================================================================

  /**
   * The fonts of the PDF elements which were in fact serialized.
   */
  protected Set<Font> usedFonts;

  /**
   * The colors of the PDF elements which were in fact serialized.
   */
  protected Set<Color> usedColors;

  // ==============================================================================================
  // Constructors.

  /**
   * Creates a new serializer that serializes a PDF document in the format as required by Robin's
   * tool that improves the search of pdf.js.
   */
  public PdfJsSerializer() {
    this.usedFonts = new HashSet<>();
    this.usedColors = new HashSet<>();
  }

  // ==============================================================================================

  @Override
  public byte[] serialize(Document pdf) {
    // The serialization to return.
    String result = "";

    if (pdf != null) {
      // The JSON object to fill.
      JSONObject json = new JSONObject();

      // Create the section that contains all serialized PDF elements.
      serializeTextBlocks(pdf, json);

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

  // ==============================================================================================

  /**
   * Serializes the text blocks of the given PDF document.
   *
   * @param pdf  The PDF document to process.
   * @param json The JSON object to which the serialization should be appended.
   */
  protected void serializeTextBlocks(Document pdf, JSONObject json) {
    JSONArray result = new JSONArray();

    if (pdf != null) {
      for (Page page : pdf.getPages()) {
        for (TextBlock block : page.getTextBlocks()) {
          JSONObject blockJson = serializeTextBlock(block);
          if (blockJson != null) {
            result.put(blockJson);
          }
        }
      }
    }

    json.put(TEXT_BLOCKS, result);
  }

  /**
   * Serializes the given text block.
   *
   * @param block The text block to serialize.
   *
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeTextBlock(TextBlock block) {
    JSONObject result = new JSONObject();

    JSONObject blockJson = new JSONObject();

    if (block != null) {
      // Serialize the position of the block.
      Position position = block.getPosition();
      JSONArray serialized = serializePositions(position);
      if (serialized != null && serialized.length() > 0) {
        result.put(POSITIONS, serialized);
      }

      // Serialize the role of the block.
      SemanticRole role = block.getSemanticRole();
      if (role != null) {
        result.put(ROLE, role.getName());
      }

      // Serialize the font face of the block.
      FontFace fontFace = block.getCharacterStatistic().getMostCommonFontFace();
      if (fontFace != null) {
        Font font = fontFace.getFont();
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

      // Serialize the most common color of the characters in the block.
      Color color = block.getCharacterStatistic().getMostCommonColor();
      if (color != null) {
        String colorId = color.getId();
        if (colorId != null) {
          JSONObject colorJson = new JSONObject();
          colorJson.put(ID, colorId);
          result.put(COLOR, colorJson);
          this.usedColors.add(color);
        }
      }

      // Compose the text of the text blocks as describe in the very first comment of this class.
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < block.getTextLines().size(); i++) {
        TextLine line = block.getTextLines().get(i);

        // Append the whole line.
        sb.append(line.getText());

        Word lastWord = line.getLastWord();
        // Append a whitespace if the last word of the line is not hyphenated.
        if (!lastWord.isDehyphenated()) {
          sb.append(" ");
        } else {
          // Remove the trailing hyphen (the last character) from the last word of the line.
          if (!lastWord.isHyphenMandatory()) {
            sb.setLength(sb.length() - 1);
          }
        }

        // Append a "\n", except when the line is the last text line.
        if (i < block.getTextLines().size() - 1) {
          sb.append("\n");
        }
      }


      String text = sb.toString();
      if (!text.isEmpty()) {
        result.put(TEXT, text);
      }
    }

    // Wrap the JSON object with a JSON object that describes a text block.
    if (blockJson != null && blockJson.length() > 0) {
      result.put(TEXT_BLOCK, blockJson);
    }
    return result;
  }

  // ==============================================================================================

  /**
   * Serializes the given PDF element.
   *
   * @param element The element to serialize.
   *
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializePdfElement(Element element) {
    JSONObject result = new JSONObject();

    if (element != null) {
      // Serialize the list of positions of the element, if there is any.
      if (element instanceof HasPositions) {
        HasPositions hasPositions = (HasPositions) element;
        List<Position> positions = hasPositions.getPositions();

        JSONArray serialized = serializePositions(positions);
        if (serialized != null && serialized.length() > 0) {
          result.put(POSITIONS, serialized);
        }
      }

      // Serialize the single position of the element, if there is any.
      if (element instanceof HasPosition) {
        HasPosition hasPosition = (HasPosition) element;
        Position position = hasPosition.getPosition();

        JSONArray serialized = serializePositions(position);
        if (serialized != null && serialized.length() > 0) {
          result.put(POSITIONS, serialized);
        }
      }

      // Serialize the role of the element, if there is any.
      if (element instanceof HasSemanticRole) {
        HasSemanticRole hasRole = (HasSemanticRole) element;
        SemanticRole role = hasRole.getSemanticRole();

        if (role != null) {
          result.put(ROLE, role.getName());
        }
      }

      // Serialize the font face of the element, if there is any.
      if (element instanceof HasFontFace) {
        HasFontFace hasFontFace = (HasFontFace) element;
        FontFace fontFace = hasFontFace.getFontFace();

        if (fontFace != null) {
          Font font = fontFace.getFont();
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
        Color color = hasColor.getColor();

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

  // ==============================================================================================

  /**
   * Serializes the given PDF positions.
   *
   * @param positions The positions to serialize.
   *
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializePositions(Position... positions) {
    return serializePositions(Arrays.asList(positions));
  }

  /**
   * Serializes the given list of PDF positions.
   *
   * @param positions The list of positions to serialize.
   *
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializePositions(List<Position> positions) {
    JSONArray result = new JSONArray();
    if (positions != null) {
      for (Position position : positions) {
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
   * @param position The position to serialize.
   *
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializePosition(Position position) {
    JSONObject positionJson = new JSONObject();

    if (position != null) {
      Page page = position.getPage();
      int pageNumber = page.getPageNumber();
      Rectangle rect = position.getRectangle();

      if (pageNumber > 0 && rect != null) {
        positionJson.put(PAGE, pageNumber);

        // If we pass primitive floats here, the values would be casted to
        // double values (yielding in inaccurate numbers). So transform the
        // values to Float objects.
        positionJson.put(MIN_X, Float.valueOf(rect.getMinX()));
        positionJson.put(MIN_Y, Float.valueOf(rect.getMinY()));
        positionJson.put(MAX_X, Float.valueOf(rect.getMaxX()));
        positionJson.put(MAX_Y, Float.valueOf(rect.getMaxY()));
      }
    }
    return positionJson;
  }

  // ==============================================================================================

  /**
   * Serializes the given fonts.
   *
   * @param fonts The fonts to serialize.
   *
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializeFonts(Font... fonts) {
    return serializeFonts(new HashSet<>(Arrays.asList(fonts)));
  }

  /**
   * Serializes the given fonts.
   *
   * @param fonts The fonts to serialize.
   *
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializeFonts(Set<Font> fonts) {
    JSONArray result = new JSONArray();
    if (fonts != null) {
      for (Font font : fonts) {
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
   * @param font The font to serialize.
   *
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeFont(Font font) {
    JSONObject fontJson = new JSONObject();
    if (font != null) {
      String fontId = font.getId();
      if (fontId != null) {
        fontJson.put(ID, fontId);
      }

      String fontName = font.getNormalizedName();
      if (fontName != null) {
        fontJson.put(NAME, fontName);
      }

      fontJson.put(IS_BOLD, font.isBold());
      fontJson.put(IS_ITALIC, font.isItalic());
      fontJson.put(IS_TYPE3, font.isType3Font());
    }
    return fontJson;
  }

  // ==============================================================================================
  // Methods to serialize colors.

  /**
   * Serializes the given colors.
   *
   * @param colors The color to serialize.
   *
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializeColors(Color... colors) {
    return serializeColors(new HashSet<>(Arrays.asList(colors)));
  }

  /**
   * Serializes the given colors.
   *
   * @param colors The colors to serialize.
   *
   * @return A JSON array that represents the serialization.
   */
  protected JSONArray serializeColors(Set<Color> colors) {
    JSONArray result = new JSONArray();

    if (colors != null) {
      for (Color color : colors) {
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
   * @param color The color to serialize.
   *
   * @return A JSON object that represents the serialization.
   */
  protected JSONObject serializeColor(Color color) {
    JSONObject colorJson = new JSONObject();
    if (color != null) {
      String colorId = color.getId();
      int[] rgb = color.getRGB();

      if (colorId != null && rgb != null && rgb.length == 3) {
        colorJson.put(ID, colorId);
        colorJson.put(R, rgb[0]);
        colorJson.put(G, rgb[1]);
        colorJson.put(B, rgb[2]);
      }
    }
    return colorJson;
  }

  // ==============================================================================================

  @Override
  public Set<ExtractionUnit> getExtractionUnits() {
    return null;
  }

  @Override
  public void setExtractionUnits(Set<ExtractionUnit> extractionUnits) {
    // Nothing to do.
  }

  @Override
  public Set<SemanticRole> getSemanticRolesToInclude() {
    return null;
  }

  @Override
  public void setSemanticRolesToInclude(Set<SemanticRole> roles) {
    // Nothing to do.
  }
}
