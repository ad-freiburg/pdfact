package model;

import java.util.Map;

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
  protected Map<Float, Integer> fontsizeFrequencies;
  
  /**
   * The font frequencies.
   */
  protected Map<PdfFont, Integer> fontFrequencies;

  /**
   * The color frequencies.
   */
  protected Map<PdfColor, Integer> colorFrequencies;
  
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
  public Map<Float, Integer> getFontsizeFrequencies() {
    return this.fontsizeFrequencies;
  }
  
  @Override
  public Map<PdfFont, Integer> getFontFrequencies() {
    return this.fontFrequencies;
  }
  
  @Override
  public Map<PdfColor, Integer> getColorFrequencies() {
    return this.colorFrequencies;
  }

  @Override
  public float getAsciiRatio() {
    return this.asciiRatio;
  }

  @Override
  public float getDigitsRatio() {
    return this.digitsRatio;
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
  public void setFontsizeFrequencies(Map<Float, Integer> freqs) {
    this.fontsizeFrequencies = freqs;
  }
  
  /**
   * Sets the font frequencies.
   */
  public void setFontFrequencies(Map<PdfFont, Integer> freqs) {
    this.fontFrequencies = freqs;
  }

  /**
   * Sets the color frequencies.
   */
  public void setColorFrequencies(Map<PdfColor, Integer> freqs) {
    this.colorFrequencies = freqs;
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
}
