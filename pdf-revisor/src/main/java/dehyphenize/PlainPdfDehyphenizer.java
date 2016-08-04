package dehyphenize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.freiburg.iif.counter.ObjectCounter;
import de.freiburg.iif.text.StringUtils;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextElement;
import model.PdfTextLine;
import model.PdfWord;

public class PlainPdfDehyphenizer implements PdfDehyphenizer {
  /** The hyphens to consider. */
  protected static final char[] HYPHENS = { '-', '–' };

  @Override
  public void dehyphenize(PdfDocument document) {
    if (document == null) {
      return;
    }

    // Search for hyphenated words and decide for each of them if we have
    // to ignore the hyphen or not.

    ObjectCounter<String> noHyphenWords = new ObjectCounter<>();
    ObjectCounter<String> hyphenPrefixes = new ObjectCounter<>();

    // -------------------------------------------------------------------------
    // Precompute some statistics: Count non-hyphenated words and count the 
    // prefixes of hyphenated words.

    for (PdfPage page : document.getPages()) {
      List<PdfWord> words = page.getWords();
      for (PdfWord word : words) {
        // Ignore stopwords.
        if (isStopWord(word)) {
          continue;
        }

        String wordText = normalize(word);

        // Find all indexes of hyphens.
        List<Integer> hyphenIndexes = StringUtils.indexesOf(wordText, HYPHENS);

        // If there are no hyphens, the word is a non-hyphenated words.
        if (hyphenIndexes.isEmpty()) {
          // Consider only words with most common font. Formula element "(cIR"
          // affects "cir-cumstance".
          if (word.getFont().equals(document.getTextStatistics().getMostCommonFont())) {
            noHyphenWords.add(wordText);
          }
        } else {
          // There are hyphens. Get the prefixes.
          for (int indexOfHyphen : hyphenIndexes) {
            // Only consider "middle" hyphens.
            if (indexOfHyphen > 0 && indexOfHyphen < wordText.length() - 1) {
              // Add the prefix, e.g. for the word "sugar-free", add "sugar".
              hyphenPrefixes.add(wordText.substring(0, indexOfHyphen));
            }
          }
        }
      }
    }

    // -------------------------------------------------------------------------    
    // Dehyphenize.

    // Keep track of previous lines per role.
    Map<PdfRole, PdfTextLine> prevLinesPerRole = new HashMap<>();

    for (PdfPage page : document.getPages()) {
      List<PdfTextLine> lines = page.getTextLines();

      if (lines == null) {
        return;
      }

      for (PdfTextLine line : lines) {
        if (line == null) {
          continue;
        }

        // Get the previous line with same role.
        PdfTextLine prevLineByRole = prevLinesPerRole.get(line.getRole());
        if (prevLineByRole != null) {
          PdfWord prevWord = prevLineByRole.getLastWord();

          if (prevWord != null) {
            PdfCharacter prevCharacter = prevWord.getLastTextCharacter();

            // Check if the previous word ends with a hyphen.
            if (isHyphen(prevCharacter)) {
              // The last line ends with a hyphen. 
              PdfWord word = line.getFirstWord();
              
              boolean ignoreHyphen = isIgnoreHyphen(prevWord, word,
                  noHyphenWords, hyphenPrefixes);
              
              // Merge the both words.
              prevWord.addAnyElements(word.getElements());
              // Ignore the word (because it was merged with the previous word)
              word.setIgnore(true);

              // Ignore the hyphen if necessary.
              if (ignoreHyphen) {
                prevCharacter.setIgnore(true);
              }
            }
          }
        }
        prevLinesPerRole.put(line.getRole(), line);
      }
    }
  }

  // ---------------------------------------------------------------------------

  /**
   * Returns true if we have to ignore the hyphen between the two given words.
   */
  protected boolean isIgnoreHyphen(PdfWord prevWord, PdfWord word,
      ObjectCounter<String> noHyphenWords, ObjectCounter<String> prefixes) {
    String prevWordText = prevWord.getUnicode();
    // The word may contain another hyphens, but we want to analyze only
    // the part until first hyphen.
    String wordText = word.getUnicode().split("-")[0];

    String prefix = prevWordText.substring(0, prevWordText.length() - 1);
    String normalizedPrefix = normalize(prefix);
    String noHyphen = normalizedPrefix + wordText;
    String normalizedNoHyphen = normalize(noHyphen);

    int prefixNum = prefixes.getCount(normalizedPrefix);
    int withoutHyphenNum = noHyphenWords.getCount(normalizedNoHyphen);

    if (prefixNum == 0 && withoutHyphenNum == 0) {
      // Neither 'noHyphenWords' nor 'prefixes' knows the word(s).

      if (normalizedPrefix.length() < 2) {
        return false;
      }

      // If prefix contains hyphens, assume that is a composed word, like
      // "state-of-the-art".
      if (StringUtils.containsAny(normalizedPrefix, HYPHENS)) {
        return false;
      }

      if (StringUtils.isStopWord(normalizedPrefix)) {
        return true;
      }

      char charBeforeHyphen = prefix.charAt(normalizedPrefix.length() - 1);
      char charAfterHyphen = wordText.charAt(0);

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
      if (normalizedPrefix.length() > 2
          && noHyphenWords.getCount(normalizedPrefix) > 0) {
        System.out.println(normalizedPrefix + " "
            + (noHyphenWords.getCount(normalizedPrefix)));
        return false;
      }

      return true;
    }

    if (withoutHyphenNum >= prefixNum) {
      return true;
    }

    return false;
  }

  // ===========================================================================

  /**
   * Returns true if the given character is a hyphen.
   */
  protected boolean isHyphen(PdfCharacter character) {
    if (character != null) {
      return StringUtils.equals(character.getUnicode(), HYPHENS);
    }
    return false;
  }

  /**
   * Returns true if the given word is a stopword.
   */
  protected boolean isStopWord(PdfWord word) {
    if (word != null) {
      String text = word.getUnicode();
      if (text != null) {
        return StringUtils.isStopWord(text);
      }
    }
    return false;
  }

  /**
   * Transforms the unicode of given element to lowercases and removes all 
   * symbols that are non-hyphens.
   */
  protected String normalize(PdfTextElement element) {
    if (element != null) {
      return normalize(element.getText(true, false, false));
    }
    return null;
  }

  /**
   * Transforms the given string to lowercases and removes all 
   * symbols that are non-hyphens.
   */
  protected String normalize(String text) {
    if (text != null) {
      return StringUtils.normalize(text, false, false, true, HYPHENS);
    }
    return null;
  }
}
