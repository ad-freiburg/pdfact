package icecite.parse.stream;

import java.io.File;

import icecite.models.PdfCharacter;
import icecite.models.PdfFigure;
import icecite.models.PdfPage;
import icecite.models.PdfShape;

/**
 * An interface that defines callback methods to be called by PdfStreamParser
 * on parsing the streams of a PDF file.
 * 
 * @author Claudius Korzen
 */
public interface HasPdfStreamParserHandlers {
  /**
   * Handles the start of parsing a PDF file.
   * 
   * @param pdf
   *        The parsed PDF file.
   */
  void handlePdfFileStart(File pdf);

  /**
   * Handles the end of parsing a PDF file.
   * 
   * @param pdf
   *        The parsed PDF file.
   */
  void handlePdfFileEnd(File pdf);

  /**
   * Handles the start of a PDF page.
   * 
   * @param page
   *        The page to process.
   */
  void handlePdfPageStart(PdfPage page);

  /**
   * Handles the end of a PDF page.
   * 
   * @param page
   *        The page to process.
   */
  void handlePdfPageEnd(PdfPage page);

  /**
   * Handles a character in the PDF file.
   * 
   * @param character
   *        The parsed character.
   */
  void handlePdfCharacter(PdfCharacter character);

  /**
   * Handles a figure in the PDF file.
   * 
   * @param figure
   *        The parsed figure.
   */
  void handlePdfFigure(PdfFigure figure);

  /**
   * Handles a shape in the PDF file.
   * 
   * @param shape
   *        The parsed shape.
   */
  void handlePdfShape(PdfShape shape);
}
