package parser.pdfbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.freiburg.iif.model.Rectangle;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfFigure;
import model.PdfPage;
import model.PdfShape;

/**
 * Tests for our PdfBoxParser.
 *
 * @author Claudius Korzen
 */
public class PdfBoxParserTest {
  /**
   * The instance of PdfBoxParser to test.
   */
  protected PdfBoxParser parser = new PdfBoxParser();
  
  /**
   * Test the parser with a null reference.
   */
  @Test
  public void testWithNoFile() throws Exception {
    PdfDocument document = parser.parse(null);
    assertEquals(null, document);
  }
  
  /**
   * Test the parser with a null reference.
   */
  @Test(expected = Exception.class)
  public void testWithNoPdfFile() throws Exception {
    Path file = Paths.get("src/test/resources/input/no-pdfs/no-pdf.xml");
    PdfDocument document = parser.parse(file);
    assertEquals(null, document);
  }
  
  /**
   * Test the parser with a pdf containing only text.
   */
  @Test
  public void testTextPdfFile() throws Exception {
    Path f = Paths.get("src/test/resources/input/only-text.pdf");
    PdfDocument d = parser.parse(f);
    assertNotNull(d);
    assertNotNull(d.getPages());
    assertEquals(1, d.getPages().size());
    
    PdfPage page = d.getPages().get(0);
    
    assertNotNull(page);
    assertNotNull(page.getTextCharacters());    
    assertEquals(22, page.getTextCharacters().size());
    
    PdfCharacter c = page.getTextCharacters().get(0);
    assertEquals("H", c.getUnicode());
    assertRect("[148.712,707.125,156.18396,713.92944]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
        
    c = page.getTextCharacters().get(1);
    assertEquals("e", c.getUnicode());
    assertRect("[156.18396,707.01544,160.60736,711.58826]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(2);
    assertEquals("l", c.getUnicode());
    assertRect("[160.60736,707.125,163.367,714.03906]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(3);
    assertEquals("l", c.getUnicode());
    assertRect("[163.367,707.125,166.12665,714.03906]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(4);
    assertEquals("o", c.getUnicode());
    assertRect("[166.12665,707.01544,171.10794,711.58826]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(5);
    assertEquals("W", c.getUnicode());
    assertRect("[174.42549,706.9058,184.65707,713.92944]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(6);
    assertEquals("o", c.getUnicode());
    assertRect("[183.83018,707.01544,188.81148,711.58826]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(7);
    assertEquals("r", c.getUnicode());
    assertRect("[188.81148,707.125,192.70685,711.52844]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(8);
    assertEquals("l", c.getUnicode());
    assertRect("[192.70685,707.125,195.46649,714.03906]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(9);
    assertEquals("d", c.getUnicode());
    assertRect("[195.46649,707.01544,200.99573,714.03906]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(10);
    assertEquals(".", c.getUnicode());
    assertRect("[200.99573,707.125,203.75537,708.181]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(11);
    assertEquals("H", c.getUnicode());
    assertRect("[208.17877,707.125,215.65073,713.92944]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(12);
    assertEquals("o", c.getUnicode());
    assertRect("[215.65073,707.01544,220.63202,711.58826]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(13);
    assertEquals("w", c.getUnicode());
    assertRect("[220.36302,707.01544,227.55602,711.4189]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(14);
    assertEquals("a", c.getUnicode());
    assertRect("[230.87357,707.01544,235.85486,711.58826]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(15);
    assertEquals("r", c.getUnicode());
    assertRect("[235.85486,707.125,239.75023,711.52844]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(16);
    assertEquals("e", c.getUnicode());
    assertRect("[239.75023,707.01544,244.17363,711.58826]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(17);
    assertEquals("y", c.getUnicode());
    assertRect("[247.49118,705.08264,252.74147,711.4189]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(18);
    assertEquals("o", c.getUnicode());
    assertRect("[252.47247,707.01544,257.45377,711.58826]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(19);
    assertEquals("u", c.getUnicode());
    assertRect("[257.45377,707.01544,262.983,711.52844]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(20);
    assertEquals("?", c.getUnicode());
    assertRect("[262.97305,707.125,267.6754,714.1486]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(21);
    assertEquals("1", c.getUnicode());
    assertRect("[303.133,139.255,308.1143,145.89009]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());

    
    assertNotNull(page.getFigures());
    assertEquals(0, page.getFigures().size());
    
    assertNotNull(page.getShapes());
    assertEquals(0, page.getShapes().size());
    
    assertNotNull(d.getPdfFile());
    assertEquals(f.toAbsolutePath(), d.getPdfFile().toAbsolutePath());
  }
  
  /**
   * Test the parser with a pdf containing text and a figure.
   */
  @Test
  public void testTextAndImagePdfFile() throws Exception {
    Path f = Paths.get("src/test/resources/input/text-with-figure.pdf");
    PdfDocument d = parser.parse(f);
    assertNotNull(d);
    assertNotNull(d.getPages());
    assertEquals(1, d.getPages().size());
    
    PdfPage page = d.getPages().get(0);
    
    assertNotNull(page);
    assertNotNull(page.getTextCharacters());    
    assertEquals(12, page.getTextCharacters().size());
        
    PdfCharacter c = page.getTextCharacters().get(0);
    assertEquals("C", c.getUnicode());
    assertRect("[133.768,657.0158,140.961,664.2586]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
        
    c = page.getTextCharacters().get(1);
    assertEquals("a", c.getUnicode());
    assertRect("[140.961,657.1254,145.94229,661.69824]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(2);
    assertEquals("t", c.getUnicode());
    assertRect("[145.94229,657.1254,149.80779,663.362]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(3);
    assertEquals("c", c.getUnicode());
    assertRect("[153.12534,657.1254,157.54874,661.69824]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(4);
    assertEquals("o", c.getUnicode());
    assertRect("[157.54874,657.1254,162.53003,661.69824]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(5);
    assertEquals("n", c.getUnicode());
    assertRect("[162.53003,657.235,168.05927,661.6384]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(6);
    assertEquals("t", c.getUnicode());
    assertRect("[167.79027,657.1254,171.65576,663.362]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(7);
    assertEquals("e", c.getUnicode());
    assertRect("[171.65576,657.1254,176.07916,661.69824]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(8);
    assertEquals("n", c.getUnicode());
    assertRect("[176.07916,657.235,181.6084,661.6384]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(9);
    assertEquals("t", c.getUnicode());
    assertRect("[181.32945,657.1254,185.19495,663.362]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(10);
    assertEquals(":", c.getUnicode());
    assertRect("[185.19495,657.235,187.95459,661.5289]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
    
    c = page.getTextCharacters().get(11);
    assertEquals("1", c.getUnicode());
    assertRect("[303.133,89.365,308.1143,96.00009]", c.getRectangle());
//  assertEquals(null, character.getFont());
//  assertEquals(9.925f, character.getFontsize(), 0.001f);
//  assertEquals(null, character.getColor());
//  assertEquals(null, character.getFont());
    assertEquals(page, c.getPage());
        
    assertNotNull(page.getFigures());
    assertEquals(1, page.getFigures().size());
    
    PdfFigure figure = page.getFigures().get(0);
    assertRect("[133.768,516.317,305.6301,645.2839]", c.getRectangle());
    assertEquals(page, figure.getPage());
    
    assertNotNull(page.getShapes());
    assertEquals(0, page.getShapes().size());
    
    assertNotNull(d.getPdfFile());
    assertEquals(f.toAbsolutePath(), d.getPdfFile().toAbsolutePath());
  }
  
  /**
   * Test a pdf file with formulas.
   */
  @Test
  public void testFormulasPdfFile() throws Exception {
    Path f = Paths.get("src/test/resources//input/subdir-1/formulas.pdf");
    PdfDocument document = parser.parse(f);
    assertNotNull(document);
    assertNotNull(document.getPages());
    assertEquals(1, document.getPages().size());
    
    PdfPage page = document.getPages().get(0);
        
    assertNotNull(page.getTextCharacters());
    assertEquals(74, page.getTextCharacters().size());
    PdfCharacter c = page.getTextCharacters().get(0);
    assertRect("[278.736,650.33026,282.9691,655.23987]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(1);
    assertRect("[273.742,633.4634,288.128,647.411]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(2);
    assertRect("[273.69202,625.8942,277.9251,630.80383]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(3);
    assertRect("[278.096,626.77295,284.21204,628.6489]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(4);
    assertRect("[284.21204,625.964,288.1801,630.5946]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(5);
    assertRect("[289.84302,637.8374,293.27014,644.5322]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(40); // x_k^2.
    assertRect("[295.8616,607.9612,298.62125,610.94]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(41);
    assertRect("[300.27502,609.7744,305.96368,614.2874]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(42);
    assertRect("[306.011,613.49896,309.97906,618.1296]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(43);
    assertRect("[306.011,606.9922,310.24408,611.9018]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(50); // The curly brace.
    assertRect("[271.52597,557.0917,279.54587,586.9695]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(57); // equal sign.
    assertRect("[353.22397,576.6886,360.9649,580.9526]", c.getRectangle());
    assertEquals(page, c.getPage());
    c = page.getTextCharacters().get(70); // equal sign.
    assertRect("[353.22394,562.3426,360.96487,566.60657]", c.getRectangle());
    assertEquals(page, c.getPage());
    
    assertNotNull(page.getFigures());
    assertEquals(0, page.getFigures().size());
    
    assertNotNull(page.getShapes());
    assertEquals(1, page.getShapes().size());
    
    PdfShape s = page.getShapes().get(0);
    assertRect("[307.755,640.437,336.361,640.437]", c.getRectangle());
//    assertEquals(null, shape.getColor());
    assertEquals(page, s.getPage());
  }
  
  /**
   * Test a pdf file with shapes.
   */
  @Test
  public void testShapesPdfFile() throws Exception {
    Path f = Paths.get("src/test/resources/input/subdir-2/shapes.pdf");
    PdfDocument document = parser.parse(f);
    assertNotNull(document);
    assertNotNull(document.getPages());
    assertEquals(1, document.getPages().size());
    
    PdfPage page = document.getPages().get(0);
    
    assertNotNull(page.getTextCharacters());
    assertEquals(1, page.getTextCharacters().size());
    
    assertNotNull(page.getFigures());
    assertEquals(0, page.getFigures().size());
    
    assertNotNull(page.getShapes());
    assertEquals(5, page.getShapes().size());
    PdfShape s = page.getShapes().get(0);
    assertRect("[178.476,521.214,320.21002,662.948]", s.getRectangle());
//  assertEquals(null, shape.getColor());
//  assertEquals(-1, s.getWindingRule());
    assertEquals(page, s.getPage());
  
    s = page.getShapes().get(1);
    assertRect("[178.476,521.214,235.16962,662.948]", s.getRectangle());
//  assertEquals(null, shape.getColor());
//  assertEquals(-1, s.getWindingRule());
    assertEquals(page, s.getPage());
    
    s = page.getShapes().get(2);
    assertRect("[150.1292,492.8672,206.8228,549.5608]", s.getRectangle());
//  assertEquals(null, shape.getColor());
//  assertEquals(-1, s.getWindingRule());
    assertEquals(page, s.getPage());
    
    s = page.getShapes().get(3);
    assertRect("[235.16962,521.214,320.21002,634.6012]", s.getRectangle());
//  assertEquals(null, shape.getColor());
//  assertEquals(-1, s.getWindingRule());
    assertEquals(page, s.getPage());
    
    s = page.getShapes().get(4);
    assertRect("[263.51642,521.214,461.9441,662.948]", s.getRectangle());
//  assertEquals(null, shape.getColor());
//  assertEquals(-1, s.getWindingRule());
    assertEquals(page, s.getPage());
  }
  
  /**
   * Returns true, if the given string is equal to the string representation of 
   * the given rectangle.
   */
  protected boolean assertRect(String rectangleStr, Rectangle rectangle) {
    return rectangleStr.equals(rectangle.toString());
  }
}