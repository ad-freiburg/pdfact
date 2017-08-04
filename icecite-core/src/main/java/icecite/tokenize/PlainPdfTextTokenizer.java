package icecite.tokenize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterStatistician;
import icecite.models.PdfCharacterStatistic;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineStatistician;
import icecite.tokenize.blocks.PdfTextBlockTokenizer;
import icecite.tokenize.lines.PdfTextLineTokenizer;

/**
 * A plain implementation of {@link PdfTextTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextTokenizer implements PdfTextTokenizer {
  /**
   * The text line tokenizer.
   */
  protected PdfTextLineTokenizer textLineTokenizer;

  /**
   * The text block tokenizer.
   */
  protected PdfTextBlockTokenizer textBlockTokenizer;

  /**
   * The statistician to compute statistics about characters.
   */
  protected PdfCharacterStatistician characterStatistician;

  /**
   * The statistician to compute statistics about text lines.
   */
  protected PdfTextLineStatistician textLineStatistician;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new text tokenizer.
   * 
   * @param textLineTokenizer
   *        The text line tokenizer.
   * @param textBlockTokenizer
   *        The text block tokenizer.
   * @param characterStatistican
   *        The statistician to compute statistics about characters.
   * @param textLineStatistician
   *        The statistician to compute statistics about text lines.
   */
  @Inject
  public PlainPdfTextTokenizer(
      PdfTextLineTokenizer textLineTokenizer,
      PdfTextBlockTokenizer textBlockTokenizer,
      PdfCharacterStatistician characterStatistican,
      PdfTextLineStatistician textLineStatistician) {
    this.textLineTokenizer = textLineTokenizer;
    this.textBlockTokenizer = textBlockTokenizer;
    this.characterStatistician = characterStatistican;
    this.textLineStatistician = textLineStatistician;
  }

  // ==========================================================================

  @Override
  public void tokenize(PdfDocument pdf) {
    computeCharacterStatistics(pdf);
    Map<PdfPage, PdfTextLineList> textLines = tokenizeTextLines(pdf);
    tokenizeIntoTextBlocks(pdf, textLines);
  }

  // ==========================================================================

  /**
   * Tokenizes the pages of the given PDF document into text lines.
   * 
   * @param pdf
   *        The PDF document to process.
   * @return A map that connects a PDF page to its text lines.
   */
  protected Map<PdfPage, PdfTextLineList> tokenizeTextLines(PdfDocument pdf) {
    Map<PdfPage, PdfTextLineList> linesMap = new HashMap<>();

    for (PdfPage page : pdf.getPages()) {
      PdfTextLineList lines = this.textLineTokenizer.tokenize(pdf, page);
      page.setTextLineStatistic(this.textLineStatistician.compute(lines));
      linesMap.put(page, lines);
    }

    pdf.setTextLineStatistic(
        this.textLineStatistician.combine(pdf.getPages()));

    return linesMap;
  }

  /**
   * Tokenizes the pages of the given PDF document into text blocks.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param lines
   *        The lines to process.
   */
  protected void tokenizeIntoTextBlocks(PdfDocument pdf,
      Map<PdfPage, PdfTextLineList> lines) {
    for (PdfPage page : lines.keySet()) {
      page.setTextBlocks(
          this.textBlockTokenizer.tokenize(pdf, page, lines.get(page)));
    }
  }

  // ==========================================================================

  /**
   * Computes the statistics about characters in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void computeCharacterStatistics(PdfDocument pdf) {
    List<PdfPage> pages = pdf.getPages();
    for (PdfPage page : pages) {
      // Compute the character statistics per page.
      PdfCharacterList chars = page.getCharacters();
      PdfCharacterStatistic stats = this.characterStatistician.compute(chars);
      // Add the statistics to the page and the list of statistics.
      page.setCharacterStatistic(stats);
    }
    // Compute the character statistics for the whole PDF document.
    pdf.setCharacterStatistic(this.characterStatistician.combine(pages));
  }

  /**
   * Computes the statistics about text lines in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void computeTextLineStatistics(PdfDocument pdf) {
    List<PdfPage> pages = pdf.getPages();
    // Compute the text line statistics for the whole PDF document.
    pdf.setTextLineStatistic(this.textLineStatistician.aggregate(pages));
  }
}
