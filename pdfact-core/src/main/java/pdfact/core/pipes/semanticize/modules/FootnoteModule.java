package pdfact.core.pipes.semanticize.modules;

import java.util.List;

import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.FontFace;
import pdfact.core.model.Line;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
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
  @Override
  public void semanticize(PdfDocument pdf) {
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
        FontFace fontFace = blockCharStats.getMostCommonFontFace();
        // The text block is *not* a footnote, if the font face of the 1st char
        // is equal to the most common font face in the text line.
        if (firstChar.getFontFace() == fontFace) {
          continue;
        }

        block.setSemanticRole(SemanticRole.FOOTNOTE);
      }
    }
  }
}
