package org.ceiba.hu03.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ceiba.hu03.domain.constant.AppConstants;
import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.model.PaginatedResult;
import org.ceiba.hu03.domain.port.in.*;
import org.ceiba.hu03.infrastructure.controller.dto.PersonaDTO;
import org.ceiba.hu03.infrastructure.controller.dto.PersonaResponse;
import org.ceiba.hu03.infrastructure.controller.dto.PaginatedResponseDTO;
import org.ceiba.hu03.infrastructure.controller.validator.SortValidator;
import org.ceiba.hu03.infrastructure.mapper.PersonaRestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/personas", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersonaController {

    private final CrearPersonaUseCase crearPersonaUseCase;
    private final BuscarPersonaUseCase buscarPersonaUseCase;
    private final ActualizarPersonaUseCase actualizarPersonaUseCase;
    private final EliminarPersonaUseCase eliminarPersonaUseCase;
    private final BuscarPersonaAvanzadaUseCase buscarPersonaAvanzadaUseCase;
    private final PersonaRestMapper personaRestMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaResponse> crearPersona(@Valid @RequestBody PersonaDTO personaDTO) {
        Persona persona = personaRestMapper.toDomain(personaDTO);
        Persona personaCreada = crearPersonaUseCase.crear(persona);
        PersonaResponse respuesta = new PersonaResponse(
                AppConstants.PERSONA_CREADA,
                personaRestMapper.toDTO(personaCreada)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/search")
    public ResponseEntity<PaginatedResponseDTO<PersonaDTO>> buscarPersonasAvanzado(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Integer edadMinima,
            @RequestParam(required = false) Integer edadMaxima,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "cedula,asc") String sort) {
        
        SortValidator.validateSortFields(sort);

        PaginatedResult<Persona> paginatedResult = buscarPersonaAvanzadaUseCase.buscar(
                nombre, apellido, edadMinima, edadMaxima, page, size, sort
        );

        return ResponseEntity.ok(personaRestMapper.toPaginatedResponse(paginatedResult));
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<PersonaDTO> buscarPersonaPorCedula(@PathVariable Long cedula) {
        Persona persona = buscarPersonaUseCase.buscarPorCedula(cedula);
        return ResponseEntity.ok(personaRestMapper.toDTO(persona));
    }

    @PutMapping(value = "/{cedula}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaResponse> actualizarPersona(@PathVariable Long cedula,
                                                             @Valid @RequestBody PersonaDTO personaDTO) {
        Persona personaActualizada = personaRestMapper.toDomain(personaDTO);
        Persona personaResult = actualizarPersonaUseCase.actualizar(cedula, personaActualizada);
        PersonaResponse respuesta = new PersonaResponse(
                AppConstants.PERSONA_ACTUALIZADA,
                personaRestMapper.toDTO(personaResult)
        );
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<PersonaResponse> eliminarPersona(@PathVariable Long cedula) {
        Persona personaEliminada = eliminarPersonaUseCase.eliminar(cedula);
        PersonaResponse respuesta = new PersonaResponse(
                AppConstants.PERSONA_ELIMINADA,
                personaRestMapper.toDTO(personaEliminada)
        );
        return ResponseEntity.ok(respuesta);
    }
}
