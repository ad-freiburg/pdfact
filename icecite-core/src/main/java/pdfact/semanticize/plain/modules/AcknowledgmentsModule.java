package pdfact.semanticize.plain.modules;

import java.util.List;

import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role
 * "acknowledgments".
 * 
 * @author Claudius Korzen
 */
public class AcknowledgmentsModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member
   * of the Acknowledgments section or not.
   */
  protected boolean isAcknowledgments = false;

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
        PdfRole secondRole = block.getSecondaryRole();

        // Check if the current block is a section heading (which would
        // denote the end of the Acknowledgments section).
        if (this.isAcknowledgments && role == PdfRole.HEADING) {
          this.isAcknowledgments = false;
        }

        if (this.isAcknowledgments) {
          block.setRole(PdfRole.ACKNOWLEDGMENTS);
        }

        // Check if the current block is the heading of the Acknowledgments
        // section (which would denote the start of the Acknowledgments
        // section).
        if (role == PdfRole.HEADING && secondRole == PdfRole.ACKNOWLEDGMENTS) {
          this.isAcknowledgments = true;
        }
      }
    }
  }
}
