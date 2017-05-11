package icecite.serializer;

import java.io.IOException;
import java.io.OutputStream;

import icecite.models.PdfDocument;

/**
 * An implementation of PdfSerializer that serializes a PDF document to JSON 
 * format.
 *
 * @author Claudius Korzen
 */
public class XmlPdfSerializer implements PdfSerializer {
  @Override
  public void serialize(PdfDocument doc, OutputStream os) throws IOException {
    // Serialize the pages.
    // List<String> serialized = serializePages(doc, os);
  }

  @Override
  public String getOutputFormat() {
    return "xml";
  }
  
  // ==========================================================================

}
