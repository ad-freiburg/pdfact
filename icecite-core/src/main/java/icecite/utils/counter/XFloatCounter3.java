package icecite.utils.counter;

// package icecite.utils.counter;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import gnu.trove.impl.Constants;
// import gnu.trove.list.array.TFloatArrayList;
// import gnu.trove.list.array.TIntArrayList;
// import gnu.trove.map.TFloatIntMap;
// import gnu.trove.map.hash.TFloatIntHashMap;
//
/// **
// * A primitive priority queue.
// *
// * @author Claudius Korzen
// */
// public class FloatCounter<T> {
// /**
// * The keys of this counter.
// */
// protected TFloatArrayList keys;
//
// /**
// * The values of this counter.
// */
// protected List<List<T>> values;
//
// /**
// * The indexes of the items in the array.
// */
// protected TFloatIntMap indexes;
//
// //
// ==========================================================================
// // Constructors.
//
// /**
// * Creates a new FloatCounter with initial capacity 10.
// */
// public FloatCounter() {
// this(10);
// }
//
// /**
// * Creates a new FloatCounter with the given initial capacity.
// *
// * @param initialCapacity
// * The initial capacity of this priority queue.
// */
// public FloatCounter(int initialCapacity) {
// this.keys = new TFloatArrayList(initialCapacity);
// this.values = new ArrayList<>(initialCapacity);
// // Set -1 as noEntryKey and noEntryValue.
// this.indexes = new TFloatIntHashMap(initialCapacity,
// Constants.DEFAULT_LOAD_FACTOR, -1, -1);
// }
//
// //
// ==========================================================================
//
// /**
// * Inserts the given key/value pair into this queue.
// *
// * @param key
// * The item to insert.
// * @param value
// * The priority of the item.
// */
// public void add(float key, T value) {
// int index = this.indexes.get(key);
// if (index == -1) {
// insert(key, value);
// } else {
// updatePriority(item, newPriority);
// }
// this.keys.add(key);
// this.values.add(value);
// this.indexes.put(key, size() - 1);
// repairHeapUpwards(size() - 1);
// }
//
// /**
// * Retrieves, but does not remove, the head of this queue, or returns
// * Float.MAX_VALUE if this queue is empty.
// *
// * @return The head of this queue.
// */
// public float peek() {
// return size() > 0 ? this.keys.get(0) : Float.MAX_VALUE;
// }
//
// /**
// * Retrieves and removes the head of this queue, or returns Float.MAX_VALUE
// * if this queue is empty.
// *
// * @return The head of this queue.
// */
// public float poll() {
// if (size() > 0) {
// int index = size() - 1;
// // Swap the minimum to the "end" of the heap.
// swapItems(0, index);
// // Delete the minimum.
// float item = this.keys.removeAt(index);
// this.values.removeAt(index);
// this.indexes.remove(item);
// repairHeapDownwards(0);
// return item;
// }
// return Float.MAX_VALUE;
// }
//
// /**
// * Changes the priority of the given item.
// *
// * @param item
// * The item to change.
// * @param newPriority
// * The new priority.
// */
// public void changePriority(float item, int newPriority) {
// int index = this.indexes.get(item);
// if (index > -1) {
// int oldPriority = this.values.get(index);
//
// // Check, if the new priority is larger or smaller than the old one.
// // (Nothing to do, if newKey = oldKey).
// if (oldPriority > newPriority) {
// // New priority is smaller. Repair heap upwards.
// this.values.set(index, newPriority);
// repairHeapUpwards(index);
// } else if (oldPriority < newPriority) {
// // New key is larger. Repair heap downwards.
// this.values.set(index, newPriority);
// repairHeapDownwards(index);
// }
// }
// }
//
// /**
// * The number of elements in this queue.
// *
// * @return The number of elements in this queue.
// */
// public int size() {
// return this.keys.size();
// }
//
// /**
// * Repair the heap from the given array position downwards.
// *
// * @param i
// * The start position in the array.
// */
// protected void repairHeapDownwards(int i) {
// // The position of the smaller child of element.
// int minChildPos;
//
// // Iterate over heap until there are still child-nodes for element at pos
// // i.
// while ((minChildPos = (2 * i) + 1) < size()) {
// // Check, if there is a right-hand child.
// if (minChildPos != size() - 1) {
// // Adjust minChildPos, if the right-hand child is smaller than the
// // right-hand child.
// if (this.values.get(minChildPos + 1) < this.values
// .get(minChildPos)) {
// minChildPos++;
// }
// }
//
// // Compare element heap[i] with its smaller child.
// if (this.values.get(i) > this.values.get(minChildPos)) {
// // Element at pos i is larger than its smallest child and hence, the
// // heap-propery doesn't hold. So swap both elements.
// swapItems(i, minChildPos);
//
// // Hence the indices of elements have changed, update i.
// i = minChildPos;
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
// * The start position in the array.
// */
// protected void repairHeapUpwards(int i) {
// // The position of the parent element.
// int parentPos;
//
// // Iterate over heap until there is a parent-node for element at pos i.
// while ((parentPos = (int) (Math.ceil((i / 2d)) - 1)) >= 0) {
// // Compare element heap[i] with its parent node.
// if (this.values.get(i) < this.values.get(parentPos)) {
// // Heap property doesn't hold. Swap.
// swapItems(i, parentPos);
// i = parentPos;
// } else {
// break;
// }
// }
// }
//
// /**
// * Swap two items in the heap and adjust their indices.
// *
// * @param pos1
// * The position of the first item.
// * @param pos2
// * The position of the second item.
// */
// protected void swapItems(int pos1, int pos2) {
// float item1 = this.keys.get(pos1);
// int priority1 = this.values.get(pos1);
//
// float item2 = this.keys.get(pos2);
// int priority2 = this.values.get(pos2);
//
// this.keys.set(pos2, item1);
// this.values.set(pos2, priority1);
// this.indexes.put(item1, pos2);
//
// this.keys.set(pos1, item2);
// this.values.set(pos1, priority2);
// this.indexes.put(item2, pos1);
// }
//
// @Override
// public String toString() {
// return this.keys.toString();
// }
// }
