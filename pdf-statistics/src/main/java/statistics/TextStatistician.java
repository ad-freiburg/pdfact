package statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.HasText;
import model.HasTextStatistics;
import model.PdfColor;
import model.PdfFont;
import model.PlainTextStatistics;
import model.TextStatistics;
import utils.math.MathUtil;

/**
 * Some statistics about the text of an object.
 * 
 * @author Claudius Korzen
 */
public class TextStatistician {
  /**
   * Computes text statistics for the given object.
   */
  public static TextStatistics compute(HasText obj) {
    return compute(Arrays.asList(obj));
  }

  /**
   * Computes text statistics for the given list of objects.
   */
  public static TextStatistics compute(List<? extends HasText> objs) {
    PlainTextStatistics statistics = new PlainTextStatistics();

    computeAverageValues(objs, statistics);
    computeMostCommonValues(objs, statistics);

    return statistics;
  }

  /**
   * Accumulates the given statistics to a single statistic.
   */
  public static TextStatistics accumulate(
      List<? extends HasTextStatistics> objs) {
    PlainTextStatistics statistics = new PlainTextStatistics();

    accumulateAverageValues(objs, statistics);
    accumulateMostCommonValues(objs, statistics);

    return statistics;
  }

  /**
   * Computes average statistic values for the given list of objects and stores
   * the values in the given statistics object.
   */
  protected static void computeAverageValues(List<? extends HasText> objs,
      PlainTextStatistics statistics) {

    float sumFontsizes = 0;
    for (HasText obj : objs) {
      sumFontsizes += obj.getFontsize();
    }

    statistics.setAvgFontsize(sumFontsizes / (float) objs.size());
  }

  /**
   * Computes average statistic values for the given list of objects and stores
   * the values in the given statistics object.
   */
  protected static void computeMostCommonValues(
      List<? extends HasText> objs, PlainTextStatistics statistics) {

    Map<Float, Integer> fontsizeFreqs = new HashMap<Float, Integer>();
    Map<PdfFont, Integer> fontFreqs = new HashMap<PdfFont, Integer>();
    Map<PdfColor, Integer> colorFreqs = new HashMap<PdfColor, Integer>();

    for (HasText obj : objs) {
      float fontsize = MathUtil.round(obj.getFontsize(), 1);
      PdfFont font = obj.getFont();
      PdfColor color = obj.getColor();

      // Count the frequencies of fontsizes.
      int count = 0;
      if (fontsizeFreqs.containsKey(fontsize)) {
        count = fontsizeFreqs.get(fontsize);
      }
      fontsizeFreqs.put(fontsize, count + 1);
      
      // Count the frequencies of fonts.
      count = 0;
      if (fontFreqs.containsKey(font)) {
        count = fontFreqs.get(font);
      }
      fontFreqs.put(font, count + 1);
      
      // Count the frequencies of colors.
      count = 0;
      if (colorFreqs.containsKey(color)) {
        count = colorFreqs.get(color);
      }
      colorFreqs.put(color, count + 1);
    }

    // Compute the most common fontsize.
    int maxFrequency = 0;
    float mostCommonFontsize = 0;
    for (Entry<Float, Integer> entry : fontsizeFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonFontsize = entry.getKey();
      }
    }
    statistics.setMostCommonFontsize(mostCommonFontsize);
    statistics.setFontsizeFrequencies(fontsizeFreqs);
    
    // Compute the most common font.
    maxFrequency = 0;
    PdfFont mostCommonFont = null;
    for (Entry<PdfFont, Integer> entry : fontFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonFont = entry.getKey();
      }
    }
    statistics.setMostCommonFont(mostCommonFont);
    
    // Compute the most common color.
    maxFrequency = 0;
    PdfColor mostCommonColor = null;
    for (Entry<PdfColor, Integer> entry : colorFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonColor = entry.getKey();
      }
    }
    statistics.setMostCommonFontColor(mostCommonColor);
  }
  
  // ___________________________________________________________________________

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateAverageValues(
      List<? extends HasTextStatistics> objs,
      PlainTextStatistics statistics) {

    float sumFontsizes = 0;
    for (HasTextStatistics obj : objs) {
      sumFontsizes += obj.getTextStatistics().getAverageFontsize();
    }

    statistics.setAvgFontsize(sumFontsizes / (float) objs.size());
  }

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateMostCommonValues(
      List<? extends HasTextStatistics> objs, PlainTextStatistics stats) {
    Map<Float, Integer> fontsizeFreqs = new HashMap<Float, Integer>();

    for (HasTextStatistics o : objs) {
      Map<Float, Integer> freq = o.getTextStatistics().getFontsizeFrequencies();
      // Accumulate the frequencies.
      for (Entry<Float, Integer> entry : freq.entrySet()) {
        int count = 0;
        float fontsize = entry.getKey();
        int fontsizeFreq = entry.getValue();
        if (fontsizeFreqs.containsKey(fontsize)) {
          count = fontsizeFreqs.get(fontsize);
        }
        fontsizeFreqs.put(fontsize, count + fontsizeFreq);
      }
    }

    // Compute the most common fontsize.
    int maxFrequency = 0;
    float mostCommonFontsize = 0;
    for (Entry<Float, Integer> entry : fontsizeFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonFontsize = entry.getKey();
      }
    }

    stats.setMostCommonFontsize(mostCommonFontsize);
    stats.setFontsizeFrequencies(fontsizeFreqs);
  }
}
