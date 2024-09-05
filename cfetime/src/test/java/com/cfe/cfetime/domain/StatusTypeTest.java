package com.cfe.cfetime.domain;

import static com.cfe.cfetime.domain.StatusTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cfe.cfetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusType.class);
        StatusType statusType1 = getStatusTypeSample1();
        StatusType statusType2 = new StatusType();
        assertThat(statusType1).isNotEqualTo(statusType2);

        statusType2.setId(statusType1.getId());
        assertThat(statusType1).isEqualTo(statusType2);

        statusType2 = getStatusTypeSample2();
        assertThat(statusType1).isNotEqualTo(statusType2);
    }
}
