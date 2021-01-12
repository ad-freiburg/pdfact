package pdfact.core.pipes.semanticize.modules;

import java.util.List;
import pdfact.core.model.Document;
import pdfact.core.model.Page;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;

/**
 * A module that identifies the role "OTHER" to all text blocks to which no semantic role was 
 * assigned yet.
 * 
 * @author Claudius Korzen
 */
public class OtherRoleModule implements PdfTextSemanticizerModule {
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

        block.setSemanticRole(SemanticRole.OTHER);
      }
    }
  }
}
