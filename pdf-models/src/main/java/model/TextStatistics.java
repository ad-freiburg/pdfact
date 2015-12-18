package model;

import java.util.Map;

/**
 * Interface for text statistics.
 *
 * @author Claudius Korzen
 *
 */
public interface TextStatistics {
  /**
   * Returns the average fontsize.
   */
  public float getAverageFontsize();

  /**
   * Returns the most common fontsize.
   */
  public float getMostCommonFontsize();

  /**
   * Returns the most common font color.
   */
  public PdfColor getMostCommonFontColor();

  /**
   * Returns the most common font.
   */
  public PdfFont getMostCommonFont();

  /**
   * Returns the fontsize frequencies in a map.
   */
  public Map<Float, Integer> getFontsizeFrequencies();
  
  /**
   * Returns the font frequencies in a map.
   */
  public Map<PdfFont, Integer> getFontFrequencies();
  
  /**
   * Returns the color frequencies in a map.
   */
  public Map<PdfColor, Integer> getColorFrequencies();

  /**
   * Returns the ascii ratio.
   */
  public float getAsciiRatio();
  
  /**
   * Returns the digits ratio.
   */
  public float getDigitsRatio();
}
