package pdfact.core.model;

import java.util.Map;

import pdfact.core.util.counter.FloatCounter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A statistic about text lines.
 * 
 * @author Claudius Korzen
 */
public class TextLineStatistic {
  /**
   * The most common line pitches per font faces.
   */
  protected Map<FontFace, FloatCounter> linePitchFrequenciesPerFontFace;

  /**
   * The frequencies of whitespace widths.
   */
  protected FloatCounter whitespaceWidthFrequencies;

  // ==============================================================================================

  /**
   * Returns the line pitch frequencies of the text lines, per font face.
   * 
   * @return The line pitch frequencies of the text lines, per font face.
   */
  public Map<FontFace, FloatCounter> getLinePitchFrequencies() {
    return this.linePitchFrequenciesPerFontFace;
  }

  /**
   * Sets the line pitch frequencies of the text lines, per font face.
   * 
   * @param freqs The line pitch frequencies of the text lines.
   */
  public void setLinePitchFrequencies(Map<FontFace, FloatCounter> freqs) {
    this.linePitchFrequenciesPerFontFace = freqs;
  }

  // ==============================================================================================

  /**
   * Returns the most common line pitch of text lines with the given font face.
   * 
   * @param fontFace The font face.
   * 
   * @return The most common line pitch of text lines with the same font face as
   *         the given font face.
   */
  public float getMostCommonLinePitch(FontFace fontFace) {
    FloatCounter freqs = this.linePitchFrequenciesPerFontFace.get(fontFace);
    return freqs != null ? freqs.getMostCommonFloat() : Float.NaN;
  }

  // ==============================================================================================

  /**
   * Returns the whitespace width frequencies in the text lines.
   * 
   * @return The whitespace width frequencies in the text lines.
   */
  public FloatCounter getWhitespaceWidthFrequencies() {
    return this.whitespaceWidthFrequencies;
  }

  /**
   * Sets the whitespace width frequencies in the text lines.
   * 
   * @param freqs The whitespace width frequencies in the text lines.
   */
  public void setWhitespaceWidthFrequencies(FloatCounter freqs) {
    this.whitespaceWidthFrequencies = freqs;
  }

  // ==============================================================================================

  /**
   * Returns the average width of white spaces between the words within the text
   * lines.
   * 
   * @return The average width of white spaces between the words within the text
   *         lines.
   */
  public float getAverageWhitespaceWidth() {
    if (this.whitespaceWidthFrequencies == null) {
      return Float.NaN;
    }
    return this.whitespaceWidthFrequencies.getAverageFloat();
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object o) {
    if (o instanceof TextLineStatistic) {
      TextLineStatistic other = (TextLineStatistic) o;

      EqualsBuilder build = new EqualsBuilder();
      build.append(getLinePitchFrequencies(), other.getLinePitchFrequencies());
      build.append(getWhitespaceWidthFrequencies(), other.getWhitespaceWidthFrequencies());

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
