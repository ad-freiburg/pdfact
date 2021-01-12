package pdfact.core.pipes.semanticize.modules;

import java.util.List;
import pdfact.core.model.Document;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.util.comparator.FontFaceComparator;

/**
 * A module that identifies the role "OTHER" to all text blocks to which no semantic role was 
 * assigned yet.
 * 
 * @author Claudius Korzen
 */
public class OtherRoleModule implements PdfTextSemanticizerModule {
  /**
   * A comparator to compare font faces.
   */
  protected FontFaceComparator fontFaceComparator;
  
  /**
   * Creates a new module.
   */
  public OtherRoleModule() {
    this.fontFaceComparator = new FontFaceComparator();
  }

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

        if (block.getSemanticRole() != null) {
          continue;
        }

        FontFace blockFontFace = block.getCharacterStatistic().getMostCommonFontFace();
        FontFace pdfFontFace = pdf.getCharacterStatistic().getMostCommonFontFace();
        int x = this.fontFaceComparator.compare(pdfFontFace, blockFontFace);
        
        if (this.fontFaceComparator.compare(pdfFontFace, blockFontFace) < 0) {
          // The font face of the block is "larger" than the most common font face in the document.
          block.setSemanticRole(SemanticRole.HEADING);  
        } else {
          block.setSemanticRole(SemanticRole.OTHER);
        }
      }
    }
  }
}
