package pdfact.semanticize.plain.modules;

import java.util.List;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfDocument;
import pdfact.models.PdfFontFace;
import pdfact.models.PdfPage;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfTextLineList;
import pdfact.utils.geometric.Line;
import pdfact.utils.geometric.Rectangle;
import pdfact.utils.math.MathUtils;

/**
 * A module that identifies the text blocks with the semantic role "footnote".
 * 
 * @author Claudius Korzen
 */
public class FootnoteModule implements PdfTextSemanticizerModule {
  @Override
  public void semanticize(PdfDocument pdf) {
    if (pdf == null) {
      return;
    }

    List<PdfPage> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    for (PdfPage page : pages) {
      if (page == null) {
        continue;
      }

      for (PdfTextBlock block : page.getTextBlocks()) {
        if (block == null) {
          continue;
        }

        // Don't overwrite existing roles.
        if (block.getRole() != null) {
          continue;
        }

        // Obtain the first character of the text block.
        PdfTextLineList textLines = block.getTextLines();
        if (textLines == null || textLines.isEmpty()) {
          continue;
        }
        PdfTextLine firstLine = textLines.get(0);
        if (firstLine == null) {
          continue;
        }

        Line firstBaseLine = firstLine.getBaseline();
        PdfCharacter firstChar = firstLine.getFirstWord().getFirstCharacter();
        Rectangle firstCharBox = firstChar.getPosition().getRectangle();
        float firstCharMinY = firstCharBox.getMinY();
        float lineBaseLineY = firstBaseLine.getStartY();

        // The block is *not* a footnote, if the first char is not raised.
        // TODO
        if (MathUtils.isSmallerOrEqual(firstCharMinY, lineBaseLineY, 1)) {
          continue;
        }

        PdfCharacterStatistic blockCharStats = block.getCharacterStatistic();
        PdfFontFace fontFace = blockCharStats.getMostCommonFontFace();
        // The text block is *not* a footnote, if the font face of the 1st char
        // is equal to the most common font face in the text line.
        if (firstChar.getFontFace() == fontFace) {
          continue;
        }

        block.setRole(PdfRole.FOOTNOTE);
      }
    }
  }
}
