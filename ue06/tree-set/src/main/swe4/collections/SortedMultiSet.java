package swe4.collections;

public interface SortedMultiSet<T extends Comparable<T>> extends Iterable<T> {
    void add(T item);
    boolean contains(T item);
    T get(T item);
    int size();
}
