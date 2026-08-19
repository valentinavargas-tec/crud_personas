package ceiba.com.co.paciente.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.modelo.entidad.Genero;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import java.time.LocalDate;

public class ServicioActualizarPaciente {

    private final RepositorioPaciente repositorioPaciente;

    public ServicioActualizarPaciente(RepositorioPaciente repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    public void ejecutar(Long numeroDocumento, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String correoElectronico, String eps, Genero genero) {
        Paciente pacienteExistente = this.repositorioPaciente.obtener(numeroDocumento);
        if (pacienteExistente == null) {
            throw new ExcepcionSinDatos("No existe el paciente que desea actualizar");
        }

        if (!pacienteExistente.getCorreoElectronico().equals(correoElectronico) && 
            this.repositorioPaciente.existeConCorreoElectronico(correoElectronico)) {
            throw new ExcepcionDuplicidad("El correo electrónico ya está registrado: " + correoElectronico);
        }

        Paciente pacienteActualizado = pacienteExistente.actualizarDatos(nombre, apellido, fechaNacimiento, telefono, correoElectronico, eps, genero);
        this.repositorioPaciente.actualizar(pacienteActualizado);
    }
}
