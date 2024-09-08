package com.cfe.cfetime.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vehicle getVehicleSample1() {
        return new Vehicle().id(1L).descripcion("descripcion1").model("model1").plates("plates1");
    }

    public static Vehicle getVehicleSample2() {
        return new Vehicle().id(2L).descripcion("descripcion2").model("model2").plates("plates2");
    }

    public static Vehicle getVehicleRandomSampleGenerator() {
        return new Vehicle()
            .id(longCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .plates(UUID.randomUUID().toString());
    }
}
