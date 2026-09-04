package ceiba.com.co.paciente.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.modelo.dto.DatosActualizarPaciente;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.modelo.entidad.Genero;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import java.time.LocalDate;

public class ServicioActualizarPaciente {

    private final RepositorioPaciente repositorioPaciente;

    public ServicioActualizarPaciente(RepositorioPaciente repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    public void ejecutar(Long numeroDocumento, DatosActualizarPaciente datos) {
        Paciente pacienteExistente = this.repositorioPaciente.obtener(numeroDocumento)
                .orElseThrow(() -> new ExcepcionSinDatos("No existe el paciente que desea actualizar"));

        if (!pacienteExistente.getCorreoElectronico().equalsIgnoreCase(datos.correoElectronico()) && 
            this.repositorioPaciente.existeConCorreoElectronico(datos.correoElectronico())) {
            throw new ExcepcionDuplicidad("El correo electrónico ya está registrado: " + datos.correoElectronico());
        }

        Paciente pacienteActualizado = pacienteExistente.actualizarDatos(
                datos.nombre(),
                datos.apellido(),
                datos.fechaNacimiento(),
                datos.telefono(),
                datos.correoElectronico(),
                datos.eps(),
                datos.genero()
        );
        this.repositorioPaciente.actualizar(pacienteActualizado);
    }

    public void ejecutar(Long numeroDocumento, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String correoElectronico, String eps, Genero genero) {
        ejecutar(numeroDocumento, new DatosActualizarPaciente(nombre, apellido, fechaNacimiento, telefono, correoElectronico, eps, genero));
    }
}
