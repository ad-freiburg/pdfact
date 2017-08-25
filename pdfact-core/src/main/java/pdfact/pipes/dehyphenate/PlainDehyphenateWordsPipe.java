package pdfact.pipes.dehyphenate;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import pdfact.model.CharacterStatistic;
import pdfact.model.FontFace;
import pdfact.model.Paragraph;
import pdfact.model.PdfDocument;
import pdfact.model.Position;
import pdfact.model.Word;
import pdfact.model.Word.WordFactory;
import pdfact.util.CollectionUtils;
import pdfact.util.StringUtils;
import pdfact.util.counter.ObjectCounter;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.WordList;
import pdfact.util.list.WordList.WordListFactory;

/**
 * A plain implementation of {@link DehyphenateWordsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainDehyphenateWordsPipe implements DehyphenateWordsPipe {
  /**
   * The hyphens to consider.
   */
  protected static final char[] HYPHENS = { '-', '–' };

  /**
   * The factory to create instances of {@link WordList}.
   */
  protected WordListFactory wordListFactory;

  /**
   * The factory to create instances of {@link Word}.
   */
  protected WordFactory wordFactory;

  /**
   * The counter for non hyphenated words.
   */
  protected ObjectCounter<String> noHyphenWords = new ObjectCounter<>();

  /**
   * The counter for all subwords in front of a hyphen.
   */
  protected ObjectCounter<String> hyphenPrefixes = new ObjectCounter<>();

  /**
   * Creates a new pipe that dehyphenates words.
   * 
   * @param wordListFactory
   *        The factory to create instances of {@link WordList}.
   * @param wordFactory
   *        The factory to create instances of {@link Word}.
   */
  @Inject
  public PlainDehyphenateWordsPipe(WordListFactory wordListFactory,
      WordFactory wordFactory) {
    this.wordListFactory = wordListFactory;
    this.wordFactory = wordFactory;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    preprocess(pdf);
    dehyphenateWords(pdf);
    return pdf;
  }

  // ==========================================================================

  /**
   * Precomputes some statistics: Counts non-hyphenated words and counts the
   * prefixes of hyphenated words.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void preprocess(PdfDocument pdf) {
    FontFace pdfFontFace = pdf.getCharacterStatistic()
        .getMostCommonFontFace();
    for (Paragraph paragraph : pdf.getParagraphs()) {
      for (Word word : paragraph.getWords()) {
        String text = StringUtils.normalize(word.getText(), false, true, true);

        // Find all indexes of hyphens.
        List<Integer> hyphenIndexes = StringUtils.indexesOf(text, HYPHENS);

        // If there are no hyphens, the word is a non-hyphenated words.
        if (hyphenIndexes.isEmpty()) {
          // Consider only words with most common font. Formula element
          // "(cIR" affects "cir-cumstance".
          CharacterStatistic charStats = word.getCharacterStatistic();
          FontFace fontFace = charStats.getMostCommonFontFace();
          if (fontFace == pdfFontFace) {
            this.noHyphenWords.add(text);
          }
        } else {
          // There are hyphens. Get the prefixes.
          for (int indexOfHyphen : hyphenIndexes) {
            // Only consider "middle" hyphens.
            if (indexOfHyphen > 0 && indexOfHyphen < text.length() - 1) {
              // Add the prefix, e.g. for word "sugar-free", add "sugar".
              this.hyphenPrefixes.add(text.substring(0, indexOfHyphen));
            }
          }
        }
      }
    }
  }
  
  // ==========================================================================
  
  /**
   * Dehyphenates the hyphenated words in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void dehyphenateWords(PdfDocument pdf) {
    if (pdf != null) {
      List<Paragraph> paragraphs = pdf.getParagraphs();
      if (paragraphs != null) {
        for (Paragraph paragraph : paragraphs) {
          WordList before = paragraph.getWords();
          WordList after = this.wordListFactory.create(before.size());
          for (int i = 0; i < before.size(); i++) {
            Word word = before.get(i);
            
            if (!word.isHyphenated()) {
              after.add(word);
            } else {
              Word nextWord = i < before.size() - 1 ? before.get(i + 1) : null;
              Word dehyphenated = dehyphenate(word, nextWord);
              after.add(dehyphenated);
              i++;
            }
          }
          paragraph.setWords(after);
          paragraph.setText(CollectionUtils.join(after, " "));
        }
      }
    }
  }

  /**
   * Merges the two given words.
   * 
   * @param word1 The first word to process.
   * @param word2 The second word to process.
   * 
   * @return The dehyphenated word.
   */
  public Word dehyphenate(Word word1, Word word2) {
    boolean isHyphenMandatory = isHyphenMandatory(word1, word2);

    Word dehyphenated = this.wordFactory.create();
    
    if (isHyphenMandatory) {
      dehyphenated.addCharacters(word1.getCharacters());
      dehyphenated.addCharacters(word2.getCharacters());
      dehyphenated.setText(word1.getText() + word2.getText());
    } else {
      for (int i = 0; i < word1.getCharacters().size() - 1; i++) {
        dehyphenated.addCharacter(word1.getCharacters().get(i));
      }
      dehyphenated.addCharacters(word2.getCharacters());
      dehyphenated.setText(word1.getText().substring(0,
          word1.getText().length() - 1) + word2.getText());
    }

    dehyphenated.setIsDehyphenated(true);
    
    List<Position> positions = new ArrayList<>();
    positions.add(word1.getFirstPosition());
    positions.add(word2.getFirstPosition());
    dehyphenated.setPositions(positions);

    return dehyphenated;
  }

  /**
   * Returns true if we have to ignore the hyphen between the two given words.
   * 
   * @param word1
   *        The first word (the part before the hyphen).
   * @param word2
   *        The second word (the part behind the hyphen).
   * 
   * @return True if we have to ignore the hyphen between the two given words;
   *         False otherwise.
   */
  protected boolean isHyphenMandatory(Word word1, Word word2) {
    String prefix = StringUtils.normalize(word1.getText(), false, true, true);

    String textWithoutHyphen =
        prefix + StringUtils.normalize(word2.getText(), false, true, true);

    if (prefix.isEmpty() || textWithoutHyphen.isEmpty()) {
      return false;
    }

    int numPrefixes = this.hyphenPrefixes.get(prefix);
    int numWithoutHyphen = this.noHyphenWords.get(textWithoutHyphen);

    if (numPrefixes > 0 || numWithoutHyphen > 0) {
      return numPrefixes > numWithoutHyphen;
    }

    char charBeforeHyphen = prefix.charAt(prefix.length() - 1);
    char charAfterHyphen = word2.getText().charAt(0);

    if (!Character.isAlphabetic(charBeforeHyphen)) {
      return false;
    }

    if (Character.isDigit(charBeforeHyphen)) {
      return false;
    }

    if (Character.isUpperCase(charBeforeHyphen)) {
      return false;
    }

    if (!Character.isAlphabetic(charAfterHyphen)) {
      return false;
    }

    if (Character.isDigit(charAfterHyphen)) {
      return false;
    }

    if (Character.isUpperCase(charAfterHyphen)) {
      return false;
    }

    // Don't ignore the hyphen if the prefix is a "well-known" word.
    if (prefix.length() > 2 && this.noHyphenWords.get(prefix) > 0) {
      return false;
    }

    return true;
  }
}
