package icecite.models.plain;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfColor;
import icecite.models.PdfDocument;
import icecite.models.PdfFigure;
import icecite.models.PdfFont;
import icecite.models.PdfPage;
import icecite.models.PdfShape;

/**
 * A plain implementation of {@link PdfDocument}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfDocument implements PdfDocument {
  /**
   * The pages of this PDF document.
   */
  protected List<PdfPage> pages;

  /**
   * The file on which this PDF document is based on.
   */
  protected Path path;

  /**
   * The characters of this PDF document.
   */
  protected PdfCharacterSet characters;
  
  /**
   * The figures of this PDF document.
   */
  protected Set<PdfFigure> figures;
  
  /**
   * The shapes of this PDF document.
   */
  protected Set<PdfShape> shapes;
  
  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF document.
   */
  @AssistedInject
  public PlainPdfDocument() {
    this.pages = new ArrayList<PdfPage>();
  }

  /**
   * Creates a new PDF document.
   * 
   * @param pdf
   *        The PDF file given as a File object
   */
  @AssistedInject
  public PlainPdfDocument(@Assisted File pdf) {
    this();
    this.path = pdf.toPath();
  }

  /**
   * Creates a new PDF document.
   * 
   * @param pdf
   *        The PDF file given as a Path object
   */
  @AssistedInject
  public PlainPdfDocument(@Assisted Path pdf) {
    this();
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
