package pdfact.pipes.semanticize.modules;

import java.util.List;

import pdfact.model.PdfDocument;
import pdfact.model.Page;
import pdfact.model.SemanticRole;
import pdfact.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role "General
 * Terms".
 * 
 * @author Claudius Korzen
 */
public class GeneralTermsModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member of
   * the "General Terms" section or not.
   */
  protected boolean isGeneralTerms = false;

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
        // denote the end of the "General Terms" section).
        if (this.isGeneralTerms && role == SemanticRole.HEADING) {
          this.isGeneralTerms = false;
        }

        if (this.isGeneralTerms) {
          block.setSemanticRole(SemanticRole.GENERAL_TERMS);
        }

        // Check if the current block is the heading of the "General Terms"
        // section heading (which would denote the start of the "General Terms"
        // section).
        if (role == SemanticRole.HEADING 
            && secondaryRole == SemanticRole.GENERAL_TERMS) {
          this.isGeneralTerms = true;
        }
      }
    }
  }
}