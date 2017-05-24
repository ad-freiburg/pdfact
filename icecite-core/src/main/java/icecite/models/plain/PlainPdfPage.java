package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;

/**
 * A plain implementation of {@link PdfPage}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfPage implements PdfPage {
  /**
   * The characters of this page.
   */
  protected PdfCharacterList characters;

  /**
   * The figures in this page.
   */
  protected Set<PdfFigure> figures;

  /**
   * The shapes in this page.
   */
  protected Set<PdfShape> shapes;

  /**
   * The number of this page in the PDF document.
   */
  protected int pageNumber;

  /**
   * The identified paragraphs of this page.
   */
  protected List<PdfParagraph> paragraphs;

  /**
   * The identified text blocks of this page.
   */
  protected List<PdfTextBlock> textBlocks;
  
  /**
   * The identified text lines of this page.
   */
  protected List<PdfTextLine> textLines;
  
  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF page.
   */
  @AssistedInject
  public PlainPdfPage() {
    this.paragraphs = new ArrayList<>();
    this.textBlocks = new ArrayList<>();
    this.textLines = new ArrayList<>();
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
  public PdfCharacterList getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(PdfCharacterList characters) {
    this.characters = characters;
  }

  // ==========================================================================

  @Override
  public Set<PdfFigure> getFigures() {
    return this.figures;
  }

  @Override
  public void setFigures(Set<PdfFigure> figures) {
    this.figures = figures;
  }

  // ==========================================================================

  @Override
  public Set<PdfShape> getShapes() {
    return this.shapes;
  }

  @Override
  public void setShapes(Set<PdfShape> shapes) {
    this.shapes = shapes;
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
  public List<PdfTextLine> getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(List<PdfTextLine> lines) {
    this.textLines = lines;
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
