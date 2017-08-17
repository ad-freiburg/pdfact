package pdfact.semanticize.plain.modules;

import java.util.List;

import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role "reference".
 * 
 * @author Claudius Korzen
 */
public class ReferenceModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member of
   * the Reference section or not.
   */
  protected boolean isReferences = false;

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
        // denote the end of the Reference section).
        if (this.isReferences && role == PdfRole.HEADING) {
          this.isReferences = false;
        }

        if (this.isReferences) {
          block.setRole(PdfRole.REFERENCE);
        }

        // Check if the current block is the section heading of the Reference
        // section (which would denote the end of the Reference section).
        if (role == PdfRole.HEADING && secondaryRole == PdfRole.REFERENCE) {
          this.isReferences = true;
        }
      }
    }
  }
}
