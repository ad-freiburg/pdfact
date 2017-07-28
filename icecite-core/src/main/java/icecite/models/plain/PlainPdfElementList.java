package icecite.models.plain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfElement;
import icecite.models.PdfElementList;
import icecite.utils.counter.FloatCounter;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;
import icecite.utils.math.MathUtils;
import icecite.utils.sort.Quicksort;

// TODO: Accelerate cut method.

/**
 * A plain implementation of {@link PdfElementList}.
 * 
 * @param <T>
 *        The concrete type of the elements in this list.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfElementList<T extends PdfElement> extends ArrayList<T>
    implements PdfElementList<T> {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 2032345121881710427L;

  /**
   * The default initial capacity of this list.
   */
  protected static final int DEFAULT_INITIAL_CAPACITY = 16;

  /**
   * The factory to create instances of {@link Rectangle}.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The average height of the elements in this list.
   */
  protected float averageHeight = Float.NaN;

  /**
   * The most common height of the elements in this list.
   */
  protected float mostCommonHeight = Float.NaN;

  /**
   * The average width of the elements in this list.
   */
  protected float averageWidth = Float.NaN;

  /**
   * The most common width of the elements in this list.
   */
  protected float mostCommonWidth = Float.NaN;

  /**
   * The smallest minX of the elements in this list.
   */
  protected float smallestMinX = Float.MAX_VALUE;

  /**
   * The elements with the smallest minX value.
   */
  protected Set<T> elementsWithSmallestMinX;

  /**
   * The smallest minY of the elements in this list.
   */
  protected float smallestMinY = Float.MAX_VALUE;

  /**
   * The elements with the smallest minY value.
   */
  protected Set<T> elementsWithSmallestMinY;

  /**
   * The largest maxX of the elements in this list.
   */
  protected float largestMaxX = -Float.MAX_VALUE;

  /**
   * The elements with the largest maxX value.
   */
  protected Set<T> elementsWithLargestMaxX;

  /**
   * The largest maxY of the elements in this list.
   */
  protected float largestMaxY = -Float.MAX_VALUE;

  /**
   * The elements with the largest maxY value.
   */
  protected Set<T> elementsWithLargestMaxY;

  /**
   * The bounding box around the elements in this list.
   */
  protected Rectangle rectangle;

  /**
   * A flag that indicates whether the statistics are outdated.
   */
  protected boolean isStatisticsOutdated = true;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new list.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   */
  @AssistedInject
  public PlainPdfElementList(RectangleFactory rectangleFactory) {
    this(rectangleFactory, DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new list.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@link Rectangle}.
   * @param initialCapacity
   *        The initial capacity.
   */
  @AssistedInject
  public PlainPdfElementList(RectangleFactory rectangleFactory,
      int initialCapacity) {
    super(initialCapacity);
    this.rectangleFactory = rectangleFactory;
  }

  // ==========================================================================
  // Override all methods that changes this list.

  @Override
  public boolean add(T t) {
    boolean added = super.add(t);
    if (added) {
      this.isStatisticsOutdated = true;
    }
    return added;
  }

  @Override
  public void add(int index, T t) {
    super.add(index, t);
    this.isStatisticsOutdated = true;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean added = false;
    for (T t : c) {
      added |= add(t);  
    }
    if (added) {
      this.isStatisticsOutdated = true;
    }
    return added;
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    int oldSize = size();
    int i = 0;
    for (T t : c) {
      add(index + (i++), t);  
    }
    int newSize = size();
    boolean added = newSize > oldSize;
    if (added) {
      this.isStatisticsOutdated = true;
    }
    return added;
  }

  @Override
  public T remove(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public T set(int i, T element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void swap(int i, int j) {
    T tmp = get(i);
    super.set(i, get(j));
    super.set(j, tmp);
  }

  @Override
  public void sort(Comparator<? super T> c) {
    Quicksort.sort(this, c);
  }

  // ==========================================================================
  // Register methods.

  /**
   * Computes some statistics about the elements in this list.
   */
  protected void computeStatistics() {
    // Count the heights and widths of the elements in this list.
    FloatCounter heightsCounter = new FloatCounter();
    FloatCounter widthsCounter = new FloatCounter();

    for (T element : this) {
      if (element == null) {
        continue;
      }

      Rectangle rectangle = element.getRectangle();

      heightsCounter.add(rectangle.getHeight());
      widthsCounter.add(rectangle.getWidth());

      if (rectangle.getMinX() < this.smallestMinX) {
        this.smallestMinX = rectangle.getMinX();
      }

      if (rectangle.getMinY() < this.smallestMinY) {
        this.smallestMinY = rectangle.getMinY();
      }

      if (rectangle.getMaxX() > this.largestMaxX) {
        this.largestMaxX = rectangle.getMaxX();
      }

      if (rectangle.getMaxY() > this.largestMaxY) {
        this.largestMaxY = rectangle.getMaxY();
      }
    }
    // TODO: Move the computation of the smallest / largest values into the
    // counters.

    // Compute the average values.
    this.averageHeight = heightsCounter.getAverageFloat();
    this.averageWidth = widthsCounter.getAverageFloat();

    // Compute the most common values.
    this.mostCommonHeight = heightsCounter.getMostCommonFloat();
    this.mostCommonWidth = widthsCounter.getMostCommonFloat();

    this.isStatisticsOutdated = false;
  }

  // ==========================================================================

  @Override
  public List<? extends PdfElementList<T>> cut(int index) {
    PdfElementView<T> left = new PdfElementView<>(this, 0, index);
    PdfElementView<T> right = new PdfElementView<>(this, index, this.size());
    return Arrays.asList(left, right);
  }

  // ==========================================================================

  @Override
  public float getAverageHeight() {
    if (Float.isNaN(this.averageHeight) || this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.averageHeight;
  }

  @Override
  public float getMostCommonHeight() {
    if (Float.isNaN(this.mostCommonHeight) || this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.mostCommonHeight;
  }

  // ==========================================================================

  @Override
  public float getAverageWidth() {
    if (Float.isNaN(this.averageWidth) || this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.averageWidth;
  }

  @Override
  public float getMostCommonWidth() {
    if (Float.isNaN(this.mostCommonWidth) || this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.mostCommonWidth;
  }

  // ==========================================================================

  @Override
  public float getSmallestMinX() {
    if (this.smallestMinX == Float.MAX_VALUE || this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.smallestMinX;
  }

  @Override
  public Set<T> getElementsWithSmallestMinX() {
    if (this.elementsWithSmallestMinX == null || this.isStatisticsOutdated) {
      this.elementsWithSmallestMinX = computeElementsWithSmallestMinX();
    }
    return this.elementsWithSmallestMinX;
  }

  /**
   * Computes the elements with smallest minX value.
   * 
   * @return The elements with smallest minX value.
   */
  protected Set<T> computeElementsWithSmallestMinX() {
    float smallestMinX = getSmallestMinX();
    Set<T> elements = new HashSet<>(); // TODO: Choose capacity.
    for (T element : this) {
      if (element.getRectangle().getMinX() == smallestMinX) {
        elements.add(element);
      }
    }
    return elements;
  }

  // ==========================================================================

  @Override
  public float getSmallestMinY() {
    if (this.smallestMinY == Float.MAX_VALUE || this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.smallestMinY;
  }

  @Override
  public Set<T> getElementsWithSmallestMinY() {
    if (this.elementsWithSmallestMinY == null || this.isStatisticsOutdated) {
      this.elementsWithSmallestMinY = computeElementsWithSmallestMinY();
    }
    return this.elementsWithSmallestMinY;
  }

  /**
   * Computes the elements with smallest minY value.
   * 
   * @return The elements with smallest minY value.
   */
  protected Set<T> computeElementsWithSmallestMinY() {
    float smallestMinY = getSmallestMinY();
    Set<T> elements = new HashSet<>(); // TODO: Choose capacity.
    for (T element : this) {
      if (element.getRectangle().getMinY() == smallestMinY) {
        elements.add(element);
      }
    }
    return elements;
  }

  // ==========================================================================

  @Override
  public float getLargestMaxX() {
    if (this.largestMaxX == -Float.MAX_VALUE || this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.largestMaxX;
  }

  @Override
  public Set<T> getElementsWithLargestMaxX() {
    if (this.elementsWithLargestMaxX == null || this.isStatisticsOutdated) {
      this.elementsWithLargestMaxX = computeElementsWithLargestMaxX();
    }
    return this.elementsWithLargestMaxX;
  }

  /**
   * Computes the elements with largest maxX value.
   * 
   * @return The elements with largest maxX value.
   */
  protected Set<T> computeElementsWithLargestMaxX() {
    float largestMaxX = getLargestMaxX();
    Set<T> elements = new HashSet<>(); // TODO: Choose capacity.
    for (T element : this) {
      // TODO: Allow a tolerance value.
      float maxX = element.getRectangle().getMaxX();
      if (MathUtils.isEqual(maxX, largestMaxX, 1f)) {
        elements.add(element);
      }
    }
    return elements;
  }

  // ==========================================================================

  @Override
  public float getLargestMaxY() {
    if (this.largestMaxY == -Float.MAX_VALUE || this.isStatisticsOutdated) {
      computeStatistics();
    }
    return this.largestMaxY;
  }

  @Override
  public Set<T> getElementsWithLargestMaxY() {
    if (this.elementsWithLargestMaxY == null || this.isStatisticsOutdated) {
      this.elementsWithLargestMaxY = computeElementsWithLargestMaxY();
    }
    return this.elementsWithLargestMaxY;
  }

  /**
   * Computes the elements with largest maxY value.
   * 
   * @return The elements with largest maxY value.
   */
  protected Set<T> computeElementsWithLargestMaxY() {
    float largestMaxY = getLargestMaxY();
    Set<T> elements = new HashSet<>(); // TODO: Choose capacity.
    for (T element : this) {
      if (element.getRectangle().getMaxY() == largestMaxY) {
        elements.add(element);
      }
    }
    return elements;
  }

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    if (this.rectangle == null || this.isStatisticsOutdated) {
      float minX = getSmallestMinX();
      float minY = getSmallestMinY();
      float maxX = getLargestMaxX();
      float maxY = getLargestMaxY();
      this.rectangle = this.rectangleFactory.create(minX, minY, maxX, maxY);
    }
    return this.rectangle;
  }

  @Override
  public void setRectangle(Rectangle rectangle) {
    // The rectangle results from the elements in this list. Hence, it is
    // not allowed to set the rectangle explicitly.
    throw new UnsupportedOperationException();
  }

  // ==========================================================================

  @Override
  public String toString() {
    return super.toString();
  }

  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  // ==========================================================================

  /**
   * A view of a PdfElementList.
   * 
   * @author Claudius Korzen
   *
   * @param <S>
   *        The concrete type of the elements in this list.
   */
  class PdfElementView<S extends PdfElement> extends PlainPdfElementList<S>
      implements PdfElementList<S> {
    /**
     * The serial id.
     */
    protected static final long serialVersionUID = 884367879377788123L;

    /**
     * The parent list.
     */
    protected final PdfElementList<S> parent;

    /**
     * The offset of this view in the parent list.
     */
    protected final int offset;

    /**
     * The size of this list (the number of elements).
     */
    protected int size;

    // ========================================================================
    // Constructors.

    /**
     * Creates a new view based on the given parent list.
     * 
     * @param parent
     *        The parent list.
     * @param fromIndex
     *        The start index in the parent list.
     * @param toIndex
     *        The end index in the parent list.
     */
    PdfElementView(PdfElementList<S> parent, int fromIndex, int toIndex) {
      super(PlainPdfElementList.this.rectangleFactory);
      this.parent = parent;
      this.offset = fromIndex;
      this.size = toIndex - fromIndex;
    }

    // ========================================================================
    // Don't allow to add elements to views.

    @Override
    public boolean add(S s) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, S s) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends S> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends S> c) {
      throw new UnsupportedOperationException();
    }

    // ========================================================================
    // Iterators.

    @Override
    public Iterator<S> iterator() {
      return listIterator(0);
    }

    @Override
    public ListIterator<S> listIterator(final int index) {
      return new ListIterator<S>() {
        int cursor = index;

        @Override
        public boolean hasNext() {
          return this.cursor != PdfElementView.this.size;
        }

        @Override
        public S next() {
          if (this.cursor >= PdfElementView.this.size) {
            throw new NoSuchElementException();
          }
          return PdfElementView.this.get(this.cursor++);
        }

        @Override
        public boolean hasPrevious() {
          return this.cursor != 0;
        }

        @Override
        public S previous() {
          if (this.cursor - 1 < 0) {
            throw new NoSuchElementException();
          }
          return PdfElementView.this.get(this.cursor--);
        }

        @Override
        public int nextIndex() {
          return this.cursor;
        }

        @Override
        public int previousIndex() {
          return this.cursor - 1;
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }

        @Override
        public void set(S e) {
          throw new UnsupportedOperationException();
        }

        @Override
        public void add(S e) {
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override
    public Spliterator<S> spliterator() {
      throw new UnsupportedOperationException();
    }

    // ========================================================================
    // Getters.

    @Override
    public S get(int index) {
      return this.parent.get(this.offset + index);
    }

    @Override
    public int size() {
      return this.size;
    }

    public boolean isEmpty() {
      return this.size == 0;
    }

    // ========================================================================

    @Override
    public void swap(int i, int j) {
      this.parent.swap(this.offset + i, this.offset + j);
    }

    // ========================================================================

    @Override
    public List<? extends PdfElementList<S>> cut(int i) {
      // Create new views.
      int left = this.offset;
      int cut = this.offset + i;
      int right = this.offset + size();
      PdfElementView<S> v1 = new PdfElementView<S>(this.parent, left, cut);
      PdfElementView<S> v2 = new PdfElementView<S>(this.parent, cut, right);
      return Arrays.asList(v1, v2);
    }

    // ========================================================================

    @Override
    public String toString() {
      return super.toString();
    }

    @Override
    public boolean equals(Object other) {
      return super.equals(other);
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }
}
