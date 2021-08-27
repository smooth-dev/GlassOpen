package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnexeOutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnexeOut.class);
        AnnexeOut annexeOut1 = new AnnexeOut();
        annexeOut1.setId("id1");
        AnnexeOut annexeOut2 = new AnnexeOut();
        annexeOut2.setId(annexeOut1.getId());
        assertThat(annexeOut1).isEqualTo(annexeOut2);
        annexeOut2.setId("id2");
        assertThat(annexeOut1).isNotEqualTo(annexeOut2);
        annexeOut1.setId(null);
        assertThat(annexeOut1).isNotEqualTo(annexeOut2);
    }
}
