package pdfact.models;

import java.util.List;

/**
 * A class that computes character statistics.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterStatistician {
  /**
   * Computes the character statistic for the given characters.
   * 
   * @param characters
   *        The characters to process.
   * 
   * @return The computed character statistics.
   */
  PdfCharacterStatistic compute(PdfCharacterList characters);
  
  /**
   * Combines the given list of character statistics to a single statistic.
   * 
   * @param stats
   *        The statistics to combine.
   * 
   * @return The computed statistics.
   */
  PdfCharacterStatistic combine(List<? extends HasCharacterStatistic> stats);
}
