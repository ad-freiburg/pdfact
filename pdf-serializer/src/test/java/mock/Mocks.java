package mock;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.mockito.Mockito;

import de.freiburg.iif.model.Rectangle;
import model.PdfCharacter;
import model.PdfColor;
import model.PdfDocument;
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
    return Mockito.mock(PdfColor.class);
  }
  
  /**
   * Returns another mocked color.
   */
  public static PdfColor mockColor2() {
    return Mockito.mock(PdfColor.class);
  }
  
  /**
   * Returns a mocked font.
   */
  public static PdfFont mockFont1() {
    return Mockito.mock(PdfFont.class);
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
    return shape1;
  }
  
  /**
   * Returns a mocked page.
   */
  public static PdfPage mockPage1(PdfDocument document) {
    PdfPage page1 = Mockito.mock(PdfPage.class);
    PdfCharacter char1 = mockCharacter1(document, page1);
    PdfCharacter char2 = mockCharacter2(document, page1);
    PdfFigure figure1 = mockFigure1(document, page1);
    PdfShape shape1 = mockShape1(document, page1);
    
    when(page1.getPageNumber()).thenReturn(1);
    when(page1.getTextCharacters()).thenReturn(Arrays.asList(char1, char2));
    when(page1.getFigures()).thenReturn(Arrays.asList(figure1));
    when(page1.getShapes()).thenReturn(Arrays.asList(shape1));
    when(page1.getPdfDocument()).thenReturn(document);
    return page1;
  }
  
  /**
   * Returns another mocked page.
   */
  public static PdfPage mockPage2(PdfDocument document) {
    PdfPage page2 = Mockito.mock(PdfPage.class);
    PdfFigure figure = mockFigure2(document, page2);
    
    when(page2.getPageNumber()).thenReturn(2);
    when(page2.getTextCharacters()).thenReturn(new ArrayList<PdfCharacter>());
    when(page2.getFigures()).thenReturn(Arrays.asList(figure));
    when(page2.getShapes()).thenReturn(new ArrayList<PdfShape>());
    when(page2.getPdfDocument()).thenReturn(document);
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
    return document1;
  }
}
