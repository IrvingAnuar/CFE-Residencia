package com.cfe.cfetime.domain;

import static com.cfe.cfetime.domain.EmployeeTestSamples.*;
import static com.cfe.cfetime.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cfe.cfetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void statusTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        employee.setStatus(statusBack);
        assertThat(employee.getStatus()).isEqualTo(statusBack);

        employee.status(null);
        assertThat(employee.getStatus()).isNull();
    }
}
