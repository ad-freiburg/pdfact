package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.util.list.ElementList;
import pdfact.core.util.list.ElementList.ElementListFactory;

/**
 * A plain implementation of {@link Page}.
 * 
 * @author Claudius Korzen
 */
public class PlainPage implements Page {
  /**
   * The characters of this page.
   */
  protected ElementList<Character> characters;

  /**
   * The figures of this page.
   */
  protected ElementList<Figure> figures;

  /**
   * The shapes of this page.
   */
  protected ElementList<Shape> shapes;

  /**
   * The text areas of this page.
   */
  protected ElementList<TextArea> textAreas;

  /**
   * The text lines of this page.
   */
  protected ElementList<TextLine> textLines;

  /**
   * The text blocks of this page.
   */
  protected ElementList<TextBlock> textBlocks;

  /**
   * The number of this page in the PDF document.
   */
  protected int pageNumber;

  /**
   * The statistic about the characters of this page.
   */
  protected CharacterStatistic characterStatistic;

  /**
   * The statistic about the text lines of this page.
   */
  protected TextLineStatistic textLineStatistic;

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create lists of characters.
   * @param figureListFactory
   *        The factory to create lists of figures.
   * @param shapeListFactory
   *        The factory to create lists of shapes.
   * @param textAreaListFactory
   *        The factory to create lists of text areas.
   * @param textLineListFactory
   *        The factory to create lists of text lines.
   * @param textBlockListFactory
   *        The factory to create lists of text blocks.
   */
  @AssistedInject
  public PlainPage(
      ElementListFactory<Character> characterListFactory,
      ElementListFactory<Figure> figureListFactory,
      ElementListFactory<Shape> shapeListFactory,
      ElementListFactory<TextArea> textAreaListFactory,
      ElementListFactory<TextLine> textLineListFactory,
      ElementListFactory<TextBlock> textBlockListFactory) {
    this(characterListFactory,
        figureListFactory,
        shapeListFactory,
        textAreaListFactory,
        textLineListFactory,
        textBlockListFactory,
        0);
  }

  /**
   * Creates a new PDF page.
   * 
   * @param characterListFactory
   *        The factory to create lists of characters.
   * @param figureListFactory
   *        The factory to create lists of figures.
   * @param shapeListFactory
   *        The factory to create lists of shapes.
   * @param textAreaListFactory
   *        The factory to create lists of text areas.
   * @param textLineListFactory
   *        The factory to create lists of text lines.
   * @param textBlockListFactory
   *        The factory to create lists of text blocks.
   * @param pageNumber
   *        The number of this page in the PDF document.
   */
  @AssistedInject
  public PlainPage(
      ElementListFactory<Character> characterListFactory,
      ElementListFactory<Figure> figureListFactory,
      ElementListFactory<Shape> shapeListFactory,
      ElementListFactory<TextArea> textAreaListFactory,
      ElementListFactory<TextLine> textLineListFactory,
      ElementListFactory<TextBlock> textBlockListFactory,
      @Assisted int pageNumber) {
    this.characters = characterListFactory.create();
    this.figures = figureListFactory.create();
    this.shapes = shapeListFactory.create();
    this.textAreas = textAreaListFactory.create();
    this.textLines = textLineListFactory.create();
    this.textBlocks = textBlockListFactory.create();
    this.pageNumber = pageNumber;
  }

  // ==========================================================================

  @Override
  public ElementList<Character> getCharacters() {
    return this.characters;
  }

  @Override
  public Character getFirstCharacter() {
    return this.characters.getFirstElement();
  }

  @Override
  public Character getLastCharacter() {
    return this.characters.getLastElement();
  }

  @Override
  public void setCharacters(ElementList<Character> characters) {
    this.characters = characters;
  }

  @Override
  public void addCharacters(ElementList<Character> characters) {
    this.characters.addAll(characters);
  }

  @Override
  public void addCharacter(Character character) {
    this.characters.add(character);
  }

  // ==========================================================================

  @Override
  public ElementList<Figure> getFigures() {
    return this.figures;
  }

  @Override
  public Figure getFirstFigure() {
    return this.figures.getFirstElement();
  }

  @Override
  public Figure getLastFigure() {
    return this.figures.getLastElement();
  }

  @Override
  public void setFigures(ElementList<Figure> figures) {
    this.figures = figures;
  }

  @Override
  public void addFigures(ElementList<Figure> figures) {
    this.figures.addAll(figures);
  }

  @Override
  public void addFigure(Figure figure) {
    this.figures.add(figure);
  }

  // ==========================================================================

  @Override
  public ElementList<Shape> getShapes() {
    return this.shapes;
  }

  @Override
  public Shape getFirstShape() {
    return this.shapes.getFirstElement();
  }

  @Override
  public Shape getLastShape() {
    return this.shapes.getLastElement();
  }

  @Override
  public void setShapes(ElementList<Shape> shapes) {
    this.shapes = shapes;
  }

  @Override
  public void addShapes(ElementList<Shape> shapes) {
    this.shapes.addAll(shapes);
  }

  @Override
  public void addShape(Shape shape) {
    this.shapes.add(shape);
  }

  // ==========================================================================

  @Override
  public ElementList<TextArea> getTextAreas() {
    return this.textAreas;
  }

  @Override
  public TextArea getFirstTextArea() {
    return this.textAreas.getFirstElement();
  }

  @Override
  public TextArea getLastTextArea() {
    return this.textAreas.getLastElement();
  }

  @Override
  public void setTextAreas(ElementList<TextArea> areas) {
    this.textAreas = areas;
  }

  @Override
  public void addTextAreas(ElementList<TextArea> areas) {
    this.textAreas.addAll(areas);
  }

  @Override
  public void addTextArea(TextArea area) {
    this.textAreas.add(area);
  }

  // ==========================================================================

  @Override
  public ElementList<TextLine> getTextLines() {
    return this.textLines;
  }

  @Override
  public TextLine getFirstTextLine() {
    return this.textLines.getFirstElement();
  }

  @Override
  public TextLine getLastTextLine() {
    return this.textLines.getLastElement();
  }

  @Override
  public void setTextLines(ElementList<TextLine> textLines) {
    this.textLines = textLines;
  }

  @Override
  public void addTextLines(ElementList<TextLine> textLines) {
    this.textLines.addAll(textLines);
  }

  @Override
  public void addTextLine(TextLine textLine) {
    this.textLines.add(textLine);
  }

  // ==========================================================================

  @Override
  public ElementList<TextBlock> getTextBlocks() {
    return this.textBlocks;
  }

  @Override
  public TextBlock getFirstTextBlock() {
    return this.textBlocks.getFirstElement();
  }

  @Override
  public TextBlock getLastTextBlock() {
    return this.textBlocks.getLastElement();
  }

  @Override
  public void setTextBlocks(ElementList<TextBlock> blocks) {
    this.textBlocks = blocks;
  }

  @Override
  public void addTextBlocks(ElementList<TextBlock> blocks) {
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
    return "Page(" + this.pageNumber + ")";
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
