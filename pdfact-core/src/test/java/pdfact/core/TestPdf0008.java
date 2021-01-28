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
 * A class that tests the extraction results of PdfAct for PDF file "PDF0007".
 */
public class TestPdf0008 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document doc;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    doc = new Document("src/test/resources/pdfs/PDF0008_dsgvo_2021.pdf");
    doc = new PlainPdfActCorePipe().execute(doc);
  }

  /**
   * Returns the text of the <blockNum>-th paragraph on the <pageNum>-th page as a string. Note that
   * blockNum is 0-based and pageNum is 1-based.
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
  public void testExtractPage03Paragraph08() {
    Paragraph paragraph = getParagraph(3, 8);

    String expectedText = "(18) Diese Verordnung gilt nicht für die Verarbeitung von "
      + "personenbezogenen Daten, die von einer natürlichen Person zur Ausübung ausschließlich "
      + "persönlicher oder familiärer Tätigkeiten und somit ohne Bezug zu einer beruflichen "
      + "oder wirtschaftlichen Tätigkeit vorgenommen wird. Als persönliche oder familiäre "
      + "Tätigkeiten könnte auch das Führen eines Schriftverkehrs oder von "
      + "Anschriftenverzeichnissen oder die Nutzung sozialer Netze und Online-Tätigkeiten im "
      + "Rahmen solcher Tätigkeiten gelten. Diese Verordnung gilt jedoch für die Verantwortlichen "
      + "oder Auftragsverarbeiter, die die Instrumente für die Verarbeitung personenbezogener "
      + "Daten für solche persönlichen oder familiären Tätigkeiten bereitstellen.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage04Paragraph00() {
    Paragraph paragraph = getParagraph(4, 0);

    String expectedText = "L 119/4 DE Amtsblatt der Europäischen Union 4.5.2016";
    SemanticRole expectedRole = SemanticRole.PAGE_HEADER;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage20Paragraph05() {
    Paragraph paragraph = getParagraph(20, 5);

    String expectedText = "(109) Die dem Verantwortlichen oder dem Auftragsverarbeiter "
      + "offenstehende Möglichkeit, auf die von der Kommission oder einer Aufsichtsbehörde " 
      + "festgelegten Standard-Datenschutzklauseln zurückzugreifen, sollte den "
      + "Verantwortlichen oder den Auftragsverarbeiter weder daran hindern, die "
      + "Standard-Datenschutzklauseln auch in umfangreicheren Verträgen, wie zum Beispiel "
      + "Verträgen zwischen dem Auftragsverarbeiter und einem anderen Auftragsverarbeiter, "
      + "zu verwenden, noch ihn daran hindern, ihnen weitere Klauseln oder zusätzliche Garantien "
      + "hinzuzufügen, solange diese weder mittelbar noch unmittelbar im Widerspruch zu den von "
      + "der Kommission oder einer Aufsichtsbehörde erlassenen Standard-Datenschutzklauseln stehen "
      + "oder die Grundrechte und Grundfreiheiten der betroffenen Personen beschneiden. Die "
      + "Verantwortlichen und die Auftragsverarbeiter sollten ermutigt werden, mit vertraglichen "
      + "Verpflichtungen, die die Standard-Schutzklauseln ergänzen, zusätzliche Garantien zu "
      + "bieten.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

   /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage20Paragraph06() {
    Paragraph paragraph = getParagraph(20, 6);

    String expectedText = "(1) Verordnung (EU) Nr. 182/2011 des Europäischen Parlaments und des "
      + "Rates vom 16. Februar 2011 zur Festlegung der allgemeinen Regeln und Grundsätze, nach "
      + "denen die Mitgliedstaaten die Wahrnehmung der Durchführungsbefugnisse durch die "
      + "Kommission kontrollieren (ABl. L 55 vom 28.2.2011, S. 13).";
    SemanticRole expectedRole = SemanticRole.OTHER;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

   /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage21Paragraph00() {
    Paragraph paragraph = getParagraph(21, 0);

    String expectedText = "4.5.2016 DE Amtsblatt der Europäischen Union L 119/21";
    SemanticRole expectedRole = SemanticRole.PAGE_HEADER;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage30Paragraph08() {
    Paragraph paragraph = getParagraph(30, 8);

    String expectedText = "(166) Um die Zielvorgaben dieser Verordnung zu erfüllen, d. h. die "
      + "Grundrechte und Grundfreiheiten natürlicher Personen und insbesondere ihr Recht auf "
      + "Schutz ihrer personenbezogenen Daten zu schützen und den freien Verkehr personenbezogener "
      + "Daten innerhalb der Union zu gewährleisten, sollte der Kommission die Befugnis übertragen "
      + "werden, gemäß Artikel 290 AEUV Rechtsakte zu erlassen. Delegierte Rechtsakte sollten "
      + "insbesondere in Bezug auf die für Zertifizierungsverfahren geltenden Kriterien und "
      + "Anforderungen, die durch standardisierte Bildsymbole darzustellenden Informationen und "
      + "die Verfahren für die Bereitstellung dieser Bildsymbole erlassen werden. Es ist von "
      + "besonderer Bedeutung, dass die Kommission im Zuge ihrer Vorbereitungsarbeit angemessene "
      + "Konsultationen, auch auf der Ebene von Sachverständigen, durchführt. Bei der Vorbereitung "
      + "und Ausarbeitung delegierter Rechtsakte sollte die Kommission gewährleisten, dass die "
      + "einschlägigen Dokumente dem Europäischen Parlament und dem Rat gleichzeitig, rechtzeitig "
      + "und auf angemessene Weise übermittelt werden.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage39Paragraph05() {
    Paragraph paragraph = getParagraph(39, 5);

    String expectedText = "Die Verarbeitung personenbezogener Daten über strafrechtliche "
      + "Verurteilungen und Straftaten oder damit zusammen­hängende Sicherungsmaßregeln aufgrund "
      + "von Artikel 6 Absatz 1 darf nur unter behördlicher Aufsicht vorgenommen werden oder wenn "
      + "dies nach dem Unionsrecht oder dem Recht der Mitgliedstaaten, das geeignete Garantien "
      + "für die Rechte und Freiheiten der betroffenen Personen vorsieht, zulässig ist. Ein "
      + "umfassendes Register der strafrechtlichen Verurteilungen darf nur unter behördlicher "
      + "Aufsicht geführt werden.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
}
