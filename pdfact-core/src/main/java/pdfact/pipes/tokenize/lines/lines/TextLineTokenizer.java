package pdfact.pipes.tokenize.lines.lines;

import java.util.List;

import pdfact.model.PdfDocument;
import pdfact.model.Page;
import pdfact.model.TextArea;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.TextLineList;

/**
 * A tokenizer that tokenizes the text areas of a PDF page into text lines.
 * 
 * @author Claudius Korzen
 */
public interface TextLineTokenizer {

  /**
   * Tokenizes the given list of text areas (all of them sharing the same page)
   * into text lines.
   * 
   * @param pdf
   *        The PDF document to which the given text areas belong to.
   * @param page
   *        The PDF page to which the given text areas belong to.
   * @param areas
   *        The list of text areas to tokenize.
   * @return The list of identified text lines.
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  TextLineList tokenize(PdfDocument pdf, Page page,
      List<TextArea> areas) throws PdfActException;

  /**
   * The factory to create instances of {@link TextLineTokenizer}.
   * 
   * @author Claudius Korzen
   */
  public interface TextLineTokenizerFactory {
    /**
     * Creates a new PdfTextLineTokenizer.
     * 
     * @return An instance of {@link TextLineTokenizer}.
     */
    TextLineTokenizer create();
  }
}
