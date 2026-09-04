package ceiba.com.co.cita.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoActualizarCita;
import ceiba.com.co.cita.comando.fabrica.FabricaCita;
import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.servicio.ServicioActualizarCita;
import org.springframework.stereotype.Component;

@Component
public class ManejadorActualizarCita {

    private final ServicioActualizarCita servicioActualizarCita;
    private final FabricaCita fabricaCita;

    public ManejadorActualizarCita(ServicioActualizarCita servicioActualizarCita, FabricaCita fabricaCita) {
        this.servicioActualizarCita = servicioActualizarCita;
        this.fabricaCita = fabricaCita;
    }

    public ComandoRespuesta<DtoCita> ejecutar(Long idCita, ComandoActualizarCita comando) {
        TipoCita tipoCita = null;
        if (comando.getTipoCita() != null && !comando.getTipoCita().isBlank()) {
            tipoCita = this.fabricaCita.parsearTipoCita(comando.getTipoCita());
        }
        Cita citaActualizada = this.servicioActualizarCita.ejecutar(
                idCita,
                comando.getFechaHora(),
                tipoCita,
                comando.getMotivo()
        );
        DtoCita dto = new DtoCita(
                citaActualizada.getId(),
                citaActualizada.getPacienteDocumento(),
                citaActualizada.getDoctorDocumento(),
                citaActualizada.getFechaHora(),
                citaActualizada.getTipoCita().name(),
                citaActualizada.getEstado().name(),
                citaActualizada.getMotivo()
        );
        return new ComandoRespuesta<>(dto, "Cita médica reprogramada exitosamente");
    }
}
