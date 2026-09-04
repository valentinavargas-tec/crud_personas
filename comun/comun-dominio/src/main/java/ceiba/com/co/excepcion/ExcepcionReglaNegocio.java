package ceiba.com.co.excepcion;

public class ExcepcionReglaNegocio extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExcepcionReglaNegocio(String mensaje) {
        super(mensaje);
    }
}
