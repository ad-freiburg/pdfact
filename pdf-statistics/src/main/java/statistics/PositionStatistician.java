package statistics;

import java.util.Arrays;
import java.util.List;

import de.freiburg.iif.counter.FloatCounter;
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

    FloatCounter minXCounter = new FloatCounter();
    FloatCounter minYCounter = new FloatCounter();
    FloatCounter maxXCounter = new FloatCounter();
    FloatCounter maxYCounter = new FloatCounter();

    for (HasRectangle obj : objects) {            
      minXCounter.add(MathUtils.round(obj.getRectangle().getMinX(), 1));
      minYCounter.add(MathUtils.round(obj.getRectangle().getMinY(), 1));
      maxXCounter.add(MathUtils.round(obj.getRectangle().getMaxX(), 1));
      maxYCounter.add(MathUtils.round(obj.getRectangle().getMaxY(), 1));
    }

    statistics.setMostCommonMinX(minXCounter.getMostFrequentFloat());
    statistics.setMostCommonMinY(minYCounter.getMostFrequentFloat());
    statistics.setMostCommonMaxX(maxXCounter.getMostFrequentFloat());
    statistics.setMostCommonMaxY(maxYCounter.getMostFrequentFloat());
  }
}
