package icecite.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.HasText;
import icecite.models.PdfCharacter;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfFeature;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.utils.collection.CollectionUtils;

// TODO: Throw an exception if there isn't exactly one type to serialize.

/**
 * An implementation of PdfSerializer that serializes a PDF document to TXT
 * format.
 *
 * @author Claudius Korzen
 */
public class TxtPdfSerializer implements PdfSerializer {
  /**
   * The roles of PDF elements to serialize.
   */
  protected Set<PdfRole> roles;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new serializer that serializes PDF documents to TXT format.
   */
  @AssistedInject
  public TxtPdfSerializer() {
  }

  // ==========================================================================

  @Override
  public void serialize(PdfDocument pdf, OutputStream os) throws IOException {
    serialize(pdf, os, null, null);
  }

  @Override
  public void serialize(PdfDocument pdf, OutputStream os,
      Set<PdfFeature> features, Set<PdfRole> roles) throws IOException {
    if (pdf == null) {
      return;
    }
    if (os == null) {
      return;
    }

    // Serialize the pages.
    List<String> serializedPages = serializePages(pdf, features, roles);

    // Write the serialization to the given stream.
    writeTo(serializedPages, os, features);
  }

  // ==========================================================================

  /**
   * Serializes the pages of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * @param features
   *        The features to serialize.
   * @param roles
   *        The roles to consider on serialization.
   * @return The text lines that represent the serialized pages of the PDF
   *         document.
   */
  protected List<String> serializePages(PdfDocument pdf,
      Set<PdfFeature> features, Set<PdfRole> roles) {
    if (pdf == null) {
      return null;
    }

    List<PdfPage> pages = pdf.getPages();
    if (pages == null || pages.isEmpty()) {
      return null;
    }

    List<String> serializedPages = new ArrayList<>();
    for (PdfPage page : pages) {
      List<String> serializedPage = serializePage(page, features, roles);
      if (serializedPage != null) {
        for (String s : serializedPage) {
          if (s != null && !s.isEmpty()) {
            serializedPages.add(s);
          }
        }
      }
    }
    return serializedPages;
  }

  /**
   * Serializes the given page.
   * 
   * @param page
   *        The page to serialize.
   * @param features
   *        The features to serialize.
   * @param roles
   *        The roles to consider on serialization.
   * @return A list of strings, representing the serialized page.
   */
  protected List<String> serializePage(PdfPage page, Set<PdfFeature> features,
      Set<PdfRole> roles) {
    if (page == null) {
      return null;
    }

    List<String> lines = new ArrayList<String>();
    // Serialize the text elements.
    for (PdfTextBlock block : page.getTextBlocks()) {
      if (isSerializePdfElement(block, features, roles)) {
        lines.add(serializePdfElement(block));
      }
      for (PdfTextLine line : block.getTextLines()) {
        if (isSerializePdfElement(line, features, roles)) {
          lines.add(serializePdfElement(line));
        }
        for (PdfWord word : line.getWords()) {
          if (isSerializePdfElement(word, features, roles)) {
            lines.add(serializePdfElement(word));
          }
          for (PdfCharacter character : word.getCharacters()) {
            if (isSerializePdfElement(character, features, roles)) {
              lines.add(serializePdfElement(character));
            }
          }
        }
      }
    }

    // Serialize the graphical elements.
    for (PdfFigure figure : page.getFigures()) {
      if (isSerializePdfElement(figure, features, roles)) {
        lines.add(serializePdfElement(figure));
      }
    }
    for (PdfShape shape : page.getShapes()) {
      if (isSerializePdfElement(shape, features, roles)) {
        lines.add(serializePdfElement(shape));
      }
    }
    return lines;
  }

  /**
   * Serializes the given PDF element.
   * 
   * @param element
   *        The element to serialize.
   * @return The serialization string representing the given PDF element.
   */
  protected String serializePdfElement(PdfElement element) {
    if (element == null) {
      return null;
    }

    if (element instanceof HasText) {
      HasText hasText = (HasText) element;
      String text = hasText.getText();

      if (text != null && !text.trim().isEmpty()) {
        return text;
      }
    }

    return null;
  }

  // ==========================================================================

  /**
   * Writes the given list of strings to the given output stream.
   * 
   * @param serialized
   *        The serialization of the PDF document.
   * @param os
   *        The stream to write to.
   * @param features The features to serialize.
   * @throws IOException
   *         If something went wrong while writing to the stream.
   */
  protected void writeTo(List<String> serialized, OutputStream os,
      Set<PdfFeature> features) throws IOException {
    if (serialized != null && os != null) {
      String delimiter;
      switch (features.iterator().next()) { // TODO
        case PARAGRAPH:
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

  // ==========================================================================

  /**
   * Checks if the given PDF element should be serialized or not, dependent on
   * the type and the role of the element.
   * 
   * @param element
   *        The PDF element to check.
   * @param features
   *        The features to serialize.
   * @param roles
   *        The roles to consider on serialization.
   * @return True, if the given PDF element should be serialized, false
   *         otherwise.
   */
  protected boolean isSerializePdfElement(PdfElement element,
      Set<PdfFeature> features, Set<PdfRole> roles) {
    // Check if the type of the given element was registered to be serialized.
    boolean isFeatureGiven = !CollectionUtils.isNullOrEmpty(features);
    if (isFeatureGiven && !features.contains(element.getFeature())) {
      return false;
    }

    // Check if the role of the given element was registered to be serialized.
    // TODO
//    boolean isRoleGiven = !CollectionUtils.isNullOrEmpty(roles);
//    if (isRoleGiven && !roles.contains(element.getRole())) {
//      return false;
//    }
    return true;
  }

  // ==========================================================================

  /**
   * Returns the output format of this serializer.
   * 
   * @return The output format of this serializer.
   */
  public static String getOutputFormat() {
    return "txt";
  }
}
