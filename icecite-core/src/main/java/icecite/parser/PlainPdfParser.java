package icecite.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterSet;
import icecite.models.PdfCharacterSet.PdfCharacterSetFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfDocument.PdfDocumentFactory;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfPage.PdfPageFactory;
import icecite.models.PdfShape;
import icecite.parser.filters.PdfCharacterFilter;
import icecite.parser.filters.PdfFigureFilter;
import icecite.parser.filters.PdfShapeFilter;
import icecite.parser.stream.HasPdfStreamParserHandlers;
import icecite.parser.stream.PdfStreamParser;
import icecite.parser.stream.PdfStreamParser.PdfStreamParserFactory;
import icecite.parser.translators.DiacriticsTranslator;
import icecite.parser.translators.LigaturesTranslator;

/**
 * A plain implementation of {@link PdfParser}.
 *
 * @author Claudius Korzen
 */
public class PlainPdfParser implements PdfParser, HasPdfStreamParserHandlers {
  /**
   * The instance of PdfStreamParser.
   */
  protected PdfStreamParserFactory pdfStreamParserFactory;

  /**
   * The factory to create instances of PdfDocument.
   */
  protected PdfDocumentFactory pdfDocumentFactory;

  /**
   * The factory to create instances of PdfPage.
   */
  protected PdfPageFactory pdfPageFactory;

  /**
   * The factory to create instances of PdfCharacterSet.
   */
  protected PdfCharacterSetFactory pdfCharacterSetFactory;
  
  /**
   * The PDF document.
   */
  protected PdfDocument pdfDocument;

  /**
   * The characters of the current PDF page.
   */
  protected PdfCharacterSet currentPdfPageCharacters;
  
  /**
   * The figures of the current PDF page.
   */
  protected List<PdfFigure> currentPdfPageFigures;
  
  /**
   * The shapes of the current PDF page.
   */
  protected List<PdfShape> currentPdfPageShapes;
  
  /**
   * The predecessor of the current character (needed to resolve diacritics).
   */
  protected PdfCharacter prevCharacter;

  /**
   * The predecessor of prevCharacter (needed to resolve diacritics).
   */
  protected PdfCharacter prevPrevCharacter;

  /**
   * The boolean flag to indicate whether ligatures should be resolved or not.
   */
  protected boolean resolveLigatures;

  /**
   * The boolean flag to indicate whether characters with diacritics should be
   * resolved or not.
   */
  protected boolean resolveDiacritics;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF parser.
   * 
   * @param pdfStreamParserFactory
   *        The PDF stream parser.
   * @param pdfDocFactory
   *        The factory to create instances of PdfDocument.
   * @param pdfPageFactory
   *        The factory to create instances of PdfPage.
   * @param characterSetFactory 
   *        The factory to create instances of PdfCharacterSet.
   */
  @AssistedInject
  public PlainPdfParser(PdfStreamParserFactory pdfStreamParserFactory,
      PdfDocumentFactory pdfDocFactory, PdfPageFactory pdfPageFactory,
      PdfCharacterSetFactory characterSetFactory) {
    this(pdfStreamParserFactory, pdfDocFactory, pdfPageFactory,
        characterSetFactory, true, true);
  }

  /**
   * Creates a new PDF parser.
   * 
   * @param pdfStreamParserFactory
   *        The PDF stream parser.
   * @param pdfDocumentFactory
   *        The factory to create instances of PdfDocument.
   * @param pdfPageFactory
   *        The factory to create instances of PdfPage.
   * @param characterSetFactory 
   *        The factory to create instances of PdfCharacterSet.
   * @param resolveLigatures
   *        The boolean flag to indicate whether ligatures should be resolved
   *        or not.
   * @param resolveDiacritics
   *        The boolean flag to indicate whether characters with diacritics
   *        should be resolved or not.
   */
  @AssistedInject
  public PlainPdfParser(PdfStreamParserFactory pdfStreamParserFactory,
      PdfDocumentFactory pdfDocumentFactory, PdfPageFactory pdfPageFactory,
      PdfCharacterSetFactory characterSetFactory,
      @Assisted("resolveLigatures") boolean resolveLigatures,
      @Assisted("resolveDiacritics") boolean resolveDiacritics) {
    this.pdfStreamParserFactory = pdfStreamParserFactory;
    this.pdfDocumentFactory = pdfDocumentFactory;
    this.pdfPageFactory = pdfPageFactory;
    this.pdfCharacterSetFactory = characterSetFactory;
    this.resolveLigatures = resolveLigatures;
    this.resolveDiacritics = resolveDiacritics;
  }

  // ==========================================================================
  // Parse methods.

  @Override
  public PdfDocument parsePdf(Path pdf) throws IOException {
    return parsePdf(pdf != null ? pdf.toFile() : null);
  }

  @Override
  public PdfDocument parsePdf(File pdf) throws IOException {
    PdfStreamParser streamParser = this.pdfStreamParserFactory.create(this);

    // Parse the PDF file.
    streamParser.parsePdf(pdf);

    // Return the PdfDocument generated by the handler methods below.
    return this.pdfDocument;
  }

  // ==========================================================================
  // Getter methods.

  /**
   * Returns true, if ligatures should be resolved; false otherwise.
   * 
   * @return true, if ligatures should be resolved; false otherwise.
   */
  public boolean isResolveLigatures() {
    return this.resolveLigatures;
  }

  /**
   * Returns true, if diacritics should be resolved; false otherwise.
   * 
   * @return true, if diacritics should be resolved; false otherwise.
   */
  public boolean isResolveDiacritics() {
    return this.resolveDiacritics;
  }

  // ==========================================================================
  // Handler methods.

  @Override
  public void handlePdfFileStart(File pdf) {
    // Create a new PDF document.
    this.pdfDocument = this.pdfDocumentFactory.create(pdf);
  }

  @Override
  public void handlePdfFileEnd(File pdf) {
    // Nothing to do so far.
  }

  @Override
  public void handlePdfPageStart(int pageNum) {
    this.currentPdfPageCharacters = this.pdfCharacterSetFactory.create();
    this.currentPdfPageFigures = new ArrayList<PdfFigure>();
    this.currentPdfPageShapes = new ArrayList<PdfShape>();
  }

  @Override
  public void handlePdfPageEnd(int pageNum) {
    // Create a new PDF page.
    PdfPage page = this.pdfPageFactory.create(pageNum);
    
    // Set the characters of the page.
    page.setCharacters(this.currentPdfPageCharacters);
    // Set the figures of the page.
    page.setFigures(this.currentPdfPageFigures);
    // Set the shapes of the page.
    page.setShapes(this.currentPdfPageShapes);
    
    // Add the page to the PDF document.
    this.pdfDocument.addPage(page);
  }

  @Override
  public void handlePdfCharacter(PdfCharacter character) {
    // Check if the character is a ligature. If so, resolve it.
    if (isResolveLigatures()) {
      LigaturesTranslator.resolveLigature(character);
    }

    // Check if the character is a diacritic. If so, resolve it.
    // In most cases, diacritic characters are represented in its decomposed
    // form. For example, "è" may be represented as the two characters "'e" or
    // "e'". To merge such characters the base character must be followed by
    // the diacritic: "e'" implicitly. To maintain this order, decide to which
    // base character a diacritic belongs and merge them together.

    // To decide to which character the diacritic belongs, we have to wait for
    // the character after the diacritic, so we check if the previous character
    // is a diacritic and compare the horizontal overlap between (a) the
    // diacritic and the character "in front" of the character
    // (prePreviousCharacter) and (b) the diacritic and the character "behind"
    // the character (the current character).
    if (isResolveDiacritics()) {
      DiacriticsTranslator.resolveDiacritic(this.prevCharacter,
          this.prevPrevCharacter, character);
    }

    if (!PdfCharacterFilter.filterPdfCharacter(character)) {
      this.currentPdfPageCharacters.add(character);
    }

    this.prevPrevCharacter = this.prevCharacter;
    this.prevCharacter = character;
  }

  @Override
  public void handlePdfFigure(PdfFigure figure) {
    if (!PdfFigureFilter.filterPdfFigure(figure)) {
      this.currentPdfPageFigures.add(figure);
    }
  }

  @Override
  public void handlePdfShape(PdfShape shape) {
    if (!PdfShapeFilter.filterPdfShape(shape)) {
      this.currentPdfPageShapes.add(shape);
    }
  }
}
