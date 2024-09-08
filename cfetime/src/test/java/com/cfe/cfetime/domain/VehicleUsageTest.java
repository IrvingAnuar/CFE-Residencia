package com.cfe.cfetime.domain;

import static com.cfe.cfetime.domain.EmployeeTestSamples.*;
import static com.cfe.cfetime.domain.StatusTestSamples.*;
import static com.cfe.cfetime.domain.VehicleTestSamples.*;
import static com.cfe.cfetime.domain.VehicleUsageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cfe.cfetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleUsageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleUsage.class);
        VehicleUsage vehicleUsage1 = getVehicleUsageSample1();
        VehicleUsage vehicleUsage2 = new VehicleUsage();
        assertThat(vehicleUsage1).isNotEqualTo(vehicleUsage2);

        vehicleUsage2.setId(vehicleUsage1.getId());
        assertThat(vehicleUsage1).isEqualTo(vehicleUsage2);

        vehicleUsage2 = getVehicleUsageSample2();
        assertThat(vehicleUsage1).isNotEqualTo(vehicleUsage2);
    }

    @Test
    void vehicleTest() throws Exception {
        VehicleUsage vehicleUsage = getVehicleUsageRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        vehicleUsage.setVehicle(vehicleBack);
        assertThat(vehicleUsage.getVehicle()).isEqualTo(vehicleBack);

        vehicleUsage.vehicle(null);
        assertThat(vehicleUsage.getVehicle()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        VehicleUsage vehicleUsage = getVehicleUsageRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        vehicleUsage.setEmployee(employeeBack);
        assertThat(vehicleUsage.getEmployee()).isEqualTo(employeeBack);

        vehicleUsage.employee(null);
        assertThat(vehicleUsage.getEmployee()).isNull();
    }

    @Test
    void statusTest() throws Exception {
        VehicleUsage vehicleUsage = getVehicleUsageRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        vehicleUsage.setStatus(statusBack);
        assertThat(vehicleUsage.getStatus()).isEqualTo(statusBack);

        vehicleUsage.status(null);
        assertThat(vehicleUsage.getStatus()).isNull();
    }
}
