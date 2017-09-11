package pdfact.core.pipes.dehyphenate;

import static pdfact.core.util.lexicon.CharacterLexicon.HYPHENS;
import static pdfact.core.util.lexicon.CharacterLexicon.LETTERS;

import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import pdfact.core.model.Paragraph;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Word;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.counter.ObjectCounter;
import pdfact.core.util.counter.ObjectCounter.ObjectCounterFactory;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.list.CharacterList;
import pdfact.core.util.list.CharacterList.CharacterListFactory;
import pdfact.core.util.list.WordList;
import pdfact.core.util.list.WordList.WordListFactory;
import pdfact.core.util.normalize.WordNormalizer;
import pdfact.core.util.normalize.WordNormalizer.WordNormalizerFactory;

/**
 * A plain implementation of {@link DehyphenateWordsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainDehyphenateWordsPipe implements DehyphenateWordsPipe {
  /**
   * The factory to create instances of {@link CharacterList}.
   */
  protected CharacterListFactory characterListFactory;

  /**
   * The factory to create instances of {@link WordList}.
   */
  protected WordListFactory wordListFactory;

  /**
   * The factory to create instances of {@link WordNormalizer}.
   */
  protected WordNormalizer wordNormalizer;

  /**
   * The counter for all words which do not include a hyphen.
   */
  protected ObjectCounter<String> singleWords;

  /**
   * The counter for all compound words (word that include a hyphen).
   */
  protected ObjectCounter<String> compoundWords;

  /**
   * The counter for all prefixes of compound words.
   */
  protected ObjectCounter<String> compoundWordPrefixes;

  /**
   * Creates a new pipe that dehyphenates words.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link CharacterListFactory}.
   * @param wordListFactory
   *        The factory to create instances of {@link WordList}.
   * @param wordNormalizerFactory
   *        The factory to create instances of {@link WordNormalizer}.
   * @param objectCounterFactory
   *        The factory to create instances of {@link ObjectCounter}.
   */
  @Inject
  public PlainDehyphenateWordsPipe(CharacterListFactory characterListFactory,
      WordListFactory wordListFactory,
      WordNormalizerFactory wordNormalizerFactory,
      ObjectCounterFactory<String> objectCounterFactory) {
    this.characterListFactory = characterListFactory;
    this.wordListFactory = wordListFactory;
    this.singleWords = objectCounterFactory.create();
    this.compoundWords = objectCounterFactory.create();
    this.compoundWordPrefixes = objectCounterFactory.create();

    this.wordNormalizer = wordNormalizerFactory.create();
    this.wordNormalizer.setIsToLowerCase(true);
    this.wordNormalizer.setLeadingCharactersToKeep(LETTERS, HYPHENS);
    this.wordNormalizer.setTrailingCharactersToKeep(LETTERS, HYPHENS);
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    countWords(pdf);
    dehyphenate(pdf);
    return pdf;
  }

  // ==========================================================================

  /**
   * Counts single, compound and prefixes of compound words.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void countWords(PdfDocument pdf) {
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

        // Count (a) words without hyphens (single words), (b) words with inner
        // hyphens (compound words) and (c) all prefixes of compound words.

        // Normalize the word: Remove leading and trailing punctuation marks.
        String wordStr = this.wordNormalizer.normalize(word);

        if (wordStr == null || wordStr.isEmpty()) {
          continue;
        }

        // Find the indexes of hyphens.
        TIntList idxsHyphens = PdfActUtils.indexesOf(wordStr, HYPHENS);

        if (idxsHyphens.isEmpty()) {
          // No hyphen was found. The word is a single word.
          this.singleWords.add(wordStr);
          continue;
        }

        if (idxsHyphens.get(0) == 0) {
          // The word starts with an hyphen. Ignore it.
          continue;
        }

        if (idxsHyphens.get(idxsHyphens.size() - 1) == wordStr.length() - 1) {
          // The word ends with an hyphen. Ignore it.
          continue;
        }

        this.compoundWords.add(wordStr);

        TIntIterator itr = idxsHyphens.iterator();
        while (itr.hasNext()) {
          // Add the prefix, e.g. for word "sugar-free", add "sugar".
          this.compoundWordPrefixes.add(wordStr.substring(0, itr.next()));
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
  protected void dehyphenate(PdfDocument pdf) {
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

      WordList words = paragraph.getWords();
      if (words == null) {
        continue;
      }

      Iterator<Word> wordItr = words.iterator();
      WordList dehyphenatedWords = this.wordListFactory.create(words.size());

      while (wordItr.hasNext()) {
        Word word = wordItr.next();

        if (word == null) {
          continue;
        }

        if (!word.isHyphenated()) {
          dehyphenatedWords.add(word);
          continue;
        }

        Word nextWord = wordItr.hasNext() ? wordItr.next() : null;
        dehyphenatedWords.add(dehyphenate(word, nextWord));
      }

      paragraph.setWords(dehyphenatedWords);
      paragraph.setText(PdfActUtils.join(dehyphenatedWords, " "));
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

    CharacterList chars1 = word1.getCharacters();
    CharacterList chars2 = word2.getCharacters();
    CharacterList mergedChars = this.characterListFactory.create();

    if (isHyphenMandatory(word1, word2)) {
      mergedChars.addAll(chars1);
    } else {
      mergedChars.addAll(chars1.subList(0, chars1.size() - 1));
    }

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

    String prefix = word1Str.replaceAll("[-]$", "");
    String withHyphen = word1Str + word2Str;
    String withoutHyphen = prefix + word2Str;

    int singleWordFreq = this.singleWords.getFrequency(withoutHyphen);
    int compoundWordFreq = this.compoundWords.getFrequency(withHyphen);
    int compoundWordPrefixFreq = this.compoundWordPrefixes.getFrequency(prefix);

    if (compoundWordFreq != singleWordFreq) {
      return compoundWordFreq > singleWordFreq;
    }

    return compoundWordPrefixFreq > 0;
  }
}
