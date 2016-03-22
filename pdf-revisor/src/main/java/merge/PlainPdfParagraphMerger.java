package merge;

import java.util.ArrayList;
import java.util.List;

import model.PdfDocument;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextParagraph;

public class PlainPdfParagraphMerger implements PdfParagraphMerger {

  @Override
  public void merge(PdfDocument document) {
    // mergeParagraphs(document);
  }
  
  public void mergeParagraphs(PdfDocument document) {
    if (document == null) {
      return;
    }
    
    List<PdfPage> pages = document.getPages();
    if (pages == null) {
      return;
    }
    
    List<PdfTextParagraph> joinedParagraphs = new ArrayList<>();
    
    PdfTextParagraph activeParagraph = null;
    PdfRole prevParagraphRole = null;
    
    for (PdfPage page : pages) {
      if (page == null) {
        continue;
      }
      
      List<PdfTextParagraph> paragraphs = page.getParagraphs();
      if (paragraphs == null) {
        continue;
      }
      
      for (PdfTextParagraph paragraph : paragraphs) {
        if (paragraph == null) {
          continue;
        }
        
        PdfRole paragraphRole = paragraph.getRole();
        if (paragraphRole == null) {
          continue;
        }
                
        if (paragraphRole != prevParagraphRole) {
          joinedParagraphs.add(paragraph);
          activeParagraph = paragraph;
        } else {
          activeParagraph.addTextLines(paragraph.getTextLines());
        }
        
        prevParagraphRole = paragraphRole;
      }
    }
    
    // TODO: Set the merged paragraphs.
  }
}
