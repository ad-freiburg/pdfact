package pdfact.core.pipes.translate.diacritics;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import pdfact.core.model.Character;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.Rectangle.RectangleFactory;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.CharacterList;
import pdfact.core.util.list.CharacterList.CharacterListFactory;
import pdfact.core.util.log.InjectLogger;

/**
 * A plain implementation of {@link MergeDiacriticsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainMergeDiacriticsPipe implements MergeDiacriticsPipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The factory to create instances of {@link CharacterList}.
   */
  protected CharacterListFactory characterListFactory;

  /**
   * The factory to create instances of {@link RectangleFactory}.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The number of processed characters.
   */
  protected int numProcessedCharacters;

  /**
   * The number of merged characters.
   */
  protected int numMergedDiacritics;

  /**
   * Creates a new pipe that merges diacritics.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link CharacterList}.
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   *
   */
  @Inject
  public PlainMergeDiacriticsPipe(CharacterListFactory characterListFactory,
      RectangleFactory rectangleFactory) {
    this.characterListFactory = characterListFactory;
    this.rectangleFactory = rectangleFactory;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Merging the diacritics.");
    mergeDiacritics(pdf);

    log.debug("Merging the diacritics done.");
    log.debug("# processed characters: " + this.numProcessedCharacters);
    log.debug("# merged diacritics: " + this.numMergedDiacritics);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  // ==========================================================================

  /**
   * Merges the diacritical marks in the given PDF document with their related
   * characters.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void mergeDiacritics(PdfDocument pdf) {
    if (pdf != null) {
      List<Page> pages = pdf.getPages();
      if (pages != null) {
        for (Page page : pages) {
          CharacterList before = page.getCharacters();
          CharacterList after = this.characterListFactory.create(before.size());
          if (before != null) {
            for (int i = 0; i < before.size(); i++) {
              Character prev = i > 0 ? before.get(i - 1) : null;
              Character character = before.get(i);
              Character next = i < before.size() - 1 ? before.get(i + 1) : null;

              // Don't proceed if the character in question is not a diacritic.
              if (isDiacritic(character)) {
                mergeDiacritic(prev, character, next);
                this.numMergedDiacritics++;
              } else {
                after.add(character);
              }
              this.numProcessedCharacters++;
            }
            page.setCharacters(after);
          }
        }
      }
    }
  }

  /**
   * Chooses the belonging base character (either the given character to the
   * left or the given character to the right of the diacritic) and merges the
   * givern diacritic with this base character.
   * 
   * @param prev
   *        The character to the left of the diacritic.
   * @param diacritic
   *        The diacritic.
   * @param next
   *        The character to the right of the diacritic.
   */
  public void mergeDiacritic(Character prev, Character diacritic,
      Character next) {
    if (diacritic == null) {
      return;
    }

    Position diacriticPosition = diacritic.getPosition();
    if (diacriticPosition == null) {
      return;
    }

    Rectangle diacriticRect = diacriticPosition.getRectangle();
    if (diacriticRect == null) {
      return;
    }

    // Choose the belonging base character:
    // (1) Compute the horizontal overlap with the left character.
    float prevOverlap = 0;
    if (prev != null) {
      Position prevPosition = prev.getPosition();
      if (prevPosition != null) {
        Rectangle prevRect = prevPosition.getRectangle();
        prevOverlap = diacriticRect.getHorizontalOverlapLength(prevRect);
      }
    }

    // (2) Compute the horizontal overlap with the right character.
    float nextOverlap = 0;
    if (next != null) {
      Position nextPosition = next.getPosition();
      if (nextPosition != null) {
        Rectangle nextRect = nextPosition.getRectangle();
        nextOverlap = diacriticRect.getHorizontalOverlapLength(nextRect);
      }
    }

    // Merge the diacritic to the base character with the largest overlap.
    if (prevOverlap > 0 && prevOverlap >= nextOverlap) {
      prev.setText(mergeTexts(prev, diacritic));
      prev.getPosition().setRectangle(mergeRectangles(prev, diacritic));
    } else if (nextOverlap > 0 && nextOverlap > prevOverlap) {
      next.setText(mergeTexts(next, diacritic));
      next.getPosition().setRectangle(mergeRectangles(next, diacritic));
    }
  }

  // ==========================================================================

  /**
   * Merges the texts of the given base character and the given diacritic.
   * 
   * @param diacritic
   *        The diacritic to merge with the base character.
   * @param base
   *        The base character to merge with the diacritic.
   *
   * @return The merged text.
   */
  protected String mergeTexts(Character base, Character diacritic) {
    String baseText = base != null ? base.getText() : null;
    String diacriticText = diacritic != null ? diacritic.getText() : null;

    if (baseText == null) {
      return null;
    }

    if (diacriticText == null || diacriticText.isEmpty()) {
      return baseText;
    }

    // Replace non-combining diacritics by their combining equivalents.
    int diacriticCode = diacriticText.codePointAt(0);
    if (COMBINING_DIACRITICS.containsKey(diacriticCode)) {
      diacriticText = COMBINING_DIACRITICS.get(diacriticCode);
    }

    // Merge the diacritic with the base character.
    String mergedText = baseText + diacriticText;
    return Normalizer.normalize(mergedText, Normalizer.Form.NFC);
  }

  /**
   * Merges the rectangles of the given base character and the given diacritic.
   * 
   * @param diacritic
   *        The diacritic to merge with the base character.
   * @param base
   *        The base character to merge with the diacritic.
   *
   * @return The merged rectangle.
   */
  protected Rectangle mergeRectangles(Character base, Character diacritic) {
    Position basePos = base != null ? base.getPosition() : null;
    if (basePos == null) {
      return null;
    }

    Rectangle baseRect = basePos.getRectangle();
    if (baseRect == null) {
      return null;
    }

    Position diacriticPos = diacritic != null ? diacritic.getPosition() : null;
    if (diacriticPos == null) {
      return baseRect;
    }

    Rectangle diacriticRect = diacriticPos.getRectangle();
    if (diacriticRect == null) {
      return baseRect;
    }

    return this.rectangleFactory.fromUnion(baseRect, diacriticRect);
  }

  // ==========================================================================

  /**
   * Checks if the given characters is a diacritic.
   * 
   * @param character
   *        The character to check.
   * @return True, if the given character is a diacritic; false otherwise.
   */
  public static boolean isDiacritic(Character character) {
    if (character == null) {
      return false;
    }

    String text = character.getText();
    if (text == null || text.length() != 1) {
      return false;
    }

    int type = java.lang.Character.getType(text.charAt(0));
    return type == java.lang.Character.NON_SPACING_MARK
        || type == java.lang.Character.MODIFIER_SYMBOL
        || type == java.lang.Character.MODIFIER_LETTER;
  }

  /**
   * Adds non-decomposing diacritics to the hash with their related combining
   * character. These are values that the unicode spec claims are equivalent but
   * are not mapped in the form NFKC normalization method. Determined by going
   * through the Combining Diacritical Marks section of the Unicode spec and
   * identifying which characters are not mapped to by the normalization. For
   * example, maps "ACUTE ACCENT" to "COMBINING ACUTE ACCENT".
   */
  static final Map<Integer, String> COMBINING_DIACRITICS;

  static {
    COMBINING_DIACRITICS = new HashMap<Integer, String>(31);
    COMBINING_DIACRITICS.put(0x0060, "\u0300");
    COMBINING_DIACRITICS.put(0x02CB, "\u0300");
    COMBINING_DIACRITICS.put(0x0027, "\u0301");
    COMBINING_DIACRITICS.put(0x00B4, "\u0301");
    COMBINING_DIACRITICS.put(0x02B9, "\u0301");
    COMBINING_DIACRITICS.put(0x02CA, "\u0301");
    COMBINING_DIACRITICS.put(0x0384, "\u0301");
    COMBINING_DIACRITICS.put(0x005E, "\u0302");
    COMBINING_DIACRITICS.put(0x02C6, "\u0302");
    COMBINING_DIACRITICS.put(0x007E, "\u0303");
    COMBINING_DIACRITICS.put(0x02DC, "\u0303");
    COMBINING_DIACRITICS.put(0x00AF, "\u0304");
    COMBINING_DIACRITICS.put(0x02C9, "\u0304");
    COMBINING_DIACRITICS.put(0x00A8, "\u0308");
    COMBINING_DIACRITICS.put(0x00B0, "\u030A");
    COMBINING_DIACRITICS.put(0x02DA, "\u030A");
    COMBINING_DIACRITICS.put(0x0022, "\u030B");
    COMBINING_DIACRITICS.put(0x02BA, "\u030B");
    COMBINING_DIACRITICS.put(0x02DD, "\u030B");
    COMBINING_DIACRITICS.put(0x02C7, "\u030C");
    COMBINING_DIACRITICS.put(0x02C8, "\u030D");
    COMBINING_DIACRITICS.put(0x02BB, "\u0312");
    COMBINING_DIACRITICS.put(0x02BC, "\u0313");
    COMBINING_DIACRITICS.put(0x0486, "\u0313");
    COMBINING_DIACRITICS.put(0x055A, "\u0313");
    COMBINING_DIACRITICS.put(0x02BD, "\u0314");
    COMBINING_DIACRITICS.put(0x0485, "\u0314");
    COMBINING_DIACRITICS.put(0x0559, "\u0314");
    COMBINING_DIACRITICS.put(0x02D4, "\u031D");
    COMBINING_DIACRITICS.put(0x02D5, "\u031E");
    COMBINING_DIACRITICS.put(0x02D6, "\u031F");
    COMBINING_DIACRITICS.put(0x02D7, "\u0320");
    COMBINING_DIACRITICS.put(0x02B2, "\u0321");
    COMBINING_DIACRITICS.put(0x02CC, "\u0329");
    COMBINING_DIACRITICS.put(0x02B7, "\u032B");
    COMBINING_DIACRITICS.put(0x02CD, "\u0331");
    COMBINING_DIACRITICS.put(0x005F, "\u0332");
    COMBINING_DIACRITICS.put(0x204E, "\u0359");
  }
}
