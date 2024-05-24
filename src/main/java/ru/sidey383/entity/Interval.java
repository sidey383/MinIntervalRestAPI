package ru.sidey383.entity;

public interface Interval<T, S extends Interval<T, S>> extends Comparable<Interval<T, ?>> {

    S union(Interval<T, ?> interval);

    boolean isIntersect(Interval<T, ?> interval);

    T[] toArray();

    T getIntervalStart();

    T getIntervalEnd();
}
