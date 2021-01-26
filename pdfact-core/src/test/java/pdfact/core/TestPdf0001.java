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
 * A class that tests the extraction results of PdfAct for PDF file "PDF0001".
 */
public class TestPdf0001 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document doc;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    doc = new Document("src/test/resources/pdfs/PDF0001_ACL_crfs_PM_2004.pdf");
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
  public void testExtractPage02Paragraph00() {
    Paragraph paragraph = getParagraph(2, 0);

    String expectedText = "We describe a large collection of experimental results on two "
      + "traditional benchmark data sets. Dramatic improvements are obtained in comparison with "
      + "previous SVM and HMM based results.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage02Paragraph01() {
    Paragraph paragraph = getParagraph(2, 1);

    String expectedText = "2 Conditional Random Fields";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph00() {
    Paragraph paragraph = getParagraph(5, 0);

    String expectedText = "3.2.1 Paper header dataset";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph01() {
    Paragraph paragraph = getParagraph(5, 1);

    String expectedText = "The header of a research paper is defined to be all of the words from "
      + "the beginning of the paper up to either the first section of the paper, usually the " 
      + "introduction, or to the end of the first page, whichever occurs first. It contains 15 "
      + "fields to be extracted: title, author, affiliation, address, note, email, date, abstract, "
      + "introduction, phone, keywords, web, degree, publication number, and page (Seymore et al., "
      + "1999). The header dataset contains 935 headers. Following previous research (Seymore et "
      + "al., 1999; McCallum et al., 2000; Han et al., 2003), for each trial we randomly select "
      + "500 for training and the remaining 435 for testing. We refer this dataset as H.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph02() {
    Paragraph paragraph = getParagraph(5, 2);

    String expectedText = "3.2.2 Paper reference dataset";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph03() {
    Paragraph paragraph = getParagraph(5, 3);

    String expectedText = "The reference dataset was created by the Cora project (McCallum et al., "
      + "2000). It contains 500 references, we use 350 for training and the rest 150 for testing. "
      + "References contain 13 fields: author, title, editor, booktitle, date, journal, volume, "
      + "tech, institution, pages, location, publisher, note. We refer this dataset as R.";
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

    String expectedText = "3.3 Performance Measures";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph05() {
    Paragraph paragraph = getParagraph(5, 5);

    String expectedText = "To give a comprehensive evaluation, we measure performance using "
      + "several different metrics. In addition to the previously-used word accuracy measure "
      + "(which over-emphasizes accuracy of the abstract field), we use per-field F1 measure (both "
      + "for individual fields and averaged over all fields—called a \"macro average\" in the " 
      + "information retrieval literature), and whole instance accuracy for measuring overall "
      + "performance in a way that is sensitive to even a single error in any part of header or "
      + "citation.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph06() {
    Paragraph paragraph = getParagraph(5, 6);

    String expectedText = "3.3.1 Measuring field-specific performance";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage05Paragraph10() {
    Paragraph paragraph = getParagraph(5, 10);

    String expectedText = "1. Overall word accuracy: Overall word accuracy is the percentage of "  
      + "words whose predicted labels equal their true labels. Word accuracy favors fields with "
      + "large number of words, such as the abstract.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
}
