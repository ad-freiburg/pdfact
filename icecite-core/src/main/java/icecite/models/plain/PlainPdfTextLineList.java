package icecite.models.plain;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfFontFace;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.utils.counter.FloatCounter;
import icecite.utils.geometric.Rectangle.RectangleFactory;
import icecite.utils.textlines.PdfTextLineUtils;

/**
 * A plain implementation of {@link PdfTextLineList}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLineList extends PlainPdfElementList<PdfTextLine>
    implements PdfTextLineList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 1759166517929546020L;

  /**
   * The line pitches between the text lines, per font faces (font / font size
   * pair).
   */
  protected Map<PdfFontFace, FloatCounter> linePitchesPerFontFace;

  /**
   * The average whitespace width.
   */
  protected float averageWhitespaceWidth;
  
  /**
   * Creates a new PlainPdfTextLineList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link RectangleFactory}.
   */
  @AssistedInject
  public PlainPdfTextLineList(RectangleFactory rectangleFactory) {
    this(rectangleFactory, DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new PlainPdfTextLineList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   * @param initialCapacity
   *        The initial capacity of this list.
   */
  @AssistedInject
  public PlainPdfTextLineList(RectangleFactory rectangleFactory,
      @Assisted int initialCapacity) {
    super(rectangleFactory, initialCapacity);
    this.linePitchesPerFontFace = new HashMap<>();
  }

  // ==========================================================================

  /**
   * Computes some statistics about the text lines in this list.
   */
  protected void computeStatistics() {
    super.computeStatistics();

    float sumWhitespaceWidths = 0;
    float numWhitespaceWidths = 0;
    
    for (int i = 1; i < this.size(); i++) {
      PdfTextLine prevLine = this.get(i - 1);
      PdfTextLine line = this.get(i);

      if (prevLine == null || line == null) {
        continue;
      }

      // Compute the font face of the current line.
      PdfFontFace fontFace = line.getCharacters().getMostCommonFontFace();
      // Compute the line pitch between the current line and the previous line.
      float linePitch = PdfTextLineUtils.computeLinePitch(prevLine, line);
     
      if (Float.isNaN(linePitch)) {
        continue;
      }
      
      // Add a new float counter if there is none for the given font face.
      this.linePitchesPerFontFace.putIfAbsent(fontFace, new FloatCounter());
      this.linePitchesPerFontFace.get(fontFace).add(linePitch);
      
      // Compute the average whitespace width.
      PdfWordList words = line.getWords();
      if (words == null) {
        continue;
      }
      for (int j = 1; j < words.size(); j++) {
        PdfWord prevWord = words.get(j - 1);
        PdfWord word = words.get(j);
        if (prevWord == null || word == null) {
          continue;
        }
        float wordMinX = word.getRectangle().getMinX();
        float prevWordMaxX = prevWord.getRectangle().getMaxX();
        float whitespaceWidth = wordMinX - prevWordMaxX;
        sumWhitespaceWidths += whitespaceWidth;
        numWhitespaceWidths++;
      }
    }

    if (numWhitespaceWidths > 0) {
      this.averageWhitespaceWidth = sumWhitespaceWidths / numWhitespaceWidths;
    }
    
    this.isStatisticsOutdated = false;
  }

  @Override
  public float getMostCommonLinePitch(PdfTextLine line) {
    if (this.isStatisticsOutdated) {
      computeStatistics();
    }
    PdfFontFace fontFace = line.getCharacters().getMostCommonFontFace();
    if (this.linePitchesPerFontFace.containsKey(fontFace)) {
      return this.linePitchesPerFontFace.get(fontFace).getMostCommonFloat();
    }
    return Float.NaN;
  }

  @Override
  public float getAverageWhitespaceWidth() {
    if (this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.averageWhitespaceWidth;
  }
  
  // ==========================================================================

  @Override
  public String toString() {
    return super.toString();
  }

  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}