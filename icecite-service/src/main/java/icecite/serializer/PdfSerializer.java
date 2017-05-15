package icecite.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import icecite.models.PdfDocument;
import icecite.models.PdfElement;

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
   * Serializes the given elements of the given PDF document to the given
   * stream.
   *
   * @param pdf
   *        the PDF document to process.
   * @param elementTypes
   *        the types of PDF elements to visualize.
   * @param os
   *        the stream to write to.
   * @throws IOException
   *         if something went wrong while visualizing.
   */
  void serialize(PdfDocument pdf, Set<Class<? extends PdfElement>> elementTypes,
      OutputStream os) throws IOException;
}
