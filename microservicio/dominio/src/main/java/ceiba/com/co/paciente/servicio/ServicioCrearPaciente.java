package ceiba.com.co.paciente.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;

public class ServicioCrearPaciente {

    private final RepositorioPaciente repositorioPaciente;

    public ServicioCrearPaciente(RepositorioPaciente repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    public Long ejecutar(Paciente paciente) {
        if (this.repositorioPaciente.existeConNumeroDocumento(paciente.getNumeroDocumento())) {
            throw new ExcepcionDuplicidad("El número de documento ya está registrado: " + paciente.getNumeroDocumento());
        }
        if (this.repositorioPaciente.existeConCorreoElectronico(paciente.getCorreoElectronico())) {
            throw new ExcepcionDuplicidad("El correo electrónico ya está registrado: " + paciente.getCorreoElectronico());
        }
        return this.repositorioPaciente.guardar(paciente);
    }
}
