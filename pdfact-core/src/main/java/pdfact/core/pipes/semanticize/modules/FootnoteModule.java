package pdfact.core.pipes.semanticize.modules;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Document;
import pdfact.core.model.FontFace;
import pdfact.core.model.Line;
import pdfact.core.model.Page;
import pdfact.core.model.Rectangle;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.util.PdfActUtils;

/**
 * A module that identifies the text blocks with the semantic role "footnote".
 * 
 * @author Claudius Korzen
 */
public class FootnoteModule implements PdfTextSemanticizerModule {
   /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("role-detection");
  
  @Override
  public void semanticize(Document pdf) {
    log.debug("=====================================================");
    log.debug("Detecting text blocks of semantic role '%s' ...", SemanticRole.FOOTNOTE);
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

        // Obtain the first character of the text block.
        TextLine firstTextLine = block.getFirstTextLine();
        if (firstTextLine == null) {
          continue;
        }

        Line firstBaseLine = firstTextLine.getBaseline();
        if (firstBaseLine == null) {
          continue;
        }

        Character firstChar = firstTextLine.getFirstWord().getFirstCharacter();
        Rectangle firstCharBox = firstChar.getPosition().getRectangle();
        float firstCharMinY = firstCharBox.getMinY();
        float lineBaseLineY = firstBaseLine.getStartY();

        // The block is *not* a footnote, if the first char is not raised.
        // TODO
        if (PdfActUtils.isSmallerOrEqual(firstCharMinY, lineBaseLineY, 1)) {
          continue;
        }

        CharacterStatistic blockCharStats = block.getCharacterStatistic();
        FontFace blockFontFace = blockCharStats.getMostCommonFontFace();
        FontFace firstCharFontFace = firstChar.getFontFace();
        // The text block is *not* a footnote, if the font face of the 1st char
        // is equal to the most common font face in the text line.
        if (firstCharFontFace == blockFontFace) {
          continue;
        }

        log.debug("-----------------------------------------------------");
        log.debug("Text block: \"%s\" ...", block.getText());
        log.debug("... page:                       %d", block.getPosition().getPageNumber());
        log.debug("... font face:                  %s", blockFontFace);
        log.debug("... min-y of 1st character:     %.1f", firstCharMinY);
        log.debug("... min-y of 1st text line:     %.1f", lineBaseLineY);
        log.debug("... font face of 1st character: %s", firstCharFontFace);
        log.debug("... assigned role:              %s", SemanticRole.FOOTNOTE);
        log.debug("... role reason:                the first character is raised compared to the "
          + "first text line and doesn't exhibit the font most commonly used in the text block");
        block.setSemanticRole(SemanticRole.FOOTNOTE);
      }
    }
  }
}
