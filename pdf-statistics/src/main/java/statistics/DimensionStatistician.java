package statistics;

import java.util.Arrays;
import java.util.List;

import de.freiburg.iif.counter.FloatCounter;
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

    FloatCounter heightsCounter = new FloatCounter();
    FloatCounter widthsCounter = new FloatCounter();
    
    for (HasRectangle obj : objects) {
      float height = MathUtils.round(obj.getRectangle().getHeight(), 1);
      float width = MathUtils.round(obj.getRectangle().getWidth(), 1);
      
      heightsCounter.add(height);
      widthsCounter.add(width);
    }

    statistics.setAverageHeight(heightsCounter.getAverageValue());
    statistics.setAverageWidth(widthsCounter.getAverageValue());
    statistics.setMostCommonHeight(heightsCounter.getMostFrequentFloat());
    statistics.setMostCommonWidth(widthsCounter.getMostFrequentFloat());
    statistics.setHeightsCounter(heightsCounter);
    statistics.setWidthsCounter(widthsCounter);
    
    return statistics;
  }

  /**
   * Accumulates the given statistics to a single statistic.
   */
  public static DimensionStatistics accumulate(
      List<? extends HasDimensionStatistics> objs) {
    PlainDimensionStatistics statistics = new PlainDimensionStatistics();

    FloatCounter heightsCounter = new FloatCounter();
    FloatCounter widthsCounter = new FloatCounter();
    
    for (HasDimensionStatistics obj : objs) {
      DimensionStatistics dimStats = obj.getDimensionStatistics();
      FloatCounter objHeightsCounter = dimStats.getHeightsCounter();
      FloatCounter objWidthsCounter = dimStats.getWidthsCounter();
            
      heightsCounter.add(objHeightsCounter);
      widthsCounter.add(objWidthsCounter);
    }

    statistics.setAverageHeight(heightsCounter.getAverageValue());
    statistics.setAverageWidth(widthsCounter.getAverageValue());
    
    statistics.setMostCommonHeight(heightsCounter.getMostFrequentFloat());
    statistics.setHeightsCounter(heightsCounter);
    statistics.setMostCommonWidth(widthsCounter.getMostFrequentFloat());
    statistics.setWidthsCounter(widthsCounter);
    
    return statistics;
  }
}
