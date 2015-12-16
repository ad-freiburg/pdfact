package statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.HasRectangle;
import model.DimensionStatistics;
import model.HasDimensionStatistics;
import model.PlainDimensionStatistics;

/**
 * Some statistics about the dimensions of an object.
 * 
 * @author Claudius Korzen
 */
public class DimensionStatistician {
  /**
   * Computes text statistics for the given object.
   */
  public static DimensionStatistics compute(HasRectangle obj) {
    return compute(Arrays.asList(obj));
  }

  /**
   * Computes dimension statistics for the given list of objects.
   */
  public static DimensionStatistics compute(
      List<? extends HasRectangle> objects) {
    PlainDimensionStatistics statistics = new PlainDimensionStatistics();

    computeAverageValues(objects, statistics);
    computeMostCommonValues(objects, statistics);

    return statistics;
  }

  /**
   * Accumulates the given statistics to a single statistic.
   */
  public static DimensionStatistics accumulate(
      List<? extends HasDimensionStatistics> objs) {
    PlainDimensionStatistics statistics = new PlainDimensionStatistics();

    accumulateAverageValues(objs, statistics);
    accumulateMostCommonValues(objs, statistics);

    return statistics;
  }
  
  /**
   * Computes average statistic values for the given list of objects and stores
   * the values in the given statistics object.
   */
  protected static void computeAverageValues(
      List<? extends HasRectangle> objects,
      PlainDimensionStatistics statistics) {

    float sumHeights = 0;
    float sumWidths = 0;
    
    for (HasRectangle obj : objects) {
      sumHeights += obj.getRectangle().getHeight();
      sumWidths += obj.getRectangle().getWidth();
    }

    statistics.setAverageHeight(sumHeights / (float) objects.size());
    statistics.setAverageWidth(sumWidths / (float) objects.size());
  }

  /**
   * Computes average statistic values for the given list of objects and stores
   * the values in the given statistics object.
   */
  protected static void computeMostCommonValues(
      List<? extends HasRectangle> objects,
      PlainDimensionStatistics statistics) {

    Map<Float, Integer> heightFreqs = new HashMap<Float, Integer>();
    Map<Float, Integer> widthFreqs = new HashMap<Float, Integer>();

    for (HasRectangle obj : objects) {      
      float height = MathUtils.round(obj.getRectangle().getHeight(), 1);
      float width = MathUtils.round(obj.getRectangle().getWidth(), 1);
      
      // Count the frequencies of heights.
      int count = 0;
      if (heightFreqs.containsKey(height)) {
        count = heightFreqs.get(height);
      }
      heightFreqs.put(height, count + 1);

      // Count the frequencies of widths.
      count = 0;
      if (widthFreqs.containsKey(width)) {
        count = widthFreqs.get(width);
      }
      widthFreqs.put(width, count + 1);
    }

    // Compute the most common height.
    int maxFrequency = 0;
    for (Entry<Float, Integer> entry : heightFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        statistics.setMostCommonHeight(entry.getKey());
      }
    }

    // Compute the most common width.
    maxFrequency = 0;
    for (Entry<Float, Integer> entry : widthFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        statistics.setMostCommonWidth(entry.getKey());
      }
    }
        
    statistics.setHeightFrequencies(heightFreqs);
    statistics.setWidthFrequencies(widthFreqs);
  }

  // ___________________________________________________________________________

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateAverageValues(
      List<? extends HasDimensionStatistics> objs,
      PlainDimensionStatistics statistics) {

    float sumHeights = 0;
    float sumWidths = 0;
    for (HasDimensionStatistics obj : objs) {
      sumHeights += obj.getDimensionStatistics().getAverageHeight();
      sumWidths += obj.getDimensionStatistics().getAverageWidth();
    }

    float numObjs = (float) objs.size();
    statistics.setAverageHeight(sumHeights / numObjs);
    statistics.setAverageWidth(sumWidths / numObjs);
  }

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateMostCommonValues(
      List<? extends HasDimensionStatistics> objs,
      PlainDimensionStatistics stats) {
    Map<Float, Integer> heightFreqs = new HashMap<>();
    Map<Float, Integer> widthFreqs = new HashMap<>();

    for (HasDimensionStatistics o : objs) {
      DimensionStatistics dimStats = o.getDimensionStatistics();
      Map<Float, Integer> freq = dimStats.getHeightFrequencies();
      // Accumulate the frequencies.
      for (Entry<Float, Integer> entry : freq.entrySet()) {
        int count = 0;
        float height = entry.getKey();
        int heightFreq = entry.getValue();
        if (heightFreqs.containsKey(height)) {
          count = heightFreqs.get(height);
        }
        heightFreqs.put(height, count + heightFreq);
      }

      freq = dimStats.getWidthFrequencies();
      // Accumulate the frequencies.
      for (Entry<Float, Integer> entry : freq.entrySet()) {
        int count = 0;
        float width = entry.getKey();
        int widthFreq = entry.getValue();
        if (widthFreqs.containsKey(width)) {
          count = widthFreqs.get(width);
        }
        widthFreqs.put(width, count + widthFreq);
      }
    }

    // Compute the most common height.
    int maxFrequency = 0;
    float mostCommonHeight = 0;
    for (Entry<Float, Integer> entry : heightFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonHeight = entry.getKey();
      }
    }

    // Compute the most common width.
    maxFrequency = 0;
    float mostCommonWidth = 0;
    for (Entry<Float, Integer> entry : widthFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        mostCommonWidth = entry.getKey();
      }
    }

    stats.setMostCommonHeight(mostCommonHeight);
    stats.setHeightFrequencies(heightFreqs);
    stats.setMostCommonWidth(mostCommonWidth);
    stats.setWidthFrequencies(widthFreqs);
  }

}
