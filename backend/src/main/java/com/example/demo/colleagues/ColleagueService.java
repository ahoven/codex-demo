package com.example.demo.colleagues;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class ColleagueService {

    private final AtomicLong idSequence = new AtomicLong(0);
    private final ConcurrentMap<Long, Colleague> colleagues = new ConcurrentHashMap<>();

    public Colleague addColleague(String name) {
        long id = idSequence.incrementAndGet();
        Colleague colleague = new Colleague(id, name);
        colleagues.put(id, colleague);
        return colleague;
    }

    public Colleague addCups(long colleagueId, int cups) {
        if (cups < 1) {
            throw new IllegalArgumentException("cups must be positive");
        }
        Colleague colleague = colleagues.get(colleagueId);
        if (colleague == null) {
            throw new ColleagueNotFoundException(colleagueId);
        }
        colleague.addCups(cups);
        return colleague;
    }

    public List<Colleague> getToplist() {
        return colleagues.values().stream()
                .sorted(Comparator.comparingInt(Colleague::getCups)
                        .reversed()
                        .thenComparing(Colleague::getName))
                .toList();
    }

    public Colleague getColleague(long id) {
        Colleague colleague = colleagues.get(id);
        if (colleague == null) {
            throw new ColleagueNotFoundException(id);
        }
        return colleague;
    }
}
