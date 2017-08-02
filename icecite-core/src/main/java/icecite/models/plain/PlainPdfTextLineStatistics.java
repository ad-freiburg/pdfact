package icecite.models.plain;

import java.util.Map;

import icecite.models.PdfFontFace;
import icecite.models.PdfTextLineStatistics;
import icecite.utils.counter.FloatCounter;

/**
 * A plain implementation of {@link PdfTextLineStatistics}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLineStatistics implements PdfTextLineStatistics {
  /**
   * The most common line pitches per font faces.
   */
  protected Map<PdfFontFace, FloatCounter> linePitchFrequenciesPerFontFace;

  /**
   * The frequencies of whitespace widths.
   */
  protected FloatCounter whitespaceWidthFrequencies;

  // ==========================================================================

  @Override
  public Map<PdfFontFace, FloatCounter> getLinePitchFrequencies() {
    return this.linePitchFrequenciesPerFontFace;
  }

  @Override
  public void setLinePitchFrequencies(Map<PdfFontFace, FloatCounter> freqs) {
    this.linePitchFrequenciesPerFontFace = freqs;
  }

  // ==========================================================================

  @Override
  public float getMostCommonLinePitch(PdfFontFace fontFace) {
    FloatCounter freqs = this.linePitchFrequenciesPerFontFace.get(fontFace);
    return freqs != null ? freqs.getMostCommonFloat() : null;
  }

  // ==========================================================================

  @Override
  public FloatCounter getWhitespaceWidthFrequencies() {
    return this.whitespaceWidthFrequencies;
  }

  @Override
  public void setWhitespaceWidthFrequencies(FloatCounter freqs) {
    this.whitespaceWidthFrequencies = freqs;
  }

  // ==========================================================================

  @Override
  public float getAverageWhitespaceWidth() {
    if (this.whitespaceWidthFrequencies == null) {
      return Float.NaN;
    }
    return this.whitespaceWidthFrequencies.getAverageFloat();
  }
}
