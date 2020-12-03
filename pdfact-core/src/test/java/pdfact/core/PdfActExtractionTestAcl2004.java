package pdfact.core;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import pdfact.core.model.Character;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.pipes.PlainPdfActCorePipe;
import pdfact.core.util.exception.PdfActException;

/**
 * A class that tests the extraction results of PdfAct for PDF file "ACL_2004.pdf".
 */
public class PdfActExtractionTestAcl2004 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document pdf;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    pdf = new PlainPdfActCorePipe().execute(new Document("src/test/resources/ACL_2004.pdf"));
  }

  /**
   * Returns the $lineId-th text line of the $pageId-th page as string.
   */
  protected String getLine(int pageId, int lineId) {
    return pdf.getPages().get(pageId).getTextLines().get(lineId).getText();
  }

  /**
   * Returns the $wordId-th word of the $lineId-th line of the $pageId-th page as string.
   */
  protected String getWord(int pageId, int lineId, int wordId) {
    return pdf.getPages().get(pageId).getTextLines().get(lineId).getWords().get(wordId).getText();
  }

  /**
   * Returns the $paragraphId-th paragraph of the given PDF.
   */
  protected String getParagraph(int paragraphId) {
    return pdf.getParagraphs().get(paragraphId).getText();
  }

  /**
   * Tests the extraction results on the level of characters.
   */
  @Test
  public void testCharactersExtraction() {
    Character character = pdf.getPages().get(0).getCharacters().get(0);
    assertEquals("A", character.getText());
    assertEquals(14.3f, character.getFontFace().getFontSize(), 0.0001f);
    assertEquals("Position(page: Page(1), rect: Rectangle(135.0,701.2,145.4,711.1))",
            character.getPosition().toString());

    character = pdf.getPages().get(0).getCharacters().get(75);
    assertEquals("F", character.getText());
    assertEquals(12f, character.getFontFace().getFontSize(), 0.0001f);
    assertEquals("Position(page: Page(1), rect: Rectangle(170.4,643.8,177.7,651.9))",
            character.getPosition().toString());

    character = pdf.getPages().get(0).getCharacters().get(281);
    assertEquals("W", character.getText());
    assertEquals(10.0f, character.getFontFace().getFontSize(), 0.0001f);
    assertEquals("Position(page: Page(1), rect: Rectangle(91.9,499.7,101.3,506.4))",
            character.getPosition().toString());
  }

  /**
   * Tests the extraction results on the level of text lines.
   */
  @Test
  public void testTextLineExtraction() {
    assertEquals("Accurate Information Extraction from Research Papers", getLine(0, 0));
    assertEquals("using Conditional Random Fields", getLine(0, 1));
    assertEquals("Fuchun Peng", getLine(0, 2));
    assertEquals("Department of Computer Science", getLine(0, 3));
    assertEquals("University of Massachusetts", getLine(0, 4));
    assertEquals("Amherst, MA 01003", getLine(0, 5));
    assertEquals("fuchun@cs.umass.edu", getLine(0, 6));
    assertEquals("Andrew McCallum", getLine(0, 7));
    assertEquals("Department of Computer Science", getLine(0, 8));
    assertEquals("search engines, such as CiteSeer, for both lit-", getLine(0, 14));
    assertEquals("data set, we achieve new state-of-the-art perfor-", getLine(0, 29));
    assertEquals("Research paper search engines, such as CiteSeer", getLine(0, 35));
    assertEquals("author analysis, and citation analysis.", getLine(0, 46));
  }

  /**
   * Tests the extraction results on the level of words.
   */
  @Test
  public void testWordExtraction() {
    assertEquals("Accurate", getWord(0, 0, 0));
    assertEquals("Research", getWord(0, 0, 4));
    assertEquals("fuchun@cs.umass.edu", getWord(0, 6, 0));
    assertEquals("accuracy", getWord(0, 15, 6));
    assertEquals("(CRFs)", getWord(0, 18, 2));
    assertEquals("ory", getWord(0, 21, 0));
    assertEquals("well-understood,", getWord(0, 21, 5));
    assertEquals("nential", getWord(0, 26, 0));
    assertEquals("hyperbolic-L1", getWord(0, 26, 2));
    assertEquals("state-of-the-art", getWord(0, 29, 5));
    assertEquals("performance,", getWord(0, 29, 6));
    assertEquals("success,", getWord(0, 53, 1));
    assertEquals("difficulty", getWord(0, 53, 6));
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testParagraphExtraction() {
    assertEquals("Accurate Information Extraction from Research Papers using Conditional Random "
            + "Fields", getParagraph(0));

    assertEquals("Fuchun Peng Department of Computer Science University of Massachusetts",
            getParagraph(1));

    assertEquals("With the increasing use of research paper search engines, such as CiteSeer, for "
            + "both literature search and hiring decisions, the accuracy of such systems is of "
            + "paramount importance. This paper employs Conditional Random Fields (CRFs) for the "
            + "task of extracting various common fields from the headers and citation of research "
            + "papers. The basic theory of CRFs is becoming well-understood, but best-practices for "
            + "applying them to real-world data requires additional exploration. This paper makes an "
            + "empirical exploration of several factors, including variations on Gaussian, "
            + "exponential and hyperbolic-L1 priors for improved regularization, and several classes "
            + "of features and Markov order. On a standard benchmark data set, we achieve new "
            + "state-of-the-art performance, reducing error in average F1 by 36%, and word error "
            + "rate by 78% in comparison with the previous best SVM results. Accuracy compares even "
            + "more favorably against HMMs.", getParagraph(7));

    assertEquals("Research paper search engines, such as CiteSeer (Lawrence et al., 1999) and Cora "
            + "(McCallum et al., 2000), give researchers tremendous power and convenience in their "
            + "research. They are also becoming increasingly used for recruiting and hiring decisions. "
            + "Thus the information quality of such systems is of significant importance. This quality "
            + "critically depends on an information extraction component that extracts meta-data, such "
            + "as title, author, institution, etc, from paper headers and references, because these "
            + "meta-data are further used in many component applications such as field-based search, "
            + "author analysis, and citation analysis.", getParagraph(9));

    assertEquals("In this paper, we present results on this research paper meta-data extraction "
            + "task using a Conditional Random Field (Lafferty et al., 2001), and explore several "
            + "practical issues in applying CRFs to information extraction in general. The CRF "
            + "approach draws together the advantages of both finite state HMM and discriminative "
            + "SVM techniques by allowing use of arbitrary, dependent features and joint inference "
            + "over entire sequences.", getParagraph(11));

    assertEquals("CRFs have been previously applied to other tasks such as name entity extraction "
            + "(McCallum and Li, 2003), table extraction (Pinto et al., 2003) and shallow parsing "
            + "(Sha and Pereira, 2003). The basic theory of CRFs is now well-understood, but the "
            + "best-practices for applying them to new, real-world data is still in an "
            + "early-exploration phase. Here we explore two key practical issues: (1) regularization, "
            + "with an empirical study of Gaussian (Chen and Rosenfeld, 2000), exponential "
            + "(Goodman, 2003), and hyperbolic-L1 (Pinto et al., 2003) priors; (2) exploration of "
            + "various families of features, including text, lexicons, and layout, as well as "
            + "proposing a method for the beneficial use of zero-count features without incurring "
            + "large memory penalties.", getParagraph(12));
  }

  /**
   * Tests the extraction results on the level of semantic roles.
   */
  @Test
  public void testSemanticRolesExtraction() {
    assertEquals(SemanticRole.TITLE, pdf.getParagraphs().get(0).getSemanticRole());
    assertEquals(SemanticRole.HEADING, pdf.getParagraphs().get(6).getSemanticRole());
    assertEquals(SemanticRole.ABSTRACT, pdf.getParagraphs().get(7).getSemanticRole());
    assertEquals(SemanticRole.HEADING, pdf.getParagraphs().get(8).getSemanticRole());
    assertEquals(SemanticRole.BODY_TEXT, pdf.getParagraphs().get(9).getSemanticRole());
    assertEquals(SemanticRole.BODY_TEXT, pdf.getParagraphs().get(10).getSemanticRole());
  }
}
