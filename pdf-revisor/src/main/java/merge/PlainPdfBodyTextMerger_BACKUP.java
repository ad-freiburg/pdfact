package merge;

import java.util.List;

import de.freiburg.iif.model.Line;
import model.PdfDocument;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextAlignment;
import model.PdfTextLine;
import model.PdfTextParagraph;
import model.PdfWord;

public class PlainPdfBodyTextMerger_BACKUP implements PdfBodyTextMerger {

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
                
        if (paragraphRole == PdfRole.BODY_TEXT) {
          if (appendToPrevBodyTextParagraph(paragraph, prevBodyTextParagraph)) {
            prevBodyTextParagraph.addTextLines(paragraph.getTextLines());
            paragraph.setIgnore(true);
          } else {
            prevBodyTextParagraph = paragraph;
          }
        }
        
        // If there is a section heading or formula in between, the paragraphs 
        // should *not* be merged.
        if (paragraphRole == PdfRole.SECTION_HEADING
            || paragraphRole == PdfRole.ABSTRACT_HEADING
            || paragraphRole == PdfRole.REFERENCES_HEADING) {
          prevBodyTextParagraph = null;
        }
      }
    }
  }
  
  protected boolean appendToPrevBodyTextParagraph(PdfTextParagraph paragraph, 
      PdfTextParagraph prevBodyTextParagraph) {
    if (paragraph == null || prevBodyTextParagraph == null) {
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
    
    // Check if there is a page or column break.
    PdfPage paraPage = paragraph.getPage();
    PdfPage prevParaPage = prevBodyTextParagraph.getPage();
    Line paraColumn = paragraph.getFirstTextLine().getColumnXRange();
    Line prevParaColumn = prevBodyTextParagraph.getFirstTextLine().getColumnXRange();
        
    if (paraPage != prevParaPage || paraColumn != prevParaColumn) {
      PdfTextLine firstLine = paragraph.getFirstTextLine();
      if (firstLine.getAlignment() != PdfTextAlignment.RIGHT) {
        return true;
      }
    }    
    return lastWordText.endsWith("-");
  }
}
