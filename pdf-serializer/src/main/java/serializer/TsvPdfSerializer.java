package serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.freiburg.iif.collection.CollectionUtils;
import model.PdfColor;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfFont;
import model.PdfPage;
import model.Serializable;

/**
 * Serializes PdfTextDocument to tsv format. 
 *
 * @author Claudius Korzen
 */
public class TsvPdfSerializer implements PdfSerializer {        
  @Override
  public String getOutputFormat() {
    return "tsv";
  }
  
  @Override
  public void serialize(PdfDocument doc, OutputStream s) throws IOException {
    serialize(doc, null, s);
  }
  
  @Override
  public void serialize(PdfDocument document, List<PdfFeature> features, 
      OutputStream stream) throws IOException {    
    List<String> lines = new ArrayList<>();
    
    // If the list of features is empty, take all features.
    if (features == null || features.isEmpty()) {
      features = Arrays.asList(PdfFeature.values());
    }
    
    // Serialize the pages.
    for (PdfPage page : document.getPages()) {
      lines.addAll(serializePage(page, features));
    }
    
    // Serializes the fonts.
    for (PdfFont font : document.getFonts()) {
      lines.add(toTsv(font));  
    }
    
    // Serializes the colors.
    for (PdfColor color : document.getColors()) {
      lines.add(toTsv(color));  
    }
    
    String serialized = CollectionUtils.join(lines, "\n");
    stream.write(serialized.getBytes(StandardCharsets.UTF_8));
  }
  
  // ___________________________________________________________________________
    
  /**
   * Serializes the given page to tsv.
   */
  protected List<String> serializePage(PdfPage page, List<PdfFeature> features) 
      throws IOException {
    List<String> lines = new ArrayList<>();
       
    for (PdfFeature feature : features) {
      lines.addAll(serialize(page.getElementsByFeature(feature)));
    }

    return lines;
  }
  
  /**
   * Serializes the given page to tsv.
   */
  protected List<String> serialize(List<? extends PdfElement> elements) 
      throws IOException {
    List<String> lines = new ArrayList<>();
    if (elements != null) {
      for (PdfElement element : elements) {
        String serialized = serialize(element);
        if (serialized != null) {
          lines.add(serialized);  
        }
      }
    }
    return lines;
  }

  /**
   * Serializes the given page to tsv.
   */
  protected String serialize(PdfElement element) throws IOException {
    return element != null && !element.ignore() ? toTsv(element) : null;
  }
  
  /**
   * Serializes the given page to tsv.
   */
  protected String toTsv(Serializable element) throws IOException {
    return element != null ? element.toTsv() : null;
  }
}
