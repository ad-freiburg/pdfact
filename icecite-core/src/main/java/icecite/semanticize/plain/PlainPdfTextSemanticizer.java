package icecite.semanticize.plain;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;
import icecite.semanticize.PdfTextSemanticizer;
import icecite.semanticize.plain.modules.SemanticRoleTester;
import icecite.semanticize.plain.modules.TitleRoleTester;

/**
 * A plain implementation of {@link PdfTextSemanticizer}.
 * 
 * @author Claudius Korzen.
 */
public class PlainPdfTextSemanticizer implements PdfTextSemanticizer {
  /**
   * The PDF document.
   */
  protected PdfDocument pdf;
  
  /**
   * The semantic role testers.
   */
  protected List<SemanticRoleTester> testers;

  // ==========================================================================

  /**
   * Creates a new PlainPdfTextSemanticizer.
   * 
   * @param pdf
   *        The PDF document.
   */
  @AssistedInject
  public PlainPdfTextSemanticizer(@Assisted PdfDocument pdf) {
    this.pdf = pdf;
    
    // List the role testers to use in this semanticizer.
    this.testers = new ArrayList<>();
    this.testers.add(new TitleRoleTester(pdf));
  }

  // ==========================================================================

  @Override
  public void semanticize() {
    if (this.pdf == null) {
      return;
    }

    List<PdfPage> pages = this.pdf.getPages();
    if (pages == null) {
      return;
    }

    // Semanticize each page separately.
    for (PdfPage page : pages) {
      semanticizePdfPage(this.pdf, page);
    }
  }

  /**
   * Semanticizes the elements of the given page.
   * 
   * @param pdf
   *        The PDF document to which the PDF page belongs to.
   * @param page
   *        The page to process.
   */
  protected void semanticizePdfPage(PdfDocument pdf, PdfPage page) {
    if (page == null) {
      return;
    }

    List<PdfTextBlock> blocks = page.getTextBlocks();
    if (blocks == null) {
      return;
    }

    for (PdfTextBlock block : blocks) {
      semanticizePdfTextBlock(pdf, page, block);
    }
  }

  /**
   * Semanticizes the given text block.
   * 
   * @param pdf
   *        The PDF document to which the PDF page belongs to.
   * @param page
   *        The page to process.
   * @param block
   *        The block to process.
   */
  protected void semanticizePdfTextBlock(PdfDocument pdf, PdfPage page,
      PdfTextBlock block) {
    for (SemanticRoleTester tester : this.testers) {
      if (tester.test(block)) {
        block.setRole(tester.getRole());
        break;
      }
    }
  }
}
