package pdfact.pipes.tokenize.lines.words;

import pdfact.model.PdfDocument;
import pdfact.model.Page;
import pdfact.model.TextLine;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.CharacterList;
import pdfact.util.list.WordList;

/**
 * A tokenizer that tokenizes the text lines of a PDF page into words.
 * 
 * @author Claudius Korzen
 */
public interface WordTokenizer {
  /**
   * Tokenizes the given text line into words.
   * 
   * @param pdf
   *        The PDF document to which the text line belongs to.
   * @param page
   *        The PDF page to which the text line belongs to.
   * @param line
   *        The text line to tokenize.
   * @param lineCharacters
   *        The characters of the text line to tokenize.
   * @return The list of identified words.
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  WordList tokenize(PdfDocument pdf, Page page, TextLine line,
      CharacterList lineCharacters) throws PdfActException;

  /**
   * The factory to create instances of {@link WordTokenizer}.
   * 
   * @author Claudius Korzen
   */
  public interface WordTokenizerFactory {
    /**
     * Creates a new PdfWordTokenizer.
     * 
     * @return An instance of {@link WordTokenizer}.
     */
    WordTokenizer create();
  }
}
