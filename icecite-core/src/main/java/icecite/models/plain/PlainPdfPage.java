package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfColor;
import icecite.models.PdfFigure;
import icecite.models.PdfFont;
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

  // ==========================================================================

  @Override
  public PdfFont getMostCommonFont() {
    return this.characters.getMostCommonFont();
  }

  @Override
  public PdfColor getMostCommonColor() {
    return this.characters.getMostCommonColor();
  }

  @Override
  public float getMostCommonFontsize() {
    return this.characters.getMostCommonFontsize();
  }

  @Override
  public float getAverageFontsize() {
    return this.characters.getAverageFontsize();
  }

  @Override
  public float getMostCommonHeight() {
    return this.characters.getMostCommonHeight();
  }

  @Override
  public float getAverageHeight() {
    return this.characters.getAverageHeight();
  }

  @Override
  public float getMostCommonWidth() {
    return this.characters.getMostCommonWidth();
  }

  @Override
  public float getAverageWidth() {
    return this.characters.getAverageWidth();
  }
}
