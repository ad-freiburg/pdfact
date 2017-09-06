package pdfact.core.model.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Color;
import pdfact.core.model.FontFace;
import pdfact.core.util.counter.FloatCounter;
import pdfact.core.util.counter.ObjectCounter;

/**
 * A plain implementation of {@link CharacterStatistic}.
 * 
 * @author Claudius Korzen
 */
public class PlainCharacterStatistic implements CharacterStatistic {
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

  // ==========================================================================

  @Override
  public FloatCounter getHeightFrequencies() {
    return this.heightFrequencies;
  }

  @Override
  public void setHeightFrequencies(FloatCounter freqs) {
    this.heightFrequencies = freqs;
  }

  // ==========================================================================

  @Override
  public float getMostCommonHeight() {
    if (this.heightFrequencies == null) {
      return Float.NaN;
    }
    return this.heightFrequencies.getMostCommonFloat();
  }

  @Override
  public float getAverageHeight() {
    if (this.heightFrequencies == null) {
      return Float.NaN;
    }
    return this.heightFrequencies.getAverageFloat();
  }

  // ==========================================================================

  @Override
  public FloatCounter getWidthFrequencies() {
    return this.widthFrequencies;
  }

  @Override
  public void setWidthFrequencies(FloatCounter freqs) {
    this.widthFrequencies = freqs;
  }

  // ==========================================================================

  @Override
  public float getMostCommonWidth() {
    if (this.widthFrequencies == null) {
      return Float.NaN;
    }
    return this.widthFrequencies.getMostCommonFloat();
  }

  @Override
  public float getAverageWidth() {
    if (this.widthFrequencies == null) {
      return Float.NaN;
    }
    return this.widthFrequencies.getAverageFloat();
  }

  // ==========================================================================

  @Override
  public ObjectCounter<FontFace> getFontFaceFrequencies() {
    return this.fontFaceFrequencies;
  }

  @Override
  public void setFontFaceFrequencies(ObjectCounter<FontFace> freqs) {
    this.fontFaceFrequencies = freqs;
  }

  // ==========================================================================

  @Override
  public FontFace getMostCommonFontFace() {
    if (this.fontFaceFrequencies == null) {
      return null;
    }
    return this.fontFaceFrequencies.getMostCommonObject();
  }

  // ==========================================================================

  @Override
  public FloatCounter getFontSizeFrequencies() {
    return this.fontsizeFrequencies;
  }

  @Override
  public void setFontSizeFrequencies(FloatCounter freqs) {
    this.fontsizeFrequencies = freqs;
  }

  // ==========================================================================

  @Override
  public float getAverageFontsize() {
    if (this.fontsizeFrequencies == null) {
      return Float.NaN;
    }
    return this.fontsizeFrequencies.getAverageFloat();
  }

  // ==========================================================================

  @Override
  public ObjectCounter<Color> getColorFrequencies() {
    return this.colorFrequencies;
  }

  @Override
  public void setColorFrequencies(ObjectCounter<Color> freqs) {
    this.colorFrequencies = freqs;
  }

  // ==========================================================================

  @Override
  public Color getMostCommonColor() {
    if (this.colorFrequencies == null) {
      return null;
    }
    return this.colorFrequencies.getMostCommonObject();
  }

  // ==========================================================================

  @Override
  public float getSmallestMinX() {
    return this.smallestMinX;
  }

  @Override
  public void setSmallestMinX(float minX) {
    this.smallestMinX = minX;
  }

  // ==========================================================================

  @Override
  public float getSmallestMinY() {
    return this.smallestMinY;
  }

  @Override
  public void setSmallestMinY(float minY) {
    this.smallestMinY = minY;
  }

  // ==========================================================================

  @Override
  public float getLargestMaxX() {
    return this.largestMaxX;
  }

  @Override
  public void setLargestMaxX(float maxX) {
    this.largestMaxX = maxX;
  }

  // ==========================================================================

  @Override
  public float getLargestMaxY() {
    return this.largestMaxY;
  }

  @Override
  public void setLargestMaxY(float maxY) {
    this.largestMaxY = maxY;
  }

  // ==========================================================================

  @Override
  public boolean equals(Object o) {
    if (o instanceof CharacterStatistic) {
      CharacterStatistic other = (CharacterStatistic) o;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getHeightFrequencies(), other.getHeightFrequencies());
      builder.append(getWidthFrequencies(), other.getWidthFrequencies());
      builder.append(getFontFaceFrequencies(), other.getFontFaceFrequencies());
      builder.append(getFontSizeFrequencies(), other.getFontSizeFrequencies());
      builder.append(getColorFrequencies(), other.getColorFrequencies());
      builder.append(getSmallestMinX(), other.getSmallestMinX());
      builder.append(getSmallestMinY(), other.getSmallestMinY());
      builder.append(getLargestMaxX(), other.getLargestMaxX());
      builder.append(getLargestMaxY(), other.getLargestMaxY());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getHeightFrequencies());
    builder.append(getWidthFrequencies());
    builder.append(getFontFaceFrequencies());
    builder.append(getFontSizeFrequencies());
    builder.append(getColorFrequencies());
    builder.append(getSmallestMinX());
    builder.append(getSmallestMinY());
    builder.append(getLargestMaxX());
    builder.append(getLargestMaxY());
    return builder.hashCode();
  }
}
