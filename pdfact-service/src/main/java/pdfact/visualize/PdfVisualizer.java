package pdfact.visualize;

import java.util.Set;

import pdfact.exception.PdfActVisualizeException;
import pdfact.models.PdfDocument;
import pdfact.models.PdfRole;
import pdfact.models.PdfElementType;

/**
 * The interface for all concrete implementations to visualize the features of a
 * PDF text document.
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
   * Returns the element types filter.
   * 
   * @return The element types filter.
   */
  Set<PdfElementType> getElementTypeFilters();

  /**
   * Sets the element types filter.
   * 
   * @param units
   *        The element types filter.
   */
  void setElementTypeFilters(Set<PdfElementType> units);

  // ==========================================================================

  /**
   * Returns the semantic roles filter.
   * 
   * @return The semantic roles filter.
   */
  Set<PdfRole> getSemanticRolesFilter();

  /**
   * Sets the semantic roles filter.
   * 
   * @param roles
   *        The semantic roles filter.
   */
  void setSemanticRolesFilter(Set<PdfRole> roles);

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
