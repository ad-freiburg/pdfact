package pdfact.core.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.util.list.CharacterList;
import pdfact.core.util.list.TextLineList;
import pdfact.core.util.list.CharacterList.CharacterListFactory;
import pdfact.core.util.list.TextLineList.TextLineListFactory;

/**
 * A plain implementation of {@link Page}.
 * 
 * @author Claudius Korzen
 */
public class PlainPage implements Page {
  /**
   * The characters of this page.
   */
  protected CharacterList characters;

  /**
   * The figures of this page.
   */
  protected List<Figure> figures;

  /**
   * The shapes of this page.
   */
  protected List<Shape> shapes;

  /**
   * The text areas of this page.
   */
  protected List<TextArea> textAreas;

  /**
   * The text lines of this page.
   */
  protected TextLineList textLines;

  /**
   * The text blocks of this page.
   */
  protected List<TextBlock> textBlocks;

  /**
   * The number of this page in the PDF document.
   */
  protected int pageNumber;

  /**
   * The statistics about the characters of this page.
   */
  protected CharacterStatistic characterStatistic;

  /**
   * The statistics about the text lines of this page.
   */
  protected TextLineStatistic textLineStatistic;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link CharacterList}.
   * @param textLineListFactory
   *        The factory to create instances of {@link TextLineList}.
   */
  @AssistedInject
  public PlainPage(CharacterListFactory characterListFactory,
      TextLineListFactory textLineListFactory) {
    this(characterListFactory, textLineListFactory, 0);
  }

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link CharacterList}.
   * @param textLineListFactory
   *        The factory to create instances of {@link TextLineList}.
   * @param num
   *        The number of this page in the PDF document.
   */
  @AssistedInject
  public PlainPage(CharacterListFactory characterListFactory,
      TextLineListFactory textLineListFactory, @Assisted int num) {
    this.characters = characterListFactory.create();
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
    this.textAreas = new ArrayList<>();
    this.textLines = textLineListFactory.create();
    this.textBlocks = new ArrayList<>();
    this.pageNumber = num;
  }

  // ==========================================================================

  @Override
  public CharacterList getCharacters() {
    return this.characters;
  }

  @Override
  public Character getFirstCharacter() {
    if (this.characters == null || this.characters.isEmpty()) {
      return null;
    }
    return this.characters.get(0);
  }

  @Override
  public Character getLastCharacter() {
    if (this.characters == null || this.characters.isEmpty()) {
      return null;
    }
    return this.characters.get(this.characters.size() - 1);
  }

  @Override
  public void setCharacters(CharacterList characters) {
    this.characters = characters;
  }

  @Override
  public void addCharacters(CharacterList characters) {
    this.characters.addAll(characters);
  }

  @Override
  public void addCharacter(Character character) {
    this.characters.add(character);
  }

  // ==========================================================================

  @Override
  public List<Figure> getFigures() {
    return this.figures;
  }

  @Override
  public Figure getFirstFigure() {
    if (this.figures == null || this.figures.isEmpty()) {
      return null;
    }
    return this.figures.get(0);
  }

  @Override
  public Figure getLastFigure() {
    if (this.figures == null || this.figures.isEmpty()) {
      return null;
    }
    return this.figures.get(this.figures.size() - 1);
  }

  @Override
  public void setFigures(List<Figure> figures) {
    this.figures = figures;
  }

  @Override
  public void addFigures(List<Figure> figures) {
    this.figures.addAll(figures);
  }

  @Override
  public void addFigure(Figure figure) {
    this.figures.add(figure);
  }

  // ==========================================================================

  @Override
  public List<Shape> getShapes() {
    return this.shapes;
  }

  @Override
  public Shape getFirstShape() {
    if (this.shapes == null || this.shapes.isEmpty()) {
      return null;
    }
    return this.shapes.get(0);
  }

  @Override
  public Shape getLastShape() {
    if (this.shapes == null || this.shapes.isEmpty()) {
      return null;
    }
    return this.shapes.get(this.shapes.size() - 1);
  }

  @Override
  public void setShapes(List<Shape> shapes) {
    this.shapes = shapes;
  }

  @Override
  public void addShapes(List<Shape> shapes) {
    this.shapes.addAll(shapes);
  }

  @Override
  public void addShape(Shape shape) {
    this.shapes.add(shape);
  }

  // ==========================================================================

  @Override
  public List<TextArea> getTextAreas() {
    return this.textAreas;
  }

  @Override
  public TextArea getFirstTextArea() {
    if (this.textAreas == null || this.textAreas.isEmpty()) {
      return null;
    }
    return this.textAreas.get(0);
  }

  @Override
  public TextArea getLastTextArea() {
    if (this.textAreas == null || this.textAreas.isEmpty()) {
      return null;
    }
    return this.textAreas.get(this.textAreas.size() - 1);
  }

  @Override
  public void setTextAreas(List<TextArea> areas) {
    this.textAreas = areas;
  }

  @Override
  public void addTextAreas(List<TextArea> areas) {
    this.textAreas.addAll(areas);
  }

  @Override
  public void addTextArea(TextArea area) {
    this.textAreas.add(area);
  }

  // ==========================================================================

  @Override
  public TextLineList getTextLines() {
    return this.textLines;
  }

  @Override
  public TextLine getFirstTextLine() {
    if (this.textLines == null || this.textLines.isEmpty()) {
      return null;
    }
    return this.textLines.get(0);
  }

  @Override
  public TextLine getLastTextLine() {
    if (this.textLines == null || this.textLines.isEmpty()) {
      return null;
    }
    return this.textLines.get(this.textLines.size() - 1);
  }

  @Override
  public void setTextLines(TextLineList textLines) {
    this.textLines = textLines;
  }

  @Override
  public void addTextLines(TextLineList textLines) {
    this.textLines.addAll(textLines);
  }

  @Override
  public void addTextLine(TextLine textLine) {
    this.textLines.add(textLine);
  }

  // ==========================================================================

  @Override
  public List<TextBlock> getTextBlocks() {
    return this.textBlocks;
  }

  @Override
  public TextBlock getFirstTextBlock() {
    if (this.textBlocks == null || this.textBlocks.isEmpty()) {
      return null;
    }
    return this.textBlocks.get(0);
  }

  @Override
  public TextBlock getLastTextBlock() {
    if (this.textBlocks == null || this.textBlocks.isEmpty()) {
      return null;
    }
    return this.textBlocks.get(this.textBlocks.size() - 1);
  }

  @Override
  public void setTextBlocks(List<TextBlock> blocks) {
    this.textBlocks = blocks;
  }

  @Override
  public void addTextBlocks(List<TextBlock> blocks) {
    this.textBlocks.addAll(blocks);
  }

  @Override
  public void addTextBlock(TextBlock block) {
    this.textBlocks.add(block);
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
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public TextLineStatistic getTextLineStatistic() {
    return this.textLineStatistic;
  }

  @Override
  public void setTextLineStatistic(TextLineStatistic statistic) {
    this.textLineStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPage(" + this.pageNumber + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Page) {
      Page otherPage = (Page) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getCharacters(), otherPage.getCharacters());
      builder.append(getFigures(), otherPage.getShapes());
      builder.append(getShapes(), otherPage.getShapes());
      builder.append(getTextAreas(), otherPage.getTextAreas());
      builder.append(getTextLines(), otherPage.getTextLines());
      builder.append(getTextBlocks(), otherPage.getTextBlocks());
      builder.append(getPageNumber(), otherPage.getPageNumber());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getCharacters());
    builder.append(getFigures());
    builder.append(getShapes());
    builder.append(getTextAreas());
    builder.append(getTextLines());
    builder.append(getTextBlocks());
    builder.append(getPageNumber());
    return builder.hashCode();
  }
}
