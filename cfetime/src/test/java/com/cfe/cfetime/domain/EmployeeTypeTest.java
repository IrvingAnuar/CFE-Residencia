package com.cfe.cfetime.domain;

import static com.cfe.cfetime.domain.EmployeeTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cfe.cfetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeType.class);
        EmployeeType employeeType1 = getEmployeeTypeSample1();
        EmployeeType employeeType2 = new EmployeeType();
        assertThat(employeeType1).isNotEqualTo(employeeType2);

        employeeType2.setId(employeeType1.getId());
        assertThat(employeeType1).isEqualTo(employeeType2);

        employeeType2 = getEmployeeTypeSample2();
        assertThat(employeeType1).isNotEqualTo(employeeType2);
    }
}
