package pdfact.serialize;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfDocument;
import pdfact.models.PdfElement;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfTextUnit;
import pdfact.models.PdfWord;
import pdfact.utils.collection.CollectionUtils;

/**
 * An implementation of PdfSerializer that serializes a PDF document to TXT
 * format.
 *
 * @author Claudius Korzen
 */
public class TxtPdfSerializer implements PdfSerializer {
  /**
   * The text units to include on serialization.
   */
  protected Set<PdfTextUnit> units;

  /**
   * The roles of text units to include on serialization.
   */
  protected Set<PdfRole> roles;

  // ==========================================================================

  /**
   * Creates a new serializer that serializes PDF documents to TXT format.
   */
  @AssistedInject
  public TxtPdfSerializer() {

  }

  /**
   * Creates a new serializer that serializes PDF documents to TXT format.
   * 
   * @param units
   *        The text units to include on serialization.
   * @param roles
   *        The roles of text units to include on serialization.
   */
  @AssistedInject
  public TxtPdfSerializer(Set<PdfTextUnit> units, Set<PdfRole> roles) {
    this.units = units;
    this.roles = roles;
  }

  // ==========================================================================

  /**
   * Returns the output format of this serializer.
   * 
   * @return The output format of this serializer.
   */
  public static PdfActSerializationFormat getSerializationFormat() {
    return PdfActSerializationFormat.TXT;
  }

  // ==========================================================================

  @Override
  public byte[] serialize(PdfDocument pdf) {
    if (pdf == null) {
      return "".getBytes(StandardCharsets.UTF_8);
    }

    List<String> lines = new ArrayList<>();

    for (PdfParagraph paragraph : pdf.getParagraphs()) {
      // Ignore the paragraph if its role doesn't match the roles filter.
      if (!hasRelevantRole(paragraph)) {
        continue;
      }

      // Serialize the paragraph if paragraphs should be included.
      if (isRelevantTextUnit(paragraph)) {
        lines.add(paragraph.getText());
      }

      for (PdfTextBlock block : paragraph.getTextBlocks()) {
        // Serialize the text block if text blocks should be included.
        if (isRelevantTextUnit(block)) {
          lines.add(block.getText());
        }
        for (PdfTextLine line : block.getTextLines()) {
          // Serialize the text line if text lines should be included.
          if (isRelevantTextUnit(line)) {
            lines.add(line.getText());
          }
          for (PdfWord word : line.getWords()) {
            // Serialize the word if words should be included.
            if (isRelevantTextUnit(word)) {
              lines.add(word.getText());
            }
            for (PdfCharacter character : word.getCharacters()) {
              // Serialize the character if characters should be included.
              if (isRelevantTextUnit(character)) {
                lines.add(character.getText());
              }
            }
          }
        }
      }
    }
    return CollectionUtils.join(lines, "\n\n").getBytes(StandardCharsets.UTF_8);
  }

  // ==========================================================================

  @Override
  public Set<PdfTextUnit> getTextUnits() {
    return this.units;
  }

  @Override
  public void setTextUnits(Set<PdfTextUnit> units) {
    this.units = units;
  }

  // ==========================================================================

  @Override
  public Set<PdfRole> getRoles() {
    return this.roles;
  }

  @Override
  public void setRoles(Set<PdfRole> roles) {
    this.roles = roles;
  }

  // ==========================================================================

  /**
   * Checks if the semantic role of the given text paragraph matches the
   * semantic roles filter of this serializer.
   * 
   * @param paragraph
   *        The text paragraph to check.
   * @return True, if the given paragraph matches the semantic roles filter of
   *         this serializer, false otherwise.
   */
  protected boolean hasRelevantRole(PdfParagraph paragraph) {
    if (paragraph == null) {
      return false;
    }

    if (this.roles == null || this.roles.isEmpty()) {
      return true;
    }

    PdfRole role = paragraph.getRole();
    if (role == null) {
      return false;
    }

    return this.roles.contains(role);
  }

  /**
   * Checks if the text unit of the given PDF element matches the text unit
   * filter of this serializer.
   * 
   * @param element
   *        The PDF element to check.
   *
   * @return True, if the text unit of the given PDF element matches the text
   *         unit filter of this serializer, false otherwise.
   */
  protected boolean isRelevantTextUnit(PdfElement element) {
    if (element == null) {
      return false;
    }

    if (this.units == null || this.units.isEmpty()) {
      return true;
    }

    return this.units.contains(element.getTextUnit());
  }
}
