package ru.sidey383.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sidey383.entity.NumberInterval;

import java.util.Collection;

@Repository
public interface NumberIntervalRepository extends IntervalRepository<Long, NumberInterval>, CrudRepository<NumberInterval, Long> {

    @Query("SELECT i from NumberInterval as i order by i.intervalStart, i.intervalEnd limit 1")
    NumberInterval getMinimalInterval();

    @Query("SELECT i from NumberInterval as i where i.intervalStart <= :end and :start <= i.intervalEnd")
    Collection<NumberInterval> getIntersectedIntervals(Long start, Long end);

}
