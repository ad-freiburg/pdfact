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
 * A class that tests the extraction results of PdfAct for PDF file "KI_2018.pdf". This file has the
 * special feature that text content is wrapped in so called "PDFormXObject" streams, which need to
 * be parsed separately in order to not miss the text.
 */
public class PdfActExtractionTestKI2018 {
  /**
   * The PDF document to examine in this test.
   */
  protected static Document pdf;

  /**
   * Parses the PDF document.
   */
  @BeforeClass
  public static void setup() throws PdfActException {
    pdf = new PlainPdfActCorePipe().execute(new Document("src/test/resources/KI_2018.pdf"));
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
    Character character = pdf.getPages().get(0).getCharacters().get(101);
    assertEquals("e", character.getText());
    assertEquals(16f, character.getFontFace().getFontSize(), 0.0001f);
    assertEquals("Position(page: Page(1), rect: Rectangle(273.0,672.4,280.1,680.2))",
            character.getPosition().toString());

    character = pdf.getPages().get(0).getCharacters().get(160);
    assertEquals("l", character.getText());
    assertEquals(10f, character.getFontFace().getFontSize(), 0.0001f);
    assertEquals("Position(page: Page(1), rect: Rectangle(204.0,625.3,206.8,632.1))",
            character.getPosition().toString());

    character = pdf.getPages().get(0).getCharacters().get(260);
    assertEquals("l", character.getText());
    assertEquals(8.5f, character.getFontFace().getFontSize(), 0.0001f);
    assertEquals("Position(page: Page(1), rect: Rectangle(102.9,530.4,105.3,536.2))",
            character.getPosition().toString());
  }

  /**
   * Tests the extraction results on the level of text lines.
   */
  @Test
  public void testTextLineExtraction() {
    assertEquals("A Quality Evaluation of Combined Search on a Knowledge Base", getLine(0, 3));
    assertEquals("of subject–predicate–object triples with a common nam-", getLine(0, 12));
    assertEquals("evance to the query. For example, typing broccoli leaves", getLine(0, 47));
    assertEquals("with evidence that broccoli leaves are indeed edible.", getLine(0, 49));
    assertEquals("with edible leaves and native to Europe, which will be our", getLine(0, 61));

    assertEquals("referring to Albert Einstein), we recognize it as that entity.", getLine(2, 1));
    assertEquals("state-of-the art approaches for named entity recognition and", getLine(2, 5));
    assertEquals("(C1) The usable parts of rhubarb are the medicinally used", getLine(2, 18));

    assertEquals("(facts from Wikipedia) and LUBM (an ontology for the", getLine(4, 2));
    assertEquals("then trivially have perfect precision and recall. We have only", getLine(4, 7));
    assertEquals("times and in a user study where users were asked to perform", getLine(4, 12));
    assertEquals("We manually fixed 92 obvious mistakes in the KB (for exam-", getLine(4, 27));

    assertEquals("ties from the English Wikipedia; see [7, Table 10]. There", getLine(7, 5));
    assertEquals("are a few things to note in this comparison. First, TET09", getLine(7, 6));
    assertEquals("above. Third, we created our queries manually, as described", getLine(7, 14));
    assertEquals("analysis has pointed out the current weak spots: missing", getLine(7, 27));
    assertEquals("mine semantic context without full parses.", getLine(7, 34));
  }

  /**
   * Tests the extraction results on the level of words.
   */
  @Test
  public void testWordExtraction() {
    assertEquals("Evaluation", getWord(0, 3, 2));
    assertEquals("Combined", getWord(0, 3, 4));
    assertEquals("Search", getWord(0, 3, 5));
    assertEquals("quality", getWord(0, 9, 3));
    assertEquals("KB+Text", getWord(0, 9, 6));
    assertEquals("subject–predicate–object", getWord(0, 12, 1));
    assertEquals("naming", getWord(0, 12, 6));
    assertEquals("pattern.", getWord(0, 56, 1));
    assertEquals("find", getWord(0, 56, 4));
    assertEquals("native", getWord(0, 56, 9));

    assertEquals("(anaphora)", getWord(2, 2, 3));
    assertEquals("Wikify!", getWord(2, 6, 3));
    // assertEquals("rhubarb", getWord(2, 18, 4));
    assertEquals("identified", getWord(2, 23, 2));
    assertEquals("Rhubarb", getWord(2, 25, 8));
    assertEquals("search-as-you-type", getWord(2, 38, 4));

    assertEquals("NDCG@R", getWord(3, 2, 1));
    assertEquals("37%", getWord(3, 2, 3));
    assertEquals("R-Precision", getWord(3, 2, 6));
    assertEquals("(P@10", getWord(3, 2, 7));
    assertEquals("DBpedia", getWord(3, 10, 3));
    assertEquals("DBpedia", getWord(3, 11, 7));
    assertEquals("difficult,", getWord(3, 13, 4));

    assertEquals("TREC", getWord(7, 4, 0));
    assertEquals("ClueWeb09", getWord(7, 7, 2));
    assertEquals("and", getWord(7, 12, 4));
    assertEquals("best.", getWord(7, 17, 2));
    assertEquals("was", getWord(7, 17, 7));
    assertEquals("approximated", getWord(7, 17, 8));
    assertEquals("missing", getWord(7, 22, 1));
    assertEquals("entities", getWord(7, 22, 2));
    assertEquals("added.", getWord(7, 22, 4));
    assertEquals("promising", getWord(7, 26, 3));
    assertEquals("broccoli.", getWord(7, 42, 3));
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testParagraphExtraction() {
    assertEquals("Künstl Intell (2018) 32:19–26", getParagraph(0));
    assertEquals("DOI 10.1007/s13218-017-0513-9", getParagraph(1));
    assertEquals("TECHNICAL CONTRIBUTION", getParagraph(2));

    assertEquals("A Quality Evaluation of Combined Search on a Knowledge Base and Text",
            getParagraph(3));

    assertEquals("Hannah Bast1 · Björn Buchhold1 · Elmar Haussmann1", getParagraph(4));

    assertEquals("In traditional full-text search, the data consists of text documents. The user "
            + "types a (typically short) list of keywords and gets a list of documents containing some "
            + "or all of these keywords, hopefully ranked by some notion of relevance to the query. "
            + "For example, typing broccoli leaves edible in a web search engine will return lots of "
            + "web pages with evidence that broccoli leaves are indeed edible.", getParagraph(18));

    assertEquals("In KB search, the data is a knowledge base, typically given as a (large) set of "
            + "subject-predicate-object triples. For example, Broccoli is-a plant or Broccoli "
            + "native-to Europe. These triples can be thought of to form a graph of entities "
            + "(the nodes) and relations (the edges), and a language like SPARQL allows to search for "
            + "subgraphs matching a given pattern. For example, find all plants that are native to "
            + "Europe.", getParagraph(19));

    assertEquals("The motivation behind KB+Text search is that many queries of a more \"semantic\" "
            + "nature require the combination of both approaches. For example, consider the query "
            + "plants with edible leaves and native to Europe, which will be our running example in "
            + "this paper. A satisfactory answer for this query requires the combination of two kinds "
            + "of information:", getParagraph(20));

    assertEquals("An efficient index for KB+Text search is described in [4]. This index provides "
            + "exactly the support needed for the system shown in Fig. 1: efficient processing of "
            + "tree-shaped KB+Text queries (without variables for relations), efficient excerpt "
            + "generation, and efficient search-as-you-type suggestions that enable a fully "
            + "interactive incremental query construction.", getParagraph(84));

    assertEquals("We briefly discuss the major benchmarks from the past decade, as well as the "
            + "relatively few systems that explicitly combine full-text search and knowledge base "
            + "search. A comprehensive survey of the broad field of semantic search on text and/or "
            + "knowledge bases is provided in [5].", getParagraph(93));

    assertEquals("In 2010, an additional task was added, Entity List Completion (a similar task "
            + "but with an additional set of example result entities given for each query) with BTC "
            + "2009 as the underlying dataset.2 This is a dataset consisting of 1.14 billion triples "
            + "crawled from the semantic web. The BTC dataset contains the complete DBpedia [9]. It "
            + "turned out that the best performing approaches all boost triples from DBpedia to obtain "
            + "good results. Still, working with the dataset turned out to be difficult, with the best "
            + "systems achieving an R-Precision of 31% (NDCG@R was not reported).",
            getParagraph(103));

    assertEquals("In the 2011 challenge [13], there were 50 queries. The best system achieved a "
            + "P@10 of 35% and a MAP of 28%. The 2011 queries are one of our benchmarks in Sect. 4.",
            getParagraph(109));
  }

  /**
   * Tests the extraction results on the level of semantic roles.
   */
  @Test
  public void testSemanticRolesExtraction() {
    assertEquals(SemanticRole.TITLE, pdf.getParagraphs().get(3).getSemanticRole());
    assertEquals(SemanticRole.BODY_TEXT, pdf.getParagraphs().get(18).getSemanticRole());
    assertEquals(SemanticRole.BODY_TEXT, pdf.getParagraphs().get(20).getSemanticRole());
    assertEquals(SemanticRole.BODY_TEXT, pdf.getParagraphs().get(65).getSemanticRole());
    assertEquals(SemanticRole.BODY_TEXT, pdf.getParagraphs().get(75).getSemanticRole());
    assertEquals(SemanticRole.BODY_TEXT, pdf.getParagraphs().get(103).getSemanticRole());
    assertEquals(SemanticRole.BODY_TEXT, pdf.getParagraphs().get(124).getSemanticRole());
  }
}
