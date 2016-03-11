package statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.HasRectangle;
import model.PlainPositionStatistics;
import model.PositionStatistics;

/**
 * Some statistics about the positions of an object.
 * 
 * @author Claudius Korzen
 */
public class PositionStatistician {
  /**
   * Computes position statistics for the given object.
   */
  public static PositionStatistics compute(HasRectangle obj) {
    return compute(Arrays.asList(obj));
  }

  /**
   * Computes position statistics for the given list of objects.
   */
  public static PositionStatistics compute(
      List<? extends HasRectangle> objects) {
    PlainPositionStatistics statistics = new PlainPositionStatistics();

    computeMostCommonValues(objects, statistics);

    return statistics;
  }
  
  /**
   * Computes average statistic values for the given list of objects and stores
   * the values in the given statistics object.
   */
  protected static void computeMostCommonValues(
      List<? extends HasRectangle> objects,
      PlainPositionStatistics statistics) {

    Map<Float, Integer> minXFreqs = new HashMap<Float, Integer>();
    Map<Float, Integer> minYFreqs = new HashMap<Float, Integer>();
    Map<Float, Integer> maxXFreqs = new HashMap<Float, Integer>();
    Map<Float, Integer> maxYFreqs = new HashMap<Float, Integer>();

    for (HasRectangle obj : objects) {      
      float minX = MathUtils.round(obj.getRectangle().getMinX(), 1);
      float minY = MathUtils.round(obj.getRectangle().getMinY(), 1);
      float maxX = MathUtils.round(obj.getRectangle().getMaxX(), 1);
      float maxY = MathUtils.round(obj.getRectangle().getMaxY(), 1);
      
      // Count the frequencies of minX.
      int count = 0;
      if (minXFreqs.containsKey(minX)) {
        count = minXFreqs.get(minX);
      }
      minXFreqs.put(minX, count + 1);

      // Count the frequencies of minY.
      count = 0;
      if (minYFreqs.containsKey(minY)) {
        count = minYFreqs.get(minY);
      }
      minYFreqs.put(minY, count + 1);
      
      // Count the frequencies of maxX.
      count = 0;
      if (maxXFreqs.containsKey(maxX)) {
        count = maxXFreqs.get(maxX);
      }
      maxXFreqs.put(maxX, count + 1);
      
      // Count the frequencies of maxY.
      count = 0;
      if (maxYFreqs.containsKey(maxY)) {
        count = maxYFreqs.get(maxY);
      }
      maxYFreqs.put(maxY, count + 1);
    }

    // Compute the most common minX.
    int maxFrequency = 0;
    for (Entry<Float, Integer> entry : minXFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        statistics.setMostCommonMinX(entry.getKey());
      }
    }

    // Compute the most common minY.
    maxFrequency = 0;
    for (Entry<Float, Integer> entry : minYFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        statistics.setMostCommonMinY(entry.getKey());
      }
    }
    
    // Compute the most common maxX.
    maxFrequency = 0;
    for (Entry<Float, Integer> entry : maxXFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        statistics.setMostCommonMaxX(entry.getKey());
      }
    }
    
    // Compute the most common maxY.
    maxFrequency = 0;
    for (Entry<Float, Integer> entry : maxYFreqs.entrySet()) {
      if (entry.getValue() > maxFrequency) {
        maxFrequency = entry.getValue();
        statistics.setMostCommonMaxY(entry.getKey());
      }
    }
  }
}
