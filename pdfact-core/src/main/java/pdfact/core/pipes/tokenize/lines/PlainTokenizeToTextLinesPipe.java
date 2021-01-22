package pdfact.core.pipes.tokenize.lines;

import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Document;
import pdfact.core.model.Line;
import pdfact.core.model.Page;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.TextArea;
import pdfact.core.model.TextLine;
import pdfact.core.util.comparator.MinXComparator;
import pdfact.core.util.counter.FloatCounter;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.lexicon.CharacterLexicon;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.statistician.CharacterStatistician;
import pdfact.core.util.statistician.TextLineStatistician;
import pdfact.core.util.xycut.XYCut;

/**
 * A plain implementation of {@link TokenizeToTextLinesPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainTokenizeToTextLinesPipe extends XYCut implements TokenizeToTextLinesPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger("line-detection");

  /**
   * The statistician to compute statistics about characters.
   */
  protected CharacterStatistician characterStatistician;

  /**
   * The statistician to compute the statistics about text lines.
   */
  protected TextLineStatistician textLineStatistician;

  /**
   * The number of processed text areas.
   */
  protected int numProcessedTextAreas;

  /**
   * The number of tokenized text lines.
   */
  protected int numTokenizedTextLines;

  /**
   * Creates a new text line tokenizer.
   */
  public PlainTokenizeToTextLinesPipe() {
    this.characterStatistician = new CharacterStatistician();
    this.textLineStatistician = new TextLineStatistician();
  }

  // ==============================================================================================

  @Override
  public Document execute(Document doc) throws PdfActException {
    tokenizeToTextLines(doc);

    return doc;
  }

  // ==============================================================================================

  /**
   * Tokenizes the text areas in the pages of the given PDF document into text lines.
   * 
   * @param pdf The PDF document to process.
   * 
   * @throws PdfActException If something went wrong while tokenization.
   */
  protected void tokenizeToTextLines(Document pdf) throws PdfActException {
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

      ElementList<TextLine> textLines = tokenizeToTextLines(pdf, page);
      page.setTextLineStatistic(this.textLineStatistician.compute(textLines));
      page.setTextLines(textLines);
    }
    pdf.setTextLineStatistic(this.textLineStatistician.aggregate(pages));
  }

  /**
   * Tokenizes the text areas in the given page into text lines.
   * 
   * @param pdf  The PDF document to which the given page belongs to.
   * @param page The PDF page to process.
   * 
   * @return The list of text lines.
   * 
   * @throws PdfActException If something went wrong while tokenization.
   */
  protected ElementList<TextLine> tokenizeToTextLines(Document pdf, Page page)
          throws PdfActException {
    ElementList<TextLine> result = new ElementList<>();

    for (TextArea area : page.getTextAreas()) {
      ElementList<Character> characters = area.getCharacters();
      List<ElementList<Character>> charLists = cut(pdf, page, characters);

      this.numProcessedTextAreas++;

      for (ElementList<Character> charList : charLists) {
        // Create a PdfTextLine object.
        TextLine textLine = new TextLine();
        textLine.setCharacters(charList);
        textLine.setBaseline(computeBaseline(charList));
        textLine.setCharacterStatistic(computeCharacterStatistic(charList));
        textLine.setPosition(computePosition(page, charList));
        result.add(textLine);
      }
    }

    this.numTokenizedTextLines += result.size();

    return result;
  }

  // ==============================================================================================

  /**
   * Computes the baseline from the given characters of a line.
   * 
   * @param characters The list of characters to process.
   * @return The computed baseline.
   */
  protected Line computeBaseline(ElementList<Character> characters) {
    Line baseLine = null;
    FloatCounter minYCounter = new FloatCounter();

    if (characters != null && !characters.isEmpty()) {
      Collections.sort(characters, new MinXComparator());

      float minX = Float.MAX_VALUE;
      float maxX = -Float.MAX_VALUE;
      for (Character character : characters) {
        if (CharacterLexicon.isBaselineCharacter(character)) {
          minYCounter.add(character.getPosition().getRectangle().getMinY());
        }

        minX = Math.min(minX, character.getPosition().getRectangle().getMinX());
        maxX = Math.max(maxX, character.getPosition().getRectangle().getMaxX());
      }

      if (!minYCounter.isEmpty()) {
        float minY = minYCounter.getMostCommonFloat();
        baseLine = new Line(minX, minY, maxX, minY);
      }
    }

    return baseLine;
  }

  /**
   * Computes the character statistics for the given text line.
   * 
   * @param chars The characters to process.
   * @return The character statistics for the given text line.
   */
  protected CharacterStatistic computeCharacterStatistic(ElementList<Character> chars) {
    return this.characterStatistician.compute(chars);
  }

  /**
   * Computes the position for the given text line.
   * 
   * @param page  The PDF page in which the line is located.
   * @param chars The characters of the text line.
   * @return The position for the given text line.
   */
  protected Position computePosition(Page page, ElementList<Character> chars) {
    Rectangle rect = Rectangle.fromHasPositionElements(chars);
    return new Position(page, rect);
  }

  // ==============================================================================================

  @Override
  public float assessVerticalCut(Document pdf, Page page, List<ElementList<Character>> halves) {
    return -1;
  }

  // ==============================================================================================

  @Override
  public float assessHorizontalCut(Document pdf, Page page, List<ElementList<Character>> halves) {
    ElementList<Character> upper = halves.get(0);
    CharacterStatistic upperStats = this.characterStatistician.compute(upper);
    float upperMinY = upperStats.getSmallestMinY();

    ElementList<Character> lower = halves.get(1);
    CharacterStatistic lowerStats = this.characterStatistician.compute(lower);
    float lowerMaxY = lowerStats.getLargestMaxY();

    return upperMinY - lowerMaxY;
  }
}
