package ceiba.com.co.paciente.servicio;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;

public class ServicioEliminarPaciente {

    private final RepositorioPaciente repositorioPaciente;

    public ServicioEliminarPaciente(RepositorioPaciente repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    public void ejecutar(Long numeroDocumento) {
        if (!this.repositorioPaciente.existeConNumeroDocumento(numeroDocumento)) {
            throw new ExcepcionSinDatos("No existe un paciente con el número de documento ingresado: " + numeroDocumento);
        }
        this.repositorioPaciente.eliminar(numeroDocumento);
    }
}
