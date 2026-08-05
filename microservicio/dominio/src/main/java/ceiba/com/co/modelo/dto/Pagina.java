package ceiba.com.co.modelo.dto;

import java.util.List;

public class Pagina<T> {
    private final List<T> contenido;
    private final long totalElementos;
    private final int totalPaginas;
    private final int numeroPagina;
    private final int tamanoPagina;

    public Pagina(List<T> contenido, long totalElementos, int totalPaginas, int numeroPagina, int tamanoPagina) {
        this.contenido = contenido != null ? List.copyOf(contenido) : List.of();
        this.totalElementos = totalElementos;
        this.totalPaginas = totalPaginas;
        this.numeroPagina = numeroPagina;
        this.tamanoPagina = tamanoPagina;
    }

    public List<T> getContenido() {
        return contenido;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getNumeroPagina() {
        return numeroPagina;
    }

    public int getTamanoPagina() {
        return tamanoPagina;
    }
}
