package pdfact.cli.pipes.serialize;

import static pdfact.core.PdfActCoreSettings.DEFAULT_ENCODING;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import pdfact.cli.model.ExtractionUnit;
import pdfact.core.model.Character;
import pdfact.core.model.Color;
import pdfact.core.model.Document;
import pdfact.core.model.Element;
import pdfact.core.model.HasColor;
import pdfact.core.model.HasPosition;
import pdfact.core.model.HasSemanticRole;
import pdfact.core.model.HasText;
import pdfact.core.model.Paragraph;
import pdfact.core.model.Rectangle;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;
import pdfact.core.util.PdfActUtils;

/**
 * An implementation of {@link PdfSerializer} that serializes a PDF document
 * in TXT format.
 *
 * @author Claudius Korzen
 */
public class PdfTxtSerializer implements PdfSerializer {
  /**
   * The units to serialize.
   */
  protected Set<ExtractionUnit> extractionUnits;

  /**
   * The semantic roles to consider on serializing.
   */
  protected Set<SemanticRole> rolesFilter;

  /**
   * The delimiter to use on joining the serialized elements.
   */
  protected static final String TYPES_DELIMITER = System.lineSeparator()
      + System.lineSeparator();

  // ==============================================================================================

  /**
   * Creates a new serializer that serializes a PDF document in TXT format.
   */
  public PdfTxtSerializer() {

  }

  /**
   * Creates a new serializer that serializes a PDF document in TXT format.
   * 
   * @param extractionUnits
   *        The units to serialize.
   * @param rolesFilter
   *        The semantic roles to consider on serializing.
   */
  public PdfTxtSerializer(Set<ExtractionUnit> extractionUnits, Set<SemanticRole> rolesFilter) {
    this();
    this.extractionUnits = extractionUnits;
    this.rolesFilter = rolesFilter;
  }

  // ==============================================================================================

  @Override
  public byte[] serialize(Document pdf) {
    // The serialization to return.
    String result = "";

    if (pdf != null) {
      List<String> lines = new ArrayList<>();

      // Create the section that contains all serialized PDF elements.
      List<String> elementsLines = serializePdfElements(pdf);
      if (elementsLines != null) {
        lines.addAll(elementsLines);
      }
      lines.add("");

      result = PdfActUtils.join(lines, TYPES_DELIMITER);
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
  protected List<String> serializePdfElements(Document pdf) {
    List<String> lines = new ArrayList<>();
    for (ExtractionUnit unit : this.extractionUnits) {
      switch (unit) {
        case CHARACTER:
          lines.addAll(serializeCharacters(pdf));
          break;
        case WORD:
          lines.addAll(serializeWords(pdf));
          break;
        case PARAGRAPH:
        default:
          lines.addAll(serializeParagraphs(pdf));
      }
    }
    return lines;
  }

  // ==============================================================================================

  /**
   * Serializes the paragraphs of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeParagraphs(Document pdf) {
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

  // ==============================================================================================
  
  /**
   * Serializes the words of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeWords(Document pdf) {
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

  // ==============================================================================================
  
  /**
   * Serializes the characters of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeCharacters(Document pdf) {
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

  // ==============================================================================================

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

      if (element instanceof HasPosition && element instanceof  HasColor) {
        HasPosition hasPosition = (HasPosition) element;
        HasColor hasColor = (HasColor) element;
        Rectangle rect = hasPosition.getPosition().getRectangle();
        Color color = hasColor.getColor();
        result.add(text + " " + rect + " " + Arrays.toString(color.getRGB()));
      } else {
        result.add(text);
      }
    }

    return result;
  }

  // ==============================================================================================

  @Override
  public Set<ExtractionUnit> getExtractionUnits() {
    return this.extractionUnits;
  }

  @Override
  public void setExtractionUnits(Set<ExtractionUnit> units) {
    this.extractionUnits = units;
  }

  // ==============================================================================================

  @Override
  public Set<SemanticRole> getSemanticRolesFilter() {
    return this.rolesFilter;
  }

  @Override
  public void setSemanticRolesFilter(Set<SemanticRole> rolesFilter) {
    this.rolesFilter = rolesFilter;
  }

  // ==============================================================================================

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
  
  // ==============================================================================================
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
