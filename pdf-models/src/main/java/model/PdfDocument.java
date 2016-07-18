package model;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * The interface for the pdf document, containing a list of pages.
 * 
 * @author Claudius Korzen
 */
public interface PdfDocument extends HasDimensionStatistics, HasTextStatistics, 
    HasTextLineStatistics {
  /**
   * Returns the list of pages.
   */
  public List<PdfPage> getPages();
   
  /**
   * Returns the most common text alignment in this document.
   */
  public PdfTextAlignment getTextAlignment();
  
  /**
   * Returns all fonts used in this document.
   */
  public Collection<PdfFont> getFonts();
  
  /**
   * Returns all colors used in this document.
   */
  public Collection<PdfColor> getColors();
  
  /**
   * Returns the pdf file, from which the data were extracted.
   */
  public Path getPdfFile();
  
  /**
   * Returns the most common fontsize in this document.
   */
  public float getFontsize();  
  
  /**
   * Returns the markup of section headings.  TODO: Move to Analyzer.
   */
  public String getSectionHeadingMarkup();

  /**
   * Sets the markup of section headings. TODO: Move to Analyzer.
   */
  public void setSectionHeadingMarkup(String markup);  
  
  public float getEstimatedLinePitch();
}
