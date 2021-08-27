package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProduitOutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProduitOut.class);
        ProduitOut produitOut1 = new ProduitOut();
        produitOut1.setId("id1");
        ProduitOut produitOut2 = new ProduitOut();
        produitOut2.setId(produitOut1.getId());
        assertThat(produitOut1).isEqualTo(produitOut2);
        produitOut2.setId("id2");
        assertThat(produitOut1).isNotEqualTo(produitOut2);
        produitOut1.setId(null);
        assertThat(produitOut1).isNotEqualTo(produitOut2);
    }
}
