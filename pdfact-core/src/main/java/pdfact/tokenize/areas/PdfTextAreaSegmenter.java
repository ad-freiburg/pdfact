package pdfact.tokenize.areas;

import java.util.List;

import pdfact.models.PdfCharacterList;
import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;

/**
 * A class to segment the characters of a PDF page into text area segments,
 * where a text area has no special definition and is merely used to distinguish
 * characters from different columns of a page.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextAreaSegmenter {
  /**
   * Segments the given characters of a page into text area segments.
   * 
   * @param pdf
   *        The PDF document to which the given characters belong to.
   * @param page
   *        The PDF page to which the given characters belong to.
   * @param chars
   *        The characters of the page to segment.
   *
   * @return The list of text area segments.
   */
  List<PdfCharacterList> segment(PdfDocument pdf, PdfPage page,
      PdfCharacterList chars);
}