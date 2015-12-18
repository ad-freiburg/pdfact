package analyzer;

import java.util.List;

import de.freiburg.iif.text.StringUtils;
import model.PdfDocument;
import model.PdfPage;
import model.PdfTextParagraph;
import rules.AnalyzeRules;

/**
 * The concrete implementation of a Pdf Analyzer. 
 *
 * @author Claudius Korzen
 *
 */
public class PlainPdfAnalyzer implements PdfAnalyzer {
  /**
   * The current paragraph role.
   */
  protected String currentParagraphContext;
  
  @Override
  public void analyze(PdfDocument document) {
    if (document == null) {
      return;
    }
    reset();
    
    analyzePages(document.getPages());
  }
  
  /**
   * Resets the member variables.
   */
  protected void reset() {
    this.currentParagraphContext = null;
  }
  
  /**
   * Analyzes the given list of pages.
   */
  protected void analyzePages(List<PdfPage> pages) {
    if (pages == null) {
      return;
    }
    for (PdfPage page : pages) {
      analyzePage(page);
    }
  }
  
  /**
   * Analyzes the given page.
   */
  protected void analyzePage(PdfPage page) {
    if (page == null) {
      return;
    }  
    analyzeParagraphs(page.getParagraphs());
  }
  
  /**
   * Analyzes the given paragraphs.
   */
  protected void analyzeParagraphs(List<PdfTextParagraph> paragraphs) {
    if (paragraphs == null) {
      return;
    }
    
    for (PdfTextParagraph paragraph : paragraphs) {
      analyzeParagraph(paragraph);
    }
  }
  
  /**
   * Analyzes the given paragraph.
   */
  protected void analyzeParagraph(PdfTextParagraph paragraph) {
    if (paragraph == null) {
      return;
    }
    
    identifyParagraphContext(paragraph);
    identifyParagraphRole(paragraph);
  }
  
  /**
   * Identify the context of a text paragraph.
   */
  protected void identifyParagraphContext(PdfTextParagraph paragraph) {
    if (AnalyzeRules.isHeading(paragraph)) {
      String text = StringUtils.removeWhitespaces(paragraph.getUnicode());
      currentParagraphContext = StringUtils.getLongestSentencePart(text);
    }
    paragraph.setContext(currentParagraphContext);
  }
  
  /**
   * Identify the role of a text paragraph.
   */
  protected void identifyParagraphRole(PdfTextParagraph paragraph) {
    // TODO: Identify the role of a paragraph.
  }
}
