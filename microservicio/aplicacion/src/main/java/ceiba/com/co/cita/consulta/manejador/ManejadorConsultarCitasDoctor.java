package ceiba.com.co.cita.consulta.manejador;

import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManejadorConsultarCitasDoctor {

    private final DaoCita daoCita;

    public ManejadorConsultarCitasDoctor(DaoCita daoCita) {
        this.daoCita = daoCita;
    }

    public List<DtoCita> ejecutar(String doctorDocumento) {
        return this.daoCita.buscarCitasPorDoctor(doctorDocumento);
    }
}
