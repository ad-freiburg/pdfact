package icecite.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import icecite.models.HasText;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfPage;
import icecite.models.PdfTextLine;

/**
 * An implementation of PdfSerializer that serializes a PDF document to plain
 * TXT format.
 *
 * @author Claudius Korzen
 */
public class TxtPdfSerializer implements PdfSerializer {
  @Override
  public void serialize(PdfDocument pdf, OutputStream os) throws IOException {
    Set<Class<? extends PdfElement>> types = new HashSet<>();

    // types.add(PdfParagraph.class);
    types.add(PdfTextLine.class);
    // types.add(PdfWord.class);
    // types.add(PdfCharacter.class);
    // types.add(PdfFigure.class);
    // types.add(PdfShape.class);

    serialize(pdf, types, os);
  }

  @Override
  public void serialize(PdfDocument pdf, Set<Class<? extends PdfElement>> types,
      OutputStream os) throws IOException {
    // Serialize the pages.
    List<String> serialized = serializePages(pdf, types);

    // Write the serialization to given stream.
    writeTo(serialized, os);
  }

  // ==========================================================================

  /**
   * Serializes the pages of the given document.
   * 
   * @param doc
   *        The PDF document to serialize.
   * @param types
   *        The types of PDF elements to serialize.
   * @return The text lines representing the serialized PDF document.
   */
  protected List<String> serializePages(PdfDocument doc,
      Set<Class<? extends PdfElement>> types) {
    if (doc == null) {
      return null;
    }

    List<PdfPage> pages = doc.getPages();

    if (pages == null || pages.isEmpty()) {
      return null;
    }

    List<String> serializedPages = new ArrayList<>();
    // TODO
    // for (PdfPage page : pages) {
    // List<String> serialized = serializePage(page);
    // if (serialized != null && !serialized.isEmpty()) {
    // serializedPages.addAll(serialized);
    // }
    // }
    return serializedPages;
  }

  /**
   * Serializes the given page.
   */
  // TODO
  // protected List<String> serializePage(PdfPage page) {
  // if (page == null) {
  // return null;
  // }

  // PdfFeature feature = getFeatureToSerialize();
  // List<? extends PdfElement> els = page.getElementsByFeature(feature);
  //
  // return serializeElementsWithRespectToRoles(els);
  // }

  /**
   * Serializes the given PDF element.
   * 
   * @param element
   *        The element to serialize.
   * @return The serialization of the element.
   */
  protected String serializePdfElement(PdfElement element) {
    if (element == null) {
      return null;
    }

    if (element instanceof HasText) {
      HasText hasText = (HasText) element;
      String text = hasText.getText();

      if (text == null || text.trim().isEmpty()) {
        return null;
      }

      return text;
    }

    return null;
  }

  // ==========================================================================

  /**
   * Writes the given json object to the given output stream.
   * 
   * @param serialized
   *        The serialization of the PDF document.
   * @param os
   *        The stream to write to.
   * @throws IOException
   *         If something went wrong while writing to the stream.
   */
  // TODO
  protected void writeTo(List<String> serialized, OutputStream os)
      throws IOException {
    // if (serialized != null && os != null) {
    // String delimiter;
    // switch (getFeatureToSerialize()) {
    // case paragraphs:
    // delimiter = "\n\n";
    // break;
    // default:
    // delimiter = " ";
    // break;
    // }
    // String string = CollectionUtils.join(serialized, delimiter);
    // os.write(string.getBytes(StandardCharsets.UTF_8));
    // }
  }
}
