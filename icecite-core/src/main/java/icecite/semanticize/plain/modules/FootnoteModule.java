package icecite.semanticize.plain.modules;

import java.util.List;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfFontFace;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.Rectangle;

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
        PdfTextLine firstTextLine = textLines.get(0);
        if (firstTextLine == null) {
          continue;
        }
        
        Line firstBaseLine = firstTextLine.getBaseline();
        PdfCharacterList firstTextLineChars = firstTextLine.getCharacters();
        PdfFontFace fontFace = firstTextLineChars.getMostCommonFontFace();
        PdfCharacterList firstLineCharacters = firstTextLine.getCharacters();
        if (firstLineCharacters == null || firstLineCharacters.isEmpty()) {
          continue;
        }
        PdfCharacter firstChar = firstLineCharacters.get(0);
        Rectangle firstCharBox = firstChar.getPosition().getRectangle();
        
        // The block is *not* a footnote, if the first char is not raised.
        if (firstCharBox.getMinY() <= firstBaseLine.getStartY()) {
          continue;
        }

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
