package pdfact.core.pipes.semanticize.modules;

import java.util.List;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

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
  public void semanticize(Document pdf) {
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

        SemanticRole role = block.getSemanticRole();
        SemanticRole secondaryRole = block.getSecondarySemanticRole();

        // Check if the current block is a section heading (which would
        // denote the end of the Categories section).
        if (this.isCategories && role == SemanticRole.HEADING) {
          this.isCategories = false;
        }

        if (this.isCategories) {
          block.setSemanticRole(SemanticRole.CATEGORIES);
        }

        // Check if the current block is the heading of the Categories section
        // (which would denote the start of the Categories section).
        if (role == SemanticRole.HEADING
            && secondaryRole == SemanticRole.CATEGORIES) {
          this.isCategories = true;
        }
      }
    }
  }
}
