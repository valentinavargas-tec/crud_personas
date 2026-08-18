package org.ceiba.hu03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.ceiba.hu03.constants.AppConstants;
import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.dto.PersonaDTO;
import org.ceiba.hu03.dto.PersonaResponse;
import org.ceiba.hu03.exception.CedulaInmutableException;
import org.ceiba.hu03.exception.EmailAlreadyExistsException;
import org.ceiba.hu03.exception.PersonaAlreadyExistsException;
import org.ceiba.hu03.exception.PersonaNotFoundException;
import org.ceiba.hu03.repository.PersonaRepository;
import org.ceiba.hu03.service.PersonaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    private static final Long CEDULA_EXISTENTE = 123456L;
    private static final Long CEDULA_NO_EXISTENTE = 999999L;
    private static final String NOMBRE_EXISTENTE = "Valentina";
    private static final String APELLIDO_EXISTENTE = "Vargas";
    private static final String EMAIL_EXISTENTE = "vale@email.com";
    private static final String EMAIL_NUEVO = "nuevo@email.com";
    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(2000, 1, 1);

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaServiceImpl personaService;

    //Tests para crear persona exitosamente
    @Test
    void should_ReturnPersonaResponse_When_PersonaIsCreatedSuccessfully() {
        //Arrange
        Persona personaMock = Persona.builder()
            .cedula(CEDULA_EXISTENTE)
            .nombre(NOMBRE_EXISTENTE)
            .apellido(APELLIDO_EXISTENTE)
            .email(EMAIL_EXISTENTE)
            .fechaNacimiento(FECHA_NACIMIENTO)
            .build();
        
        when(personaRepository.existsById(CEDULA_EXISTENTE)).thenReturn(false);
        when(personaRepository.existsByEmail(EMAIL_EXISTENTE)).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenReturn(personaMock);

        //Act
        PersonaResponse resultado = personaService.crear(personaMock);

        //Assert
        assertNotNull(resultado);
        assertEquals(AppConstants.PERSONA_CREADA, resultado.getMensaje());
        assertNotNull(resultado.getPersona());
        assertEquals(CEDULA_EXISTENTE, resultado.getPersona().getCedula());
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    //Tests para crear persona con datos inválidos (casos de error)
    @Test
    void should_ThrowPersonaAlreadyExistsException_When_CedulaAlreadyExists() {
        Persona personaMock = crearPersonaMock();
        
        when(personaRepository.existsById(CEDULA_EXISTENTE)).thenReturn(true);

        assertThrows(PersonaAlreadyExistsException.class, () -> personaService.crear(personaMock));
        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    void should_ThrowEmailAlreadyExistsException_When_EmailAlreadyExists() {
        Persona personaMock = crearPersonaMock();
        
        when(personaRepository.existsById(CEDULA_EXISTENTE)).thenReturn(false);
        when(personaRepository.existsByEmail(EMAIL_EXISTENTE)).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> personaService.crear(personaMock));
        verify(personaRepository, never()).save(any(Persona.class));
    }

    //Tests para buscar persona 
    // existente
    @Test
    void should_ReturnPersona_When_IdExists() {
        Persona personaMock = crearPersonaMock();
        when(personaRepository.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaMock));

        Persona resultado = personaService.buscarPorCedula(CEDULA_EXISTENTE);

        assertNotNull(resultado);
        assertEquals(CEDULA_EXISTENTE, resultado.getCedula());
    }

    //Tests para buscar persona 
    // no existente
    @Test
    void should_ThrowPersonaNotFoundException_When_IdDoesNotExist() {
        when(personaRepository.findById(CEDULA_NO_EXISTENTE)).thenReturn(Optional.empty());

        assertThrows(PersonaNotFoundException.class, () -> personaService.buscarPorCedula(CEDULA_NO_EXISTENTE));
    }

    // Tests para listar personas
    // funciona para saber cómo me trae la lista completa y cómo convierte cada registro a DTO
    @Test
    void should_ReturnListOfPersonaDTOs_When_ListarIsCalled() {
        Persona p1 = crearPersonaMock();
        Persona p2 = Persona.builder()
            .cedula(789012L)
            .nombre("Carlos")
            .apellido("Zapata")
            .email("carlos@email.com")
            .fechaNacimiento(LocalDate.of(1995, 5, 10))
            .build();

        List<Persona> listaMock = List.of(p1, p2);

        when(personaRepository.findAllByOrderByApellidoAsc()).thenReturn(listaMock);

        List<PersonaDTO> resultado = personaService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(APELLIDO_EXISTENTE, resultado.get(0).getApellido());
    }

    // Tests para actualizar persona
    @Test
    void should_ReturnPersonaResponse_When_PersonaIsUpdatedSuccessfully() {
        Persona personaExistente = crearPersonaMock();
        Persona personaActualizada = Persona.builder()
            .cedula(CEDULA_EXISTENTE)
            .nombre("Vale")
            .apellido(APELLIDO_EXISTENTE)
            .email(EMAIL_EXISTENTE)
            .fechaNacimiento(FECHA_NACIMIENTO)
            .build();

        when(personaRepository.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaExistente));
        when(personaRepository.save(any(Persona.class))).thenReturn(personaActualizada);

        PersonaResponse resultado = personaService.actualizar(CEDULA_EXISTENTE, personaActualizada);

        assertNotNull(resultado);
        assertEquals("Vale", resultado.getPersona().getNombre());
    }

    // Tests para actualizar persona
    // fallido por intentar modificar la cédula
    @Test
    void should_ThrowCedulaInmutableException_When_TryingToUpdateCedula() {
        Persona personaExistente = crearPersonaMock();
            Persona personaConNuevaCedula = Persona.builder()
            .cedula(CEDULA_NO_EXISTENTE)
            .nombre(NOMBRE_EXISTENTE)
            .apellido(APELLIDO_EXISTENTE)
            .email(EMAIL_EXISTENTE)
            .fechaNacimiento(FECHA_NACIMIENTO)
            .build();

        when(personaRepository.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaExistente));

        assertThrows(CedulaInmutableException.class, () -> personaService.actualizar(CEDULA_EXISTENTE, personaConNuevaCedula));
    }

    // Tests para actualizar persona
    // exitoso cambiando el correo electrónico (valida si el nuevo correo no está repetido)
    @Test
    void should_ValidateNewEmail_When_PersonaUpdatesWithDifferentEmail() {
        Persona personaExistente = crearPersonaMock();
        Persona personaNuevoEmail = Persona.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_NUEVO)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        when(personaRepository.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaExistente));
        when(personaRepository.existsByEmail(EMAIL_NUEVO)).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenReturn(personaNuevoEmail);

        PersonaResponse resultado = personaService.actualizar(CEDULA_EXISTENTE, personaNuevoEmail);

        assertNotNull(resultado);
        verify(personaRepository, times(1)).existsByEmail(EMAIL_NUEVO);
    }

    // Tests para buscar persona DTO
    // funciona para saber cómo imprime el DTO y cómo me trae los datos
    @Test
    void should_ReturnPersonaDTO_When_BuscarPorCedulaDTOIsCalled() {
        Persona personaMock = crearPersonaMock();
        when(personaRepository.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaMock));

        PersonaDTO resultado = personaService.buscarPorCedulaDTO(CEDULA_EXISTENTE);

        assertNotNull(resultado);
        assertEquals(NOMBRE_EXISTENTE, resultado.getNombre());
    }

    // Tests para eliminar persona
    @Test
    void should_ReturnPersonaResponse_When_PersonaIsDeletedSuccessfully() {
        Persona personaMock = crearPersonaMock();
        
        when(personaRepository.findById(CEDULA_EXISTENTE)).thenReturn(Optional.of(personaMock));

        PersonaResponse resultado = personaService.eliminar(CEDULA_EXISTENTE);

        assertNotNull(resultado);
        verify(personaRepository, times(1)).delete(personaMock);
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

