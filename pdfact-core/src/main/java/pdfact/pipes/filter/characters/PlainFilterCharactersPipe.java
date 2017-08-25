package pdfact.pipes.filter.characters;

import java.util.List;

import com.google.inject.Inject;

import pdfact.model.Character;
import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.CharacterList;
import pdfact.util.list.CharacterList.CharacterListFactory;

/**
 * A plain implementation of {@link FilterCharactersPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainFilterCharactersPipe implements FilterCharactersPipe {
  /**
   * The factory to create instances of {@link CharacterList}.
   */
  protected CharacterListFactory characterListFactory;

  /**
   * Creates a pipe that filters those characters of a PDF document that should
   * not be considered.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link CharacterList}.
   */
  @Inject
  public PlainFilterCharactersPipe(CharacterListFactory characterListFactory) {
    this.characterListFactory = characterListFactory;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    filterCharacters(pdf);
    return pdf;
  }

  // ==========================================================================
  
  /**
   * Filters those characters of a PDF document that should not be considered.
   * 
   * @param pdf The PDF document to process.
   */
  protected void filterCharacters(PdfDocument pdf) {
    if (pdf != null) {
      List<Page> pages = pdf.getPages();
      for (Page page : pages) {
        CharacterList before = page.getCharacters();
        // Create a new list of characters which should not be filtered.
        CharacterList after = this.characterListFactory.create(before.size());
        for (Character character : before) {
          if (!isFilterCharacter(character)) {
            after.add(character);
          }
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
    String text = character.getText();
    if (text == null || text.trim().isEmpty()) {
      return true;
    }
    return false;
  }
}
