package parser.pdfbox.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.PdfCharacter;
import model.PdfColor;
import model.PdfElement;
import model.PdfFeature;
import model.PdfFigure;
import model.PdfFont;
import model.PdfPage;
import model.PdfParagraph;
import model.PdfShape;
import model.PdfTextLine;
import model.PdfWord;
import model.TextLineStatistics;
import statistics.TextLineStatistician;

/**
 * Concrete implementation of a PdfTextPage using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxPage extends PdfBoxTextArea implements PdfPage {
  /**
   * The page in fashion of PdfBox.
   */
  protected PDPage pdPage;

  /** 
   * The mediaBox of this page. 
   */
  protected Rectangle mediaBox;
  
  /** 
   * The cropBox of this page. 
   */
  protected Rectangle cropBox;
  
  /** 
   * The content box (the bounding box of all content in page). 
   */
  protected Rectangle artBox;
  
  /** 
   * The trim box (the bounding box of all content but headers/footers.
   */
  protected Rectangle trimBox;

  /**
   * The page number.
   */
  protected int pageNumber;

  /** 
   * The x-offset between cropBox and mediaBox. 
   */
  protected float cropXOffset;
  
  /** 
   * The y-offset between cropBox and mediaBox.
   */
  protected float cropYOffset;
  
  /**
   * The elements of this page by feature.
   */
  protected Map<PdfFeature, List<? extends PdfElement>> elementsByFeature;
    
  /**
   * The list of text characters.
   */
  protected List<PdfCharacter> textCharacters;

  /**
   * The list of words.
   */
  protected List<PdfWord> words;

  /**
   * The list of lines.
   */
  protected List<PdfTextLine> textLines;

  /**
   * The list of paragraphs.
   */
  protected List<PdfParagraph> paragraphs;

  /**
   * The list of figures.
   */
  protected List<PdfFigure> figures;

  /**
   * The list of shapes.
   */
  protected List<PdfShape> shapes;

  /**
   * The text document, in which this page is located.
   */
  protected PdfBoxDocument pdfDocument;

  /**
   * The text line statistics.
   */
  protected TextLineStatistics textLineStatistics;
  
  /**
   * Flag to indicate if the text line statistics is outdated.
   */
  protected boolean isTextLineStatisticsOutdated;
  
  /**
   * All fonts in this page.
   */
  protected Map<String, PdfFont> fonts;
  
  /**
   * All colors in this page.
   */
  protected Map<String, PdfColor> colors;
  
  /**
   * The default constructor.
   */
  public PdfBoxPage(PDPage page, int pageNumber) {
    super(null); // We cannot pass this here.
    this.pdPage = page;
    this.pageNumber = pageNumber;
    
    this.textCharacters = new ArrayList<>();
    this.words = new ArrayList<>();
    this.textLines = new ArrayList<>();
    this.paragraphs = new ArrayList<>();
    this.figures = new ArrayList<>();
    this.shapes = new ArrayList<>();
    this.elementsByFeature = new HashMap<>();
    this.elementsByFeature.put(PdfFeature.characters, textCharacters);
    this.elementsByFeature.put(PdfFeature.words, words);
    this.elementsByFeature.put(PdfFeature.lines, textLines);
    this.elementsByFeature.put(PdfFeature.paragraphs, paragraphs);
    this.elementsByFeature.put(PdfFeature.figures, figures);
    this.elementsByFeature.put(PdfFeature.shapes, shapes);
    this.colors = new HashMap<>();
    this.fonts = new HashMap<>();
    
    // The CropBox is usually the visible page area within a larger MediaBox.
    // It is often used to hide things like printers crop marks and is generally
    // the visible part you see in a PDF viewer.
    // But it is possible, that the cropBox is larger than the mediaBox. In that
    // cases, take the mediaBox as cropBox.
    // See https://blog.idrsolutions.com/2012/02/...
    // what-happens-if-the-cropbox-is-smaller-than-the-mediabox/
    PDRectangle mediaBox = pdPage.getMediaBox();
    PDRectangle cropBox = pdPage.getCropBox();

    this.mediaBox = new SimpleRectangle(
        mediaBox.getLowerLeftX(), mediaBox.getLowerLeftY(),
        mediaBox.getUpperRightX(), mediaBox.getUpperRightY());

    this.cropBox = new SimpleRectangle(
        cropBox.getLowerLeftX(), cropBox.getLowerLeftY(),
        cropBox.getUpperRightX(), cropBox.getUpperRightY());

    this.cropXOffset = this.cropBox.getMinX() - this.mediaBox.getMinX();
    this.cropYOffset = this.cropBox.getMinY() - this.mediaBox.getMinX();

    // Check, if the cropBox is larger than the mediaBox.
    if (this.cropBox.getArea() > this.mediaBox.getArea()) {
      // Swap cropBox and mediaBox.
      Rectangle tmp = this.cropBox;
      this.cropBox = this.mediaBox;
      this.mediaBox = tmp;
    }

    this.artBox = this.cropBox;
    this.trimBox = this.cropBox;
  }

  @Override
  public Rectangle getMediaBox() {
    return this.mediaBox;
  }

  @Override
  public Rectangle getCropBox() {
    return this.cropBox;
  }

  @Override
  public Rectangle getArtBox() {
    return this.artBox;
  }

  @Override
  public float getCropXOffset() {
    return cropXOffset;
  }

  @Override
  public float getCropYOffset() {
    return cropYOffset;
  }

  @Override
  public Rectangle getTrimBox() {
    return this.trimBox;
  }

  @Override
  // Override getPage(), because we have passed null to the super constructor.
  public PdfPage getPage() {
    return this;
  }

  @Override
  public int getPageNumber() {
    return pageNumber;
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfCharacter> getTextCharacters() {
    return this.textCharacters;
  }

  @Override
  public void setTextCharacters(List<? extends PdfCharacter> characters) {
    this.textCharacters.clear();
    if (characters != null) {
      for (PdfCharacter character : characters) {
        addTextCharacter(character);
      }
    }
  }

  /**
   * Adds the given text character to this page.
   */
  public void addTextCharacter(PdfCharacter character) {
    if (character != null) {
      this.textCharacters.add(character);
      this.put(character);
      this.fonts.put(character.getFont().getId(), character.getFont());
      this.colors.put(character.getColor().getId(), character.getColor());
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfWord> getWords() {
    return this.words;
  }

  @Override
  public void setWords(List<? extends PdfWord> words) {
    this.words.clear();
    if (words != null) {
      for (PdfWord word : words) {
        this.words.add(word);
      }
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfTextLine> getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(List<? extends PdfTextLine> lines) {    
    this.textLines.clear();
    if (lines != null) {
      for (PdfTextLine line : lines) {
        this.textLines.add(line);
      }
    }
    this.isTextLineStatisticsOutdated = true;
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfParagraph> getParagraphs() {
    return this.paragraphs;
  }

  @Override
  public void setParagraphs(List<? extends PdfParagraph> paragraphs) {
    this.paragraphs.clear();
    if (paragraphs != null) {
      for (PdfParagraph paragraph : paragraphs) {
        this.paragraphs.add(paragraph);
      }
    }
  }

  // ___________________________________________________________________________

  @Override
  public List<PdfFigure> getFigures() {
    return this.figures;
  }

  /**
   * Adds the given text character to this page.
   */
  public void addFigure(PdfBoxFigure figure) {
    if (figure != null) {
      this.figures.add(figure);
      put(figure);
    }
  }

  /**
   * Sets the list of figures of this page to the given one.
   */
  public void setFigures(List<PdfBoxFigure> figures) {
    this.figures.clear();
    if (figures != null) {
      for (PdfBoxFigure figure : figures) {
        addFigure(figure);
      }
    }
  }

  @Override
  public List<PdfShape> getShapes() {
    return this.shapes;
  }

  /**
   * Adds the given shape to this page.
   */
  public void addShape(PdfBoxShape shape) {
    if (shape != null) {
      this.shapes.add(shape);
      put(shape);
    }
  }

  /**
   * Sets the list of shapes of this page to the given one.
   */
  public void setShapes(List<PdfBoxShape> shapes) {
    this.shapes.clear();
    if (shapes != null) {
      for (PdfBoxShape shape : shapes) {
        addShape(shape);
      }
    }
  }

  /**
   * Returns the page in fashion of PdfBox.
   */
  protected PDPage getPdPage() {
    return this.pdPage;
  }

  @Override
  public PdfBoxDocument getPdfDocument() {
    return this.pdfDocument;
  }

  /**
   * Sets the text document, in which this text document is located.
   */
  public void setPdfDocument(PdfBoxDocument document) {
    this.pdfDocument = document;
  }

  /**
   * Returns the reosurces.
   */
  public PDResources getResources() {
    return pdPage.getResources();
  }
  
  /**
   * Returns the text line statistics.
   */
  public TextLineStatistics getTextLineStatistics() {
    if (textLineStatistics == null || isTextLineStatisticsOutdated) {
      computeTextLineStatistics();
    }
    return textLineStatistics;
  }
  
  /**
   * Returns the text line statistics.
   */
  public void computeTextLineStatistics() {    
    textLineStatistics = TextLineStatistician.compute(getTextLines());
    isTextLineStatisticsOutdated = false;
  }

  @Override
  public List<? extends PdfElement> getElementsByFeature(PdfFeature feature) {
    return elementsByFeature.get(feature);
  }

  @Override
  public Collection<PdfFont> getFonts() {
    return this.fonts.values();
  }

  @Override
  public Collection<PdfColor> getColors() {
    return colors.values();
  }
}
