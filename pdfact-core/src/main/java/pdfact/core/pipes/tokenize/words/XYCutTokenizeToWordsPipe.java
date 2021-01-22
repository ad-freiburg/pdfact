package pdfact.core.pipes.tokenize.words;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Color;
import pdfact.core.model.Document;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.comparator.MinXComparator;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.lexicon.CharacterLexicon;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.statistician.CharacterStatistician;
import pdfact.core.util.xycut.XYCut;

/**
 * An implementation of {@link TokenizeToWordsPipe} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutTokenizeToWordsPipe extends XYCut implements TokenizeToWordsPipe {
  /**
   * The loggers.
   */
  protected static Logger llog = LogManager.getLogger("line-detection");
  protected static Logger wlog = LogManager.getLogger("word-detection");

  /**
   * The statistician to compute statistics about characters.
   */
  protected CharacterStatistician charStatistician;

  /**
   * The number of processed text lines.
   */
  protected int numProcessedTextLines;

  /**
   * The number of tokenized words.
   */
  protected int numTokenizedWords;

  /**
   * Creates a new word tokenizer.
   */
  public XYCutTokenizeToWordsPipe() {
    this.charStatistician = new CharacterStatistician();
  }

  // ==============================================================================================

  @Override
  public Document execute(Document doc) throws PdfActException {
    tokenizeToWords(doc);

    // Print the debug info for line detection here (and not in PlainTokenizeToTextLines.class),
    // because the text of text lines is only known after words were detected.
    if (llog.isDebugEnabled()) {
      for (Page page : doc.getPages()) {
        llog.debug("==================== Page " + page.getPageNumber() + " ====================");
        for (TextLine line : page.getTextLines()) {
          llog.debug("-------------------------------------------");
          llog.debug("Text line:          " + line.getText());
          llog.debug("... page:           " + line.getPosition().getPageNumber());
          String x1 = String.format("%.1f", line.getPosition().getRectangle().getMinX());
          String y1 = String.format("%.1f", line.getPosition().getRectangle().getMinY());
          String x2 = String.format("%.1f", line.getPosition().getRectangle().getMaxX());
          String y2 = String.format("%.1f", line.getPosition().getRectangle().getMaxY());
          llog.debug("... position:       [" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + "]");          
          FontFace fontFace = line.getCharacterStatistic().getMostCommonFontFace();
          String avgFs = String.format("%.1f", line.getCharacterStatistic().getAverageFontsize());
          llog.debug("... main font:      " + fontFace.getFont().getBaseName());
          llog.debug("... main fontsize:  " + fontFace.getFontSize() + "pt");
          llog.debug("... avg. fontsize:  " + avgFs + "pt");
          llog.debug("... mainly bold:    " + fontFace.getFont().isBold());
          llog.debug("... mainly italic:  " + fontFace.getFont().isItalic());
          llog.debug("... mainly type3:   " + fontFace.getFont().isType3Font());
          Color color = line.getCharacterStatistic().getMostCommonColor();
          llog.debug("... main RGB color: " + Arrays.toString(color.getRGB()));
          llog.debug("... baseline:       " + line.getBaseline());
        }
      }
    }

    if (wlog.isDebugEnabled()) {
      for (Page page : doc.getPages()) {
        wlog.debug("==================== Page " + page.getPageNumber() + " ====================");
        for (TextLine line : page.getTextLines()) {
          for (Word word : line.getWords()) {
            wlog.debug("-------------------------------------------");
            wlog.debug("Word:               " + word.getText());
            Position position = word.getFirstPosition();
            wlog.debug("... page:           " + position.getPageNumber());
            String x1 = String.format("%.1f", position.getRectangle().getMinX());
            String y1 = String.format("%.1f", position.getRectangle().getMinY());
            String x2 = String.format("%.1f", position.getRectangle().getMaxX());
            String y2 = String.format("%.1f", position.getRectangle().getMaxY());
            wlog.debug("... position:       [" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + "]");          
            FontFace fontFace = word.getCharacterStatistic().getMostCommonFontFace();
            String avgFs = String.format("%.1f", word.getCharacterStatistic().getAverageFontsize());
            wlog.debug("... main font:      " + fontFace.getFont().getBaseName());
            wlog.debug("... main fontsize:  " + fontFace.getFontSize() + "pt");
            wlog.debug("... avg. fontsize:  " + avgFs + "pt");
            wlog.debug("... mainly bold:    " + fontFace.getFont().isBold());
            wlog.debug("... mainly italic:  " + fontFace.getFont().isItalic());
            wlog.debug("... mainly type3:   " + fontFace.getFont().isType3Font());
            Color color = word.getCharacterStatistic().getMostCommonColor();
            wlog.debug("... main RGB color: " + Arrays.toString(color.getRGB()));
            wlog.debug("... hyphenated:     " + word.isHyphenated());
          }
        }
      }
    }

    return doc;
  }

  // ==============================================================================================

  /**
   * Tokenizes the text lines in the pages of the given PDF document into words.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  protected void tokenizeToWords(Document pdf) throws PdfActException {
    if (pdf == null) {
      return;
    }

    for (Page page : pdf.getPages()) {
      for (TextLine line : page.getTextLines()) {
        ElementList<Word> words = tokenizeToWords(pdf, page, line);
        line.setWords(words);
        line.setText(PdfActUtils.join(words, " "));

        this.numProcessedTextLines++;
        this.numTokenizedWords += words.size();
      }
    }
  }

  /**
   * Tokenizes the given text line into words.
   * 
   * @param pdf
   *        The PDF document to which the given text line belongs to.
   * @param page
   *        The PDF document to which the given page belongs to.
   * @param line
   *        The text line to process.
   * 
   * @return The words.
   * 
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  public ElementList<Word> tokenizeToWords(Document pdf, Page page,
      TextLine line) throws PdfActException {
    ElementList<Word> result = new ElementList<>();

    ElementList<Character> characters = line.getCharacters();
    List<ElementList<Character>> charLists = cut(pdf, page, characters);
    Word word = null;
    for (ElementList<Character> charList : charLists) {
      word = new Word();
      word.setCharacters(charList);
      word.setText(computeText(word));
      word.setPositions(computePositions(page, word));
      word.setCharacterStatistic(computeCharStatistics(word));
      result.add(word);
    }

    // Check if the last word in the line is hyphenated.
    if (word != null) {
      word.setIsHyphenated(computeIsHyphenated(word));
    }

    return result;
  }

  // ==============================================================================================

  @Override
  public float assessVerticalCut(Document pdf, Page page,
      List<ElementList<Character>> halves) {
    ElementList<Character> left = halves.get(0);
    CharacterStatistic leftStats = this.charStatistician.compute(left);
    float leftMaxX = leftStats.getLargestMaxX();

    ElementList<Character> right = halves.get(1);
    CharacterStatistic rightStats = this.charStatistician.compute(right);
    float rightMinX = rightStats.getSmallestMinX();

    float width = rightMinX - leftMaxX;
    if (width < 1f) {
      return -1;
    }
    return width;
  }

  // ==============================================================================================

  @Override
  public float assessHorizontalCut(Document pdf, Page page,
      List<ElementList<Character>> halves) {
    return -1;
  }

  // ==============================================================================================

  /**
   * Computes the character statistics for the given word.
   * 
   * @param word
   *        The word to process.
   * @return The character statistics for the given word.
   */
  protected CharacterStatistic computeCharStatistics(Word word) {
    return this.charStatistician.compute(word.getCharacters());
  }

  /**
   * Computes the position for the given word.
   * 
   * @param page
   *        The PDF page in which the word is located.
   * @param word
   *        The word to process.
   * @return The position for the given word.
   */
  protected List<Position> computePositions(Page page, Word word) {
    List<Position> positions = new ArrayList<>();
    ElementList<Character> characters = word.getCharacters();
    Rectangle rect = Rectangle.fromHasPositionElements(characters);
    Position position = new Position(page, rect);
    positions.add(position);
    return positions;
  }

  /**
   * Computes the text for the given word.
   * 
   * @param word
   *        The word to process.
   * @return The text for the given word.
   */
  protected String computeText(Word word) {
    Collections.sort(word.getCharacters(), new MinXComparator());
    return PdfActUtils.join(word.getCharacters(), "");
  }

  /**
   * Checks if the given word is hyphenated.
   * 
   * @param word
   *        The word to check.
   * @return True if the given word is hyphenated; false otherwise.
   */
  protected boolean computeIsHyphenated(Word word) {
    if (word == null) {
      return false;
    }

    ElementList<Character> characters = word.getCharacters();
    if (characters == null || characters.size() < 2) {
      return false;
    }

    return CharacterLexicon.isHyphen(word.getLastCharacter());
  }
}