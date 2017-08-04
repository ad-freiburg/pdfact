package icecite.tokenize.paragraphs.dehyphenate;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterStatistic;
import icecite.models.PdfDocument;
import icecite.models.PdfFontFace;
import icecite.models.PdfParagraph;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
import icecite.models.PdfWord.PdfWordFactory;
import icecite.utils.counter.ObjectCounter;
import icecite.utils.text.StringUtils;
import icecite.utils.word.PdfWordUtils;

/**
 * A plain implementation of {@link PdfWordDehyphenator}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfWordDehyphenator implements PdfWordDehyphenator {
  /**
   * The hyphens to consider.
   */
  protected static final char[] HYPHENS = { '-', '–' };

  /**
   * The factory to create instances of {@link PdfWord}.
   */
  protected PdfWordFactory wordFactory;

  /**
   * The counter for non hyphenated words.
   */
  protected ObjectCounter<String> noHyphenWords = new ObjectCounter<>();

  /**
   * The counter for all subwords in front of a hyphen.
   */
  protected ObjectCounter<String> hyphenPrefixes = new ObjectCounter<>();

  /**
   * The default constructor.
   * 
   * @param pdf
   *        The PDF document to process.
   * @param wordFactory
   *        The factory to create instances of {@link PdfWord}.
   */
  @AssistedInject
  public PlainPdfWordDehyphenator(@Assisted PdfDocument pdf,
      PdfWordFactory wordFactory) {
    preprocess(pdf);
    this.wordFactory = wordFactory;
  }

  /**
   * Precomputes some statistics: Counts non-hyphenated words and counts the
   * prefixes of hyphenated words.
   * 
   * @param pdf
   *        The pdf document to process.
   */
  protected void preprocess(PdfDocument pdf) {
    PdfFontFace pdfFontFace = pdf.getCharacterStatistic()
        .getMostCommonFontFace();
    for (PdfParagraph paragraph : pdf.getParagraphs()) {
      for (PdfTextBlock textBlock : paragraph.getTextBlocks()) {
        for (PdfTextLine line : textBlock.getTextLines()) {
          for (PdfWord word : line.getWords()) {
            String text = PdfWordUtils.normalize(word);

            // Find all indexes of hyphens.
            List<Integer> hyphenIndexes = StringUtils.indexesOf(text, HYPHENS);

            // If there are no hyphens, the word is a non-hyphenated words.
            if (hyphenIndexes.isEmpty()) {
              // Consider only words with most common font. Formula element
              // "(cIR" affects "cir-cumstance".
              PdfCharacterStatistic charStats = word.getCharacterStatistic();
              PdfFontFace fontFace = charStats.getMostCommonFontFace();
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
    }
  }

  @Override
  public PdfWord dehyphenate(PdfWord word1, PdfWord word2) {
    boolean isHyphenMandatory = isHyphenMandatory(word1, word2);

    PdfWord dehyphenated = this.wordFactory.create();
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
  protected boolean isHyphenMandatory(PdfWord word1, PdfWord word2) {
    String prefix = PdfWordUtils.normalize(word1);

    String textWithoutHyphen = prefix + PdfWordUtils.normalize(word2);

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
