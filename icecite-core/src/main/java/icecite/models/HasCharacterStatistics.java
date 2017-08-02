package icecite.models;

/**
 * A class that declares that the implementing object has statistics about
 * characters.
 * 
 * @author Claudius Korzen
 */
public interface HasCharacterStatistics {
  /**
   * Returns the statistics about characters.
   * 
   * @return The statistics about characters.
   */
  PdfCharacterStatistics getCharacterStatistics();

  /**
   * Sets the statistics about characters.
   * 
   * @param statistics
   *        The statistics.
   */
  void setCharacterStatistics(PdfCharacterStatistics statistics);
}
