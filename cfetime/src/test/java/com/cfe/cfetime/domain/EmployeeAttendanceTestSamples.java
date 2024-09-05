package com.cfe.cfetime.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeAttendanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmployeeAttendance getEmployeeAttendanceSample1() {
        return new EmployeeAttendance().id(1L).comments("comments1");
    }

    public static EmployeeAttendance getEmployeeAttendanceSample2() {
        return new EmployeeAttendance().id(2L).comments("comments2");
    }

    public static EmployeeAttendance getEmployeeAttendanceRandomSampleGenerator() {
        return new EmployeeAttendance().id(longCount.incrementAndGet()).comments(UUID.randomUUID().toString());
    }
}
