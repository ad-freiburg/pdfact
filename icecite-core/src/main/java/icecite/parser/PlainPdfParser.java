package icecite.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
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
  protected PdfStreamParserFactory streamParserFactory;

  /**
   * The factory to create instances of PdfDocument.
   */
  protected PdfDocumentFactory documentFactory;

  /**
   * The factory to create instances of PdfPage.
   */
  protected PdfPageFactory pageFactory;

  /**
   * The factory to create instances of PdfCharacterList.
   */
  protected PdfCharacterListFactory characterListFactory;

  /**
   * The PDF document.
   */
  protected PdfDocument pdfDocument;

  /**
   * The current page.
   */
  protected PdfPage currentPage;
  
  /**
   * All characters of the current PDF document.
   */
  protected PdfCharacterList charactersOfPdfDocument;

  /**
   * All figures of the current PDF document.
   */
  protected Set<PdfFigure> figuresOfPdfDocument;

  /**
   * All shapes of the current PDF document.
   */
  protected Set<PdfShape> shapesOfPdfDocument;

  /**
   * The characters of the current PDF page.
   */
  protected PdfCharacterList charactersOfPdfPage;

  /**
   * The figures of the current PDF page.
   */
  protected Set<PdfFigure> figuresOfPdfPage;

  /**
   * The shapes of the current PDF page.
   */
  protected Set<PdfShape> shapesOfPdfPage;

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
   * @param characterListFactory
   *        The factory to create instances of PdfCharacterList.
   */
  @AssistedInject
  public PlainPdfParser(PdfStreamParserFactory pdfStreamParserFactory,
      PdfDocumentFactory pdfDocFactory, PdfPageFactory pdfPageFactory,
      PdfCharacterListFactory characterListFactory) {
    this(pdfStreamParserFactory, pdfDocFactory, pdfPageFactory,
        characterListFactory, true, true);
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
   * @param characterListFactory
   *        The factory to create instances of PdfCharacterList.
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
      PdfCharacterListFactory characterListFactory,
      @Assisted("resolveLigatures") boolean resolveLigatures,
      @Assisted("resolveDiacritics") boolean resolveDiacritics) {
    this.streamParserFactory = pdfStreamParserFactory;
    this.documentFactory = pdfDocumentFactory;
    this.pageFactory = pdfPageFactory;
    this.characterListFactory = characterListFactory;
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
    PdfStreamParser streamParser = this.streamParserFactory.create(this);

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
    this.pdfDocument = this.documentFactory.create(pdf);

    // Initialize the list for the elements of PDF document.
    this.charactersOfPdfDocument = this.characterListFactory.create();
    this.figuresOfPdfDocument = new HashSet<>();
    this.shapesOfPdfDocument = new HashSet<>();
  }

  @Override
  public void handlePdfFileEnd(File pdf) {
    // Set the elements of PDF document.
    this.pdfDocument.setCharacters(this.charactersOfPdfDocument);
    this.pdfDocument.setFigures(this.figuresOfPdfDocument);
    this.pdfDocument.setShapes(this.shapesOfPdfDocument);
  }

  @Override
  public void handlePdfPageStart(int pageNum) {
    // Create a new PDF page.
    this.currentPage = this.pageFactory.create(pageNum);
    
    // Initialize the list for the elements of the page.
    this.charactersOfPdfPage = this.characterListFactory.create();
    this.figuresOfPdfPage = new HashSet<>();
    this.shapesOfPdfPage = new HashSet<>();
  }

  @Override
  public void handlePdfPageEnd(int pageNum) {
    // Set the characters of the page.
    this.currentPage.setCharacters(this.charactersOfPdfPage);
    // Set the figures of the page.
    this.currentPage.setFigures(this.figuresOfPdfPage);
    // Set the shapes of the page.
    this.currentPage.setShapes(this.shapesOfPdfPage);

    // Add the page to the PDF document.
    this.pdfDocument.addPage(this.currentPage);
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
      character.setExtractionOrderNumber(this.charactersOfPdfPage.size());
      character.setPage(this.currentPage);
      
      this.charactersOfPdfPage.add(character);
      this.charactersOfPdfDocument.add(character);
    }

    this.prevPrevCharacter = this.prevCharacter;
    this.prevCharacter = character;
  }

  @Override
  public void handlePdfFigure(PdfFigure figure) {
    if (!PdfFigureFilter.filterPdfFigure(figure)) {
      figure.setPage(this.currentPage);
      
      this.figuresOfPdfPage.add(figure);
      this.figuresOfPdfDocument.add(figure);
    }
  }

  @Override
  public void handlePdfShape(PdfShape shape) {
    if (!PdfShapeFilter.filterPdfShape(shape)) {
      shape.setPage(this.currentPage);
      
      this.shapesOfPdfPage.add(shape);
      this.shapesOfPdfDocument.add(shape);
    }
  }
}
