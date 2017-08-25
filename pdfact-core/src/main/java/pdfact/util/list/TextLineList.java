package pdfact.util.list;

import java.util.List;

import pdfact.model.TextLine;

//TODO: Is TextLineList still needed?

/**
 * A list of text lines in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface TextLineList extends ElementList<TextLine> {
  /**
   * Splits this list at the given index into two halves. Both halves are views
   * of the related portion of the list, that is (1) the portion between index
   * 0, inclusive, and splitIndex, exclusive; and (2) the portion between
   * splitIndex, inclusive, and this.size(), exclusive.
   * 
   * @param splitIndex
   *        The index where to split this list.
   * 
   * @return A list of length 2, containing the two resulting views.
   */
  List<TextLineList> cut(int splitIndex);
  
  /**
   * The factory to create instances of {@link TextLineList}.
   * 
   * @author Claudius Korzen
   */
  public interface TextLineListFactory {
    /**
     * Creates a new instance of {@link TextLineList}.
     * 
     * @return A new instance of {@link TextLineList}.
     */
    TextLineList create();

    /**
     * Creates a new instance of {@link TextLineList}.
     * 
     * @param initialCapacity
     *        The initial capacity of this list.
     * 
     * @return A new instance of {@link TextLineList}.
     */
    TextLineList create(int initialCapacity);
  }
}
