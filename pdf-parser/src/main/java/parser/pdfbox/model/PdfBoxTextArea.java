package parser.pdfbox.model;

import java.util.List;

import de.freiburg.iif.collection.CollectionUtils;
import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.rtree.RTree;
import de.freiburg.iif.rtree.SimpleRTree;
import model.HasText;
import model.PdfCharacter;
import model.PdfColor;
import model.PdfFont;
import model.PdfPage;
import model.PdfTextArea;
import model.TextStatistics;
import statistics.TextStatistician;

/**
 * An area, which only consists of a text elements.
 *
 * @author Claudius Korzen
 *
 */
public class PdfBoxTextArea extends PdfBoxArea implements PdfTextArea {
  /**
   * The index of this area.
   */
  protected RTree<PdfCharacter> index;
  
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
  public PdfBoxTextArea(PdfPage page) {
    super(page);
    this.index = new SimpleRTree<>();
  }
  
  // ___________________________________________________________________________
  
  /**
   * Adds the given object to this area.
   */
  public void put(HasRectangle object) {
    super.put(object);
    if (object instanceof PdfCharacter) {
      this.index.insert((PdfCharacter) object);
      this.isTextStatisticsOutdated = true;
    }
  }
  
  @Override
  public List<PdfCharacter> getCharacters() {
    return this.index.getIndexEntries();
  }

  @Override
  public List<PdfCharacter> getCharactersContainedBy(Rectangle rect) {
    return this.index.containedBy(rect);
  }

  @Override
  public List<PdfCharacter> getCharactersOverlappedBy(Rectangle rect) {
    return this.index.overlappedBy(rect);
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
