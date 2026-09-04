package ceiba.com.co.cita.comando;

public class ComandoCancelarCita {

    private String motivo;

    public ComandoCancelarCita() {
    }

    public ComandoCancelarCita(String motivo) {
        this.motivo = motivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
