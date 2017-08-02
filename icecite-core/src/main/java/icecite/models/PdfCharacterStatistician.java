package icecite.models;

import java.util.List;

/**
 * A class that computes statistics about characters.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterStatistician {
  /**
   * Computes statistics about characters.
   * 
   * @param characters
   *        The characters to process.
   * 
   * @return The computed statistics.
   */
  PdfCharacterStatistics compute(PdfCharacterList characters);

  /**
   * Aggregates the given characters statistics.
   * 
   * @param statistics
   *        The statistics to aggregate.
   * 
   * @return The aggregated statistics.
   */
  PdfCharacterStatistics aggregate(PdfCharacterStatistics... statistics);

  /**
   * Aggregates the given characters statistics.
   * 
   * @param statistics
   *        The statistics to aggregate.
   * 
   * @return The aggregated statistics.
   */
  PdfCharacterStatistics aggregate(List<PdfCharacterStatistics> statistics);
}
