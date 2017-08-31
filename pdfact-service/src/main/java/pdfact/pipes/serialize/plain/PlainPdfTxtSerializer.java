package pdfact.pipes.serialize.plain;

import static pdfact.PdfActCoreSettings.DEFAULT_ENCODING;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.model.Character;
import pdfact.model.Element;
import pdfact.model.HasSemanticRole;
import pdfact.model.HasText;
import pdfact.model.Paragraph;
import pdfact.model.PdfDocument;
import pdfact.model.SemanticRole;
import pdfact.model.TextBlock;
import pdfact.model.TextLine;
import pdfact.model.TextUnit;
import pdfact.model.Word;
import pdfact.pipes.serialize.PdfTxtSerializer;
import pdfact.util.PdfActUtils;

/**
 * An implementation of {@link PdfTxtSerializer} that serializes a PDF document
 * in TXT format.
 *
 * @author Claudius Korzen
 */
public class PlainPdfTxtSerializer implements PdfTxtSerializer {
  /**
   * The text unit.
   */
  protected TextUnit textUnit;

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
   * @param textUnit
   *        The text unit.
   * @param rolesFilter
   *        The semantic roles to consider on serializing.
   */
  @AssistedInject
  public PlainPdfTxtSerializer(
      @Assisted TextUnit textUnit,
      @Assisted Set<SemanticRole> rolesFilter) {
    this();
    this.textUnit = textUnit;
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

      result = PdfActUtils.join(elementsLines, TYPES_DELIMITER);
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
    switch (this.textUnit) {
      case CHARACTER:
        return serializeCharacters(pdf);
      case WORD:
        return serializeWords(pdf);
      case PARAGRAPH:
      default:
        return serializeParagraphs(pdf);
    }
  }

  // ==========================================================================

  /**
   * Serializes the paragraphs of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeParagraphs(PdfDocument pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      for (Paragraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        List<String> paragraphLines = serializeParagraph(paragraph);
        if (paragraphLines != null) {
          result.addAll(paragraphLines);
        }
      }
    }

    return result;
  }
  
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

  // ==========================================================================
  
  /**
   * Serializes the words of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeWords(PdfDocument pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      for (Paragraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        for (Word word : paragraph.getWords()) {
          List<String> wordLines = serializeWord(word);
          if (wordLines != null) {
            result.addAll(wordLines);
          }
        }
      }
    }

    return result;
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

  // ==========================================================================
  
  /**
   * Serializes the characters of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeCharacters(PdfDocument pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      for (Paragraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        for (Word word : paragraph.getWords()) {
          for (Character character : word.getCharacters()) {
            List<String> characterLines = serializeCharacter(character);
            if (characterLines != null) {
              result.addAll(characterLines);
            }
          }
        }
      }
    }

    return result;
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
  public TextUnit getTextUnit() {
    return this.textUnit;
  }

  @Override
  public void setTextUnit(TextUnit textUnit) {
    this.textUnit = textUnit;
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
  
  // ==========================================================================
  // Remaining methods.
  
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
}
