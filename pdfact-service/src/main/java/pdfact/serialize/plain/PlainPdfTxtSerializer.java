package pdfact.serialize.plain;

import static pdfact.PdfActCoreSettings.DEFAULT_ENCODING;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.HasElementType;
import pdfact.models.HasRole;
import pdfact.models.HasText;
import pdfact.models.PdfCharacter;
import pdfact.models.PdfDocument;
import pdfact.models.PdfElement;
import pdfact.models.PdfElementType;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfWord;
import pdfact.serialize.PdfSerializer;
import pdfact.serialize.PdfTxtSerializer;
import pdfact.utils.collection.CollectionUtils;

/**
 * An implementation of {@link PdfSerializer} that serializes a PDF document in
 * TXT format.
 *
 * @author Claudius Korzen
 */
public class PlainPdfTxtSerializer implements PdfTxtSerializer {
  /**
   * The element types to consider on serializing.
   */
  protected Set<PdfElementType> typesFilter;

  /**
   * The semantic roles to consider on serializing.
   */
  protected Set<PdfRole> rolesFilter;

  /**
   * The delimiter to use on joining the serialized elements.
   */
  protected static final String TYPES_DELIMITER = System.lineSeparator()
      + System.lineSeparator();

  // ==========================================================================

  /**
   * Creates a new serializer that serializes a PDF document in TXT format.
   */
  @AssistedInject
  public PlainPdfTxtSerializer() {

  }

  /**
   * Creates a new serializer that serializes a PDF document in TXT format.
   * 
   * @param typesFilter
   *        The element types to consider on serializing.
   * @param rolesFilter
   *        The semantic roles to consider on serializing.
   */
  @AssistedInject
  public PlainPdfTxtSerializer(
      @Assisted Set<PdfElementType> typesFilter,
      @Assisted Set<PdfRole> rolesFilter) {
    this();
    this.typesFilter = typesFilter;
    this.rolesFilter = rolesFilter;
  }

  // ==========================================================================

  @Override
  public byte[] serialize(PdfDocument pdf) {
    // The serialization to return.
    String result = "";

    if (pdf != null) {
      List<String> lines = new ArrayList<>();

      // Create the section that contains all serialized PDF elements.
      List<String> elementsLines = serializePdfElements(pdf);
      if (elementsLines != null) {
        lines.addAll(elementsLines);
      }

      result = CollectionUtils.join(elementsLines, TYPES_DELIMITER);
    }

    return result.getBytes(DEFAULT_ENCODING);
  }

  /**
   * Serializes the elements of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   *
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializePdfElements(PdfDocument pdf) {
    // The serialized elements.
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      for (PdfParagraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        // Serialize the paragraph (if types filter includes paragraphs).
        if (hasRelevantElementType(paragraph)) {
          List<String> paragraphLines = serializeParagraph(paragraph);
          if (paragraphLines != null) {
            result.addAll(paragraphLines);
          }
        }

        for (PdfTextBlock block : paragraph.getTextBlocks()) {
          // Serialize the text block (if types filter includes text blocks).
          if (hasRelevantElementType(block)) {
            List<String> blockLines = serializeTextBlock(block);
            if (blockLines != null) {
              result.addAll(blockLines);
            }
          }
          for (PdfTextLine line : block.getTextLines()) {
            // Serialize the text line (if types filter includes text lines).
            if (hasRelevantElementType(line)) {
              List<String> lineLines = serializeTextLine(line);
              if (lineLines != null) {
                result.addAll(lineLines);
              }
            }
            for (PdfWord word : line.getWords()) {
              // Serialize the word (if types filter includes words).
              if (hasRelevantElementType(word)) {
                List<String> wordLines = serializeWord(word);
                if (wordLines != null) {
                  result.addAll(wordLines);
                }
              }
              for (PdfCharacter c : word.getCharacters()) {
                // Serialize the char (if types filter includes text chars).
                if (hasRelevantElementType(c)) {
                  List<String> characterLines = serializeCharacter(c);
                  if (characterLines != null) {
                    result.addAll(characterLines);
                  }
                }
              }
            }
          }
        }
      }
    }
    return result;
  }

  // ==========================================================================

  /**
   * Serializes the given paragraph.
   * 
   * @param paragraph
   *        The paragraph to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeParagraph(PdfParagraph paragraph) {
    return serializePdfElement(paragraph);
  }

  /**
   * Serializes the given text block.
   * 
   * @param block
   *        The text block to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeTextBlock(PdfTextBlock block) {
    return serializePdfElement(block);
  }

  /**
   * Serializes the given text line.
   * 
   * @param line
   *        The text line to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeTextLine(PdfTextLine line) {
    return serializePdfElement(line);
  }

  /**
   * Serializes the given word.
   * 
   * @param word
   *        The word to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeWord(PdfWord word) {
    return serializePdfElement(word);
  }

  /**
   * Serializes the given character.
   * 
   * @param character
   *        The character to serialize.
   *
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeCharacter(PdfCharacter character) {
    return serializePdfElement(character);
  }

  // ==========================================================================

  /**
   * Serializes the given PDF element.
   * 
   * @param element
   *        The PDF element to serialize.
   *
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializePdfElement(PdfElement element) {
    List<String> result = new ArrayList<>();

    if (element instanceof HasText) {
      HasText hasText = (HasText) element;
      String text = hasText.getText();

      result.add(text);
    }

    return result;
  }

  // ==========================================================================

  @Override
  public Set<PdfElementType> getElementTypesFilter() {
    return this.typesFilter;
  }

  @Override
  public void setElementTypesFilter(Set<PdfElementType> typesFilter) {
    this.typesFilter = typesFilter;
  }

  // ==========================================================================

  @Override
  public Set<PdfRole> getSemanticRolesFilter() {
    return this.rolesFilter;
  }

  @Override
  public void setSemanticRolesFilter(Set<PdfRole> rolesFilter) {
    this.rolesFilter = rolesFilter;
  }

  // ==========================================================================

  /**
   * Checks if the semantic role of the given element matches the semantic
   * roles filter of this serializer.
   * 
   * @param element
   *        The element to check.
   * 
   * @return True, if the role of the given element matches the semantic roles
   *         filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantRole(HasRole element) {
    if (element == null) {
      return false;
    }

    if (this.rolesFilter == null || this.rolesFilter.isEmpty()) {
      // No filter is given -> The element is relevant.
      return true;
    }

    PdfRole role = element.getRole();
    if (role == null) {
      return false;
    }

    return this.rolesFilter.contains(role);
  }

  /**
   * Checks if the type of the given PDF element matches the element type
   * filter of this serializer.
   * 
   * @param element
   *        The PDF element to check.
   *
   * @return True, if the type of the given PDF element matches the element
   *         type filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantElementType(HasElementType element) {
    if (element == null) {
      return false;
    }

    if (this.typesFilter == null || this.typesFilter.isEmpty()) {
      // No filter is given -> The element is relevant.
      return true;
    }

    PdfElementType elementType = element.getType();
    if (elementType == null) {
      return false;
    }

    return this.typesFilter.contains(elementType);
  }
}
