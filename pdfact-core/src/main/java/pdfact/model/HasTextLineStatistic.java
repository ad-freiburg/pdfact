package pdfact.model;

/**
 * An interface that is implemented by PDF elements that have statistics about
 * the contained text lines.
 * 
 * @author Claudius Korzen
 */
public interface HasTextLineStatistic {
  /**
   * Returns a statistic about text lines.
   * 
   * @return A statistic about text lines.
   */
  TextLineStatistic getTextLineStatistic();

  /**
   * Returns a statistic about text lines.
   * 
   * @param statistic
   *        A statistic about text lines.
   */
  void setTextLineStatistic(TextLineStatistic statistic);
}
