package serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.freiburg.iif.collection.CollectionUtils;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextElement;

/**
 * Serializes a PdfDocument to txt format.
 *
 * @author Claudius Korzen
 */
public class TxtPdfSerializer implements PdfSerializer {
  /** The feature to serialize. */
  protected PdfFeature feature = PdfFeature.paragraphs;
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
    return "txt";
  }

  @Override
  public void serialize(PdfDocument doc, OutputStream os) throws IOException {
    // Serialize the pages.
    List<String> serialized = serializePages(doc);

    // Write the serialization to given stream.
    writeTo(serialized, os);
  }

  // ___________________________________________________________________________

  /**
   * Writes the given json object to the given output stream.
   */
  protected void writeTo(List<String> serialized, OutputStream os)
    throws IOException {
    if (serialized != null && os != null) {
      String delimiter;
      switch (getFeatureToSerialize()) {
        case paragraphs:
          delimiter = "\n\n";
          break;
        default:
          delimiter = " ";
          break;
      }
      String string = CollectionUtils.join(serialized, delimiter);
      os.write(string.getBytes(StandardCharsets.UTF_8));
    }
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

    List<String> serializedPages = new ArrayList<>();
    for (PdfPage page : pages) {
      List<String> serialized = serializePage(page);
      if (serialized != null && !serialized.isEmpty()) {
        serializedPages.addAll(serialized);
      }
    }
    return serializedPages;
  }

  /**
   * Serializes the given page.
   */
  protected List<String> serializePage(PdfPage page) {
    if (page == null) {
      return null;
    }

    PdfFeature feature = getFeatureToSerialize();
    List<? extends PdfElement> els = page.getElementsByFeature(feature);
    
    return serializeElementsWithRespectToRoles(els);
  }

  /**
   * Serializes the given elements with respect to the given roles.
   */
  protected List<String> serializeElementsWithRespectToRoles(
      List<? extends PdfElement> elements) {
    if (elements == null) {
      return null;
    }
    
    List<String> serializedElements = new ArrayList<>();
    for (PdfElement element : elements) {
      // Serialize the element only if its role is included in the given roles.
      if (getRolesToSerialize().contains(element.getRole())) {
        String serialized = serializeElement(element);
        if (serialized != null && !serialized.isEmpty()) {
          serializedElements.add(serialized);
        }
      }
    }
    return serializedElements;
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
    }
    
    return null;
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

    return text;
  }

  @Override
  public void setFeatures(List<PdfFeature> features) {
    if (features == null || features.isEmpty()) {
      throw new IllegalArgumentException("No feature given.");
    }
    
    if (features.size() > 1) {
      throw new IllegalArgumentException(
          "Txt serialization is only allowed for single features.");
    }
    
    this.feature = features.get(0);
  }

  /**
   * Returns the features to serialize.
   */
  public PdfFeature getFeatureToSerialize() {
    return this.feature;
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
