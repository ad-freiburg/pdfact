package pdfact.core.pipes.semanticize.modules;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "General
 * Terms".
 * 
 * @author Claudius Korzen
 */
public class GeneralTermsModule implements PdfTextSemanticizerModule {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");

  /**
   * A boolean flag that indicates whether the current text block is a member of
   * the "General Terms" section or not.
   */
  protected boolean isGeneralTerms = false;

  @Override
  public void semanticize(Document pdf) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", SemanticRole.GENERAL_TERMS);
    log.debug("=====================================================");
    
    if (pdf == null) {
      return;
    }

    List<Page> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    for (Page page : pages) {
      if (page == null) {
        continue;
      }

      for (TextBlock block : page.getTextBlocks()) {
        if (block == null) {
          continue;
        }

        SemanticRole role = block.getSemanticRole();
        SemanticRole secondaryRole = block.getSecondarySemanticRole();

        // Check if the current block is a section heading (which would
        // denote the end of the "General Terms" section).
        if (this.isGeneralTerms && role == SemanticRole.HEADING) {
          this.isGeneralTerms = false;
        }

        if (this.isGeneralTerms) {
          log.debug("-----------------------------------------------------");
          log.debug("Text block: \"%s\" ...", block.getText());
          log.debug("... page:          %d", block.getPosition().getPageNumber());
          log.debug("... assigned role: %s", SemanticRole.GENERAL_TERMS);
          log.debug("... role reason:   the block is located between the detected " 
              + "start/end of the General Terms section");
          block.setSemanticRole(SemanticRole.GENERAL_TERMS);
        }

        // Check if the current block is the heading of the "General Terms" section heading (which
        // would denote the start of the "General Terms" section).
        if (role == SemanticRole.HEADING && secondaryRole == SemanticRole.GENERAL_TERMS) {
          this.isGeneralTerms = true;
        }
      }
    }
  }
}