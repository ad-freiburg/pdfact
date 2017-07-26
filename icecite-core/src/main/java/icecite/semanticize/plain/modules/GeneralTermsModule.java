package icecite.semanticize.plain.modules;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role 
 * "General Terms".
 * 
 * @author Claudius Korzen
 */
public class GeneralTermsModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member
   * of the "General Terms" section or not.
   */
  protected boolean isGeneralTerms = false;

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
        // denote the end of the "General Terms" section).
        if (this.isGeneralTerms && role == PdfRole.HEADING) {
          this.isGeneralTerms = false;
        }

        if (this.isGeneralTerms) {
          block.setRole(PdfRole.GENERAL_TERMS);
        }

        // Check if the current block is the heading of the "General Terms"
        // section heading (which would denote the start of the "General Terms"
        // section).
        if (role == PdfRole.HEADING && secondaryRole == PdfRole.GENERAL_TERMS) {
          this.isGeneralTerms = true;
        }
      }
    }
  }
}