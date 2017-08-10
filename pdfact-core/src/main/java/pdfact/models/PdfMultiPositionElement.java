package pdfact.models;

/**
 * An element in a PDF document that may live at multiple positions
 * (within mutiple pages or multiple columns).
 * 
 * Examples for such elements are: paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface PdfMultiPositionElement extends PdfElement, HasPositions {

}
