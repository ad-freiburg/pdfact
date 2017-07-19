package icecite.visualize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import icecite.models.PdfDocument;
import icecite.models.PdfRole;
import icecite.models.PdfType;

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

    /**
     * Creates a new PdfVisualizer.
     * 
     * @param types
     *        The types of PDF elements to visualize.
     * @return An instance of PdfVisualizer.
     */
    PdfVisualizer create(@Assisted Set<PdfType> types);

    /**
     * Creates a new PdfVisualizer.
     * 
     * @param types
     *        The types of PDF elements to visualize.
     * @param roles
     *        The roles of PDF elements to visualize.
     * @return An instance of PdfVisualizer.
     */
    PdfVisualizer create(@Assisted("types") Set<PdfType> types,
        @Assisted("roles") Set<PdfRole> roles);
  }
}
