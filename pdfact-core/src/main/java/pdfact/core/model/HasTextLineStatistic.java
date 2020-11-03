package pdfact.core.model;

/**
 * An interface to implement by elements that consist of at least one text line
 * and provide a statistic about that text lines.
 * 
 * @author Claudius Korzen
 */
public interface HasTextLineStatistic {
  /**
   * Returns the statistic about the text lines.
   * 
   * @return The statistic about the text lines.
   */
  TextLineStatistic getTextLineStatistic();

  /**
   * Returns the statistic about the text lines.
   * 
   * @param statistic The statistic about the text lines.
   */
  void setTextLineStatistic(TextLineStatistic statistic);
}
