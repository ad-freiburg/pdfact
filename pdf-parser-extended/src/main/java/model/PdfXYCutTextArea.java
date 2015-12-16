package model;

import java.util.List;

import de.freiburg.iif.collection.CollectionUtils;
import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.rtree.RTree;
import de.freiburg.iif.rtree.SimpleRTree;
import statistics.TextStatistician;

/**
 * An area, which only consists of a text elements.
 *
 * @author Claudius Korzen
 *
 */
public class PdfXYCutTextArea extends PdfXYCutArea implements PdfTextArea {
  /**
   * The index of this area.
   */
  protected RTree<PdfCharacter> charIndex;
  
  /**
   * The text statistics of this area.
   */
  protected TextStatistics textStatistics;
  
  /**
   * Flag to indicate, whether the text statistics are outdated.
   */
  protected boolean isTextStatisticsOutdated;
  
  // ___________________________________________________________________________
  
  /**
   * The default constructor.
   */
  public PdfXYCutTextArea(PdfPage page) {
    super(page);
    this.charIndex = new SimpleRTree<>();
  }
  
  // ___________________________________________________________________________
  
  /**
   * Adds the given object to this area.
   */
  public void index(HasRectangle object) {
    super.index(object);
    this.isTextStatisticsOutdated = true;
    if (object instanceof PdfCharacter) {
      this.charIndex.insert((PdfCharacter) object);
    }
  }
  
  @Override
  public List<PdfCharacter> getCharacters() {
    return this.charIndex.getIndexEntries();
  }

  @Override
  public List<PdfCharacter> getCharactersContainedBy(Rectangle rect) {
    return this.charIndex.containedBy(rect);
  }

  @Override
  public List<PdfCharacter> getCharactersOverlappedBy(Rectangle rect) {
    return this.charIndex.overlappedBy(rect);
  }
  
  @Override
  public String getUnicode() {
    return CollectionUtils.join(getCharacters(), "");
  }

  @Override
  public PdfColor getColor() {
    return getTextStatistics().getMostCommonFontColor();
  }

  @Override
  public PdfFont getFont() {
    return getTextStatistics().getMostCommonFont();
  }

  @Override
  public float getFontsize() {
    return getTextStatistics().getMostCommonFontsize();
  }
    
  /**
   * Returns the text statistics.
   */
  public TextStatistics getTextStatistics() {
    if (needsTextStatisticsUpdate()) {
      computeTextStatistics();
    }
    return this.textStatistics;
  }
  
  /**
   * Computes the text statistics.
   */
  protected void computeTextStatistics() {
    List<HasText> objects = index.getIndexEntriesByClass(HasText.class);
    this.textStatistics = TextStatistician.compute(objects);
    this.isTextStatisticsOutdated = false;
  }
  
  /**
   * Returns true, if the text statistics needs an update.
   */
  protected boolean needsTextStatisticsUpdate() {
    return this.textStatistics == null || isTextStatisticsOutdated;
  }
  
  @Override
  public String toString() {
    return getUnicode();
  }
}
