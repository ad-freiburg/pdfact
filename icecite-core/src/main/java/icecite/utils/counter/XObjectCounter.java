package icecite.utils.counter;

// package icecite.utils.counter;
//
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.Set;
//
/// **
// * A class that groups objects by another given key objects and computes some
// * statistics.
// *
// * @param <S>
// * The type of the key objects.
// * @param <T>
// * The type of the objects to group.
// *
// * @author Claudius Korzen
// */
// public class ObjectCounter<S, T> extends HashMap<S, Set<T>> {
// /**
// * The serial id.
// */
// protected static final long serialVersionUID = -3759048794193605026L;
//
// /**
// * Flag that indicates whether the statistics need to be recomputed.
// */
// protected boolean isStatisticOutdated = true;
//
// /**
// * (One of) the most frequent key object(s).
// */
// protected S mostFrequentObject = null;
//
// /**
// * All most frequent key objects.
// */
// protected Set<S> allMostFrequentObjects = new HashSet<>();
//
// /**
// * The frequency of the most frequent key object.
// */
// protected int mostFrequentObjectFrequency = -Integer.MAX_VALUE;
//
// /**
// * (One of) the least frequent key object(s).
// */
// protected S leastFrequentObject = null;
//
// /**
// * All least frequent key objects.
// */
// protected Set<S> allLeastFrequentObjects = new HashSet<>();
//
// /**
// * The frequency of the least frequent object.
// */
// protected int leastFrequentObjectFrequencyCount = Integer.MAX_VALUE;
//
// //
// ==========================================================================
// // The constructors.
//
// /**
// * Creates a new object counter.
// */
// public ObjectCounter() {
// super();
// }
//
// /**
// * Creates a new object counter.
// *
// * @param initialCapacity
// * The initial capacity of this counter.
// */
// public ObjectCounter(int initialCapacity) {
// super(initialCapacity);
// }
//
// //
// ==========================================================================
// // Add methods.
//
// /**
// * Adds the given object to this counter.
// *
// * @param key
// * The key object.
// * @param value
// * The object to group.
// */
// public void add(S key, T value) {
// if (!containsKey(key)) {
// put(key, new HashSet<>());
// }
//
// get(key).add(value);
// this.isStatisticOutdated = true;
// }
//
// /**
// * Merges this ObjectCounter with another ObjectCounter.
// *
// * @param counter
// * The counter to merge with this counter.
// */
// public void add(ObjectCounter<S, T> counter) {
// if (counter == null) {
// return;
// }
// if (counter.keySet() == null) {
// return;
// }
// for (S s : counter.keySet()) {
// for (T object : counter.get(s)) {
// add(s, object);
// }
// }
// }
//
// //
// ==========================================================================
// // Remove methods.
//
// /**
// * Removes the given object with the given int value from this counter.
// *
// * @param s
// * The key object.
// * @param t
// * The object to remove.
// */
// public boolean remove(Object s, Object t) {
// this.isStatisticOutdated = containsKey(s) && get(s).remove(t);
// return this.isStatisticOutdated;
// }
//
// /**
// * Removes the given ObjectCounter from this counter.
// *
// * @param counter
// * The counter to remove.
// */
// public void remove(ObjectCounter<S, T> counter) {
// if (counter == null) {
// return;
// }
// if (counter.keySet() == null) {
// return;
// }
// for (S s : counter.keySet()) {
// for (T object : counter.get(s)) {
// remove(s, object);
// }
// }
// }
//
// //
// ==========================================================================
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
// this.mostFrequentObject = null;
// this.allMostFrequentObjects = new HashSet<>();
// this.mostFrequentObjectFrequency = -Integer.MAX_VALUE;
// this.leastFrequentObject = null;
// this.allLeastFrequentObjects = new HashSet<>();
// this.leastFrequentObjectFrequencyCount = Integer.MAX_VALUE;
// }
//
// //
// ==========================================================================
// // Getter methods.
//
// /**
// * Returns the most frequent object.
// *
// * @return The most frequent object.
// */
// public S getMostFrequentObject() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.mostFrequentObject;
// }
//
// /**
// * Returns the frequency of the most frequent object.
// *
// * @return The frequency of the most frequent object.
// */
// public int getMostFrequentObjectFrequency() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.mostFrequentObjectFrequency;
// }
//
// /**
// * Returns the objects that are associated with the most frequent object in
// * this counter.
// *
// * @return The objects that are associated with the most frequent object in
// * this counter.
// */
// public Set<T> getObjectsWithMostFrequentObject() {
// return get(getMostFrequentObject());
// }
//
// //
// ==========================================================================
//
// /**
// * Returns all most frequent key objects.
// *
// * @return All most frequent objects in a set.
// */
// public Set<S> getAllMostFrequentObjects() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.allMostFrequentObjects;
// }
//
// /**
// * Returns all objects that are associated with all most frequent key objects
// * in this counter.
// *
// * @return all objects that are associated with all most frequent key objects
// * in this counter.
// */
// public Set<T> getAllObjectsWithMostFrequentObjects() {
// Set<T> allObjects = new HashSet<>();
// for (S s : getAllMostFrequentObjects()) {
// allObjects.addAll(get(s));
// }
// return allObjects;
// }
//
// //
// ==========================================================================
//
// /**
// * Returns the least frequent object.
// *
// * @return The least frequent object.
// */
// public S getLeastFrequentObject() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.leastFrequentObject;
// }
//
// /**
// * Returns the frequency of the least frequent object.
// *
// * @return The frequency of the least frequent object.
// */
// public int getLeastFrequentObjectFrequency() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.leastFrequentObjectFrequencyCount;
// }
//
// /**
// * Returns the objects that are associated with the least frequent key object
// * in this counter.
// *
// * @return The objects that are associated with the least frequent key object
// * in this counter.
// */
// public Set<T> getObjectsWithLeastFrequentInt() {
// return get(getLeastFrequentObject());
// }
//
// //
// ==========================================================================
//
// /**
// * Returns all least frequent objects.
// *
// * @return All least frequent objects.
// */
// public Set<S> getAllLeastFrequentObjects() {
// if (this.isStatisticOutdated) {
// count();
// }
// return this.allLeastFrequentObjects;
// }
//
// /**
// * Returns all objects that are associated with all least frequent key
// * objects in this counter.
// *
// * @return all objects that are associated with all least frequent key
// * objects in this counter.
// */
// public Set<T> getAllObjectsWithLeastFrequentObjects() {
// Set<T> allObjects = new HashSet<>();
// for (S s : getAllLeastFrequentObjects()) {
// allObjects.addAll(get(s));
// }
// return allObjects;
// }
//
// //
// ==========================================================================
//
// /**
// * Counts the frequencies of the objects.
// */
// protected void count() {
// resetComputedValues();
//
// for (S object : keySet()) {
// int count = get(object).size();
//
// if (count == 0) {
// continue;
// }
//
// if (count > this.mostFrequentObjectFrequency) {
// this.mostFrequentObject = object;
// this.mostFrequentObjectFrequency = count;
// this.allMostFrequentObjects.clear();
// this.allMostFrequentObjects.add(object);
// }
//
// if (count == this.mostFrequentObjectFrequency) {
// this.allMostFrequentObjects.add(object);
// }
//
// if (count < this.leastFrequentObjectFrequencyCount) {
// this.leastFrequentObject = object;
// this.leastFrequentObjectFrequencyCount = count;
// this.allLeastFrequentObjects.clear();
// this.allLeastFrequentObjects.add(object);
// }
//
// if (count == this.leastFrequentObjectFrequencyCount) {
// this.allLeastFrequentObjects.add(object);
// }
// }
// this.isStatisticOutdated = false;
// }
//
// //
// ==========================================================================
// // Util methods.
//
// public Set<T> get(Object o) {
// if (containsKey(o)) {
// return super.get(o);
// }
// // Return an empty list if this counter does not contain the given object.
// return new HashSet<>();
// }
// }
