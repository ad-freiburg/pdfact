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
import pdfact.pipes.tokenize.blocks.DetectTextBlocksPipe;
import pdfact.pipes.tokenize.blocks.DetectTextBlocksPipe.DetectTextBlocksPipeFactory;
import pdfact.pipes.tokenize.lines.DetectTextLinesPipe;
import pdfact.pipes.tokenize.lines.DetectTextLinesPipe.DetectTextLinesPipeFactory;
import pdfact.pipes.tokenize.paragraphs.DetectParagraphsPipe;
import pdfact.pipes.tokenize.paragraphs.DetectParagraphsPipe.DetectParagraphsPipeFactory;
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
   * The factory to create instances of {@link DetectTextLinesPipe}.
   */
  protected DetectTextLinesPipeFactory tokenizeTextLinesPipeFactory;
  
  /**
   * The factory to create instances of {@link DetectTextBlocksPipe}.
   */
  protected DetectTextBlocksPipeFactory tokenizeTextBlocksPipeFactory;

  /**
   * The factory to create instances of {@link DetectSemanticsPipe}.
   */
  protected DetectSemanticsPipeFactory semanticizeTextBlocksPipeFactory;

  /**
   * The factory to create instances of {@link DetectParagraphsPipe}.
   */
  protected DetectParagraphsPipeFactory tokenizeParagraphsPipeFactory;

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
   * @param tokenizeTextLinesPipeFactory
   *        A factory to create instances of {@link DetectTextLinesPipe}.
   * @param tokenizeTextBlocksPipeFactory
   *        A factory to create instances of {@link DetectTextBlocksPipe}.
   * @param semanticizeTextBlocksPipeFactory
   *        A factory to create instances of {@link DetectSemanticsPipe}.
   * @param tokenizeParagraphsPipeFactory
   *        A factory to create instances of {@link DetectParagraphsPipe}.
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
      DetectTextLinesPipeFactory tokenizeTextLinesPipeFactory,
      DetectTextBlocksPipeFactory tokenizeTextBlocksPipeFactory,
      DetectSemanticsPipeFactory semanticizeTextBlocksPipeFactory,
      DetectParagraphsPipeFactory tokenizeParagraphsPipeFactory,
      DehyphenateWordsPipeFactory dehyphenateWordsPipeFactory) {
    this.pipelineFactory = pipelineFactory;
    this.validatePdfPathPipeFactory = validatePdfPathPipeFactory;
    this.parsePdfPipeFactory = parsePdfPipeFactory;
    this.mergeDiacriticsPipeFactory = mergeDiacriticsFactory;
    this.splitLigaturesPipeFactory = splitLigaturesFactory;
    this.filterCharactersPipeFactory = filterCharactersPipeFactory;
    this.filterFiguresPipeFactory = filterFiguresPipeFactory;
    this.filterShapesPipeFactory = filterShapesPipeFactory;
    this.semanticizeTextBlocksPipeFactory = semanticizeTextBlocksPipeFactory;
    this.tokenizeTextLinesPipeFactory = tokenizeTextLinesPipeFactory;
    this.tokenizeTextBlocksPipeFactory = tokenizeTextBlocksPipeFactory;
    this.tokenizeParagraphsPipeFactory = tokenizeParagraphsPipeFactory;
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
    // Tokenize the text lines.
    pipeline.addPipe(this.tokenizeTextLinesPipeFactory.create());
    // Tokenize the text blocks.
    pipeline.addPipe(this.tokenizeTextBlocksPipeFactory.create());
    // Identify the roles of the text blocks.
    pipeline.addPipe(this.semanticizeTextBlocksPipeFactory.create());
    // Tokenize the paragraphs.
    pipeline.addPipe(this.tokenizeParagraphsPipeFactory.create());
    // Dehyphenate the words.
    pipeline.addPipe(this.dehyphenateWordsPipeFactory.create());

    long start = System.currentTimeMillis();
    pipeline.process(pdf);
    long end = System.currentTimeMillis();

    System.out.println(end - start);

    return pdf;
  }
}
