package ceiba.com.co.cita.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoAgendarCita;
import ceiba.com.co.cita.comando.fabrica.FabricaCita;
import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.servicio.ServicioAgendarCita;
import org.springframework.stereotype.Component;

@Component
public class ManejadorAgendarCita {

    private final ServicioAgendarCita servicioAgendarCita;
    private final FabricaCita fabricaCita;

    public ManejadorAgendarCita(ServicioAgendarCita servicioAgendarCita, FabricaCita fabricaCita) {
        this.servicioAgendarCita = servicioAgendarCita;
        this.fabricaCita = fabricaCita;
    }

    public ComandoRespuesta<DtoCita> ejecutar(ComandoAgendarCita comando) {
        Cita cita = this.fabricaCita.crear(comando);
        Long idGenerado = this.servicioAgendarCita.ejecutar(cita);
        DtoCita dto = new DtoCita(
                idGenerado,
                cita.getPacienteDocumento(),
                cita.getDoctorDocumento(),
                cita.getFechaHora(),
                cita.getTipoCita().name(),
                cita.getEstado().name(),
                cita.getMotivo()
        );
        return new ComandoRespuesta<>(dto, "Cita médica agendada exitosamente");
    }
}
