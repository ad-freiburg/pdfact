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
 * A class that tests the extraction results of PdfAct for PDF file "PDF0005".
 */
public class TestPdf0005 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document doc;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    doc = new Document("src/test/resources/pdfs/PDF0005_landeshochschulgesetz_2005.pdf");
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
  public void testExtractPage05Paragraph02() {
    Paragraph paragraph = getParagraph(5, 2);

    String expectedText = "Die Hochschulen können durch Regelung in der Grundordnung ihrem Namen "
            + "nach Satz 1 geeignete Zusätze voranstellen oder anfügen.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph03() {
    Paragraph paragraph = getParagraph(5, 3);

    String expectedText = "(3) Hochschulen in freier Trägerschaft sind die kirchlichen und "
      + "sonstigen nicht staatlichen Einrichtungen des Bildungswesens, die nach Maßgabe dieses "
      + "Gesetzes staatlich anerkannt sind. Unberührt bleiben die kirchlichen Hochschulen im Sinne "
      + "von Artikel 9 der Verfassung des Landes Baden-Württemberg.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph04() {
    Paragraph paragraph = getParagraph(5, 4);

    String expectedText = "(4) Staatliche Hochschulen, ausgenommen die Hochschulen nach § 69, "
      + "werden durch Gesetz errichtet, zusammengelegt oder aufgehoben. Studienakademien der "
      + "Dualen Hochschule werden durch Rechtsverordnung des Wissenschaftsministeriums errichtet, "
      + "zusammengelegt oder aufgehoben. Die Errichtung, Änderung oder Aufhebung von Außenstellen "
      + "bedürfen eines Beschlusses der Landesregierung.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph05() {
    Paragraph paragraph = getParagraph(5, 5);

    String expectedText = "§ 2 Aufgaben";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph06() {
    Paragraph paragraph = getParagraph(5, 6);

    String expectedText = "(1) Die Hochschulen dienen entsprechend ihrer Aufgabenstellung der "
      + "Pflege und der Entwicklung der Wissenschaften und der Künste durch Forschung, Lehre, "
      + "Studium und Weiterbildung in einem freiheitlichen, demokratischen und sozialen Rechtsstaat. "
      + "Die Hochschulen bereiten auf berufliche Tätigkeiten vor, welche die Anwendung "
      + "wissenschaftlicher Erkenntnisse und wissenschaftlicher Methoden oder die Fähigkeit zu "
      + "künstlerischer Gestaltung erfordern. Hierzu tragen die Hochschulen entsprechend ihrer "
      + "besonderen Aufgabenstellung wie folgt bei:";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage14Paragraph09() {
    Paragraph paragraph = getParagraph(14, 9);

    String expectedText = "(2) Für Amtspflichtverletzungen der in Absatz 1 genannten Beschäftigten "
      + "trifft die Verantwortlichkeit die Hochschule. Ansprüche auf Schadensersatz und Rückgriff "
      + "nach § 48 BeamtStG und § 59 LBG gegen Beamtinnen und Beamte stehen dem Land zu, wenn "
      + "diese Aufgaben im Rahmen des § 8 Absatz 1 Satz 2 und Absatz 2 wahrgenommen haben. "
      + "Ansprüche der Hochschule gegen Organe und Mitglieder von Organen werden im Namen der "
      + "Hochschule vom Wissenschaftsministerium geltend gemacht.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage14Paragraph10() {
    Paragraph paragraph = getParagraph(14, 10);

    String expectedText = "- Seite 14 von 94 -";
    SemanticRole expectedRole = SemanticRole.PAGE_FOOTER;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage17Paragraph00() {
    Paragraph paragraph = getParagraph(17, 0);

    String expectedText = "(5) Sachen und Rechte, die allein oder überwiegend mit Mitteln des "
      + "Staatshaushaltsplans erworben werden, gehen in das Eigentum des Landes über.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage18Paragraph00() {
    Paragraph paragraph = getParagraph(18, 0);

    String expectedText = "(2) Die Hochschulen dürfen im Rahmen der Aufgaben nach § 2 ungeachtet "
      + "der Rechtsform privatrechtliche Unternehmen nur errichten, übernehmen, wesentlich "
      + "erweitern oder sich daran beteiligen, wenn";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage18Paragraph01() {
    Paragraph paragraph = getParagraph(18, 1);

    String expectedText = "1. die Aufgaben der Hochschulen, die das Unternehmen wahrnehmen soll, "
      + "nicht ebenso gut und wirtschaftlich von der Hochschule als eigene Aufgabe im Sinne des "
      + "Absatzes 1 erfüllt werden können,";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage18Paragraph02() {
    Paragraph paragraph = getParagraph(18, 2);

    String expectedText = "2. das Unternehmen nach Art und Umfang in einem angemessenen Verhältnis "
      + " zur Leistungsfähigkeit der Hochschule und zum voraussichtlichen Bedarf steht,";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage83Paragraph03() {
    Paragraph paragraph = getParagraph(83, 3);

    String expectedText = "(9) Die Organisationssatzung der Studierendenschaft soll die "
      + "Einrichtung einer Schlichtungskommission vorsehen. Die Schlichtungskommission kann von " 
      + "jeder oder jedem Studierenden der Hochschule mit der Behauptung angerufen werden, die "
      + "Studierendenschaft habe in einem konkreten Einzelfall ihre Aufgaben nach § 65 Absätze 2 "
      + "bis 4 überschritten. Einzelheiten der Schlichtungskommission einschließlich ihrer "
      + "Besetzung regelt die Organisationssatzung der Studierendenschaft.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  
}
