package ceiba.com.co.cita.comando;

import java.time.LocalDateTime;

public class ComandoAgendarCita {

    private String pacienteDocumento;
    private String doctorDocumento;
    private LocalDateTime fechaHora;
    private String tipoCita;
    private String motivo;

    public ComandoAgendarCita() {
    }

    public ComandoAgendarCita(String pacienteDocumento, String doctorDocumento,
                               LocalDateTime fechaHora, String tipoCita, String motivo) {
        this.pacienteDocumento = pacienteDocumento;
        this.doctorDocumento = doctorDocumento;
        this.fechaHora = fechaHora;
        this.tipoCita = tipoCita;
        this.motivo = motivo;
    }

    public String getPacienteDocumento() {
        return pacienteDocumento;
    }

    public void setPacienteDocumento(String pacienteDocumento) {
        this.pacienteDocumento = pacienteDocumento;
    }

    public String getDoctorDocumento() {
        return doctorDocumento;
    }

    public void setDoctorDocumento(String doctorDocumento) {
        this.doctorDocumento = doctorDocumento;
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
