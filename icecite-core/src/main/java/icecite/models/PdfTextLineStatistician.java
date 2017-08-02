package icecite.models;

import java.util.List;

/**
 * A class that computes statistics about text lines.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineStatistician {
  /**
   * Computes statistics about text lines.
   * 
   * @param textLines
   *        The text lines to process.
   * 
   * @return The computed statistics.
   */
  PdfTextLineStatistics compute(PdfTextLineList textLines);

  /**
   * Aggregates the given text line statistics.
   * 
   * @param statistics
   *        The statistics to aggregate.
   * 
   * @return The aggregated statistics.
   */
  PdfTextLineStatistics aggregate(PdfTextLineStatistics... statistics);

  /**
   * Aggregates the given text line statistics.
   * 
   * @param statistics
   *        The statistics to aggregate.
   * 
   * @return The aggregated statistics.
   */
  PdfTextLineStatistics aggregate(List<PdfTextLineStatistics> statistics);
}