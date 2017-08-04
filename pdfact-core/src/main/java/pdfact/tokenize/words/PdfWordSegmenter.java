package pdfact.tokenize.words;

import java.util.List;

import pdfact.models.PdfCharacterList;
import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;

/**
 * A class to segment the characters of a text line into word segments.
 * 
 * @author Claudius Korzen
 */
public interface PdfWordSegmenter {
  /**
   * Segments the given characters of a text line into word segments.
   * 
   * @param pdf
   *        The PDF document to which the given characters belong to.
   * @param page
   *        The PDF page to which the given characters belong to.
   * @param line
   *        The characters of the text line to segment.
   *
   * @return The list of word segments.
   */
  List<PdfCharacterList> segment(PdfDocument pdf, PdfPage page,
      PdfCharacterList line);
}
