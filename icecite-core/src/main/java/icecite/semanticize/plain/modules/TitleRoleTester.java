package icecite.semanticize.plain.modules;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests for the role "title".
 * 
 * @author Claudius Korzen
 */
public class TitleRoleTester implements SemanticRoleTester {
  /**
   * The PDF document.
   */
  protected PdfDocument pdf;

  /**
   * The title candidate of the given PDF document.
   */
  protected PdfTextBlock titleCandidate;
  
  /**
   * A boolean flag that indicates whether the title was already found for the
   * given PDF document.
   */
  protected boolean isTitleFound;
  
  // ==========================================================================

  /**
   * Creates a new TitleRoleTester.
   * 
   * @param pdf
   *        The PDF document.
   */
  public TitleRoleTester(PdfDocument pdf) {
    this.pdf = pdf;
    // Find the title candidate.
    this.titleCandidate = findTitleCandidate();
  }

  // ==========================================================================

  @Override
  public boolean test(PdfTextBlock block) {
    // Check if the title was already found.
    if (this.isTitleFound) {
      return false;
    }
        
    if (block == null) {
      return false;
    }
    
    // Do *not* overwrite an existing role.
    if (block.getRole() != null) {
      return false;
    }
    
    // Check if the block is equal to the found title candidate.
    if (block == this.titleCandidate) {
      this.isTitleFound = true;
      return true;
    }
    
    return false;
  }

  @Override
  public PdfRole getRole() {
    return PdfRole.TITLE;
  }
  
  // ==========================================================================
  
  /**
   * Searches the PDF document for a title candidate.
   * 
   * @return The title candidate.
   */
  protected PdfTextBlock findTitleCandidate() {
    if (this.pdf == null) {
      return null;
    }
    
    List<PdfPage> pages = this.pdf.getPages();
    if (pages == null || pages.isEmpty()) {
      return null;
    }
    
    // Search the text blocks of only the first page.
    PdfPage firstPage = pages.get(0);
    if (firstPage == null) {
      return null;
    }
    
    List<PdfTextBlock> textBlocks = firstPage.getTextBlocks();
    if (textBlocks == null) {
      return null;
    }
    
    // Find the block with largest font size in the first page.
    float largestFontsize = -Float.MAX_VALUE;
    PdfTextBlock largestFontSizeBlock = null;
    
    for (PdfTextBlock block : textBlocks) {
      if (block == null || block.getCharacters() == null) {
        continue;
      }
      
      float fontsize = block.getCharacters().getMostCommonFontsize();
      if (fontsize > largestFontsize) {
        largestFontsize = fontsize;
        largestFontSizeBlock = block;
      }
    }
    
    return largestFontSizeBlock;
  }
}
