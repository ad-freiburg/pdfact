package pdfact.pipes.semanticize.modules;

import java.util.List;

import pdfact.model.PdfDocument;
import pdfact.model.Rectangle;
import pdfact.model.Page;
import pdfact.model.SemanticRole;
import pdfact.model.Shape;
import pdfact.model.TextBlock;

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

    List<Page> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    for (Page page : pages) {
      if (page == null) {
        continue;
      }

      List<Shape> shapes = page.getShapes();

      for (TextBlock block : page.getTextBlocks()) {
        if (block == null) {
          continue;
        }

        if (block.getSemanticRole() != null) {
          continue;
        }

        Rectangle blockRectangle = block.getPosition().getRectangle();
        
        for (Shape shape : shapes) {
          // The block is a part of a table if there is a shape that overlaps
          // the block, but the shape is not completely included in the block
          // (that avoids that blocks that contains underlined words would be
          // identified as table).
          Rectangle shapeRectangle = shape.getPosition().getRectangle();
          if (blockRectangle.contains(shapeRectangle)) {
            continue;
          }
          if (!shapeRectangle.overlaps(blockRectangle)) {
            continue;
          }
          block.setSemanticRole(SemanticRole.TABLE);
          break;
        }
      }
    }
  }
}
