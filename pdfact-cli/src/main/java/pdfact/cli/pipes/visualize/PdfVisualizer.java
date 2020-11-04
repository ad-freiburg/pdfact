package pdfact.cli.pipes.visualize;

import java.util.Set;

import pdfact.cli.model.TextUnit;
import pdfact.cli.util.exception.PdfActVisualizeException;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.SemanticRole;

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

  // ==============================================================================================

  /**
   * Returns the text unit.
   * 
   * @return The text unit.
   */
  TextUnit getTextUnit();

  /**
   * Sets the text unit.
   * 
   * @param unit
   *        The text unit.
   */
  void setTextUnit(TextUnit unit);

  // ==============================================================================================

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

  // ==============================================================================================

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
     * @param unit
     *        The text unit to include.
     * @param roles
     *        The semantic roles of text units to include.
     * 
     * @return An instance of PdfVisualizer.
     */
    PdfVisualizer create(TextUnit unit, Set<SemanticRole> roles);
  }
}
