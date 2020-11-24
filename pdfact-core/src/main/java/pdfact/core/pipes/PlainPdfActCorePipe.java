package pdfact.core.pipes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pdfact.core.model.Document;
import pdfact.core.pipes.dehyphenate.PlainDehyphenateWordsPipe;
import pdfact.core.pipes.filter.characters.PlainFilterCharactersPipe;
import pdfact.core.pipes.filter.figures.PlainFilterFiguresPipe;
import pdfact.core.pipes.filter.shapes.PlainFilterShapesPipe;
import pdfact.core.pipes.parse.PlainParseDocumentPipe;
import pdfact.core.pipes.semanticize.PlainDetectSemanticsPipe;
import pdfact.core.pipes.tokenize.areas.XYCutTokenizeToTextAreasPipe;
import pdfact.core.pipes.tokenize.blocks.PlainTokenizeToTextBlocksPipe;
import pdfact.core.pipes.tokenize.lines.PlainTokenizeToTextLinesPipe;
import pdfact.core.pipes.tokenize.paragraphs.PlainTokenizeToParagraphsPipe;
import pdfact.core.pipes.tokenize.words.XYCutTokenizeToWordsPipe;
import pdfact.core.pipes.translate.characters.PlainStandardizeCharactersPipe;
import pdfact.core.pipes.translate.diacritics.PlainMergeDiacriticsPipe;
import pdfact.core.pipes.translate.ligatures.PlainSplitLigaturesPipe;
import pdfact.core.pipes.validate.PlainValidatePdfPathPipe;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.pipeline.Pipeline;
import pdfact.core.util.pipeline.PlainPipeline;

/**
 * A plain implementation of {@link PdfActCorePipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfActCorePipe implements PdfActCorePipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(PlainPdfActCorePipe.class);

  // ==============================================================================================

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
  public Document execute(Document pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Processing the core pipeline.");

    // Fill the pipeline with the pipes to execute
    Pipeline pipeline = new PlainPipeline();

    // Validate the path to the PDF file.
    pipeline.addPipe(new PlainValidatePdfPathPipe());
    // Extract the characters, shapes and figures.
    pipeline.addPipe(new PlainParseDocumentPipe());
    // Merge the diacritics.
    pipeline.addPipe(new PlainMergeDiacriticsPipe());
    // Split the ligatures.
    pipeline.addPipe(new PlainSplitLigaturesPipe());
    // Standardize characters.
    pipeline.addPipe(new PlainStandardizeCharactersPipe());
    // Filter the characters.
    pipeline.addPipe(new PlainFilterCharactersPipe());
    // Filter the figures.
    pipeline.addPipe(new PlainFilterFiguresPipe());
    // Filter the shapes.
    pipeline.addPipe(new PlainFilterShapesPipe());
    // Tokenize the page into text areas.
    pipeline.addPipe(new XYCutTokenizeToTextAreasPipe());
    // Tokenize the text areas into text lines.
    pipeline.addPipe(new PlainTokenizeToTextLinesPipe());
    // Tokenize the text lines into words.
    pipeline.addPipe(new XYCutTokenizeToWordsPipe());
    // Tokenize the text lines into text blocks.
    pipeline.addPipe(new PlainTokenizeToTextBlocksPipe());
    // Identify the roles of the text blocks.
    pipeline.addPipe(new PlainDetectSemanticsPipe());
    // Tokenize the text blocks into paragraphs.
    pipeline.addPipe(new PlainTokenizeToParagraphsPipe());
    // Dehyphenate the words.
    pipeline.addPipe(new PlainDehyphenateWordsPipe());

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
