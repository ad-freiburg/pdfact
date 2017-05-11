package icecite.serializer;

import java.io.IOException;
import java.io.OutputStream;

import icecite.models.PdfDocument;

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
   * Returns the output format of this serializer.
   * 
   * @return the output format of this serializer
   */
  String getOutputFormat();
}
