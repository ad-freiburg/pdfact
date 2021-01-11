package pdfact.core.pipes.tokenize.areas;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Document;
import pdfact.core.model.Page;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.TextArea;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.statistician.CharacterStatistician;
import pdfact.core.util.xycut.XYCut;

/**
 * An implementation of {@link TokenizeToTextAreasPipe} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutTokenizeToTextAreasPipe extends XYCut implements TokenizeToTextAreasPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(XYCutTokenizeToTextAreasPipe.class);

  /**
   * The statistician to compute statistics about characters.
   */
  protected CharacterStatistician characterStatistician;

  /**
   * The number of processed pages.
   */
  protected int numProcessedPages;

  /**
   * The number of tokenized text areas.
   */
  protected int numTokenizedTextAreas;

  /**
   * Creates a new pipe that tokenizes the pages of a PDF document into text
   * areas.
   */
  public XYCutTokenizeToTextAreasPipe() {
    this.characterStatistician = new CharacterStatistician();
  }

  // ==============================================================================================

  @Override
  public Document execute(Document pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Tokenizing the pages into text areas.");
    tokenizeToTextAreas(pdf);

    log.debug("Tokenizing the pages into text areas done.");
    log.debug("# processed pages     : " + this.numProcessedPages);
    log.debug("# tokenized text areas: " + this.numTokenizedTextAreas);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  // ==============================================================================================

  /**
   * Tokenizes the pages of the given PDF document into text areas.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  protected void tokenizeToTextAreas(Document pdf) throws PdfActException {
    if (pdf == null) {
      return;
    }

    List<Page> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    for (Page page : pages) {
      if (page == null) {
        continue;
      }

      this.numProcessedPages++;

      ElementList<TextArea> textAreas = tokenizeToTextAreas(pdf, page);

      page.setTextAreas(textAreas);
      this.numTokenizedTextAreas += textAreas.size();
    }
  }

  /**
   * Tokenizes the given page into text areas.
   * 
   * @param pdf
   *        The PDF document to which the given page belongs to.
   * @param page
   *        The PDF page to process.
   * 
   * @return The list of text areas.
   * 
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  protected ElementList<TextArea> tokenizeToTextAreas(Document pdf,
      Page page) throws PdfActException {
    ElementList<TextArea> result = new ElementList<>();

    ElementList<Character> characters = page.getCharacters();
    List<ElementList<Character>> areaCharsList = cut(pdf, page, characters);
    if (areaCharsList != null) {
      for (ElementList<Character> areaChars : areaCharsList) {
        TextArea area = new TextArea();
        area.setCharacters(areaChars);
        area.setPosition(computePosition(pdf, page, area));
        area.setCharacterStatistic(computeCharacterStatistic(pdf, page, area));
        result.add(area);
      }
    }

    return result;
  }

  /**
   * Computes the position for the given text area.
   * 
   * @param pdf
   *        The PDF document to which the given text area belongs to.
   * @param page
   *        The PDF page to which the given text area belongs to.
   * @param a
   *        The text area to process.
   * 
   * @return The computed position.
   */
  protected Position computePosition(Document pdf, Page page, TextArea a) {
    ElementList<Character> characters = a.getCharacters();
    Rectangle r = new Rectangle(characters);
    return new Position(page, r);
  }

  /**
   * Computes the statistic about the given characters.
   * 
   * @param pdf
   *        The PDF document to which the given text area belongs to.
   * @param page
   *        The PDF page to which the given text area belongs to.
   * @param area
   *        The text area to process.
   * 
   * @return The computed statistic.
   */
  protected CharacterStatistic computeCharacterStatistic(Document pdf,
      Page page, TextArea area) {
    return this.characterStatistician.compute(area.getCharacters());
  }

  // ==============================================================================================

  @Override
  public float assessVerticalCut(Document pdf, Page page,
      List<ElementList<Character>> halves) {
    // Compute the statistics for the characters in the left half.
    ElementList<Character> left = halves.get(0);
    CharacterStatistic leftStats = this.characterStatistician.compute(left);

    // Compute the statistics for the characters in the right half.
    ElementList<Character> right = halves.get(1);
    CharacterStatistic rightStats = this.characterStatistician.compute(right);

    // Compute the (fictive) lane between the left and right half.
    float laneMinX = leftStats.getLargestMaxX();
    float laneMaxX = rightStats.getSmallestMinX();
    float laneWidth = laneMaxX - laneMinX;

    CharacterStatistic pdfCharStats = pdf.getCharacterStatistic();
    CharacterStatistic pageCharStats = page.getCharacterStatistic();
    float pdfCharWidth = pdfCharStats.getMostCommonWidth();
    float pageCharWidth = pageCharStats.getMostCommonWidth();

    // Don't allow the lane, if it is too narrow.
    if (laneWidth < Math.max(pdfCharWidth, pageCharWidth)) {
      return -1;
    }

    // Don't allow the lane, if it separates consecutive chars.
    if (separatesConsecutiveCharacters(left, leftStats, right, rightStats)) {
      return -1;
    }

    return laneWidth;
  }

  // ==============================================================================================

  @Override
  public float assessHorizontalCut(Document pdf, Page page,
      List<ElementList<Character>> halves) {
    // Compute the statistics for the characters in the upper half.
    ElementList<Character> upper = halves.get(0);
    CharacterStatistic upperStats = this.characterStatistician.compute(upper);

    // Compute the statistics for the characters in the lower half.
    ElementList<Character> lower = halves.get(1);
    CharacterStatistic lowerStats = this.characterStatistician.compute(lower);

    // Compute the (fictive) lane between the lower and upper half.
    float laneMinY = lowerStats.getLargestMaxY();
    float laneMaxY = upperStats.getSmallestMinY();
    float laneHeight = laneMaxY - laneMinY;

    // Don't allow lanes with negative heights.
    if (laneHeight < 0) {
      return -1;
    }

    float pdfCharHeight = pdf.getCharacterStatistic().getMostCommonHeight();
    float pageCharHeight = page.getCharacterStatistic().getMostCommonHeight();

    // Don't allow the lane, if it is too shallow.
    if (laneHeight < Math.min(pdfCharHeight, pageCharHeight)) {
      return -1;
    }

    return laneHeight;
  }

  // ==============================================================================================
  // Utility methods.

  /**
   * Checks if there is a character in the first given list of characters with
   * an extraction order number i and a character in the second given list of
   * characters with extraction order number i + 1, where both characters
   * overlap vertically.
   * 
   * @param left
   *        The characters in the left half.
   * @param leftStats
   *        The statistics about the characters in the left half.
   * @param right
   *        The characters in the right half.
   * @param rightStats
   *        The statistics about the characters in the right half.
   * @return True if there is such a character pair, false otherwise.
   */
  protected boolean separatesConsecutiveCharacters(ElementList<Character> left,
      CharacterStatistic leftStats, ElementList<Character> right,
      CharacterStatistic rightStats) {
    float largestMaxX = leftStats.getLargestMaxX();
    Set<Character> leftChars = new HashSet<>();
    for (Character c : left) {
      Rectangle rect = c.getPosition().getRectangle();
      if (PdfActUtils.isEqual(rect.getMaxX(), largestMaxX, 3 * leftStats.getMostCommonWidth())) {
        leftChars.add(c);
      }
    }

    float smallestMinX = rightStats.getSmallestMinX();
    Set<Character> rightChars = new HashSet<>();
    for (Character c : right) {
      Rectangle rect = c.getPosition().getRectangle();
      if (PdfActUtils.isEqual(rect.getMinX(), smallestMinX, 3 * rightStats.getMostCommonWidth())) {
        rightChars.add(c);
      }
    }

    for (Character leftChar : leftChars) {
      int leftCharNum = leftChar.getExtractionRank();
      Rectangle leftCharBox = leftChar.getPosition().getRectangle();
      for (Character rightChar : rightChars) {
        int rightCharNum = rightChar.getExtractionRank();
        Rectangle rightCharNox = rightChar.getPosition().getRectangle();

        // Check if the characters are consecutive.
        if (rightCharNum != leftCharNum + 1) {
          continue;
        }
        // Check if the characters overlap.
        if (!leftCharBox.overlapsVertically(rightCharNox)) {
          continue;
        }
        return true;
      }
    }
    return false;
  }
}
