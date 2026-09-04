package ceiba.com.co.cita.comando;

import java.time.LocalDateTime;

public class ComandoActualizarCita {

    private LocalDateTime fechaHora;
    private String tipoCita;
    private String motivo;

    public ComandoActualizarCita() {
    }

    public ComandoActualizarCita(LocalDateTime fechaHora, String tipoCita, String motivo) {
        this.fechaHora = fechaHora;
        this.tipoCita = tipoCita;
        this.motivo = motivo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
