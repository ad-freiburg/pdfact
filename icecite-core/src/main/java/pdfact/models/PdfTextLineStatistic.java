package pdfact.models;

import java.util.Map;

import pdfact.utils.counter.FloatCounter;

/**
 * A class that provides statistics about a set of text lines.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineStatistic {
  /**
   * Returns the frequencies of line pitches.
   * 
   * @return The frequencies of line pitches.
   */
  Map<PdfFontFace, FloatCounter> getLinePitchFrequencies();

  /**
   * Sets the frequencies of line pitches.
   * 
   * @param freqs
   *        The frequencies of line pitches.
   */
  void setLinePitchFrequencies(Map<PdfFontFace, FloatCounter> freqs);

  // ==========================================================================

  /**
   * Returns the most common line pitch of lines with the given font face (pair
   * font/font size).
   * 
   * @param fontFace
   *        The font face.
   * 
   * @return The most common line pitch of lines with the same font face as of
   *         the given line.
   */
  float getMostCommonLinePitch(PdfFontFace fontFace);

  // ==========================================================================

  /**
   * Returns the frequencies of whitespace widths.
   * 
   * @return The frequencies of whitespace widths.
   */
  FloatCounter getWhitespaceWidthFrequencies();

  /**
   * Sets the frequencies of whitespace widths.
   * 
   * @param freqs
   *        The frequencies of whitespace widths.
   */
  void setWhitespaceWidthFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the average width of white spaces between the words within the
   * lines.
   * 
   * @return The average width of white spaces between the words.
   */
  float getAverageWhitespaceWidth();

  // ==========================================================================

  /**
   * A factory to create instances of PdfTextLineStats.
   */
  public interface PdfTextLineStatisticFactory {
    /**
     * Creates a PdfTextLineStats.
     * 
     * @return An instance of {@link PdfTextLineStatistic}.
     */
    PdfTextLineStatistic create();
  }
}
