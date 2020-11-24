package pdfact.core.pipes.semanticize.modules;

import java.util.List;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the text blocks with the semantic role
 * "acknowledgments".
 * 
 * @author Claudius Korzen
 */
public class AcknowledgmentsModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member of
   * the Acknowledgments section or not.
   */
  protected boolean isAcknowledgments = false;

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
        SemanticRole secondRole = block.getSecondarySemanticRole();

        // Check if the current block is a section heading (which would
        // denote the end of the Acknowledgments section).
        if (this.isAcknowledgments && role == SemanticRole.HEADING) {
          this.isAcknowledgments = false;
        }

        if (this.isAcknowledgments) {
          block.setSemanticRole(SemanticRole.ACKNOWLEDGMENTS);
        }

        // Check if the current block is the heading of the Acknowledgments
        // section (which would denote the start of the Acknowledgments
        // section).
        if (role == SemanticRole.HEADING
            && secondRole == SemanticRole.ACKNOWLEDGMENTS) {
          this.isAcknowledgments = true;
        }
      }
    }
  }
}
