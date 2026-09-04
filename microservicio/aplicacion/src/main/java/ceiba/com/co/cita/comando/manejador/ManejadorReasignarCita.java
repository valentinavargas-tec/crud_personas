package ceiba.com.co.cita.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoReasignarCita;
import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.servicio.ServicioReasignarCita;
import org.springframework.stereotype.Component;

@Component
public class ManejadorReasignarCita {

    private final ServicioReasignarCita servicioReasignarCita;

    public ManejadorReasignarCita(ServicioReasignarCita servicioReasignarCita) {
        this.servicioReasignarCita = servicioReasignarCita;
    }

    public ComandoRespuesta<DtoCita> ejecutar(Long idCita, ComandoReasignarCita comando) {
        Cita citaReasignada = this.servicioReasignarCita.ejecutar(idCita, comando.getNuevoDoctorDocumento());
        DtoCita dto = new DtoCita(
                citaReasignada.getId(),
                citaReasignada.getPacienteDocumento(),
                citaReasignada.getDoctorDocumento(),
                citaReasignada.getFechaHora(),
                citaReasignada.getTipoCita().name(),
                citaReasignada.getEstado().name(),
                citaReasignada.getMotivo()
        );
        return new ComandoRespuesta<>(dto, "Cita médica reasignada exitosamente");
    }
}
