package ceiba.com.co.modelo.dto;

import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import java.util.Set;
import java.util.regex.Pattern;

public class CriteriosBusquedaPersona {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;
    private static final Pattern SORT_PATTERN = Pattern.compile("^[a-zA-Z]+(,(asc|desc))?$", Pattern.CASE_INSENSITIVE);
    private static final Set<String> CAMPOS_ORDENAMIENTO_PERMITIDOS = Set.of(
            "cedula", "nombre", "apellido", "email", "fechaNacimiento"
    );

    private final String nombre;
    private final String apellido;
    private final Integer edadMinima;
    private final Integer edadMaxima;
    private final int page;
    private final int size;
    private final String sort;

    public CriteriosBusquedaPersona(String nombre, String apellido, Integer edadMinima, Integer edadMaxima, int page,
            int size, String sort) {
        this.nombre = limpiar(nombre);
        this.apellido = limpiar(apellido);

        if (edadMinima != null && edadMinima < 0) {
            throw new ExcepcionValorInvalido("La edad mínima no puede ser negativa: " + edadMinima);
        }
        if (edadMaxima != null && edadMaxima < 0) {
            throw new ExcepcionValorInvalido("La edad máxima no puede ser negativa: " + edadMaxima);
        }
        if (edadMinima != null && edadMaxima != null && edadMinima > edadMaxima) {
            throw new ExcepcionValorInvalido(
                    "La edad mínima no puede ser mayor que la edad máxima: " + edadMinima + " > " + edadMaxima);
        }

        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;

        this.page = Math.max(0, page);
        this.size = (size <= 0) ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        this.sort = validarOrdenamiento(sort);
    }

    private String validarOrdenamiento(String sort) {
        if (sort == null || sort.isBlank()) {
            return null;
        }
        String sortLimpio = limpiar(sort);
        if (!SORT_PATTERN.matcher(sortLimpio).matches()) {
            throw new ExcepcionValorInvalido(
                    "El formato del parámetro de ordenamiento es inválido o contiene caracteres sospechosos.");
        }
        String property = limpiar(sortLimpio.split(",")[0]);

        if (!CAMPOS_ORDENAMIENTO_PERMITIDOS.contains(property)) {
            throw new ExcepcionValorInvalido(
                    "El campo de ordenamiento solicitado no está permitido: " + property);
        }
        return sortLimpio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getEdadMinima() {
        return edadMinima;
    }

    public Integer getEdadMaxima() {
        return edadMaxima;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSort() {
        return sort;
    }

    private static String limpiar(String valor) {
        return valor != null ? valor.trim() : null;
    }
}
