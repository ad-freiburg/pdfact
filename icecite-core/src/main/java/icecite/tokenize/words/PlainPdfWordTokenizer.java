package icecite.tokenize.words;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfWord;
import icecite.models.PdfWord.PdfWordFactory;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;

/**
 * A plain implementation of {@link PdfWordTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfWordTokenizer implements PdfWordTokenizer {
  /**
   * The factory to create instances of {@link PdfWordListFactory}.
   */
  protected PdfWordListFactory wordListFactory;

  /**
   * The factory to create instances of {@link PdfWordFactory}.
   */
  protected PdfWordFactory wordFactory;

  /**
   * The word classifier.
   */
  protected PdfWordSegmenter segmenter;

  /**
   * The default constructor.
   * 
   * @param wordListFactory
   *        The factory to create instances of {@link PdfWordListFactory}.
   * @param wordFactory
   *        The factory to create instances of {@link PdfWordFactory}.
   * @param segmenter
   *        The word segmented.
   */
  @Inject
  public PlainPdfWordTokenizer(PdfWordListFactory wordListFactory,
      PdfWordFactory wordFactory, PdfWordSegmenter segmenter) {
    this.wordListFactory = wordListFactory;
    this.wordFactory = wordFactory;
    this.segmenter = segmenter;
  }

  @Override
  public PdfWordList tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterList line) {
    PdfWordList words = this.wordListFactory.create();
    
    // Segment the given characters of a line into word segments.
    List<PdfCharacterList> segments = this.segmenter.segment(pdf, page, line);
    
    // Create the PdfWord objects.
    for (PdfCharacterList segment : segments) {
      PdfWord word = this.wordFactory.create();
      word.setCharacters(segment);
      // TODO:
//      word.setCharacterStatistics(characters);
//      word.setIsHyphenated(isHyphenated);
//      word.setPosition(position);
//      word.setText(text);
      
      words.add(word);
    }
    
    return words;
  }

}
