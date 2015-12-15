package model;

/**
 * Interface to declare, that the implementing class holds a PdfTextPage.
 *
 * @author Claudius Korzen
 */
public interface HasPdfPage {
  /**
   * Returns the PdfTextPage object.
   */
  PdfPage getPage();
}
