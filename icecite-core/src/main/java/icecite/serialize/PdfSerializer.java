package icecite.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import icecite.models.PdfDocument;
import icecite.models.PdfRole;
import icecite.models.PdfType;

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

    /**
     * Creates a new PdfSerializer.
     * 
     * @param types
     *        The types of PDF elements to serialize.
     * @return An instance of PdfSerializer.
     */
    PdfSerializer create(@Assisted("types") Set<PdfType> types);
    
    /**
     * Creates a new PdfSerializer.
     * 
     * @param types
     *        The types of PDF elements to serialize.
     * @param roles
     *        The roles of PDF elements to serialize.
     * @return An instance of PdfSerializer.
     */
    PdfSerializer create(@Assisted("types") Set<PdfType> types,
        @Assisted("roles") Set<PdfRole> roles);
  }
}
