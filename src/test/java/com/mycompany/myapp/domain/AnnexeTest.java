package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnexeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annexe.class);
        Annexe annexe1 = new Annexe();
        annexe1.setId("id1");
        Annexe annexe2 = new Annexe();
        annexe2.setId(annexe1.getId());
        assertThat(annexe1).isEqualTo(annexe2);
        annexe2.setId("id2");
        assertThat(annexe1).isNotEqualTo(annexe2);
        annexe1.setId(null);
        assertThat(annexe1).isNotEqualTo(annexe2);
    }
}
