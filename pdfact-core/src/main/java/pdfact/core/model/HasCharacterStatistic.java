package pdfact.core.model;

/**
 * An interface to implement by elements that consist of at least one character and provide a 
 * statistic about that characters.
 * 
 * @author Claudius Korzen
 */
public interface HasCharacterStatistic {
  /**
   * Returns the statistic about the characters.
   * 
   * @return The statistic about the characters.
   */
  CharacterStatistic getCharacterStatistic();

  /**
   * Sets the statistic about the characters.
   * 
   * @param statistic The statistic about the characters.
   */
  void setCharacterStatistic(CharacterStatistic statistic);
}
