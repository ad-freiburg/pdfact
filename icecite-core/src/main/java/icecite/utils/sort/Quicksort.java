package icecite.utils.sort;

import java.util.Comparator;
import java.util.Random;

import icecite.models.PdfElement;
import icecite.models.PdfElementList;

/**
 * Implementation of Quicksort.
 * 
 * @author Claudius Korzen
 */
public class Quicksort {
  /**
   * The random generator.
   */
  protected static Random random = new Random();

  /**
   * Identifier for the method for choosing the pivot element. For example, 1 =
   * always pick first position of range, 2 = pick random element from range.
   */
  protected static final int PIVOT_METHOD = 2;

  /**
   * Sorts the given array using QuickSort.
   * 
   * @param list
   *        The list to sort.
   * @param c
   *        The comparator to use.
   */
  public static <T extends PdfElement> void sort(PdfElementList<T> list,
      Comparator<? super T> c) {
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
   * rightmost element in the range. The public method above just calls this
   * one with l = 0 and r = n - 1, where n is the size of the array.
   * 
   * @param list
   *        The list to process.
   * @param c
   *        The comparator to use.
   * @param l
   *        The start index.
   * @param r
   *        The end index.
   */
  protected static <T extends PdfElement> void quickSortRecursive(
      PdfElementList<T> list, Comparator<? super T> c, int l, int r) {
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
   * that all elements in the left part are smaller than all elements in the
   * right part. Returns the index m of the dividing element, that is left part
   * array[l..m] and right part = array[m+1..r].
   * 
   * @param list
   *        The list to process.
   * @param c
   *        The comparator to use.
   * @param l
   *        The start index.
   * @param r
   *        The end index.
   * @return The index of dividing element.
   */
  protected static <T extends PdfElement> int quickSortDivide(
      PdfElementList<T> list, Comparator<? super T> c, int l, int r) {
    // Define the two pointers i and j.
    int i = l;
    int j = r - 1;

    // Choose the pivot index: Take the leftmost element per default.
    int pivotIndex = getRandomNumber(l, r);
    // int pivotIndex = 0;
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

  /**
   * Returns a random number in the interval [min, max].
   * 
   * @param min
   *        The start of interval.
   * @param max
   *        The end of interval.
   * @return A random number in [min, max].
   */
  protected static int getRandomNumber(int min, int max) {
    return random.nextInt((max - min) + 1) + min;
  }
}
