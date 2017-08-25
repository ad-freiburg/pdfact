package pdfact.model;

import java.util.Map;

import pdfact.util.counter.FloatCounter;

// TODO: Implement interfaces for counters and use them in the methods below.

/**
 * A statistic about a list of text lines.
 * 
 * @author Claudius Korzen
 */
public interface TextLineStatistic {
  /**
   * Returns the line pitch frequencies of the text lines, per font face.
   * 
   * @return The line pitch frequencies of the text lines, per font face.
   */
  Map<FontFace, FloatCounter> getLinePitchFrequencies();

  /**
   * Sets the line pitch frequencies of the text lines, per font face.
   * 
   * @param freqs
   *        The line pitch frequencies of the text lines.
   */
  void setLinePitchFrequencies(Map<FontFace, FloatCounter> freqs);

  // ==========================================================================

  /**
   * Returns the most common line pitch of text lines with the given font face.
   * 
   * @param fontFace
   *        The font face.
   * 
   * @return The most common line pitch of text lines with the same font face as
   *         the given font face.
   */
  float getMostCommonLinePitch(FontFace fontFace);

  // ==========================================================================

  /**
   * Returns the whitespace width frequencies in the text lines.
   * 
   * @return The whitespace width frequencies in the text lines.
   */
  FloatCounter getWhitespaceWidthFrequencies();

  /**
   * Sets the whitespace width frequencies in the text lines.
   * 
   * @param freqs
   *        The whitespace width frequencies in the text lines.
   */
  void setWhitespaceWidthFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the average width of white spaces between the words within the text
   * lines.
   * 
   * @return The average width of white spaces between the words within the text
   *         lines.
   */
  float getAverageWhitespaceWidth();

  // ==========================================================================

  /**
   * A factory to create instances of {@link TextLineStatistic}.
   */
  public interface TextLineStatisticFactory {
    /**
     * Creates a new instance of {@link TextLineStatistic}.
     * 
     * @return A new instance of {@link TextLineStatistic}.
     */
    TextLineStatistic create();
  }
}
