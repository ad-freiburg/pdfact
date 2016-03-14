package model;

import de.freiburg.iif.counter.FloatCounter;
import de.freiburg.iif.counter.IntCounter;
import de.freiburg.iif.counter.ObjectCounter;

/**
 * A plain implementation of TextStatistics.
 *
 * @author Claudius Korzen
 *
 */
public class PlainTextStatistics implements TextStatistics {
  /**
   * The average fontsize.
   */
  protected float avgFontsize;
    
  /**
   * The most common fontsize.
   */
  protected float mostCommonFontsize;
    
  /**
   * The most common font.
   */
  protected PdfFont mostCommonFont;
  
  /**
   * The most common font color.
   */
  protected PdfColor mostCommonFontColor;
  
  /**
   * The fontsize frequencies.
   */
  protected FloatCounter fontsizeCounter;
  
  /**
   * The font frequencies.
   */
  protected ObjectCounter<PdfFont> fontsCounter;

  /**
   * The color frequencies.
   */
  protected ObjectCounter<PdfColor> colorsCounter;
  
  /**
   * The digits frequencies.
   */
  protected IntCounter digitsCounter;
  
  /**
   * The ascii counter.
   */
  protected IntCounter asciiCounter;
  
  /**
   * The ascii ratio.
   */
  protected float asciiRatio;
  
  /**
   * The digits ratio.
   */
  protected float digitsRatio;
  
  // ___________________________________________________________________________

  @Override
  public float getAverageFontsize() {
    return this.avgFontsize;
  }
 
  @Override
  public PdfFont getMostCommonFont() {
    return this.mostCommonFont;
  }
  
  @Override
  public PdfColor getMostCommonFontColor() {
    return this.mostCommonFontColor;
  }
  
  @Override
  public float getMostCommonFontsize() {
    return this.mostCommonFontsize;
  }

  @Override
  public FloatCounter getFontsizesCounter() {
    return this.fontsizeCounter;
  }
  
  @Override
  public ObjectCounter<PdfFont> getFontsCounter() {
    return this.fontsCounter;
  }
  
  @Override
  public ObjectCounter<PdfColor> getColorsCounter() {
    return this.colorsCounter;
  }

  @Override
  public float getAsciiRatio() {
    return this.asciiRatio;
  }

  @Override
  public float getDigitsRatio() {
    return this.digitsRatio;
  }
  
  @Override
  public IntCounter getAsciiCounter() {
    return this.asciiCounter;
  }

  @Override
  public IntCounter getDigitsCounter() {
    return this.digitsCounter;
  }
  
  // ___________________________________________________________________________

  /**
   * Sets the average fontsize.
   */
  public void setAvgFontsize(float avgFontsize) {
    this.avgFontsize = avgFontsize;
  }
  
  /**
   * Sets the most common font.
   */
  public void setMostCommonFont(PdfFont mostCommonFont) {
    this.mostCommonFont = mostCommonFont;
  }
    
  /**
   * Sets the most common font color.
   */
  public void setMostCommonColor(PdfColor mostCommonColor) {
    this.mostCommonFontColor = mostCommonColor;
  }
  
  /**
   * Sets the most common fontsize.
   */
  public void setMostCommonFontsize(float mostCommonFontsize) {
    this.mostCommonFontsize = mostCommonFontsize;
  }

  /**
   * Sets the fontsize frequencies.
   */
  public void setFontsizesCounter(FloatCounter freqs) {
    this.fontsizeCounter = freqs;
  }
  
  /**
   * Sets the font frequencies.
   */
  public void setFontsCounter(ObjectCounter<PdfFont> freqs) {
    this.fontsCounter = freqs;
  }

  /**
   * Sets the color frequencies.
   */
  public void setColorsCounter(ObjectCounter<PdfColor> freqs) {
    this.colorsCounter = freqs;
  }
  
  /**
   * Sets the ascii ratio.
   */
  public void setAsciiRatio(float asciiRatio) {
    this.asciiRatio = asciiRatio;
  }

  /**
   * Sets the digits ratio.
   */
  public void setDigitsRatio(float digitsRatio) {
    this.digitsRatio = digitsRatio;
  }
  
  /**
   * Sets the ascii counter.
   */
  public void setAsciiCounter(IntCounter asciiCounter) {
    this.asciiCounter = asciiCounter;
  }

  /**
   * Sets the digits ratio.
   */
  public void setDigitsCounter(IntCounter digitsCounter) {
    this.digitsCounter = digitsCounter;
  }
}
