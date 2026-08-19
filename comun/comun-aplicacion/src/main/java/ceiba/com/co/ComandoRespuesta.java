package ceiba.com.co;

public class ComandoRespuesta<T> {

    private final T valor;
    private final String mensaje;

    public ComandoRespuesta(T valor) {
        this.valor = valor;
        this.mensaje = "Petición procesada exitosamente";
    }

    public ComandoRespuesta(T valor, String mensaje) {
        this.valor = valor;
        this.mensaje = mensaje;
    }

    public T getValor() {
        return valor;
    }

    public String getMensaje() {
        return mensaje;
    }
}
