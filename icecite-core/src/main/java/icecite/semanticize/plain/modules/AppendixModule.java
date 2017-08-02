package icecite.semanticize.plain.modules;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role "appendix".
 * 
 * @author Claudius Korzen
 */
public class AppendixModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member
   * of the appendix or not.
   */
  protected boolean isAppendix = false;

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
        // denote the end of the appendix).
        if (this.isAppendix && role == PdfRole.HEADING) {
          this.isAppendix = false;
        }

        if (this.isAppendix) {
          block.setRole(PdfRole.APPENDIX);
        }

        // Check if the current block is the heading of the appendix (which
        // would denote the start of the appendix).
        if (role == PdfRole.HEADING && secondaryRole == PdfRole.APPENDIX) {
          this.isAppendix = true;
        }
      }
    }
  }
}
