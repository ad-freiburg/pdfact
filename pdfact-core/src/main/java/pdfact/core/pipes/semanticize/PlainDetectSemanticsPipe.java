package pdfact.core.pipes.semanticize;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.pipes.semanticize.modules.AbstractModule;
import pdfact.core.pipes.semanticize.modules.AcknowledgmentsModule;
import pdfact.core.pipes.semanticize.modules.BodyTextModule;
import pdfact.core.pipes.semanticize.modules.CaptionModule;
import pdfact.core.pipes.semanticize.modules.CategoriesModule;
import pdfact.core.pipes.semanticize.modules.FootnoteModule;
import pdfact.core.pipes.semanticize.modules.GeneralTermsModule;
import pdfact.core.pipes.semanticize.modules.HeadingModule;
import pdfact.core.pipes.semanticize.modules.KeywordsModule;
import pdfact.core.pipes.semanticize.modules.OtherRoleModule;
import pdfact.core.pipes.semanticize.modules.PageHeaderFooterModule;
import pdfact.core.pipes.semanticize.modules.PdfTextSemanticizerModule;
import pdfact.core.pipes.semanticize.modules.ReferenceModule;
import pdfact.core.pipes.semanticize.modules.TitleModule;
import pdfact.core.util.exception.PdfActException;

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
  protected static Logger log = LogManager.getFormatterLogger("role-detection");

  /**
   * The semanticizer modules.
   */
  protected List<PdfTextSemanticizerModule> modules;

  /**
   * The default constructor.
   */
  public PlainDetectSemanticsPipe() {
    this.modules = new ArrayList<>();
    this.modules.add(new TitleModule());
    this.modules.add(new PageHeaderFooterModule());
    this.modules.add(new HeadingModule());
    this.modules.add(new AbstractModule());
    this.modules.add(new KeywordsModule());
    this.modules.add(new CategoriesModule());
    this.modules.add(new GeneralTermsModule());
    this.modules.add(new AcknowledgmentsModule());
    this.modules.add(new ReferenceModule());
    this.modules.add(new CaptionModule());
    this.modules.add(new FootnoteModule());
    // this.modules.add(new ItemizeItemModule());
    // this.modules.add(new TableModule());
    this.modules.add(new BodyTextModule());
    this.modules.add(new OtherRoleModule());
  }

  // ==============================================================================================

  @Override
  public Document execute(Document pdf) throws PdfActException {
    detectSemantics(pdf);
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
  protected Document detectSemantics(Document pdf) {
    for (PdfTextSemanticizerModule module : this.modules) {
      module.semanticize(pdf);
    }
    return pdf;
  }
}
