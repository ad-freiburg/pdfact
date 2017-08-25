package pdfact.pipes.tokenize.blocks;

import java.util.List;

import com.google.inject.Inject;

import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.model.TextBlock;
import pdfact.pipes.tokenize.blocks.blocks.TextBlockTokenizer;
import pdfact.pipes.tokenize.blocks.blocks.TextBlockTokenizer.TextBlockTokenizerFactory;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.TextLineList;

/**
 * A plain implementation of {@link DetectTextBlocksPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainDetectTextBlocksPipe implements DetectTextBlocksPipe {
  /**
   * The factory to create instances of {@link TextBlockTokenizer}.
   */
  protected TextBlockTokenizerFactory textBlockTokenizerFactory;

  /**
   * The default constructor.
   * 
   * @param textBlockTokenizerFactory
   *        The factory to create instances of {@link TextBlockTokenizer}.
   */
  @Inject
  public PlainDetectTextBlocksPipe(
      TextBlockTokenizerFactory textBlockTokenizerFactory) {
    this.textBlockTokenizerFactory = textBlockTokenizerFactory;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    if (pdf != null) {
      for (Page page : pdf.getPages()) {
        // Tokenize the lines into blocks.
        List<TextBlock> blocks = tokenizeIntoTextBlocks(pdf, page,
            page.getTextLines());

        page.setTextBlocks(blocks);
      }
    }
    return pdf;
  }

  // ==========================================================================

  /**
   * Tokenizes the given text lines into text blocks.
   * 
   * @param pdf
   *        The PDF document to which the given text lines belong to.
   * @param page
   *        The PDF page to which the given text lines belong to.
   * @param lines
   *        The lines to tokenize.
   * @return The list of identified text blocks.
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  protected List<TextBlock> tokenizeIntoTextBlocks(PdfDocument pdf,
      Page page, TextLineList lines) throws PdfActException {
    return this.textBlockTokenizerFactory.create().tokenize(pdf, page, lines);
  }
}