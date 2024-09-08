package com.cfe.cfetime.domain;

import static com.cfe.cfetime.domain.VehicleTestSamples.*;
import static com.cfe.cfetime.domain.VehicleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cfe.cfetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicle.class);
        Vehicle vehicle1 = getVehicleSample1();
        Vehicle vehicle2 = new Vehicle();
        assertThat(vehicle1).isNotEqualTo(vehicle2);

        vehicle2.setId(vehicle1.getId());
        assertThat(vehicle1).isEqualTo(vehicle2);

        vehicle2 = getVehicleSample2();
        assertThat(vehicle1).isNotEqualTo(vehicle2);
    }

    @Test
    void vehicleTypeTest() throws Exception {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        VehicleType vehicleTypeBack = getVehicleTypeRandomSampleGenerator();

        vehicle.setVehicleType(vehicleTypeBack);
        assertThat(vehicle.getVehicleType()).isEqualTo(vehicleTypeBack);

        vehicle.vehicleType(null);
        assertThat(vehicle.getVehicleType()).isNull();
    }
}
