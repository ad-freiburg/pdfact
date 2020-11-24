package pdfact.core.pipes.semanticize.modules;

import java.util.List;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "appendix".
 * 
 * @author Claudius Korzen
 */
public class AppendixModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member of
   * the appendix or not.
   */
  protected boolean isAppendix = false;

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
        // denote the end of the appendix).
        if (this.isAppendix && role == SemanticRole.HEADING) {
          this.isAppendix = false;
        }

        if (this.isAppendix) {
          block.setSemanticRole(SemanticRole.APPENDIX);
        }

        // Check if the current block is the heading of the appendix (which
        // would denote the start of the appendix).
        if (role == SemanticRole.HEADING
            && secondaryRole == SemanticRole.APPENDIX) {
          this.isAppendix = true;
        }
      }
    }
  }
}
