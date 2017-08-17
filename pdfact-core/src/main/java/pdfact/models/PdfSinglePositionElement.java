package pdfact.models;

/**
 * An element in a PDF document that lives at a well-defined, single position
 * (within a single page and a single column).
 * 
 * Examples for such elements are: shapes, images, characters, words, text lines
 * or text blocks.
 * 
 * @author Claudius Korzen
 */
public interface PdfSinglePositionElement extends PdfElement, HasPosition {

}
