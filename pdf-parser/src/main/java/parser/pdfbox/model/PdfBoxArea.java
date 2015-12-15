package parser.pdfbox.model;

import java.util.List;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.rtree.RTree;
import de.freiburg.iif.rtree.SimpleRTree;
import model.DimensionStatistics;
import model.PdfArea;
import model.PdfDocument;
import model.PdfPage;
import statistics.DimensionStatistician;

/**
 * An area, which consists of multiple elements.
 *
 * @author Claudius Korzen
 *
 */
public class PdfBoxArea implements PdfArea {
  /**
   * The page, in which this area is located.
   */
  protected PdfPage page;

  /**
   * The index of this area.
   */
  protected RTree<HasRectangle> index;

  /**
   * The dimension statistics of this area.
   */
  protected DimensionStatistics dimStatistics;

  /**
   * Flag to indicate, whether the dimension statistics are outdated.
   */
  protected boolean isDimensionStatisticsOutdated;

  // ___________________________________________________________________________
  // Constructors.

  /**
   * The default constructor is only available for extending classes.
   */
  public PdfBoxArea(PdfPage page) {
    this.page = page;
    this.index = new SimpleRTree<>();
  }
  
  /**
   * Creates a page area from the given parent, indexing all content objects
   * within the given rectangle.
   */
  public PdfBoxArea(PdfArea parent, Rectangle area) {
    this(parent.getPage());
    put(parent.getElementsContainedBy(area));
  }

  // ___________________________________________________________________________
  // Put methods.

  /**
   * Puts the given objects into this area.
   */
  public void put(List<? extends HasRectangle> objects) {
    for (HasRectangle object : objects) {
      put(object);
    }
  }

  /**
   * Puts the given object to this area.
   */
  public void put(HasRectangle object) {
    this.index.insert(object);
    this.isDimensionStatisticsOutdated = true;
  }

  // ___________________________________________________________________________
  // Getters and setters.
  
  @Override
  public List<HasRectangle> getElements() {
    return this.index.getIndexEntries();
  }

  /**
   * Returns the objects which are contained by the given rectangle.
   */
  public List<HasRectangle> getElementsContainedBy(Rectangle rect) {
    return this.index.containedBy(rect);
  }

  /**
   * Returns the objects which are overlapped by the given rectangle.
   */
  public List<HasRectangle> getElementsOverlappedBy(Rectangle rect) {
    return this.index.overlappedBy(rect);
  }

  @Override
  public PdfDocument getPdfDocument() {
    return this.page.getPdfDocument();
  }

  @Override
  public PdfPage getPage() {
    return this.page;
  }

  @Override
  public Rectangle getRectangle() {
    return this.index.getRoot().getRectangle();
  }

  // ___________________________________________________________________________
  // Statistics methods.  
  
  /**
   * Returns the dimension statistics.
   */
  public DimensionStatistics getDimensionStatistics() {
    if (needsDimensionStatisticsUpdate()) {
      computeDimensionStatistics();
    }
    return this.dimStatistics;
  }

  /**
   * Computes the dimension statistics.
   */
  protected void computeDimensionStatistics() {
    List<HasRectangle> objects = index.getIndexEntries();
    this.dimStatistics = DimensionStatistician.compute(objects);
    this.isDimensionStatisticsOutdated = false;
  }

  /**
   * Returns true, if the dimension statistics needs an update.
   */
  protected boolean needsDimensionStatisticsUpdate() {
    return this.dimStatistics == null || isDimensionStatisticsOutdated;
  }
}
