package ceiba.com.co.controlador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.comando.ComandoActualizarPersona;
import ceiba.com.co.comando.ComandoEliminarPersona;
import ceiba.com.co.comando.ComandoPersona;
import ceiba.com.co.comando.manejador.ManejadorActualizarPersona;
import ceiba.com.co.comando.manejador.ManejadorCrearPersona;
import ceiba.com.co.comando.manejador.ManejadorEliminarPersona;
import ceiba.com.co.controlador.doc.ComandoControladorPersonaApiDoc;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personas")
public class ComandoControladorPersona implements ComandoControladorPersonaApiDoc {

    private final ManejadorCrearPersona manejadorCrearPersona;
    private final ManejadorActualizarPersona manejadorActualizarPersona;
    private final ManejadorEliminarPersona manejadorEliminarPersona;

    public ComandoControladorPersona(ManejadorCrearPersona manejadorCrearPersona,
            ManejadorActualizarPersona manejadorActualizarPersona,
            ManejadorEliminarPersona manejadorEliminarPersona) {
        this.manejadorCrearPersona = manejadorCrearPersona;
        this.manejadorActualizarPersona = manejadorActualizarPersona;
        this.manejadorEliminarPersona = manejadorEliminarPersona;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Override
    public ComandoRespuesta<Long> crear(@RequestBody ComandoPersona comandoPersona) {
        return this.manejadorCrearPersona.ejecutar(comandoPersona);
    }

    @PutMapping("/{cedula}")
    @Override
    public ComandoRespuesta<Long> actualizar(@PathVariable("cedula") Long cedula, @RequestBody ComandoActualizarPersona comando) {
        this.manejadorActualizarPersona.ejecutar(cedula, comando);
        return new ComandoRespuesta<>(cedula);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{cedula}")
    @Override
    public void eliminar(@PathVariable("cedula") Long cedula) {
        this.manejadorEliminarPersona.ejecutar(new ComandoEliminarPersona(cedula));
    }
}
