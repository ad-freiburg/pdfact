package pdfact.core.model;

/**
 * An interface that is implemented by PDF elements that have characters and
 * provide a statistic about the characters.
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
   * @param statistic
   *        The statistic about the characters.
   */
  void setCharacterStatistic(CharacterStatistic statistic);
}
