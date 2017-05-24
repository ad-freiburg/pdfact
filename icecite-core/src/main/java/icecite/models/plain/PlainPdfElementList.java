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
import icecite.utils.sort.Quicksort;

// TODO: Accelerate cut method.

/**
 * A plain implementation of {@link PdfElementList}.
 * 
 * @param <T>
 *        The concrete type of the elements in this List.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfElementList<T extends PdfElement> extends ArrayList<T>
    implements PdfElementList<T> {
  /**
   * The serial id.
   */
  private static final long serialVersionUID = 2032345121881710427L;

  /**
   * The default initial capacity of this list.
   */
  protected static final int DEFAULT_INITIAL_CAPACITY = 16;

  /**
   * The factory to create instances of Rectangle.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The average height.
   */
  protected float averageHeight = Float.NaN;

  /**
   * The most common height.
   */
  protected float mostCommonHeight = Float.NaN;

  /**
   * The average width.
   */
  protected float averageWidth = Float.NaN;

  /**
   * The most common width.
   */
  protected float mostCommonWidth = Float.NaN;

  /**
   * The smallest minX.
   */
  protected float smallestMinX = Float.MAX_VALUE;

  /**
   * The elements with the smallest minX value.
   */
  protected Set<T> elementsWithSmallestMinX;

  /**
   * The smallest minY.
   */
  protected float smallestMinY = Float.MAX_VALUE;

  /**
   * The elements with the smallest minY value.
   */
  protected Set<T> elementsWithSmallestMinY;

  /**
   * The largest maxX.
   */
  protected float largestMaxX = -Float.MAX_VALUE;

  /**
   * The elements with the largest maxX value.
   */
  protected Set<T> elementsWithLargestMaxX;

  /**
   * The largest maxY.
   */
  protected float largestMaxY = -Float.MAX_VALUE;

  /**
   * The elements with the largest maxY value.
   */
  protected Set<T> elementsWithLargestMaxY;

  /**
   * The bounding box around the elements in this list.
   */
  protected Rectangle boundingBox;

  /**
   * A flag that indicates whether the statistics were already computed.
   */
  protected boolean isStatisticsComputed;

  /**
   * The size of the ArrayList (the number of elements it contains).
   */
  protected int size;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new list.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @AssistedInject
  public PlainPdfElementList(RectangleFactory rectangleFactory) {
    this(rectangleFactory, DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new list.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
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
    if (this.isStatisticsComputed) {
      throw new IllegalStateException("Statistics were already computed.");
    }
    this.size++;
    return super.add(t);
  }

  @Override
  public void add(int index, T t) {
    if (this.isStatisticsComputed) {
      throw new IllegalStateException("Statistics were already computed.");
    }
    super.add(index, t);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    if (this.isStatisticsComputed) {
      throw new IllegalStateException("Statistics were already computed.");
    }
    return super.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    if (this.isStatisticsComputed) {
      throw new IllegalStateException("Statistics were already computed.");
    }
    return super.addAll(index, c);
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
   * Registers the given element to this list.
   */
  protected void computeStatistics() {
    FloatCounter heightsCounter = new FloatCounter();
    FloatCounter widthsCounter = new FloatCounter();

    for (T element : this) {
      // Process the height.
      heightsCounter.add(element.getBoundingBox().getHeight());

      // Process the width.
      widthsCounter.add(element.getBoundingBox().getWidth());

      // Process minX.
      if (element.getBoundingBox().getMinX() < this.smallestMinX) {
        this.smallestMinX = element.getBoundingBox().getMinX();
      }

      // Process minY.
      if (element.getBoundingBox().getMinY() < this.smallestMinY) {
        this.smallestMinY = element.getBoundingBox().getMinY();
      }

      // Process maxX.
      if (element.getBoundingBox().getMaxX() > this.largestMaxX) {
        this.largestMaxX = element.getBoundingBox().getMaxX();
      }

      // Process maxY.
      if (element.getBoundingBox().getMaxY() > this.largestMaxY) {
        this.largestMaxY = element.getBoundingBox().getMaxY();
      }
    }
    this.averageHeight = heightsCounter.getAverageFloat();
    this.mostCommonHeight = heightsCounter.getMostCommonFloat();

    this.averageWidth = widthsCounter.getAverageFloat();
    this.mostCommonWidth = widthsCounter.getMostCommonFloat();

    this.isStatisticsComputed = true;
  }

  // ==========================================================================

  @Override
  public List<? extends PdfElementList<T>> cut(int index) {
    // Create new views.
    PdfElementView<T> left = new PdfElementView<>(this, 0, index);
    PdfElementView<T> right = new PdfElementView<>(this, index, this.size());
    return Arrays.asList(left, right);
  }

  // ==========================================================================

  @Override
  public float getAverageHeight() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.averageHeight;
  }

  @Override
  public float getMostCommonHeight() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.mostCommonHeight;
  }

  // ==========================================================================

  @Override
  public float getAverageWidth() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.averageWidth;
  }

  @Override
  public float getMostCommonWidth() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.mostCommonWidth;
  }

  // ==========================================================================

  @Override
  public float getSmallestMinX() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.smallestMinX;
  }

  @Override
  public Set<T> getElementsWithSmallestMinX() {
    if (this.elementsWithSmallestMinX == null) {
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
      if (element.getBoundingBox().getMinX() == smallestMinX) {
        elements.add(element);
      }
    }
    return elements;
  }

  // ==========================================================================

  @Override
  public float getSmallestMinY() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.smallestMinY;
  }

  @Override
  public Set<T> getElementsWithSmallestMinY() {
    if (this.elementsWithSmallestMinY == null) {
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
      if (element.getBoundingBox().getMinY() == smallestMinY) {
        elements.add(element);
      }
    }
    return elements;
  }

  // ==========================================================================

  @Override
  public float getLargestMaxX() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.largestMaxX;
  }

  @Override
  public Set<T> getElementsWithLargestMaxX() {
    if (this.elementsWithLargestMaxX == null) {
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
      if (element.getBoundingBox().getMaxX() == largestMaxX) {
        elements.add(element);
      }
    }
    return elements;
  }

  // ==========================================================================

  @Override
  public float getLargestMaxY() {
    if (!this.isStatisticsComputed) {
      computeStatistics();
    }
    return this.largestMaxY;
  }

  @Override
  public Set<T> getElementsWithLargestMaxY() {
    if (this.elementsWithLargestMaxY == null) {
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
      if (element.getBoundingBox().getMaxY() == largestMaxY) {
        elements.add(element);
      }
    }
    return elements;
  }

  // ==========================================================================

  @Override
  public Rectangle getBoundingBox() {
    if (this.boundingBox == null) {
      this.boundingBox = this.rectangleFactory.create();
      this.boundingBox.setMinX(getSmallestMinX());
      this.boundingBox.setMinY(getSmallestMinY());
      this.boundingBox.setMaxX(getLargestMaxX());
      this.boundingBox.setMaxY(getLargestMaxY());
    }
    return this.boundingBox;
  }

  @Override
  public void setBoundingBox(Rectangle boundingBox) {
    // The bounding box results from the elements in this list. It is not
    // allowed to set the bounding box explicitly.
    throw new UnsupportedOperationException();
  }

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
    protected final PdfElementList<S> base;

    /**
     * The offset in the parent list.
     */
    protected final int offset;

    /**
     * The size of the ArrayList (the number of elements it contains).
     */
    private int size;

    // ========================================================================
    // Constructors.

    /**
     * Creates a new view based on the given parent list.
     * 
     * @param parent
     *        The parent list.
     * @param fromIndex
     *        The start index.
     * @param toIndex
     *        The end index.
     */
    PdfElementView(PdfElementList<S> parent, int fromIndex, int toIndex) {
      super(PlainPdfElementList.this.rectangleFactory);
      this.base = parent;
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
      return this.base.get(this.offset + index);
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
      this.base.swap(this.offset + i, this.offset + j);
    }

    // ========================================================================

    @Override
    public List<? extends PdfElementList<S>> cut(int i) {
      // Create new views.
      int left = this.offset;
      int cut = this.offset + i;
      int right = this.offset + size();
      PdfElementView<S> v1 = new PdfElementView<S>(this.base, left, cut);
      PdfElementView<S> v2 = new PdfElementView<S>(this.base, cut, right);
      return Arrays.asList(v1, v2);
    }
  }
}
