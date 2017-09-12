package pdfact.core.util.statistician;

import java.util.List;

import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.HasCharacterStatistic;
import pdfact.core.model.HasCharacters;
import pdfact.core.util.list.CharacterList;

/**
 * A class that computes statistics about characters.
 * 
 * @author Claudius Korzen
 */
public interface CharacterStatistician {
  /**
   * Computes the character statistic for the given characters.
   * 
   * @param hasCharacters
   *        An element that has characters.
   * 
   * @return The computed character statistics.
   */
  CharacterStatistic compute(HasCharacters hasCharacters);

  /**
   * Computes the character statistic for the given characters.
   * 
   * @param characters
   *        The characters to process.
   * 
   * @return The computed character statistics.
   */
  CharacterStatistic compute(CharacterList characters);

  /**
   * Combines the given list of character statistics to a single statistic.
   * 
   * @param stats
   *        The statistics to combine.
   * 
   * @return The combined statistic.
   */
  CharacterStatistic aggregate(List<? extends HasCharacterStatistic> stats);
}
