package ceiba.com.co.excepcion;

public class ExcepcionConflictoHorario extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExcepcionConflictoHorario(String mensaje) {
        super(mensaje);
    }
}
