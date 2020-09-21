package pdfact.core.pipes;

import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.dehyphenate.DehyphenateWordsPipe.DehyphenateWordsPipeFactory;
import pdfact.core.pipes.filter.characters.FilterCharactersPipe.FilterCharactersPipeFactory;
import pdfact.core.pipes.filter.figures.FilterFiguresPipe.FilterFiguresPipeFactory;
import pdfact.core.pipes.filter.shapes.FilterShapesPipe.FilterShapesPipeFactory;
import pdfact.core.pipes.parse.ParsePdfStreamsPipe.ParsePdfPipeFactory;
import pdfact.core.pipes.semanticize.DetectSemanticsPipe.DetectSemanticsPipeFactory;
import pdfact.core.pipes.tokenize.areas.TokenizeToTextAreasPipe.TokenizeToTextAreasPipeFactory;
import pdfact.core.pipes.tokenize.blocks.TokenizeToTextBlocksPipe.TokenizeToTextBlocksPipeFactory;
import pdfact.core.pipes.tokenize.lines.TokenizeToTextLinesPipe.TokenizeToTextLinesPipeFactory;
import pdfact.core.pipes.tokenize.paragraphs.TokenizeToParagraphsPipe.TokenizeToParagraphsPipeFactory;
import pdfact.core.pipes.tokenize.words.TokenizeToWordsPipe.TokenizeToWordsPipeFactory;
import pdfact.core.pipes.translate.characters.StandardizeCharactersPipe.StandardizeCharactersPipeFactory;
import pdfact.core.pipes.translate.diacritics.MergeDiacriticsPipe.MergeDiacriticsPipeFactory;
import pdfact.core.pipes.translate.ligatures.SplitLigaturesPipe.SplitLigaturesPipeFactory;
import pdfact.core.pipes.validate.ValidatePdfPathPipe.ValidatePdfPathPipeFactory;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.log.InjectLogger;
import pdfact.core.util.pipeline.Pipeline;
import pdfact.core.util.pipeline.Pipeline.PdfActPipelineFactory;

/**
 * A plain implementation of {@link PdfActCorePipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfActCorePipe implements PdfActCorePipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The factory to create pipelines.
   */
  protected PdfActPipelineFactory pipelineFactory;

  /**
   * The factory to create the pipe that validates PDF paths.
   */
  protected ValidatePdfPathPipeFactory validatePdfPathPipeFactory;

  /**
   * The factory to create the pipe that parses PDF files.
   */
  protected ParsePdfPipeFactory parsePdfPipeFactory;

  /**
   * The factory to create the pipe that merges diacritic characters.
   */
  protected MergeDiacriticsPipeFactory mergeDiacriticsPipeFactory;

  /**
   * The factory to create the pipe that splits ligatures.
   */
  protected SplitLigaturesPipeFactory splitLigaturesPipeFactory;

  /**
   * The factory to create the pipe that standardizes characters.
   */
  protected StandardizeCharactersPipeFactory standardizeCharactersPipeFactory;

  /**
   * The factory to create the pipe that filters chosen characters.
   */
  protected FilterCharactersPipeFactory filterCharactersPipeFactory;

  /**
   * The factory to create the pipe that filters chosen figures.
   */
  protected FilterFiguresPipeFactory filterFiguresPipeFactory;

  /**
   * The factory to create the pipe that filters chosen shapes.
   */
  protected FilterShapesPipeFactory filterShapesPipeFactory;

  /**
   * The factory to create the pipe that tokenizes pages to text areas.
   */
  protected TokenizeToTextAreasPipeFactory tokenizeToTextAreasPipeFactory;

  /**
   * The factory to create the pipe that tokenizes text areas to lines.
   */
  protected TokenizeToTextLinesPipeFactory tokenizeToTextLinesPipeFactory;

  /**
   * The factory to create the pipe that tokenizes text lines to words.
   */
  protected TokenizeToWordsPipeFactory tokenizeToWordsPipeFactory;

  /**
   * The factory to create the pipe that tokenizes text lines to blocks.
   */
  protected TokenizeToTextBlocksPipeFactory tokenizeToTextBlocksPipeFactory;

  /**
   * The factory to create the pipe that detects the semantics of blocks.
   */
  protected DetectSemanticsPipeFactory semanticizeTextBlocksPipeFactory;

  /**
   * The factory to create the pipe that tokenizes blocks to paragraphs.
   */
  protected TokenizeToParagraphsPipeFactory tokenizeToParagraphsPipeFactory;

  /**
   * The factory to create the pipe that dehyphenates words.
   */
  protected DehyphenateWordsPipeFactory dehyphenateWordsPipeFactory;

  // ==========================================================================

  /**
   * The default constructor.
   *
   * @param pipelineFactory
   *        The factory to create pipelines.
   * @param validatePdfPathPipeFactory
   *        The factory to create the pipe that validates PDF paths.
   * @param parsePdfPipeFactory
   *        The factory to create the pipe that parses PDF files.
   * @param mergeDiacriticsPipeFactory
   *        The factory to create the pipe that merges diacritic characters.
   * @param splitLigaturesPipeFactory
   *        The factory to create the pipe that splits ligatures.
   * @param standardizeCharactersPipeFactory
   *        The factory to create the pipe that standardizes characters.
   * @param filterCharactersPipeFactory
   *        The factory to create the pipe that filters chosen characters.
   * @param filterFiguresPipeFactory
   *        The factory to create the pipe that filters chosen figures.
   * @param filterShapesPipeFactory
   *        The factory to create the pipe that filters chosen shapes.
   * @param tokenizeToTextAreasPipeFactory
   *        The factory to create the pipe that tokenizes pages to text areas.
   * @param tokenizeToTextLinesPipeFactory
   *        The factory to create the pipe that tokenizes text areas to lines.
   * @param tokenizeToWordsPipeFactory
   *        The factory to create the pipe that tokenizes text lines to words.
   * @param tokenizeToTextBlocksPipeFactory
   *        The factory to create the pipe that tokenizes text lines to blocks.
   * @param semanticizeTextBlocksPipeFactory
   *        The factory to create the pipe that detects the semantics of blocks.
   * @param tokenizeToParagraphsPipeFactory
   *        The factory to create the pipe that tokenizes blocks to paragraphs.
   * @param dehyphenateWordsPipeFactory
   *        The factory to create the pipe that dehyphenates words.
   */
  @Inject
  public PlainPdfActCorePipe(PdfActPipelineFactory pipelineFactory,
      ValidatePdfPathPipeFactory validatePdfPathPipeFactory,
      ParsePdfPipeFactory parsePdfPipeFactory,
      MergeDiacriticsPipeFactory mergeDiacriticsPipeFactory,
      SplitLigaturesPipeFactory splitLigaturesPipeFactory,
      StandardizeCharactersPipeFactory standardizeCharactersPipeFactory,
      FilterCharactersPipeFactory filterCharactersPipeFactory,
      FilterFiguresPipeFactory filterFiguresPipeFactory,
      FilterShapesPipeFactory filterShapesPipeFactory,
      TokenizeToTextAreasPipeFactory tokenizeToTextAreasPipeFactory,
      TokenizeToTextLinesPipeFactory tokenizeToTextLinesPipeFactory,
      TokenizeToWordsPipeFactory tokenizeToWordsPipeFactory,
      TokenizeToTextBlocksPipeFactory tokenizeToTextBlocksPipeFactory,
      DetectSemanticsPipeFactory semanticizeTextBlocksPipeFactory,
      TokenizeToParagraphsPipeFactory tokenizeToParagraphsPipeFactory,
      DehyphenateWordsPipeFactory dehyphenateWordsPipeFactory) {
    this.pipelineFactory = pipelineFactory;
    this.validatePdfPathPipeFactory = validatePdfPathPipeFactory;
    this.parsePdfPipeFactory = parsePdfPipeFactory;
    this.mergeDiacriticsPipeFactory = mergeDiacriticsPipeFactory;
    this.splitLigaturesPipeFactory = splitLigaturesPipeFactory;
    this.standardizeCharactersPipeFactory = standardizeCharactersPipeFactory;
    this.filterCharactersPipeFactory = filterCharactersPipeFactory;
    this.filterFiguresPipeFactory = filterFiguresPipeFactory;
    this.filterShapesPipeFactory = filterShapesPipeFactory;
    this.tokenizeToTextAreasPipeFactory = tokenizeToTextAreasPipeFactory;
    this.tokenizeToTextLinesPipeFactory = tokenizeToTextLinesPipeFactory;
    this.tokenizeToWordsPipeFactory = tokenizeToWordsPipeFactory;
    this.tokenizeToTextBlocksPipeFactory = tokenizeToTextBlocksPipeFactory;
    this.semanticizeTextBlocksPipeFactory = semanticizeTextBlocksPipeFactory;
    this.tokenizeToParagraphsPipeFactory = tokenizeToParagraphsPipeFactory;
    this.dehyphenateWordsPipeFactory = dehyphenateWordsPipeFactory;
  }

  // ==========================================================================

  /**
   * Processes the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return The PDF document after processing.
   * 
   * @throws PdfActException
   *         If something went wrong on processing the PDF document.
   */
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Processing the core pipeline.");

    // Fill the pipeline with the pipes to execute
    Pipeline pipeline = this.pipelineFactory.create();

    // Validate the path to the PDF file.
    pipeline.addPipe(this.validatePdfPathPipeFactory.create());
    // Extract the characters, shapes and figures.
    pipeline.addPipe(this.parsePdfPipeFactory.create());
    // Merge the diacritics.
//    pipeline.addPipe(this.mergeDiacriticsPipeFactory.create());
    // Split the ligatures.
//    pipeline.addPipe(this.splitLigaturesPipeFactory.create());
    // Standardize characters.
//    pipeline.addPipe(this.standardizeCharactersPipeFactory.create());
    // Filter the characters.
//    pipeline.addPipe(this.filterCharactersPipeFactory.create());
    // Filter the figures.
    pipeline.addPipe(this.filterFiguresPipeFactory.create());
    // Filter the shapes.
    pipeline.addPipe(this.filterShapesPipeFactory.create());
    // Tokenize the page into text areas.
    pipeline.addPipe(this.tokenizeToTextAreasPipeFactory.create());
    // Tokenize the text areas into text lines.
    pipeline.addPipe(this.tokenizeToTextLinesPipeFactory.create());
    // Tokenize the text lines into words.
    pipeline.addPipe(this.tokenizeToWordsPipeFactory.create());
    // Tokenize the text lines into text blocks.
    pipeline.addPipe(this.tokenizeToTextBlocksPipeFactory.create());
    // Identify the roles of the text blocks.
    pipeline.addPipe(this.semanticizeTextBlocksPipeFactory.create());
    // Tokenize the text blocks into paragraphs.
    pipeline.addPipe(this.tokenizeToParagraphsPipeFactory.create());
    // Dehyphenate the words.
    pipeline.addPipe(this.dehyphenateWordsPipeFactory.create());

    log.debug("# pipes in the pipeline: " + pipeline.size());

    long start = System.currentTimeMillis();
    pipeline.process(pdf);
    long length = System.currentTimeMillis() - start;

    log.debug("Processing the core pipeline done.");
    log.debug("Time needed to process the core pipeline: " + length + "ms.");

    log.debug("End of pipe: " + getClass().getSimpleName() + ".");
    return pdf;
  }
}
