package pdfact.core.pipes.filter.characters;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pdfact.core.model.Character;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.ElementList;

/**
 * A plain implementation of {@link FilterCharactersPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainFilterCharactersPipe implements FilterCharactersPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PlainFilterCharactersPipe.class);

  /**
   * The number of processed characters.
   */
  protected int numProcessedCharacters;

  /**
   * The number of filtered characters.
   */
  protected int numFilteredCharacters;

  // ==============================================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Filtering characters.");
    filterCharacters(pdf);

    log.debug("Filtering characters done.");
    log.debug("# processed characters: " + this.numProcessedCharacters);
    log.debug("# filtered characters : " + this.numFilteredCharacters);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  // ==============================================================================================

  /**
   * Filters those characters of a PDF document that should not be considered.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void filterCharacters(PdfDocument pdf) {
    if (pdf != null) {
      List<Page> pages = pdf.getPages();
      for (Page page : pages) {
        ElementList<Character> before = page.getCharacters();
        // Create a new list of characters which should not be filtered.
        ElementList<Character> after = new ElementList<>(before.size());
        for (Character character : before) {
          this.numProcessedCharacters++;

          if (isFilterCharacter(character)) {
            this.numFilteredCharacters++;
            continue;
          }

          after.add(character);
        }
        page.setCharacters(after);
      }
    }
  }

  /**
   * Checks if the given characters should be filtered out.
   * 
   * @param character
   *        The character to check.
   * 
   * @return True if the given PDF character should be filtered out; False
   *         otherwise.
   */
  public static boolean isFilterCharacter(Character character) {
    // Ignore the character, if it is null.
    if (character == null) {
      return true;
    }

    // Ignore the character, if the width of its bounding box is <= 0.
    if (character.getPosition().getRectangle().getWidth() <= 0) {
      return true;
    }

    // Ignore the character, if the height of its bounding box is <= 0.
    if (character.getPosition().getRectangle().getHeight() <= 0) {
      return true;
    }

    // Ignore the the character, if its text is empty.
//    String text = character.getText();
//    if (text == null || text.trim().isEmpty()) {
//      return true;
//    }
    return false;
  }
}
