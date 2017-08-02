package icecite.models;

import icecite.utils.counter.FloatCounter;
import icecite.utils.counter.ObjectCounter;

/**
 * A class that provides statistics about a set of characters.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterStatistics {
  /**
   * Returns the frequencies of the heights.
   * 
   * @return The frequencies of the heights.
   */
  // TODO: Use an interface here.
  FloatCounter getHeightFrequencies();

  /**
   * Sets the frequencies of the heights.
   * 
   * @param freqs
   *        The frequencies of the heights.
   */
  // TODO: Use an interface here.
  void setHeightFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the most common height of the PDF elements.
   * 
   * @return The most common height of the PDF elements.
   */
  float getMostCommonHeight();

  /**
   * Returns the average height of the PDF elements.
   * 
   * @return The average height of the PDF elements.
   */
  float getAverageHeight();

  // ==========================================================================

  /**
   * Returns the frequencies of the widths.
   * 
   * @return The frequencies of the widths.
   */
  // TODO: Use an interface here.
  FloatCounter getWidthFrequencies();

  /**
   * Sets the frequencies of the widths.
   * 
   * @param freqs
   *        The frequencies of the widths.
   */
  // TODO: Use an interface here.
  void setWidthFrequencies(FloatCounter freqs);

  // ==========================================================================

  /**
   * Returns the most common width of the PDF elements.
   * 
   * @return The most common width of the PDF elements.
   */
  float getMostCommonWidth();

  /**
   * Returns the average width of the PDF elements.
   * 
   * @return The average width of the PDF elements.
   */
  float getAverageWidth();

  // ==========================================================================

  /**
   * Returns the smallest minX value of the PDF elements.
   * 
   * @return The smallest minX value of the PDF elements.
   */
  float getSmallestMinX();

  /**
   * Sets the smallest minX value of the PDF elements.
   * 
   * @param minX
   *        The smallest minX value of the PDF elements.
   */
  void setSmallestMinX(float minX);

  // ==========================================================================

  /**
   * Returns the smallest minY value of the PDF elements.
   * 
   * @return The smallest minY value of the PDF elements.
   */
  float getSmallestMinY();

  /**
   * Sets the smallest minY value of the PDF elements.
   * 
   * @param minY
   *        The smallest minY value of the PDF elements.
   */
  void setSmallestMinY(float minY);

  // ==========================================================================

  /**
   * Returns the largest maxX value of the PDF elements.
   * 
   * @return The largest maxX value of the PDF elements.
   */
  float getLargestMaxX();

  /**
   * Sets the largest maxX value of the PDF elements.
   * 
   * @param maxX
   *        The largest maxX value of the PDF elements.
   */
  void setLargestMaxX(float maxX);

  // ==========================================================================

  /**
   * Returns the largest maxY value of the PDF elements.
   * 
   * @return The largest maxY value of the PDF elements.
   */
  float getLargestMaxY();

  /**
   * Sets the largest maxY value of the PDF elements.
   * 
   * @param maxY
   *        The largest maxY value of the PDF elements.
   */
  void setLargestMaxY(float maxY);

  // ==========================================================================

  /**
   * Returns the frequencies of the font faces.
   * 
   * @return The frequencies of the font faces.
   */
  // TODO: Use an interface here.
  ObjectCounter<PdfFontFace> getFontFaceFrequencies();

  /**
   * Sets the frequencies of the font faces.
   * 
   * @param freqs
   *        The frequencies of the font faces.
   */
  // TODO: Use an interface here.
  void setFontFaceFrequencies(ObjectCounter<PdfFontFace> freqs);

  // ==========================================================================

  /**
   * Returns the most common font face of the characters.
   * 
   * @return The most common font face of the characters.
   */
  PdfFontFace getMostCommonFontFace();

  // ==========================================================================

  /**
   * Returns the frequencies of the font sizes.
   * 
   * @return The frequencies of the font sizes.
   */
  // TODO: Use an interface here.
  FloatCounter getFontSizeFrequencies();

  /**
   * Sets the frequencies of the font sizes.
   * 
   * @param freqs
   *        The frequencies of the font sizes.
   */
  // TODO: Use an interface here.
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
   * Returns the frequencies of the colors.
   * 
   * @return The frequencies of the colors.
   */
  // TODO: Use an interface here.
  ObjectCounter<PdfColor> getColorFrequencies();

  /**
   * Sets the frequencies of the colors.
   * 
   * @param freqs
   *        The frequencies of the colors.
   */
  // TODO: Use an interface here.
  void setColorFrequencies(ObjectCounter<PdfColor> freqs);

  // ==========================================================================

  /**
   * Returns the most common color of the characters.
   * 
   * @return The most common color of the characters.
   */
  PdfColor getMostCommonColor();

  // ==========================================================================

  /**
   * A factory to create instances of PdfCharacterStats.
   */
  public interface PdfCharacterStatisticsFactory {
    /**
     * Creates a PdfCharacterStats.
     * 
     * @return An instance of {@link PdfCharacterStatistics}.
     */
    PdfCharacterStatistics create();
  }
}
