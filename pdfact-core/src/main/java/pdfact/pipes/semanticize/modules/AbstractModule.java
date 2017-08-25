package pdfact.pipes.semanticize.modules;

import java.util.List;

import pdfact.model.PdfDocument;
import pdfact.model.Page;
import pdfact.model.SemanticRole;
import pdfact.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "abstract".
 * 
 * @author Claudius Korzen
 */
public class AbstractModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member of
   * the abstract or not.
   */
  protected boolean isAbstract = false;

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

        SemanticRole role = block.getSemanticRole();
        SemanticRole secondaryRole = block.getSecondaryRole();

        // Check if the current block is a section heading (which would
        // denote the end of the abstract).
        if (this.isAbstract && role == SemanticRole.HEADING) {
          this.isAbstract = false;
        }

        if (this.isAbstract) {
          block.setSemanticRole(SemanticRole.ABSTRACT);
        }

        // Check if the current block is the heading of the abstract (which
        // would denote the start of the abstract).
        if (role == SemanticRole.HEADING 
            && secondaryRole == SemanticRole.ABSTRACT) {
          this.isAbstract = true;
        }
      }
    }
  }
}
