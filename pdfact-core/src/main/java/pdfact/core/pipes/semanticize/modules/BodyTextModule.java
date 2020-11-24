package pdfact.core.pipes.semanticize.modules;

import java.util.List;

import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "body".
 * 
 * @author Claudius Korzen
 */
public class BodyTextModule implements PdfTextSemanticizerModule {
  @Override
  public void semanticize(Document pdf) {
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
        FontFace fontFace = blockCharStats.getMostCommonFontFace();
        if (fontFace == pdfFontFace) {
          block.setSemanticRole(SemanticRole.BODY_TEXT);
        }
      }
    }
  }
}
