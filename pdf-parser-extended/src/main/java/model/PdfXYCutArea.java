package model;

import static de.freiburg.iif.math.MathUtils.isEqual;
import static de.freiburg.iif.math.MathUtils.isLarger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.freiburg.iif.counter.ObjectCounter;
import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Line;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.rtree.RTree;
import de.freiburg.iif.rtree.SimpleRTree;
import statistics.DimensionStatistician;
import statistics.PositionStatistician;
import statistics.TextLineStatistician;
import statistics.TextStatistician;

/**
 * An area, which consists of multiple elements.
 *
 * @author Claudius Korzen
 *
 */
public class PdfXYCutArea implements PdfArea {
  /**
   * The index of all elements.
   */
  protected RTree<PdfElement> elementsIndex;

  /**
   * The index of all text elements.
   */
  protected RTree<PdfTextElement> textElementsIndex;

  /**
   * The index of text characters.
   */
  protected RTree<PdfCharacter> charactersIndex;

  /**
   * The index of words.
   */
  protected RTree<PdfWord> wordsIndex;

  /**
   * The index of lines.
   */
  protected RTree<PdfTextLine> textLinesIndex;

  /**
   * The index of paragraphs.
   */
  protected RTree<PdfTextParagraph> textParagraphsIndex;

  /**
   * The index of all non text elements.
   */
  protected RTree<PdfElement> nonTextElementsIndex;

  /**
   * The index of all non text paragraphs.
   */
  protected RTree<PdfNonTextParagraph> nonTextParagraphsIndex;

  /**
   * The index of figures.
   */
  protected RTree<PdfFigure> figuresIndex;

  /**
   * The index of shapes.
   */
  protected RTree<PdfShape> shapesIndex;

  /**
   * The elements of this page by feature.
   */
  protected Map<PdfFeature, RTree<? extends PdfElement>> indexesByFeature;

  /**
   * All fonts in this page.
   */
  protected Map<String, PdfFont> fonts;

  /**
   * All colors in this page.
   */
  protected Map<String, PdfColor> colors;

  /**
   * The page, in which this area is located.
   */
  protected PdfPage page;

  /**
   * The dimension statistics of this area.
   */
  protected DimensionStatistics dimStatistics;

  /**
   * Flag to indicate, whether the dimension statistics are outdated.
   */
  protected boolean isDimensionStatisticsOutdated;

  /**
   * The text statistics.
   */
  protected TextStatistics textStatistics;

  /**
   * Flag to indicate if the text statistics is outdated.
   */
  protected boolean isTextStatisticsOutdated;

  /**
   * The text line statistics.
   */
  protected TextLineStatistics textLineStatistics;

  /**
   * Flag to indicate if the text line statistics is outdated.
   */
  protected boolean isTextLineStatisticsOutdated;

  /**
   * The position statistics.
   */
  protected PositionStatistics positionStatistics;

  /**
   * Flag to indicate if the position statistics is outdated.
   */
  protected boolean isPositionStatisticsOutdated;

  /**
   * Flag to indicate if we have to ignore this area.
   */
  protected boolean ignore;

  /**
   * This is the rectangle as it was computed from the XYCut. (In contrast,
   * 'rectangle' is the bounding box around the elements.)
   */
  protected Rectangle rawRectangle;

  /**
   * The alignment of text lines in this area.
   */
  protected PdfTextAlignment alignment;

  /**
   * Flag to indicate if we have to recompute the alignment.
   */
  protected boolean isAlignmentOutdated;

  protected boolean hasParagraphIndentations;

  protected boolean hasBodyIndentations;

  protected PdfArea block;
  
  protected Line columnXRange;
  
  // ___________________________________________________________________________
  // Constructors.

  /**
   * The default constructor is only available for extending classes.
   */
  public PdfXYCutArea(PdfPage page) {
    this.page = page;
    this.elementsIndex = new SimpleRTree<>();
    this.textElementsIndex = new SimpleRTree<>();
    this.nonTextElementsIndex = new SimpleRTree<>();
    this.charactersIndex = new SimpleRTree<>();
    this.wordsIndex = new SimpleRTree<>();
    this.textLinesIndex = new SimpleRTree<>();
    this.textParagraphsIndex = new SimpleRTree<>();
    this.figuresIndex = new SimpleRTree<>();
    this.shapesIndex = new SimpleRTree<>();
    this.nonTextParagraphsIndex = new SimpleRTree<>();
    this.indexesByFeature = new HashMap<>();
    this.indexesByFeature.put(PdfFeature.characters, charactersIndex);
    this.indexesByFeature.put(PdfFeature.words, wordsIndex);
    this.indexesByFeature.put(PdfFeature.lines, textLinesIndex);
    this.indexesByFeature.put(PdfFeature.paragraphs, textParagraphsIndex);
    this.indexesByFeature.put(PdfFeature.figures, figuresIndex);
    this.indexesByFeature.put(PdfFeature.shapes, shapesIndex);
    this.colors = new HashMap<>();
    this.fonts = new HashMap<>();
  }

  /**
   * Creates a page area from the given parent, indexing all content objects
   * within the given rectangle.
   */
  public PdfXYCutArea(PdfArea parent, Rectangle area) {
    this(parent, parent.getElementsWithin(area));
    this.rawRectangle = area;
  }

  /**
   * Creates a page area from the given parent and the given elements.
   */
  public PdfXYCutArea(PdfArea parent, List<? extends PdfElement> elements) {
    this(parent.getPage());
    addAnyElements(elements);
  }

  @Override
  public void addAnyElements(List<? extends PdfElement> elements) {
    if (elements == null) {
      return;
    }

    for (PdfElement element : elements) {
      addAnyElement(element);
    }
  }

  @Override
  public void addAnyElement(PdfElement element) {
    if (element == null) {
      return;
    }

    addElement(element);

    if (element instanceof PdfTextElement) {
      addTextElement((PdfTextElement) element);
    } else {
      nonTextElementsIndex.insert(element);
    }

    if (element instanceof PdfCharacter) {
      charactersIndex.insert((PdfCharacter) element);
    }
    if (element instanceof PdfXYCutWord) {
      wordsIndex.insert((PdfXYCutWord) element);
    }
    if (element instanceof PdfTextLine) {
      textLinesIndex.insert((PdfTextLine) element);
      this.isTextLineStatisticsOutdated = true;
    }
    if (element instanceof PdfXYCutTextParagraph) {
      textParagraphsIndex.insert((PdfXYCutTextParagraph) element);
    }
    if (element instanceof PdfFigure) {
      figuresIndex.insert((PdfFigure) element);
    }
    if (element instanceof PdfShape) {
      shapesIndex.insert((PdfShape) element);
    }
    if (element instanceof PdfXYCutNonTextParagraph) {
      nonTextParagraphsIndex.insert((PdfXYCutNonTextParagraph) element);
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfElement> getElements() {
    return this.elementsIndex.getIndexEntries();
  }

  @Override
  public List<PdfElement> getElementsWithin(HasRectangle object) {
    return elementsIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfElement> getElementsSurrounding(HasRectangle object) {
    return elementsIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfElement> getElementsOverlapping(HasRectangle object) {
    return elementsIndex.overlappedBy(object.getRectangle());
  }

  protected void addElement(PdfElement element) {
    this.elementsIndex.insert(element);
    registerColor(element.getColor());
    registerFont(element.getFont());
    this.isDimensionStatisticsOutdated = true;
    this.isPositionStatisticsOutdated = true;
  }

  protected void addTextElement(PdfTextElement element) {
    this.textElementsIndex.insert(element);
    this.isTextStatisticsOutdated = true;
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfTextElement> getTextElements() {
    return this.textElementsIndex.getIndexEntries();
  }

  @Override
  public List<PdfTextElement> getTextElementsWithin(HasRectangle object) {
    return this.textElementsIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfTextElement> getTextElementsSurrounding(HasRectangle object) {
    return this.textElementsIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfTextElement> getTextElementsOverlapping(HasRectangle object) {
    return this.textElementsIndex.overlappedBy(object.getRectangle());
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfElement> getNonTextElements() {
    return this.nonTextElementsIndex.getIndexEntries();
  }

  @Override
  public List<PdfElement> getNonTextElementsWithin(HasRectangle object) {
    return this.nonTextElementsIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfElement> getNonTextElementsSurrounding(HasRectangle object) {
    return this.nonTextElementsIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfElement> getNonTextElementsOverlapping(HasRectangle object) {
    return this.nonTextElementsIndex.overlappedBy(object.getRectangle());
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfCharacter> getTextCharacters() {
    return this.charactersIndex.getIndexEntries();
  }

  @Override
  public List<PdfCharacter> getTextCharactersWithin(HasRectangle object) {
    return this.charactersIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfCharacter> getTextCharactersSurrounding(HasRectangle object) {
    return this.charactersIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfCharacter> getTextCharactersOverlapping(HasRectangle object) {
    return this.charactersIndex.overlappedBy(object.getRectangle());
  }

  @Override
  public void setTextCharacters(List<? extends PdfCharacter> characters) {
    this.charactersIndex.clear();
    addTextCharacters(characters);
  }

  @Override
  public void addTextCharacters(List<? extends PdfCharacter> characters) {
    if (characters != null) {
      for (PdfCharacter character : characters) {
        addTextCharacter(character);
      }
    }
  }

  /**
   * Adds the given text character to this page.
   */
  @Override
  public void addTextCharacter(PdfCharacter character) {
    if (character != null) {
      addElement(character);
      addTextElement(character);
      this.charactersIndex.insert(character);
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfWord> getWords() {
    return this.wordsIndex.getIndexEntries();
  }

  @Override
  public List<PdfWord> getWordsWithin(HasRectangle object) {
    return this.wordsIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfWord> getWordsSurrounding(HasRectangle object) {
    return this.wordsIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfWord> getWordsOverlapping(HasRectangle object) {
    return this.wordsIndex.overlappedBy(object.getRectangle());
  }

  @Override
  public void setWords(List<? extends PdfWord> words) {
    this.wordsIndex.clear();
    addWords(words);
  }

  @Override
  public void addWords(List<? extends PdfWord> words) {
    if (words != null) {
      for (PdfWord word : words) {
        addWord(word);
      }
    }
  }

  @Override
  public void addWord(PdfWord word) {
    if (word != null) {
      addElement(word);
      addTextElement(word);
      this.wordsIndex.insert(word);
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfTextLine> getTextLines() {
    return this.textLinesIndex.getIndexEntries();
  }

  @Override
  public List<PdfTextLine> getTextLinesWithin(HasRectangle object) {
    return this.textLinesIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfTextLine> getTextLinesSurrounding(HasRectangle object) {
    return this.textLinesIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfTextLine> getTextLinesOverlapping(HasRectangle object) {
    return this.textLinesIndex.overlappedBy(object.getRectangle());
  }

  @Override
  public void setTextLines(List<? extends PdfTextLine> lines) {
    this.textLinesIndex.clear();
    addTextLines(lines);
  }

  @Override
  public void addTextLines(List<? extends PdfTextLine> lines) {
    if (lines != null) {
      for (PdfTextLine line : lines) {
        addTextLine(line);
      }
    }
  }

  @Override
  public void addTextLine(PdfTextLine line) {
    if (line != null) {
      addElement(line);
      addTextElement(line);
      this.textLinesIndex.insert(line);
      this.isTextLineStatisticsOutdated = true;
      this.isAlignmentOutdated = true;
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfTextParagraph> getParagraphs() {
    return this.textParagraphsIndex.getIndexEntries();
  }

  @Override
  public List<PdfTextParagraph> getParagraphsWithin(HasRectangle object) {
    return this.textParagraphsIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfTextParagraph> getParagraphsSurrounding(
      HasRectangle object) {
    return this.textParagraphsIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfTextParagraph> getParagraphsOverlapping(
      HasRectangle object) {
    return this.textParagraphsIndex.overlappedBy(object.getRectangle());
  }

  @Override
  public void setParagraphs(List<? extends PdfTextParagraph> paragraphs) {
    this.textParagraphsIndex.clear();
    addParagraphs(paragraphs);
  }

  @Override
  public void addParagraphs(List<? extends PdfTextParagraph> paragraphs) {
    if (paragraphs != null) {
      for (PdfTextParagraph paragraph : paragraphs) {
        addParagraph(paragraph);
      }
    }
  }

  @Override
  public void addParagraph(PdfTextParagraph paragraph) {
    if (paragraph != null) {
      addElement(paragraph);
      addTextElement(paragraph);
      this.textParagraphsIndex.insert(paragraph);
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfFigure> getFigures() {
    return this.figuresIndex.getIndexEntries();
  }

  @Override
  public List<PdfFigure> getFiguresWithin(HasRectangle object) {
    return this.figuresIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfFigure> getFiguresSurrounding(HasRectangle object) {
    return this.figuresIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfFigure> getFiguresOverlapping(HasRectangle object) {
    return this.figuresIndex.overlappedBy(object.getRectangle());
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfShape> getShapes() {
    return this.shapesIndex.getIndexEntries();
  }

  @Override
  public List<PdfShape> getShapesWithin(HasRectangle object) {
    return this.shapesIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfShape> getShapesSurrounding(HasRectangle object) {
    return this.shapesIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfShape> getShapesOverlapping(HasRectangle object) {
    return this.shapesIndex.overlappedBy(object.getRectangle());
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfNonTextParagraph> getNonTextParagraphs() {
    return this.nonTextParagraphsIndex.getIndexEntries();
  }

  @Override
  public List<PdfNonTextParagraph> getNonTextParagraphsWithin(
      HasRectangle object) {
    return this.nonTextParagraphsIndex.containedBy(object.getRectangle());
  }

  @Override
  public List<PdfNonTextParagraph> getNonTextParagraphsSurrounding(
      HasRectangle object) {
    return this.nonTextParagraphsIndex.contain(object.getRectangle());
  }

  @Override
  public List<PdfNonTextParagraph> getNonTextParagraphsOverlapping(
      HasRectangle object) {
    return this.nonTextParagraphsIndex.overlappedBy(object.getRectangle());
  }

  @Override
  public void setNonTextParagraphs(
      List<? extends PdfNonTextParagraph> paragraphs) {
    this.nonTextParagraphsIndex.clear();
    addNonTextParagraphs(paragraphs);
  }

  @Override
  public void addNonTextParagraphs(
      List<? extends PdfNonTextParagraph> paragraphs) {
    if (paragraphs != null) {
      for (PdfNonTextParagraph paragraph : paragraphs) {
        addNonTextParagraph(paragraph);
      }
    }
  }

  @Override
  public void addNonTextParagraph(PdfNonTextParagraph paragraph) {
    if (paragraph != null) {
      addElement(paragraph);
      this.nonTextElementsIndex.insert(paragraph);
      this.nonTextParagraphsIndex.insert(paragraph);
    }
  }

  protected void registerColor(PdfColor color) {
    if (color != null) {
      if (!this.colors.containsKey(color.getId())) {
        this.colors.put(color.getId(), color);
      }
    }
  }

  protected void registerFont(PdfFont font) {
    if (font != null) {
      if (!this.fonts.containsKey(font.getId())) {
        this.fonts.put(font.getId(), font);
      }
    }
  }

  @Override
  public PdfDocument getPdfDocument() {
    return this.page.getPdfDocument();
  }

  @Override
  public PdfPage getPage() {
    return this.page;
  }

  @Override
  public Rectangle getRectangle() {
    return this.elementsIndex.getRoot().getRectangle();
  }

  // ___________________________________________________________________________
  // Statistics methods.

  /**
   * Returns the dimension statistics.
   */
  public DimensionStatistics getDimensionStatistics() {
    if (needsDimensionStatisticsUpdate()) {
      this.dimStatistics = computeDimensionStatistics();
      this.isDimensionStatisticsOutdated = false;
    }
    return this.dimStatistics;
  }

  /**
   * Computes the dimension statistics.
   */
  protected DimensionStatistics computeDimensionStatistics() {
    return DimensionStatistician.compute(getElements());
  }

  /**
   * Returns true, if the dimension statistics needs an update.
   */
  protected boolean needsDimensionStatisticsUpdate() {
    return this.dimStatistics == null || isDimensionStatisticsOutdated;
  }

  // ___________________________________________________________________________

  /**
   * Returns the text statistics.
   */
  public TextStatistics getTextStatistics() {
    if (needsTextStatisticsUpdate()) {
      this.textStatistics = computeTextStatistics();
      this.isTextStatisticsOutdated = false;
    }
    return this.textStatistics;
  }

  /**
   * Computes the text statistics.
   */
  public TextStatistics computeTextStatistics() {
    return TextStatistician.compute(getTextCharacters());
  }

  /**
   * Returns true, if the text line statistics needs an update.
   */
  protected boolean needsTextStatisticsUpdate() {
    return this.textStatistics == null || isTextStatisticsOutdated;
  }

  // ___________________________________________________________________________

  /**
   * Returns the text line statistics.
   */
  public TextLineStatistics getTextLineStatistics() {
    if (needsTextLineStatisticsUpdate()) {
      this.textLineStatistics = computeTextLineStatistics();
      this.isTextLineStatisticsOutdated = false;
    }
    return textLineStatistics;
  }

  /**
   * Computes the text line statistics.
   */
  public TextLineStatistics computeTextLineStatistics() {
    return TextLineStatistician.compute(getTextLines());
  }

  /**
   * Returns true, if the text line statistics needs an update.
   */
  protected boolean needsTextLineStatisticsUpdate() {
    return this.textLineStatistics == null || isTextLineStatisticsOutdated;
  }

  // ___________________________________________________________________________

  /**
   * Returns the position statistics.
   */
  public PositionStatistics getPositionStatistics() {
    if (needsPositionStatisticsUpdate()) {
      this.positionStatistics = computePositionStatistics();
      this.isPositionStatisticsOutdated = false;
    }
    return positionStatistics;
  }

  /**
   * Computes the position statistics.
   */
  public PositionStatistics computePositionStatistics() {
    return PositionStatistician.compute(getElements());
  }

  /**
   * Returns true, if the position statistics needs an update.
   */
  protected boolean needsPositionStatisticsUpdate() {
    return this.positionStatistics == null || isPositionStatisticsOutdated;
  }

  // ___________________________________________________________________________

  @Override
  public List<? extends PdfElement> getElementsByFeature(PdfFeature feature) {
    RTree<? extends PdfElement> index = indexesByFeature.get(feature);
    if (index != null) {
      return index.getIndexEntries();
    }
    return new ArrayList<>();
  }

  @Override
  public PdfFont getFont() {
    return getTextStatistics().getMostCommonFont();
  }

  @Override
  public PdfColor getColor() {
    // TODO: Consider also the color of figures.
    return getTextStatistics().getMostCommonFontColor();
  }

  @Override
  public float getFontsize() {
    return getTextStatistics().getMostCommonFontsize();
  }

  @Override
  public Collection<PdfFont> getFonts() {
    return this.fonts.values();
  }

  @Override
  public Collection<PdfColor> getColors() {
    return colors.values();
  }

  @Override
  public boolean ignore() {
    return ignore;
  }

  @Override
  public void setIgnore(boolean ignore) {
    this.ignore = ignore;
  }

  public Rectangle getRawRectangle() {
    return this.rawRectangle;
  }

  public PdfTextAlignment getTextLineAlignment() {
    if (alignment == null || isAlignmentOutdated) {
      this.alignment = computeTextLineAlignment();
    }
    return this.alignment;
  }

  public PdfTextAlignment computeTextLineAlignment() {
    List<PdfTextLine> lines = getTextLines();

    if (lines == null) {
      return null;
    }

    Rectangle rect = getRectangle();
    float tolerance = 0.5f * getDimensionStatistics().getAverageWidth();

    ObjectCounter<PdfTextAlignment> counter = new ObjectCounter<>();

    for (PdfTextLine line : lines) {
      Rectangle lineRect = line.getRectangle();

      if (rect != null && line.getRectangle() != null) {
        float leftMargin = lineRect.getMinX() - rect.getMinX();
        float rightMargin = rect.getMaxX() - lineRect.getMaxX();

        if (isEqual(rightMargin, leftMargin, tolerance)) {
          if (leftMargin > tolerance) {
            counter.add(PdfTextAlignment.CENTERED);
          } else {
            counter.add(PdfTextAlignment.JUSTIFIED);
          }
        } else if (isLarger(leftMargin, rightMargin, tolerance)) {
          counter.add(PdfTextAlignment.RIGHT);
        } else {
          counter.add(PdfTextAlignment.LEFT);
        }
      }
    }
    Set<PdfTextAlignment> alignments = counter.getAllMostFrequentObjects();
    if (alignments.contains(PdfTextAlignment.CENTERED)) {
      return PdfTextAlignment.CENTERED;
    } else if (alignments.contains(PdfTextAlignment.JUSTIFIED)) {
      return PdfTextAlignment.JUSTIFIED;
    }
    return counter.getMostFrequentObject();
  }

  @Override
  public String getMarkup() {
    float fontsize = MathUtils.round(getFontsize(), 0);
    
    PdfFont font = getFont();
    if (font == null) {
      return null;
    }

    String fontname = font.getFullName();
    if (fontname == null) {
      return null;
    }

    return fontname + "-" + fontsize;
  }

  @Override
  public PdfArea getBlock() {
    return block;
  }

  @Override
  public void setBlock(PdfArea block) {
    this.block = block;
  }
  
  List<Rectangle> rects;
  
  @Override
  public void setRects(List<Rectangle> subRects) {
    this.rects = subRects;
  }

  @Override
  public List<Rectangle> getRects() {
    return this.rects;
  }

  @Override
  public Line getColumnXRange() {
    return this.columnXRange;
  }

  @Override
  public void setColumnXRange(Line range) {
    this.columnXRange = range;
  }
}
