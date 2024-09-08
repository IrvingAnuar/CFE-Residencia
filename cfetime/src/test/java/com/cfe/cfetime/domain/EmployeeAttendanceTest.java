package com.cfe.cfetime.domain;

import static com.cfe.cfetime.domain.EmployeeAttendanceTestSamples.*;
import static com.cfe.cfetime.domain.EmployeeTestSamples.*;
import static com.cfe.cfetime.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cfe.cfetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeAttendanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAttendance.class);
        EmployeeAttendance employeeAttendance1 = getEmployeeAttendanceSample1();
        EmployeeAttendance employeeAttendance2 = new EmployeeAttendance();
        assertThat(employeeAttendance1).isNotEqualTo(employeeAttendance2);

        employeeAttendance2.setId(employeeAttendance1.getId());
        assertThat(employeeAttendance1).isEqualTo(employeeAttendance2);

        employeeAttendance2 = getEmployeeAttendanceSample2();
        assertThat(employeeAttendance1).isNotEqualTo(employeeAttendance2);
    }

    @Test
    void employeeTest() throws Exception {
        EmployeeAttendance employeeAttendance = getEmployeeAttendanceRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employeeAttendance.setEmployee(employeeBack);
        assertThat(employeeAttendance.getEmployee()).isEqualTo(employeeBack);

        employeeAttendance.employee(null);
        assertThat(employeeAttendance.getEmployee()).isNull();
    }

    @Test
    void statusTest() throws Exception {
        EmployeeAttendance employeeAttendance = getEmployeeAttendanceRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        employeeAttendance.setStatus(statusBack);
        assertThat(employeeAttendance.getStatus()).isEqualTo(statusBack);

        employeeAttendance.status(null);
        assertThat(employeeAttendance.getStatus()).isNull();
    }
}
