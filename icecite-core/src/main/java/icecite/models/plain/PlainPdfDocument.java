package icecite.models.plain;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfParagraph;
import icecite.models.PdfShape;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;

/**
 * A plain implementation of {@link PdfDocument}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfDocument implements PdfDocument {
  /**
   * The file on which this PDF document is based on.
   */
  protected Path path;

  /**
   * The pages of this PDF document.
   */
  protected List<PdfPage> pages;

  /**
   * The characters of this PDF document. Needed to get document-wide
   * statistics about characters.
   */
  protected PdfCharacterList characters;

  /**
   * The figures of this PDF document. Needed to get document-wide statistics
   * about figures.
   */
  protected List<PdfFigure> figures;

  /**
   * The shapes of this PDF document. Needed to get document-wide statistics
   * about shapes.
   */
  protected List<PdfShape> shapes;

  /**
   * The text blocks in this PDF document.
   */
  protected List<PdfTextBlock> textBlocks;

  /**
   * The text lines in this PDF document.
   */
  protected List<PdfTextLine> textLines;

  /**
   * The paragraphs in this PDF document.
   */
  protected List<PdfParagraph> paragraphs;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF document.
   * 
   * @param characterListFactory
   *        The factory to create instances of PdfCharacterList.
   */
  @AssistedInject
  public PlainPdfDocument(PdfCharacterListFactory characterListFactory) {
    this.pages = new ArrayList<PdfPage>();
    this.characters = characterListFactory.create();
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
    this.textBlocks = new ArrayList<>();
    this.textLines = new ArrayList<>();
    this.paragraphs = new ArrayList<>();
  }

  /**
   * Creates a new PDF document.
   * 
   * @param characterListFactory
   *        The factory to create instances of PdfCharacterList.
   * @param pdf
   *        The PDF file given as a File object
   */
  @AssistedInject
  public PlainPdfDocument(PdfCharacterListFactory characterListFactory,
      @Assisted File pdf) {
    this(characterListFactory);
    this.path = pdf.toPath();
  }

  /**
   * Creates a new PDF document.
   * 
   * @param characterListFactory
   *        The factory to create instances of PdfCharacterList.
   * @param pdf
   *        The PDF file given as a Path object
   */
  @AssistedInject
  public PlainPdfDocument(PdfCharacterListFactory characterListFactory,
      @Assisted Path pdf) {
    this(characterListFactory);
    this.path = pdf;
  }

  // ==========================================================================

  @Override
  public List<PdfPage> getPages() {
    return this.pages;
  }

  @Override
  public void setPages(List<PdfPage> pages) {
    this.pages = pages;
  }

  @Override
  public void addPage(PdfPage page) {
    this.pages.add(page);
  }

  // ==========================================================================

  @Override
  public File getFile() {
    return this.path != null ? this.path.toFile() : null;
  }

  @Override
  public void setFile(File file) {
    this.path = file != null ? file.toPath() : null;
  }

  @Override
  public Path getPath() {
    return this.path;
  }

  @Override
  public void setPath(Path path) {
    this.path = path;
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
  public List<PdfTextLine> getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(List<PdfTextLine> lines) {
    this.textLines = lines;
  }

  @Override
  public void addTextLines(List<PdfTextLine> lines) {
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
  public List<PdfParagraph> getParagraphs() {
    return this.paragraphs;
  }

  @Override
  public void setParagraphs(List<PdfParagraph> paragraphs) {
    this.paragraphs = paragraphs;
  }

  @Override
  public void addParagraphs(List<PdfParagraph> paragraphs) {
    for (PdfParagraph paragraph : paragraphs) {
      addParagraph(paragraph);
    }
  }

  @Override
  public void addParagraph(PdfParagraph paragraph) {
    this.paragraphs.add(paragraph);
  }
}
