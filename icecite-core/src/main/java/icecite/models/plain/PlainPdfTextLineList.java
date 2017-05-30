package icecite.models.plain;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
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
  private static final long serialVersionUID = 1759166517929546020L;

  /**
   * The line pitches between the text lines, per font / font size pair.
   */
  protected Map<String, FloatCounter> linePitchesPerFontFace;

  /**
   * Creates a new PlainPdfTextLineList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
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
   *        The initial capacity.
   */
  @AssistedInject
  public PlainPdfTextLineList(RectangleFactory rectangleFactory,
      @Assisted int initialCapacity) {
    super(rectangleFactory, initialCapacity);
    this.linePitchesPerFontFace = new HashMap<>();
  }

  // ==========================================================================

  /**
   * Computes statistics about the text lines in this list.
   */
  protected void computeStatistics() {
    super.computeStatistics();

    for (int i = 1; i < this.size(); i++) {
      PdfTextLine prevLine = this.get(i - 1);
      PdfTextLine line = this.get(i);

      if (prevLine == null || line == null) {
        continue;
      }

      // Compose the font face of the current line.
      String fontFace = PdfTextLineUtils.computeFontFace(line);
      float linePitch = PdfTextLineUtils.computeLinePitch(prevLine, line);

      if (Float.isNaN(linePitch)) {
        continue;
      }

      // Add a new float counter if there is none for the given font face.
      this.linePitchesPerFontFace.putIfAbsent(fontFace, new FloatCounter());
      this.linePitchesPerFontFace.get(fontFace).add(linePitch);
    }

    this.isStatisticsOutdated = false;
  }

  @Override
  public float getMostCommonLinePitch(PdfTextLine line) {
    if (this.isStatisticsOutdated) {
      computeStatistics();
    }
    String fontFace = PdfTextLineUtils.computeFontFace(line);
    if (this.linePitchesPerFontFace.containsKey(fontFace)) {
      return this.linePitchesPerFontFace.get(fontFace).getMostCommonFloat();
    }
    return Float.NaN;
  }
}