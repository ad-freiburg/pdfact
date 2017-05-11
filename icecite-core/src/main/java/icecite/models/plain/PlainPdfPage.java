package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;

/**
 * A plain implementation of {@link PdfPage}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfPage implements PdfPage {  
  /**
   * The characters of this page.
   */
  protected PdfCharacterSet characters;

  /**
   * The figures in this page.
   */
  protected List<PdfFigure> figures;

  /**
   * The shapes in this page.
   */
  protected List<PdfShape> shapes;

  /**
   * The number of this page in the PDF document.
   */
  protected int pageNumber;

  /**
   * The identified text blocks of this page.
   */
  protected List<PdfTextBlock> textBlocks;

  /**
   * The identified paragraphs of this page.
   */
  protected List<PdfParagraph> paragraphs;

  // ==========================================================================

  /**
   * Creates a new PDF page.
   */
  @AssistedInject
  public PlainPdfPage() {
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
    this.paragraphs = new ArrayList<>();
    this.textBlocks = new ArrayList<>();
  }

  /**
   * Creates a new PDF page.
   * 
   * @param pageNumber
   *        The number of this page in the PDF document.
   */
  @AssistedInject
  public PlainPdfPage(@Assisted int pageNumber) {
    this.pageNumber = pageNumber;
  }

  // ==========================================================================

  @Override
  public PdfCharacterSet getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(PdfCharacterSet characters) {
    this.characters = characters;
  }

  // ==========================================================================

  @Override
  public List<PdfFigure> getFigures() {
    return this.figures;
  }

  @Override
  public void setFigures(List<PdfFigure> figures) {
    this.figures = figures;
  }

  @Override
  public void addFigure(PdfFigure figure) {
    this.figures.add(figure);
  }

  // ==========================================================================

  @Override
  public List<PdfShape> getShapes() {
    return this.shapes;
  }

  @Override
  public void setShapes(List<PdfShape> shapes) {
    this.shapes = shapes;
  }

  @Override
  public void addShape(PdfShape shape) {
    this.shapes.add(shape);
  }

  // ==========================================================================

  @Override
  public int getPageNumber() {
    return this.pageNumber;
  }

  @Override
  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  // ==========================================================================

  @Override
  public List<PdfTextBlock> getTextBlocks() {
    return this.textBlocks;
  }

  @Override
  public void setTextBlocks(List<PdfTextBlock> blocks) {
    this.textBlocks = blocks;
  }

  @Override
  public void addTextBlock(PdfTextBlock block) {
    this.textBlocks.add(block);
  }

  // ==========================================================================

  @Override
  public List<PdfParagraph> getParagraphs() {
    return this.paragraphs;
  }

  @Override
  public void setParagraphs(List<PdfParagraph> paragraphs) {
    this.paragraphs = paragraphs;
  }

  @Override
  public void addParagraph(PdfParagraph paragraph) {
    this.paragraphs.add(paragraph);
  }
}
