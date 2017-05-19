package icecite.utils.counter;

// package icecite.utils.counter;
//
// import java.util.HashSet;
// import java.util.Set;
//
// import gnu.trove.list.array.TFloatArrayList;
// import gnu.trove.map.hash.TFloatObjectHashMap;
//
/// **
// * A class that groups objects by associated float values and computes some
// * statistics.
// *
// * @param <T>
// * The type of the objects to group.
// *
// * @author Claudius Korzen
// */
// public class FloatCounter<T> extends TFloatObjectHashMap<Set<T>> {
// /**
// * Flag that indicates whether the statistics need to be recomputed.
// */
// protected boolean isStatisticOutdated = true;
//
// /**
// * The largest float value.
// */
// protected float largestFloat = -Float.MAX_VALUE;
//
// /**
// * The smallest float value.
// */
// protected float smallestFloat = Float.MAX_VALUE;
//
// /**
// * (One of) the most frequent float values.
// */
// protected float mostFrequentFloat = -Float.MAX_VALUE;
//
// /**
// * All most frequent float values.
// */
// protected float[] allMostFrequentFloats;
//
// /**
// * The frequency of the most frequent float value.
// */
// protected int mostFrequentFloatFrequency = -Integer.MAX_VALUE;
//
// /**
// * (One of) the least frequent float values.
// */
// protected float leastFrequentFloat = Float.MAX_VALUE;
//
// /**
// * All least frequent float values.
// */
// protected float[] allLeastFrequentFloats;
//
// /**
// * The frequency of the least frequent float value.
// */
// protected int leastFrequentFloatFrequency = Integer.MAX_VALUE;
//
// /**
// * The average value over all float values.
// */
// protected float averageValue = Float.MAX_VALUE;
//
// //
// ==========================================================================
// // The constructors.
//
// /**
// * Creates a new float counter.
// */
// public FloatCounter() {
// super();
// }
//
// /**
// * Creates a new float counter.
// *
// * @param initialCapacity
// * The initial capacity of this counter.
// */
// public FloatCounter(int initialCapacity) {
// super(initialCapacity);
// }
//
// //
// ==========================================================================
// // Add methods.
//
// /**
// * Adds the given object with the given float value.
// *
// * @param f
// * The float value to associate with the given object.
// * @param object
// * The object to group.
// */
// public void add(float f, T object) {
// if (Float.isNaN(f)) {
// return;
// }
//
// if (!containsKey(f)) {
// put(f, new HashSet<>());
// }
//
// get(f).add(object);
// this.isStatisticOutdated = true;
// }
//
// /**
// * Merges this FloatCounter with another FloatCounter.
// *
// * @param counter
// * The counter to merge with this counter.
// */
// public void add(FloatCounter<T> counter) {
// if (counter == null) {
// return;
// }
// if (counter.keys() == null) {
// return;
// }
// for (float f : counter.keys()) {
// for (T object : counter.get(f)) {
// add(f, object);
// }
// }
// }
//
// //
// ==========================================================================
// // Remove methods.
//
// /**
// * Removes the given object with the given float value from this counter.
// *
// * @param f
// * The float value associated with the given object.
// * @param object
// * The object to remove.
// */
// public void remove(float f, Object object) {
// this.isStatisticOutdated = containsKey(f) && get(f).remove(object);
// }
//
// /**
// * Removes the given FloatCounter from this counter.
// *
// * @param counter
// * The counter to remove.
// */
// public void remove(FloatCounter<T> counter) {
// if (counter == null) {
// return;
// }
// if (counter.keys() == null) {
// return;
// }
// for (float f : counter.keys()) {
// for (T object : counter.get(f)) {
// remove(f, object);
// }
// }
// }
//
// //
// ==========================================================================
// // Clear methods.
//
// /**
// * Resets (clears) this counter.
// */
// public void reset() {
// clear();
// resetComputedValues();
// }
//
// /**
// * Resets the internal counters.
// */
// protected void resetComputedValues() {
// this.largestFloat = -Float.MAX_VALUE;
// this.smallestFloat = Float.MAX_VALUE;
// this.mostFrequentFloat = -Float.MAX_VALUE;
// this.allMostFrequentFloats = null;
// this.mostFrequentFloatFrequency = -Integer.MAX_VALUE;
// this.leastFrequentFloat = Float.MAX_VALUE;
// this.allLeastFrequentFloats = null;
// this.leastFrequentFloatFrequency = Integer.MAX_VALUE;
// this.averageValue = Float.MAX_VALUE;
// }
//
// //
// ==========================================================================
// // Getter methods.
//
// /**
// * Returns (one of) the most frequent float values.
// *
// * @return (One of) the most frequent float values.
// */
// public float getMostFrequentFloat() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.mostFrequentFloat;
// }
//
// /**
// * Returns the frequency of the most frequent float value.
// *
// * @return The frequency of the most frequent float value.
// */
// public int getMostFrequentFloatFrequency() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.mostFrequentFloatFrequency;
// }
//
// /**
// * Returns the objects that are associated with the most frequent float in
// * this counter.
// *
// * @return The objects that are associated with the most frequent float in
// * this counter.
// */
// public Set<T> getObjectsWithMostFrequentFloat() {
// return get(getMostFrequentFloat());
// }
//
// //
// ==========================================================================
//
// /**
// * Returns all most frequent float values.
// *
// * @return All most frequent float values.
// */
// public float[] getAllMostFrequentFloats() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.allMostFrequentFloats;
// }
//
// /**
// * Returns all objects that are associated with all most frequent float
// * values in this counter.
// *
// * @return all objects that are associated with all most frequent float
// * values in this counter.
// */
// public Set<T> getAllObjectsWithMostFrequentFloats() {
// Set<T> allObjects = new HashSet<>();
// for (float f : getAllMostFrequentFloats()) {
// allObjects.addAll(get(f));
// }
// return allObjects;
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the least frequent float value.
// *
// * @return The least frequent float value.
// */
// public float getLeastFrequentFloat() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.leastFrequentFloat;
// }
//
// /**
// * Returns the frequency of the least frequent float value.
// *
// * @return The frequency of the least frequent float value.
// */
// public int getLeastFrequentFloatFrequency() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.leastFrequentFloatFrequency;
// }
//
// /**
// * Returns the objects that are associated with the least frequent float in
// * this counter.
// *
// * @return The objects that are associated with the least frequent float in
// * this counter.
// */
// public Set<T> getObjectsWithLeastFrequentFloat() {
// return get(getLeastFrequentFloat());
// }
//
// //
// ==========================================================================
//
// /**
// * Returns all least frequent float values in this counter.
// *
// * @return All least frequent float values in this counter.
// */
// public float[] getAllLeastFrequentFloats() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.allLeastFrequentFloats;
// }
//
// /**
// * Returns all objects that are associated with all least frequent float
// * values in this counter.
// *
// * @return all objects that are associated with all least frequent float
// * values in this counter.
// */
// public Set<T> getAllObjectsWithLeastFrequentFloats() {
// Set<T> allObjects = new HashSet<>();
// for (float f : getAllMostFrequentFloats()) {
// allObjects.addAll(get(f));
// }
// return allObjects;
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the average value over all float values.
// *
// * @return The average value over all float values.
// */
// public float getAverageValue() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.averageValue;
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the smallest float value.
// *
// * @return The smallest float value.
// */
// public float getSmallestFloat() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.smallestFloat;
// }
//
// /**
// * Returns the objects that are associated with the smallest float value.
// *
// * @return The objects that are associated with the smallest float value.
// */
// public Set<T> getObjectsWithSmallestFloat() {
// return get(getSmallestFloat());
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the smallest float value that occurs at least 'freq'-times.
// *
// * @param freq
// * The frequency.
// * @return The smallest float value that occurs at least 'freq'-times.
// */
// public float getSmallestFloatOccuringAtLeast(int freq) {
// float smallestFloat = Float.MAX_VALUE;
// for (float f : keys()) {
// if (get(f).size() >= freq && f < smallestFloat) {
// smallestFloat = f;
// }
// }
// return smallestFloat;
// }
//
// /**
// * Returns the objects that are associated with the smallest float value that
// * occurs at least 'freq'-times.
// *
// * @param freq
// * The frequency.
// * @return The objects that are associated with the smallest float value that
// * occurs at least 'freq'-times.
// */
// public Set<T> getObjectsWithSmallestFloatOccuringAtLeast(int freq) {
// return get(getSmallestFloatOccuringAtLeast(freq));
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the smallest float value that occurs at most 'freq'-times.
// *
// * @param freq
// * The frequency.
// * @return The smallest float value that occurs at most 'freq'-times.
// */
// public float getSmallestFloatOccuringAtMost(int freq) {
// float smallestFloat = Float.MAX_VALUE;
// for (float f : keys()) {
// if (get(f).size() <= freq && f < smallestFloat) {
// smallestFloat = f;
// }
// }
// return smallestFloat;
// }
//
// /**
// * Returns the objects that are associated with the smallest float value that
// * occurs at most 'freq'-times.
// *
// * @param freq
// * The frequency.
// * @return The objects that are associated with the smallest float value that
// * occurs at most 'freq'-times.
// */
// public Set<T> getObjectsWithSmallestFloatOccuringAtMost(int freq) {
// return get(getSmallestFloatOccuringAtMost(freq));
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the largest float value.
// *
// * @return The largest float value.
// */
// public float getLargestFloat() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.largestFloat;
// }
//
// /**
// * Returns the objects that are associated with the largest float value.
// *
// * @return The objects that are associated with the largest float value.
// */
// public Set<T> getObjectsWithLargestFloat() {
// return get(getLargestFloat());
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the largest float value that occurs at most 'freq'-times.
// *
// * @param freq
// * The frequency.
// * @return The largest float value that occurs at most 'freq'-times.
// */
// public float getLargestFloatOccuringAtMost(int freq) {
// float largestFloat = -Float.MAX_VALUE;
// for (float f : keys()) {
// if (get(f).size() <= freq && f > largestFloat) {
// largestFloat = f;
// }
// }
// return largestFloat;
// }
//
// /**
// * Returns the objects that are associated with the largest float value that
// * occurs at most 'freq'-times.
// *
// * @param freq
// * The frequency.
// * @return The objects that are associated with the largest float value that
// * occurs at most 'freq'-times.
// */
// public Set<T> getObjectsWithLargestFloatOccuringAtMost(int freq) {
// return get(getLargestFloatOccuringAtMost(freq));
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the largest float value that occurs at least 'freq'-times.
// *
// * @param freq
// * The frequency.
// * @return The largest float value that occurs at least 'freq'-times.
// */
// public float getLargestFloatOccuringAtLeast(int freq) {
// float largestFloat = -Float.MAX_VALUE;
// for (float f : keys()) {
// if (get(f).size() >= freq && f > largestFloat) {
// largestFloat = f;
// }
// }
// return largestFloat;
// }
//
// /**
// * Returns the objects that are associated with the largest float value that
// * occurs at least 'freq'-times.
// *
// * @param freq
// * The frequency.
// * @return The objects that are associated with the largest float value that
// * occurs at least 'freq'-times.
// */
// public Set<T> getObjectsWithLargestFloatOccuringAtLeast(int freq) {
// return get(getLargestFloatOccuringAtLeast(freq));
// }
//
// //
// ==========================================================================
//
// /**
// * Counts the frequencies of the floats.
// */
// protected void count() {
// resetComputedValues();
//
// float sumFloats = 0;
// float numFloats = 0;
// TFloatArrayList allMostFrequentFloats = new TFloatArrayList();
// TFloatArrayList allLeastFrequentFloats = new TFloatArrayList();
//
// for (float f : keys()) {
// int count = get(f).size();
//
// if (count == 0) {
// continue;
// }
//
// if (f > this.largestFloat) {
// this.largestFloat = f;
// }
//
// if (f < this.smallestFloat) {
// this.smallestFloat = f;
// }
//
// if (count > this.mostFrequentFloatFrequency) {
// this.mostFrequentFloat = f;
// this.mostFrequentFloatFrequency = count;
// allMostFrequentFloats.clear();
// allMostFrequentFloats.add(f);
// }
//
// if (count == this.mostFrequentFloatFrequency) {
// allMostFrequentFloats.add(f);
// }
//
// if (count < this.mostFrequentFloatFrequency) {
// this.leastFrequentFloat = f;
// this.leastFrequentFloatFrequency = count;
// allLeastFrequentFloats.clear();
// allMostFrequentFloats.add(f);
// }
//
// if (count == this.leastFrequentFloatFrequency) {
// allLeastFrequentFloats.add(f);
// }
//
// sumFloats += count * f;
// numFloats += count;
// }
//
// this.averageValue = numFloats > 0 ? (sumFloats / numFloats) : 0;
// this.allMostFrequentFloats = allMostFrequentFloats.toArray();
// this.allLeastFrequentFloats = allLeastFrequentFloats.toArray();
//
// this.isStatisticOutdated = false;
// }
//
// //
// ==========================================================================
// // Util methods.
//
// @Override
// public Set<T> get(float f) {
// if (containsKey(f)) {
// return super.get(f);
// }
// // Return an empty list if this counter does not contain the given float.
// return new HashSet<>();
// }
// }
