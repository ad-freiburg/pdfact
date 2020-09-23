package pdfact.cli;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pdfact.core.model.Paragraph;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;
import pdfact.core.util.exception.PdfActException;

/**
 * A class that tests the extraction results of PdfAct for PDF file 
 * "ACL_2004.pdf".
 */
public class PdfActExtractionTestACL2004 {
  /**
   * The PDF document to examine in this test.
   */
  protected static PdfDocument pdf;

  /**
   * Parses the PDF document.
   */
  @Before
  public void setup() throws PdfActException {    
    pdf = new PdfAct().parse("src/test/resources/ACL_2004.pdf");
  }

  /**
   * Tests the extraction results on the level of text lines.
   */
  @Test
  public void testTextLineExtraction()  {
    List<TextLine> lines = pdf.getFirstPage().getTextLines();
    
    Assert.assertEquals(
      "Accurate Information Extraction from Research Papers", 
      lines.get(0).getText()
    );

    Assert.assertEquals(
      "using Conditional Random Fields", 
      lines.get(1).getText()
    );

    Assert.assertEquals(
      "Fuchun Peng", 
      lines.get(2).getText()
    );

    Assert.assertEquals(
      "Department of Computer Science", 
      lines.get(3).getText()
    );

    Assert.assertEquals(
      "University of Massachusetts", 
      lines.get(4).getText()
    );

    Assert.assertEquals(
      "Amherst, MA 01003", 
      lines.get(5).getText()
    );

    Assert.assertEquals(
      "fuchun@cs.umass.edu", 
      lines.get(6).getText()
    );

    Assert.assertEquals(
      "Andrew McCallum", 
      lines.get(7).getText()
    );

    Assert.assertEquals(
      "Department of Computer Science", 
      lines.get(8).getText()
    );

    Assert.assertEquals(
      "search engines, such as CiteSeer, for both lit-", 
      lines.get(14).getText()
    );

    Assert.assertEquals(
      "data set, we achieve new state-of-the-art perfor-", 
      lines.get(29).getText()
    );

    Assert.assertEquals(
      "Research paper search engines, such as CiteSeer", 
      lines.get(35).getText()
    );

    Assert.assertEquals(
      "author analysis, and citation analysis.", 
      lines.get(46).getText()
    );
  }

  /**
   * Tests the extraction results on the level of words.
   */
  @Test
  public void testWordExtraction()  {
    List<Word> words0 = pdf.getFirstPage().getTextLines().get(0).getWords();
    Assert.assertEquals("Accurate", words0.get(0).getText());
    Assert.assertEquals("Research", words0.get(4).getText());

    List<Word> words6 = pdf.getFirstPage().getTextLines().get(6).getWords();
    Assert.assertEquals("fuchun@cs.umass.edu", words6.get(0).getText());

    List<Word> words15 = pdf.getFirstPage().getTextLines().get(15).getWords();
    Assert.assertEquals("accuracy", words15.get(6).getText());

    List<Word> words18 = pdf.getFirstPage().getTextLines().get(18).getWords();
    Assert.assertEquals("(CRFs)", words18.get(2).getText());

    List<Word> words21 = pdf.getFirstPage().getTextLines().get(21).getWords();
    Assert.assertEquals("ory", words21.get(0).getText());
    Assert.assertEquals("well-understood,", words21.get(5).getText());

    List<Word> words26 = pdf.getFirstPage().getTextLines().get(26).getWords();
    Assert.assertEquals("nential", words26.get(0).getText());
    Assert.assertEquals("hyperbolic-L1", words26.get(2).getText());

    List<Word> words29 = pdf.getFirstPage().getTextLines().get(29).getWords();
    Assert.assertEquals("state-of-the-art", words29.get(5).getText());
    Assert.assertEquals("performance,", words29.get(6).getText());
    
    List<Word> words53 = pdf.getFirstPage().getTextLines().get(53).getWords();
    Assert.assertEquals("success,", words53.get(1).getText());
    Assert.assertEquals("difﬁculty", words53.get(6).getText());
  }

  /**
   * Tests the extraction results on the level of paragraphs.
   */
  @Test
  public void testParagraphExtraction()  {
    List<Paragraph> paragraphs = pdf.getParagraphs();
    
    Paragraph p0 = paragraphs.get(0);
    Assert.assertEquals("Accurate Information Extraction from Research Papers "
     + "using Conditional Random Fields", p0.getText());

    Paragraph p1 = paragraphs.get(1);
    Assert.assertEquals("Fuchun Peng Department of Computer Science University "
     + "of Massachusetts", p1.getText());

     Paragraph p7 = paragraphs.get(7);
     Assert.assertEquals("With the increasing use of research paper search "
       + "engines, such as CiteSeer, for both literature search and hiring "
       + "decisions, the accuracy of such systems is of paramount importance. "
       + "This paper employs Conditional Random Fields (CRFs) for the task of "
       + "extracting various common ﬁelds from the headers and citation of "
       + "research papers. The basic theory of CRFs is becoming "
       + "well-understood, but best-practices for applying them to real-world "
       + "data requires additional exploration. This paper makes an empirical "
       + "exploration of several factors, including variations on Gaussian, "
       + "exponential and hyperbolic-L1 priors for improved regularization, "
       + "and several classes of features and Markov order. On a standard "
       + "benchmark data set, we achieve new state-of-the-art performance, "
       + "reducing error in average F1 by 36%, and word error rate by 78% in "
       + "comparison with the previous best SVM results. Accuracy compares "
       + "even more favorably against HMMs.", p7.getText());

    Paragraph p9 = paragraphs.get(9);
    Assert.assertEquals("Research paper search engines, such as CiteSeer "
      + "(Lawrence et al., 1999) and Cora (McCallum et al., 2000), give "
      + "researchers tremendous power and convenience in their research. They "
      + "are also becoming increasingly used for recruiting and hiring "
      + "decisions. Thus the information quality of such systems is of "
      + "signiﬁcant importance. This quality critically depends on an "
      + "information extraction component that extracts meta-data, such as "
      + "title, author, institution, etc, from paper headers and references, "
      + "because these meta-data are further used in many component "
      + "applications such as ﬁeld-based search, author analysis, and citation "
      + "analysis.", p9.getText());
   
    Paragraph p11 = paragraphs.get(11);
    Assert.assertEquals("In this paper, we present results on this research "
     + "paper meta-data extraction task using a Conditional Random Field "
     + "(Lafferty et al., 2001), and explore several practical issues in "
     + "applying CRFs to information extraction in general. The CRF approach "
     + "draws together the advantages of both ﬁnite state HMM and "
     + "discriminative SVM techniques by allowing use of arbitrary, dependent "
     + "features and joint inference over entire sequences.", p11.getText());

    Paragraph p12 = paragraphs.get(12);
    Assert.assertEquals("CRFs have been previously applied to other tasks such "
      + "as name entity extraction (McCallum and Li, 2003), table extraction "
      + "(Pinto et al., 2003) and shallow parsing (Sha and Pereira, 2003). The "
      + "basic theory of CRFs is now well-understood, but the best-practices "
      + "for applying them to new, real-world data is still in an "
      + "early-exploration phase. Here we explore two key practical issues: "
      + "(1) regularization, with an empirical study of Gaussian (Chen and "
      + "Rosenfeld, 2000), exponential (Goodman, 2003), and hyperbolic-L1 "
      + "(Pinto et al., 2003) priors; (2) exploration of various families of "
      + "features, including text, lexicons, and layout, as well as proposing "
      + "a method for the beneﬁcial use of zero-count features without "
      + "incurring large memory penalties.", p12.getText());
  }

  /**
   * Tests the extraction results on the level of semantic roles.
   */
  @Test
  public void testSemanticRolesExtraction()  {
    Assert.assertEquals(
      SemanticRole.TITLE, 
      pdf.getParagraphs().get(0).getSemanticRole()
    );

    Assert.assertEquals(
      SemanticRole.HEADING, 
      pdf.getParagraphs().get(6).getSemanticRole()
    );
    
    Assert.assertEquals(
      SemanticRole.ABSTRACT, 
      pdf.getParagraphs().get(7).getSemanticRole()
    );

    Assert.assertEquals(
      SemanticRole.HEADING, 
      pdf.getParagraphs().get(8).getSemanticRole()
    );

    Assert.assertEquals(
      SemanticRole.BODY_TEXT, 
      pdf.getParagraphs().get(9).getSemanticRole()
    );

    Assert.assertEquals(
      SemanticRole.BODY_TEXT, 
      pdf.getParagraphs().get(10).getSemanticRole()
    );
  }
}