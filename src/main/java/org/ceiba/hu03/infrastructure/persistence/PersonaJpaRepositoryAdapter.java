package org.ceiba.hu03.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.model.PaginatedResult;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;
import org.ceiba.hu03.infrastructure.controller.validator.SortValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PersonaJpaRepositoryAdapter implements PersonaRepositoryPort {

    private final PersonaJpaRepository personaJpaRepository;
    private final PersonaPersistenceMapper personaPersistenceMapper;

    @Override
    public Persona save(Persona persona) {
        PersonaEntity entity = personaPersistenceMapper.toEntity(persona);
        PersonaEntity savedEntity = personaJpaRepository.save(entity);
        return personaPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public List<Persona> findAllByOrderByApellidoAsc() {
        return personaJpaRepository.findAllByOrderByApellidoAsc().stream()
                .map(personaPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Persona> findById(Long cedula) {
        return personaJpaRepository.findById(cedula)
                .map(personaPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(Long cedula) {
        return personaJpaRepository.existsById(cedula);
    }

    @Override
    public boolean existsByEmail(String email) {
        return personaJpaRepository.existsByEmail(email);
    }

    @Override
    public void delete(Persona persona) {
        PersonaEntity entity = personaPersistenceMapper.toEntity(persona);
        personaJpaRepository.delete(entity);
    }

    @Override
    public PaginatedResult<Persona> findByCriteria(
            String nombre,
            String apellido,
            Integer edadMinima,
            Integer edadMaxima,
            int page,
            int size,
            String sort
    ) {
        Specification<PersonaEntity> spec = Specification.where(PersonaSpecifications.nombreContiene(nombre))
                .and(PersonaSpecifications.apellidoContiene(apellido))
                .and(PersonaSpecifications.edadMinima(edadMinima))
                .and(PersonaSpecifications.edadMaxima(edadMaxima));

        Sort sortObj = SortValidator.toSpringSort(sort);

        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<PersonaEntity> entityPage = personaJpaRepository.findAll(spec, pageable);

        List<Persona> content = entityPage.getContent().stream()
                .map(personaPersistenceMapper::toDomain)
                .toList();

        return new PaginatedResult<>(
                content,
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.getNumber(),
                entityPage.getSize()
        );
    }
}
