package ru.sidey383.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@Setter
public class NumberInterval implements Interval<Long, NumberInterval> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long intervalStart;
    @NotNull
    private Long intervalEnd;

    public NumberInterval() {}

    public NumberInterval(Long... values) {
        if (values.length != 2 || values[0] == null || values[1] == null) {
            throw new IllegalArgumentException("A couple of numbers are expected");
        }
        if (values[0] < values[1]) {
            intervalStart = values[0];
            intervalEnd = values[1];
        } else {
            intervalStart = values[1];
            intervalEnd = values[0];
        }
    }

    public Long[] toArray() {
        return new Long[] {intervalStart, intervalEnd};
    }

    @Override
    public int compareTo(@NotNull Interval<Long, ?> o) {
        int result = Long.compare(this.getIntervalStart(), o.getIntervalStart());
        if (result == 0) {
            result = Long.compare(this.getIntervalEnd(), o.getIntervalEnd());
        }
        return result;
    }

    @Override
    @Contract("_ -> new")
    public NumberInterval union(Interval<Long, ?> interval) {
        return new NumberInterval(Math.min(this.getIntervalStart(), interval.getIntervalStart()), Math.max(this.getIntervalEnd(), interval.getIntervalEnd()));
    }

    @Override
    public boolean isIntersect(Interval<Long, ?> interval) {
        return interval.getIntervalStart() <= this.getIntervalEnd() && this.getIntervalStart() <= interval.getIntervalEnd();
    }

}
