package pdfact.core.pipes.semanticize;

import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.semanticize.modules.PdfTextSemanticizerModule;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.log.InjectLogger;

/**
 * A plain implementation of {@link DetectSemanticsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainDetectSemanticsPipe
    implements DetectSemanticsPipe {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The semanticizer modules.
   */
  protected Set<PdfTextSemanticizerModule> modules;

  /**
   * The default constructor.
   * 
   * @param set
   *        The semanticizer modules.
   */
  @Inject
  public PlainDetectSemanticsPipe(Set<PdfTextSemanticizerModule> set) {
    this.modules = set;
  }

  // ==========================================================================

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    log.debug("Start of pipe: " + getClass().getSimpleName() + ".");

    log.debug("Process: Detecting the semantics of the text blocks.");
    log.debug("# registered semanticizer modules: " + this.modules.size());
    detectSemantics(pdf);

    log.debug("Detecting the semantics of the text blocks done.");
    log.debug("End of pipe: " + getClass().getSimpleName() + ".");

    return pdf;
  }

  /**
   * Detects the semantics of the text blocks in the given PDF document.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return The processed PDF document.
   */
  protected PdfDocument detectSemantics(PdfDocument pdf) {
    for (PdfTextSemanticizerModule module : this.modules) {
      module.semanticize(pdf);
    }
    return pdf;
  }
}
