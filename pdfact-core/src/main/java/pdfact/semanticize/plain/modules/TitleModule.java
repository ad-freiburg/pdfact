package pdfact.semanticize.plain.modules;

import java.util.List;

import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfDocument;
import pdfact.models.PdfFontFace;
import pdfact.models.PdfPage;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;

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

    List<PdfPage> pages = pdf.getPages();
    if (pages == null || pages.isEmpty()) {
      return;
    }

    // Search the text blocks of only the first page.
    PdfPage firstPage = pages.get(0);
    if (firstPage == null) {
      return;
    }

    List<PdfTextBlock> textBlocks = firstPage.getTextBlocks();
    if (textBlocks == null) {
      return;
    }

    // Find the block with largest font size in the first page.
    float largestFontsize = -Float.MAX_VALUE;
    PdfTextBlock largestFontSizeBlock = null;

    for (PdfTextBlock block : textBlocks) {
      if (block == null) {
        continue;
      }

      PdfCharacterStatistic blockCharStats = block.getCharacterStatistic();
      PdfFontFace fontFace = blockCharStats.getMostCommonFontFace();
      if (fontFace.getFontSize() > largestFontsize) {
        largestFontsize = fontFace.getFontSize();
        largestFontSizeBlock = block;
      }
    }

    if (largestFontSizeBlock != null) {
      largestFontSizeBlock.setRole(PdfRole.TITLE);
    }
  }
}
