package pdfact.model;

import pdfact.util.counter.FloatCounter;
import pdfact.util.counter.ObjectCounter;

// TODO: Implement interfaces for counters and use them in the methods below.

/**
 * A statistic about a list of characters.
 * 
 * @author Claudius Korzen
 */
public interface CharacterStatistic {
  /**
   * Returns the height frequencies of the characters.
   * 
   * @return The height frequencies of the characters.
   */
  FloatCounter getHeightFrequencies();

  /**
   * Sets the height frequencies of the characters.
   * 
   * @param freqs
   *        The height frequencies of the characters.
   */
  void setHeightFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the most common height of the characters.
   * 
   * @return The most common height of the characters.
   */
  float getMostCommonHeight();

  /**
   * Returns the average height of the characters.
   * 
   * @return The average height of the characters.
   */
  float getAverageHeight();

  // ==========================================================================

  /**
   * Returns the width frequencies of the characters.
   * 
   * @return The width frequencies of the characters.
   */
  FloatCounter getWidthFrequencies();

  /**
   * Sets the width frequencies of the characters.
   * 
   * @param freqs
   *        The width frequencies of the characters.
   */
  void setWidthFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the most common width of the characters.
   * 
   * @return The most common width of the characters.
   */
  float getMostCommonWidth();

  /**
   * Returns the average width of the characters.
   * 
   * @return The average width of the characters.
   */
  float getAverageWidth();

  // ==========================================================================

  /**
   * Returns the font face frequencies of the characters.
   * 
   * @return The font face frequencies of the characters.
   */
  ObjectCounter<FontFace> getFontFaceFrequencies();

  /**
   * Sets the font face frequencies of the characters.
   * 
   * @param freqs
   *        The font face frequencies of the characters.
   */
  void setFontFaceFrequencies(ObjectCounter<FontFace> freqs);

  // ==========================================================================

  /**
   * Returns the most common font face of the characters.
   * 
   * @return The most common font face of the characters.
   */
  FontFace getMostCommonFontFace();

  // ==========================================================================

  /**
   * Returns the font size frequencies of the characters.
   * 
   * @return The font size frequencies of the characters.
   */
  FloatCounter getFontSizeFrequencies();

  /**
   * Sets the font size frequencies of the characters.
   * 
   * @param freqs
   *        The font size frequencies of the characters.
   */
  void setFontSizeFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the average font size of the characters.
   * 
   * @return The average font size of the characters.
   */
  float getAverageFontsize();

  // ==========================================================================

  /**
   * Returns the color frequencies of the characters.
   * 
   * @return The color frequencies of the characters.
   */
  ObjectCounter<Color> getColorFrequencies();

  /**
   * Sets the color frequencies of the characters.
   * 
   * @param freqs
   *        The color frequencies of the characters.
   */
  void setColorFrequencies(ObjectCounter<Color> freqs);

  // ==========================================================================

  /**
   * Returns the most common color of the characters.
   * 
   * @return The most common color of the characters.
   */
  Color getMostCommonColor();

  // ==========================================================================

  /**
   * Returns the smallest minX value of the characters.
   * 
   * @return The smallest minX value of the characters.
   */
  float getSmallestMinX();

  /**
   * Sets the smallest minX value of the characters.
   * 
   * @param minX
   *        The smallest minX value of the characters.
   */
  void setSmallestMinX(float minX);

  // ==========================================================================

  /**
   * Returns the smallest minY value of the characters.
   * 
   * @return The smallest minY value of the characters.
   */
  float getSmallestMinY();

  /**
   * Sets the smallest minY value of the characters.
   * 
   * @param minY
   *        The smallest minY value of the characters.
   */
  void setSmallestMinY(float minY);

  // ==========================================================================

  /**
   * Returns the largest maxX value of the characters.
   * 
   * @return The largest maxX value of the characters.
   */
  float getLargestMaxX();

  /**
   * Sets the largest maxX value of the characters.
   * 
   * @param maxX
   *        The largest maxX value of the characters.
   */
  void setLargestMaxX(float maxX);

  // ==========================================================================

  /**
   * Returns the largest maxY value of the characters.
   * 
   * @return The largest maxY value of the characters.
   */
  float getLargestMaxY();

  /**
   * Sets the largest maxY value of the characters.
   * 
   * @param maxY
   *        The largest maxY value of the characters.
   */
  void setLargestMaxY(float maxY);

  // ==========================================================================

  /**
   * A factory to create instances of {@link CharacterStatistic}.
   */
  public interface CharacterStatisticFactory {
    /**
     * Creates a new instance of {@link CharacterStatistic}.
     * 
     * @return A new instance of {@link CharacterStatistic}.
     */
    CharacterStatistic create();
  }
}
