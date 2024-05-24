package ru.sidey383.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sidey383.entity.CharacterInterval;

import java.util.Collection;

@Repository
public interface CharacterIntervalRepository extends IntervalRepository<Character, CharacterInterval>, CrudRepository<CharacterInterval, Long> {

    @Query("SELECT i from CharacterInterval as i order by i.intervalStart, i.intervalEnd limit 1")
    CharacterInterval getMinimalInterval();

    @Query("SELECT i from CharacterInterval as i where i.intervalStart <= :end and :start <= i.intervalEnd")
    Collection<CharacterInterval> getIntersectedIntervals(Character start, Character end);

}
