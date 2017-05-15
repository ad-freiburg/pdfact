package icecite.visualizer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import icecite.models.PdfDocument;
import icecite.models.PdfElement;

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
   * Visualizes the given elements of the given PDF document to the given
   * stream.
   *
   * @param pdf
   *        the PDF document to process.
   * @param elementTypes
   *        the types of PDF elements to visualize.
   * @param stream
   *        the stream to write to.
   * @throws IOException
   *         if something went wrong while visualizing.
   */
  void visualize(PdfDocument pdf, Set<Class<? extends PdfElement>> elementTypes,
      OutputStream stream) throws IOException;
}
