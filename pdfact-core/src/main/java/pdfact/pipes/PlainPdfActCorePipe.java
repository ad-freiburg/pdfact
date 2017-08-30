package pdfact.pipes;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import pdfact.PdfActCoreSettings;
import pdfact.model.LogLevel;
import pdfact.model.PdfDocument;
import pdfact.pipes.dehyphenate.DehyphenateWordsPipe;
import pdfact.pipes.dehyphenate.DehyphenateWordsPipe.DehyphenateWordsPipeFactory;
import pdfact.pipes.filter.characters.FilterCharactersPipe;
import pdfact.pipes.filter.characters.FilterCharactersPipe.FilterCharactersPipeFactory;
import pdfact.pipes.filter.figures.FilterFiguresPipe;
import pdfact.pipes.filter.figures.FilterFiguresPipe.FilterFiguresPipeFactory;
import pdfact.pipes.filter.shapes.FilterShapesPipe;
import pdfact.pipes.filter.shapes.FilterShapesPipe.FilterShapesPipeFactory;
import pdfact.pipes.parse.ParsePdfStreamsPipe;
import pdfact.pipes.parse.ParsePdfStreamsPipe.ParsePdfPipeFactory;
import pdfact.pipes.semanticize.DetectSemanticsPipe;
import pdfact.pipes.semanticize.DetectSemanticsPipe.DetectSemanticsPipeFactory;
import pdfact.pipes.tokenize.areas.TokenizeToTextAreasPipe;
import pdfact.pipes.tokenize.areas.TokenizeToTextAreasPipe.TokenizeToTextAreasPipeFactory;
import pdfact.pipes.tokenize.blocks.TokenizeToTextBlocksPipe;
import pdfact.pipes.tokenize.blocks.TokenizeToTextBlocksPipe.TokenizeToTextBlocksPipeFactory;
import pdfact.pipes.tokenize.lines.TokenizeToTextLinesPipe;
import pdfact.pipes.tokenize.lines.TokenizeToTextLinesPipe.TokenizeToTextLinesPipeFactory;
import pdfact.pipes.tokenize.paragraphs.TokenizeToParagraphsPipe;
import pdfact.pipes.tokenize.paragraphs.TokenizeToParagraphsPipe.TokenizeToParagraphsPipeFactory;
import pdfact.pipes.tokenize.words.TokenizeToWordsPipe;
import pdfact.pipes.tokenize.words.TokenizeToWordsPipe.TokenizeToWordsPipeFactory;
import pdfact.pipes.translate.diacritics.MergeDiacriticsPipe;
import pdfact.pipes.translate.diacritics.MergeDiacriticsPipe.MergeDiacriticsPipeFactory;
import pdfact.pipes.translate.ligatures.SplitLigaturesPipe;
import pdfact.pipes.translate.ligatures.SplitLigaturesPipe.SplitLigaturesPipeFactory;
import pdfact.pipes.validate.ValidatePdfPathPipe;
import pdfact.pipes.validate.ValidatePdfPathPipe.ValidatePdfPathPipeFactory;
import pdfact.util.exception.PdfActException;
import pdfact.util.pipeline.Pipeline;
import pdfact.util.pipeline.Pipeline.PdfActPipelineFactory;

/**
 * A plain implementation of {@link PdfActCorePipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfActCorePipe implements PdfActCorePipe {
  /**
   * The logger.
   */
  protected static final Logger LOG = Logger.getLogger(PdfActCorePipe.class);

  /**
   * The logger level.
   */
  protected LogLevel logLevel = PdfActCoreSettings.DEFAULT_LOG_LEVEL;

  // ==========================================================================

  /**
   * The factory to create instance of {@link Pipeline}.
   */
  protected PdfActPipelineFactory pipelineFactory;

  /**
   * The factory to create instances of {@link ValidatePdfPathPipe}.
   */
  protected ValidatePdfPathPipeFactory validatePdfPathPipeFactory;

  /**
   * The factory to create instances of {@link ParsePdfStreamsPipe}.
   */
  protected ParsePdfPipeFactory parsePdfPipeFactory;

  /**
   * The factory to create instances of {@link MergeDiacriticsPipe}.
   */
  protected MergeDiacriticsPipeFactory mergeDiacriticsPipeFactory;

  /**
   * The factory to create instances of {@link SplitLigaturesPipe}.
   */
  protected SplitLigaturesPipeFactory splitLigaturesPipeFactory;

  /**
   * The factory to create instances of {@link FilterCharactersPipe}.
   */
  protected FilterCharactersPipeFactory filterCharactersPipeFactory;

  /**
   * The factory to create instances of {@link FilterFiguresPipe}.
   */
  protected FilterFiguresPipeFactory filterFiguresPipeFactory;

  /**
   * The factory to create instances of {@link FilterShapesPipe}.
   */
  protected FilterShapesPipeFactory filterShapesPipeFactory;

  /**
   * The factory to create instances of {@link TokenizeToTextAreasPipe}.
   */
  protected TokenizeToTextAreasPipeFactory tokenizeToTextAreasPipeFactory;

  /**
   * The factory to create instances of {@link TokenizeToTextLinesPipe}.
   */
  protected TokenizeToTextLinesPipeFactory tokenizeToTextLinesPipeFactory;

  /**
   * The factory to create instances of {@link TokenizeToWordsPipe}.
   */
  protected TokenizeToWordsPipeFactory tokenizeToWordsPipeFactory;

  /**
   * The factory to create instances of {@link TokenizeToTextBlocksPipe}.
   */
  protected TokenizeToTextBlocksPipeFactory tokenizeToTextBlocksPipeFactory;

  /**
   * The factory to create instances of {@link DetectSemanticsPipe}.
   */
  protected DetectSemanticsPipeFactory semanticizeTextBlocksPipeFactory;

  /**
   * The factory to create instances of {@link TokenizeToParagraphsPipe}.
   */
  protected TokenizeToParagraphsPipeFactory tokenizeToParagraphsPipeFactory;

  /**
   * The factory to create instances of {@link DehyphenateWordsPipe}.
   */
  protected DehyphenateWordsPipeFactory dehyphenateWordsPipeFactory;

  // ==========================================================================

  /**
   * The default constructor.
   *
   * @param pipelineFactory
   *        A factory to create instances of {@link Pipeline}.
   * @param validatePdfPathPipeFactory
   *        A factory to create instances of {@link ValidatePdfPathPipe}.
   * @param parsePdfPipeFactory
   *        A factory to create instances of {@link ParsePdfStreamsPipe}.
   * @param mergeDiacriticsFactory
   *        A factory to create instances of {@link MergeDiacriticsPipeFactory}.
   * @param filterCharactersPipeFactory
   *        A factory to create instances of {@link FilterCharactersPipe}.
   * @param filterFiguresPipeFactory
   *        A factory to create instances of {@link FilterFiguresPipe}.
   * @param filterShapesPipeFactory
   *        A factory to create instances of {@link FilterShapesPipe}.
   * @param splitLigaturesFactory
   *        A factory to create instances of {@link SplitLigaturesPipeFactory}.
   * @param tokenizeToTextAreasPipeFactory
   *        A factory to create instances of {@link TokenizeToTextAreasPipe}.
   * @param tokenizeToTextLinesPipeFactory
   *        A factory to create instances of {@link TokenizeToTextLinesPipe}.
   * @param tokenizeToWordsPipeFactory
   *        A factory to create instances of {@link TokenizeToWordsPipe}.
   * @param tokenizeToTextBlocksPipeFactory
   *        A factory to create instances of {@link TokenizeToTextBlocksPipe}.
   * @param semanticizeTextBlocksPipeFactory
   *        A factory to create instances of {@link DetectSemanticsPipe}.
   * @param tokenizeToParagraphsPipeFactory
   *        A factory to create instances of {@link TokenizeToParagraphsPipe}.
   * @param dehyphenateWordsPipeFactory
   *        A factory to create instances of {@link DehyphenateWordsPipe}.
   */
  @Inject
  public PlainPdfActCorePipe(PdfActPipelineFactory pipelineFactory,
      ValidatePdfPathPipeFactory validatePdfPathPipeFactory,
      ParsePdfPipeFactory parsePdfPipeFactory,
      MergeDiacriticsPipeFactory mergeDiacriticsFactory,
      SplitLigaturesPipeFactory splitLigaturesFactory,
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
    this.mergeDiacriticsPipeFactory = mergeDiacriticsFactory;
    this.splitLigaturesPipeFactory = splitLigaturesFactory;
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
    // Fill the pipeline with the pipes to execute
    Pipeline pipeline = this.pipelineFactory.create();

    // Validate the path to the PDF file.
    pipeline.addPipe(this.validatePdfPathPipeFactory.create());
    // Extract the characters, shapes and figures.
    pipeline.addPipe(this.parsePdfPipeFactory.create());
    // Merge the diacritics.
    pipeline.addPipe(this.mergeDiacriticsPipeFactory.create());
    // Split the ligatures.
    pipeline.addPipe(this.splitLigaturesPipeFactory.create());
    // Filter the characters.
    pipeline.addPipe(this.filterCharactersPipeFactory.create());
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

    return pipeline.process(pdf);
  }
}
