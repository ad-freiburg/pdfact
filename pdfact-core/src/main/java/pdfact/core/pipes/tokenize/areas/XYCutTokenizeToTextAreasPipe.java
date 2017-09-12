package pdfact.core.pipes.tokenize.areas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.TextArea;
import pdfact.core.model.Position.PositionFactory;
import pdfact.core.model.Rectangle.RectangleFactory;
import pdfact.core.model.TextArea.TextAreaFactory;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.CharacterList;
import pdfact.core.util.log.InjectLogger;
import pdfact.core.util.statistician.CharacterStatistician;
import pdfact.core.util.xycut.XYCut;

/**
 * An implementation of {@link TokenizeToTextAreasPipe} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutTokenizeToTextAreasPipe extends XYCut
    implements TokenizeToTextAreasPipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The factory to create instances of {@link TextArea}.
   */
  protected TextAreaFactory textAreaFactory;

  /**
   * The factory to create instances of {@link Position}.
   */
  protected PositionFactory positionFactory;

  /**
   * The factory to create instances of {@link Rectangle}.
   */
  protected RectangleFactory rectangleFactory;

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
   * 
   * @param textAreaFactory
   *        The factory to create instances of {@link TextArea}.
   * @param positionFactory
   *        The factory to create instances of {@link Position}.
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param characterStatistician
   *        The statistician to compute statistics about characters.
   */
  @Inject
  public XYCutTokenizeToTextAreasPipe(
      TextAreaFactory textAreaFactory,
      PositionFactory positionFactory,
      RectangleFactory rectangleFactory,
      CharacterStatistician characterStatistician) {
    this.textAreaFactory = textAreaFactory;
    this.positionFactory = positionFactory;
    this.rectangleFactory = rectangleFactory;
    this.characterStatistician = characterStatistician;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Tokenizing the pages into text areas.");
    tokenizeToTextAreas(pdf);

    log.debug("Tokenizing the pages into text areas done.");
    log.debug("# processed pages     : " + this.numProcessedPages);
    log.debug("# tokenized text areas: " + this.numTokenizedTextAreas);

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }

  // ==========================================================================

  /**
   * Tokenizes the pages of the given PDF document into text areas.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  protected void tokenizeToTextAreas(PdfDocument pdf) throws PdfActException {
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

      List<TextArea> textAreas = tokenizeToTextAreas(pdf, page);

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
  protected List<TextArea> tokenizeToTextAreas(PdfDocument pdf, Page page)
      throws PdfActException {
    List<TextArea> result = new ArrayList<>();

    List<CharacterList> areaCharsList = cut(pdf, page, page.getCharacters());
    if (areaCharsList != null) {
      for (CharacterList areaChars : areaCharsList) {
        TextArea area = this.textAreaFactory.create();
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
  protected Position computePosition(PdfDocument pdf, Page page, TextArea a) {
    CharacterList characters = a.getCharacters();
    Rectangle r = this.rectangleFactory.fromHasPositionElements(characters);
    return this.positionFactory.create(page, r);
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
  protected CharacterStatistic computeCharacterStatistic(PdfDocument pdf,
      Page page, TextArea area) {
    return this.characterStatistician.compute(area.getCharacters());
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, Page page,
      List<CharacterList> halves) {
    // Compute the statistics for the characters in the left half.
    CharacterList left = halves.get(0);
    CharacterStatistic leftStats = this.characterStatistician.compute(left);

    // Compute the statistics for the characters in the right half.
    CharacterList right = halves.get(1);
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

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, Page page,
      List<CharacterList> halves) {
    // Compute the statistics for the characters in the upper half.
    CharacterList upper = halves.get(0);
    CharacterStatistic upperStats = this.characterStatistician.compute(upper);

    // Compute the statistics for the characters in the lower half.
    CharacterList lower = halves.get(1);
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

  // ==========================================================================
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
  protected boolean separatesConsecutiveCharacters(CharacterList left,
      CharacterStatistic leftStats, CharacterList right,
      CharacterStatistic rightStats) {
    float largestMaxX = leftStats.getLargestMaxX();
    Set<Character> leftChars = new HashSet<>();
    for (Character c : left) {
      // TODO: Allow a certain threshold value.
      Rectangle rect = c.getPosition().getRectangle();
      if (PdfActUtils.isEqual(rect.getMaxX(), largestMaxX, 1f)) {
        leftChars.add(c);
      }
    }

    float smallestMinX = rightStats.getSmallestMinX();
    Set<Character> rightChars = new HashSet<>();
    for (Character c : right) {
      // TODO: Allow a certain threshold value.
      Rectangle rect = c.getPosition().getRectangle();
      if (PdfActUtils.isEqual(rect.getMinX(), smallestMinX, 1f)) {
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
