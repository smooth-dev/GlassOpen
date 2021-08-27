package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttributClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributClient.class);
        AttributClient attributClient1 = new AttributClient();
        attributClient1.setId("id1");
        AttributClient attributClient2 = new AttributClient();
        attributClient2.setId(attributClient1.getId());
        assertThat(attributClient1).isEqualTo(attributClient2);
        attributClient2.setId("id2");
        assertThat(attributClient1).isNotEqualTo(attributClient2);
        attributClient1.setId(null);
        assertThat(attributClient1).isNotEqualTo(attributClient2);
    }
}
