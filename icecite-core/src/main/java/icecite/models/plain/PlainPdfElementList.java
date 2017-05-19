package icecite.models.plain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Set;
import java.util.Spliterator;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.HasBoundingBox;
import icecite.models.PdfElement;
import icecite.models.PdfElementList;
import icecite.utils.counter.FloatCounter;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;

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
  protected static final long serialVersionUID = 7436122359215261783L;

  /**
   * The default initial capacity of this list.
   */
  protected static final int DEFAULT_INITIAL_CAPACITY = 16;

  /**
   * The factory to create instances of {@Rectangle}.
   */
  protected RectangleFactory rectangleFactory;

  /**
   * The counter for the minX values.
   */
  protected FloatCounter<T> minXCounter;

  /**
   * The counter for the minY values.
   */
  protected FloatCounter<T> minYCounter;

  /**
   * The counter for the maxX values.
   */
  protected FloatCounter<T> maxXCounter;

  /**
   * The counter for the maxY values.
   */
  protected FloatCounter<T> maxYCounter;

  /**
   * The counter for the height values.
   */
  protected FloatCounter<T> heightCounter;

  /**
   * The counter for the width values.
   */
  protected FloatCounter<T> widthCounter;

  /**
   * The bounding box around the elements in this list.
   */
  protected Rectangle boundingBox;

  /**
   * A boolean flag that indicates whether the bounding box is outdated.
   */
  protected boolean isBoundingBoxOutdated;

  /**
   * The index of the previous split.
   */
  protected int currentSplitIndex;

  /**
   * The previous portion between index 0 and previousSplitIndex.
   */
  protected PdfElementListView<T> currentLeftHalf;

  /**
   * The previous portion between previousSplitIndex and index this.size().
   */
  protected PdfElementListView<T> currentRightHalf;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PdfElementList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@Rectangle}.
   */
  @AssistedInject
  public PlainPdfElementList(RectangleFactory rectangleFactory) {
    this(rectangleFactory, DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new PdfElementList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@Rectangle}.
   * @param initialCapacity
   *        The initial capacity of this list.
   */
  @AssistedInject
  public PlainPdfElementList(RectangleFactory rectangleFactory,
      @Assisted int initialCapacity) {
    super(initialCapacity);
    this.rectangleFactory = rectangleFactory;
    this.minXCounter = new FloatCounter<>(initialCapacity);
    this.minYCounter = new FloatCounter<>(initialCapacity);
    this.maxXCounter = new FloatCounter<>(initialCapacity);
    this.maxYCounter = new FloatCounter<>(initialCapacity);
    this.heightCounter = new FloatCounter<>(initialCapacity);
    this.widthCounter = new FloatCounter<>(initialCapacity);
  }

  // ==========================================================================
  // Override all methods that changes this set.

  @Override
  public boolean add(T t) {
    boolean added = super.add(t);
    if (added) {
      registerPdfElement(t);
    }
    return added;
  }

  @Override
  public void add(int index, T t) {
    super.add(index, t);
    registerPdfElement(t);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean added = super.addAll(c);
    if (added) {
      registerPdfElements(c);
    }
    return added;
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    boolean added = super.addAll(index, c);
    if (added) {
      registerPdfElements(c);
    }
    return added;
  }

  @Override
  public T set(int index, T element) {
    T old = super.set(index, element);
    unregisterPdfElement(old);
    registerPdfElement(element);
    return old;
  }

  @Override
  public T remove(int index) {
    T element = super.remove(index);
    unregisterPdfElement(element);
    return element;
  }

  @Override
  public boolean remove(Object o) {
    boolean removed = super.remove(o);
    if (removed) {
      unregisterPdfElement(o);
    }
    return removed;
  }

  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    unregisterPdfElements(super.subList(fromIndex, toIndex));
    super.removeRange(fromIndex, toIndex);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean removed = super.removeAll(c);
    if (removed) {
      for (Object o : c) {
        unregisterPdfElement(o);
      }
    }
    return removed;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    super.clear();
    this.minXCounter.clear();
    this.minYCounter.clear();
    this.maxXCounter.clear();
    this.maxYCounter.clear();
  }

  // ==========================================================================
  // Split methods.

  @Override
  public List<? extends PdfElementList<T>> split(int splitIndex) {
    PdfElementListView<T> leftHalf = new PdfElementListView<>(this, 0,
        splitIndex);
    PdfElementListView<T> rightHalf = new PdfElementListView<>(this, splitIndex,
        this.size());
    split(splitIndex, leftHalf, rightHalf);
    return Arrays.asList(leftHalf, rightHalf);
  }

  /**
   * Splits this list at the given index into two halves. Both halves are views
   * of the related portion of the list, that is (1) the portion between index
   * 0, inclusive, and splitIndex, exclusive; and (2) the portion between
   * splitIndex, inclusive, and this.size(), exclusive.
   * 
   * @param splitIndex
   *        The index where to split this list.
   * @param leftHalfToFill
   *        The left half to populate with the counters.
   * @param rightHalfToFill
   *        The left half to populate with the counters.
   */
  public void split(int splitIndex, PdfElementListView<T> leftHalfToFill,
      PdfElementListView<T> rightHalfToFill) {
    FloatCounter<T> leftMinXCounter, rightMinXCounter;
    FloatCounter<T> leftMinYCounter, rightMinYCounter;
    FloatCounter<T> leftMaxXCounter, rightMaxXCounter;
    FloatCounter<T> leftMaxYCounter, rightMaxYCounter;
    FloatCounter<T> leftWidthCounter, rightWidthCounter;
    FloatCounter<T> leftHeightCounter, rightHeightCounter;

    if (this.currentLeftHalf != null && this.currentRightHalf != null) {
      leftMinXCounter = this.currentLeftHalf.getMinXCounter();
      rightMinXCounter = this.currentRightHalf.getMinXCounter();
      leftMinYCounter = this.currentLeftHalf.getMinYCounter();
      rightMinYCounter = this.currentRightHalf.getMinYCounter();
      leftMaxXCounter = this.currentLeftHalf.getMaxXCounter();
      rightMaxXCounter = this.currentRightHalf.getMaxXCounter();
      leftMaxYCounter = this.currentLeftHalf.getMaxYCounter();
      rightMaxYCounter = this.currentRightHalf.getMaxYCounter();
      leftWidthCounter = this.currentLeftHalf.getWidthCounter();
      rightWidthCounter = this.currentRightHalf.getWidthCounter();
      leftHeightCounter = this.currentLeftHalf.getHeightCounter();
      rightHeightCounter = this.currentRightHalf.getHeightCounter();
    } else {
      int initialCapacity = 16; // TODO
      leftMinXCounter = new FloatCounter<>(initialCapacity);
      rightMinXCounter = new FloatCounter<>(initialCapacity);
      leftMinYCounter = new FloatCounter<>(initialCapacity);
      rightMinYCounter = new FloatCounter<>(initialCapacity);
      leftMaxXCounter = new FloatCounter<>(initialCapacity);
      rightMaxXCounter = new FloatCounter<>(initialCapacity);
      leftMaxYCounter = new FloatCounter<>(initialCapacity);
      rightMaxYCounter = new FloatCounter<>(initialCapacity);
      leftWidthCounter = new FloatCounter<>(initialCapacity);
      rightWidthCounter = new FloatCounter<>(initialCapacity);
      leftHeightCounter = new FloatCounter<>(initialCapacity);
      rightHeightCounter = new FloatCounter<>(initialCapacity);
    }

    // Update the counters.
    if (splitIndex < this.currentSplitIndex) {
      for (int i = splitIndex; i < this.currentSplitIndex; i++) {
        T element = get(i);
        if (element == null) {
          continue;
        }
        Rectangle boundingBox = element.getBoundingBox();
        if (boundingBox == null) {
          continue;
        }

        // Remove elements from the left half.
        leftMinXCounter.remove(boundingBox.getMinX(), element);
        leftMinYCounter.remove(boundingBox.getMinY(), element);
        leftMaxXCounter.remove(boundingBox.getMaxX(), element);
        leftMaxYCounter.remove(boundingBox.getMaxY(), element);
        leftWidthCounter.remove(boundingBox.getWidth(), element);
        leftHeightCounter.remove(boundingBox.getHeight(), element);

        // Add elements to the right half.
        rightMinXCounter.add(boundingBox.getMinX(), element);
        rightMinYCounter.add(boundingBox.getMinY(), element);
        rightMaxXCounter.add(boundingBox.getMaxX(), element);
        rightMaxYCounter.add(boundingBox.getMaxY(), element);
        rightWidthCounter.add(boundingBox.getWidth(), element);
        rightHeightCounter.add(boundingBox.getHeight(), element);
      }
    } else if (splitIndex > this.currentSplitIndex) {
      for (int i = this.currentSplitIndex; i < splitIndex; i++) {
        T element = get(i);
        if (element == null) {
          continue;
        }
        Rectangle boundingBox = element.getBoundingBox();
        if (boundingBox == null) {
          continue;
        }

        // Add elements to the left half.
        leftMinXCounter.add(boundingBox.getMinX(), element);
        leftMinYCounter.add(boundingBox.getMinY(), element);
        leftMaxXCounter.add(boundingBox.getMaxX(), element);
        leftMaxYCounter.add(boundingBox.getMaxY(), element);
        leftWidthCounter.add(boundingBox.getWidth(), element);
        leftHeightCounter.add(boundingBox.getHeight(), element);

        // Remove elements from the right half.
        rightMinXCounter.remove(boundingBox.getMinX(), element);
        rightMinYCounter.remove(boundingBox.getMinY(), element);
        rightMaxXCounter.remove(boundingBox.getMaxX(), element);
        rightMaxYCounter.remove(boundingBox.getMaxY(), element);
        rightWidthCounter.remove(boundingBox.getWidth(), element);
        rightHeightCounter.remove(boundingBox.getHeight(), element);
      }
    }

    // Set the counters of the left half.
    leftHalfToFill.setMinXCounter(leftMinXCounter);
    leftHalfToFill.setMinYCounter(leftMinYCounter);
    leftHalfToFill.setMaxXCounter(leftMaxXCounter);
    leftHalfToFill.setMaxYCounter(leftMaxYCounter);
    leftHalfToFill.setHeightCounter(leftHeightCounter);
    leftHalfToFill.setWidthCounter(leftWidthCounter);

    // Set the counters of the right half.
    rightHalfToFill.setMinXCounter(rightMinXCounter);
    rightHalfToFill.setMinYCounter(rightMinYCounter);
    rightHalfToFill.setMaxXCounter(rightMaxXCounter);
    rightHalfToFill.setMaxYCounter(rightMaxYCounter);
    rightHalfToFill.setHeightCounter(rightHeightCounter);
    rightHalfToFill.setWidthCounter(rightWidthCounter);

    this.currentLeftHalf = leftHalfToFill;
    this.currentRightHalf = rightHalfToFill;
    this.currentSplitIndex = splitIndex;
  }

  // ==========================================================================
  // Register methods.

  /**
   * Registers the given elements to this list.
   * 
   * @param elements
   *        The elements to register.
   */
  protected void registerPdfElements(Collection<? extends T> elements) {
    for (T t : elements) {
      registerPdfElement(t);
    }
  }

  /**
   * Registers the given element to this list.
   * 
   * @param element
   *        The element to register.
   */
  protected void registerPdfElement(T element) {
    if (element == null) {
      return;
    }

    Rectangle boundingBox = element.getBoundingBox();
    if (boundingBox == null) {
      return;
    }

    this.minXCounter.add(boundingBox.getMinY(), element);
    this.minYCounter.add(boundingBox.getMinY(), element);
    this.maxXCounter.add(boundingBox.getMinY(), element);
    this.maxYCounter.add(boundingBox.getMinY(), element);
    this.heightCounter.add(boundingBox.getHeight(), element);
    this.widthCounter.add(boundingBox.getWidth(), element);
    this.isBoundingBoxOutdated = true;
  }

  /**
   * Unregisters the given elements from this list.
   * 
   * @param elements
   *        The elements to unregister.
   */
  protected void unregisterPdfElements(Collection<? extends T> elements) {
    for (T t : elements) {
      unregisterPdfElement(t);
    }
  }

  /**
   * Unregisters the given element from this list.
   * 
   * @param object
   *        The element to unregister.
   */
  protected void unregisterPdfElement(Object object) {
    if (object instanceof HasBoundingBox) {
      HasBoundingBox hasBoundingBox = (HasBoundingBox) object;

      Rectangle boundingBox = hasBoundingBox.getBoundingBox();
      if (boundingBox == null) {
        return;
      }

      this.minXCounter.remove(boundingBox.getMinY(), object);
      this.minYCounter.remove(boundingBox.getMinY(), object);
      this.maxXCounter.remove(boundingBox.getMinY(), object);
      this.maxYCounter.remove(boundingBox.getMinY(), object);
      this.heightCounter.remove(boundingBox.getHeight(), object);
      this.widthCounter.remove(boundingBox.getWidth(), object);
      this.isBoundingBoxOutdated = true;
    }
  }

  // ==========================================================================

  @Override
  public Rectangle getBoundingBox() {
    if (this.boundingBox == null || this.isBoundingBoxOutdated) {
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

  // ==========================================================================

  @Override
  public float getMostCommonHeight() {
    return this.heightCounter.getMostCommonFloat();
  }

  @Override
  public Set<T> getElementsWithMostCommonHeight() {
    return this.heightCounter.getElementsWithMostCommonFloat();
  }

  @Override
  public float getAverageHeight() {
    return this.heightCounter.getAverageFloat();
  }

  // ==========================================================================

  @Override
  public float getMostCommonWidth() {
    return this.widthCounter.getMostCommonFloat();
  }

  @Override
  public Set<T> getElementsWithMostCommonWidth() {
    return this.widthCounter.getElementsWithMostCommonFloat();
  }

  @Override
  public float getAverageWidth() {
    return this.widthCounter.getAverageFloat();
  }

  // ==========================================================================

  @Override
  public float getSmallestMinX() {
    return this.minXCounter.getSmallestFloat();
  }

  @Override
  public Set<T> getElementsWithSmallestMinX() {
    return this.minXCounter.getElementsWithSmallestFloat();
  }

  // ==========================================================================

  @Override
  public float getSmallestMinY() {
    return this.minYCounter.getSmallestFloat();
  }

  @Override
  public Set<T> getElementsWithSmallestMinY() {
    return this.minYCounter.getElementsWithSmallestFloat();
  }

  // ==========================================================================

  @Override
  public float getLargestMaxX() {
    return this.maxXCounter.getLargestFloat();
  }

  @Override
  public Set<T> getElementsWithLargestMaxX() {
    return this.maxXCounter.getElementsWithLargestFloat();
  }

  // ==========================================================================

  @Override
  public float getLargestMaxY() {
    return this.maxYCounter.getLargestFloat();
  }

  @Override
  public Set<T> getElementsWithLargestMaxY() {
    return this.maxYCounter.getElementsWithLargestFloat();
  }

  // ==========================================================================

  /**
   * A sublist of PDF elements.
   * 
   * @author Claudius Korzen
   *
   * @param <S>
   *        The concrete type of the elements in this List.
   */
  class PdfElementListView<S extends PdfElement> extends ArrayList<S>
      implements PdfElementList<S>, RandomAccess {
    /**
     * The serial id.
     */
    private static final long serialVersionUID = 4550840178348993069L;

    /**
     * The parent list.
     */
    protected final PdfElementList<S> parent;

    /**
     * The offset in the parent list.
     */
    protected final int offset;

    /**
     * The size of this list.
     */
    protected final int size;

    /**
     * The counter for the minX values.
     */
    protected FloatCounter<S> minXCounter;

    /**
     * The counter for the minY values.
     */
    protected FloatCounter<S> minYCounter;

    /**
     * The counter for the maxX values.
     */
    protected FloatCounter<S> maxXCounter;

    /**
     * The counter for the maxY values.
     */
    protected FloatCounter<S> maxYCounter;

    /**
     * The counter for the height values.
     */
    protected FloatCounter<S> heightCounter;

    /**
     * The counter for the width values.
     */
    protected FloatCounter<S> widthCounter;

    /**
     * The bounding box around the elements in this list.
     */
    protected Rectangle boundingBox;

    /**
     * The index of the previous split.
     */
    protected int currentSplitIndex;

    /**
     * The previous portion between index 0 and previousSplitIndex.
     */
    protected PdfElementListView<S> currentLeftHalf;

    /**
     * The previous portion between previousSplitIndex and index this.size().
     */
    protected PdfElementListView<S> currentRightHalf;

    // ========================================================================
    // Constructors.

    /**
     * Creates a new sublist based on the given parent list.
     * 
     * @param parent
     *        The parent list.
     * @param fromIndex
     *        The start index.
     * @param toIndex
     *        The end index.
     */
    PdfElementListView(PdfElementList<S> parent, int fromIndex, int toIndex) {
      this.parent = parent;
      this.offset = fromIndex;
      this.size = toIndex - fromIndex;
    }

    // ========================================================================
    // Add methods.

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
    // set methods.

    @Override
    public S set(int index, S element) {
      throw new UnsupportedOperationException();
    }

    // ========================================================================
    // Remove methods.

    @Override
    public S remove(int index) {
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
    public void clear() {
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
          return this.cursor != PdfElementListView.this.size;
        }

        @Override
        public S next() {
          if (this.cursor >= PdfElementListView.this.size) {
            throw new NoSuchElementException();
          }
          return PdfElementListView.this.get(this.cursor++);
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
          return PdfElementListView.this.get(this.cursor--);
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

    @Override
    public List<S> subList(int fromIndex, int toIndex) {
      throw new UnsupportedOperationException();
    }

    // ========================================================================
    // Custom methods.

    /**
     * Returns the height counter of this view.
     * 
     * @return The height counter of this view.
     */
    public FloatCounter<S> getHeightCounter() {
      return this.heightCounter;
    }

    /**
     * Sets the height counter of this view.
     * 
     * @param counter
     *        The height counter of this view.
     */
    public void setHeightCounter(FloatCounter<S> counter) {
      this.heightCounter = counter;
    }

    // ========================================================================

    /**
     * Returns the width counter of this view.
     * 
     * @return The width counter of this view.
     */
    public FloatCounter<S> getWidthCounter() {
      return this.widthCounter;
    }

    /**
     * Sets the width counter of this view.
     * 
     * @param counter
     *        The width counter of this view.
     */
    public void setWidthCounter(FloatCounter<S> counter) {
      this.widthCounter = counter;
    }

    // ========================================================================

    /**
     * Returns the minX counter of this view.
     * 
     * @return The minX counter of this view.
     */
    public FloatCounter<S> getMinXCounter() {
      return this.minXCounter;
    }

    /**
     * Sets the minX counter of this view.
     * 
     * @param counter
     *        The minX counter of this view.
     */
    public void setMinXCounter(FloatCounter<S> counter) {
      this.minXCounter = counter;
    }

    // ========================================================================

    /**
     * Returns the minY counter of this view.
     * 
     * @return The minY counter of this view.
     */
    public FloatCounter<S> getMinYCounter() {
      return this.minYCounter;
    }

    /**
     * Sets the minY counter of this view.
     * 
     * @param counter
     *        The minY counter of this view.
     */
    public void setMinYCounter(FloatCounter<S> counter) {
      this.minYCounter = counter;
    }

    // ========================================================================

    /**
     * Returns the maxX counter of this view.
     * 
     * @return The maxX counter of this view.
     */
    public FloatCounter<S> getMaxXCounter() {
      return this.maxXCounter;
    }

    /**
     * Sets the maxX counter of this view.
     * 
     * @param counter
     *        The maxX counter of this view.
     */
    public void setMaxXCounter(FloatCounter<S> counter) {
      this.maxXCounter = counter;
    }

    // ========================================================================

    /**
     * Returns the maxY counter of this view.
     * 
     * @return The maxY counter of this view.
     */
    public FloatCounter<S> getMaxYCounter() {
      return this.maxYCounter;
    }

    /**
     * Sets the maxY counter of this view.
     * 
     * @param counter
     *        The maxY counter of this view.
     */
    public void setMaxYCounter(FloatCounter<S> counter) {
      this.maxYCounter = counter;
    }

    // ========================================================================

    @Override
    public List<? extends PdfElementList<S>> split(int splitIndex) {
      PdfElementListView<S> leftHalf = new PdfElementListView<>(this, 0,
          splitIndex);
      PdfElementListView<S> rightHalf = new PdfElementListView<>(this,
          splitIndex, this.size());
      split(splitIndex, leftHalf, rightHalf);
      return Arrays.asList(leftHalf, rightHalf);
    }

    /**
     * Splits this list at the given index into two halves. Both halves are
     * views of the related portion of the list, that is (1) the portion
     * between index 0, inclusive, and splitIndex, exclusive; and (2) the
     * portion between splitIndex, inclusive, and this.size(), exclusive.
     * 
     * @param splitIndex
     *        The index where to split this list.
     * @param leftHalfToFill
     *        The left half to populate with the counters.
     * @param rightHalfToFill
     *        The left half to populate with the counters.
     */
    public void split(int splitIndex, PdfElementListView<S> leftHalfToFill,
        PdfElementListView<S> rightHalfToFill) {
      FloatCounter<S> leftMinXCounter, rightMinXCounter;
      FloatCounter<S> leftMinYCounter, rightMinYCounter;
      FloatCounter<S> leftMaxXCounter, rightMaxXCounter;
      FloatCounter<S> leftMaxYCounter, rightMaxYCounter;
      FloatCounter<S> leftWidthCounter, rightWidthCounter;
      FloatCounter<S> leftHeightCounter, rightHeightCounter;

      if (this.currentLeftHalf != null && this.currentRightHalf != null) {
        leftMinXCounter = this.currentLeftHalf.getMinXCounter();
        rightMinXCounter = this.currentRightHalf.getMinXCounter();
        leftMinYCounter = this.currentLeftHalf.getMinYCounter();
        rightMinYCounter = this.currentRightHalf.getMinYCounter();
        leftMaxXCounter = this.currentLeftHalf.getMaxXCounter();
        rightMaxXCounter = this.currentRightHalf.getMaxXCounter();
        leftMaxYCounter = this.currentLeftHalf.getMaxYCounter();
        rightMaxYCounter = this.currentRightHalf.getMaxYCounter();
        leftWidthCounter = this.currentLeftHalf.getWidthCounter();
        rightWidthCounter = this.currentRightHalf.getWidthCounter();
        leftHeightCounter = this.currentLeftHalf.getHeightCounter();
        rightHeightCounter = this.currentRightHalf.getHeightCounter();
      } else {
        int initialCapacity = 16; // TODO
        leftMinXCounter = new FloatCounter<>(initialCapacity);
        rightMinXCounter = new FloatCounter<>(initialCapacity);
        leftMinYCounter = new FloatCounter<>(initialCapacity);
        rightMinYCounter = new FloatCounter<>(initialCapacity);
        leftMaxXCounter = new FloatCounter<>(initialCapacity);
        rightMaxXCounter = new FloatCounter<>(initialCapacity);
        leftMaxYCounter = new FloatCounter<>(initialCapacity);
        rightMaxYCounter = new FloatCounter<>(initialCapacity);
        leftWidthCounter = new FloatCounter<>(initialCapacity);
        rightWidthCounter = new FloatCounter<>(initialCapacity);
        leftHeightCounter = new FloatCounter<>(initialCapacity);
        rightHeightCounter = new FloatCounter<>(initialCapacity);
      }

      // Update the counters.
      if (splitIndex < this.currentSplitIndex) {
        for (int i = splitIndex; i < this.currentSplitIndex; i++) {
          S element = get(i);
          if (element == null) {
            continue;
          }
          Rectangle boundingBox = element.getBoundingBox();
          if (boundingBox == null) {
            continue;
          }

          // Remove elements from the left half.
          leftMinXCounter.remove(boundingBox.getMinX(), element);
          leftMinYCounter.remove(boundingBox.getMinY(), element);
          leftMaxXCounter.remove(boundingBox.getMaxX(), element);
          leftMaxYCounter.remove(boundingBox.getMaxY(), element);
          leftWidthCounter.remove(boundingBox.getWidth(), element);
          leftHeightCounter.remove(boundingBox.getHeight(), element);

          // Add elements to the right half.
          rightMinXCounter.add(boundingBox.getMinX(), element);
          rightMinYCounter.add(boundingBox.getMinY(), element);
          rightMaxXCounter.add(boundingBox.getMaxX(), element);
          rightMaxYCounter.add(boundingBox.getMaxY(), element);
          rightWidthCounter.add(boundingBox.getWidth(), element);
          rightHeightCounter.add(boundingBox.getHeight(), element);
        }
      } else if (splitIndex > this.currentSplitIndex) {
        for (int i = this.currentSplitIndex; i < splitIndex; i++) {
          S element = get(i);
          if (element == null) {
            continue;
          }
          Rectangle boundingBox = element.getBoundingBox();
          if (boundingBox == null) {
            continue;
          }

          // Add elements to the left half.
          leftMinXCounter.add(boundingBox.getMinX(), element);
          leftMinYCounter.add(boundingBox.getMinY(), element);
          leftMaxXCounter.add(boundingBox.getMaxX(), element);
          leftMaxYCounter.add(boundingBox.getMaxY(), element);
          leftWidthCounter.add(boundingBox.getWidth(), element);
          leftHeightCounter.add(boundingBox.getHeight(), element);

          // Remove elements from the right half.
          rightMinXCounter.remove(boundingBox.getMinX(), element);
          rightMinYCounter.remove(boundingBox.getMinY(), element);
          rightMaxXCounter.remove(boundingBox.getMaxX(), element);
          rightMaxYCounter.remove(boundingBox.getMaxY(), element);
          rightWidthCounter.remove(boundingBox.getWidth(), element);
          rightHeightCounter.remove(boundingBox.getHeight(), element);
        }
      }

      // Set the counters of the left half.
      leftHalfToFill.setMinXCounter(leftMinXCounter);
      leftHalfToFill.setMinYCounter(leftMinYCounter);
      leftHalfToFill.setMaxXCounter(leftMaxXCounter);
      leftHalfToFill.setMaxYCounter(leftMaxYCounter);
      leftHalfToFill.setHeightCounter(leftHeightCounter);
      leftHalfToFill.setWidthCounter(leftWidthCounter);

      // Set the counters of the right half.
      rightHalfToFill.setMinXCounter(rightMinXCounter);
      rightHalfToFill.setMinYCounter(rightMinYCounter);
      rightHalfToFill.setMaxXCounter(rightMaxXCounter);
      rightHalfToFill.setMaxYCounter(rightMaxYCounter);
      rightHalfToFill.setHeightCounter(rightHeightCounter);
      rightHalfToFill.setWidthCounter(rightWidthCounter);

      this.currentLeftHalf = leftHalfToFill;
      this.currentRightHalf = rightHalfToFill;
      this.currentSplitIndex = splitIndex;
    }

    // ========================================================================

    @Override
    public Rectangle getBoundingBox() {
      if (this.boundingBox == null) {
        this.boundingBox = PlainPdfElementList.this.rectangleFactory.create();
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

    // ========================================================================

    @Override
    public float getMostCommonHeight() {
      return this.heightCounter.getMostCommonFloat();
    }

    @Override
    public Set<S> getElementsWithMostCommonHeight() {
      return this.heightCounter.getElementsWithMostCommonFloat();
    }

    @Override
    public float getAverageHeight() {
      return this.heightCounter.getAverageFloat();
    }

    // ========================================================================

    @Override
    public float getMostCommonWidth() {
      return this.widthCounter.getMostCommonFloat();
    }

    @Override
    public Set<S> getElementsWithMostCommonWidth() {
      return this.widthCounter.getElementsWithMostCommonFloat();
    }

    @Override
    public float getAverageWidth() {
      return this.widthCounter.getAverageFloat();
    }

    // ========================================================================

    @Override
    public float getSmallestMinX() {
      return this.minXCounter.getSmallestFloat();
    }

    @Override
    public Set<S> getElementsWithSmallestMinX() {
      return this.minXCounter.getElementsWithSmallestFloat();
    }

    // ========================================================================

    @Override
    public float getSmallestMinY() {
      return this.minYCounter.getSmallestFloat();
    }

    @Override
    public Set<S> getElementsWithSmallestMinY() {
      return this.minYCounter.getElementsWithSmallestFloat();
    }

    // ========================================================================

    @Override
    public float getLargestMaxX() {
      return this.maxXCounter.getLargestFloat();
    }

    @Override
    public Set<S> getElementsWithLargestMaxX() {
      return this.maxXCounter.getElementsWithLargestFloat();
    }

    // ========================================================================

    @Override
    public float getLargestMaxY() {
      return this.maxYCounter.getLargestFloat();
    }

    @Override
    public Set<S> getElementsWithLargestMaxY() {
      return this.maxYCounter.getElementsWithLargestFloat();
    }
  }
}
