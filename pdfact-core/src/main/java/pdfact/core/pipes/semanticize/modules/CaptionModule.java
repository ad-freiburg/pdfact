package pdfact.core.pipes.semanticize.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Document;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "caption".
 * 
 * @author Claudius Korzen
 */
public class CaptionModule implements PdfTextSemanticizerModule {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");
  
  /**
   * The patterns to identify caption, per *secondary* role.
   */
  protected static final Map<SemanticRole, Pattern> CAPTION_PATTERNS;

  static {
    CAPTION_PATTERNS = new HashMap<>();

    Pattern figureCaptionPattern = Pattern.compile(
        "^(fig(\\.?|ure)|abbildung)\\s*\\d+", Pattern.CASE_INSENSITIVE);
    CAPTION_PATTERNS.put(SemanticRole.FIGURE, figureCaptionPattern);

    Pattern tableCaptionPattern = Pattern.compile(
        "^(table|tabelle)\\s*\\d+(\\.|:)", Pattern.CASE_INSENSITIVE);
    CAPTION_PATTERNS.put(SemanticRole.TABLE, tableCaptionPattern);
  }

  // ==============================================================================================

  @Override
  public void semanticize(Document pdf) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", SemanticRole.CAPTION);
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

        // Don't overwrite existing roles.
        if (block.getSemanticRole() != null) {
          continue;
        }

        // The text block is a caption if its text matches to one of the given patterns.
        for (SemanticRole role : CAPTION_PATTERNS.keySet()) {
          Pattern captionPattern = CAPTION_PATTERNS.get(role);
          Matcher captionMatcher = captionPattern.matcher(block.getText());
          if (captionMatcher.find()) {
            log.debug("-----------------------------------------------------");
            log.debug("Text block: \"%s\" ...", block.getText());
            log.debug("... page:          %d", block.getPosition().getPageNumber());
            log.debug("... assigned role: %s", SemanticRole.CAPTION);
            log.debug("... role reason:   the text matches the regex '%s'", captionPattern);
            block.setSemanticRole(SemanticRole.CAPTION);
            // Set also the secondary role, e.g. "figure" for a figures
            // caption.
            block.setSecondarySemanticRole(role);
            break;
          }
        }
      }
    }
  }
}
