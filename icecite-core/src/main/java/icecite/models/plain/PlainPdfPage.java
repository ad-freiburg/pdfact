package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfShape;

/**
 * A plain implementation of {@link PdfPage}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfPage implements PdfPage {
  /**
   * The shapes in this page.
   */
  protected List<PdfCharacter> characters;

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
   * Creates a new PDF page.
   */
  @AssistedInject
  public PlainPdfPage() {
    this.characters = new ArrayList<>();
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
  }

  /**
   * Creates a new PDF page.
   * 
   * @param pageNumber
   *        The number of this page in the PDF document.
   */
  @AssistedInject
  public PlainPdfPage(@Assisted int pageNumber) {
    this();
    this.pageNumber = pageNumber;
  }

  // ==========================================================================

  @Override
  public List<PdfCharacter> getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(List<PdfCharacter> characters) {
    this.characters = characters;
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
  public void addShape(PdfShape shape) {
    this.shapes.add(shape);
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
