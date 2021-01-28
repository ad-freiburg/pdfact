package pdfact.core;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import pdfact.core.model.Document;
import pdfact.core.model.Paragraph;
import pdfact.core.model.Position;
import pdfact.core.model.SemanticRole;
import pdfact.core.pipes.PlainPdfActCorePipe;
import pdfact.core.util.exception.PdfActException;

/**
 * A class that tests the extraction results of PdfAct for PDF file "PDF0004".
 */
public class TestPdf0004 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document doc;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    doc = new Document("src/test/resources/pdfs/PDF0004_protokoll_berufungskomission_2019.pdf");
    doc = new PlainPdfActCorePipe().execute(doc);
  }

  /**
   * Returns the text of the <blockNum>-th paragraph on the <pageNum>-th page as a string. 
   * Note that blockNum is 0-based and pageNum is 1-based.
   */
  protected Paragraph getParagraph(int pageNum, int paragraphNum) {
    int prevParagraphPageNum = -1;
    int currentParagraphIndex = -1;
    for (Paragraph paragraph : doc.getParagraphs()) {
      Position pos = paragraph.getFirstPosition();
      if (prevParagraphPageNum != pos.getPageNumber()) {
        currentParagraphIndex = 0;
      }
      if (pos.getPageNumber() == pageNum && currentParagraphIndex == paragraphNum) {
        return paragraph;
      }
      if (pageNum < pos.getPageNumber()) {
        return null;
      }
      prevParagraphPageNum = pos.getPageNumber();
      currentParagraphIndex++;
    }
    return null;
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage2Paragraph0() {
    Paragraph paragraph = getParagraph(2, 0);

    String expectedText = "TOP 2 Ausschreibung";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage02Paragraph02() {
    Paragraph paragraph = getParagraph(2, 2);

    String expectedText = "Der deutsche Ausschreibungstext wird durch den Vorsitzenden "
      + "vorgestellt. Formale Änderungen im Rahmen der Anpassung an den neuesten Standard durch " 
      + "das Rektorat stehen noch aus. Die Ausschreibung wurde in der aktuellen Form bereits Ende "
      + "2017 erstellt.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage02Paragraph06() {
    Paragraph paragraph = getParagraph(2, 6);

    String expectedText = "Die Ausschreibung soll in der Zeit, Forschung & Lehre sowie auf der Job "
      + "Site der IEEE geschaltet werden. Außerdem soll sie in der Stellenbörse der Universität "
      + "erscheinen. Die Ausschreibung soll zum 01.03.2019 erfolgen. Als Bewerbungsfrist werden "
      + "sechs Wochen nach erfolgter Ausschreibung genannt.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage02Paragraph07() {
    Paragraph paragraph = getParagraph(2, 7);

    String expectedText = "Zu Punkt 3: Auswahlkriterien und deren Gewichtung";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage02Paragraph08() {
    Paragraph paragraph = getParagraph(2, 8);

    String expectedText = "Die Kommission einigt sich einstimmig auf den folgenden Kanon von "
      + "Kriterien: "
      + "1. Exzellenz in der Forschung mit Schwerpunkt wie in Ausschreibung genannt "
      + "2. Lehrerfahrung bzw. Lehrbefähigung "
      + "3. Fähigkeit zur Akquise und Leitung grundlagen- und anwendungsorientierter, öffentlich "
      + "und industriell geförderter Projekte "
      + "4. Führungserfahrung, Fähigkeit zu strategischer Planung, Aufbau sowie Leitung größerer "
      + "Gruppen "
      + "5. Kooperationsperspektiven mit Universitäten, Forschungsinstituten und Industrie, "
      + "insbesondere innerhalb der Technischen Fakultät, der Universität Freiburg, sowie mit den "
      + "lokalen Fraunhofer-Instituten " 
      + "Alle Kriterien gleich gewichtet.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage03Paragraph00() {
    Paragraph paragraph = getParagraph(3, 0);

    String expectedText = "TOP 5 Befangenheit";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage03Paragraph01() {
    Paragraph paragraph = getParagraph(3, 1);

    String expectedText = "Zum Zeitpunkt der ersten Sitzung liegt keine Befangenheit vor.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage03Paragraph02() {
    Paragraph paragraph = getParagraph(3, 2);

    String expectedText = "TOP 5 Termine";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
}
