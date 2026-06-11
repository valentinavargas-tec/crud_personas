package org.ceiba.hu03.domain.port.out;

import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.model.PaginatedResult;

import java.util.List;
import java.util.Optional;

public interface PersonaRepositoryPort {
    Persona save(Persona persona);
    List<Persona> findAllByOrderByApellidoAsc();
    Optional<Persona> findById(Long cedula);
    boolean existsById(Long cedula);
    boolean existsByEmail(String email);
    void delete(Persona persona);

    PaginatedResult<Persona> findByCriteria(
            String nombre,
            String apellido,
            Integer edadMinima,
            Integer edadMaxima,
            int page,
            int size,
            String sort
    );
}
