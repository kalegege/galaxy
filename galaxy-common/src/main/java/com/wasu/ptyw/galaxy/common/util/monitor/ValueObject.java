package com.wasu.ptyw.galaxy.common.util.monitor;

import java.util.concurrent.atomic.AtomicReference;


/**
 * @author wenguang
 */

public class ValueObject {
    public static final int NUM_VALUES = 2;

    private final AtomicReference<long[]> values = new AtomicReference<long[]>();


    public ValueObject() {
        final long[] init = new long[NUM_VALUES];
        this.values.set(init);
    }


    public ValueObject(final long value1, final long value2) {
        this();
        this.addCount(value1, value2);
    }


    public void addCount(final long value1, final long value2) {
        long[] current;
        final long[] update = new long[NUM_VALUES];
        do {
            current = values.get();
            update[0] = current[0] + value1;
            update[1] = current[1] + value2;
        } while (!values.compareAndSet(current, update));

    }


    /**
     * Should only be used by log writer to deduct written counts. This method
     * does not affect stat rules.
     */
    void deductCount(final long value1, final long value2) {
        long[] current;
        final long[] update = new long[NUM_VALUES];
        do {
            current = values.get();
            update[0] = current[0] - value1;
            update[1] = current[1] - value2;
        } while (!values.compareAndSet(current, update));
    }


    /**
     * 原子化读取所有值
     */
    public long[] getValues() {
        return values.get();
    }
}