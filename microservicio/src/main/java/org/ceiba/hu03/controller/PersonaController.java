package org.ceiba.hu03.controller;

import jakarta.validation.Valid;
import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.dto.PersonaDTO;
import org.ceiba.hu03.dto.PersonaResponse;
import org.ceiba.hu03.mapper.PersonaMapper;
import org.ceiba.hu03.service.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    public ResponseEntity<PersonaResponse> crearPersona(@Valid @RequestBody PersonaDTO personaDTO) {
        Persona persona = PersonaMapper.toEntity(personaDTO);
        PersonaResponse respuesta = personaService.crear(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<PersonaDTO>> listarPersonas() {
        List<PersonaDTO> personas = personaService.listar();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<PersonaDTO> buscarPersonaPorCedula(@PathVariable Long cedula) {
        PersonaDTO persona = personaService.buscarPorCedulaDTO(cedula);
        return ResponseEntity.ok(persona);
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<PersonaResponse> actualizarPersona(@PathVariable Long cedula, @Valid @RequestBody PersonaDTO personaDTO) {
        Persona personaActualizada = PersonaMapper.toEntity(personaDTO);
        PersonaResponse respuesta = personaService.actualizar(cedula, personaActualizada);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<PersonaResponse> eliminarPersona(@PathVariable Long cedula) {
        PersonaResponse respuesta = personaService.eliminar(cedula);
        return ResponseEntity.ok(respuesta);
    }
}
