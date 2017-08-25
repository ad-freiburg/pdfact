package pdfact.pipes.semanticize.modules;

import java.util.List;

import pdfact.model.Character;
import pdfact.model.CharacterStatistic;
import pdfact.model.FontFace;
import pdfact.model.Line;
import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.model.Rectangle;
import pdfact.model.SemanticRole;
import pdfact.model.TextBlock;
import pdfact.model.TextLine;
import pdfact.util.MathUtils;

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
        if (MathUtils.isSmallerOrEqual(firstCharMinY, lineBaseLineY, 1)) {
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
