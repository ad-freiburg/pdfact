package icecite.semanticize.plain;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfDocument;
import icecite.semanticize.PdfTextSemanticizer;
import icecite.semanticize.plain.modules.PdfTextSemanticizerModule;

/**
 * A plain implementation of {@link PdfTextSemanticizer}.
 * 
 * @author Claudius Korzen.
 */
public class PlainPdfTextSemanticizer implements PdfTextSemanticizer {
  /**
   * The PDF document.
   */
  protected PdfDocument pdf;

  /**
   * The semantic role testers.
   */
  protected Set<PdfTextSemanticizerModule> modules;

  // ==========================================================================

  /**
   * Creates a new PlainPdfTextSemanticizer.
   * 
   * @param modules
   *        The semantic role modules.
   * @param pdf
   *        The PDF document.
   */
  @AssistedInject
  public PlainPdfTextSemanticizer(@Assisted PdfDocument pdf,
      Set<PdfTextSemanticizerModule> modules) {
    this.pdf = pdf;
    this.modules = modules;
  }

  // ==========================================================================

  @Override
  public void semanticize() {
    if (this.pdf == null) {
      return;
    }

    // Run each semanticizer module.
    for (PdfTextSemanticizerModule module : this.modules) {
      module.semanticize(this.pdf);
    }
  }
}
