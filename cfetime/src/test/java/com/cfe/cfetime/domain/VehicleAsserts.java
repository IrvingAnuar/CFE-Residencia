package com.cfe.cfetime.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class VehicleAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehicleAllPropertiesEquals(Vehicle expected, Vehicle actual) {
        assertVehicleAutoGeneratedPropertiesEquals(expected, actual);
        assertVehicleAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehicleAllUpdatablePropertiesEquals(Vehicle expected, Vehicle actual) {
        assertVehicleUpdatableFieldsEquals(expected, actual);
        assertVehicleUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehicleAutoGeneratedPropertiesEquals(Vehicle expected, Vehicle actual) {
        assertThat(expected)
            .as("Verify Vehicle auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehicleUpdatableFieldsEquals(Vehicle expected, Vehicle actual) {
        assertThat(expected)
            .as("Verify Vehicle relevant properties")
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()))
            .satisfies(e -> assertThat(e.getModel()).as("check model").isEqualTo(actual.getModel()))
            .satisfies(e -> assertThat(e.getPlates()).as("check plates").isEqualTo(actual.getPlates()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehicleUpdatableRelationshipsEquals(Vehicle expected, Vehicle actual) {
        assertThat(expected)
            .as("Verify Vehicle relationships")
            .satisfies(e -> assertThat(e.getVehicleType()).as("check vehicleType").isEqualTo(actual.getVehicleType()));
    }
}
