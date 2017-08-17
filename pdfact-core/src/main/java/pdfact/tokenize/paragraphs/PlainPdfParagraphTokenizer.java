package pdfact.tokenize.paragraphs;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfCharacterStatistician;
import pdfact.models.PdfDocument;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfParagraph.PdfParagraphFactory;
import pdfact.models.PdfPosition;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLineStatistic;
import pdfact.models.PdfTextLineStatistician;
import pdfact.utils.collection.CollectionUtils;

/**
 * A plain implementation of {@link PdfParagraphTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraphTokenizer implements PdfParagraphTokenizer {
  /**
   * The paragraph segmenter.
   */
  protected PdfParagraphSegmenter paragraphSegmenter;

  /**
   * The factory to create instances of {@link PdfParagraph}.
   */
  protected PdfParagraphFactory paragraphFactory;

  /**
   * The character statistician.
   */
  protected PdfCharacterStatistician characterStatistician;

  /**
   * The text line statistician.
   */
  protected PdfTextLineStatistician textLineStatistician;

  // ==========================================================================

  /**
   * Creates a new {@link PlainPdfParagraphTokenizer}.
   * 
   * @param paragraphSegmenter
   *        The paragraph segmenter.
   * @param paragraphFactory
   *        The factory to create instances of {@link PdfParagraph}.
   * @param characterStatistician
   *        The character statistician.
   * @param textLineStatistician
   *        The text line statistician.
   */
  @Inject
  public PlainPdfParagraphTokenizer(
      PdfParagraphSegmenter paragraphSegmenter,
      PdfParagraphFactory paragraphFactory,
      PdfCharacterStatistician characterStatistician,
      PdfTextLineStatistician textLineStatistician) {
    this.paragraphSegmenter = paragraphSegmenter;
    this.paragraphFactory = paragraphFactory;
    this.characterStatistician = characterStatistician;
    this.textLineStatistician = textLineStatistician;
  }

  // ==========================================================================

  // TODO: Compose the text of the paragraphs, including the dehyphenation of
  // words.

  @Override
  public List<PdfParagraph> tokenize(PdfDocument pdf) {
    List<PdfParagraph> paragraphs = new ArrayList<>();

    // Segment the PDF document into paragraphs.
    List<List<PdfTextBlock>> segments = segmentIntoParagraphs(pdf);

    // Create the PdfParagraph objects.
    for (List<PdfTextBlock> segment : segments) {
      PdfParagraph paragraph = this.paragraphFactory.create();
      paragraph.setTextBlocks(segment);
      paragraph.setText(computeText(paragraph));
      paragraph.setPositions(computePositions(paragraph));
      paragraph.setRole(computeRole(paragraph));
      paragraph.setCharacterStatistic(computeCharacterStatistics(paragraph));
      paragraph.setTextLineStatistic(computeTextLineStatistics(paragraph));
      paragraphs.add(paragraph);
    }

    // TODO
    pdf.setParagraphs(paragraphs);

    return paragraphs;
  }

  // ==========================================================================

  /**
   * Segments the given PDF document into paragraphs.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return The list of list of text blocks of a paragraph.
   */
  protected List<List<PdfTextBlock>> segmentIntoParagraphs(PdfDocument pdf) {
    return this.paragraphSegmenter.segment(pdf);
  }

  // ==========================================================================

  /**
   * Computes the text for the given paragraph.
   * 
   * @param p
   *        The paragraph to process.
   * @return The text for the given paragraph.
   */
  protected String computeText(PdfParagraph p) {
    // TODO: Dehyphenate
    return CollectionUtils.join(p.getTextBlocks(), " ");
  }

  /**
   * Computes the positions for the given paragraph.
   * 
   * @param p
   *        The paragraph to process.
   * 
   * @return The text for the given paragraph.
   */
  protected List<PdfPosition> computePositions(PdfParagraph p) {
    List<PdfPosition> positions = new ArrayList<>();
    for (PdfTextBlock block : p.getTextBlocks()) {
      positions.add(block.getPosition());
    }
    return positions;
  }

  /**
   * Computes the role for the given paragraph.
   * 
   * @param p
   *        The paragraph to process.
   * 
   * @return The role for the given paragraph.
   */
  protected PdfRole computeRole(PdfParagraph p) {
    List<PdfTextBlock> textBlocks = p.getTextBlocks();
    if (textBlocks == null || textBlocks.isEmpty()) {
      return null;
    }
    // Return the role of the first text block.
    return textBlocks.get(0).getRole();
  }

  /**
   * Computes the statistics about the characters in the given paragraph.
   * 
   * @param p
   *        The paragraph to process.
   * 
   * @return The text for the given paragraph.
   */
  protected PdfCharacterStatistic computeCharacterStatistics(PdfParagraph p) {
    return this.characterStatistician.aggregate(p.getTextBlocks());
  }

  /**
   * Computes the statistics about the text lines in the given paragraph.
   * 
   * @param p
   *        The paragraph to process.
   * 
   * @return The text for the given paragraph.
   */
  protected PdfTextLineStatistic computeTextLineStatistics(PdfParagraph p) {
    return this.textLineStatistician.aggregate(p.getTextBlocks());
  }

  // ==========================================================================

  // /**
  // * Compose the texts for the paragraphs.
  // *
  // * @param pdf
  // * The PDF document.
  // * @param paragraphs
  // * The paragraphs to process.
  // */
  // protected void composeTexts(PdfDocument pdf, List<PdfParagraph> paragraphs)
  // {
  // PdfWordDehyphenator deyphenator = this.dehyphenatorFactory.create(pdf);
  //
  // for (PdfParagraph paragraph : pdf.getParagraphs()) {
  // // Put all words to a single list to be able to iterate them in one go.
  // List<PdfWord> words = new ArrayList<>();
  // for (PdfTextBlock block : paragraph.getTextBlocks()) {
  // for (PdfTextLine line : block.getTextLines()) {
  // words.addAll(line.getWords());
  // }
  // }
  //
  // List<PdfWord> newWords = new ArrayList<>();
  // for (int i = 0; i < words.size() - 1; i++) {
  // PdfWord word = words.get(i);
  // PdfWord nextWord = words.get(i + 1);
  //
  // if (word.isHyphenated()) {
  // PdfWord dehyphenated = deyphenator.dehyphenate(word, nextWord);
  // newWords.add(dehyphenated);
  // i++;
  // } else {
  // newWords.add(word);
  // }
  // }
  // // TODO: Don't forget the last word.
  // newWords.add(words.get(words.size() - 1));
  // paragraph.setText(CollectionUtils.join(newWords, " "));
  // }
  // }
}
