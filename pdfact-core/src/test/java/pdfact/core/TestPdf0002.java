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
 * A class that tests the extraction results of PdfAct for PDF file "PDF0002".
 */
public class TestPdf0002 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document doc;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    doc = new Document("src/test/resources/pdfs/PDF0002_KI_evaluation_combined_search_BBH_2018.pdf");
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
  public void testExtractPage01Paragraph02() {
    Paragraph paragraph = getParagraph(1, 2);

    String expectedText = "A Quality Evaluation of Combined Search on a Knowledge Base and Text";
    SemanticRole expectedRole = SemanticRole.TITLE;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage04Paragraph01() {
    Paragraph paragraph = getParagraph(4, 1);

    String expectedText = "The task remained hard, with the best system achieving an NDCG@R of 37% "
      + "and an R-Precision (P@10 was not reported that year) of 32% even for manually tuned "
      + "queries (and 30% for automatic runs).";
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

    String expectedText = "In 2010, an additional task was added, Entity List Completion (a "
      + "similar task but with an additional set of example result entities given for each query) "
      + "with BTC 2009 as the underlying dataset.2 This is a dataset consisting of 1.14 billion "
      + "triples crawled from the semantic web. The BTC dataset contains the complete DBpedia [9]. "
      + "It turned out that the best performing approaches all boost triples from DBpedia to "
      + "obtain good results. Still, working with the dataset turned out to be difficult, with "
      + "the best systems achieving an R-Precision of 31% (NDCG@R was not reported).";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage04Paragraph03() {
    Paragraph paragraph = getParagraph(4, 3);

    String expectedText = "In the 2011 track [10], another semantic web dataset was used "
      + "(Sindice [11]). However, the number of participating teams was very low, and results "
      + "were disappointing compared to previous years.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

 /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage04Paragraph04() {
    Paragraph paragraph = getParagraph(4, 4);

    String expectedText = "3.2 SemSearch Challenges";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage04Paragraph05() {
    Paragraph paragraph = getParagraph(4, 5);

    String expectedText = "The task in the SemSearch challenges is also referred to as ad-hoc "
      + "object retrieval [12]. The user inputs free-form keyword queries, e.g. Apollo astronauts "
      + "who walked on the moon or movies starring Joe Frazier. Results are ranked lists of "
      + "entities. The benchmarks were run on BTC 2009 as a dataset.";
    SemanticRole expectedRole = SemanticRole.BODY_TEXT;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph02() {
    Paragraph paragraph = getParagraph(8, 2);

    String expectedText = "5 Conclusions and Future Wo";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

    /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph03() {
    Paragraph paragraph = getParagraph(8, 3);

    String expectedText = "We have evaluated the quality of KB+Text search on three benchmarks, "
      + "with very promising results. A detailed error analysis has pointed out the current weak "
      + "spots: missing entities in the knowledge base, missing evidence in the full text, errors "
      + "in the entity recognition, errors in the full parses of the sentences. Promising "
      + "directions for future research are therefore: switch to a richer knowledge base "
      + "(e.g., Freebase), switch to a larger corpus than Wikipedia (e.g., ClueWeb), develop a "
      + "more sophisticated entity recognition, try to determine semantic context without full "
      + "parses.";
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

    String expectedText = "References";
    SemanticRole expectedRole = SemanticRole.HEADING;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testExtractPage08Paragraph05() {
    Paragraph paragraph = getParagraph(8, 5);

    String expectedText = "1. Bast H, Bäurle F, Buchhold B, Haußmann E (2014) Semantic full-text "
    + "search with broccoli. In: SIGIR, ACM, pp 1265–1266";
    SemanticRole expectedRole = SemanticRole.REFERENCE;

    assertEquals(expectedText, paragraph.getText());
    assertEquals(expectedRole, paragraph.getSemanticRole());
  }
}
