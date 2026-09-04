package ceiba.com.co.cita.comando.fabrica;

import ceiba.com.co.cita.comando.ComandoAgendarCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import org.springframework.stereotype.Component;

@Component
public class FabricaCita {

    public Cita crear(ComandoAgendarCita comando) {
        return Cita.builder()
                .conPacienteDocumento(comando.getPacienteDocumento())
                .conDoctorDocumento(comando.getDoctorDocumento())
                .conFechaHora(comando.getFechaHora())
                .conTipoCita(parsearTipoCita(comando.getTipoCita()))
                .conMotivo(comando.getMotivo())
                .build();
    }

    public TipoCita parsearTipoCita(String tipoCitaStr) {
        if (tipoCitaStr == null || tipoCitaStr.isBlank()) {
            throw new ceiba.com.co.excepcion.ExcepcionValorObligatorio("El tipo de cita es obligatorio");
        }
        try {
            return TipoCita.valueOf(tipoCitaStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new ceiba.com.co.excepcion.ExcepcionValorInvalido("El tipo de cita " + tipoCitaStr + " no es válido en el sistema");
        }
    }
}
