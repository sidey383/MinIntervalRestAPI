package ru.sidey383;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sidey383.entity.CharacterInterval;
import ru.sidey383.entity.Interval;
import ru.sidey383.entity.NumberInterval;
import ru.sidey383.repository.CharacterIntervalRepository;
import ru.sidey383.repository.IntervalRepository;
import ru.sidey383.repository.NumberIntervalRepository;

import java.util.*;

@RestController
@RequestMapping("/api/v1/intervals")
public class IntervalsRestAPI {

    private final NumberIntervalRepository numberIntervalRepository;

    private final CharacterIntervalRepository characterIntervalRepository;

    private final ObjectMapper mapper = new ObjectMapper();


    public IntervalsRestAPI(
            @Autowired
            NumberIntervalRepository numberIntervalRepository,
            @Autowired
            CharacterIntervalRepository characterIntervalRepository) {
        this.numberIntervalRepository = numberIntervalRepository;
        this.characterIntervalRepository = characterIntervalRepository;
    }

    @PostMapping(value = "/merge", params = "kind")
    public ResponseEntity<?> makeMerge(@RequestParam String kind, @RequestBody String body) {
        Kind k = Kind.getKind(kind);
        if (k == null) {
            return new ResponseEntity<>("Wrong request parameter", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            switch (k) {
                case DIGITS -> {
                    makeDigitsMerge(body);
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
                case LETTERS -> {
                    makeCharactersMerge(body);
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
                default -> {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new ResponseEntity<>("Wrong request body", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/min", params = "kind")
    public ResponseEntity<?> selectMin(@RequestParam String kind) {
        Kind k = Kind.getKind(kind);
        if (k == null) {
            return new ResponseEntity<>("Wrong request parameter", HttpStatus.NOT_ACCEPTABLE);
        }
        switch (k) {
            case DIGITS -> {
                NumberInterval ni = numberIntervalRepository.getMinimalInterval();
                if (ni == null)
                    return new ResponseEntity<>( HttpStatus.NO_CONTENT);
                else
                    return new ResponseEntity<>(ni.toArray(), HttpStatus.OK);
            }
            case LETTERS -> {
                CharacterInterval ci = characterIntervalRepository.getMinimalInterval();
                if (ci == null)
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                else
                 return new ResponseEntity<>(ci.toArray(), HttpStatus.OK);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping(value = "/stored", params = "kind")
    public ResponseEntity<?> getIntervals(@RequestParam String kind) {
        Kind k = Kind.getKind(kind);
        if (k == null) {
            return new ResponseEntity<>("Wrong request parameter", HttpStatus.NOT_ACCEPTABLE);
        }
        switch (k) {
            case DIGITS -> {
                Iterable<NumberInterval> ni = numberIntervalRepository.findAll();
                ArrayList<Long[]> pairs = new ArrayList<>();
                for (NumberInterval i : ni) {
                    pairs.add(i.toArray());
                }
                return new ResponseEntity<>(pairs, HttpStatus.OK);
            }
            case LETTERS -> {
                Iterable<CharacterInterval> ni = characterIntervalRepository.findAll();
                ArrayList<Character[]> pairs = new ArrayList<>();
                for (CharacterInterval i : ni) {
                    pairs.add(i.toArray());
                }
                return new ResponseEntity<>(pairs, HttpStatus.OK);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void makeDigitsMerge(String body) throws JsonProcessingException, IllegalArgumentException {
        Long[][] pairs = mapper.readValue(body, Long[][].class);

        List<NumberInterval> intervalList = new ArrayList<>();
        for (Long[] pair : pairs) {
            intervalList.add(new NumberInterval(pair));
        }
        makeMerge(intervalList, numberIntervalRepository);
    }

    private void makeCharactersMerge(String body) throws JsonProcessingException, IllegalArgumentException {
        Character[][] pairs = mapper.readValue(body, Character[][].class);

        List<CharacterInterval> intervalList = new ArrayList<>();
        for (Character[] pair : pairs) {
            intervalList.add(new CharacterInterval(pair));
        }
        makeMerge(intervalList, characterIntervalRepository);
    }

    private <V extends Comparable<? super V>, I extends Interval<V, I>> void makeMerge(List<I> intervalList, IntervalRepository<V, I> repository) {
        Optional<V> start = intervalList.stream().map(I::getIntervalStart).min(V::compareTo);
        Optional<V> end = intervalList.stream().map(I::getIntervalEnd).max(V::compareTo);
        if (start.isEmpty() || end.isEmpty())
            return;
        Collection<I> oldIntervals = repository.getIntersectedIntervals(start.get(), end.get());
        intervalList.addAll(oldIntervals);
        Set<I> intersected = new HashSet<>(calculateIntersectIntervals(intervalList));
        repository.saveAll(intersected);
        repository.deleteAll(() -> oldIntervals.stream().filter(v -> !intersected.contains(v)).iterator());
    }

    private <V, T extends Interval<V, T>> List<T> calculateIntersectIntervals(List<T> values) {
        if (values.isEmpty())
            return List.of();
        List<T> sorted = new ArrayList<>(values);
        sorted.sort(T::compareTo);
        List<T> result = new ArrayList<>();
        result.add(sorted.get(0));
        for (int i = 1; i < sorted.size(); i++) {
            T last = result.get(result.size() - 1);
            T current = sorted.get(i);
            if (last.isIntersect(current)) {
                result.set(result.size() - 1, last.union(current));
            } else {
                result.add(current);
            }
        }
        return result;
    }

}
