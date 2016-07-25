package merge;

import java.util.List;

import model.PdfDocument;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextAlignment;
import model.PdfTextLine;
import model.PdfTextParagraph;

public class PlainPdfBodyTextMerger implements PdfBodyTextMerger {
  protected boolean isParagraphSplitterInBetween = false;

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
                  
        if (appendToPrevParagraph(paragraph, prevBodyTextParagraph)) {
          prevBodyTextParagraph.addTextLines(paragraph.getTextLines());
          paragraph.setIgnore(true);
        } else if (paragraph.getRole() == PdfRole.BODY_TEXT) {
          prevBodyTextParagraph = paragraph;
        }
      }
    }
  }
  
  protected boolean appendToPrevParagraph(PdfTextParagraph paragraph, 
      PdfTextParagraph prevBodyTextParagraph) {
    if (paragraph == null || prevBodyTextParagraph == null) {
      return false;
    }
    
    PdfRole paraRole = paragraph.getRole();
        
    if (paraRole == PdfRole.FORMULA
        || paraRole == PdfRole.SECTION_HEADING
        || paraRole == PdfRole.ABSTRACT_HEADING
        || paraRole == PdfRole.REFERENCES_HEADING) {
      this.isParagraphSplitterInBetween = true;
      return false;
    }
    
    if (paraRole != PdfRole.BODY_TEXT) {
      return false;
    }
    
    if (this.isParagraphSplitterInBetween) {
      this.isParagraphSplitterInBetween = false;
      return false;
    }
    
    this.isParagraphSplitterInBetween = false;
        
    PdfTextLine lastTextLine = prevBodyTextParagraph.getLastTextLine();
    if (lastTextLine == null) {
      return false;
    }
    
    PdfTextLine firstTextLine = paragraph.getFirstTextLine();
    if (firstTextLine == null) {
      return false;
    }
        
    if (firstTextLine.getAlignment() == PdfTextAlignment.JUSTIFIED
        || firstTextLine.getAlignment() == PdfTextAlignment.LEFT) {
      return true;
    }
    
    return false;
  }
}
