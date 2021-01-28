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
  public void testExtractPage04TextBlock03() {
    Paragraph paragraph = getParagraph(4, 3);

    String expectedText = "T e i l V";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage04TextBlock04() {
    Paragraph paragraph = getParagraph(4, 4);

    String expectedText = "Besondere Verfahrensarten Abschnitt 1 Förmliches Verwaltungsverfahren "
      + "§ 63 Anwendung der Vorschriften über das förmliche Verwaltungsverfahren "
      + "§ 64 Form des Antrags "
      + "§ 65 Mitwirkung von Zeugen und Sachverständigen "
      + "§ 66 Verpflichtung zur Anhörung von Beteiligten "
      + "§ 67 Erfordernis der mündlichen Verhandlung "
      + "§ 68 Verlauf der mündlichen Verhandlung "
      + "§ 69 Entscheidung "
      + "§ 70 Anfechtung der Entscheidung "
      + "§ 71 Besondere Vorschriften für das förmliche Verfahren vor Ausschüssen "
      + "Abschnitt 1a Verfahren über eine einheitliche Stelle "
      + "§ 71a Anwendbarkeit "
      + "§ 71b Verfahren "
      + "§ 71c Informationspflichten "
      + "§ 71d Gegenseitige Unterstützung "
      + "§ 71e Elektronisches Verfahren Abschnitt 2 Planfeststellungsverfahren "
      + "§ 72 Anwendung der Vorschriften über das Planfeststellungsverfahren "
      + "§ 73 Anhörungsverfahren "
      + "§ 74 Planfeststellungsbeschluss, Plangenehmigung "
      + "§ 75 Rechtswirkungen der Planfeststellung "
      + "§ 76 Planänderungen vor Fertigstellung des Vorhabens "
      + "§ 77 Aufhebung des Planfeststellungsbeschlusses "
      + "§ 78 Zusammentreffen mehrerer Vorhaben";
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

    String expectedText = "§ 3a Elektronische Kommunikation";
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
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

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
      + "die Behörde ermöglicht, ist nicht zulässig. Die Schriftform kann auch ersetzt werden "
      + "1. durch unmittelbare Abgabe der Erklärung in einem elektronischen Formular, das von der "
      + "Behörde in einem Eingabegerät oder über öffentlich zugängliche Netze zur Verfügung "
      + "gestellt wird; "
      + "2. bei Anträgen und Anzeigen durch Versendung eines elektronischen Dokuments an die "
      + "Behörde mit der Versandart nach § 5 Absatz 5 des De-Mail-Gesetzes; "
      + "3. bei elektronischen Verwaltungsakten oder sonstigen elektronischen Dokumenten der "
      + "Behörden durch Versendung einer De-Mail-Nachricht nach § 5 Absatz 5 des De-Mail-Gesetzes, "
      + "bei der die Bestätigung des akkreditierten Diensteanbieters die erlassende Behörde als "
      + "Nutzer des De-Mail-Kontos erkennen lässt; "
      + "4. durch sonstige sichere Verfahren, die durch Rechtsverordnung der Bundesregierung mit "
      + "Zustimmung des Bundesrates festgelegt werden, welche den Datenübermittler (Absender der "
      + "Daten) authentifizieren und die Integrität des elektronisch übermittelten Datensatzes "
      + "sowie die Barrierefreiheit gewährleisten; der IT-Planungsrat gibt Empfehlungen zu "
      + "geeigneten Verfahren ab.";
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

    String expectedText = "In den Fällen des Satzes 4 Nummer 1 muss bei einer Eingabe über "
      + "öffentlich zugängliche Netze ein elektronischer Identitätsnachweis nach § 18 des " 
      + "Personalausweisgesetzes, nach § 12 des eID-Karte-Gesetzes oder nach § 78 Absatz 5 des "
      + "Aufenthaltsgesetzes erfolgen.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage16Paragraph03() {
    Paragraph paragraph = getParagraph(16, 3);

    String expectedText = "§ 26 Beweismittel";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage16Paragraph04() {
    Paragraph paragraph = getParagraph(16, 4);

    String expectedText = "(1) Die Behörde bedient sich der Beweismittel, die sie nach "
      + "pflichtgemäßem Ermessen zur Ermittlung des Sachverhalts für erforderlich hält. Sie kann "
      + "insbesondere "
      + "1. Auskünfte jeder Art einholen, "
      + "2. Beteiligte anhören, Zeugen und Sachverständige vernehmen oder die schriftliche oder "
      + "elektronische Äußerung von Beteiligten, Sachverständigen und Zeugen einholen, "
      + "3. Urkunden und Akten beiziehen, "
      + "4. den Augenschein einnehmen.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
  
  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage31Paragraph16() {
    Paragraph paragraph = getParagraph(31, 16);

    String expectedText = "(3) Soll durch die Anzeige, den Antrag oder die Abgabe einer "
      + "Willenserklärung eine Frist in Lauf gesetzt werden, innerhalb deren die zuständige "
      + "Behörde tätig werden muss, stellt die zuständige Behörde eine Empfangsbestätigung aus. "
      + "In der Empfangsbestätigung ist das Datum des Eingangs bei der einheitlichen Stelle "
      + "mitzuteilen und auf die Frist, die Voraussetzungen für den Beginn des Fristlaufs und auf "
      + "eine an den Fristablauf geknüpfte Rechtsfolge sowie auf die verfügbaren Rechtsbehelfe "
      + "hinzuweisen.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
  
  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage31Paragraph17() {
    Paragraph paragraph = getParagraph(31, 17);

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
  public void testExtractPage39Paragraph01() {
    Paragraph paragraph = getParagraph(39, 1);

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
  public void testExtractPage39Paragraph02() {
    Paragraph paragraph = getParagraph(39, 2);

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
