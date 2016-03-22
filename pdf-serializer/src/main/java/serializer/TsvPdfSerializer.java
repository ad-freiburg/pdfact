package serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.freiburg.iif.collection.CollectionUtils;
import de.freiburg.iif.model.Rectangle;
import model.PdfColor;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfFont;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextElement;

/**
 * Serializes a PdfDocument to tsv format.
 *
 * @author Claudius Korzen
 */
public class TsvPdfSerializer implements PdfSerializer {
  /** The features to serialize (all features per default). */
  protected List<PdfFeature> features = PdfFeature.valuesAsList();
  /** The roles to serialize (all roles per default). */
  protected Set<PdfRole> roles = PdfRole.valuesAsSet();

  /** The flag indicating whether to serialize punctuation marks. */
  protected boolean serializePunctuationMarks = true;
  /** The flag indicating whether to serialize subscripts. */
  protected boolean serializeSubscripts = true;
  /** The flag indicating whether to serialize superscripts. */
  protected boolean serializeSuperscripts = true;

  // ___________________________________________________________________________

  @Override
  public String getOutputFormat() {
    return "tsv";
  }

  @Override
  public void serialize(PdfDocument doc, OutputStream os) throws IOException {
    List<String> tsvLines = new ArrayList<>();

    // Serialize the pages.
    List<String> serializedPages = serializePages(doc);
    if (serializedPages != null && !serializedPages.isEmpty()) {
      tsvLines.addAll(serializedPages);
    }

    // Serialize the fonts.
    List<String> serializedFonts = serializeFonts(doc);
    if (serializedFonts != null && !serializedFonts.isEmpty()) {
      tsvLines.addAll(serializedFonts);
    }

    // Serialize the colors.
    List<String> serializedColors = serializeColors(doc);
    if (serializedColors != null && !serializedColors.isEmpty()) {
      tsvLines.addAll(serializedColors);
    }

    // Write the serialization to given stream.
    writeTo(tsvLines, os);
  }

  // ___________________________________________________________________________

  /**
   * Writes the given json object to the given output stream.
   */
  protected void writeTo(List<String> lines, OutputStream os)
    throws IOException {
    String serialized = CollectionUtils.join(lines, "\n");
    os.write(serialized.getBytes(StandardCharsets.UTF_8));
  }

  // ___________________________________________________________________________

  /**
   * Serializes the pages of the given document.
   */
  protected List<String> serializePages(PdfDocument doc) {
    if (doc == null) {
      return null;
    }

    List<PdfPage> pages = doc.getPages();

    if (pages == null || pages.isEmpty()) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfPage page : pages) {
      List<String> serialized = serializePage(page);
      if (serialized != null && !serialized.isEmpty()) {
        lines.addAll(serialized);
      }
    }
    return lines;
  }

  /**
   * Serializes the given page.
   */
  protected List<String> serializePage(PdfPage page) {
    if (page == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();

    // Obtain the features to serialize.
    List<PdfFeature> features = getFeaturesToSerialize();

    if (features != null) {
      for (PdfFeature f : features) {
        // Obtain the elements by feature.
        List<? extends PdfElement> elements = page.getElementsByFeature(f);

        List<String> serialized = serializeElementsWithRespectToRoles(elements);
        if (serialized != null && !serialized.isEmpty()) {
          lines.addAll(serialized);
        }
      }
    }

    return lines;
  }

  /**
   * Serializes the given elements with respect to the given roles.
   */
  protected List<String> serializeElementsWithRespectToRoles(
      List<? extends PdfElement> elements) {
    if (elements == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfElement element : elements) {
      // Serialize the element only if its role is included in the given roles.
      if (getRolesToSerialize().contains(element.getRole())) {
        String serialized = serializeElement(element);
        if (serialized != null && !serialized.isEmpty()) {
          lines.add(serialized);
        }
      }
    }
    return lines;
  }

  /**
   * Serializes the given element.
   */
  protected String serializeElement(PdfElement element) {
    if (element == null) {
      return null;
    }

    // TODO: Get rid of ignore method. Is still needed for dehyphenation.
    if (element.ignore()) {
      return null;
    }

    if (element instanceof PdfTextElement) {
      return serializeTextElement((PdfTextElement) element);
    } else {
      return serializeNonTextElement(element);
    }
  }

  /**
   * Serializes the fonts of the given document.
   */
  protected List<String> serializeFonts(PdfDocument document) {
    if (document == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfFont font : document.getFonts()) {
      String serialized = serializeFont(font);
      if (serialized != null && !serialized.isEmpty()) {
        lines.add(serialized);
      }
    }
    return lines;
  }

  /**
   * Serializes the colors of the given document.
   */
  protected List<String> serializeColors(PdfDocument document) {
    if (document == null) {
      return null;
    }

    List<String> lines = new ArrayList<>();
    for (PdfColor color : document.getColors()) {
      String serialized = serializeColor(color);
      if (serialized != null && !serialized.isEmpty()) {
        lines.add(serialized);
      }
    }
    return lines;
  }

  /**
   * Serializes the given text element.
   */
  protected String serializeTextElement(PdfTextElement element) {
    if (element == null) {
      return null;
    }

    String text = element.getText(getSerializePunctuationMarks(),
        getSerializeSubscripts(), getSerializeSuperscripts());

    if (text == null || text.trim().isEmpty()) {
      return null;
    }

    List<String> fields = new ArrayList<>();
    
    // Add feature name.
    PdfFeature feature = element.getFeature();
    fields.add(feature != null ? feature.getFieldName() : "");
    
    // Add text.
    fields.add(text.replaceAll("\t", " "));
    
    // Add page.
    PdfPage page = element.getPage();
    fields.add(page != null ? String.valueOf(page.getPageNumber()) : "");

    // Add bounding box.
    Rectangle rect = element.getRectangle();
    fields.add(rect != null ? String.valueOf(rect.getMinX()) : "");
    fields.add(rect != null ? String.valueOf(rect.getMinY()) : "");
    fields.add(rect != null ? String.valueOf(rect.getMaxX()) : "");
    fields.add(rect != null ? String.valueOf(rect.getMaxY()) : "");

    // Add font.
    PdfFont font = element.getFont();
    fields.add(font != null ? font.getId() : "");
    fields.add(String.valueOf(element.getFontsize()));
    
    // Add color.
    PdfColor color = element.getColor();
    fields.add(color != null ? color.getId() : "");

    // Add role.
    PdfRole role = element.getRole();
    fields.add(role != null ? role.name : "");

    return CollectionUtils.join(fields, "\t");
  }

  /**
   * Serializes the given (non-text) element.
   */
  protected String serializeNonTextElement(PdfElement element) {
    if (element == null) {
      return null;
    }

    List<String> fields = new ArrayList<>();

    // Add feature name.
    PdfFeature feature = element.getFeature();
    fields.add(feature != null ? feature.getFieldName() : "");
    
    // Add bounding box.
    Rectangle rect = element.getRectangle();
    fields.add(rect != null ? String.valueOf(rect.getMinX()) : "");
    fields.add(rect != null ? String.valueOf(rect.getMinY()) : "");
    fields.add(rect != null ? String.valueOf(rect.getMaxX()) : "");
    fields.add(rect != null ? String.valueOf(rect.getMaxY()) : "");

    // Add color.
    PdfColor color = element.getColor();
    fields.add(color != null ? color.getId() : "");

    // Add role.
    PdfRole role = element.getRole();
    fields.add(role != null ? role.name : "");

    return CollectionUtils.join(fields, "\t");
  }

  /**
   * Serializes the given font.
   */
  protected String serializeFont(PdfFont font) {
    if (font == null) {
      return null;
    }

    String id = font.getId();
    if (id == null) {
      return null;
    }

    List<String> fields = new ArrayList<>();

    // Add feature name.
    fields.add("font");
    
    // Add id.
    fields.add(id);

    // Add basename
    String basename = font.getBasename();
    fields.add(basename != null ? basename : "");

    fields.add(font.isBold() ? "1" : "0");
    fields.add(font.isItalic() ? "1" : "0");
    fields.add(font.isType3Font() ? "1" : "0");

    return CollectionUtils.join(fields, "\t");
  }

  /**
   * Serializes the given color.
   */
  protected String serializeColor(PdfColor color) {
    if (color == null) {
      return null;
    }

    String id = color.getId();

    if (id == null) {
      return null;
    }

    List<String> fields = new ArrayList<>();

    // Add feature name.
    fields.add("color");
    
    // Add id.
    fields.add(id);
    
    // Add rgb
    fields.add(String.valueOf(color.getR()));
    fields.add(String.valueOf(color.getG()));
    fields.add(String.valueOf(color.getB()));

    return CollectionUtils.join(fields, "\t");
  }

  @Override
  public void setFeatures(List<PdfFeature> features) {
    this.features = features;
  }

  /**
   * Returns the features to serialize.
   */
  public List<PdfFeature> getFeaturesToSerialize() {
    return this.features;
  }

  @Override
  public void setRoles(List<PdfRole> roles) {
    this.roles = new HashSet<>(roles);
  }

  /**
   * Returns the features to serialize.
   */
  public Set<PdfRole> getRolesToSerialize() {
    return this.roles;
  }

  @Override
  public void setSerializePunctuationMarks(boolean serialize) {
    this.serializePunctuationMarks = serialize;
  }

  /**
   * Returns true if punctuation marks should be serialized.
   */
  public boolean getSerializePunctuationMarks() {
    return this.serializePunctuationMarks;
  }

  @Override
  public void setSerializeSubscripts(boolean serialize) {
    this.serializeSubscripts = serialize;
  }

  /**
   * Returns true if subscripts should be serialized.
   */
  public boolean getSerializeSubscripts() {
    return this.serializeSubscripts;
  }

  @Override
  public void setSerializeSuperscripts(boolean serialize) {
    this.serializeSuperscripts = serialize;
  }

  /**
   * Returns true if superscripts should be serialized.
   */
  public boolean getSerializeSuperscripts() {
    return this.serializeSuperscripts;
  }
}
