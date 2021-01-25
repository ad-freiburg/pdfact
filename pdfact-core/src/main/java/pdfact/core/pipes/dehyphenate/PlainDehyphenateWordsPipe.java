package pdfact.core.pipes.dehyphenate;

import static pdfact.core.util.lexicon.CharacterLexicon.HYPHENS;
import static pdfact.core.util.lexicon.CharacterLexicon.LETTERS;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import pdfact.core.model.Character;
import pdfact.core.model.Document;
import pdfact.core.model.Paragraph;
import pdfact.core.model.Word;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.counter.ObjectCounter;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.normalize.WordNormalizer;

/**
 * A plain implementation of {@link DehyphenateWordsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainDehyphenateWordsPipe implements DehyphenateWordsPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("word-dehyphenation");

  /**
   * The word normalizer.
   */
  protected WordNormalizer wordNormalizer;

  /**
   * The index of all words which do not include a hyphen (normal words).
   */
  protected ObjectCounter<String> normalWordsIndex;

  /**
   * The index of all words with an inner hyphen (compound words).
   */
  protected ObjectCounter<String> compoundWordsIndex;

  /**
   * The index of all prefixes of compound words.
   */
  protected ObjectCounter<String> prefixesIndex;

  /**
   * The total number of words in the PDF document.
   */
  protected int numWords;

  /**
   * The total number of processed words on dehyphenation.
   */
  protected int numProcessedWords;

  /**
   * The total number of dehyphenated words.
   */
  protected int numDehyphenatedWords;

  /**
   * The number of dehyphenated words, which resulted in normal words.
   */
  protected int numNormalWords;

  /**
   * The number of dehyphenated words, which resulted in compound words.
   */
  protected int numCompoundWords;

  /**
   * Creates a new pipe that dehyphenates words.
   */
  public PlainDehyphenateWordsPipe() {
    this.normalWordsIndex = new ObjectCounter<>();
    this.compoundWordsIndex = new ObjectCounter<>();
    this.prefixesIndex = new ObjectCounter<>();

    this.wordNormalizer = new WordNormalizer();
    this.wordNormalizer.setIsToLowerCase(true);
    this.wordNormalizer.setLeadingCharactersToKeep(LETTERS, HYPHENS);
    this.wordNormalizer.setTrailingCharactersToKeep(LETTERS, HYPHENS);
  }

  // ==============================================================================================

  @Override
  public Document execute(Document pdf) throws PdfActException {
    countWords(pdf);
    dehyphenate(pdf);
    return pdf;
  }

  // ==============================================================================================

  /**
   * Counts single, compound and prefixes of compound words.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void countWords(Document pdf) {
    if (pdf == null) {
      return;
    }

    List<Paragraph> paragraphs = pdf.getParagraphs();
    if (paragraphs == null) {
      return;
    }

    for (Paragraph paragraph : pdf.getParagraphs()) {
      if (paragraph == null) {
        continue;
      }

      List<Word> words = paragraph.getWords();
      if (words == null) {
        continue;
      }

      for (Word word : words) {
        if (word == null) {
          continue;
        }

        this.numWords++;

        // Count normal words, compound words and prefixes of compound words.
        // The prefixes of a compound word are the substrings before each
        // hyphen, e.g. for the compound word "sugar-free", the prefix is
        // "sugar".

        // Normalize the word: Remove leading and trailing punctuation marks
        // (but not hyphens).
        String wordStr = this.wordNormalizer.normalize(word);

        if (wordStr == null || wordStr.isEmpty()) {
          continue;
        }

        // Check if the word contains hyphens.
        TIntList idxsHyphens = PdfActUtils.indexesOf(wordStr, HYPHENS);

        if (idxsHyphens.isEmpty()) {
          // No hyphen was found. The word is a single word.
          this.normalWordsIndex.add(wordStr);
          continue;
        }

        // We are interested only in compound words with inner hyphens.
        if (idxsHyphens.get(0) == 0) {
          // The word starts with an hyphen. Ignore the word.
          continue;
        }

        if (idxsHyphens.get(idxsHyphens.size() - 1) == wordStr.length() - 1) {
          // The word ends with an hyphen. Ignore it.
          continue;
        }

        this.compoundWordsIndex.add(wordStr);

        // Count the prefixes of compound words.
        TIntIterator itr = idxsHyphens.iterator();
        while (itr.hasNext()) {
          this.prefixesIndex.add(wordStr.substring(0, itr.next()));
        }
      }
    }
  }

  // ==============================================================================================

  /**
   * Dehyphenates the hyphenated words in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void dehyphenate(Document pdf) {
    if (pdf == null) {
      return;
    }

    List<Paragraph> paragraphs = pdf.getParagraphs();
    if (paragraphs == null) {
      return;
    }

    for (Paragraph paragraph : paragraphs) {
      if (paragraph == null) {
        continue;
      }

      ElementList<Word> words = paragraph.getWords();
      if (words == null) {
        continue;
      }

      Iterator<Word> wordItr = words.iterator();
      ElementList<Word> dehyphWords = new ElementList<>(words.size());

      while (wordItr.hasNext()) {
        Word word = wordItr.next();

        this.numProcessedWords++;

        if (word == null) {
          continue;
        }

        if (!word.isHyphenated()) {
          dehyphWords.add(word);
          continue;
        }

        Word nextWord = wordItr.hasNext() ? wordItr.next() : null;
        if (nextWord != null) {
          dehyphWords.add(dehyphenate(word, nextWord));
          this.numProcessedWords++;
        } else {
          dehyphWords.add(word);
        }
      }

      paragraph.setWords(dehyphWords);
      paragraph.setText(PdfActUtils.join(dehyphWords, " "));
    }
  }

  /**
   * Dehyphenates the two given words.
   * 
   * @param word1
   *        The first word to process.
   * @param word2
   *        The second word to process.
   * 
   * @return The dehyphenated word.
   */
  public Word dehyphenate(Word word1, Word word2) {
    if (word1 == null) {
      return null;
    }

    if (word2 == null) {
      return word1;
    }

    ElementList<Character> chars1 = word1.getCharacters();
    ElementList<Character> chars2 = word2.getCharacters();
    ElementList<Character> mergedChars = new ElementList<>();

    if (isHyphenMandatory(word1, word2)) {
      mergedChars.addAll(chars1);
      this.numCompoundWords++;
    } else {
      mergedChars.addAll(chars1.subList(0, chars1.size() - 1));
      this.numNormalWords++;
    }
    this.numDehyphenatedWords++;

    mergedChars.addAll(chars2);
    word1.setCharacters(chars2);

    word1.addPositions(word2.getPositions());
    word1.setIsHyphenated(false);
    word1.setIsDehyphenated(true);
    word1.setText(PdfActUtils.join(mergedChars, ""));

    return word1;
  }

  /**
   * Returns true if the hyphen between the two given words is mandatory on.
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
    String word1Str = this.wordNormalizer.normalize(word1);
    String word2Str = this.wordNormalizer.normalize(word2);

    log.debug("-------------------------------------------");
    log.debug("Merging words \"%s\" and \"%s\" ...", word1Str, word2Str);

    // TODO: Use the word normalizer.
    String prefix = word1Str.replaceAll("[-]$", "");
    String withHyphen = word1Str + word2Str;
    String withoutHyphen = prefix + word2Str;

    int singleWordFreq = this.normalWordsIndex.getFrequency(withoutHyphen);
    int compoundWordFreq = this.compoundWordsIndex.getFrequency(withHyphen);
    int compoundWordPrefixFreq = this.prefixesIndex.getFrequency(prefix);

    log.debug("... frequency of combined word without hyphen: %d", singleWordFreq);
    log.debug("... frequency of combined word with hyphen:    %d", compoundWordFreq);
    log.debug("... frequency of prefix with hyphen:           %d", compoundWordPrefixFreq);

    if (compoundWordFreq != singleWordFreq) {
      if (compoundWordFreq > singleWordFreq) {
        log.debug("... hyphen is mandatory:                       true");
        log.debug("... reason:                                    freq(\"%s\") > freq(\"%s\")", 
            withHyphen, withoutHyphen);
        return true;
      } else {
        log.debug("... hyphen is mandatory:                       false");
        log.debug("... reason:                                    freq(\"%s\") < freq(\"%s\")", 
            withHyphen, withoutHyphen);
        return false;
      }
    }

    if (compoundWordPrefixFreq > 0) {
      log.debug("... hyphen is mandatory:                       true");
      log.debug("... reason:                                    freq(\"%s\") == freq(\"%s\") and "
         + "freq(\"%s\") > 0.", withHyphen, withoutHyphen, prefix);
      return true;
    } else {
      log.debug("... hyphen is mandatory:                       false");
      log.debug("... reason:                                    freq(\"%s\") == freq(\"%s\") and "
      + "freq(\"%s\") < 0.", withHyphen, withoutHyphen, prefix);
      return false;
    }
  }
}
