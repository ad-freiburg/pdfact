package pdfact.pipes.semanticize.modules;

import java.util.List;

import pdfact.model.CharacterStatistic;
import pdfact.model.PdfDocument;
import pdfact.model.FontFace;
import pdfact.model.Page;
import pdfact.model.SemanticRole;
import pdfact.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "title".
 * 
 * @author Claudius Korzen
 */
public class TitleModule implements PdfTextSemanticizerModule {
  @Override
  public void semanticize(PdfDocument pdf) {
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
      largestFontSizeBlock.setSemanticRole(SemanticRole.TITLE);
    }
  }
}