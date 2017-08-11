package pdfact.models;

import java.util.List;

/**
 * A class that computes text line statistics.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineStatistician {
  /**
   * Computes the text line statistic for the given text lines.
   * 
   * @param textLines
   *        The text lines to process.
   * 
   * @return The computed text line statistics.
   */
  PdfTextLineStatistic compute(PdfTextLineList textLines);

  /**
   * Combines the given list of text line statistics to a single statistic.
   * 
   * @param stats
   *        The statistics to combine.
   * 
   * @return The computed statistics.
   */
  PdfTextLineStatistic aggregate(List<? extends HasTextLineStatistic> stats);
}