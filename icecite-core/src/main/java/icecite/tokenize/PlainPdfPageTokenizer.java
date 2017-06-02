package icecite.tokenize;

import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.utils.collection.CollectionUtils;
import icecite.utils.comparators.MinXComparator;
import icecite.utils.comparators.MinYComparator;

/**
 * A plain implementation of {@link PdfPageTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfPageTokenizer implements PdfPageTokenizer {
  /**
   * The text area tokenizer.
   */
  protected PdfTextAreaTokenizer textAreaTokenizer;

  /**
   * The text line tokenizer.
   */
  protected PdfTextLineTokenizer textLineTokenizer;

  /**
   * The word tokenizer.
   */
  protected PdfWordTokenizer wordTokenizer;

  /**
   * The text block tokenizer.
   */
  protected PdfTextBlockTokenizer textBlockTokenizer;

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
  public PlainPdfPageTokenizer(PdfTextAreaTokenizer textBlockTokenizer,
      PdfTextLineTokenizer textLineTokenizer, PdfWordTokenizer wordTokenizer,
      PdfTextBlockTokenizer paragraphTokenizer) {
    this.textAreaTokenizer = textBlockTokenizer;
    this.textLineTokenizer = textLineTokenizer;
    this.wordTokenizer = wordTokenizer;
    this.textBlockTokenizer = paragraphTokenizer;
  }

  // ==========================================================================

  @Override
  public void tokenizePdfPages(PdfDocument document) {
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
   * Tokenizes the given page into text areas, text lines, words and text
   * blocks.
   * 
   * @param pdf
   *        The PDF document to which the PDF page belongs to.
   * @param page
   *        The page to process.
   */
  protected void tokenizePdfPage(PdfDocument pdf, PdfPage page) {
    // Tokenize the given page into (loose) text areas.
    List<PdfCharacterList> areas = tokenizeIntoTextAreas(pdf, page);
    // Tokenize the text area into text lines.
    for (PdfCharacterList area : areas) {
      PdfTextLineList lines = tokenizeIntoTextLines(pdf, page, area);
      // Tokenize the text lines into words.
      for (PdfTextLine line : lines) {
        // Identify the word in the given text line.
        line.setWords(tokenizeIntoWords(pdf, page, line));

        // Register the line to the page *and* the PDF document.
        page.addTextLine(line);
        pdf.addTextLine(line);
      }
    }

    // Compute the texts for text blocks, text lines and words.
    computeTexts2(page);
    
    // Tokenize the page into text blocks.
    page.setTextBlocks(tokenizeIntoTextBlocks(pdf, page));

    // Compute the texts for text blocks, text lines and words.
    computeTexts(page);
  }

  /**
   * Tokenizes the given page into (loose) text areas.
   * 
   * @param doc
   *        The PDF document to which the page belongs to.
   * @param page
   *        The page to tokenize.
   * 
   * @return The list of identified text areas.
   */
  protected List<PdfCharacterList> tokenizeIntoTextAreas(PdfDocument doc,
      PdfPage page) {
    return this.textAreaTokenizer.tokenize(doc, page);
  }

  /**
   * Tokenizes the given text area into text lines.
   * 
   * @param doc
   *        The PDF document to which the text blocks belong to.
   * @param page
   *        The page in which the text blocks are located.
   * @param area
   *        The text area to tokenize.
   * 
   * @return The list of identified text lines.
   */
  protected PdfTextLineList tokenizeIntoTextLines(PdfDocument doc, PdfPage page,
      PdfCharacterList area) {
    return this.textLineTokenizer.tokenize(doc, page, area);
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
   *
   * @return The list of identified words.
   */
  protected PdfWordList tokenizeIntoWords(PdfDocument doc, PdfPage page,
      PdfTextLine line) {
    return this.wordTokenizer.tokenize(doc, page, line);
  }

  /**
   * Tokenizes the given page into text blocks.
   * 
   * @param doc
   *        The PDF document to process.
   * @param page
   *        The page to process.
   * 
   * @return The identified paragraphs.
   */
  protected List<PdfTextBlock> tokenizeIntoTextBlocks(PdfDocument doc,
      PdfPage page) {
    return this.textBlockTokenizer.tokenize(doc, page);
  }

  /**
   * Computes the texts of the text blocks, text lines and words of the given
   * page.
   * 
   * @param page
   *        The page to process.
   */
  protected void computeTexts(PdfPage page) {
    if (page == null) {
      return;
    }

    List<PdfTextBlock> textBlocks = page.getTextBlocks();
    if (textBlocks == null) {
      return;
    }

    for (PdfTextBlock block : textBlocks) {
      PdfTextLineList lines = block.getTextLines();
      if (lines == null) {
        continue;
      }

      for (PdfTextLine line : lines) {
        PdfWordList words = line.getWords();
        if (words == null) {
          continue;
        }

        for (PdfWord word : words) {
          // Compute the text for the word.
          Collections.sort(word.getCharacters(), new MinXComparator());
          word.setText(CollectionUtils.join(word.getCharacters(), ""));
        }

        // Compute the text for the line.
        Collections.sort(words, new MinXComparator());
        line.setText(CollectionUtils.join(words, " "));
      }

      // Compute the text for the block.
      Collections.sort(lines, Collections.reverseOrder(new MinYComparator()));
      block.setText(CollectionUtils.join(lines, " "));
    }
  }

  /**
   * Computes the texts of the text blocks, text lines and words of the given
   * page.
   * 
   * @param page
   *        The page to process.
   */
  protected void computeTexts2(PdfPage page) {
    if (page == null) {
      return;
    }

    List<PdfTextBlock> textBlocks = page.getTextBlocks();
    if (textBlocks == null) {
      return;
    }

    for (PdfTextLine line : page.getTextLines()) {
      PdfWordList words = line.getWords();
      if (words == null) {
        continue;
      }

      for (PdfWord word : words) {
        // Compute the text for the word.
        Collections.sort(word.getCharacters(), new MinXComparator());
        word.setText(CollectionUtils.join(word.getCharacters(), ""));
      }

      // Compute the text for the line.
      Collections.sort(words, new MinXComparator());
      line.setText(CollectionUtils.join(words, " "));
    }
  }
}
