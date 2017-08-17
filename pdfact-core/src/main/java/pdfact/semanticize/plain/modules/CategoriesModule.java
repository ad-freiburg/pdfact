package pdfact.semanticize.plain.modules;

import java.util.List;

import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role "categories".
 * 
 * @author Claudius Korzen
 */
public class CategoriesModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member of
   * the Categories section or not.
   */
  protected boolean isCategories = false;

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

        PdfRole role = block.getRole();
        PdfRole secondaryRole = block.getSecondaryRole();

        // Check if the current block is a section heading (which would
        // denote the end of the Categories section).
        if (this.isCategories && role == PdfRole.HEADING) {
          this.isCategories = false;
        }

        if (this.isCategories) {
          block.setRole(PdfRole.CATEGORIES);
        }

        // Check if the current block is the heading of the Categories section
        // (which would denote the start of the Categories section).
        if (role == PdfRole.HEADING && secondaryRole == PdfRole.CATEGORIES) {
          this.isCategories = true;
        }
      }
    }
  }
}
