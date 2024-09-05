package com.cfe.cfetime.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Employee getEmployeeSample1() {
        return new Employee().id(1L).clave(1).name("name1").firstSurname("firstSurname1").secondSurname("secondSurname1");
    }

    public static Employee getEmployeeSample2() {
        return new Employee().id(2L).clave(2).name("name2").firstSurname("firstSurname2").secondSurname("secondSurname2");
    }

    public static Employee getEmployeeRandomSampleGenerator() {
        return new Employee()
            .id(longCount.incrementAndGet())
            .clave(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .firstSurname(UUID.randomUUID().toString())
            .secondSurname(UUID.randomUUID().toString());
    }
}
