package icecite.models;

/**
 * An interface that is implemented by PDF elements that know the statistics
 * about the contained text lines. For example, a PdfTextBlock implements this
 * interface to provide the statistics about the text lines within this text
 * block.
 * 
 * @author Claudius Korzen
 */
public interface HasTextLineStatistic {
  /**
   * Returns the text line statistics.
   * 
   * @return The text line statistics.
   */
  PdfTextLineStatistic getTextLineStatistic();

  /**
   * Returns the text line statistics.
   * 
   * @param statistics
   *        The text line statistics.
   */
  void setTextLineStatistic(PdfTextLineStatistic statistics);
}
