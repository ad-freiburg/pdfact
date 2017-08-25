package pdfact.model;

/**
 * An interface that is implemented by PDF elements that have statistics about
 * the contained characters.
 * 
 * @author Claudius Korzen
 */
public interface HasCharacterStatistic {
  /**
   * Returns a statistic about characters.
   * 
   * @return A statistic about characters.
   */
  CharacterStatistic getCharacterStatistic();

  /**
   * Sets a character statistic.
   * 
   * @param statistic
   *        The statistic about characters to set.
   */
  void setCharacterStatistic(CharacterStatistic statistic);
}
