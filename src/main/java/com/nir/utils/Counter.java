package com.nir.utils;

import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class Counter {
    private Integer initialValue;
    private Integer currentValue;

    public Counter(Integer initialValue) {
        this.initialValue = initialValue;
        this.currentValue = this.initialValue;
    }

    public void increment() {
        this.currentValue ++;
    }

    public Integer get() {
        return this.currentValue;
    }

    public Integer getAndIncrement() {
        Integer temp = this.currentValue;
        this.increment();
        return temp;
    }
}
