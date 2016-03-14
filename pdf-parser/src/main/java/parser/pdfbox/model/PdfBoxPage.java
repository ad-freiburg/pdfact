package parser.pdfbox.model;

import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import de.freiburg.iif.counter.ObjectCounter;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.PdfArea;
import model.PdfFigure;
import model.PdfPage;
import model.PdfTextAlignment;

/**
 * Concrete implementation of a PdfTextPage using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxPage extends PdfBoxArea implements PdfPage {
  /**
   * The text document, in which this page is located.
   */
  protected PdfBoxDocument pdfDocument;

  /**
   * The page in fashion of PdfBox.
   */
  protected PDPage pdPage;

  /**
   * The mediaBox of this page.
   */
  protected Rectangle mediaBox;

  /**
   * The cropBox of this page.
   */
  protected Rectangle cropBox;

  /**
   * The content box (the bounding box of all content in page).
   */
  protected Rectangle artBox;

  /**
   * The trim box (the bounding box of all content but headers/footers.
   */
  protected Rectangle trimBox;

  /**
   * The page number.
   */
  protected int pageNumber;

  /**
   * The x-offset between cropBox and mediaBox.
   */
  protected float cropXOffset;

  /**
   * The y-offset between cropBox and mediaBox.
   */
  protected float cropYOffset;

  /**
   * The alignment of the text line in this page.
   */
  protected PdfTextAlignment alignment;
  
  /**
   * The default constructor.
   */
  public PdfBoxPage(PDPage page, int pageNumber) {
    super(null); // We cannot pass this here.
    this.pdPage = page;
    this.pageNumber = pageNumber;

    // The CropBox is usually the visible page area within a larger MediaBox.
    // It is often used to hide things like printers crop marks and is generally
    // the visible part you see in a PDF viewer.
    // But it is possible, that the cropBox is larger than the mediaBox. In that
    // cases, take the mediaBox as cropBox.
    // See https://blog.idrsolutions.com/2012/02/...
    // what-happens-if-the-cropbox-is-smaller-than-the-mediabox/
    PDRectangle mediaBox = pdPage.getMediaBox();
    PDRectangle cropBox = pdPage.getCropBox();

    this.mediaBox = new SimpleRectangle(
        mediaBox.getLowerLeftX(), mediaBox.getLowerLeftY(),
        mediaBox.getUpperRightX(), mediaBox.getUpperRightY());

    this.cropBox = new SimpleRectangle(
        cropBox.getLowerLeftX(), cropBox.getLowerLeftY(),
        cropBox.getUpperRightX(), cropBox.getUpperRightY());

    this.cropXOffset = this.cropBox.getMinX() - this.mediaBox.getMinX();
    this.cropYOffset = this.cropBox.getMinY() - this.mediaBox.getMinX();

    // Check, if the cropBox is larger than the mediaBox.
    if (this.cropBox.getArea() > this.mediaBox.getArea()) {
      // Swap cropBox and mediaBox.
      Rectangle tmp = this.cropBox;
      this.cropBox = this.mediaBox;
      this.mediaBox = tmp;
    }

    this.artBox = this.cropBox;
    this.trimBox = this.cropBox;
  }

  @Override
  public Rectangle getMediaBox() {
    return this.mediaBox;
  }

  @Override
  public Rectangle getCropBox() {
    return this.cropBox;
  }

  @Override
  public Rectangle getArtBox() {
    return this.artBox;
  }

  @Override
  public float getCropXOffset() {
    return cropXOffset;
  }

  @Override
  public float getCropYOffset() {
    return cropYOffset;
  }

  @Override
  public Rectangle getTrimBox() {
    return this.trimBox;
  }

  @Override
  // Override getPage(), because we have passed null to the super constructor.
  public PdfPage getPage() {
    return this;
  }

  @Override
  public int getPageNumber() {
    return pageNumber;
  }

  // ___________________________________________________________________________

  /**
   * Returns the page in fashion of PdfBox.
   */
  protected PDPage getPdPage() {
    return this.pdPage;
  }

  @Override
  public PdfBoxDocument getPdfDocument() {
    return this.pdfDocument;
  }

  /**
   * Sets the text document, in which this text document is located.
   */
  public void setPdfDocument(PdfBoxDocument document) {
    this.pdfDocument = document;
  }

  /**
   * Returns the reosurces.
   */
  public PDResources getResources() {
    return pdPage.getResources();
  }

  @Override
  public float getFiguresCoverage() {
    float cover = 0;
    for (PdfFigure figure : getFigures()) {
      cover += figure.getRectangle().getArea() / this.getRectangle().getArea();
    }
    return cover;
  }

  List<? extends PdfArea> blocks;
  
  @Override
  public void setBlocks(List<? extends PdfArea> blocks) {
    this.blocks = blocks;
  }
  
  @Override
  public List<? extends PdfArea> getBlocks() {
    return this.blocks;
  }

  @Override
  public PdfTextAlignment getTextLineAlignment() {
    if (alignment == null) {
      alignment = computeTextLineAlignment();
    }
    return alignment;
  }
  
  public PdfTextAlignment computeTextLineAlignment() {
    ObjectCounter<PdfTextAlignment> alignmentCounter = new ObjectCounter<>(); 
    
    for (PdfArea block : getBlocks()) {
      alignmentCounter.add(block.getTextLineAlignment());
    }
        
    return alignmentCounter.getMostFrequentObject();
  }
}
