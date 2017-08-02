package icecite.tokenize.lines;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLine.PdfTextLineFactory;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.models.PdfWordList;
import icecite.tokenize.areas.PdfTextAreaSegmenter;
import icecite.tokenize.words.PdfWordTokenizer;

/**
 * A plain implementation of {@link PdfTextLineTokenizer}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLineTokenizer implements PdfTextLineTokenizer {
  /**
   * The text area tokenizer.
   */
  protected PdfTextAreaSegmenter textAreaSegmenter;

  /**
   * The text line classifier;
   */
  protected PdfTextLineSegmenter segmenter;

  /**
   * The word tokenizer.
   */
  protected PdfWordTokenizer wordTokenizer;

  /**
   * The factory to create instances of {@link PdfTextLineList}.
   */
  protected PdfTextLineListFactory textLineListFactory;

  /**
   * The factory to create instances of {@link PdfTextLine}.
   */
  protected PdfTextLineFactory textLineFactory;
  
  /**
   * The default constructor.
   * 
   * @param textAreaTokenizer
   *        The text area tokenizer.
   * @param textLineClassifier
   *        The text line classifier.
   * @param wordTokenizer
   *        The tokenizer to tokenize words.
   * @param textLineListFactory
   *        The factory to create instances of {@link PdfTextLineList}.
   * @param textLineFactory
   *        The factory to create instances of {@link PdfTextLine}.
   */
  @Inject
  public PlainPdfTextLineTokenizer(PdfTextAreaSegmenter textAreaTokenizer,
      PdfTextLineSegmenter textLineClassifier,
      PdfWordTokenizer wordTokenizer,
      PdfTextLineListFactory textLineListFactory,
      PdfTextLineFactory textLineFactory) {
    this.textAreaSegmenter = textAreaTokenizer;
    this.segmenter = textLineClassifier;
    this.wordTokenizer = wordTokenizer;
    this.textLineListFactory = textLineListFactory;
    this.textLineFactory = textLineFactory;
  }

  @Override
  public PdfTextLineList tokenize(PdfDocument pdf, PdfPage page) {
    PdfTextLineList textLines = this.textLineListFactory.create();

    // Segment the characters of the page into text area segments.
    List<PdfCharacterList> textAreaSegments = 
        this.textAreaSegmenter.segment(pdf, page, page.getCharacters());
    
    // Segment each text area segment into text line segments.
    for (PdfCharacterList textAreaSegment : textAreaSegments) {
      List<PdfCharacterList> lineSegments =
          this.segmenter.segment(pdf, page, textAreaSegment);
      
      // Segment each text line segment into words.
      for (PdfCharacterList lineSegment : lineSegments) {
        // Tokenize the segment into words.
        PdfWordList words = this.wordTokenizer.tokenize(pdf, page, lineSegment);
        
        // Create a PdfTextLine object.
        PdfTextLine textLine = this.textLineFactory.create();
        textLine.setWords(words);
        // TODO:
//        textLine.setCharacterStatistics(statistics);
//        textLine.setPosition(position);
//        textLine.setBaseline(baseLine);
//        textLine.setText(text);
        
        textLines.add(textLine);
      }
    }

    return textLines;
  }
}
