package icecite.models.plain;

import icecite.models.PdfCharacterStatistic;
import icecite.models.PdfColor;
import icecite.models.PdfFontFace;
import icecite.utils.counter.FloatCounter;
import icecite.utils.counter.ObjectCounter;

// TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfCharacterStatistic}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacterStatistic implements PdfCharacterStatistic {
  /**
   * The height frequencies.
   */
  protected FloatCounter heightFrequencies;

  /**
   * The width frequencies.
   */
  protected FloatCounter widthFrequencies;

  /**
   * The color frequencies.
   */
  protected ObjectCounter<PdfColor> colorFrequencies;

  /**
   * The font face frequencies.
   */
  protected ObjectCounter<PdfFontFace> fontFaceFrequencies;

  /**
   * The font size frequencies.
   */
  protected FloatCounter fontsizeFrequencies;

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
  public ObjectCounter<PdfFontFace> getFontFaceFrequencies() {
    return this.fontFaceFrequencies;
  }

  @Override
  public void setFontFaceFrequencies(ObjectCounter<PdfFontFace> freqs) {
    this.fontFaceFrequencies = freqs;
  }

  // ==========================================================================

  @Override
  public PdfFontFace getMostCommonFontFace() {
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
  public ObjectCounter<PdfColor> getColorFrequencies() {
    return this.colorFrequencies;
  }

  @Override
  public void setColorFrequencies(ObjectCounter<PdfColor> freqs) {
    this.colorFrequencies = freqs;
  }

  // ==========================================================================

  @Override
  public PdfColor getMostCommonColor() {
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
}
