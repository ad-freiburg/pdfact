package serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfPage;

/**
 * Serializes PdfTextDocument to txt format, without any metadata. 
 *
 * @author Claudius Korzen
 */
public class TxtPdfSerializer implements PdfSerializer {
  
  @Override
  public String getOutputFormat() {
    return "txt";
  }
  
  @Override
  public void serialize(PdfDocument doc, OutputStream s) throws IOException {
    serialize(doc, null, s);
  }
  
  @Override
  public void serialize(PdfDocument document, List<PdfFeature> features, 
      OutputStream stream) throws IOException {    
    StringBuffer sb = new StringBuffer();
    
    // If the list of features is empty, take all features.
    if (features == null || features.isEmpty()) {
      features = Arrays.asList(PdfFeature.values());
    }
    
    // Serialize the pages.
    for (PdfPage page : document.getPages()) {
      sb.append(serializePage(page, features));
    }
    
    stream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
  }
  
  // ___________________________________________________________________________
    
  /**
   * Serializes the given page to tsv.
   */
  protected String serializePage(PdfPage page, List<PdfFeature> features) 
      throws IOException {
    StringBuffer sb = new StringBuffer();
       
    for (PdfFeature feature : features) {
      sb.append(serialize(page.getElementsByFeature(feature)));
    }

    return sb.toString();
  }
  
  /**
   * Serializes the given page to tsv.
   */
  protected String serialize(List<? extends PdfElement> elements) 
      throws IOException {
    StringBuffer sb = new StringBuffer();
    if (elements != null) {
      for (PdfElement element : elements) {
        String serialized = serialize(element);
        if (serialized != null) {
          sb.append(serialized);  
        }
      }
    }
    return sb.toString();
  }

  /**
   * Serializes the given page to tsv.
   */
  protected String serialize(PdfElement element) throws IOException {
    return element != null ? element.toString() : null;
  }
}
