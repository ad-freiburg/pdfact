package pdfact.core.pipes.semanticize.modules;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Document;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "title".
 * 
 * @author Claudius Korzen
 */
public class TitleModule implements PdfTextSemanticizerModule {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");

  @Override
  public void semanticize(Document pdf) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", SemanticRole.TITLE);
    log.debug("=====================================================");
    
    if (pdf == null) {
      return;
    }

    List<Page> pages = pdf.getPages();
    if (pages == null || pages.isEmpty()) {
      return;
    }

    // Search the text blocks of only the first page.
    Page firstPage = pages.get(0);
    if (firstPage == null) {
      return;
    }

    List<TextBlock> textBlocks = firstPage.getTextBlocks();
    if (textBlocks == null) {
      return;
    }

    // Find the block with largest font size in the first page.
    float largestFontsize = -Float.MAX_VALUE;
    TextBlock largestFontSizeBlock = null;

    for (TextBlock block : textBlocks) {
      if (block == null) {
        continue;
      }

      CharacterStatistic blockCharStats = block.getCharacterStatistic();
      FontFace fontFace = blockCharStats.getMostCommonFontFace();
      if (fontFace.getFontSize() > largestFontsize) {
        largestFontsize = fontFace.getFontSize();
        largestFontSizeBlock = block;
      }
    }

    if (largestFontSizeBlock != null) {
      log.debug("-----------------------------------------------------");
      log.debug("Text block: \"%s\" ...", largestFontSizeBlock.getText());
      log.debug("... page:          %d", largestFontSizeBlock.getPosition().getPageNumber());
      float fs = largestFontSizeBlock.getCharacterStatistic().getMostCommonFontFace().getFontSize();
      log.debug("... fontsize:      %.1f", fs);
      log.debug("... assigned role: %s ", SemanticRole.TITLE);
      log.debug("... role reason:   the block exhibits the largest font size.");
      largestFontSizeBlock.setSemanticRole(SemanticRole.TITLE);
    }
  }
}
