package pdfact.models.plain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfCharacterList;
import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfFigure;
import pdfact.models.PdfPage;
import pdfact.models.PdfShape;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLineStatistic;
import pdfact.models.PdfCharacterList.PdfCharacterListFactory;

/**
 * A plain implementation of {@link PdfPage}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfPage implements PdfPage {
  /**
   * The number of this page in the PDF document.
   */
  protected int pageNumber;

  /**
   * The characters of this page.
   */
  protected PdfCharacterList characters;

  /**
   * The figures of this page.
   */
  protected List<PdfFigure> figures;

  /**
   * The shapes of this page.
   */
  protected List<PdfShape> shapes;

  /**
   * The text blocks of this page.
   */
  protected List<PdfTextBlock> textBlocks;

  /**
   * The statistics about the characters in this page.
   */
  protected PdfCharacterStatistic characterStatistic;

  /**
   * The statistics about the text lines in this page.
   */
  protected PdfTextLineStatistic textLineStatistic;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link PdfCharacterList}.
   */
  @AssistedInject
  public PlainPdfPage(PdfCharacterListFactory characterListFactory) {
    this(characterListFactory, 0);
  }

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link PdfCharacterList}.
   * @param pageNumber
   *        The number of this page in the PDF document.
   */
  @AssistedInject
  public PlainPdfPage(PdfCharacterListFactory characterListFactory,
      @Assisted int pageNumber) {
    this.characters = characterListFactory.create();
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
    this.textBlocks = new ArrayList<>();
    this.pageNumber = pageNumber;
  }

  // ==========================================================================

  @Override
  public PdfCharacterList getCharacters() {
    return this.characters;
  }

  @Override
  public PdfCharacter getFirstCharacter() {
    if (this.characters == null) {
      return null;
    }
    return this.characters.get(0);
  }

  @Override
  public PdfCharacter getLastCharacter() {
    if (this.characters == null) {
      return null;
    }
    return this.characters.get(this.characters.size() - 1);
  }

  @Override
  public void setCharacters(PdfCharacterList characters) {
    this.characters = characters;
  }

  @Override
  public void addCharacters(PdfCharacterList characters) {
    this.characters.addAll(characters);
  }

  @Override
  public void addCharacter(PdfCharacter character) {
    this.characters.add(character);
  }

  // ==========================================================================

  @Override
  public List<PdfFigure> getFigures() {
    return this.figures;
  }

  @Override
  public PdfFigure getFirstFigure() {
    if (this.figures == null || this.figures.isEmpty()) {
      return null;
    }
    return this.figures.get(0);
  }

  @Override
  public PdfFigure getLastFigure() {
    if (this.figures == null || this.figures.isEmpty()) {
      return null;
    }
    return this.figures.get(this.figures.size() - 1);
  }

  @Override
  public void setFigures(List<PdfFigure> figures) {
    this.figures = figures;
  }

  @Override
  public void addFigures(List<PdfFigure> figures) {
    this.figures.addAll(figures);
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
  public PdfShape getFirstShape() {
    if (this.shapes == null || this.shapes.isEmpty()) {
      return null;
    }
    return this.shapes.get(0);
  }

  @Override
  public PdfShape getLastShape() {
    if (this.shapes == null || this.shapes.isEmpty()) {
      return null;
    }
    return this.shapes.get(this.shapes.size() - 1);
  }

  @Override
  public void setShapes(List<PdfShape> shapes) {
    this.shapes = shapes;
  }

  @Override
  public void addShapes(List<PdfShape> shapes) {
    this.shapes.addAll(shapes);
  }

  @Override
  public void addShape(PdfShape shape) {
    this.shapes.add(shape);
  }

  // ==========================================================================

  @Override
  public List<PdfTextBlock> getTextBlocks() {
    return this.textBlocks;
  }

  @Override
  public PdfTextBlock getFirstTextBlock() {
    if (this.textBlocks == null || this.textBlocks.isEmpty()) {
      return null;
    }
    return this.textBlocks.get(0);
  }

  @Override
  public PdfTextBlock getLastTextBlock() {
    if (this.textBlocks == null || this.textBlocks.isEmpty()) {
      return null;
    }
    return this.textBlocks.get(this.textBlocks.size() - 1);
  }

  @Override
  public void setTextBlocks(List<PdfTextBlock> blocks) {
    this.textBlocks = blocks;
  }

  @Override
  public void addTextBlocks(List<PdfTextBlock> blocks) {
    this.textBlocks.addAll(blocks);
  }

  @Override
  public void addTextBlock(PdfTextBlock block) {
    this.textBlocks.add(block);
  }

  // ==========================================================================

  @Override
  public PdfCharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(PdfCharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==========================================================================
  
  @Override
  public PdfTextLineStatistic getTextLineStatistic() {
    return this.textLineStatistic;
  }

  @Override
  public void setTextLineStatistic(PdfTextLineStatistic statistic) {
    this.textLineStatistic = statistic;
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
  public String toString() {
    return "PlainPdfPage(" + this.pageNumber + ")";
  }

  // ==========================================================================
  
  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfPage) {
      PdfPage otherPage = (PdfPage) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPageNumber(), otherPage.getPageNumber());
      builder.append(getCharacters(), otherPage.getCharacters());
      builder.append(getFigures(), otherPage.getShapes());
      builder.append(getShapes(), otherPage.getShapes());
      builder.append(getTextBlocks(), otherPage.getTextBlocks());
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPageNumber());
    return builder.hashCode();
  }
}
