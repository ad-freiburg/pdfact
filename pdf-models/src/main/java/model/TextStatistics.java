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
  float getAverageFontsize();
  
  /**
   * Returns the most common fontsize.
   */
  float getMostCommonFontsize();
    
  /**
   * Returns the most common font color.
   */
  PdfColor getMostCommonFontColor();
  
  /**
   * Returns the most common font.
   */
  PdfFont getMostCommonFont();
  
  /**
   * Returns the fontsize frequencies in a map.
   */
  Map<Float, Integer> getFontsizeFrequencies();
}
