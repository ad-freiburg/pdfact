package model;

import de.freiburg.iif.counter.FloatCounter;
import de.freiburg.iif.counter.IntCounter;
import de.freiburg.iif.counter.ObjectCounter;

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
  public FloatCounter getFontsizesCounter();
  
  /**
   * Returns the font frequencies in a map.
   */
  public ObjectCounter<PdfFont> getFontsCounter();
  
  /**
   * Returns the color frequencies in a map.
   */
  public ObjectCounter<PdfColor> getColorsCounter();

  /**
   * Returns the ascii ratio.
   */
  public float getAsciiRatio();
  
  /**
   * Returns the ascii counter.
   */
  public IntCounter getAsciiCounter();
  
  /**
   * Returns the digits ratio.
   */
  public float getDigitsRatio();
  
  /**
   * Returns the digits counter.
   */
  public IntCounter getDigitsCounter();
}
