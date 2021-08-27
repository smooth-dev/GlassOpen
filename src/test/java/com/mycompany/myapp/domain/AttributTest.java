package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttributTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribut.class);
        Attribut attribut1 = new Attribut();
        attribut1.setId("id1");
        Attribut attribut2 = new Attribut();
        attribut2.setId(attribut1.getId());
        assertThat(attribut1).isEqualTo(attribut2);
        attribut2.setId("id2");
        assertThat(attribut1).isNotEqualTo(attribut2);
        attribut1.setId(null);
        assertThat(attribut1).isNotEqualTo(attribut2);
    }
}
