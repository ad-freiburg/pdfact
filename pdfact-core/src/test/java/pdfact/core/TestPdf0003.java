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
 * A class that tests the extraction results of PdfAct for PDF file "PDF0003".
 */
public class TestPdf0003 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document doc;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    doc = new Document("src/test/resources/pdfs/PDF0003_niederschrift_senatssitzung_2019.pdf");
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
  public void testExtractPage03Paragraph00() {
    Paragraph paragraph = getParagraph(3, 0);

    String expectedText = "- 3 -";
    SemanticRole expectedRole = SemanticRole.PAGE_HEADER;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage03Paragraph01() {
    Paragraph paragraph = getParagraph(3, 1);

    String expectedText = "TOP 2: Bericht des Rektorats 2.1 Struktur- und Entwicklungsplan 2019-23";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage03Paragraph02() {
    Paragraph paragraph = getParagraph(3, 2);

    String expectedText = "Neben der Darstellung der fachlichen, strukturellen, baulichen und "
      + "finanziellen Entwicklungen und Bedarfe gegenüber dem Land sei der Struktur- und "
      + "Entwicklungsplan (StrEP) eine wichtige Orientierungsgrundlage bei der Umsetzung von " 
      + "strategischen Entscheidungen von gesamtuniversitärer Bedeutung, z. B. bei der weiteren "  
      + "Schärfung des Forschungsprofils im Wettbewerb der Universitäten, der Einführung neuer "  
      + "Studiengänge, der Ausschreibung neu bzw. wieder zu besetzender Professuren oder der "
      + "Zuteilung von Ressourcen.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

 /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage04Paragraph02() {
    Paragraph paragraph = getParagraph(4, 2);

    String expectedText = "Der Rektor betont, dass der Universitätsrat dem "
     + "Hochschulfinanzierungsvertrag zustimmen müsse. Gleichwohl sei allen Beteiligten auch "
     + "klar, dass man bedachtsam agieren sollte. Ziel sei, einen akzeptablen Finanzrahmen für "
     + "die nächsten fünf Jahre zu sichern, der den Universitäten Planungssicherheit gebe.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage07Paragraph15() {
    Paragraph paragraph = getParagraph(7, 15);

    String expectedText = "Die Findungskommission habe die Aufgabe, einen Wahlvorschlag zu "
      + "beschließen. Der Wahlvorschlag bedürfe des Einvernehmens des Wissenschaftsministeriums. "
      + "Auf Verlangen des Universitätsrats oder des Senats (Wahlgremien) können weitere "
      + "Kandidatinnen oder Kandidaten in den Wahlvorschlag aufgenommen werden, sofern das " 
      + "Wissenschaftsministerium dazu das Einvernehmen erteilt.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph01() {
    Paragraph paragraph = getParagraph(8, 1);

    String expectedText = "Der Senat und der Universitätsrat wählen dann in einer gemeinsamen "
      + "öffentlichen Sitzung unter der Leitung des Vorsitzenden des Universitätsrats den Rektor "
      + "bzw. die Rektorin.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph04() {
    Paragraph paragraph = getParagraph(8, 4);

    String expectedText = "Die Gleichstellungsbeauftragte gehört der Kommission mit beratender "
      + "Stimme an.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage09Paragraph01() {
    Paragraph paragraph = getParagraph(9, 1);

    String expectedText = "Diskussion.";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage16Paragraph09() {
    Paragraph paragraph = getParagraph(16, 9);

    String expectedText = "Auf Nachfrage des Rektors wird festgehalten, dass kein Mitglied geheime "
      + "Abstimmung wünscht.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage16Paragraph10() {
    Paragraph paragraph = getParagraph(16, 10);

    String expectedText = "Beschluss:";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
}
