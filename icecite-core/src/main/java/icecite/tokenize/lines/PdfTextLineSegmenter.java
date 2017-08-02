package icecite.tokenize.lines;

import java.util.List;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;

/**
 * A class to segment the characters of a text area into text line segments.
 * 
 * @author Claudius Korzen
 */
public interface PdfTextLineSegmenter {
  /**
   * Segments the given characters of a text area into text line segments.
   * 
   * @param pdf
   *        The PDF document to which the given characters belong to.
   * @param page
   *        The PDF page to which the given characters belong to.
   * @param textArea
   *        The characters of the text area to segment.
   * 
   * @return The list of text line segments.
   */
  List<PdfCharacterList> segment(PdfDocument pdf, PdfPage page,
      PdfCharacterList textArea);
}