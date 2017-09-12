package pdfact.core.pipes.translate.characters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import pdfact.core.model.Character;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.log.InjectLogger;

/**
 * A plain implementation of {@link StandardizeCharactersPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainStandardizeCharactersPipe
    implements StandardizeCharactersPipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * A map that maps some characters to a character with the same semantic
   * meaning.
   */
  protected static final Map<String, String> CHARACTER_SYNONYMS;

  /**
   * The number of processed characters.
   */
  protected int numProcessedCharacters;

  /**
   * The number of standardized characters.
   */
  protected int numStandardizedCharacters;

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
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Standardizing the characters.");
    standardizeCharacters(pdf);

    log.debug("Standardizing the characters done.");
    log.debug("# processed characters: " + this.numProcessedCharacters);
    log.debug("# standardized characters: " + this.numStandardizedCharacters);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
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

    if (CHARACTER_SYNONYMS.containsKey(ch.getText())) {
      ch.setText(CHARACTER_SYNONYMS.get(ch.getText()));
      this.numStandardizedCharacters++;
    }
    this.numProcessedCharacters++;
  }
}
