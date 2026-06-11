package org.ceiba.hu03.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PersonaJpaRepository extends JpaRepository<PersonaEntity, Long>, JpaSpecificationExecutor<PersonaEntity> {
    List<PersonaEntity> findAllByOrderByApellidoAsc();
    boolean existsByEmail(String email);
}
