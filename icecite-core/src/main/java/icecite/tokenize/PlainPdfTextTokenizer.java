package icecite.tokenize;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterStatistician;
import icecite.models.PdfCharacterStatistics;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.tokenize.blocks.PdfTextBlockTokenizer;

/**
 * A plain implementation of {@link PdfTextTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextTokenizer implements PdfTextTokenizer {
  /**
   * The statistician to compute statistics about characters.
   */
  protected PdfCharacterStatistician characterStatistician;
  
  /**
   * The text block identifier.
   */
  protected PdfTextBlockTokenizer textBlockIdentifier;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new text tokenizer.
   *
   * @param characterStatistican
   *        The statistician to compute statistics about characters.
   * @param textBlockIdentifier
   *        The identifier to identify text blocks.
   */
  @Inject
  public PlainPdfTextTokenizer(PdfCharacterStatistician characterStatistican,
      PdfTextBlockTokenizer textBlockIdentifier) {
    this.characterStatistician = characterStatistican;
    this.textBlockIdentifier = textBlockIdentifier;
  }

  // ==========================================================================

  @Override
  public void tokenize(PdfDocument pdf) {
    computeCharacterStatistics(pdf);
    tokenizePdfDocument(pdf);
  }

  // ==========================================================================

  /**
   * Computes the statistics about characters in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void computeCharacterStatistics(PdfDocument pdf) {
    List<PdfCharacterStatistics> pageStats = new ArrayList<>();
    for (PdfPage page : pdf.getPages()) {
      // Compute the character statistics per page.
      PdfCharacterList chars = page.getCharacters();
      PdfCharacterStatistics stats = this.characterStatistician.compute(chars);
      // Add the statistics to the page and the list of statistics.
      page.setCharacterStatistics(stats);
      pageStats.add(stats);
    }
    // Compute the character statistics for the whole PDF document.
    pdf.setCharacterStatistics(this.characterStatistician.aggregate(pageStats));
  }

  // ==========================================================================

  /**
   * Tokenizes the given PDF document into text areas, text lines, words and
   * text blocks.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void tokenizePdfDocument(PdfDocument pdf) {
    // Tokenize each page separately.
    for (PdfPage page : pdf.getPages()) {
      page.setTextBlocks(this.textBlockIdentifier.tokenize(pdf, page));
    }
  }

//  /**
//   * Tokenizes the given PDF page.
//   * 
//   * @param pdf
//   *        The PDF document to which the PDF page belongs to.
//   * @param page
//   *        The PDF page to process.
//   */
//  protected void tokenizePdfPage(PdfDocument pdf, PdfPage page) {
//    page.setTextBlocks(identifyTextBlocks(pdf, page));
//  }
//
//  /**
//   * Identifies the text blocks in the given PDF page.
//   * 
//   * @param pdf
//   *        The PDF document to which the PDF page belongs to.
//   * @param page
//   *        The PDF page to process.
//   * 
//   * @return The list of identified text blocks.
//   */
//  protected List<PdfTextBlock> identifyTextBlocks(PdfDocument pdf,
//      PdfPage page) {
//    // Identify the text lines in the given page.
//    PdfTextLineList lines = identifyTextLines(pdf, page);
//    // Build the text blocks from the text lines.
//    return this.textBlockTokenizer.xxx(pdf, page, lines);
//  }
//
//  /**
//   * Identifies the text lines in the given PDF page.
//   * 
//   * @param pdf
//   *        The PDF document to which the PDF page belongs to.
//   * @param page
//   *        The PDF page to process.
//   * 
//   * @return The list of identified text lines.
//   */
//  protected PdfTextLineList identifyTextLines(PdfDocument pdf, PdfPage page) {
//    // Tokenize the given page into (loose) text areas.
//    List<PdfCharacterList> areas = splitIntoTextAreas(pdf, page);
//    // Tokenize the text areas into text lines.
//    for (PdfCharacterList area : areas) {
//      List<PdfCharacterList> lineChars = splitIntoTextLines(pdf, page, area);
//      PdfTextLineList lines = buildLines(lineChars);
//      // Tokenize the text lines into words.
//      for (PdfCharacterList line : lineChars) {
//        // Identify the word in the given text line.
//        List<PdfCharacterList> wordChars = splitIntoWords(pdf, page, line);
//        List<PdfWord> words = buildWords(pdf, page, wordChars);
//      }
//    }
//  }
//
//  // ==========================================================================
//
//  /**
//   * Splits the given page into text areas.
//   * 
//   * @param pdf
//   *        The PDF document to which the page belong to.
//   * @param page
//   *        The PDF page to process.
//   *
//   * @return The lists of text areas.
//   */
//  protected List<PdfCharacterList> splitIntoTextAreas(PdfDocument pdf, PdfPage page) {
//    return this.textAreaTokenizer.splitIntoTextAreas(pdf, page, page.getCharacters());
//  }
//
//  /**
//   * Splits the given text area into text lines.
//   * 
//   * @param doc
//   *        The PDF document to which the text area belong to.
//   * @param page
//   *        The PDF page to which the text area belong to.
//   * @param textArea
//   *        The text area characters.
//   *
//   * @return The lists of line characters.
//   */
//  protected List<PdfCharacterList> splitIntoTextLines(PdfDocument doc,
//      PdfPage page, PdfCharacterList textArea) {
//    return this.textLineTokenizer.splitIntoTextLines(doc, page, textArea);
//  }
//  
//  /**
//   * Splits the given text line into words.
//   * 
//   * @param doc
//   *        The PDF document to which the text line belong to.
//   * @param page
//   *        The PDF page to which the text line belong to.
//   * @param line
//   *        The text line to process.
//   *
//   * @return The lists of words.
//   */
//  protected List<PdfCharacterList> splitIntoWords(PdfDocument doc, PdfPage page,
//      PdfCharacterList line) {
//    return this.wordTokenizer.splitIntoWords(doc, page, line);
//  }
//  
//  // ==========================================================================
//
//  /**
//   * Builds PdfTextLine objects from the given lists of included words.
//   * 
//   * @param doc
//   *        The PDF document to which the characters belong to.
//   * @param page
//   *        The PDF page to which the characters belong to.
//   * @param words
//   *        The lists of word characters.
//   * 
//   * @return The list of built words.
//   */
//  protected List<PdfWord> buildTextLines(PdfDocument doc, PdfPage page,
//      List<PdfWord> words) {
//    return this.textLineTokenizer.buildTextLines(words);
//  }
//
//  // ==========================================================================
//
//
//
//  // ==========================================================================
//
//
//
//  /**
//   * Builds PdfWord objects from the given lists of word characters.
//   * 
//   * @param doc
//   *        The PDF document to which the characters belong to.
//   * @param page
//   *        The PDF page to which the characters belong to.
//   * @param wordCharacters
//   *        The lists of word characters.
//   * 
//   * @return The list of built words.
//   */
//  protected List<PdfWord> buildWords(PdfDocument doc, PdfPage page,
//      List<PdfCharacterList> wordCharacters) {
//    return this.wordTokenizer.buildWords(wordCharacters);
//  }
//
//  // ==========================================================================
//
//  /**
//   * Builds PdfTextBlock objects from the given list of lines.
//   * 
//   * @param doc
//   *        The PDF document to which the lines belong to.
//   * @param page
//   *        The PDF page to which the lines belong to.
//   * @param lines
//   *        The lines to process.
//   * 
//   * @return The list of built PdfTextBlock objects.
//   */
//  protected List<PdfTextBlock> buildTextBlocks(PdfDocument doc,
//      PdfPage page, List<PdfTextLine> lines) {
//    return this.textBlockTokenizer.tokenize(doc, page, lines);
//  }
//
//  // ==========================================================================
//
//  // /**
//  // * Computes the texts of the text blocks, text lines and words of the given
//  // * page.
//  // *
//  // * @param page
//  // * The page to process.
//  // */
//  // protected void computeTexts(PdfPage page) {
//  // if (page == null) {
//  // return;
//  // }
//  //
//  // List<PdfTextBlock> textBlocks = page.getTextBlocks();
//  // if (textBlocks == null) {
//  // return;
//  // }
//  //
//  // for (PdfTextBlock block : textBlocks) {
//  // PdfTextLineList lines = block.getTextLines();
//  // if (lines == null) {
//  // continue;
//  // }
//  //
//  // for (PdfTextLine line : lines) {
//  // PdfWordList words = line.getWords();
//  // if (words == null) {
//  // continue;
//  // }
//  //
//  // for (PdfWord word : words) {
//  // // Compute the text for the word.
//  // PdfCharacterList characters = word.getCharacters();
//  // Collections.sort(characters, new MinXComparator());
//  // word.setText(CollectionUtils.join(characters, ""));
//  // }
//  //
//  // // Compute the text for the line.
//  // Collections.sort(words, new MinXComparator());
//  // line.setText(CollectionUtils.join(words, " "));
//  //
//  // // Check if the last word in the line is hyphenated.
//  // PdfWord lastWord = line.getLastWord();
//  // if (lastWord != null) {
//  // PdfCharacter lastCharacter = lastWord.getLastCharacter();
//  // lastWord.setIsHyphenated(PdfCharacterUtils.isHyphen(lastCharacter));
//  // }
//  // }
//  //
//  // // Compute the text for the block.
//  // Collections.sort(lines, Collections.reverseOrder(new MinYComparator()));
//  // block.setText(CollectionUtils.join(lines, " "));
//  // }
//  // }
//  //
//  // /**
//  // * Computes the texts of the text blocks, text lines and words of the given
//  // * page.
//  // *
//  // * @param page
//  // * The page to process.
//  // */
//  // protected void computeTexts2(PdfPage page) {
//  // if (page == null) {
//  // return;
//  // }
//  //
//  // List<PdfTextBlock> textBlocks = page.getTextBlocks();
//  // if (textBlocks == null) {
//  // return;
//  // }
//  //
//  // for (PdfTextBlock textBlock : textBlocks) {
//  // for (PdfTextLine line : textBlock.getTextLines()) {
//  // PdfWordList words = line.getWords();
//  // if (words == null) {
//  // continue;
//  // }
//  //
//  // for (PdfWord word : words) {
//  // // Compute the text for the word.
//  // Collections.sort(word.getCharacters(), new MinXComparator());
//  // word.setText(CollectionUtils.join(word.getCharacters(), ""));
//  // }
//  //
//  // // Compute the text for the line.
//  // Collections.sort(words, new MinXComparator());
//  // line.setText(CollectionUtils.join(words, " "));
//  // }
//  // }
//  // }
}
