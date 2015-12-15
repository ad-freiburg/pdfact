package analyzer;

import model.PdfDocument;

/**
 * The interface for a pdf analyzer.
 *
 * @author Claudius Korzen
 */
public interface PdfAnalyzer {
  /**
   * Analyzes the given document.
   * 
   * @param document
   */
  public void analyze(PdfDocument document);
}
