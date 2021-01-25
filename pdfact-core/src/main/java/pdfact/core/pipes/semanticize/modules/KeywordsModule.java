package pdfact.core.pipes.semanticize.modules;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "keywords".
 * 
 * @author Claudius Korzen
 */
public class KeywordsModule implements PdfTextSemanticizerModule {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");

  /**
   * A boolean flag that indicates whether the current text block is a member of the Keywords
   * section or not.
   */
  protected boolean isKeywords = false;

  @Override
  public void semanticize(Document pdf) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", SemanticRole.KEYWORDS);
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
        // denote the end of the Keywords section).
        if (this.isKeywords && role == SemanticRole.HEADING) {
          this.isKeywords = false;
        }

        if (this.isKeywords) {
          log.debug("-----------------------------------------------------");
          log.debug("Text block: \"%s\" ...", block.getText());
          log.debug("... page:          %d", block.getPosition().getPageNumber());
          log.debug("... assigned role: %s", SemanticRole.KEYWORDS);
          log.debug("... role reason:   the block is located between the detected " 
              + "start/end of the Keywords section");
          block.setSemanticRole(SemanticRole.KEYWORDS);
        }

        // Check if the current block is the heading of the Keywords section
        // (which would denote the start of the Keywords section).
        if (role == SemanticRole.HEADING && secondaryRole == SemanticRole.KEYWORDS) {
          this.isKeywords = true;
        }
      }
    }
  }
}
