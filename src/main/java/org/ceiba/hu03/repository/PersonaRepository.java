package org.ceiba.hu03.repository;

import org.ceiba.hu03.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    List<Persona> findAllByOrderByApellidoAsc();
    boolean existsByEmail(String email);

}
