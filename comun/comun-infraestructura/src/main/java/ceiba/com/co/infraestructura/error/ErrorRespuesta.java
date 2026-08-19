package ceiba.com.co.infraestructura.error;

public class ErrorRespuesta {
    
    private String nombreExcepcion;
    private String mensaje;
    
    public ErrorRespuesta(String nombreExcepcion, String mensaje) {
        this.nombreExcepcion = nombreExcepcion;
        this.mensaje = mensaje;
    }

    public String getNombreExcepcion() {
        return nombreExcepcion;
    }

    public String getMensaje() {
        return mensaje;
    }

}
