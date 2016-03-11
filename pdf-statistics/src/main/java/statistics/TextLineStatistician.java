package statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Line;
import de.freiburg.iif.model.Rectangle;
import model.HasTextLineStatistics;
import model.PdfTextLine;
import model.PlainTextLineStatistics;
import model.TextLineStatistics;

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
  public static TextLineStatistics compute(List<? extends PdfTextLine> lines) {
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
  protected static void computeAverageValues(List<? extends PdfTextLine> lines,
      PlainTextLineStatistics statistics) {

    float sumLinePitches = 0;
    float sumLinePitchElements = 0;
    float sumBaselinePitches = 0;
    float sumBaselinePitchElements = 0;

    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine line = lines.get(i);
      PdfTextLine prevLine = (i > 0) ? lines.get(i - 1) : null;

      float linepitch = computeLinePitch(prevLine, line);
      float baselinePitch = computeBaseLinePitch(prevLine, line);

      if (linepitch > 0 && linepitch < Float.MAX_VALUE) {
        sumLinePitches += linepitch;
        sumLinePitchElements++;
      }

      if (baselinePitch > 0 && baselinePitch < Float.MAX_VALUE) {
        sumBaselinePitches += baselinePitch;
        sumBaselinePitchElements++;
      }
    }

    statistics.setAverageLinePitch(sumLinePitches / sumLinePitchElements);
    statistics
        .setAverageBaselinePitch(sumBaselinePitches / sumBaselinePitchElements);
  }

  /**
   * Computes average statistic values for the given list of objects and stores
   * the values in the given statistics object.
   */
  protected static void computeMostCommonValues(
      List<? extends PdfTextLine> lines, PlainTextLineStatistics statistics) {

    Map<Float, Integer> linepitchFreqs = new HashMap<Float, Integer>();
    Map<Float, Integer> baselinePitchFreqs = new HashMap<Float, Integer>();

    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine line = lines.get(i);
      PdfTextLine prevLine = (i > 0) ? lines.get(i - 1) : null;

      float linepitch = computeLinePitch(prevLine, line);
      float baselinePitch = computeBaseLinePitch(prevLine, line);

      // Count the frequencies of line pitches.
      int count = 0;
      if (linepitchFreqs.containsKey(linepitch)) {
        count = linepitchFreqs.get(linepitch);
      }
      linepitchFreqs.put(linepitch, count + 1);

      // Count the frequencies of line pitches.
      count = 0;
      if (baselinePitchFreqs.containsKey(baselinePitch)) {
        count = baselinePitchFreqs.get(baselinePitch);
      }
      baselinePitchFreqs.put(baselinePitch, count + 1);
    }

    // Compute the most common line pitch.
    int maxFrequency = 0;
    float mostCommonLinePitch = 0;
    float largestLinePitch = -Float.MAX_VALUE;
    float smallestLinePitch = Float.MAX_VALUE;
    for (Entry<Float, Integer> entry : linepitchFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonLinePitch = entry.getKey();
      }
      if (entry.getKey() > 0) {
        if (entry.getKey() > largestLinePitch) {
          largestLinePitch = entry.getKey();
        }
        if (entry.getKey() < smallestLinePitch) {
          smallestLinePitch = entry.getKey();
        }
      }
    }

    float significanceBorder = maxFrequency * (2 / 3f);
    float smallestSignificantLinePitch = Float.MAX_VALUE;
    for (Entry<Float, Integer> entry : linepitchFreqs.entrySet()) {
      if (entry.getValue() >= significanceBorder
          && entry.getKey() < smallestSignificantLinePitch) {
        smallestSignificantLinePitch = entry.getKey();
      }
    }

    // Compute the most common base line pitch.
    maxFrequency = 0;
    float mostCommonBaselinePitch = 0;
    float largestBaselinePitch = -Float.MAX_VALUE;
    float smallestBaseLinePitch = Float.MAX_VALUE;
    for (Entry<Float, Integer> entry : baselinePitchFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonBaselinePitch = entry.getKey();
      }
      if (entry.getKey() > 0) {
        if (entry.getKey() > largestBaselinePitch) {
          largestBaselinePitch = entry.getKey();
        }
        if (entry.getKey() < smallestBaseLinePitch) {
          smallestBaseLinePitch = entry.getKey();
        }
      }
    }

    significanceBorder = maxFrequency * (2 / 3f);
    float smallestSignificantBaselinePitch = Float.MAX_VALUE;
    for (Entry<Float, Integer> entry : baselinePitchFreqs.entrySet()) {
      if (entry.getValue() >= significanceBorder
          && entry.getKey() < smallestSignificantBaselinePitch) {
        smallestSignificantBaselinePitch = entry.getKey();
      }
    }

    statistics.setLargestLinePitch(largestLinePitch);
    statistics.setSmallestLinePitch(smallestLinePitch);
    statistics.setMostCommonLinePitch(mostCommonLinePitch);
    statistics.setLinepitchFrequencies(linepitchFreqs);
    statistics.setLargestBaselinePitch(largestBaselinePitch);
    statistics.setSmallestBaselinePitch(smallestBaseLinePitch);
    statistics.setMostCommonBaselinePitch(mostCommonBaselinePitch);
    statistics.setBaselinePitchFrequencies(baselinePitchFreqs);
    statistics.setSmallestSignificantLinePitch(smallestSignificantLinePitch);
    statistics
        .setSmallestSignificantBaselinePitch(smallestSignificantBaselinePitch);
  }

  // ___________________________________________________________________________

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateAverageValues(
      List<? extends HasTextLineStatistics> objs,
      PlainTextLineStatistics statistics) {

    float sumLinePitches = 0;
    float sumBaselinePitches = 0;
    for (HasTextLineStatistics obj : objs) {
      sumLinePitches += obj.getTextLineStatistics().getAverageLinePitch();
      sumBaselinePitches +=
          obj.getTextLineStatistics().getAverageBaselinePitch();
    }

    statistics.setAverageLinePitch(sumLinePitches / (float) objs.size());
    statistics
        .setAverageBaselinePitch(sumBaselinePitches / (float) objs.size());
  }

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateMostCommonValues(
      List<? extends HasTextLineStatistics> objs,
      PlainTextLineStatistics stats) {

    Map<Float, Integer> linepitchFreqs = new HashMap<Float, Integer>();
    Map<Float, Integer> baseLinepitchFreqs = new HashMap<Float, Integer>();

    for (HasTextLineStatistics o : objs) {
      TextLineStatistics textLineStats = o.getTextLineStatistics();
      Map<Float, Integer> freq = textLineStats.getLinepitchFrequencies();
      Map<Float, Integer> baseFreq =
          textLineStats.getBaselinePitchFrequencies();

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

      // Accumulate the frequencies.
      for (Entry<Float, Integer> entry : baseFreq.entrySet()) {
        int count = 0;
        float baseLinepitch = entry.getKey();
        int baseLinepitchFreq = entry.getValue();
        if (baseLinepitchFreqs.containsKey(baseLinepitch)) {
          count = baseLinepitchFreqs.get(baseLinepitch);
        }
        baseLinepitchFreqs.put(baseLinepitch, count + baseLinepitchFreq);
      }
    }

    int maxFrequency = 0;
    float mostCommonLinePitch = 0;
    float largestLinePitch = -Float.MAX_VALUE;
    float smallestLinePitch = Float.MAX_VALUE;
    for (Entry<Float, Integer> entry : linepitchFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonLinePitch = entry.getKey();
      }
      if (entry.getKey() > 0) {
        if (entry.getKey() > largestLinePitch) {
          largestLinePitch = entry.getKey();
        }
        if (entry.getKey() < smallestLinePitch) {
          smallestLinePitch = entry.getKey();
        }
      }
    }

    float significanceBorder = maxFrequency * (2 / 3f);
    float smallestSignificantLinePitch = Float.MAX_VALUE;
    for (Entry<Float, Integer> entry : linepitchFreqs.entrySet()) {
      if (entry.getValue() >= significanceBorder
          && entry.getKey() < smallestSignificantLinePitch) {
        smallestSignificantLinePitch = entry.getKey();
      }
    }

    maxFrequency = 0;
    float mostCommonBaselinePitch = 0;
    float largestBaselinePitch = -Float.MAX_VALUE;
    float smallestBaselinePitch = Float.MAX_VALUE;
    for (Entry<Float, Integer> entry : baseLinepitchFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonBaselinePitch = entry.getKey();
      }
      if (entry.getKey() > 0) {
        if (entry.getKey() > largestBaselinePitch) {
          largestBaselinePitch = entry.getKey();
        }
        if (entry.getKey() < smallestBaselinePitch) {
          smallestBaselinePitch = entry.getKey();
        }
      }
    }

    significanceBorder = maxFrequency * (2 / 3f);
    float smallestSignificantBaselinePitch = Float.MAX_VALUE;
    for (Entry<Float, Integer> entry : linepitchFreqs.entrySet()) {
      if (entry.getValue() >= significanceBorder
          && entry.getKey() < smallestSignificantBaselinePitch) {
        smallestSignificantBaselinePitch = entry.getKey();
      }
    }
    
    stats.setLargestLinePitch(largestLinePitch);
    stats.setSmallestLinePitch(smallestLinePitch);
    stats.setMostCommonLinePitch(mostCommonLinePitch);
    stats.setLinepitchFrequencies(linepitchFreqs);
    stats.setLargestBaselinePitch(largestBaselinePitch);
    stats.setSmallestBaselinePitch(smallestBaselinePitch);
    stats.setMostCommonBaselinePitch(mostCommonBaselinePitch);
    stats.setBaselinePitchFrequencies(baseLinepitchFreqs);
    stats.setSmallestSignificantLinePitch(smallestSignificantLinePitch);
    stats.setSmallestSignificantBaselinePitch(smallestSignificantBaselinePitch);
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

    return MathUtils.round(minY - maxY, 0);
  }

  /**
   * Computes the vertical pitch between the given two elements.
   */
  public static float computeBaseLinePitch(PdfTextLine line1,
      PdfTextLine line2) {
    if (line1 == null || line2 == null) {
      return Float.MAX_VALUE;
    }

    Line baseline1 = line1.getBaseLine();
    Line baseline2 = line2.getBaseLine();

    if (baseline1 == null || baseline2 == null) {
      return Float.MAX_VALUE;
    }

    // Compute the pitch to the previous line and to the next line.
    float minY = baseline1.getStartY();
    // float lineMinY = line != null ? line.getRectangle().getMinY() : 0;
    float maxY = baseline2.getStartY();

    return MathUtils.round(minY - maxY, 0);
  }
}
