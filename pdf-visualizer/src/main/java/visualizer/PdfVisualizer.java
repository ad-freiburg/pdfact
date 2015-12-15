package visualizer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import model.PdfDocument;
import model.PdfFeature;

/**
 * The interface for all concrete implementations to visualize the features of
 * a pdf text document.
 *
 * @author Claudius Korzen
 */
public interface PdfVisualizer {
  /**
   * Visualizes all features of the given document to the given stream.
   */
  public void visualize(PdfDocument document, OutputStream stream)
    throws IOException;

  /**
   * Visualizes the given features of the given document to the given stream.
   */
  public void visualize(PdfDocument document, List<PdfFeature> features,
      OutputStream stream) throws IOException;
}
