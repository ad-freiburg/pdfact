package pdfact.pipes.visualize;

import java.util.Set;

import pdfact.exception.PdfActVisualizeException;
import pdfact.model.PdfDocument;
import pdfact.model.ElementType;
import pdfact.model.SemanticRole;

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
  Set<ElementType> getElementTypeFilters();

  /**
   * Sets the element types filter.
   * 
   * @param units
   *        The element types filter.
   */
  void setElementTypeFilters(Set<ElementType> units);

  // ==========================================================================

  /**
   * Returns the semantic roles filter.
   * 
   * @return The semantic roles filter.
   */
  Set<SemanticRole> getSemanticRolesFilter();

  /**
   * Sets the semantic roles filter.
   * 
   * @param roles
   *        The semantic roles filter.
   */
  void setSemanticRolesFilter(Set<SemanticRole> roles);

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
    PdfVisualizer create(Set<ElementType> units, Set<SemanticRole> roles);
  }
}