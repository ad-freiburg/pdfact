package statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.freiburg.iif.model.Rectangle;
import model.HasTextLineStatistics;
import model.PdfTextLine;
import model.PlainTextLineStatistics;
import model.TextLineStatistics;
import utils.math.MathUtil;

/**
 * Some statistics about text lines.
 *
 * @author Claudius Korzen
 *
 */
public class TextLineStatistician {
  /**
   * Computes text statistics for the given object.
   */
  public static TextLineStatistics compute(List<PdfTextLine> lines) {
    PlainTextLineStatistics statistics = new PlainTextLineStatistics();

    computeAverageValues(lines, statistics);
    computeMostCommonValues(lines, statistics);

    return statistics;
  }

  /**
   * Accumulates the given statistics to a single statistic.
   */
  public static TextLineStatistics accumulate(
      List<? extends HasTextLineStatistics> objs) {
    PlainTextLineStatistics statistics = new PlainTextLineStatistics();

    accumulateAverageValues(objs, statistics);
    accumulateMostCommonValues(objs, statistics);

    return statistics;
  }

  /**
   * Computes average statistic values for the given list of objects and stores
   * the values in the given statistics object.
   */
  protected static void computeAverageValues(List<PdfTextLine> lines,
      PlainTextLineStatistics statistics) {

    float sumLinePitches = 0;
    float sumLinePitchElements = 0;
    
    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine line = lines.get(i);
      PdfTextLine prevLine = (i > 0) ? lines.get(i - 1) : null;
      
      float linepitch = computeLinePitch(prevLine, line);
      if (linepitch > 0 && linepitch < Float.MAX_VALUE) {
        sumLinePitches += linepitch;
        sumLinePitchElements++;
      }
    }

    statistics.setAverageLinePitch(sumLinePitches / sumLinePitchElements);
  }
  
  /**
   * Computes average statistic values for the given list of objects and stores
   * the values in the given statistics object.
   */
  protected static void computeMostCommonValues(List<PdfTextLine> lines, 
      PlainTextLineStatistics statistics) {

    Map<Float, Integer> linepitchFreqs = new HashMap<Float, Integer>();

    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine line = lines.get(i);
      PdfTextLine prevLine = (i > 0) ? lines.get(i - 1) : null;
      
      float linepitch = computeLinePitch(prevLine, line);
      
      // Count the frequencies of line pitches.
      int count = 0;
      if (linepitchFreqs.containsKey(linepitch)) {
        count = linepitchFreqs.get(linepitch);
      }
      linepitchFreqs.put(linepitch, count + 1);
    }

    // Compute the most common line pitch.
    int maxFrequency = 0;
    float mostCommonLinePitch = 0;
    for (Entry<Float, Integer> entry : linepitchFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonLinePitch = entry.getKey();
      }
    }
    statistics.setMostCommonLinePitch(mostCommonLinePitch);
    statistics.setLinepitchFrequencies(linepitchFreqs);
  }
  
  // ___________________________________________________________________________

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateAverageValues(
      List<? extends HasTextLineStatistics> objs,
      PlainTextLineStatistics statistics) {

    float sumLinePitches = 0;
    for (HasTextLineStatistics obj : objs) {
      sumLinePitches += obj.getTextLineStatistics().getAverageLinePitch();
    }

    statistics.setAverageLinePitch(sumLinePitches / (float) objs.size());
  }

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateMostCommonValues(
      List<? extends HasTextLineStatistics> objs, 
      PlainTextLineStatistics stats) {
    
    Map<Float, Integer> linepitchFreqs = new HashMap<Float, Integer>();

    for (HasTextLineStatistics o : objs) {
      TextLineStatistics textLineStats = o.getTextLineStatistics();
      Map<Float, Integer> freq = textLineStats.getLinepitchFrequencies();
      // Accumulate the frequencies.
      for (Entry<Float, Integer> entry : freq.entrySet()) {
        int count = 0;
        float linepitch = entry.getKey();
        int linepitchFreq = entry.getValue();
        if (linepitchFreqs.containsKey(linepitch)) {
          count = linepitchFreqs.get(linepitch);
        }
        linepitchFreqs.put(linepitch, count + linepitchFreq);
      }
    }

    // Compute the most common fontsize.
    int maxFrequency = 0;
    float mostCommonLinepitch = 0;
    for (Entry<Float, Integer> entry : linepitchFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonLinepitch = entry.getKey();
      }
    }

    stats.setMostCommonLinePitch(mostCommonLinepitch);
    stats.setLinepitchFrequencies(linepitchFreqs);
  }
  
  /**
   * Computes the vertical pitch between the given two elements.
   */
  public static float computeLinePitch(PdfTextLine line1, PdfTextLine line2) {
    if (line1 == null || line2 == null) {
      return Float.MAX_VALUE;
    }
    
    Rectangle rect1 = line1.getRectangle();
    Rectangle rect2 = line2.getRectangle();
    
    if (rect1 == null || rect2 == null) {
      return Float.MAX_VALUE;
    }
    
    // Compute the pitch to the previous line and to the next line.
    float minY = rect1.getMinY();
    // float lineMinY = line != null ? line.getRectangle().getMinY() : 0;
    float maxY = rect2.getMaxY();
    
    return MathUtil.round(minY - maxY, 0);
  }
}
