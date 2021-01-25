package pdfact.core.pipes.semanticize.modules;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.util.comparator.FontFaceComparator;

/**
 * A module that identifies the role "OTHER" to all text blocks to which no semantic role was
 * assigned yet.
 * 
 * @author Claudius Korzen
 */
public class OtherRoleModule implements PdfTextSemanticizerModule {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");

  /**
   * A comparator to compare font faces.
   */
  protected FontFaceComparator fontFaceComparator;

  /**
   * Creates a new module.
   */
  public OtherRoleModule() {
    this.fontFaceComparator = new FontFaceComparator();
  }

  @Override
  public void semanticize(Document pdf) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", SemanticRole.OTHER);
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

        if (block.getSemanticRole() != null) {
          continue;
        }

        FontFace blockFontFace = block.getCharacterStatistic().getMostCommonFontFace();
        FontFace pdfFontFace = pdf.getCharacterStatistic().getMostCommonFontFace();

        log.debug("-----------------------------------------------------");
        log.debug("Text block: \"%s\" ...", block.getText());
        log.debug("... page:          %d", block.getPosition().getPageNumber());

        if (this.fontFaceComparator.compare(pdfFontFace, blockFontFace) < 0) {
          log.debug("... assigned role: %s", SemanticRole.HEADING);
          log.debug("... role reason:   the block wasn't assigned to any role yet and its font face "
                  + "is larger than the most common font face).");
          // The font face of the block is "larger" than the most common font face in the document.
          block.setSemanticRole(SemanticRole.HEADING);
        } else {
          log.debug("... assigned role: %s", SemanticRole.OTHER);
          log.debug("... role reason:   the block wasn't assigned to any other role yet).");
          block.setSemanticRole(SemanticRole.OTHER);
        }
      }
    }
  }
}
