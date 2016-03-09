package model;

import java.util.List;

import de.freiburg.iif.model.Rectangle;

/**
 * The interface for a pdf page, containing the list of characters, figures and
 * shapes.
 * 
 * @author Claudius Korzen
 */
public interface PdfPage extends PdfArea, HasTextLineStatistics {
  /**
   * Returns the number of this page in the document.
   */
  public int getPageNumber();

  /**
   * Returns the media box of this page. TODO: Explain media box.
   */
  public Rectangle getMediaBox();

  /**
   * Returns the crop box of this page. TODO: Explain crop box.
   */
  public Rectangle getCropBox();

  /**
   * Returns the art box of this page. TODO: Explain art box.
   */
  public Rectangle getArtBox();

  /**
   * Returns the trim box of this page. TODO: Explain trim box.
   */
  public Rectangle getTrimBox();

  /**
   * Returns the x-offset between cropBox and mediaBox.
   * 
   * @return the x-offset between cropBox and mediaBox.
   */
  public abstract float getCropXOffset();

  /**
   * Returns the y-offset between cropBox and mediaBox.
   * 
   * @return the y-offset between cropBox and mediaBox.
   */
  public abstract float getCropYOffset();

  // ___________________________________________________________________________

  /**
   * Returns the coverage of figures in this page.
   */
  public float getFiguresCoverage();

  public void setBlocks(List<? extends PdfArea> blocks);
  
  public List<? extends PdfArea> getBlocks();
}
