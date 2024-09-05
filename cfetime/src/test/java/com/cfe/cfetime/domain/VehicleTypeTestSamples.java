package com.cfe.cfetime.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleType getVehicleTypeSample1() {
        return new VehicleType().id(1L).descripcion("descripcion1");
    }

    public static VehicleType getVehicleTypeSample2() {
        return new VehicleType().id(2L).descripcion("descripcion2");
    }

    public static VehicleType getVehicleTypeRandomSampleGenerator() {
        return new VehicleType().id(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString());
    }
}
