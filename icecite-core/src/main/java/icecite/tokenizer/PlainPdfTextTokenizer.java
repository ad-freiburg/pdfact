package icecite.tokenizer;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;

/**
 * An implementation of {@link PdfTextTokenizer} using XYCut.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextTokenizer implements PdfTextTokenizer {
  /**
   * The text block tokenizer.
   */
  protected PdfTextBlockTokenizer textBlockTokenizer;

  /**
   * The text line tokenizer.
   */
  protected PdfTextLineTokenizer textLineTokenizer;

  /**
   * The word tokenizer.
   */
  protected PdfWordTokenizer wordTokenizer;

  /**
   * The paragraph tokenizer.
   */
  protected PdfParagraphTokenizer paragraphTokenizer;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new text tokenizer.
   * 
   * @param textBlockTokenizer
   *        The tokenizer to identify text blocks.
   * @param textLineTokenizer
   *        The tokenizer to identify text lines.
   * @param wordTokenizer
   *        The tokenizer to identify words.
   * @param paragraphTokenizer
   *        The tokenizer to identify paragraphs.
   */
  @Inject
  public PlainPdfTextTokenizer(PdfTextBlockTokenizer textBlockTokenizer,
      PdfTextLineTokenizer textLineTokenizer, PdfWordTokenizer wordTokenizer,
      PdfParagraphTokenizer paragraphTokenizer) {
    this.textBlockTokenizer = textBlockTokenizer;
    this.textLineTokenizer = textLineTokenizer;
    this.wordTokenizer = wordTokenizer;
    this.paragraphTokenizer = paragraphTokenizer;
  }

  // ==========================================================================

  @Override
  public void tokenizePdfDocument(PdfDocument document) {
    if (document == null) {
      return;
    }

    List<PdfPage> pages = document.getPages();
    if (pages == null) {
      return;
    }

    // Tokenize each page separately.
    for (PdfPage page : pages) {
      tokenizePdfPage(document, page);
    }
  }

  // ==========================================================================

  /**
   * Tokenizes the given page into text blocks, words, text lines and
   * paragraphs in the given page.
   * 
   * @param document
   *        The PDF document to which the PDF page belongs to.
   * @param page
   *        The page to process.
   */
  protected void tokenizePdfPage(PdfDocument document, PdfPage page) {
    // Tokenize the given page into text blocks.
    List<PdfTextBlock> blocks = tokenizeIntoTextBlocks(document, page);
    page.setTextBlocks(blocks);
    // // Tokenize the text blocks into text lines.
    // List<PdfTextLine> lines = tokenizeIntoTextLines(document, page, blocks);
    // // Tokenize the text lines into words.
    // for (PdfTextLine line : lines) {
    // line.setWords(tokenizeIntoWords(document, page, line));
    // }
    // // Tokenize the text lines into paragraphs.
    // page.setParagraphs(tokenizeIntoParagraphs(document, page, lines));
  }

  /**
   * Tokenizes the given page into text blocks.
   * 
   * @param doc
   *        The PDF document to which the page belongs to.
   * @param page
   *        The page to tokenize.
   * 
   * @return The list of identified text blocks.
   */
  protected List<PdfTextBlock> tokenizeIntoTextBlocks(PdfDocument doc,
      PdfPage page) {
    return this.textBlockTokenizer.tokenize(doc, page, page.getCharacters());
  }

  /**
   * Tokenizes the the given list of text blocks into text lines.
   * 
   * @param doc
   *        The PDF document to which the text blocks belong to.
   * @param page
   *        The page in which the text blocks are located.
   * @param blocks
   *        The list of text blocks to tokenize.
   * 
   * @return The list of identified text lines from all text blocks.
   */
  protected List<PdfTextLine> tokenizeIntoTextLines(PdfDocument doc,
      PdfPage page, List<PdfTextBlock> blocks) {
    // The text lines of all blocks.
    List<PdfTextLine> textLines = new ArrayList<>();

    if (blocks != null) {
      // Iterate through the blocks and identify lines in each single block.
      for (PdfTextBlock block : blocks) {
        List<PdfTextLine> lines = tokenizeIntoTextLines(doc, page, block);
        if (lines != null) {
          textLines.addAll(lines);
        }
      }
    }

    return textLines;
  }

  /**
   * Tokenizes the given text block into text lines.
   * 
   * @param doc
   *        The PDF document to which the text blocks belong to.
   * @param page
   *        The page in which the text blocks are located. 
   * @param block
   *        The text block to tokenize.
   * 
   * @return The list of identified text lines.
   */
  protected List<PdfTextLine> tokenizeIntoTextLines(PdfDocument doc, 
      PdfPage page, PdfTextBlock block) {
    return this.textLineTokenizer.tokenize(doc, page, block.getCharacters());
  }

  /**
   * Tokenizes the given text line into words.
   * 
   * @param doc
   *        The PDF document to which the text blocks belong to.
   * @param page
   *        The page in which the text blocks are located. 
   * @param line
   *        The text line to tokenize.
   * @return The list of identified words.
   */
  protected List<PdfWord> tokenizeIntoWords(PdfDocument doc, 
      PdfPage page, PdfTextLine line) {
    return this.wordTokenizer.tokenize(doc, page, line.getCharacters());
  }

  /**
   * Tokenizes the given text lines into paragraphs.
   * 
   * @param doc
   *        The PDF document to which the text blocks belong to.
   * @param page
   *        The page in which the text blocks are located. 
   * @param lines
   *        The text lines to process.
   * @return The identified paragraphs.
   */
  protected List<PdfParagraph> tokenizeIntoParagraphs(PdfDocument doc, 
      PdfPage page, List<PdfTextLine> lines) {
    return this.paragraphTokenizer.tokenize(doc, page, lines);
  }
}
