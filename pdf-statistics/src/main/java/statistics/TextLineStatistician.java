package statistics;

import java.util.List;

import de.freiburg.iif.counter.FloatCounter;
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
    PlainTextLineStatistics stats = new PlainTextLineStatistics();

    FloatCounter linePitchesCounter = new FloatCounter();
    FloatCounter basePitchesCounter = new FloatCounter();
    
    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine line = lines.get(i);
      PdfTextLine prevLine = (i > 0) ? lines.get(i - 1) : null;

      float linepitch = computeLinePitch(prevLine, line);
      float baselinePitch = computeBaseLinePitch(prevLine, line);

      if (linepitch < Float.MAX_VALUE) {
        linePitchesCounter.add(linepitch);
      }
      if (baselinePitch < Float.MAX_VALUE) {
        basePitchesCounter.add(baselinePitch);
      }
    }

    stats.setAverageLinePitch(linePitchesCounter.getAverageValue());
    stats.setAverageBaselinePitch(basePitchesCounter.getAverageValue());

    stats.setLargestLinePitch(linePitchesCounter.getLargestFloat());
    stats.setSmallestLinePitch(linePitchesCounter.getSmallestFloat());
    stats.setMostCommonLinePitch(linePitchesCounter.getMostFrequentFloat());
    stats.setLinepitchesCounter(linePitchesCounter);
    stats.setLargestBaselinePitch(basePitchesCounter.getLargestFloat());
    stats.setSmallestBaselinePitch(basePitchesCounter.getSmallestFloat());
    stats.setMostCommonBaselinePitch(basePitchesCounter.getMostFrequentFloat());
    stats.setBaselinePitchesCounter(basePitchesCounter);
    
    float freq = linePitchesCounter.getMostFrequentFloatCount();
    int significanceBorder = (int) ((2 / 3f) * freq);
    stats.setSmallestSignificantLinePitch(
        linePitchesCounter.getSmallestFloatOccuringAtLeast(significanceBorder));
    
    freq = basePitchesCounter.getMostFrequentFloatCount();
    significanceBorder = (int) ((2 / 3f) * freq);
    stats.setSmallestSignificantBaselinePitch(basePitchesCounter
        .getSmallestFloatOccuringAtLeast(significanceBorder));
    
    return stats;
  }

  /**
   * Accumulates the given statistics to a single statistic.
   */
  public static TextLineStatistics accumulate(
      List<? extends HasTextLineStatistics> objs) {
    PlainTextLineStatistics stats = new PlainTextLineStatistics();

    FloatCounter linePitchesCounter = new FloatCounter();
    FloatCounter basePitchesCounter = new FloatCounter();

    for (HasTextLineStatistics o : objs) {
      TextLineStatistics textLineStats = o.getTextLineStatistics();
      FloatCounter freq = textLineStats.getLinepitchesCounter();
      FloatCounter baseFreq = textLineStats.getBaselinePitchesCounter();

      linePitchesCounter.add(freq);
      baseFreq.add(baseFreq);
    }
    
    stats.setAverageLinePitch(linePitchesCounter.getAverageValue());
    stats.setAverageBaselinePitch(basePitchesCounter.getAverageValue());
    
    stats.setLargestLinePitch(linePitchesCounter.getLargestFloat());
    stats.setSmallestLinePitch(linePitchesCounter.getSmallestFloat());
    stats.setMostCommonLinePitch(linePitchesCounter.getMostFrequentFloat());
    stats.setLinepitchesCounter(linePitchesCounter);
    stats.setLargestBaselinePitch(basePitchesCounter.getLargestFloat());
    stats.setSmallestBaselinePitch(basePitchesCounter.getSmallestFloat());
    stats.setMostCommonBaselinePitch(basePitchesCounter.getMostFrequentFloat());
    stats.setBaselinePitchesCounter(basePitchesCounter);
    
    float freq = linePitchesCounter.getMostFrequentFloatCount();
    int significanceBorder = (int) ((2 / 3f) * freq);
    stats.setSmallestSignificantLinePitch(
        linePitchesCounter.getSmallestFloatOccuringAtLeast(significanceBorder));
    
    freq = basePitchesCounter.getMostFrequentFloatCount();
    significanceBorder = (int) ((2 / 3f) * freq);
    stats.setSmallestSignificantBaselinePitch(basePitchesCounter
        .getSmallestFloatOccuringAtLeast(significanceBorder));
    
    return stats;
  }

  // ___________________________________________________________________________
  
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

    String string1 = line1.getUnicode();
    String string2 = line2.getUnicode();
    
    if (string1 == null || string2 == null) {
      return Float.MAX_VALUE;
    }
    
    // Don't take too short lines into account. 
    if (string1.length() < 10 && string2.length() < 10) {
      return Float.MAX_VALUE;
    }
    
    // Compute the pitch to the previous line and to the next line.
    float minY = rect1.getMinY();
    // float lineMinY = line != null ? line.getRectangle().getMinY() : 0;
    float maxY = rect2.getMaxY();

    return Math.abs(MathUtils.round(minY - maxY, 0));
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

    String string1 = line1.getUnicode();
    String string2 = line2.getUnicode();
    
    if (string1 == null || string2 == null) {
      return Float.MAX_VALUE;
    }
    
    // Don't take too short lines into account. 
    if (string1.length() < 10 && string2.length() < 10) {
      return Float.MAX_VALUE;
    }
    
    // Compute the pitch to the previous line and to the next line.
    float minY = baseline1.getStartY();
    // float lineMinY = line != null ? line.getRectangle().getMinY() : 0;
    float maxY = baseline2.getStartY();

    return Math.abs(MathUtils.round(minY - maxY, 0));
  }
}
