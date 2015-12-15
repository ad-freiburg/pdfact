package serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import model.PdfDocument;
import model.PdfFeature;

/**
 * The interface for all concrete implementations to output a pdf text document
 * in a specific format.
 *
 * @author Claudius Korzen
 */
public interface PdfSerializer {
  /**
   * Serializes the given document to the given stream in a specific format.
   */
  public void serialize(PdfDocument document, OutputStream stream)
    throws IOException;

  /**
   * Serializes the given features of the given document to the given stream in
   * a specific format.
   */
  public void serialize(PdfDocument document, List<PdfFeature> features,
      OutputStream stream) throws IOException;

  /**
   * Returns the produced format of this serializer.
   */
  public String getOutputFormat();
}
