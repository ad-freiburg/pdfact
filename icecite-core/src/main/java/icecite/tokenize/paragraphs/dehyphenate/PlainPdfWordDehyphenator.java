package icecite.tokenize.paragraphs.dehyphenate;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfDocument;
import icecite.models.PdfFontFace;
import icecite.models.PdfTextLine;
import icecite.models.PdfWord;
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
   */
  @AssistedInject
  public PlainPdfWordDehyphenator(@Assisted PdfDocument pdf) {
    preprocess(pdf);
  }

  /**
   * Precomputes some statistics: Counts non-hyphenated words and counts the
   * prefixes of hyphenated words.
   * 
   * @param pdf
   *        The pdf document to process.
   */
  protected void preprocess(PdfDocument pdf) {
    PdfFontFace pdfFontFace = pdf.getCharacters().getMostCommonFontFace();
    for (PdfTextLine line : pdf.getTextLines()) {
      for (PdfWord word : line.getWords()) {
        String text = PdfWordUtils.normalize(word);

        // Find all indexes of hyphens.
        List<Integer> hyphenIndexes = StringUtils.indexesOf(text, HYPHENS);

        // If there are no hyphens, the word is a non-hyphenated words.
        if (hyphenIndexes.isEmpty()) {
          // Consider only words with most common font. Formula element "(cIR"
          // affects "cir-cumstance".
          PdfFontFace fontFace = word.getCharacters().getMostCommonFontFace();
          if (fontFace == pdfFontFace) {
            this.noHyphenWords.add(text);
          }
        } else {
          // There are hyphens. Get the prefixes.
          for (int indexOfHyphen : hyphenIndexes) {
            // Only consider "middle" hyphens.
            if (indexOfHyphen > 0 && indexOfHyphen < text.length() - 1) {
              // Add the prefix, e.g. for the word "sugar-free", add "sugar".
              this.hyphenPrefixes.add(text.substring(0, indexOfHyphen));
            }
          }
        }
      }
    }
  }

  @Override
  public PdfWord dehyphenate(PdfWord word1, PdfWord word2) {
    // TODO: Implement.
    return null;
  }
}
