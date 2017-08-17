package pdfact.semanticize.plain.modules;

import java.util.List;

import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;

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
        // denote the end of the abstract).
        if (this.isAbstract && role == PdfRole.HEADING) {
          this.isAbstract = false;
        }

        if (this.isAbstract) {
          block.setRole(PdfRole.ABSTRACT);
        }

        // Check if the current block is the heading of the abstract (which
        // would denote the start of the abstract).
        if (role == PdfRole.HEADING && secondaryRole == PdfRole.ABSTRACT) {
          this.isAbstract = true;
        }
      }
    }
  }
}
