package com.victorchen.mycurrency.network.eventbus;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton class that generate Integer id atomically
 */
public class IdGenerator {
    private AtomicInteger id = new AtomicInteger(0);
    private static IdGenerator instance = new IdGenerator();

    public static IdGenerator getInstance() {
        return instance;
    }

    public void setInitialId(int value) {
        id.set(value);
    }

    public Integer nextId() {
        return id.getAndIncrement();
    }
}
