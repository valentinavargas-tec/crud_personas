package ceiba.com.co.cita.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoCancelarCita;
import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.servicio.ServicioCancelarCita;
import org.springframework.stereotype.Component;

@Component
public class ManejadorCancelarCita {

    private final ServicioCancelarCita servicioCancelarCita;

    public ManejadorCancelarCita(ServicioCancelarCita servicioCancelarCita) {
        this.servicioCancelarCita = servicioCancelarCita;
    }

    public ComandoRespuesta<DtoCita> ejecutar(Long idCita, ComandoCancelarCita comando) {
        String motivo = (comando != null) ? comando.getMotivo() : null;
        Cita citaCancelada = this.servicioCancelarCita.ejecutar(idCita, motivo);
        DtoCita dto = new DtoCita(
                citaCancelada.getId(),
                citaCancelada.getPacienteDocumento(),
                citaCancelada.getDoctorDocumento(),
                citaCancelada.getFechaHora(),
                citaCancelada.getTipoCita().name(),
                citaCancelada.getEstado().name(),
                citaCancelada.getMotivo()
        );
        return new ComandoRespuesta<>(dto, "Cita médica cancelada exitosamente");
    }
}
