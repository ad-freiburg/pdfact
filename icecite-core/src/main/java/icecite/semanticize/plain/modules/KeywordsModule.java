package icecite.semanticize.plain.modules;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role "keywords".
 * 
 * @author Claudius Korzen
 */
public class KeywordsModule implements PdfTextSemanticizerModule {
  /**
   * A boolean flag that indicates whether the current text block is a member
   * of the Keywords section or not.
   */
  protected boolean isKeywords = false;
  
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
        // denote the end of the Keywords section).
        if (this.isKeywords && role == PdfRole.HEADING) {
          this.isKeywords = false;
        }
        
        if (this.isKeywords) {
          block.setRole(PdfRole.KEYWORDS);
        }

        // Check if the current block is the heading of the Keywords section 
        // (which would denote the start of the Keywords section).
        if (role == PdfRole.HEADING && secondaryRole == PdfRole.KEYWORDS) {
          this.isKeywords = true;
        }
      }
    }
  }
}
