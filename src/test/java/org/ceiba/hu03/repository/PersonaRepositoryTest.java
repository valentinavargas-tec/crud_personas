package org.ceiba.hu03.repository;

import org.ceiba.hu03.domain.Persona;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonaRepositoryTest {

    @Autowired
    private PersonaRepository personaRepository;

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
        Persona persona = crearPersona(CEDULA_EXISTENTE, NOMBRE_EXISTENTE, APELLIDO_EXISTENTE, EMAIL_EXISTENTE);

        // Act
        Persona guardada = personaRepository.save(persona);

        // Assert
        assertNotNull(guardada);
        assertEquals(CEDULA_EXISTENTE, guardada.getCedula());

        Persona encontrada = personaRepository.findById(CEDULA_EXISTENTE).orElse(null);
        assertNotNull(encontrada);
        assertEquals(NOMBRE_EXISTENTE, encontrada.getNombre());
        assertEquals(APELLIDO_EXISTENTE, encontrada.getApellido());
    }

    @Test
    void should_ReturnTrue_When_EmailExists() {
        Persona persona = crearPersona(CEDULA_EXISTENTE, NOMBRE_EXISTENTE, APELLIDO_EXISTENTE, EMAIL_EXISTENTE);
        entityManager.persistAndFlush(persona);

        boolean existe = personaRepository.existsByEmail(EMAIL_EXISTENTE);

        assertTrue(existe);
    }

    @Test
    void should_ReturnFalse_When_EmailDoesNotExist() {
        boolean existe = personaRepository.existsByEmail("inexistente@email.com");

        assertFalse(existe);
    }

    @Test
    void should_ReturnPersonasOrderedByApellidoAsc_When_FindAllByOrderByApellidoAscIsCalled() {
        // Arrange
        Persona p1 = crearPersona(111L, "Carlos", "Zapata", "carlos@email.com");
        Persona p2 = crearPersona(222L, "Ana", "Alvarez", "ana@email.com");
        Persona p3 = crearPersona(333L, "Beto", "Gomez", "beto@email.com");

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(p3);
        entityManager.flush();

        List<Persona> resultado = personaRepository.findAllByOrderByApellidoAsc();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Alvarez", resultado.get(0).getApellido());
        assertEquals("Gomez", resultado.get(1).getApellido());
        assertEquals("Zapata", resultado.get(2).getApellido());
    }

    @Test
    void should_ThrowPersistenceException_When_CedulaIsDuplicate() {

        Persona p1 = crearPersona(CEDULA_EXISTENTE, NOMBRE_EXISTENTE, APELLIDO_EXISTENTE, EMAIL_EXISTENTE);
        entityManager.persistAndFlush(p1);

        Persona p2 = crearPersona(CEDULA_EXISTENTE, "Otro", "Nombre", "otro@email.com");

        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(p2);
        });
    }

    private Persona crearPersona(Long cedula, String nombre, String apellido, String email) {
        return Persona.builder()
                .cedula(cedula)
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();
    }
}
