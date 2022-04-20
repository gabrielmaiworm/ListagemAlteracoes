package com.maiworm.alteracao.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.alteracao.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApartamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apartamento.class);
        Apartamento apartamento1 = new Apartamento();
        apartamento1.setId(1L);
        Apartamento apartamento2 = new Apartamento();
        apartamento2.setId(apartamento1.getId());
        assertThat(apartamento1).isEqualTo(apartamento2);
        apartamento2.setId(2L);
        assertThat(apartamento1).isNotEqualTo(apartamento2);
        apartamento1.setId(null);
        assertThat(apartamento1).isNotEqualTo(apartamento2);
    }
}
