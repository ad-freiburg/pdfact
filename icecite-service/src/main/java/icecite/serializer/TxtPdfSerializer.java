package icecite.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.HasText;
import icecite.models.PdfCharacter;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfType;
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
   * The types of the PDF elements to serialize.
   */
  protected Set<PdfType> types;

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
    this.types = new HashSet<>();
    this.types.add(PdfType.CHARACTERS);
    this.roles = new HashSet<>();
  }

  /**
   * Creates a new serializer that serializes PDF documents to TXT format.
   * 
   * @param types
   *        The types of the PDF elements to serialize.
   */
  @AssistedInject
  public TxtPdfSerializer(@Assisted Set<PdfType> types) {
    this.types = types;
    this.roles = new HashSet<>();
  }

  /**
   * Creates a new serializer that serializes PDF documents to TXT format.
   * 
   * @param types
   *        The types of the PDF elements to serialize.
   * @param roles
   *        The roles of the PDF elements to serialize.
   */
  @AssistedInject
  public TxtPdfSerializer(@Assisted Set<PdfType> types,
      @Assisted Set<PdfRole> roles) {
    this.types = types;
    this.roles = roles;
  }

  // ==========================================================================

  @Override
  public void serialize(PdfDocument pdf, OutputStream os) throws IOException {
    if (pdf == null) {
      return;
    }
    if (os == null) {
      return;
    }
    
    // Serialize the pages.
    List<String> serializedPages = serializePages(pdf);

    // Write the serialization to the given stream.
    writeTo(serializedPages, os);
  }

  // ==========================================================================

  /**
   * Serializes the pages of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to serialize.
   * @return The text lines that represent the serialized pages of the PDF
   *         document.
   */
  protected List<String> serializePages(PdfDocument pdf) {
    if (pdf == null) {
      return null;
    }

    List<PdfPage> pages = pdf.getPages();
    if (pages == null || pages.isEmpty()) {
      return null;
    }
    
    List<String> serializedPages = new ArrayList<>();
    for (PdfPage page : pages) {
      List<String> serializedPage = serializePage(page);
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
   * @return A list of strings, representing the serialized page.
   */
  protected List<String> serializePage(PdfPage page) {
    if (page == null) {
      return null;
    }

    List<String> lines = new ArrayList<String>();
    // Serialize the text elements.
    for (PdfTextBlock block : page.getTextBlocks()) {
      if (isSerializePdfElement(block)) {
        lines.add(serializePdfElement(block));
      }
      for (PdfTextLine line : block.getTextLines()) {
        if (isSerializePdfElement(line)) {
          lines.add(serializePdfElement(line));
        }
        for (PdfWord word : line.getWords()) {
          if (isSerializePdfElement(word)) {
            lines.add(serializePdfElement(word));
          }
          for (PdfCharacter character : word.getCharacters()) {
            if (isSerializePdfElement(character)) {
              lines.add(serializePdfElement(character));
            }
          }
        }
      }
    }

    // Serialize the graphical elements.
    for (PdfFigure figure : page.getFigures()) {
      if (isSerializePdfElement(figure)) {
        lines.add(serializePdfElement(figure));
      }
    }
    for (PdfShape shape : page.getShapes()) {
      if (isSerializePdfElement(shape)) {
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
   * @throws IOException
   *         If something went wrong while writing to the stream.
   */
  protected void writeTo(List<String> serialized, OutputStream os)
      throws IOException {
    if (serialized != null && os != null) {
      String delimiter;
      switch (this.types.iterator().next()) { // TODO
        case PARAGRAPHS:
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
   * @return True, if the given PDF element should be serialized, false
   *         otherwise.
   */
  protected boolean isSerializePdfElement(PdfElement element) {
    // Check if the type of the given element was registered to be serialized.
    boolean isTypeGiven = !CollectionUtils.isNullOrEmpty(this.types);
    if (isTypeGiven && !this.types.contains(element.getType())) {
      return false;
    }

    // Check if the role of the given element was registered to be serialized.
    boolean isRoleGiven = !CollectionUtils.isNullOrEmpty(this.roles);
    if (isRoleGiven && !this.roles.contains(element.getRole())) {
      return false;
    }
    return true;
  }
}
