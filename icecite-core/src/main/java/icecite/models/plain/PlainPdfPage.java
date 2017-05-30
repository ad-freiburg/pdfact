package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;

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
   * The identified text lines of this page.
   */
  protected PdfTextLineList textLines;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create instances of PdfCharacterList.
   * @param textLineListFactory
   *        The factory to create instances of PdfTextLineList.
   */
  @AssistedInject
  public PlainPdfPage(PdfCharacterListFactory characterListFactory,
      PdfTextLineListFactory textLineListFactory) {
    this.characters = characterListFactory.create();
    this.textLines = textLineListFactory.create();
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
    this.textBlocks = new ArrayList<>();
  }

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create instances of PdfCharacterList.
   * @param textLineListFactory
   *        The factory to create instances of PdfTextLineList.
   * @param pageNumber
   *        The number of this page in the PDF document.
   */
  @AssistedInject
  public PlainPdfPage(PdfCharacterListFactory characterListFactory,
      PdfTextLineListFactory textLineListFactory, @Assisted int pageNumber) {
    this(characterListFactory, textLineListFactory);
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
  public PdfTextLineList getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(PdfTextLineList lines) {
    this.textLines = lines;
  }

  @Override
  public void addTextLines(PdfTextLineList lines) {
    for (PdfTextLine line : lines) {
      addTextLine(line);
    }
  }

  @Override
  public void addTextLine(PdfTextLine line) {
    this.textLines.add(line);
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
}
