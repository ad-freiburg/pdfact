package pdfact.core.pipes.semanticize.modules;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "reference".
 * 
 * @author Claudius Korzen
 */
public class ReferenceModule implements PdfTextSemanticizerModule {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");

  /**
   * A boolean flag that indicates whether the current text block is a member of the Reference
   * section or not.
   */
  protected boolean isReferences = false;

  @Override
  public void semanticize(Document pdf) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", SemanticRole.REFERENCE);
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
        // denote the end of the Reference section).
        if (this.isReferences && role == SemanticRole.HEADING) {
          this.isReferences = false;
        }

        if (this.isReferences) {
          log.debug("-----------------------------------------------------");
          log.debug("Text block: \"%s\" ...", block.getText());
          log.debug("... page:          %d", block.getPosition().getPageNumber());
          log.debug("... assigned role: %s", SemanticRole.REFERENCE);
          log.debug("... role reason:   the block is located between the detected start/end of the "
                  + "Bibliography section");
          block.setSemanticRole(SemanticRole.REFERENCE);
        }

        // Check if the current block is the section heading of the Reference
        // section (which would denote the end of the Reference section).
        if (role == SemanticRole.HEADING && secondaryRole == SemanticRole.REFERENCE) {
          this.isReferences = true;
        }
      }
    }
  }
}
