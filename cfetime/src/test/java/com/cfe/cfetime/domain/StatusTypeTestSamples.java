package com.cfe.cfetime.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StatusTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StatusType getStatusTypeSample1() {
        return new StatusType().id(1L).name("name1");
    }

    public static StatusType getStatusTypeSample2() {
        return new StatusType().id(2L).name("name2");
    }

    public static StatusType getStatusTypeRandomSampleGenerator() {
        return new StatusType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
