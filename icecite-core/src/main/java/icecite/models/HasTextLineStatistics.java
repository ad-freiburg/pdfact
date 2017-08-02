package icecite.models;

/**
 * A class that declares that the implementing object has statistics about text
 * lines.
 * 
 * @author Claudius Korzen
 */
public interface HasTextLineStatistics {
  /**
   * Returns the statistics about text lines.
   * 
   * @return The statistics about text lines.
   */
  PdfTextLineStatistics getTextLineStatistics();

  /**
   * Returns the statistics about text lines.
   * 
   * @param statistics
   *        The statistics.
   */
  void setPdfTextLineStatistics(PdfTextLineStatistics statistics);
}
