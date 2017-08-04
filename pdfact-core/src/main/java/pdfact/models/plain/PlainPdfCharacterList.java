package pdfact.models.plain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfCharacterList;

// TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfCharacterList}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacterList extends PlainPdfElementList<PdfCharacter>
    implements PdfCharacterList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 6187582288718513001L;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new, empty list of characters.
   */
  @AssistedInject
  public PlainPdfCharacterList() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates a new, empty list of characters.
   * 
   * @param initialCapacity
   *        The initial capacity of this list.
   */
  @AssistedInject
  public PlainPdfCharacterList(@Assisted int initialCapacity) {
    super(initialCapacity);
  }

  // ==========================================================================

  @Override
  public List<PdfCharacterList> cut(int index) {
    PdfCharacterView left = new PdfCharacterView(this, 0, index);
    PdfCharacterView right = new PdfCharacterView(this, index, this.size());
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
   * A view of a PdfCharacterList.
   * 
   * @author Claudius Korzen
   */
  class PdfCharacterView extends PlainPdfCharacterList 
      implements PdfCharacterList {
    /**
     * The serial id.
     */
    protected static final long serialVersionUID = 884367879377788123L;

    /**
     * The parent list.
     */
    protected final PdfCharacterList parent;

    /**
     * The offset of this view in the parent list.
     */
    protected final int offset;

    /**
     * The size of this list (the number of elements in the list).
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
    PdfCharacterView(PdfCharacterList parent, int fromIndex, int toIndex) {
      super();
      this.parent = parent;
      this.offset = fromIndex;
      this.size = toIndex - fromIndex;
    }

    // ========================================================================
    // Don't allow to add elements to views.

    @Override
    public boolean add(PdfCharacter c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, PdfCharacter c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends PdfCharacter> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends PdfCharacter> c) {
      throw new UnsupportedOperationException();
    }

    // ========================================================================
    // Iterators.

    @Override
    public Iterator<PdfCharacter> iterator() {
      return listIterator(0);
    }

    @Override
    public ListIterator<PdfCharacter> listIterator(final int index) {
      return new ListIterator<PdfCharacter>() {
        int cursor = index;

        @Override
        public boolean hasNext() {
          return this.cursor != PdfCharacterView.this.size;
        }

        @Override
        public PdfCharacter next() {
          if (this.cursor >= PdfCharacterView.this.size) {
            throw new NoSuchElementException();
          }
          return PdfCharacterView.this.get(this.cursor++);
        }

        @Override
        public boolean hasPrevious() {
          return this.cursor != 0;
        }

        @Override
        public PdfCharacter previous() {
          if (this.cursor - 1 < 0) {
            throw new NoSuchElementException();
          }
          return PdfCharacterView.this.get(this.cursor--);
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
        public void set(PdfCharacter c) {
          throw new UnsupportedOperationException();
        }

        @Override
        public void add(PdfCharacter c) {
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override
    public Spliterator<PdfCharacter> spliterator() {
      throw new UnsupportedOperationException();
    }

    // ========================================================================
    // Getters.

    @Override
    public PdfCharacter get(int index) {
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
    public List<PdfCharacterList> cut(int i) {
      // Create new views.
      int left = this.offset;
      int cut = this.offset + i;
      int right = this.offset + size();
      PdfCharacterView v1 = new PdfCharacterView(this.parent, left, cut);
      PdfCharacterView v2 = new PdfCharacterView(this.parent, cut, right);
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