package org.ceiba.hu03.application.usecase;

import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;
import org.ceiba.hu03.domain.exception.PersonaAlreadyExistsException;
import org.ceiba.hu03.domain.exception.EmailAlreadyExistsException;
import org.ceiba.hu03.domain.exception.PersonaNotFoundException;
import org.ceiba.hu03.domain.exception.CedulaInmutableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaUseCasesTest {

    private static final Long CEDULA_EXISTENTE = 123456L;
    private static final Long CEDULA_NO_EXISTENTE = 999999L;
    private static final String NOMBRE_EXISTENTE = "Valentina";
    private static final String APELLIDO_EXISTENTE = "Vargas";
    private static final String EMAIL_EXISTENTE = "vale@email.com";
    private static final String EMAIL_NUEVO = "nuevo@email.com";
    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(2000, 1, 1);

    @Mock
    private PersonaRepositoryPort personaRepositoryPort;

    private CrearPersonaUseCaseImpl crearPersonaUseCase;
    private BuscarPersonaUseCaseImpl buscarPersonaUseCase;
    private ActualizarPersonaUseCaseImpl actualizarPersonaUseCase;
    private EliminarPersonaUseCaseImpl eliminarUseCase;

    @BeforeEach
    void setUp() {
        crearPersonaUseCase = new CrearPersonaUseCaseImpl(personaRepositoryPort);
        buscarPersonaUseCase = new BuscarPersonaUseCaseImpl(personaRepositoryPort);
        actualizarPersonaUseCase = new ActualizarPersonaUseCaseImpl(personaRepositoryPort);
        eliminarUseCase = new EliminarPersonaUseCaseImpl(personaRepositoryPort);
    }

    @Test
    void should_SavePersona_When_CreatedSuccessfully() {
        // Arrange
        Persona personaMock = crearPersonaMock();
        when(personaRepositoryPort.existsById(CEDULA_EXISTENTE)).thenReturn(false);
        when(personaRepositoryPort.existsByEmail(EMAIL_EXISTENTE)).thenReturn(false);
        when(personaRepositoryPort.save(any(Persona.class))).thenReturn(personaMock);

        // Act
        Persona resultado = crearPersonaUseCase.crear(personaMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(CEDULA_EXISTENTE, resultado.getCedula());
        verify(personaRepositoryPort, times(1)).save(any(Persona.class));
    }

    @Test
    void should_ThrowPersonaAlreadyExistsException_When_CedulaAlreadyExists() {
        // Arrange
        Persona personaMock = crearPersonaMock();
        when(personaRepositoryPort.existsById(CEDULA_EXISTENTE)).thenReturn(true);

        // Act & Assert
        assertThrows(PersonaAlreadyExistsException.class, () -> crearPersonaUseCase.crear(personaMock));
        verify(personaRepositoryPort, never()).save(any(Persona.class));
    }

    @Test
    void should_ThrowEmailAlreadyExistsException_When_EmailAlreadyExists() {
        // Arrange
        Persona personaMock = crearPersonaMock();
        when(personaRepositoryPort.existsById(CEDULA_EXISTENTE)).thenReturn(false);
        when(personaRepositoryPort.existsByEmail(EMAIL_EXISTENTE)).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> crearPersonaUseCase.crear(personaMock));
        verify(personaRepositoryPort, never()).save(any(Persona.class));
    }

    @Test
    void should_ReturnPersona_When_IdExists() {
        // Arrange
        Persona personaMock = crearPersonaMock();
        when(personaRepositoryPort.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaMock));

        // Act
        Persona resultado = buscarPersonaUseCase.buscarPorCedula(CEDULA_EXISTENTE);

        // Assert
        assertNotNull(resultado);
        assertEquals(CEDULA_EXISTENTE, resultado.getCedula());
    }

    @Test
    void should_ThrowPersonaNotFoundException_When_IdDoesNotExist() {
        // Arrange
        when(personaRepositoryPort.findById(CEDULA_NO_EXISTENTE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonaNotFoundException.class, () -> buscarPersonaUseCase.buscarPorCedula(CEDULA_NO_EXISTENTE));
    }

    @Test
    void should_ReturnUpdatedPersona_When_UpdatedSuccessfully() {
        // Arrange
        Persona personaExistente = crearPersonaMock();
        Persona personaActualizada = Persona.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre("Vale")
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        when(personaRepositoryPort.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaExistente));
        when(personaRepositoryPort.save(any(Persona.class))).thenReturn(personaActualizada);

        // Act
        Persona resultado = actualizarPersonaUseCase.actualizar(CEDULA_EXISTENTE, personaActualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals("Vale", resultado.getNombre());
    }

    @Test
    void should_ThrowCedulaInmutableException_When_UpdatingCedula() {
        // Arrange
        Persona personaExistente = crearPersonaMock();
        Persona personaConNuevaCedula = Persona.builder()
                .cedula(CEDULA_NO_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        when(personaRepositoryPort.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaExistente));

        // Act & Assert
        assertThrows(CedulaInmutableException.class, () -> actualizarPersonaUseCase.actualizar(CEDULA_EXISTENTE, personaConNuevaCedula));
    }

    @Test
    void should_ValidateNewEmail_When_UpdatingWithDifferentEmail() {
        // Arrange
        Persona personaExistente = crearPersonaMock();
        Persona personaNuevoEmail = Persona.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_NUEVO)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        when(personaRepositoryPort.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaExistente));
        when(personaRepositoryPort.existsByEmail(EMAIL_NUEVO)).thenReturn(false);
        when(personaRepositoryPort.save(any(Persona.class))).thenReturn(personaNuevoEmail);

        // Act
        Persona resultado = actualizarPersonaUseCase.actualizar(CEDULA_EXISTENTE, personaNuevoEmail);

        // Assert
        assertNotNull(resultado);
        verify(personaRepositoryPort, times(1)).existsByEmail(EMAIL_NUEVO);
    }

    @Test
    void should_DeletePersona_When_DeletedSuccessfully() {
        // Arrange
        Persona personaMock = crearPersonaMock();
        when(personaRepositoryPort.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaMock));

        // Act
        Persona resultado = eliminarUseCase.eliminar(CEDULA_EXISTENTE);

        // Assert
        assertNotNull(resultado);
        verify(personaRepositoryPort, times(1)).delete(personaMock);
    }

    private Persona crearPersonaMock() {
        return Persona.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();
    }
}
