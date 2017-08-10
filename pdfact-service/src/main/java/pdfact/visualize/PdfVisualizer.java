package pdfact.visualize;

import java.util.Set;

import pdfact.exception.PdfActVisualizeException;
import pdfact.models.PdfDocument;
import pdfact.models.PdfRole;
import pdfact.models.PdfElementType;

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
   * @return the visualization.
   * @throws PdfActVisualizeException
   *         If something went wrong while visualizing.
   */
  byte[] visualize(PdfDocument pdf) throws PdfActVisualizeException;

  // ==========================================================================

  /**
   * Returns the text units to include on serialization.
   * 
   * @return The text units to include on serialization.
   */
  Set<PdfElementType> getElementTypeFilters();

  /**
   * Sets the text units to include on serialization.
   * 
   * @param units
   *        The text units to include on serialization.
   */
  void setElementTypeFilters(Set<PdfElementType> units);

  // ==========================================================================

  /**
   * Returns the semantic roles of text units to include on serialization.
   * 
   * @return The semantic roles of text units to include on serialization.
   */
  Set<PdfRole> getElementRoleFilters();

  /**
   * Sets the semantic roles of text units to include on serialization.
   * 
   * @param roles
   *        The semantic roles of text units to include on serialization.
   */
  void setElementRoleFilters(Set<PdfRole> roles);

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
     * @param units
     *        The text units to include.
     * @param roles
     *        The semantic roles of text units to include.
     * 
     * @return An instance of PdfVisualizer.
     */
    PdfVisualizer create(Set<PdfElementType> units, Set<PdfRole> roles);
  }
}
