package ceiba.com.co.doctor.servicio;

import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionSinDatos;

public class ServicioActualizarDoctor {

    private static final String DOCTOR_NO_ENCONTRADO = "No existe un doctor con el número de documento %s.";
    private static final String CORREO_EN_USO =  "El correo institucional %s ya está siendo usado por otro doctor.";

    private final RepositorioDoctor repositorioDoctor;

    public ServicioActualizarDoctor(RepositorioDoctor repositorioDoctor) {
        this.repositorioDoctor = repositorioDoctor;
    }

    public void ejecutar(Doctor doctor) {
        verificarExistencia(doctor.getNumeroDocumento());
        verificarExistencia(doctor.getCorreoInstitucional(), doctor.getNumeroDocumento());
            this.repositorioDoctor.actualizar(doctor);
    }

    private void verificarExistencia(String numeroDocumento) {
        boolean existe = this.repositorioDoctor.obtenerPorNumeroDocumento(numeroDocumento).isPresent();
        if (!existe) {
            throw new ExcepcionSinDatos(String.format(DOCTOR_NO_ENCONTRADO, numeroDocumento));
        }
    }

    private void verificarExistencia(String correo, String numeroDocumentoExcluido) {
        boolean correoEnUso = this.repositorioDoctor.existeCorreoParaOtroDoctor(correo, numeroDocumentoExcluido);
        if (correoEnUso) {
            throw new ExcepcionDuplicidad(String.format(CORREO_EN_USO, correo));
        }
    }


}
