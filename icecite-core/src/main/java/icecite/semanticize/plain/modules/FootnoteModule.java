package icecite.semanticize.plain.modules;

import java.util.List;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterStatistics;
import icecite.models.PdfDocument;
import icecite.models.PdfFontFace;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.Rectangle;
import icecite.utils.math.MathUtils;

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

        PdfCharacterStatistics blockCharStats = block.getCharacterStatistics();
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
