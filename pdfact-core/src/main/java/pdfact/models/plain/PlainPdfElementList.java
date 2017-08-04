package pdfact.models.plain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;

import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfElement;
import pdfact.models.PdfElementList;
import pdfact.utils.sort.Quicksort;

// TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfElementList}.
 * 
 * @param <T>
 *        The type of the PDF elements in this list.
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

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new, empty list.
   */
  @AssistedInject
  public PlainPdfElementList() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new, empty list.
   * 
   * @param initialCapacity
   *        The initial capacity of this list.
   */
  @AssistedInject
  public PlainPdfElementList(int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

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

  @Override
  public List<? extends PdfElementList<T>> cut(int index) {
    PdfElementView<T> left = new PdfElementView<>(this, 0, index);
    PdfElementView<T> right = new PdfElementView<>(this, index, this.size());
    return Arrays.asList(left, right);
  }

  // ==========================================================================

  @Override
  public String toString() {
    return super.toString();
  }

  // ==========================================================================
  
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
   *        The type of the PDF elements in this list.
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
      super();
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

    // ========================================================================
    
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