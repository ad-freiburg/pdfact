package model;

import java.util.List;
import java.util.Set;

/**
 * Interface for a single pdf paragraph.
 *
 * @author Claudius Korzen
 */
public interface PdfTextParagraph extends PdfTextElement,
    HasTextLineStatistics {
  /**
   * Returns the lines in this paragraph.
   */
  public List<PdfTextLine> getTextLines();

  /**
   * Returns the first line in this paragraph.
   */
  public PdfTextLine getFirstTextLine();

  /**
   * Returns the last line in this paragraph.
   */
  public PdfTextLine getLastTextLine();

  /**
   * Adds the given line to this paragraph.
   */
  public void addTextLine(PdfTextLine line);

  /**
   * Sets the context of this paragraph.
   */
  public void setContext(String context);

  /**
   * Returns the context of this paragraph.
   */
  public String getContext();
  
  /**
   * Returns all possible alignments for this paragraph.
   */
  public Set<PdfTextAlignment> getTextAlignmentVariants();
  
  /**
   * Computes the alignment for the given line that results for the line if it
   * would be added to this paragraph.
   */
  public PdfTextAlignment computeTextAlignment(PdfTextLine line);
}
