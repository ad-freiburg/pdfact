package pdfact.core.pipes.semanticize.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.util.comparator.MinYComparator;
import pdfact.core.util.counter.ObjectCounter;

/**
 * A module that identifies the text blocks with the semantic role "page header"
 * and "page footer".
 * 
 * @author Claudius Korzen
 */
public class PageHeaderFooterModule implements PdfTextSemanticizerModule {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");

  @Override
  public void semanticize(Document pdf) {
    if (pdf == null) {
      return;
    }

    List<Page> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    // Compute the lowest and topmost blocks of the pages.
    List<TextBlock> lowestBlocks = new ArrayList<>(pages.size());
    List<TextBlock> topMostBlocks = new ArrayList<>(pages.size());

    for (Page page : pages) {
      if (page == null) {
        continue;
      }

      List<TextBlock> textBlocks = page.getTextBlocks();
      if (textBlocks == null || textBlocks.isEmpty()) {
        continue;
      }

      // Sort the blocks by minY to obtain the lowest and topmost block.
      List<TextBlock> sortedBlocks = new ArrayList<>(textBlocks);
      Collections.sort(sortedBlocks, new MinYComparator());

      lowestBlocks.add(sortedBlocks.get(0));
      topMostBlocks.add(sortedBlocks.get(sortedBlocks.size() - 1));
    }

    semanticizeBlocks(lowestBlocks, SemanticRole.PAGE_FOOTER);
    semanticizeBlocks(topMostBlocks, SemanticRole.PAGE_HEADER);
  }

  // ==============================================================================================

  /**
   * Searches the given text blocks for page headers (or page footers). Assigns
   * the given role to each text block that we consider as an member of a page
   * header (or page footer).
   * 
   * @param blocks
   *        The text blocks to process.
   * @param role
   *        The semantic role to assign to the related text blocks.
   */
  protected void semanticizeBlocks(List<TextBlock> blocks, SemanticRole role) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", role);
    log.debug("=====================================================");
    
    if (blocks == null || blocks.isEmpty()) {
      return;
    }

    // Count the frequencies of texts in the blocks.
    ObjectCounter<String> textCounter = new ObjectCounter<>();
    for (TextBlock block : blocks) {
      textCounter.add(getNormalizedText(block));
    }

    int mostCommonTextFreq = textCounter.getMostCommonObjectFrequency();
    if (mostCommonTextFreq < blocks.size() / 2) {
      return;
    }

    String mostCommonText = textCounter.getMostCommonObject();

    for (TextBlock block : blocks) {
      String normalizedText = getNormalizedText(block);
      if (normalizedText.equals(mostCommonText)) {
        log.debug("-----------------------------------------------------");
        log.debug("Text block: \"%s\" ...", block.getText());
        log.debug("... page:          %d", block.getPosition().getPageNumber());
        log.debug("... assigned role: %s", role);
        log.debug("... role reason:   the text occurs on more than half of the pages.");
        block.setSemanticRole(role);
      }
    }
  }

  // ==============================================================================================

  /**
   * Returns the text of the given text blocks without any numbers.
   * 
   * @param block
   *        The text block to process.
   * 
   * @return The text of the given text blocks without any numbers.
   */
  protected String getNormalizedText(TextBlock block) {
    if (block == null) {
      return null;
    }

    String text = block.getText();
    if (text == null) {
      return null;
    }

    return text.replaceAll("\\d", "");
  }
}
