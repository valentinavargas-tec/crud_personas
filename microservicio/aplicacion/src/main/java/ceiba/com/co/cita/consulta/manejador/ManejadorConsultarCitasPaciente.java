package ceiba.com.co.cita.consulta.manejador;

import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManejadorConsultarCitasPaciente {

    private final DaoCita daoCita;

    public ManejadorConsultarCitasPaciente(DaoCita daoCita) {
        this.daoCita = daoCita;
    }

    public List<DtoCita> ejecutar(String pacienteDocumento) {
        return this.daoCita.buscarCitasPorPaciente(pacienteDocumento);
    }
}
