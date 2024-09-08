package com.cfe.cfetime.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmployeeType getEmployeeTypeSample1() {
        return new EmployeeType().id(1L).name("name1");
    }

    public static EmployeeType getEmployeeTypeSample2() {
        return new EmployeeType().id(2L).name("name2");
    }

    public static EmployeeType getEmployeeTypeRandomSampleGenerator() {
        return new EmployeeType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
