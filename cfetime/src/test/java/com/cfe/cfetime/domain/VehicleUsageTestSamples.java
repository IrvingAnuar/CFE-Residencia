package com.cfe.cfetime.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleUsageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleUsage getVehicleUsageSample1() {
        return new VehicleUsage().id(1L).comments("comments1");
    }

    public static VehicleUsage getVehicleUsageSample2() {
        return new VehicleUsage().id(2L).comments("comments2");
    }

    public static VehicleUsage getVehicleUsageRandomSampleGenerator() {
        return new VehicleUsage().id(longCount.incrementAndGet()).comments(UUID.randomUUID().toString());
    }
}
