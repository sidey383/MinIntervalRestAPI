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
public class CharacterInterval implements Interval<Character, CharacterInterval> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Character intervalStart;
    @NotNull
    private Character intervalEnd;

    public CharacterInterval() {}

    public CharacterInterval(Character... values) {
        if (values.length != 2 || values[0] == null || values[1] == null) {
            throw new IllegalArgumentException("A couple of characters are expected");
        }
        if (values[0] < values[1]) {
            intervalStart = values[0];
            intervalEnd = values[1];
        } else {
            intervalStart = values[1];
            intervalEnd = values[0];
        }
    }

    public Character[] toArray() {
        return new Character[]{intervalStart, intervalEnd};
    }

    @Override
    public int compareTo(@NotNull Interval<Character, ?> o) {
        int result = Character.compare(this.getIntervalStart(), o.getIntervalStart());
        if (result == 0) {
            result = Character.compare(this.getIntervalEnd(), o.getIntervalEnd());
        }
        return result;
    }

    @Override
    @Contract("_ -> new")
    public CharacterInterval union(Interval<Character, ?> interval) {
        return new CharacterInterval((char) Math.min((int)this.getIntervalStart(), (int) interval.getIntervalStart()), (char)Math.max((int)this.getIntervalEnd(),(int) interval.getIntervalEnd()));
    }

    @Override
    public boolean isIntersect(Interval<Character, ?> interval) {
        return interval.getIntervalStart() <= this.getIntervalEnd() && this.getIntervalStart() <= interval.getIntervalEnd();
    }
}
