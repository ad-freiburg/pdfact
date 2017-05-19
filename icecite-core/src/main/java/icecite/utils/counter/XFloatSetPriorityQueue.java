package icecite.utils.counter;

// package icecite.utils.counter;
//
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;
//
// import gnu.trove.impl.Constants;
// import gnu.trove.list.array.TFloatArrayList;
// import gnu.trove.list.array.TIntArrayList;
// import gnu.trove.map.TFloatIntMap;
// import gnu.trove.map.hash.TFloatIntHashMap;
//
/// **
// * A priority queue of primitive float values and sets of elements, where the
// * lengths of sets are the priorities.
// *
// * @param <T>
// * The type of elements.
// *
// * @author Claudius Korzen
// */
// public class NFloatSetPriorityQueue<T> {
// /**
// * The default initial capacity.
// */
// protected static final int DEFAULT_INITIAL_CAPACITY = 10;
//
// /**
// * The default flag that indicates whether this queue is max-based or not.
// */
// protected static final boolean DEFAULT_IS_MAX_BASED = false;
//
// /**
// * The default value that indicates that there is no key given.
// */
// protected static final int DEFAULT_NO_ENTRY_KEY = -1;
//
// /**
// * The default value that indicates that there is no value given.
// */
// protected static final int DEFAULT_NO_ENTRY_VALUE = -1;
//
// /**
// * The floats of this priority queue.
// */
// protected TFloatArrayList floats;
//
// /**
// * The priorities of the floats (the sizes of sets below).
// */
// protected TIntArrayList priorities;
//
// /**
// * The indexes of the floats in the underlying array.
// */
// protected TFloatIntMap indexes;
//
// /**
// * The lists of this queue.
// */
// protected List<Set<T>> sets;
//
// /**
// * A boolean flag that indicates whether this queue should be max-based.
// */
// protected boolean maxBased;
//
// //
// ==========================================================================
// // Constructors.
//
// /**
// * Creates a new priority queue with the default initial capacity.
// */
// public NFloatSetPriorityQueue() {
// this(DEFAULT_IS_MAX_BASED);
// }
//
// /**
// * Creates a new priority queue with the default initial capacity.
// *
// * @param maxBased
// * A flag that indicates whether this queue should be max-based.
// */
// public NFloatSetPriorityQueue(boolean maxBased) {
// this(DEFAULT_INITIAL_CAPACITY, maxBased);
// }
//
// /**
// * Creates a new priority queue with the given initial capacity.
// *
// * @param initialCapacity
// * The initial capacity of this queue.
// */
// public NFloatSetPriorityQueue(int initialCapacity) {
// this(initialCapacity, DEFAULT_IS_MAX_BASED);
// }
//
// /**
// * Creates a new priority queue with the given initial capacity.
// *
// * @param capacity
// * The initial capacity of this queue.
// * @param maxBased
// * A boolean flag that indicates whether this priority queue should be
// * max-based.
// */
// public NFloatSetPriorityQueue(int capacity, boolean maxBased) {
// this.floats = new TFloatArrayList(capacity);
// this.priorities = new TIntArrayList(capacity);
// this.indexes = new TFloatIntHashMap(capacity, Constants.DEFAULT_LOAD_FACTOR,
// DEFAULT_NO_ENTRY_KEY, DEFAULT_NO_ENTRY_VALUE);
// this.sets = new ArrayList<>(capacity);
// this.maxBased = maxBased;
// }
//
// //
// ==========================================================================
//
// /**
// * Inserts a new float/element pair to this queue.
// *
// * @param f
// * The float to insert.
// * @param element
// * The element to insert.
// */
// public void insert(float f, T element) {
// int index = this.indexes.get(f);
// if (index != DEFAULT_NO_ENTRY_VALUE) {
// // The queue already contains the given float. Update the associated set.
// Set<T> set = this.sets.get(index);
// set.add(element);
// updatePriority(f, this.maxBased ? -set.size() : set.size());
// } else {
// // The queue does not contain the given float yet. Add it.
// Set<T> set = new HashSet<>(DEFAULT_INITIAL_CAPACITY);
// set.add(element);
// this.sets.add(set);
// this.floats.add(f);
// this.priorities.add(this.maxBased ? -set.size() : set.size());
// this.indexes.put(f, size() - 1);
// repairHeapUpwards(size() - 1);
// }
// }
//
// /**
// * Removes a float/element pair from this queue.
// *
// * @param f
// * The float to remove.
// * @param element
// * The element to remove.
// */
// public void remove(float f, T element) {
// int index = this.indexes.get(f);
// if (index != DEFAULT_NO_ENTRY_VALUE) {
// Set<T> set = this.sets.get(index);
// set.remove(element);
// updatePriority(f, this.maxBased ? -set.size() : set.size());
// }
// }
//
// /**
// * Updates the priority of the given float.
// *
// * @param f
// * The float to update.
// * @param newPriority
// * The new priority.
// */
// protected void updatePriority(float f, int newPriority) {
// int index = this.indexes.get(f);
// if (index != DEFAULT_NO_ENTRY_VALUE) {
// int oldPriority = this.priorities.get(index);
// newPriority = this.maxBased ? -newPriority : newPriority;
//
// // Check, if the new priority is larger or smaller than the old one.
// // (Nothing to do, if newKey = oldKey).
// if (oldPriority > newPriority) {
// // New priority is smaller. Repair heap upwards.
// this.priorities.set(index, newPriority);
// repairHeapUpwards(index);
// } else if (oldPriority < newPriority) {
// // New key is larger. Repair heap downwards.
// this.priorities.set(index, newPriority);
// repairHeapDownwards(index);
// }
// }
// }
//
// /**
// * Retrieves and removes the head of this queue. Returns Float.NaN if this
// * queue is empty.
// *
// * @return The head of this queue.
// */
// public float poll() {
// if (size() > 0) {
// int index = size() - 1;
// // Swap the minimum to the "end" of the heap.
// swapFloats(0, index);
// // Delete the minimum.
// float f = this.floats.removeAt(index);
// this.priorities.removeAt(index);
// this.indexes.remove(f);
// repairHeapDownwards(0);
// return f;
// }
// return Float.NaN;
// }
//
// /**
// * Retrieves, but does not remove, the head of this queue. Returns Float.NaN
// * if this queue is empty.
// *
// * @return The head of this queue.
// */
// public float peek() {
// return size() > 0 ? this.floats.get(0) : Float.NaN;
// }
//
// //
// ==========================================================================
//
// /**
// * The number of elements in this queue.
// *
// * @return The number of elements in this queue.
// */
// public int size() {
// return this.floats.size();
// }
//
// /**
// * Returns true if this queue is empty.
// *
// * @return True if this queue is empty; False otherwise.
// */
// public boolean isEmpty() {
// return size() == 0;
// }
//
// @Override
// public String toString() {
// return this.floats.toString();
// }
//
// //
// ==========================================================================
//
// /**
// * Repair the heap from the given array position downwards.
// *
// * @param i
// * The array position where to start the repairing.
// */
// protected void repairHeapDownwards(int i) {
// // The position of the smaller child of element.
// int childPos;
//
// // Iterate over heap until there are still child-nodes for element at pos
// // i.
// while ((childPos = (2 * i) + 1) < size()) {
// // Check, if there is a right-hand child.
// if (childPos != size() - 1) {
// // Adjust minChildPos, if the right-hand child is smaller than the
// // right-hand child.
// if (this.priorities.get(childPos + 1) < this.priorities.get(childPos)) {
// childPos++;
// }
// }
//
// // Compare element heap[i] with its smaller child.
// if (this.priorities.get(i) > this.priorities.get(childPos)) {
// // Element at pos i is larger than its smallest child and hence, the
// // heap-propery doesn't hold. So swap both elements.
// swapFloats(i, childPos);
//
// // Hence the indices of elements have changed, update i.
// i = childPos;
// } else {
// // Element array[i] is smaller than its smallest child.
// // There is nothing to do anymore, because the heap-property holds.
// break;
// }
// }
// }
//
// /**
// * Repair the heap from the given array position upwards.
// *
// * @param i
// * The array position where to start the repairing.
// */
// protected void repairHeapUpwards(int i) {
// // The position of the parent element.
// int parentPos;
//
// // Iterate over heap until there is a parent-node for element at pos i.
// while ((parentPos = (int) (Math.ceil((i / 2d)) - 1)) >= 0) {
// // Compare element heap[i] with its parent node.
// if (this.priorities.get(i) < this.priorities.get(parentPos)) {
// // Heap property doesn't hold. Swap.
// swapFloats(i, parentPos);
// i = parentPos;
// } else {
// break;
// }
// }
// }
//
// /**
// * Swaps two floats in the heap and adjusts their indices.
// *
// * @param pos1
// * The position of the first float.
// * @param pos2
// * The position of the second float.
// */
// protected void swapFloats(int pos1, int pos2) {
// float f1 = this.floats.get(pos1);
// Set<T> set1 = this.sets.get(pos1);
// int priority1 = this.priorities.get(pos1);
//
// float f2 = this.floats.get(pos2);
// Set<T> set2 = this.sets.get(pos2);
// int priority2 = this.priorities.get(pos2);
//
// // Swap.
// this.floats.set(pos2, f1);
// this.sets.set(pos2, set1);
// this.priorities.set(pos2, priority1);
// this.indexes.put(f1, pos2);
//
// this.floats.set(pos1, f2);
// this.sets.set(pos1, set2);
// this.priorities.set(pos1, priority2);
// this.indexes.put(f2, pos1);
// }
// }
