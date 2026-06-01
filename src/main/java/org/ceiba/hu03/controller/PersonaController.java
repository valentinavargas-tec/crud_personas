package org.ceiba.hu03.controller;

import jakarta.validation.Valid;
import org.ceiba.hu03.constants.AppConstants;
import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.dto.PersonaResponse;
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
    public ResponseEntity<PersonaResponse> crearPersona(@Valid @RequestBody Persona persona ) {

        Long idEnviado = persona.getId();

        persona.setId(null);

        String mensajeAdvertenciaId = null;

        if (idEnviado != null) {
            mensajeAdvertenciaId = AppConstants.ID_AUTOGENERADO;
        }

        PersonaResponse respuestaCreada = new PersonaResponse(AppConstants.PERSONA_CREADA, mensajeAdvertenciaId, personaService.guardar(persona));
            return ResponseEntity.status(HttpStatus.CREATED).body(respuestaCreada);
    }


    @GetMapping
    public ResponseEntity<List<Persona>> listarPersonas() {

        List<Persona> personas = personaService.listar();
            return ResponseEntity.ok(personas);

    }

    @GetMapping("/{id}")
    public Persona buscarPersonaPorId(@PathVariable Long id) {
        return personaService.buscarPorId(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponse> actualizarPersona(@PathVariable Long id, @Valid @RequestBody Persona personaActualizada ) {
        Persona persona =  personaService.actualizarPersona(id, personaActualizada);

        String mensajeAdvertenciaId = null;
        if (personaActualizada.getId() != null && !personaActualizada.getId().equals(id)) {
            mensajeAdvertenciaId = AppConstants.ID_NO_MODIFICABLE;
        }

        PersonaResponse respuestaActualizacion = new PersonaResponse(
                        AppConstants.PERSONA_ACTUALIZADA, mensajeAdvertenciaId, persona
                );


        return ResponseEntity.ok(respuestaActualizacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PersonaResponse> eliminarPersona(@PathVariable Long id) {

        PersonaResponse respuestaEliminacion = new PersonaResponse(AppConstants.PERSONA_ELIMINADA, null, personaService.eliminarPersona(id));
            return ResponseEntity.ok((respuestaEliminacion));
    }

}
