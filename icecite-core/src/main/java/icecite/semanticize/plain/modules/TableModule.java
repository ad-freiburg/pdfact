package icecite.semanticize.plain.modules;

import java.util.List;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role "table".
 * 
 * @author Claudius Korzen
 */
public class TableModule implements PdfTextSemanticizerModule {  
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
      
      List<PdfShape> shapes = page.getShapes();
      
      for (PdfTextBlock block : page.getTextBlocks()) {
        if (block == null) {
          continue;
        }
        
        if (block.getRole() != null) {
          continue;
        }
        
        for (PdfShape shape : shapes) {
          // The block is a part of a table if there is a shape that overlaps
          // the block, but the shape is not completely included in the block
          // (that avoids that blocks that contains underlined words would be
          // identified as table).
          if (block.getRectangle().contains(shape.getRectangle())) {
            continue;
          }
          if (!shape.getRectangle().overlaps(block.getRectangle())) {
            continue;
          }
          block.setRole(PdfRole.TABLE);
          break;
        }
      }
    }
  }
}
