package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttributOutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributOut.class);
        AttributOut attributOut1 = new AttributOut();
        attributOut1.setId("id1");
        AttributOut attributOut2 = new AttributOut();
        attributOut2.setId(attributOut1.getId());
        assertThat(attributOut1).isEqualTo(attributOut2);
        attributOut2.setId("id2");
        assertThat(attributOut1).isNotEqualTo(attributOut2);
        attributOut1.setId(null);
        assertThat(attributOut1).isNotEqualTo(attributOut2);
    }
}
