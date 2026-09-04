package ceiba.com.co.cita.servicio;

import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.excepcion.ExcepcionSinDatos;

public class ServicioCancelarCita {

    private static final String CITA_NO_ENCONTRADA = "La cita médica con identificador %s no se encuentra registrada en el sistema.";

    private final RepositorioCita repositorioCita;

    public ServicioCancelarCita(RepositorioCita repositorioCita) {
        this.repositorioCita = repositorioCita;
    }

    public Cita ejecutar(Long idCita, String motivoCancelacion) {
        Cita cita = this.repositorioCita.obtenerPorId(idCita)
                .orElseThrow(() -> new ExcepcionSinDatos(String.format(CITA_NO_ENCONTRADA, idCita)));

        cita.cancelar(motivoCancelacion);
        this.repositorioCita.actualizar(cita);

        return cita;
    }
}
