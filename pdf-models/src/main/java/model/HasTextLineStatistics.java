package model;

/**
 * Interface to declare, that the implementing class contains text line
 * statistics.
 *
 * @author Claudius Korzen
 */
public interface HasTextLineStatistics {
  /**
   * Returns the text line statistics.
   */
  public TextLineStatistics getTextLineStatistics();
}
