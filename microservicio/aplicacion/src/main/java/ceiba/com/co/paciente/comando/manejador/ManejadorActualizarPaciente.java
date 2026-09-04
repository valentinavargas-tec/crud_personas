package ceiba.com.co.paciente.comando.manejador;

import ceiba.com.co.paciente.comando.ComandoActualizarPaciente;
import ceiba.com.co.paciente.servicio.ServicioActualizarPaciente;
import ceiba.com.co.paciente.modelo.entidad.Genero;
import org.springframework.stereotype.Component;

import ceiba.com.co.paciente.modelo.dto.DatosActualizarPaciente;

@Component
public class ManejadorActualizarPaciente {

    private final ServicioActualizarPaciente servicioActualizarPaciente;

    public ManejadorActualizarPaciente(ServicioActualizarPaciente servicioActualizarPaciente) {
        this.servicioActualizarPaciente = servicioActualizarPaciente;
    }

    public void ejecutar(Long numeroDocumento, ComandoActualizarPaciente comando) {
        Genero genero = parsearGenero(comando.genero());
        DatosActualizarPaciente datos = new DatosActualizarPaciente(
                comando.nombre(),
                comando.apellido(),
                comando.fechaNacimiento(),
                comando.telefono(),
                comando.correoElectronico(),
                comando.eps(),
                genero
        );
        this.servicioActualizarPaciente.ejecutar(numeroDocumento, datos);
    }

    private Genero parsearGenero(String generoStr) {
        if (generoStr == null || generoStr.isBlank()) {
            throw new ceiba.com.co.excepcion.ExcepcionValorObligatorio("El género es obligatorio");
        }
        try {
            return Genero.valueOf(generoStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new ceiba.com.co.excepcion.ExcepcionValorInvalido("El género " + generoStr + " no es válido");
        }
    }
}
