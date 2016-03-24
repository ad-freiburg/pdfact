package merge;

import java.util.ArrayList;
import java.util.List;

import model.PdfDocument;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextLine;
import model.PdfTextParagraph;
import model.PdfWord;

public class PlainPdfBodyTextMerger implements PdfBodyTextMerger {

  @Override
  public void mergeBodyText(PdfDocument document) {
    mergeParagraphs(document);
  }
  
  public void mergeParagraphs(PdfDocument document) {
    if (document == null) {
      return;
    }
    
    List<PdfPage> pages = document.getPages();
    if (pages == null) {
      return;
    }
    
    List<PdfTextParagraph> revisedParagraphs = new ArrayList<>();
    PdfTextParagraph prevBodyTextParagraph = null;
    
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
        
        if (appendToPrevBodyTextParagraph(paragraph, prevBodyTextParagraph)) {
          prevBodyTextParagraph.addTextLines(paragraph.getTextLines());
        } else {
          revisedParagraphs.add(paragraph);
        }
        
        if (paragraphRole == PdfRole.BODY_TEXT) {
          prevBodyTextParagraph = paragraph;
        }
      }
    }
  }
  
  protected boolean appendToPrevBodyTextParagraph(PdfTextParagraph paragraph, 
      PdfTextParagraph prevBodyTextParagraph) {
    if (paragraph == null || prevBodyTextParagraph == null) {
      return false;
    }
    
    if (paragraph.getRole() != PdfRole.BODY_TEXT) {
      return false;
    }
    
    PdfTextLine lastTextLine = prevBodyTextParagraph.getLastTextLine();
    if (lastTextLine == null) {
      return false;
    }
    
    PdfWord lastWord = lastTextLine.getLastWord();
    if (lastWord == null) {
      return false;
    }
    
    String lastWordText = lastWord.getText(true, true, true);
    if (lastWordText == null) {
      return false;
    }
    
    return lastWordText.endsWith("-");
  }
}
