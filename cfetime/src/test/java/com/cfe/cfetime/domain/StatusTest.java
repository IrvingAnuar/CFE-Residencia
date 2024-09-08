package com.cfe.cfetime.domain;

import static com.cfe.cfetime.domain.StatusTestSamples.*;
import static com.cfe.cfetime.domain.StatusTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cfe.cfetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Status.class);
        Status status1 = getStatusSample1();
        Status status2 = new Status();
        assertThat(status1).isNotEqualTo(status2);

        status2.setId(status1.getId());
        assertThat(status1).isEqualTo(status2);

        status2 = getStatusSample2();
        assertThat(status1).isNotEqualTo(status2);
    }

    @Test
    void statusTypeTest() throws Exception {
        Status status = getStatusRandomSampleGenerator();
        StatusType statusTypeBack = getStatusTypeRandomSampleGenerator();

        status.setStatusType(statusTypeBack);
        assertThat(status.getStatusType()).isEqualTo(statusTypeBack);

        status.statusType(null);
        assertThat(status.getStatusType()).isNull();
    }
}
