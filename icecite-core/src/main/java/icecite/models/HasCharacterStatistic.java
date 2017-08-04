package icecite.models;

/**
 * An interface that is implemented by PDF elements that know the statistics
 * about the contained characters. For example, a PdfTextBlock implements this
 * interface to provide the statistics about the characters within this text
 * block.
 * 
 * @author Claudius Korzen
 */
public interface HasCharacterStatistic {
  /**
   * Returns the character statistics.
   * 
   * @return The character statistics.
   */
  PdfCharacterStatistic getCharacterStatistic();

  /**
   * Sets the character statistics.
   * 
   * @param statistics
   *        The character statistics.
   */
  void setCharacterStatistic(PdfCharacterStatistic statistics);
}
