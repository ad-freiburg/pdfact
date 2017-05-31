package icecite.models.plain;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;

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
   * All characters of this PDF document.
   */
  protected PdfCharacterList characters;

  /**
   * All figures of this PDF document.
   */
  protected List<PdfFigure> figures;

  /**
   * All shapes of this PDF document.
   */
  protected List<PdfShape> shapes;

  /**
   * All text lines of this PDF document.
   */
  protected PdfTextLineList textLines;

  /**
   * All paragraphs of this PDF document.
   */
  protected List<PdfParagraph> paragraphs;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF document.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link PdfCharacterList}.
   * @param textLineListFactory
   *        The factory to create instances of {@link PdfTextLineList}.
   */
  protected PlainPdfDocument(PdfCharacterListFactory characterListFactory,
      PdfTextLineListFactory textLineListFactory) {
    this.characters = characterListFactory.create();
    this.textLines = textLineListFactory.create();
    this.pages = new ArrayList<>();
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
    this.paragraphs = new ArrayList<>();
  }

  /**
   * Creates a new PDF document.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link PdfCharacterList}.
   * @param textLineListFactory
   *        The factory to create instances of {@link PdfTextLineList}.
   * @param pdf
   *        The PDF file given as a File object.
   */
  @AssistedInject
  public PlainPdfDocument(PdfCharacterListFactory characterListFactory,
      PdfTextLineListFactory textLineListFactory, @Assisted File pdf) {
    this(characterListFactory, textLineListFactory);
    this.path = pdf.toPath();
  }

  /**
   * Creates a new PDF document.
   * 
   * @param characterListFactory
   *        The factory to create instances of {@link PdfCharacterList}.
   * @param textLineListFactory
   *        The factory to create instances of {@link PdfTextLineList}.
   * @param pdf
   *        The PDF file given as a File object.
   */
  @AssistedInject
  public PlainPdfDocument(PdfCharacterListFactory characterListFactory,
      PdfTextLineListFactory textLineListFactory, @Assisted Path pdf) {
    this(characterListFactory, textLineListFactory);
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

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfDocument(" + this.path + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfDocument) {
      PdfDocument otherDocument = (PdfDocument) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPath(), otherDocument.getPath());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPath());
    return builder.hashCode();
  }
}
