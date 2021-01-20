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
import pdfact.core.model.HasPositions;
import pdfact.core.model.HasSemanticRole;
import pdfact.core.model.HasText;
import pdfact.core.model.Paragraph;
import pdfact.core.model.Position;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;
import pdfact.core.util.PdfActUtils;

/**
 * An implementation of {@link PdfSerializer} that serializes a PDF document in TXT format.
 *
 * @author Claudius Korzen
 */
public class PdfTxtSerializer implements PdfSerializer {
  /**
   * The control character to optionally insert when a page break occurs in the PDF.
   */
  protected static final char PAGE_BREAK_CONTROL_CHARACTER = '\f';

  /**
   * The control character to optionally insert in front of a heading.
   */
  protected static final char HEADING_CONTROL_CHARACTER = '\u0001';

  // ==============================================================================================

  /**
   * The default boolean flag indicating whether or not this serializer should insert control
   * characters, i.e.: "^L" between two PDF elements in case a page break between the two elements
   * occurs in the PDF and "^A" in front of headings.
   */
  protected boolean withControlCharacters;

  /**
   * The units to serialize.
   */
  protected Set<ExtractionUnit> extractionUnits;

  /**
   * The semantic roles to consider on serializing.
   */
  protected Set<SemanticRole> semanticRolesToInclude;

  /**
   * The position of the last serialized PDF element.
   */
  protected Position prevPosition;

  /**
   * The delimiter to use on joining the serialized elements.
   */
  protected static final String TYPES_DELIMITER = System.lineSeparator() + System.lineSeparator();

  // ==============================================================================================

  /**
   * Creates a new serializer that serializes a PDF document in TXT format.
   * 
   * @param withControlCharacters Whether or not control characters should be inserted.
   */
  public PdfTxtSerializer(boolean withControlCharacters) {
    this.withControlCharacters = withControlCharacters;
  }

  /**
   * Creates a new serializer that serializes a PDF document in TXT format.
   * 
   * @param withControlCharacters Whether or not control characters should be inserted.
   * @param extractionUnits       The units to serialize.
   * @param roles                 The semantic roles to include on serialization.
   */
  public PdfTxtSerializer(boolean withControlCharacters, Set<ExtractionUnit> extractionUnits,
          Set<SemanticRole> roles) {
    this(withControlCharacters);
    this.extractionUnits = extractionUnits;
    this.semanticRolesToInclude = roles;
  }

  // ==============================================================================================

  @Override
  public byte[] serialize(Document pdf) {
    this.prevPosition = null;

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
   * @param pdf The PDF document to process.
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
   * @param pdf The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeParagraphs(Document pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      for (Paragraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role should not be extracted.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        List<String> paragraphLines = serializeParagraph(paragraph);
        if (paragraphLines != null) {
          for (String paragraphLine : paragraphLines) {      
            // Check if we have to insert control character for the headings.
            if (withControlCharacters) {
              if (paragraph.getSemanticRole() == SemanticRole.HEADING) {
                paragraphLine = HEADING_CONTROL_CHARACTER + paragraphLine;
              }
            }
            result.add(paragraphLine);
          }
        }
      }
    }

    return result;
  }

  /**
   * Serializes the given paragraph.
   * 
   * @param paragraph The paragraph to serialize.
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
   * @param pdf The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeWords(Document pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      for (Paragraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role should not be extracted.
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
   * @param word The word to serialize.
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
   * @param pdf The PDF document to process.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeCharacters(Document pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      for (Paragraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role should not be extracted.
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
   * @param character The character to serialize.
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
   * @param element The PDF element to serialize.
   *
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializePdfElement(Element element) {
    List<String> result = new ArrayList<>();

    Position position = null;
    Color color = null;

    if (element instanceof HasText) {
      HasText hasText = (HasText) element;
      String text = hasText.getText();

      // Determine the (first) position of the element.
      if (element instanceof HasPositions) {
        position = ((HasPositions) element).getFirstPosition();
      }
      if (element instanceof HasPosition) {
        position = ((HasPosition) element).getPosition();
      }

      // Determine the color of the element.
      if (element instanceof HasColor) {
        color = ((HasColor) element).getColor();
      }

      // Check if we have to insert the control characters that marks page breaks.
      if (withControlCharacters) {
        // Insert the page break control characters between the previous element and the current
        // element if the page numbers of both elements differ.
        if (prevPosition != null && position != null) {
          if (prevPosition.getPageNumber() != position.getPageNumber()) {
            result.add(java.lang.Character.toString(PAGE_BREAK_CONTROL_CHARACTER));
          }
        }
      }

      if (position != null && color != null) {
        result.add(text + " " + position.getRectangle() + " " + Arrays.toString(color.getRGB()));
      } else {
        result.add(text);
      }
    }

    // Keep track of the position of this element, in order to decide if we have to insert a
    // page separator between this element and the next element.
    this.prevPosition = position;

    return result;
  }

  // ==============================================================================================

  /**
   * Returns the boolean flag indicating whether or not this serializer should insert control
   * characters, i.e.: "^L" between two PDF elements in case a page break between the two elements
   * occurs in the PDF and "^A" in front of headings.
   */
  public boolean isWithControlCharacters() {
    return this.withControlCharacters;
  }

  /**
   * Sets the boolean flag indicating whether or not this serializer should insert control
   * characters, i.e.: "^L" between two PDF elements in case a page break between the two elements
   * occurs in the PDF and "^A" in front of headings.
   */
  public void setWithControlCharacters(boolean withControlCharacters) {
    this.withControlCharacters = withControlCharacters;
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
  public Set<SemanticRole> getSemanticRolesToInclude() {
    return this.semanticRolesToInclude;
  }

  @Override
  public void setSemanticRolesToInclude(Set<SemanticRole> roles) {
    this.semanticRolesToInclude = roles;
  }

  // ==============================================================================================

  /**
   * Checks if the semantic role of the given element is relevant, that is: if it is included in
   * this.semanticRolesToInclude.
   * 
   * @param element The element to check.
   * 
   * @return True, if the role of the given element is relevant.
   */
  protected boolean hasRelevantRole(HasSemanticRole element) {
    if (element == null) {
      return false;
    }

    if (this.semanticRolesToInclude == null || this.semanticRolesToInclude.isEmpty()) {
      // No semantic roles to include given -> The element is not relevant.
      return false;
    }

    SemanticRole role = element.getSemanticRole();
    if (role == null) {
      return false;
    }

    return this.semanticRolesToInclude.contains(role);
  }

  // ==============================================================================================
  // Remaining methods.

  /**
   * Serializes the given text block.
   * 
   * @param block The text block to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeTextBlock(TextBlock block) {
    return serializePdfElement(block);
  }


  /**
   * Serializes the given text line.
   * 
   * @param line The text line to serialize.
   * 
   * @return A list of strings that represent the lines of the serialization.
   */
  protected List<String> serializeTextLine(TextLine line) {
    return serializePdfElement(line);
  }
}
