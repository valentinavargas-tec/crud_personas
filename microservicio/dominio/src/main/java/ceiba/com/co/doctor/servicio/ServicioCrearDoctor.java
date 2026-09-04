package ceiba.com.co.doctor.servicio;

import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioEspecialidad;
import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;

public class ServicioCrearDoctor {

    private static final String LA_ESPECIALIDAD_NO_ES_VALIDA = "La especialidad %s no es una especialidad médica válida en el sistema.";
    private static final String EL_CORREO_YA_EXISTE = "El correo institucional %s ya se encuentra registrado para otro doctor.";
    private static final String LA_TARJETA_YA_EXISTE = "El número de tarjeta profesional %s ya está registrado en el sistema.";

    private final RepositorioDoctor repositorioDoctor;
    private final RepositorioEspecialidad repositorioEspecialidad;

    public ServicioCrearDoctor(RepositorioDoctor repositorioDoctor, RepositorioEspecialidad repositorioEspecialidad) {
        this.repositorioDoctor = repositorioDoctor;
        this.repositorioEspecialidad = repositorioEspecialidad;
    }

    public String ejecutar(Doctor doctor) {
        validarExistenciaEspecialidad(doctor.getEspecialidad());
        validarExistenciaCorreo(doctor.getCorreoInstitucional());
        validarExistenciaTarjetaProfesional(doctor.getTarjetaProfesional());

        this.repositorioDoctor.guardar(doctor);
        return doctor.getNumeroDocumento();
    }

    private void validarExistenciaEspecialidad(String especialidad) {
        boolean existe = this.repositorioEspecialidad.existe(especialidad);
        if (!existe) {
            throw new ExcepcionValorInvalido(String.format(LA_ESPECIALIDAD_NO_ES_VALIDA, especialidad));
        }
    }

    private void validarExistenciaCorreo(String correo) {
        boolean existe = this.repositorioDoctor.existePorCorreoInstitucional(correo);
        if (existe) {
            throw new ExcepcionDuplicidad(String.format(EL_CORREO_YA_EXISTE, correo));
        }
    }

    private void validarExistenciaTarjetaProfesional(String tarjeta) {
        boolean existe = this.repositorioDoctor.existePorTarjetaProfesional(tarjeta);
        if (existe) {
            throw new ExcepcionDuplicidad(String.format(LA_TARJETA_YA_EXISTE, tarjeta));
        }
    }
}
