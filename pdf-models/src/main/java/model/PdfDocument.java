package model;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * The interface for the pdf document, containing a list of pages.
 * 
 * @author Claudius Korzen
 */
public interface PdfDocument extends HasDimensionStatistics, HasTextStatistics {
  /**
   * Returns the list of pages.
   */
  public List<PdfPage> getPages();
    
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
}
