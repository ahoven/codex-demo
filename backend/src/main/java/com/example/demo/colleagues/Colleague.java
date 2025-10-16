package com.example.demo.colleagues;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory representation of a colleague and their coffee count.
 */
class Colleague {

    private final long id;
    private final String name;
    private final AtomicInteger cups = new AtomicInteger();

    Colleague(long id, String name) {
        this.id = id;
        this.name = name;
    }

    long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getCups() {
        return cups.get();
    }

    int addCups(int amount) {
        return cups.addAndGet(amount);
    }
}
