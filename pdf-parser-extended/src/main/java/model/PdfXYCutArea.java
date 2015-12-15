package model;

import java.util.List;

import de.freiburg.iif.model.HasRectangle;
import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.rtree.RTree;
import de.freiburg.iif.rtree.SimpleRTree;
import statistics.DimensionStatistician;

/**
 * An area, which consists of multiple elements.
 *
 * @author Claudius Korzen
 *
 */
public class PdfXYCutArea implements PdfArea {
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
   * Creates a new area in the given page.
   */
  public PdfXYCutArea(PdfPage page) {
    this.page = page;
    this.index = new SimpleRTree<>();
  }

  /**
   * Creates a new area in the given page with the given elements.
   */
  public PdfXYCutArea(PdfPage page, List<? extends HasRectangle> elements) {
    this(page);
    index(elements);
  }

  /**
   * Creates a new area from the given parent, indexing all content objects
   * within the given rectangle.
   */
  public PdfXYCutArea(PdfArea parent, Rectangle area) {
    this(parent.getPage());
    index(parent.getElementsContainedBy(area));
  }

  // ___________________________________________________________________________
  // Getters and setters.

  /**
   * Adds the given objects to this area.
   */
  public void index(List<? extends HasRectangle> objects) {
    for (HasRectangle object : objects) {
      index(object);
    }
  }

  /**
   * Adds the given object to this area.
   */
  public void index(HasRectangle object) {
    this.index.insert(object);
    this.isDimensionStatisticsOutdated = true;
  }

  @Override
  public List<? extends HasRectangle> getElements() {
    return index.getIndexEntries();
  }

  /**
   * Returns the objects which are contained by the given rectangle.
   */
  public List<? extends HasRectangle> getElementsContainedBy(Rectangle rect) {
    return index.containedBy(rect);
  }

  /**
   * Returns the objects which are overlapped by the given rectangle.
   */
  public List<? extends HasRectangle> getElementsOverlappedBy(Rectangle rect) {
    return index.overlappedBy(rect);
  }
  
  @Override
  public PdfDocument getPdfDocument() {
    return this.page.getPdfDocument();
  }

  /**
   * Returns the wrapped page object.
   */
  public PdfPage getPage() {
    return this.page;
  }

  @Override
  public Rectangle getRectangle() {
    return this.index.getRoot().getRectangle();
  }

  // ___________________________________________________________________________

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
