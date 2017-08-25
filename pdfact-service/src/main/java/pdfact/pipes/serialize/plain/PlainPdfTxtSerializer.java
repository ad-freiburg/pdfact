package pdfact.pipes.serialize.plain;

import static pdfact.PdfActCoreSettings.DEFAULT_ENCODING;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.model.HasElementType;
import pdfact.model.HasSemanticRole;
import pdfact.model.HasText;
import pdfact.model.PdfDocument;
import pdfact.model.Character;
import pdfact.model.Element;
import pdfact.model.ElementType;
import pdfact.model.Paragraph;
import pdfact.model.SemanticRole;
import pdfact.model.TextBlock;
import pdfact.model.TextLine;
import pdfact.model.Word;
import pdfact.pipes.serialize.PdfTxtSerializer;
import pdfact.util.CollectionUtils;

/**
 * An implementation of {@link PdfTxtSerializer} that serializes a PDF document
 * in TXT format.
 *
 * @author Claudius Korzen
 */
public class PlainPdfTxtSerializer implements PdfTxtSerializer {
  /**
   * The element types to consider on serializing.
   */
  protected Set<ElementType> typesFilter;

  /**
   * The semantic roles to consider on serializing.
   */
  protected Set<SemanticRole> rolesFilter;

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
      @Assisted Set<ElementType> typesFilter,
      @Assisted Set<SemanticRole> rolesFilter) {
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
      for (Paragraph paragraph : pdf.getParagraphs()) {
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

        for (Word word : paragraph.getWords()) {
          // Serialize the word (if types filter includes words).
          if (hasRelevantElementType(word)) {
            List<String> wordLines = serializeWord(word);
            if (wordLines != null) {
              result.addAll(wordLines);
            }
          }
          for (Character c : word.getCharacters()) {
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
  protected List<String> serializeParagraph(Paragraph paragraph) {
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
  protected List<String> serializeTextBlock(TextBlock block) {
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
  protected List<String> serializeTextLine(TextLine line) {
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
  protected List<String> serializeWord(Word word) {
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
  protected List<String> serializeCharacter(Character character) {
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
  protected List<String> serializePdfElement(Element element) {
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
  public Set<ElementType> getElementTypesFilter() {
    return this.typesFilter;
  }

  @Override
  public void setElementTypesFilter(Set<ElementType> typesFilter) {
    this.typesFilter = typesFilter;
  }

  // ==========================================================================

  @Override
  public Set<SemanticRole> getSemanticRolesFilter() {
    return this.rolesFilter;
  }

  @Override
  public void setSemanticRolesFilter(Set<SemanticRole> rolesFilter) {
    this.rolesFilter = rolesFilter;
  }

  // ==========================================================================

  /**
   * Checks if the semantic role of the given element matches the semantic roles
   * filter of this serializer.
   * 
   * @param element
   *        The element to check.
   * 
   * @return True, if the role of the given element matches the semantic roles
   *         filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantRole(HasSemanticRole element) {
    if (element == null) {
      return false;
    }

    if (this.rolesFilter == null || this.rolesFilter.isEmpty()) {
      // No filter is given -> The element is relevant.
      return true;
    }

    SemanticRole role = element.getSemanticRole();
    if (role == null) {
      return false;
    }

    return this.rolesFilter.contains(role);
  }

  /**
   * Checks if the type of the given PDF element matches the element type filter
   * of this serializer.
   * 
   * @param element
   *        The PDF element to check.
   *
   * @return True, if the type of the given PDF element matches the element type
   *         filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantElementType(HasElementType element) {
    if (element == null) {
      return false;
    }

    if (this.typesFilter == null || this.typesFilter.isEmpty()) {
      // No filter is given -> The element is relevant.
      return true;
    }

    ElementType elementType = element.getType();
    if (elementType == null) {
      return false;
    }

    return this.typesFilter.contains(elementType);
  }
}
