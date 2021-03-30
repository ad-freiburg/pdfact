package pdfact.core.pipes.semanticize.modules;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Document;
import pdfact.core.model.Font;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "body".
 *
 * @author Claudius Korzen
 */
public class BodyTextModule implements PdfTextSemanticizerModule {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");

  @Override
  public void semanticize(Document pdf) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", SemanticRole.BODY_TEXT);
    log.debug("=====================================================");

    if (pdf == null) {
      return;
    }

    List<Page> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    // Compute the most common font face in the PDF document.
    CharacterStatistic pdfCharStats = pdf.getCharacterStatistic();
    FontFace pdfFontFace = pdfCharStats.getMostCommonFontFace();

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

        // The text block is a member of the body text if its font face is
        // equal to the most common font face.
        CharacterStatistic blockCharStats = block.getCharacterStatistic();
        FontFace blockFontFace = blockCharStats.getMostCommonFontFace();

        Font pdfFont = pdfFontFace.getFont();
        Font blockFont = blockFontFace.getFont();
        if (pdfFont != blockFont) {
          continue;
        }

        float pdfFontSize = pdfFontFace.getFontSize();
        float blockFontSize = blockFontFace.getFontSize();
        if (Math.abs(pdfFontSize - blockFontSize) > 0.05 * pdfFontSize) {
          continue;
        }

        log.debug("-----------------------------------------------------");
        log.debug("Text block: \"%s\" ...", block.getText());
        log.debug("... page:          %d", block.getPosition().getPageNumber());
        log.debug("... font face:     %s", block.getCharacterStatistic().getMostCommonFontFace());
        log.debug("... assigned role: %s", SemanticRole.BODY_TEXT);
        log.debug("... role reason:   the block exhibits the most common font face");
        block.setSemanticRole(SemanticRole.BODY_TEXT);
      }
    }
  }
}
