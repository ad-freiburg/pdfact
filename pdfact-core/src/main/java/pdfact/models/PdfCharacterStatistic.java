package pdfact.models;

import pdfact.utils.counter.FloatCounter;
import pdfact.utils.counter.ObjectCounter;

// TODO: Implement interfaces for counters and use them in the methods below.

/**
 * A class that provides a statistic about a list of characters.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterStatistic {
  /**
   * Returns the height frequencies.
   * 
   * @return The height frequencies.
   */
  FloatCounter getHeightFrequencies();

  /**
   * Sets the height frequencies.
   * 
   * @param freqs
   *        The height frequencies.
   */
  void setHeightFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the most common height.
   * 
   * @return The most common height.
   */
  float getMostCommonHeight();

  /**
   * Returns the average height.
   * 
   * @return The average height.
   */
  float getAverageHeight();

  // ==========================================================================

  /**
   * Returns the width frequencies.
   * 
   * @return The width frequencies.
   */
  FloatCounter getWidthFrequencies();

  /**
   * Sets the width frequencies.
   * 
   * @param freqs
   *        The width frequencies.
   */
  void setWidthFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the most common width.
   * 
   * @return The most common width.
   */
  float getMostCommonWidth();

  /**
   * Returns the average width.
   * 
   * @return The average width.
   */
  float getAverageWidth();

  // ==========================================================================

  /**
   * Returns the font face frequencies.
   * 
   * @return The font face frequencies.
   */
  ObjectCounter<PdfFontFace> getFontFaceFrequencies();

  /**
   * Sets the font face frequencies.
   * 
   * @param freqs
   *        The font face frequencies.
   */
  void setFontFaceFrequencies(ObjectCounter<PdfFontFace> freqs);

  // ==========================================================================

  /**
   * Returns the most common font face.
   * 
   * @return The most common font face.
   */
  PdfFontFace getMostCommonFontFace();

  // ==========================================================================

  /**
   * Returns the font size frequencies.
   * 
   * @return The font size frequencies.
   */
  FloatCounter getFontSizeFrequencies();

  /**
   * Sets the font size frequencies.
   * 
   * @param freqs
   *        The font size frequencies.
   */
  void setFontSizeFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the average font size.
   * 
   * @return The average font size.
   */
  float getAverageFontsize();

  // ==========================================================================

  /**
   * Returns the color frequencies.
   * 
   * @return The color frequencies.
   */
  ObjectCounter<PdfColor> getColorFrequencies();

  /**
   * Sets the color frequencies.
   * 
   * @param freqs
   *        The color frequencies.
   */
  void setColorFrequencies(ObjectCounter<PdfColor> freqs);

  // ==========================================================================

  /**
   * Returns the most common color.
   * 
   * @return The most common color.
   */
  PdfColor getMostCommonColor();

  // ==========================================================================

  /**
   * Returns the smallest minX value.
   * 
   * @return The smallest minX value.
   */
  float getSmallestMinX();

  /**
   * Sets the smallest minX value.
   * 
   * @param minX
   *        The smallest minX value.
   */
  void setSmallestMinX(float minX);

  // ==========================================================================

  /**
   * Returns the smallest minY value.
   * 
   * @return The smallest minY value.
   */
  float getSmallestMinY();

  /**
   * Sets the smallest minY value.
   * 
   * @param minY
   *        The smallest minY value.
   */
  void setSmallestMinY(float minY);

  // ==========================================================================

  /**
   * Returns the largest maxX value.
   * 
   * @return The largest maxX value.
   */
  float getLargestMaxX();

  /**
   * Sets the largest maxX value.
   * 
   * @param maxX
   *        The largest maxX value.
   */
  void setLargestMaxX(float maxX);

  // ==========================================================================

  /**
   * Returns the largest maxY value.
   * 
   * @return The largest maxY value.
   */
  float getLargestMaxY();

  /**
   * Sets the largest maxY value.
   * 
   * @param maxY
   *        The largest maxY value.
   */
  void setLargestMaxY(float maxY);

  // ==========================================================================

  /**
   * A factory to create instances of {@link PdfCharacterStatistic}.
   */
  public interface PdfCharacterStatisticFactory {
    /**
     * Creates a new instance of PdfCharacterStatistic.
     * 
     * @return A new instance of {@link PdfCharacterStatistic}.
     */
    PdfCharacterStatistic create();
  }
}
