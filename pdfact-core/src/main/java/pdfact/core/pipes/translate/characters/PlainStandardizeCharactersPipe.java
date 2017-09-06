package pdfact.core.pipes.translate.characters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pdfact.core.model.Character;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.util.exception.PdfActException;

/**
 * A plain implementation of {@link StandardizeCharactersPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainStandardizeCharactersPipe
    implements StandardizeCharactersPipe {
  /**
   * A map that maps some characters to a character with the same semantic
   * meaning.
   */
  protected static final Map<String, String> CHARACTER_SYNONYMS;

  static {
    CHARACTER_SYNONYMS = new HashMap<String, String>();
    CHARACTER_SYNONYMS.put("\u2018", "'"); // ‘
    CHARACTER_SYNONYMS.put("\u2019", "'"); // ’
    CHARACTER_SYNONYMS.put("\u201b", "'"); // ‛
    CHARACTER_SYNONYMS.put("\u201c", "\""); // “
    CHARACTER_SYNONYMS.put("\u201d", "\""); // ”
    CHARACTER_SYNONYMS.put("\u201f", "\""); // ‟
    CHARACTER_SYNONYMS.put("\u301d", "\""); // 〝
    CHARACTER_SYNONYMS.put("\u301e", "\""); // 〞
    CHARACTER_SYNONYMS.put("\uff02", "\""); // ＂
    CHARACTER_SYNONYMS.put("\uff07", "'"); // ＇
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    standardizeCharacters(pdf);
    return pdf;
  }

  // ==========================================================================

  /**
   * Standardizes the characters of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void standardizeCharacters(PdfDocument pdf) {
    if (pdf != null) {
      List<Page> pages = pdf.getPages();
      if (pages != null) {
        for (Page page : pages) {
          standardizeCharacters(page);
        }
      }
    }
  }

  /**
   * Standardizes the characters of the given PDF page.
   * 
   * @param page
   *        The page to process.
   */
  protected void standardizeCharacters(Page page) {
    if (page == null) {
      return;
    }

    List<Character> characters = page.getCharacters();
    if (characters == null) {
      return;
    }

    for (Character character : characters) {
      standardizeCharacter(character);
    }
  }

  /**
   * Standardizes the given character.
   * 
   * @param ch
   *        The character to process.
   */
  protected void standardizeCharacter(Character ch) {
    if (ch == null) {
      return;
    }

    ch.setText(CHARACTER_SYNONYMS.getOrDefault(ch.getText(), ch.getText()));
  }
}
