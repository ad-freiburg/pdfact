package pdfact.models.plain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfElementList;
import pdfact.models.PdfSinglePositionElement;
import pdfact.utils.sort.Quicksort;

/**
 * A plain implementation of {@link PdfElementList}.
 * 
 * @param <T>
 *        The type of the PDF elements in this list.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfElementList<T extends PdfSinglePositionElement>
    implements PdfElementList<T> {
  /**
   * The underlying list.
   */
  protected ArrayList<T> list;

  // ==========================================================================

  /**
   * Creates an empty list.
   */
  @AssistedInject
  public PlainPdfElementList() {
    this.list = new ArrayList<>();
  }

  /**
   * Creates an empty list with the given initial capacity.
   *
   * @param initialCapacity
   *        The initial capacity of the list
   */
  @AssistedInject
  public PlainPdfElementList(@Assisted int initialCapacity) {
    this.list = new ArrayList<>(initialCapacity);
  }

  // ==========================================================================

  @Override
  public T get(int index) {
    return this.list.get(index);
  }

  @Override
  public T set(int index, T element) {
    return this.list.set(index, element);
  }

  // ==========================================================================

  @Override
  public boolean contains(Object o) {
    return this.list.contains(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return this.list.containsAll(c);
  }

  @Override
  public int indexOf(Object o) {
    return this.list.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return this.list.lastIndexOf(o);
  }

  // ==========================================================================

  @Override
  public boolean add(T e) {
    return this.list.add(e);
  }

  @Override
  public void add(int index, T element) {
    this.list.add(index, element);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return this.list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    return this.list.addAll(index, c);
  }

  // ==========================================================================

  @Override
  public boolean remove(Object o) {
    return this.list.remove(o);
  }

  @Override
  public T remove(int index) {
    return this.list.remove(index);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return this.list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return this.list.retainAll(c);
  }

  @Override
  public void clear() {
    this.list.clear();
  }

  // ==========================================================================

  @Override
  public int size() {
    return this.list.size();
  }

  @Override
  public boolean isEmpty() {
    return this.list.isEmpty();
  }

  // ==========================================================================

  @Override
  public Object[] toArray() {
    return this.list.toArray();
  }

  @Override
  public <X> X[] toArray(X[] a) {
    return this.list.toArray(a);
  }

  // ==========================================================================

  @Override
  public Iterator<T> iterator() {
    return this.list.iterator();
  }

  @Override
  public ListIterator<T> listIterator() {
    return this.list.listIterator();
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return this.list.listIterator(index);
  }

  // ==========================================================================

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return this.list.subList(fromIndex, toIndex);
  }

  // ==========================================================================

  @Override
  public void swap(int i, int j) {
    T first = this.list.get(i);
    T second = this.list.get(j);
    this.list.set(i, second);
    this.list.set(j, first);
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

  /**
   * A view of a PdfElementList.
   * 
   * @author Claudius Korzen
   *
   * @param <S>
   *        The type of the PDF elements in this list.
   */
  class PdfElementView<S extends PdfSinglePositionElement>
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
     * The left boundary of this view in the parent list.
     */
    protected final int from;

    /**
     * The right boundary of this view in the parent list.
     */
    protected final int to;
    
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
      this.from = fromIndex;
      this.to = toIndex;
      this.size = toIndex - fromIndex;
    }

    // ========================================================================

    @Override
    public S get(int index) {
      return this.parent.get(this.from + index);
    }

    @Override
    public S set(int index, S element) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    // ========================================================================

    @Override
    public boolean contains(Object o) {
      return indexOf(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      for (Object o : c) {
        if (!contains(o)) {
          return false;
        }
      }
      return true;
    }

    @Override
    public int indexOf(Object o) {
      for (int i = this.from; i < this.size; i++) {
        if (o.equals(get(i))) {
          return i;
        }
      }
      return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
      for (int i = this.size - 1; i >= this.from; i--) {
        if (o.equals(get(i))) {
          return i;
        }
      }
      return -1;
    }

    // ========================================================================

    @Override
    public boolean add(S e) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, S element) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends S> c) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends S> c) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    // ========================================================================

    @Override
    public boolean remove(Object o) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    @Override
    public S remove(int index) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    // ========================================================================

    @Override
    public int size() {
      return this.size;
    }

    @Override
    public boolean isEmpty() {
      return this.size == 0;
    }

    // ========================================================================

    @Override
    public Object[] toArray() {
      return Arrays.copyOfRange(this.parent.toArray(), this.from, this.to);
    }

    @Override
    public <X> X[] toArray(X[] a) {
      throw new UnsupportedOperationException();
    }

    // ========================================================================

    @Override
    public Iterator<S> iterator() {
      return listIterator(0);
    }

    @Override
    public ListIterator<S> listIterator() {
      return listIterator(0);
    }

    @Override
    public ListIterator<S> listIterator(int index) {
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

    // ========================================================================

    @Override
    public List<S> subList(int from, int to) {
      return this.parent.subList(this.from + from, this.from + to);
    }

    // ========================================================================

    @Override
    public void swap(int i, int j) {
      this.parent.swap(this.from + i, this.from + j);
    }

    @Override
    public void sort(Comparator<? super S> c) {
      Quicksort.sort(this, c);
    }

    @Override
    public List<? extends PdfElementList<S>> cut(int index) {
      // Create new views.
      int left = this.from;
      int cut = this.from + index;
      int right = this.from + size();
      PdfElementView<S> v1 = new PdfElementView<S>(this.parent, left, cut);
      PdfElementView<S> v2 = new PdfElementView<S>(this.parent, cut, right);
      return Arrays.asList(v1, v2);
    }
  }
}