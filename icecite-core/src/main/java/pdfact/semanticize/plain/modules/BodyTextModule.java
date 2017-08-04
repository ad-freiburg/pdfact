package pdfact.semanticize.plain.modules;

import java.util.List;

import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfDocument;
import pdfact.models.PdfFontFace;
import pdfact.models.PdfPage;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role "body".
 * 
 * @author Claudius Korzen
 */
public class BodyTextModule implements PdfTextSemanticizerModule {
  @Override
  public void semanticize(PdfDocument pdf) {
    if (pdf == null) {
      return;
    }

    List<PdfPage> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    // Compute the most common font face in the PDF document.
    PdfCharacterStatistic pdfCharStats = pdf.getCharacterStatistic();
    PdfFontFace pdfFontFace = pdfCharStats.getMostCommonFontFace();

    for (PdfPage page : pages) {
      if (page == null) {
        continue;
      }

      for (PdfTextBlock block : page.getTextBlocks()) {
        if (block == null) {
          continue;
        }

        if (block.getRole() != null) {
          continue;
        }

        // The text block is a member of the body text if its font face is
        // equal to the most common font face.
        PdfCharacterStatistic blockCharStats = block.getCharacterStatistic();
        PdfFontFace fontFace = blockCharStats.getMostCommonFontFace();
        if (fontFace == pdfFontFace) {
          block.setRole(PdfRole.BODY_TEXT);
        }
      }
    }
  }
}
