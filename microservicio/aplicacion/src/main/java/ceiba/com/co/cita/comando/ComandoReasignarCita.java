package ceiba.com.co.cita.comando;

public class ComandoReasignarCita {

    private String nuevoDoctorDocumento;

    public ComandoReasignarCita() {
    }

    public ComandoReasignarCita(String nuevoDoctorDocumento) {
        this.nuevoDoctorDocumento = nuevoDoctorDocumento;
    }

    public String getNuevoDoctorDocumento() {
        return nuevoDoctorDocumento;
    }

    public void setNuevoDoctorDocumento(String nuevoDoctorDocumento) {
        this.nuevoDoctorDocumento = nuevoDoctorDocumento;
    }
}
