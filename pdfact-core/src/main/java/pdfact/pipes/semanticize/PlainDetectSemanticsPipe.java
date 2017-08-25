package pdfact.pipes.semanticize;

import java.util.Set;

import com.google.inject.Inject;

import pdfact.model.PdfDocument;
import pdfact.pipes.semanticize.modules.PdfTextSemanticizerModule;
import pdfact.util.exception.PdfActException;

/**
 * A plain implementation of {@link DetectSemanticsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainDetectSemanticsPipe
    implements DetectSemanticsPipe {
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

  @Override
  public PdfDocument execute(PdfDocument pdf) throws PdfActException {
    for (PdfTextSemanticizerModule module : this.modules) {
      module.semanticize(pdf);
    }
    return pdf;
  }
}
