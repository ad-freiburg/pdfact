package pdfact.core.model.plain;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.core.model.FontFace;
import pdfact.core.model.TextLineStatistic;
import pdfact.core.util.counter.FloatCounter;

/**
 * A plain implementation of {@link TextLineStatistic}.
 * 
 * @author Claudius Korzen
 */
public class PlainTextLineStatistic implements TextLineStatistic {
  /**
   * The most common line pitches per font faces.
   */
  protected Map<FontFace, FloatCounter> linePitchFrequenciesPerFontFace;

  /**
   * The frequencies of whitespace widths.
   */
  protected FloatCounter whitespaceWidthFrequencies;

  // ==========================================================================

  @Override
  public Map<FontFace, FloatCounter> getLinePitchFrequencies() {
    return this.linePitchFrequenciesPerFontFace;
  }

  @Override
  public void setLinePitchFrequencies(Map<FontFace, FloatCounter> freqs) {
    this.linePitchFrequenciesPerFontFace = freqs;
  }

  // ==========================================================================

  @Override
  public float getMostCommonLinePitch(FontFace fontFace) {
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

  // ==========================================================================

  @Override
  public boolean equals(Object o) {
    if (o instanceof TextLineStatistic) {
      TextLineStatistic other = (TextLineStatistic) o;

      EqualsBuilder build = new EqualsBuilder();
      build.append(getLinePitchFrequencies(), other.getLinePitchFrequencies());
      build.append(getWhitespaceWidthFrequencies(),
          other.getWhitespaceWidthFrequencies());

      return build.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getLinePitchFrequencies());
    builder.append(getWhitespaceWidthFrequencies());
    return builder.hashCode();
  }
}
