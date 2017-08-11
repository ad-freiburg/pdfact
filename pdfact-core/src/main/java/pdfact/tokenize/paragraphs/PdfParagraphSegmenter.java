package pdfact.tokenize.paragraphs;

import java.util.List;

import pdfact.models.PdfDocument;
import pdfact.models.PdfTextBlock;

/**
 * A class to segment a PDF document into paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface PdfParagraphSegmenter {
  /**
   * Segments the given PDF document paragraphs.
   * 
   * @param pdf
   *        The PDF document to process.
   *
   * @return The list of list of text blocks of a paragraph.
   */
  List<List<PdfTextBlock>> segment(PdfDocument pdf);
}