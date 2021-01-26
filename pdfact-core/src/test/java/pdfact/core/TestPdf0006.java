package pdfact.core;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import pdfact.core.model.Document;
import pdfact.core.model.Paragraph;
import pdfact.core.model.Position;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.pipes.PlainPdfActCorePipe;
import pdfact.core.util.exception.PdfActException;

/**
 * A class that tests the extraction results of PdfAct for PDF file "PDF0006".
 */
public class TestPdf0006 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document doc;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    doc = new Document("src/test/resources/pdfs/PDF0006_verwaltungsverfahrensgesetz_2019.pdf");
    doc = new PlainPdfActCorePipe().execute(doc);
  }

  /**
   * Returns the <blockNum>-th text block on the <pageNum>-th page. Note that blockNum is
   * 0-based and pageNum is 1-based.
   */
  protected TextBlock getTextBlock(int pageNum, int blockNum) {
    return doc.getPages().get(pageNum - 1).getTextBlocks().get(blockNum);
  }

  /**
   * Returns the <paragraphNum>-th paragraph on the <pageNum>-th page. Note that paragraphNum is
   * 0-based and pageNum is 1-based.
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
   * The PDF contains non-breaking whitespaces. Make sure that they are not extracted.
   */
  @Test
  public void testExtractPage02TextBlock02() {
    TextBlock block = getTextBlock(2, 2);

    String expectedText = "§ 8 Kosten der Amtshilfe";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, block.getText());
    assertEquals(expectedRole, block.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage02Paragraph02() {
    Paragraph paragraph = getParagraph(2, 2);

    String expectedText = "§8 Kosten der Amtshilfe";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph00() {
    Paragraph paragraph = getParagraph(8, 0);

    String expectedText = "Ein Service des Bundesministeriums der Justiz und für Verbraucherschutz "
      + "sowie des Bundesamts für Justiz ‒ www.gesetze-im-internet.de";
    SemanticRole expectedRole = SemanticRole.PAGE_HEADER;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph01() {
    Paragraph paragraph = getParagraph(8, 1);

    String expectedText = "§ 3a Elektronische Kommunikation";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph02() {
    Paragraph paragraph = getParagraph(8, 2);

    String expectedText = "(1) Die Übermittlung elektronischer Dokumente ist zulässig, soweit der "
      + "Empfänger hierfür einen Zugang eröffnet.";
    SemanticRole expectedRole = SemanticRole.ITEMIZE_ITEM;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph03() {
    Paragraph paragraph = getParagraph(8, 3);

    String expectedText = "(2) Eine durch Rechtsvorschrift angeordnete Schriftform kann, soweit "
      + "nicht durch Rechtsvorschrift etwas anderes bestimmt ist, durch die elektronische Form "
      + "ersetzt werden. Der elektronischen Form genügt ein elektronisches Dokument, das mit einer "
      + "qualifizierten elektronischen Signatur versehen ist. Die Signierung mit einem Pseudonym, "
      + "das die Identifizierung der Person des Signaturschlüsselinhabers nicht unmittelbar durch "
      + "die Behörde ermöglicht, ist nicht zulässig. Die Schriftform kann auch ersetzt werden";
    SemanticRole expectedRole = SemanticRole.ITEMIZE_ITEM;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

/**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph04() {
    Paragraph paragraph = getParagraph(8, 4);

    String expectedText = "1. durch unmittelbare Abgabe der Erklärung in einem elektronischen "
     + "Formular, das von der Behörde in einem Eingabegerät oder über öffentlich zugängliche Netze "
     + "zur Verfügung gestellt wird;";
    SemanticRole expectedRole = SemanticRole.ITEMIZE_ITEM;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage31Paragraph13() {
    Paragraph paragraph = getParagraph(31, 13);

    String expectedText = "(3) Soll durch die Anzeige, den Antrag oder die Abgabe einer "
      + "Willenserklärung eine Frist in Lauf gesetzt werden, innerhalb deren die zuständige "
      + "Behörde tätig werden muss, stellt die zuständige Behörde eine Empfangsbestätigung aus. "
      + "In der Empfangsbestätigung ist das Datum des Eingangs bei der einheitlichen Stelle "
      + "mitzuteilen und auf die Frist, die Voraussetzungen für den Beginn des Fristlaufs und auf "
      + "eine an den Fristablauf geknüpfte Rechtsfolge sowie auf die verfügbaren Rechtsbehelfe "
      + "hinzuweisen.";
    SemanticRole expectedRole = SemanticRole.ITEMIZE_ITEM;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
  
  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage31Paragraph14() {
    Paragraph paragraph = getParagraph(31, 14);

    String expectedText = "- Seite 31 von 40 -";
    SemanticRole expectedRole = SemanticRole.PAGE_FOOTER;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage38Paragraph01() {
    Paragraph paragraph = getParagraph(38, 1);

    String expectedText = "§ 86 Abberufung";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage39Paragraph05() {
    Paragraph paragraph = getParagraph(39, 5);

    String expectedText = "Die Niederschrift ist von dem Vorsitzenden und, soweit ein "
      + "Schriftführer hinzugezogen worden ist, auch von diesem zu unterzeichnen.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage39Paragraph06() {
    Paragraph paragraph = getParagraph(39, 6);

    String expectedText = "Teil VIII Schlussvorschriften";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage40Paragraph01() {
    Paragraph paragraph = getParagraph(40, 1);

    String expectedText = "Die Senate der Länder Berlin, Bremen und Hamburg werden ermächtigt, "
      + "die örtliche Zuständigkeit abweichend von § 3 dem besonderen Verwaltungsaufbau ihrer "
      + "Länder entsprechend zu regeln.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
}
