package icecite.visualize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import icecite.models.PdfDocument;
import icecite.models.PdfFeature;
import icecite.models.PdfRole;

/**
 * The interface for all concrete implementations to visualize the features of
 * a PDF text document.
 *
 * @author Claudius Korzen
 */
public interface PdfVisualizer {
  /**
   * Visualizes all features of the given document to the given stream.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param stream
   *        The stream to write to.
   * @throws IOException
   *         If something went wrong while visualizing.
   */
  void visualize(PdfDocument pdf, OutputStream stream) throws IOException;

  /**
   * Visualizes all features of the given document to the given stream.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param stream
   *        The stream to write to.
   * @param features
   *        The features to visualize.
   * @param roles
   *        The roles to consider on visualization.
   * @throws IOException
   *         If something went wrong while visualizing.
   */
  void visualize(PdfDocument pdf, OutputStream stream, Set<PdfFeature> features,
      Set<PdfRole> roles) throws IOException;

  // ==========================================================================

  /**
   * The factory to create instances of PdfVisualizer.
   * 
   * @author Claudius Korzen
   */
  public interface PdfVisualizerFactory {
    /**
     * Creates a new PdfVisualizer.
     * 
     * @return An instance of PdfVisualizer.
     */
    PdfVisualizer create();
  }
}
