package pdfact.core.util.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import pdfact.core.model.Element;

// TODO: Accelerate the cut method in PdfElementList.

/**
 * A list of PDF elements.
 * 
 * @param <T> The type of the PDF elements.
 * 
 * @author Claudius Korzen
 */
public class ElementList<T extends Element> implements List<T> {
  /**
   * The underlying list.
   */
  protected final ArrayList<T> list;

  /**
   * Creates an empty list.
   */
  public ElementList() {
    this.list = new ArrayList<>();
  }

  /**
   * Creates an empty list with the given initial capacity.
   *
   * @param initialCapacity The initial capacity of the list
   */
  public ElementList(int initialCapacity) {
    this.list = new ArrayList<>(initialCapacity);
  }

  // ==============================================================================================

  /**
   * Returns the first element in this list.
   * 
   * @return The first element in this list or null if this list is empty.
   */
  public T get(int index) {
    return this.list.get(index);
  }

  /**
   * Returns the last element in this list.
   * 
   * @return The last element in this list or null if this list is empty.
   */
  public T set(int index, T element) {
    return this.list.set(index, element);
  }

  // ==============================================================================================

  /**
   * Swaps the elements at index i and j.
   * 
   * @param i The index of the first element to swap.
   * @param j The index of the second element to swap.
   */
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

  /**
   * Splits this list at the given index into two halves. Both halves are views of
   * the related portion of the list, that is (1) the portion between index 0,
   * inclusive, and splitIndex, exclusive; and (2) the portion between splitIndex,
   * inclusive, and this.size(), exclusive.
   * 
   * @param splitIndex The index where to split this list.
   * @return A list of length 2, containing the two resulting views.
   */
  public List<ElementList<T>> cut(int index) {
    ElementListView<T> left = new ElementListView<>(this, 0, index);
    ElementListView<T> right = new ElementListView<>(this, index, this.size());
    return Arrays.asList(left, right);
  }

  // ==============================================================================================

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

  // ==============================================================================================

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

  // ==============================================================================================

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

  // ==============================================================================================

  @Override
  public int size() {
    return this.list.size();
  }

  @Override
  public boolean isEmpty() {
    return this.list.isEmpty();
  }

  // ==============================================================================================

  @Override
  public Object[] toArray() {
    return this.list.toArray();
  }

  @Override
  public <X> X[] toArray(X[] a) {
    return this.list.toArray(a);
  }

  // ==============================================================================================

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

  // ==============================================================================================

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return new ElementListView<>(this, fromIndex, toIndex);
  }

  // ==============================================================================================

  /**
   * Returns the first element in this list.
   * 
   * @return The first element in this list.
   */
  public T getFirstElement() {
    if (isEmpty()) {
      return null;
    }
    return get(0);
  }

  /**
   * Returns the last element in this list.
   * 
   * @return The last element in this list.
   */
  public T getLastElement() {
    if (isEmpty()) {
      return null;
    }
    return get(size() - 1);
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    return this.list.equals(other);
  }

  @Override
  public int hashCode() {
    return this.list.hashCode();
  }

  // ==============================================================================================

  /**
   * A view of a PdfElementList.
   * 
   * @author Claudius Korzen
   *
   * @param <S> The type of the PDF elements in this list.
   */
  class ElementListView<S extends Element> extends ElementList<S> {
    /**
     * The serial id.
     */
    protected static final long serialVersionUID = 884367879377788123L;

    /**
     * The parent list.
     */
    protected final ElementList<S> parent;

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

    // ==============================================================================================
    // Constructors.

    /**
     * Creates a new view based on the given parent list.
     * 
     * @param parent    The parent list.
     * @param fromIndex The start index in the parent list.
     * @param toIndex   The end index in the parent list.
     */
    ElementListView(ElementList<S> parent, int fromIndex, int toIndex) {
      super();
      this.parent = parent;
      this.from = fromIndex;
      this.to = toIndex;
      this.size = toIndex - fromIndex;
    }

    // ==============================================================================================

    @Override
    public S get(int index) {
      return this.parent.get(this.from + index);
    }

    @Override
    public S set(int index, S element) {
      // Don't allow to change the content of the list.
      throw new UnsupportedOperationException();
    }

    // ==============================================================================================

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

    // ==============================================================================================

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

    // ==============================================================================================

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

    // ==============================================================================================

    @Override
    public int size() {
      return this.size;
    }

    @Override
    public boolean isEmpty() {
      return this.size == 0;
    }

    // ==============================================================================================

    @Override
    public Object[] toArray() {
      return Arrays.copyOfRange(this.parent.toArray(), this.from, this.to);
    }

    @Override
    public <X> X[] toArray(X[] a) {
      throw new UnsupportedOperationException();
    }

    // ==============================================================================================

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
          return this.cursor != ElementListView.this.size;
        }

        @Override
        public S next() {
          if (this.cursor >= ElementListView.this.size) {
            throw new NoSuchElementException();
          }
          return ElementListView.this.get(this.cursor++);
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
          return ElementListView.this.get(this.cursor--);
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

    // ============================================================================================

    // @Override
    public List<S> subList(int from, int to) {
      return this.parent.subList(this.from + from, this.from + to);
    }

    // ============================================================================================

    @Override
    public S getFirstElement() {
      if (isEmpty()) {
        return null;
      }
      return get(0);
    }

    @Override
    public S getLastElement() {
      if (isEmpty()) {
        return null;
      }
      return get(size() - 1);
    }

    // ============================================================================================

    @Override
    public void swap(int i, int j) {
      this.parent.swap(this.from + i, this.from + j);
    }

    @Override
    public void sort(Comparator<? super S> c) {
      Quicksort.sort(this, c);
    }

    @Override
    public List<ElementList<S>> cut(int index) {
      // Create new views.
      int left = this.from;
      int cut = this.from + index;
      int right = this.from + size();
      ElementListView<S> v1 = new ElementListView<S>(this.parent, left, cut);
      ElementListView<S> v2 = new ElementListView<S>(this.parent, cut, right);
      return Arrays.asList(v1, v2);
    }

    // ==============================================================================================

    @Override
    public boolean equals(Object other) {
      if (other == this) {
        return true;
      }
      if (!(other instanceof List)) {
        return false;
      }
      ListIterator<S> e1 = listIterator();
      ListIterator<?> e2 = ((List<?>) other).listIterator();
      while (e1.hasNext() && e2.hasNext()) {
        S o1 = e1.next();
        Object o2 = e2.next();
        if (!(o1 == null ? o2 == null : o1.equals(o2))) {
          return false;
        }
      }
      return !(e1.hasNext() || e2.hasNext());
    }

    @Override
    public int hashCode() {
      int hashCode = 1;
      for (S e : this) {
        hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
      }
      return hashCode;
    }
  }
}

/**
 * A custom implementation of Quicksort.
 * 
 * @author Claudius Korzen
 */
class Quicksort {
  /**
   * Sorts the given array using QuickSort.
   * 
   * @param list The list to sort.
   * @param c    The comparator to use.
   */
  public static <T extends Element> void sort(ElementList<T> list, Comparator<? super T> c) {
    // Do nothing, if the array is not set.
    if (list == null) {
      return;
    }

    // Do nothing, if the array contains less than 2 elements.
    if (list.size() < 2) {
      return;
    }

    quickSortRecursive(list, c, 0, list.size() - 1);
  }

  // ___________________________________________________________________________

  /**
   * Recursive method for Quicksort. Each call gets the full array plus two
   * variables specifying which range of the array should be sorted, where l is
   * the index of the leftmost element in the range, and r is the index of the
   * rightmost element in the range. The public method above just calls this one
   * with l = 0 and r = n - 1, where n is the size of the array.
   * 
   * @param list The list to process.
   * @param c    The comparator to use.
   * @param l    The start index.
   * @param r    The end index.
   */
  protected static <T extends Element> void quickSortRecursive(ElementList<T> list, 
      Comparator<? super T> c, int l,
      int r) {
    // Do nothing, if the range contains no more than 1 element.
    int index = quickSortDivide(list, c, l, r);

    if (index > l + 1) {
      quickSortRecursive(list, c, l, index - 1);
    }
    if (index < r - 1) {
      quickSortRecursive(list, c, index + 1, r);
    }
  }

  /**
   * Method for dividing the given range of the given array into two parts such
   * that all elements in the left part are smaller than all elements in the right
   * part. Returns the index m of the dividing element, that is left part
   * array[l..m] and right part = array[m+1..r].
   * 
   * @param list The list to process.
   * @param c    The comparator to use.
   * @param l    The start index.
   * @param r    The end index.
   * @return The index of dividing element.
   */
  protected static <T extends Element> int quickSortDivide(ElementList<T> list, 
      Comparator<? super T> c, int l, int r) {
    // Define the two pointers i and j.
    int i = l;
    int j = r - 1;

    // Choose the pivot index: Take the leftmost element per default.
    int pivotIndex = r;
    T pivot = list.get(pivotIndex);

    // Swap the pivot to the right.
    list.swap(pivotIndex, r);

    while (true) {
      // Iterate the array from the left and search for the first element which
      // is larger than the pivot.
      while (i <= j && c.compare(list.get(i), pivot) < 0) {
        i++;
      }

      // Iterate the array from the right and search for the first element
      // which is smaller than the pivot.
      while (i <= j && c.compare(list.get(j), pivot) >= 0) {
        j--;
      }

      if (i > j) {
        break;
      }

      // Swap the element in the left array which is larger than the pivot
      // with the element in the right array which is smaller than the pivot.
      list.swap(i, j);
    }

    // Swap the pivot back.
    list.swap(r, i);

    return i;
  }
}
