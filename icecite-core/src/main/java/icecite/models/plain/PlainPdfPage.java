package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfCharacterStatistics;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLineStatistics;

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
   * The statistics about the characters.
   */
  protected PdfCharacterStatistics characterStatistics;

  /**
   * The statistics about text lines.
   */
  protected PdfTextLineStatistics textLineStatistics;

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
    this.characters = characterListFactory.create();
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
    this.textBlocks = new ArrayList<>();
  }

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create instances of PdfCharacterList.
   * @param pageNumber
   *        The number of this page in the PDF document.
   */
  @AssistedInject
  public PlainPdfPage(PdfCharacterListFactory characterListFactory,
      @Assisted int pageNumber) {
    this(characterListFactory);
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
    for (PdfCharacter character : characters) {
      addCharacter(character);
    }
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
    if (this.figures == null) {
      return null;
    }
    return this.figures.get(0);
  }

  @Override
  public PdfFigure getLastFigure() {
    if (this.figures == null) {
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
    for (PdfFigure figure : figures) {
      addFigure(figure);
    }
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
    if (this.shapes == null) {
      return null;
    }
    return this.shapes.get(0);
  }

  @Override
  public PdfShape getLastShape() {
    if (this.shapes == null) {
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
    for (PdfShape shape : shapes) {
      addShape(shape);
    }
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
    if (this.textBlocks == null) {
      return null;
    }
    return this.textBlocks.get(0);
  }

  @Override
  public PdfTextBlock getLastTextBlock() {
    if (this.textBlocks == null) {
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
    for (PdfTextBlock block : blocks) {
      addTextBlock(block);
    }
  }

  @Override
  public void addTextBlock(PdfTextBlock block) {
    this.textBlocks.add(block);
  }

  // ==========================================================================

  @Override
  public PdfCharacterStatistics getCharacterStatistics() {
    return this.characterStatistics;
  }

  @Override
  public void setCharacterStatistics(PdfCharacterStatistics statistics) {
    this.characterStatistics = statistics;
  }

  @Override
  public PdfTextLineStatistics getTextLineStatistics() {
    return this.textLineStatistics;
  }

  @Override
  public void setPdfTextLineStatistics(PdfTextLineStatistics statistics) {
    this.textLineStatistics = statistics;
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

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfPage) {
      PdfPage otherPage = (PdfPage) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPageNumber(), otherPage.getPageNumber());

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
