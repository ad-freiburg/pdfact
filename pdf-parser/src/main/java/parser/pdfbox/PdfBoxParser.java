package parser.pdfbox;

import java.io.IOException;
import java.nio.file.Path;

import model.PdfDocument;
import parser.PdfParser;
import parser.pdfbox.core.PdfTextStreamEngine;
import parser.pdfbox.model.PdfBoxCharacter;
import parser.pdfbox.model.PdfBoxDocument;
import parser.pdfbox.model.PdfBoxFigure;
import parser.pdfbox.model.PdfBoxPage;
import parser.pdfbox.model.PdfBoxShape;
import parser.pdfbox.rules.ConsiderRules;

/**
 * An implementation of a pdf parser using PdfBox. 
 *
 * @author Claudius Korzen
 *
 */
public class PdfBoxParser extends PdfTextStreamEngine implements PdfParser {  
  /** 
   * The current pdf document. 
   */
  protected PdfBoxDocument document;
  
  /**
   * The current page.
   */
  protected PdfBoxPage page;
  
  /**
   * The current page number.
   */
  protected int currentPageNumber;
  
  protected int currentExtractionOrderNumber;
  
  // ___________________________________________________________________________
    
  @Override
  public PdfDocument parse(Path file) throws IOException {
    processFile(file);    
    return this.document;
  }

  @Override
  public String getName() {
    return "pdfbox";
  }
  
  @Override 
  public String toString() {
    return getName();
  }
  
  // ___________________________________________________________________________
  // Overriding methods.
  
  @Override
  public void startDocument(PdfBoxDocument document) throws IOException {
    super.startDocument(document);    
    // Create new PdfTextDocument.
    this.document = document;
  }
    
  @Override
  public void startPage(PdfBoxPage page) throws IOException {
    super.startPage(page);    
    this.page = page;
    this.document.addTextPage(this.page);
  }
  
  @Override
  protected void showPdfTextCharacter(PdfBoxCharacter character) {
    super.showPdfTextCharacter(character);
            
    if (ConsiderRules.considerPdfCharacter(character)) {
      character.setExtractionOrderNumber(this.currentExtractionOrderNumber++);
      this.page.addTextCharacter(character);
    }
  }
  
  @Override
  public void showFigure(PdfBoxFigure figure) {
    super.showFigure(figure);
      
    if (ConsiderRules.considerPdfFigure(figure)) {
      figure.setExtractionOrderNumber(this.currentExtractionOrderNumber++);
      this.page.addFigure(figure);
    }
  }
    
  @Override
  public void showShape(PdfBoxShape shape) {
    super.showShape(shape);
        
    if (ConsiderRules.considerPdfShape(shape)) {
      shape.setExtractionOrderNumber(this.currentExtractionOrderNumber++);
      this.page.addShape(shape);
    }
  }
}
