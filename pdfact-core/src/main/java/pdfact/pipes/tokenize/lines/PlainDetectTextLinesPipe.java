package pdfact.pipes.tokenize.lines;

import java.util.List;

import com.google.inject.Inject;

import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.model.TextArea;
import pdfact.pipes.tokenize.lines.areas.TextAreaTokenizer;
import pdfact.pipes.tokenize.lines.areas.TextAreaTokenizer.TextAreaTokenizerFactory;
import pdfact.pipes.tokenize.lines.lines.TextLineTokenizer;
import pdfact.pipes.tokenize.lines.lines.TextLineTokenizer.TextLineTokenizerFactory;
import pdfact.util.exception.PdfActException;
import pdfact.util.list.TextLineList;
import pdfact.util.statistic.TextLineStatistician;

/**
 * A plain implementation of {@link DetectTextLinesPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainDetectTextLinesPipe implements DetectTextLinesPipe {
  /**
   * The factory to create instances of {@link TextAreaTokenizer}.
   */
  protected TextAreaTokenizerFactory textAreaTokenizerFactory;

  /**
   * The factory to create instances of {@link TextLineTokenizer}.
   */
  protected TextLineTokenizerFactory textLineTokenizerFactory;

  /**
   * The statistician to compute the statistics about text lines.
   */
  protected TextLineStatistician textLineStatistician;

  /**
   * The default constructor.
   * 
   * @param textAreaTokenizerFactory
   *        The factory to create instances of {@link TextAreaTokenizer}.
   * @param textLineTokenizerFactory
   *        The factory to create instances of {@link TextLineTokenizer}.
   * @param textLineStatistician
   *        The statistician to compute the statistics about text lines.
   */
  @Inject
  public PlainDetectTextLinesPipe(
      TextAreaTokenizerFactory textAreaTokenizerFactory,
      TextLineTokenizerFactory textLineTokenizerFactory,
      TextLineStatistician textLineStatistician) {
    this.textAreaTokenizerFactory = textAreaTokenizerFactory;
    this.textLineTokenizerFactory = textLineTokenizerFactory;
    this.textLineStatistician = textLineStatistician;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    detectTextLines(pdf);
    return pdf;
  }

  // ==========================================================================

  /**
   * Detects the text lines in the pages of the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @throws PdfActException
   *         If something went wrong while detecting the text lines.
   */
  protected void detectTextLines(PdfDocument pdf) throws PdfActException {
    if (pdf != null) {
      List<Page> pages = pdf.getPages();
      if (pages != null) {
        for (Page page : pages) {
          // Tokenize the page into text areas.
          List<TextArea> areas = tokenizeIntoTextAreas(pdf, page);

          // Tokenize the areas into text lines.
          TextLineList lines = tokenizeIntoTextLines(pdf, page, areas);

          page.setTextLines(lines);
          page.setTextLineStatistic(this.textLineStatistician.compute(lines));
        }
        pdf.setTextLineStatistic(this.textLineStatistician.aggregate(pages));
      }
    }
  }

  /**
   * Tokenizes the given PDF page into text areas.
   * 
   * @param pdf
   *        The PDF document to which the given page belongs to.
   * @param page
   *        The page to tokenize.
   * @return The list of identified text areas.
   * @throws PdfActException
   *         If something went wrong while tokenization.
   */
  protected List<TextArea> tokenizeIntoTextAreas(PdfDocument pdf,
      Page page) throws PdfActException {
    return this.textAreaTokenizerFactory.create().tokenize(pdf, page,
        page.getCharacters());
  }

  /**
   * Tokenizes the given list of text areas into text lines.
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
  protected TextLineList tokenizeIntoTextLines(PdfDocument pdf,
      Page page, List<TextArea> areas) throws PdfActException {
    return this.textLineTokenizerFactory.create().tokenize(pdf, page, areas);
  }
}