package ceiba.com.co.doctor.servicio;

import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionSinDatos;

public class ServicioDeshabilitarDoctor {

    private static final String DOCTOR_NO_ENCONTRADO = "No existe un doctor con el número de documento %s.";

    private final RepositorioDoctor repositorioDoctor;

    public ServicioDeshabilitarDoctor(RepositorioDoctor repositorio) {
        this.repositorioDoctor = repositorio;
    }

    public void ejecutar(String numeroDocumento) {
        Doctor doctor = obtenerDoctorOExcepcion(numeroDocumento);
        doctor.deshabilitar();
        this.repositorioDoctor.deshabilitar(numeroDocumento);
    }

    private Doctor obtenerDoctorOExcepcion(String numeroDocumento) {
        return this.repositorioDoctor.obtenerPorNumeroDocumento(numeroDocumento)
                .orElseThrow(()-> new ExcepcionSinDatos(String.format(DOCTOR_NO_ENCONTRADO, numeroDocumento)));
    }
}
