package icecite.visualizer;

import java.io.IOException;
import java.io.OutputStream;

import icecite.models.PdfDocument;

/**
 * The interface for all concrete implementations to visualize the features of
 * a pdf text document.
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

  // /**
  // * Visualizes selected elements of the given PDF document to the given
  // stream.
  // *
  // * @param pdf the PDF document to process.
  // * @param filters the elements to visualize.
  // * @param stream the stream to write to.
  // * @throws IOException if something went wrong while visualizing.
  // */
  // public void visualize(PdfDocument pdf, List<Class<PdfElement>> filters,
  // OutputStream stream) throws IOException;
}
