package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnexeGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnexeGroup.class);
        AnnexeGroup annexeGroup1 = new AnnexeGroup();
        annexeGroup1.setId("id1");
        AnnexeGroup annexeGroup2 = new AnnexeGroup();
        annexeGroup2.setId(annexeGroup1.getId());
        assertThat(annexeGroup1).isEqualTo(annexeGroup2);
        annexeGroup2.setId("id2");
        assertThat(annexeGroup1).isNotEqualTo(annexeGroup2);
        annexeGroup1.setId(null);
        assertThat(annexeGroup1).isNotEqualTo(annexeGroup2);
    }
}
