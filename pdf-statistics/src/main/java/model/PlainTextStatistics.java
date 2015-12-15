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
   * The fonstsize frequencies.
   */
  protected Map<Float, Integer> fontsizeFrequencies;

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
  public void setMostCommonFontColor(PdfColor mostCommonColor) {
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
}
