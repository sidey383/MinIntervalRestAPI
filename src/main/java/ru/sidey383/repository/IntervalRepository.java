package ru.sidey383.repository;

import ru.sidey383.entity.Interval;

import java.util.Collection;

public interface IntervalRepository<T, S extends Interval<T, S>> {

    S getMinimalInterval();

    Collection<S> getIntersectedIntervals(T start, T end);

    @SuppressWarnings("UnusedReturnValue")
    <V extends S> Iterable<V> saveAll(Iterable<V> entities);

    void deleteAll(Iterable<? extends S> entities);

}
