package pdfact.core.model;

import pdfact.core.util.counter.FloatCounter;
import pdfact.core.util.counter.ObjectCounter;

/**
 * A statistic about a collection of characters.
 * 
 * @author Claudius Korzen
 */
public class CharacterStatistic {
  /**
   * The height frequencies.
   */
  protected FloatCounter heightFrequencies;

  /**
   * The width frequencies.
   */
  protected FloatCounter widthFrequencies;

  /**
   * The font face frequencies.
   */
  protected ObjectCounter<FontFace> fontFaceFrequencies;

  /**
   * The font size frequencies.
   */
  protected FloatCounter fontsizeFrequencies;

  /**
   * The color frequencies.
   */
  protected ObjectCounter<Color> colorFrequencies;

  /**
   * The smallest minX value.
   */
  protected float smallestMinX = Float.MAX_VALUE;

  /**
   * The smallest minY value.
   */
  protected float smallestMinY = Float.MAX_VALUE;

  /**
   * The largest maxX value.
   */
  protected float largestMaxX = -Float.MAX_VALUE;

  /**
   * The largest maxY value.
   */
  protected float largestMaxY = -Float.MAX_VALUE;

  // ==============================================================================================

  /**
   * Returns the height frequencies of the characters.
   * 
   * @return The height frequencies of the characters.
   */
  public FloatCounter getHeightFrequencies() {
    return this.heightFrequencies;
  }

  /**
   * Sets the height frequencies of the characters.
   * 
   * @param freqs The height frequencies of the characters.
   */
  public void setHeightFrequencies(FloatCounter freqs) {
    this.heightFrequencies = freqs;
  }

  // ==============================================================================================

  /**
   * Returns the most common height of the characters.
   * 
   * @return The most common height of the characters.
   */
  public float getMostCommonHeight() {
    if (this.heightFrequencies == null) {
      return Float.NaN;
    }
    return this.heightFrequencies.getMostCommonFloat();
  }

  /**
   * Returns the average height of the characters.
   * 
   * @return The average height of the characters.
   */
  public float getAverageHeight() {
    if (this.heightFrequencies == null) {
      return Float.NaN;
    }
    return this.heightFrequencies.getAverageFloat();
  }

  // ==============================================================================================

  /**
   * Returns the width frequencies of the characters.
   * 
   * @return The width frequencies of the characters.
   */
  public FloatCounter getWidthFrequencies() {
    return this.widthFrequencies;
  }

  /**
   * Sets the width frequencies of the characters.
   * 
   * @param freqs The width frequencies of the characters.
   */
  public void setWidthFrequencies(FloatCounter freqs) {
    this.widthFrequencies = freqs;
  }

  // ==============================================================================================

  /**
   * Returns the most common width of the characters.
   * 
   * @return The most common width of the characters.
   */
  public float getMostCommonWidth() {
    if (this.widthFrequencies == null) {
      return Float.NaN;
    }
    return this.widthFrequencies.getMostCommonFloat();
  }

  /**
   * Returns the average width of the characters.
   * 
   * @return The average width of the characters.
   */
  public float getAverageWidth() {
    if (this.widthFrequencies == null) {
      return Float.NaN;
    }
    return this.widthFrequencies.getAverageFloat();
  }

  // ==============================================================================================

  /**
   * Returns the font face frequencies of the characters.
   * 
   * @return The font face frequencies of the characters.
   */
  public ObjectCounter<FontFace> getFontFaceFrequencies() {
    return this.fontFaceFrequencies;
  }

  /**
   * Sets the font face frequencies of the characters.
   * 
   * @param freqs The font face frequencies of the characters.
   */
  public void setFontFaceFrequencies(ObjectCounter<FontFace> freqs) {
    this.fontFaceFrequencies = freqs;
  }

  // ==============================================================================================

  /**
   * Returns the most common font face of the characters.
   * 
   * @return The most common font face of the characters.
   */
  public FontFace getMostCommonFontFace() {
    if (this.fontFaceFrequencies == null) {
      return null;
    }
    return this.fontFaceFrequencies.getMostCommonObject();
  }

  // ==============================================================================================

  /**
   * Returns the font size frequencies of the characters.
   * 
   * @return The font size frequencies of the characters.
   */
  public FloatCounter getFontSizeFrequencies() {
    return this.fontsizeFrequencies;
  }

  /**
   * Sets the font size frequencies of the characters.
   * 
   * @param freqs The font size frequencies of the characters.
   */
  public void setFontSizeFrequencies(FloatCounter freqs) {
    this.fontsizeFrequencies = freqs;
  }

  // ==============================================================================================

  /**
   * Returns the average font size of the characters.
   * 
   * @return The average font size of the characters.
   */
  public float getAverageFontsize() {
    if (this.fontsizeFrequencies == null) {
      return Float.NaN;
    }
    return this.fontsizeFrequencies.getAverageFloat();
  }

  // ==============================================================================================

  /**
   * Returns the color frequencies of the characters.
   * 
   * @return The color frequencies of the characters.
   */
  public ObjectCounter<Color> getColorFrequencies() {
    return this.colorFrequencies;
  }

  /**
   * Sets the color frequencies of the characters.
   * 
   * @param freqs The color frequencies of the characters.
   */
  public void setColorFrequencies(ObjectCounter<Color> freqs) {
    this.colorFrequencies = freqs;
  }
  // ==============================================================================================

  /**
   * Returns the most common color of the characters.
   * 
   * @return The most common color of the characters.
   */
  public Color getMostCommonColor() {
    if (this.colorFrequencies == null) {
      return null;
    }
    return this.colorFrequencies.getMostCommonObject();
  }

  // ==============================================================================================

  /**
   * Returns the smallest minX value of the characters.
   * 
   * @return The smallest minX value of the characters.
   */
  public float getSmallestMinX() {
    return this.smallestMinX;
  }

  /**
   * Sets the smallest minX value of the characters.
   * 
   * @param minX The smallest minX value of the characters.
   */
  public void setSmallestMinX(float minX) {
    this.smallestMinX = minX;
  }

  // ==============================================================================================

  /**
   * Returns the smallest minY value of the characters.
   * 
   * @return The smallest minY value of the characters.
   */
  public float getSmallestMinY() {
    return this.smallestMinY;
  }

  /**
   * Sets the smallest minY value of the characters.
   * 
   * @param minY The smallest minY value of the characters.
   */
  public void setSmallestMinY(float minY) {
    this.smallestMinY = minY;
  }

  // ==============================================================================================

  /**
   * Returns the largest maxX value of the characters.
   * 
   * @return The largest maxX value of the characters.
   */
  public float getLargestMaxX() {
    return this.largestMaxX;
  }

  /**
   * Sets the largest maxX value of the characters.
   * 
   * @param maxX The largest maxX value of the characters.
   */
  public void setLargestMaxX(float maxX) {
    this.largestMaxX = maxX;
  }

  // ==============================================================================================

  /**
   * Returns the largest maxY value of the characters.
   * 
   * @return The largest maxY value of the characters.
   */
  public float getLargestMaxY() {
    return this.largestMaxY;
  }

  /**
   * Sets the largest maxY value of the characters.
   * 
   * @param maxY The largest maxY value of the characters.
   */
  public void setLargestMaxY(float maxY) {
    this.largestMaxY = maxY;
  }
}
