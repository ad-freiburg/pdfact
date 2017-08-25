package pdfact.pipes.tokenize.lines.areas;

import java.util.List;

import pdfact.model.PdfDocument;
import pdfact.model.Page;
import pdfact.model.TextArea;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.CharacterList;

/**
 * A tokenizer that tokenizes the characters of a PDF page into text areas.
 * 
 * @author Claudius Korzen
 */
public interface TextAreaTokenizer {
  /**
   * Tokenizes the given characters into text areas.
   * 
   * @param pdf
   *        The PDF document to which the given characters belong to.
   * @param page
   *        The PDF page to which the given characters belong to.
   * @param chars
   *        The characters to tokenize.
   * 
   * @return A list of the identified text areas.
   * 
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  List<TextArea> tokenize(PdfDocument pdf, Page page, CharacterList chars)
      throws PdfActException;

  // ==========================================================================

  /**
   * The factory to create instances of {@link TextAreaTokenizer}.
   * 
   * @author Claudius Korzen
   */
  public interface TextAreaTokenizerFactory {
    /**
     * Creates a new instance of {@link TextAreaTokenizer}.
     * 
     * @return A new instance of {@link TextAreaTokenizer}.
     */
    TextAreaTokenizer create();
  }
}
