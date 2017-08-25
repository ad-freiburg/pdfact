package pdfact.util.statistic;

import java.util.List;

import pdfact.model.HasTextLineStatistic;
import pdfact.model.HasTextLines;
import pdfact.model.TextLineStatistic;
import pdfact.util.list.TextLineList;

/**
 * A class that computes statistics about text line.
 * 
 * @author Claudius Korzen
 */
public interface TextLineStatistician {
  /**
   * Computes the text line statistic for the given text lines.
   * 
   * @param hasTextLines
   *        An element that has text lines.
   * 
   * @return The computed text line statistics.
   */
  TextLineStatistic compute(HasTextLines hasTextLines);
  
  /**
   * Computes the text line statistic for the given text lines.
   * 
   * @param textLines
   *        The text lines to process.
   * 
   * @return The computed text line statistics.
   */
  TextLineStatistic compute(TextLineList textLines);

  /**
   * Combines the given list of text line statistics to a single statistic.
   * 
   * @param stats
   *        The statistics to combine.
   * 
   * @return The combined statistics.
   */
  TextLineStatistic aggregate(List<? extends HasTextLineStatistic> stats);
}