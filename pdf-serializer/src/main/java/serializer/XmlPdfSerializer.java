package serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.freiburg.iif.collection.CollectionUtils;
import de.freiburg.iif.text.StringUtils;
import model.HasFeature;
import model.PdfColor;
import model.PdfDocument;
import model.PdfFeature;
import model.PdfFont;
import model.PdfPage;
import model.Serializable;

/**
 * Serializes PdfTextDocument to xml format. 
 *
 * @author Claudius Korzen
 */
public class XmlPdfSerializer implements PdfSerializer {
  /**
   * The indent length.
   */
  protected static final int INDENT_LENGTH = 4; 
  
  @Override
  public String getOutputFormat() {
    return "xml";
  }
  
  @Override
  public void serialize(PdfDocument doc, OutputStream s) throws IOException {
    serialize(doc, null, s);
  }
  
  @Override
  public void serialize(PdfDocument document, List<PdfFeature> features, 
      OutputStream stream) throws IOException {    
    List<String> lines = new ArrayList<>();
    
    int level = 0;
    
    lines.add(indent("<document>", level));
    
    lines.add(indent("<pages>", ++level));
    // Serialize the pages.
    level++;
    for (PdfPage page : document.getPages()) {
      lines.addAll(serializePage(page, features, level));
    }
    lines.add(indent("</pages>", --level));
    
    lines.add(indent("<fonts>", level++));
    // Serializes the fonts.
    for (PdfFont font : document.getFonts()) {
      lines.add(serialize(font, level));  
    }
    lines.add(indent("</fonts>", --level));
    
    lines.add(indent("<colors>", level++));
    // Serializes the colors.
    for (PdfColor color : document.getColors()) {
      lines.add(serialize(color, level));  
    }
    lines.add(indent("</colors>", --level));
    
    lines.add(indent("</document>", --level));
    
    String serialized = CollectionUtils.join(lines, System.lineSeparator());
    stream.write(serialized.getBytes(StandardCharsets.UTF_8));
  }
  
  // ___________________________________________________________________________
    
  /**
   * Serializes the given page to tsv.
   */
  protected List<String> serializePage(PdfPage page, List<PdfFeature> features, 
      int level) throws IOException {
    List<String> lines = new ArrayList<>();
    
    // If the list of features is empty, take all features.
    if (features == null || features.isEmpty()) {
      features = Arrays.asList(PdfFeature.values());
    }
    
    lines.add(indent("<page id=\"" + page.getPageNumber() + "\">", level));
    level++;
    for (PdfFeature feature : features) {
      lines.addAll(serialize(page.getElementsByFeature(feature), level));
    }
    level--;
    lines.add(indent("</page>", level));

    return lines;
  }
  
  /**
   * Serializes the given page to tsv.
   */
  protected List<String> serialize(List<? extends HasFeature> elements, 
      int indentLevel) throws IOException {
    List<String> lines = new ArrayList<>();
    if (elements != null) {
      for (HasFeature element : elements) {
        String serialized = serialize(element, indentLevel);
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
  protected String serialize(Serializable element, int indentLevel) 
      throws IOException {
    return element != null ? element.toXml(indentLevel, INDENT_LENGTH) : null;
  }
  
  /**
   * Indents the given string.
   */
  protected String indent(String string, int level) {
    String indent = StringUtils.repeat(" ", level * INDENT_LENGTH);
    return indent + string;
  }
}
