package org.ceiba.hu03.repository;

import jakarta.persistence.PersistenceException;
import org.ceiba.hu03.infrastructure.persistence.PersonaEntity;
import org.ceiba.hu03.infrastructure.persistence.PersonaJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PersonaRepositoryTest {

    @Autowired
    private PersonaJpaRepository personaJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private static final Long CEDULA_EXISTENTE = 123456L;
    private static final String NOMBRE_EXISTENTE = "Valentina";
    private static final String APELLIDO_EXISTENTE = "Vargas";
    private static final String EMAIL_EXISTENTE = "vale@email.com";
    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(2000, 1, 1);

    @Test
    void should_SaveAndFindPersonaSuccessfully_When_ValidPersonaIsPersisted() {
        // Arrange
        PersonaEntity persona = PersonaEntity.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        // Act
        PersonaEntity guardada = personaJpaRepository.save(persona);

        // Assert
        assertNotNull(guardada);
        assertEquals(CEDULA_EXISTENTE, guardada.getCedula());

        PersonaEntity encontrada = personaJpaRepository.findById(CEDULA_EXISTENTE).orElse(null);
        assertNotNull(encontrada);
        assertEquals(NOMBRE_EXISTENTE, encontrada.getNombre());
        assertEquals(APELLIDO_EXISTENTE, encontrada.getApellido());
    }

    @Test
    void should_ReturnTrue_When_EmailExists() {
        // Arrange
        PersonaEntity persona = PersonaEntity.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();
        entityManager.persistAndFlush(persona);

        // Act
        boolean existe = personaJpaRepository.existsByEmail(EMAIL_EXISTENTE);

        // Assert
        assertTrue(existe);
    }

    @Test
    void should_ReturnFalse_When_EmailDoesNotExist() {
        // Act
        boolean existe = personaJpaRepository.existsByEmail("inexistente@email.com");

        // Assert
        assertFalse(existe);
    }

    @Test
    void should_ReturnPersonasOrderedByApellidoAsc_When_FindAllByOrderByApellidoAscIsCalled() {
        // Arrange
        PersonaEntity p1 = PersonaEntity.builder()
                .cedula(111L)
                .nombre("Carlos")
                .apellido("Zapata")
                .email("carlos@email.com")
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        PersonaEntity p2 = PersonaEntity.builder()
                .cedula(222L)
                .nombre("Ana")
                .apellido("Alvarez")
                .email("ana@email.com")
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        PersonaEntity p3 = PersonaEntity.builder()
                .cedula(333L)
                .nombre("Beto")
                .apellido("Gomez")
                .email("beto@email.com")
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(p3);
        entityManager.flush();

        // Act
        List<PersonaEntity> resultado = personaJpaRepository.findAllByOrderByApellidoAsc();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Alvarez", resultado.get(0).getApellido());
        assertEquals("Gomez", resultado.get(1).getApellido());
        assertEquals("Zapata", resultado.get(2).getApellido());
    }

    @Test
    void should_ThrowPersistenceException_When_CedulaIsDuplicate() {
        // Arrange
        PersonaEntity p1 = PersonaEntity.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();
        entityManager.persistAndFlush(p1);

        PersonaEntity p2 = PersonaEntity.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre("Otro")
                .apellido("Nombre")
                .email("otro@email.com")
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        // Act & Assert
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(p2);
        });
    }
}
