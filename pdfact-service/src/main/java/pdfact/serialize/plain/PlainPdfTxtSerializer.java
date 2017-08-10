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
   * The element types filter.
   */
  protected Set<PdfElementType> typesFilter;

  /**
   * The semantic roles filter.
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
   *        The element types filter.
   * @param rolesFilter
   *        The semantic roles filter.
   */
  @AssistedInject
  public PlainPdfTxtSerializer(@Assisted Set<PdfElementType> typesFilter,
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
      // The serialized elements.
      List<String> elements = new ArrayList<>();

      for (PdfParagraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        // Serialize the paragraph if paragraphs match the types filter.
        if (hasRelevantElementType(paragraph)) {
          List<String> paragraphLines = serializeParagraph(paragraph);
          if (paragraphLines != null) {
            elements.addAll(paragraphLines);
          }
        }

        for (PdfTextBlock block : paragraph.getTextBlocks()) {
          // Serialize the text block if text blocks match the types filter.
          if (hasRelevantElementType(block)) {
            List<String> blockLines = serializeTextBlock(block);
            if (blockLines != null) {
              elements.addAll(blockLines);
            }
          }
          for (PdfTextLine line : block.getTextLines()) {
            // Serialize the text line if text lines match the types filter.
            if (hasRelevantElementType(line)) {
              List<String> lineLines = serializeTextLine(line);
              if (lineLines != null) {
                elements.addAll(lineLines);
              }
            }
            for (PdfWord word : line.getWords()) {
              // Serialize the word if words match the types filter.
              if (hasRelevantElementType(word)) {
                List<String> wordLines = serializeWord(word);
                if (wordLines != null) {
                  elements.addAll(wordLines);
                }
              }
              for (PdfCharacter c : word.getCharacters()) {
                // Serialize the character if characters match the type filter.
                if (hasRelevantElementType(c)) {
                  List<String> characterLines = serializeCharacter(c);
                  if (characterLines != null) {
                    elements.addAll(characterLines);
                  }
                }
              }
            }
          }
        }
      }
      
      // Join the units to a single string.
      result = CollectionUtils.join(elements, TYPES_DELIMITER);
    }

    return result.getBytes(DEFAULT_ENCODING);
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
  public Set<PdfElementType> getElementTypeFilters() {
    return this.typesFilter;
  }

  @Override
  public void setElementTypeFilters(Set<PdfElementType> types) {
    this.typesFilter = types;
  }

  // ==========================================================================

  @Override
  public Set<PdfRole> getElementRoleFilters() {
    return this.rolesFilter;
  }

  @Override
  public void setElementRoleFilters(Set<PdfRole> roles) {
    this.rolesFilter = roles;
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
      // No filter is given -> The paragraph is relevant.
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

    PdfElementType textUnit = element.getType();
    if (textUnit == null) {
      return false;
    }

    return this.typesFilter.contains(textUnit);
  }
}
