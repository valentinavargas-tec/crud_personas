package ceiba.com.co.cita.consulta.manejador;

import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.springframework.stereotype.Component;

@Component
public class ManejadorConsultarCita {

    private final DaoCita daoCita;

    public ManejadorConsultarCita(DaoCita daoCita) {
        this.daoCita = daoCita;
    }

    public DtoCita ejecutar(Long idCita) {
        return this.daoCita.buscarPorId(idCita)
                .orElseThrow(() -> new ExcepcionSinDatos(
                        String.format("La cita con id %d no se encuentra registrada en el sistema.", idCita)));
    }
}
