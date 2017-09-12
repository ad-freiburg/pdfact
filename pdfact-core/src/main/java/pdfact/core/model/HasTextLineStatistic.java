package pdfact.core.model;

/**
 * An interface that is implemented by PDF elements that have text lines and
 * provide a statistic about the text lines.
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
   * @param statistic
   *        The statistic about the text lines.
   */
  void setTextLineStatistic(TextLineStatistic statistic);
}
