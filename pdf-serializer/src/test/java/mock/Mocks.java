package mock;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml11;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;
import org.mockito.Mockito;

import de.freiburg.iif.model.Rectangle;
import model.Comparators;
import model.PdfCharacter;
import model.PdfColor;
import model.PdfDocument;
import model.PdfFeature;
import model.PdfFigure;
import model.PdfFont;
import model.PdfPage;
import model.PdfShape;

/**
 * Class that provides mocks for objects within a PdfDocument.
 *
 * @author Claudius Korzen
 */
public class Mocks {
  /**
   * Returns a mocked rectangle.
   */
  public static Rectangle mockRectangle1() {
    Rectangle rect1 = Mockito.mock(Rectangle.class);
    when(rect1.getMinX()).thenReturn(0.0f);
    when(rect1.getMinY()).thenReturn(0.0f);
    when(rect1.getMaxX()).thenReturn(5.0f);
    when(rect1.getMaxY()).thenReturn(5.0f);
    when(rect1.toString()).thenReturn("[0.0,0.0,5.0,5.0]");
    return rect1;
  }

  /**
   * Returns another mocked rectangle.
   */
  public static Rectangle mockRectangle2() {
    Rectangle rect2 = Mockito.mock(Rectangle.class);
    when(rect2.getMinX()).thenReturn(5.0f);
    when(rect2.getMinY()).thenReturn(2.5f);
    when(rect2.getMaxX()).thenReturn(7.2f);
    when(rect2.getMaxY()).thenReturn(3.7f);
    when(rect2.toString()).thenReturn("[5.0,2.5,7.2,3.7]");
    return rect2;
  }

  /**
   * Returns a mocked color.
   */
  public static PdfColor mockColor1() {
    PdfColor color = Mockito.mock(PdfColor.class);

    when(color.getId()).thenReturn("color-1");

    JSONObject json = toJson(color);
    when(color.toJson()).thenReturn(json);

    String xml = toXml(color);
    when(color.toXml(Mockito.anyInt(), Mockito.anyInt())).thenReturn(xml);

    String tsv = toTsv(color);
    when(color.toTsv()).thenReturn(tsv);

    return color;
  }

  /**
   * Returns another mocked color.
   */
  public static PdfColor mockColor2() {
    PdfColor color = Mockito.mock(PdfColor.class);

    when(color.getId()).thenReturn("color-2");

    JSONObject json = toJson(color);
    when(color.toJson()).thenReturn(json);

    String xml = toXml(color);
    when(color.toXml(Mockito.anyInt(), Mockito.anyInt())).thenReturn(xml);

    String tsv = toTsv(color);
    when(color.toTsv()).thenReturn(tsv);

    return color;
  }

  /**
   * Returns a mocked font.
   */
  public static PdfFont mockFont1() {
    PdfFont font = Mockito.mock(PdfFont.class);

    when(font.getId()).thenReturn("font-1");
    when(font.getBasename()).thenReturn("arial");
    
    JSONObject json = toJson(font);
    when(font.toJson()).thenReturn(json);

    String xml = toXml(font);
    when(font.toXml(Mockito.anyInt(), Mockito.anyInt())).thenReturn(xml);

    String tsv = toTsv(font);
    when(font.toTsv()).thenReturn(tsv);

    return font;
  }

  /**
   * Returns a mocked character.
   */
  public static PdfCharacter mockCharacter1(PdfDocument doc, PdfPage page) {
    PdfCharacter char1 = Mockito.mock(PdfCharacter.class);
    PdfColor color = mockColor1();
    PdfFont font = mockFont1();
    Rectangle rect = mockRectangle1();

    when(char1.getColor()).thenReturn(color);
    when(char1.getFont()).thenReturn(font);
    when(char1.getFontsize()).thenReturn(12f);
    when(char1.getPage()).thenReturn(page);
    when(char1.getPdfDocument()).thenReturn(doc);
    when(char1.getRectangle()).thenReturn(rect);
    when(char1.getUnicode()).thenReturn("a");
    when(char1.getFeature()).thenReturn(PdfFeature.characters);
    
    JSONObject json = toJson(char1);
    when(char1.toJson()).thenReturn(json);

    String xml = toXml(char1);
    when(char1.toXml(Mockito.anyInt(), Mockito.anyInt())).thenReturn(xml);

    String tsv = toTsv(char1);
    when(char1.toTsv()).thenReturn(tsv);

    return char1;
  }

  /**
   * Returns another mocked character.
   */
  public static PdfCharacter mockCharacter2(PdfDocument doc, PdfPage page) {
    PdfCharacter char2 = Mockito.mock(PdfCharacter.class);
    PdfColor color = mockColor2();
    PdfFont font = mockFont1();
    Rectangle rect = mockRectangle2();

    when(char2.getColor()).thenReturn(color);
    when(char2.getFont()).thenReturn(font);
    when(char2.getFontsize()).thenReturn(10.5f);
    when(char2.getPage()).thenReturn(page);
    when(char2.getPdfDocument()).thenReturn(doc);
    when(char2.getRectangle()).thenReturn(rect);
    when(char2.getUnicode()).thenReturn("b");
    when(char2.getFeature()).thenReturn(PdfFeature.characters);
    
    JSONObject json = toJson(char2);
    when(char2.toJson()).thenReturn(json);

    String xml = toXml(char2);
    when(char2.toXml(Mockito.anyInt(), Mockito.anyInt())).thenReturn(xml);

    String tsv = toTsv(char2);
    when(char2.toTsv()).thenReturn(tsv);

    return char2;
  }

  /**
   * Returns a mocked figure.
   */
  public static PdfFigure mockFigure1(PdfDocument document, PdfPage page) {
    PdfFigure figure1 = Mockito.mock(PdfFigure.class);
    Rectangle rect = mockRectangle1();

    when(figure1.getPage()).thenReturn(page);
    when(figure1.getPdfDocument()).thenReturn(document);
    when(figure1.getRectangle()).thenReturn(rect);
    when(figure1.getFeature()).thenReturn(PdfFeature.figures);
    
    JSONObject json = toJson(figure1);
    when(figure1.toJson()).thenReturn(json);

    String xml = toXml(figure1);
    when(figure1.toXml(Mockito.anyInt(), Mockito.anyInt())).thenReturn(xml);

    String tsv = toTsv(figure1);
    when(figure1.toTsv()).thenReturn(tsv);

    return figure1;
  }

  /**
   * Returns another mocked figure.
   */
  public static PdfFigure mockFigure2(PdfDocument document, PdfPage page) {
    PdfFigure figure2 = Mockito.mock(PdfFigure.class);
    Rectangle rect = mockRectangle2();

    when(figure2.getPage()).thenReturn(page);
    when(figure2.getPdfDocument()).thenReturn(document);
    when(figure2.getRectangle()).thenReturn(rect);
    when(figure2.getFeature()).thenReturn(PdfFeature.figures);
    
    JSONObject json = toJson(figure2);
    when(figure2.toJson()).thenReturn(json);

    String xml = toXml(figure2);
    when(figure2.toXml(Mockito.anyInt(), Mockito.anyInt())).thenReturn(xml);

    String tsv = toTsv(figure2);
    when(figure2.toTsv()).thenReturn(tsv);

    return figure2;
  }

  /**
   * Returns a mocked shape.
   */
  public static PdfShape mockShape1(PdfDocument document, PdfPage page) {
    PdfShape shape1 = Mockito.mock(PdfShape.class);
    PdfColor color = mockColor2();
    Rectangle rect = mockRectangle2();

    when(shape1.getColor()).thenReturn(color);
    when(shape1.getPage()).thenReturn(page);
    when(shape1.getPdfDocument()).thenReturn(document);
    when(shape1.getRectangle()).thenReturn(rect);
    when(shape1.getFeature()).thenReturn(PdfFeature.shapes);
    
    JSONObject json = toJson(shape1);
    when(shape1.toJson()).thenReturn(json);

    String xml = toXml(shape1);
    when(shape1.toXml(Mockito.anyInt(), Mockito.anyInt())).thenReturn(xml);

    String tsv = toTsv(shape1);
    when(shape1.toTsv()).thenReturn(tsv);

    return shape1;
  }

  /**
   * Returns a mocked page.
   */
  public static PdfPage mockPage1(PdfDocument document) {
    PdfPage page1 = Mockito.mock(PdfPage.class);
    when(page1.getPageNumber()).thenReturn(1);
    
    PdfCharacter char1 = mockCharacter1(document, page1);
    PdfCharacter char2 = mockCharacter2(document, page1);
    PdfFigure figure1 = mockFigure1(document, page1);
    PdfShape shape1 = mockShape1(document, page1);

    List<PdfCharacter> chars = Arrays.asList(char1, char2);
    List<PdfFigure> figures = Arrays.asList(figure1);
    List<PdfShape> shapes = Arrays.asList(shape1);

    HashSet<PdfFont> fonts = new HashSet<>();
    HashSet<PdfColor> colors = new HashSet<>();
    for (PdfCharacter character : chars) {
      fonts.add(character.getFont());
      colors.add(character.getColor());
    }
    for (PdfShape shape : shapes) {
      colors.add(shape.getColor());
    }
    when(page1.getTextCharacters()).thenReturn(chars);
    when(page1.getFigures()).thenReturn(figures);
    when(page1.getShapes()).thenReturn(shapes);
    when(page1.getPdfDocument()).thenReturn(document);
    when(page1.getColors()).thenReturn(colors);
    when(page1.getFonts()).thenReturn(fonts);

    doReturn(chars).when(page1).getElementsByFeature(PdfFeature.characters);
    doReturn(figures).when(page1).getElementsByFeature(PdfFeature.figures);
    doReturn(shapes).when(page1).getElementsByFeature(PdfFeature.shapes);
    doReturn(fonts).when(page1).getFonts();
    doReturn(colors).when(page1).getColors();

    return page1;
  }

  /**
   * Returns another mocked page.
   */
  public static PdfPage mockPage2(PdfDocument document) {
    PdfPage page2 = Mockito.mock(PdfPage.class);
   
    when(page2.getPageNumber()).thenReturn(2);
    when(page2.getTextCharacters()).thenReturn(new ArrayList<PdfCharacter>());
    when(page2.getShapes()).thenReturn(new ArrayList<PdfShape>());
    
    PdfFigure figure = mockFigure2(document, page2);
    List<PdfFigure> figures = Arrays.asList(figure);
    when(page2.getFigures()).thenReturn(figures);

    doReturn(figures).when(page2).getElementsByFeature(PdfFeature.figures);

    return page2;
  }

  /**
   * Returns a mocked document.
   */
  public static PdfDocument mockDocument1() {
    PdfDocument document1 = Mockito.mock(PdfDocument.class);
    PdfPage page1 = mockPage1(document1);
    PdfPage page2 = mockPage2(document1);

    when(document1.getPages()).thenReturn(Arrays.asList(page1, page2));

    List<PdfFont> fonts = getFonts(document1);
    when(document1.getFonts()).thenReturn(fonts);

    List<PdfColor> colors = getColors(document1);
    when(document1.getColors()).thenReturn(colors);

    return document1;
  }

  // ___________________________________________________________________________

  protected static List<PdfFont> getFonts(PdfDocument doc) {
    HashMap<String, PdfFont> fonts = new HashMap<>();

    for (PdfPage page : doc.getPages()) {
      for (PdfFont font : page.getFonts()) {
        fonts.put(font.getId(), font);
      }
    }
    List<PdfFont> fontsList = new ArrayList<>(fonts.values());

    Collections.sort(fontsList, new Comparators.IdComparator());

    return fontsList;
  }

  protected static List<PdfColor> getColors(PdfDocument doc) {
    HashMap<String, PdfColor> colors = new HashMap<>();

    for (PdfPage page : doc.getPages()) {
      for (PdfColor color : page.getColors()) {
        colors.put(color.getId(), color);
      }
    }
    List<PdfColor> colorsList = new ArrayList<>(colors.values());

    Collections.sort(colorsList, new Comparators.IdComparator());

    return colorsList;
  }

  // ___________________________________________________________________________

  protected static JSONObject toJson(PdfFont font) {
    JSONObject json = new JSONObject();

    json.put("id", font.getId());
    json.put("name", font.getBasename());
    json.put("bold", font.isBold());
    json.put("italic", font.isItalic());
    json.put("type3", font.isType3Font());

    return json;
  }

  protected static String toXml(PdfFont font) {
    StringBuilder xml = new StringBuilder();

    String indent = " ";

    xml.append(indent);
    xml.append("<");
    xml.append("font");
    xml.append(" id=\"" + font.getId() + "\"");
    xml.append(" name=\"" + escapeXml11(font.getBasename()) + "\"");
    xml.append(" bold=\"" + (font.isBold() ? "true" : "false") + "\"");
    xml.append(" italic=\"" + (font.isItalic() ? "true" : "false") + "\"");
    xml.append(" type3=\"" + (font.isType3Font() ? "true" : "false") + "\"");
    xml.append(" />");

    return xml.toString();
  }

  protected static String toTsv(PdfFont font) {
    StringBuilder tsv = new StringBuilder();

    tsv.append("font");
    tsv.append("\t");
    tsv.append(font.getId());
    tsv.append("\t");
    tsv.append(font.getBasename());
    tsv.append("\t");
    tsv.append(font.isBold() ? "1" : 0);
    tsv.append("\t");
    tsv.append(font.isItalic() ? "1" : 0);
    tsv.append("\t");
    tsv.append(font.isType3Font() ? "1" : 0);

    return tsv.toString();
  }

  // ___________________________________________________________________________

  protected static JSONObject toJson(PdfColor color) {
    JSONObject json = new JSONObject();

    json.put("id", color.getId());
    json.put("r", color.getR());
    json.put("g", color.getG());
    json.put("b", color.getB());

    return json;
  }

  protected static String toXml(PdfColor color) {
    StringBuilder xml = new StringBuilder();

    String indent = " ";

    xml.append(indent);
    xml.append("<");
    xml.append("color");
    xml.append(" id=\"" + color.getId() + "\"");
    xml.append(" r=\"" + color.getR() + "\"");
    xml.append(" g=\"" + color.getG() + "\"");
    xml.append(" b=\"" + color.getB() + "\"");
    xml.append(" />");

    return xml.toString();
  }

  protected static String toTsv(PdfColor color) {
    StringBuilder tsv = new StringBuilder();

    float[] rgb = { color.getR(), color.getG(), color.getB() };

    tsv.append("color");
    tsv.append("\t");
    tsv.append(color.getId());
    tsv.append("\t");
    tsv.append(Arrays.toString(rgb));

    return tsv.toString();
  }

  // ___________________________________________________________________________

  protected static JSONObject toJson(PdfCharacter character) {
    JSONObject json = new JSONObject();

    json.put("unicode", character.getUnicode());
    json.put("minX", character.getRectangle().getMinX());
    json.put("minY", character.getRectangle().getMinY());
    json.put("maxX", character.getRectangle().getMaxX());
    json.put("maxY", character.getRectangle().getMaxY());
    json.put("font", character.getFont().getId());
    json.put("fontsize", character.getFontsize());
    json.put("color", character.getColor().getId());

    return json;
  }

  protected static String toXml(PdfCharacter character) {
    StringBuilder xml = new StringBuilder();

    String indent = " ";

    xml.append(indent);
    xml.append("<");
    xml.append(character.getFeature().getField());
    xml.append(" minX=\"" + character.getRectangle().getMinX() + "\"");
    xml.append(" minY=\"" + character.getRectangle().getMinY() + "\"");
    xml.append(" maxX=\"" + character.getRectangle().getMaxX() + "\"");
    xml.append(" maxY=\"" + character.getRectangle().getMaxY() + "\"");
    xml.append(" font=\"" + character.getFont().getId() + "\"");
    xml.append(" fontsize=\"" + character.getFontsize() + "\"");
    xml.append(" color=\"" + character.getColor().getId() + "\"");
    xml.append(">");
    xml.append(escapeXml11(character.getUnicode()));
    xml.append("</" + character.getFeature().getField() + ">");

    return xml.toString();
  }

  protected static String toTsv(PdfCharacter character) {
    StringBuilder tsv = new StringBuilder();

    tsv.append(character.getFeature().getField());
    tsv.append("\t");
    tsv.append(character.getUnicode().replaceAll("\t", " "));
    tsv.append("\t");
    tsv.append(character.getRectangle());
    tsv.append("\t");
    tsv.append(character.getFont().getId());
    tsv.append("\t");
    tsv.append(character.getFontsize());
    tsv.append("\t");
    tsv.append(character.getColor().getId());

    return tsv.toString();
  }

  // ___________________________________________________________________________

  protected static JSONObject toJson(PdfFigure figure) {
    JSONObject json = new JSONObject();

    json.put("minX", figure.getRectangle().getMinX());
    json.put("minY", figure.getRectangle().getMinY());
    json.put("maxX", figure.getRectangle().getMaxX());
    json.put("maxY", figure.getRectangle().getMaxY());

    return json;
  }

  protected static String toXml(PdfFigure figure) {
    StringBuilder xml = new StringBuilder();

    String indent = " ";

    xml.append(indent);
    xml.append("<");
    xml.append(figure.getFeature().getField());
    xml.append(" minX=\"" + figure.getRectangle().getMinX() + "\"");
    xml.append(" minY=\"" + figure.getRectangle().getMinY() + "\"");
    xml.append(" maxX=\"" + figure.getRectangle().getMaxX() + "\"");
    xml.append(" maxY=\"" + figure.getRectangle().getMaxY() + "\"");
    xml.append(" />");

    return xml.toString();
  }

  protected static String toTsv(PdfFigure figure) {
    StringBuilder tsv = new StringBuilder();

    tsv.append(figure.getFeature().getField());
    tsv.append("\t");
    tsv.append(figure.getPage().getPageNumber());
    tsv.append("\t");
    tsv.append(figure.getRectangle());

    return tsv.toString();
  }

  // ___________________________________________________________________________

  protected static JSONObject toJson(PdfShape shape) {
    JSONObject json = new JSONObject();

    json.put("minX", shape.getRectangle().getMinX());
    json.put("minY", shape.getRectangle().getMinY());
    json.put("maxX", shape.getRectangle().getMaxX());
    json.put("maxY", shape.getRectangle().getMaxY());
    json.put("color", shape.getColor().getId());

    return json;
  }

  protected static String toXml(PdfShape shape) {
    StringBuilder xml = new StringBuilder();

    String indent = " ";

    xml.append(indent);
    xml.append("<");
    xml.append(shape.getFeature().getField());
    xml.append(" minX=\"" + shape.getRectangle().getMinX() + "\"");
    xml.append(" minY=\"" + shape.getRectangle().getMinY() + "\"");
    xml.append(" maxX=\"" + shape.getRectangle().getMaxX() + "\"");
    xml.append(" maxY=\"" + shape.getRectangle().getMaxY() + "\"");
    xml.append(" color=\"" + shape.getColor().getId() + "\"");
    xml.append(" />");

    return xml.toString();
  }

  protected static String toTsv(PdfShape shape) {
    StringBuilder tsv = new StringBuilder();

    tsv.append(shape.getFeature().getField());
    tsv.append("\t");
    tsv.append(shape.getPage().getPageNumber());
    tsv.append("\t");
    tsv.append(shape.getRectangle());
    tsv.append("\t");
    tsv.append(shape.getColor().getId());

    return tsv.toString();
  }
}
