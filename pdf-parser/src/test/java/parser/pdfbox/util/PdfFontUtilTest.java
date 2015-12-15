package parser.pdfbox.util;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the PdfFontUtil.
 *
 * @author Claudius Korzen
 *
 */
public class PdfFontUtilTest {
  // ___________________________________________________________________________
  // Test the computeBasename() method.
  
  /**
   * Test the computeBasename() method.
   */
  @Test
  public void testComputeBasename() {
    Assert.assertEquals(null, PdfFontUtil.computeBasename(null));
    
    String result = PdfFontUtil.computeBasename(PDType1Font.COURIER);
    Assert.assertEquals("courier", result);
    
    result = PdfFontUtil.computeBasename(PDType1Font.HELVETICA_OBLIQUE);
    Assert.assertEquals("helvetica-oblique", result);
    
    result = PdfFontUtil.computeBasename(PDType1Font.SYMBOL);
    Assert.assertEquals("symbol", result);
    
    result = PdfFontUtil.computeBasename(PDType1Font.TIMES_BOLD_ITALIC);
    Assert.assertEquals("times-bolditalic", result);

  }
}
