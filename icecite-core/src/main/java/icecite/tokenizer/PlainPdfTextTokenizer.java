package icecite.tokenizer;

import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.utils.collection.CollectionUtils;
import icecite.utils.comparators.MinXComparator;

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

  /**
   * The factory to create instances of PdfTextLineList.
   */
  protected PdfTextLineListFactory textLineListFactory;

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
   * @param textLineListFactory
   *        The factory to create instances of {@link PdfTextLineList}.
   */
  @Inject
  public PlainPdfTextTokenizer(PdfTextBlockTokenizer textBlockTokenizer,
      PdfTextLineTokenizer textLineTokenizer, PdfWordTokenizer wordTokenizer,
      PdfParagraphTokenizer paragraphTokenizer,
      PdfTextLineListFactory textLineListFactory) {
    this.textBlockTokenizer = textBlockTokenizer;
    this.textLineTokenizer = textLineTokenizer;
    this.wordTokenizer = wordTokenizer;
    this.paragraphTokenizer = paragraphTokenizer;
    this.textLineListFactory = textLineListFactory;
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

    // Tokenize the text lines into paragraphs.
    document.setParagraphs(tokenizeIntoParagraphs(document));
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
    // Tokenize the text blocks into text lines.
    PdfTextLineList lines = tokenizeIntoTextLines(document, page, blocks);
    // Tokenize the text lines into words.
    for (PdfTextLine line : lines) {
      // Identify the word in the given text line.
      line.setWords(tokenizeIntoWords(document, page, line));
      // Compose the text of the given text line.
      computeTextOfTextLine(line);
    }

    // Register the blocks and lines to the PDF document.
    document.addTextBlocks(blocks);
    document.addTextLines(lines);

    // Register the blocks and lines to the page.
    page.addTextBlocks(blocks);
    page.addTextLines(lines);
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
  protected PdfTextLineList tokenizeIntoTextLines(PdfDocument doc, PdfPage page,
      List<PdfTextBlock> blocks) {
    // The text lines of all blocks.
    PdfTextLineList textLines = this.textLineListFactory.create();

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
  protected PdfTextLineList tokenizeIntoTextLines(PdfDocument doc, PdfPage page,
      PdfTextBlock block) {
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
  protected PdfWordList tokenizeIntoWords(PdfDocument doc, PdfPage page,
      PdfTextLine line) {
    return this.wordTokenizer.tokenize(doc, page, line.getCharacters());
  }

  /**
   * Tokenizes the given text lines into paragraphs.
   * 
   * @param doc
   *        The PDF document to process.
   * 
   * @return The identified paragraphs.
   */
  protected List<PdfParagraph> tokenizeIntoParagraphs(PdfDocument doc) {
    return this.paragraphTokenizer.tokenize(doc);
  }

  /**
   * Computes the texts of the given line and its included words.
   * 
   * @param line
   *        The line to process.
   */
  protected void computeTextOfTextLine(PdfTextLine line) {
    List<PdfWord> words = line.getWords();

    // Compute the text for each word of the line.
    for (PdfWord word : words) {
      Collections.sort(word.getCharacters(), new MinXComparator());
      word.setText(CollectionUtils.join(word.getCharacters(), ""));
    }

    // Compute the text for the line.
    Collections.sort(words, new MinXComparator());
    line.setText(CollectionUtils.join(words, " "));
  }
}
