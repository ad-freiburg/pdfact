package icecite.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import icecite.models.PdfDocument;
import icecite.models.PdfFeature;
import icecite.models.PdfRole;

/**
 * A serializer to serialize a PDF document in a specific format.
 *
 * @author Claudius Korzen
 */
public interface PdfSerializer {
  /**
   * Serializes the given PDF document to the given output stream.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * @param os
   *        The output stream to write to.
   * @throws IOException
   *         If something went wrong on serialization.
   */
  void serialize(PdfDocument pdf, OutputStream os) throws IOException;

  /**
   * Serializes the given PDF document to the given output stream.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * @param os
   *        The output stream to write to.
   * @param features
   *        The features to serialize.
   * @param roles
   *        The roles to consider on serializing.
   * @throws IOException
   *         If something went wrong on serialization.
   */
  void serialize(PdfDocument pdf, OutputStream os, Set<PdfFeature> features,
      Set<PdfRole> roles) throws IOException;

  // ==========================================================================

  /**
   * The factory to create instances of PdfSerializer.
   * 
   * @author Claudius Korzen
   */
  public interface PdfSerializerFactory {
    /**
     * Creates a new PdfSerializer.
     * 
     * @return An instance of PdfSerializer.
     */
    PdfSerializer create();
  }
}
