package com.maiworm.alteracao.repository;

import com.maiworm.alteracao.domain.Apartamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Apartamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApartamentoRepository extends JpaRepository<Apartamento, Long> {}
