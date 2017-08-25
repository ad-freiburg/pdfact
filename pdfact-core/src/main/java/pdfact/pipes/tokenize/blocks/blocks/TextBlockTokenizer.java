package pdfact.pipes.tokenize.blocks.blocks;

import java.util.List;

import pdfact.model.PdfDocument;
import pdfact.model.Page;
import pdfact.model.TextBlock;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.TextLineList;

/**
 * A tokenizer that tokenizes text lines into text blocks.
 * 
 * @author Claudius Korzen
 */
public interface TextBlockTokenizer {
  /**
   * Tokenizes the given list of text lines (all of them sharing the same page)
   * into text blocks.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   * @param page
   *        The PDF page to which the given text lines belong to.
   * @param lines
   *        The list of text lines to tokenize.
   * 
   * @return A list of the identified text blocks.
   * 
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  List<TextBlock> tokenize(PdfDocument pdf, Page page, TextLineList lines)
      throws PdfActException;

  /**
   * The factory to create instances of {@link TextBlockTokenizer}.
   * 
   * @author Claudius Korzen
   */
  public interface TextBlockTokenizerFactory {
    /**
     * Creates a new instance of {@link TextBlockTokenizer}.
     * 
     * @return A new instance of {@link TextBlockTokenizer}.
     */
    TextBlockTokenizer create();
  }
}
