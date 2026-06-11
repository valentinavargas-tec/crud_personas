package org.ceiba.hu03.infrastructure.controller.validator;

import org.springframework.data.domain.Sort;
import java.util.Set;
import java.util.regex.Pattern;

public final class SortValidator {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "cedula", "nombre", "apellido", "email", "fechaNacimiento"
    );

    // Valida formatos como: "apellido", "apellido,asc" o "apellido,desc"
    private static final Pattern SORT_PATTERN = Pattern.compile("^[a-zA-Z]+(,(asc|desc))?$", Pattern.CASE_INSENSITIVE);

    private SortValidator() {
    }

    public static void validateSortFields(String sort) {
        if (sort == null || sort.isBlank()) {
            return;
        }

        if (!SORT_PATTERN.matcher(sort).matches()) {
            throw new IllegalArgumentException("El formato del parámetro de ordenamiento es inválido o contiene caracteres sospechosos.");
        }

        String[] parts = sort.split(",");
        String property = parts[0].trim();

        if (!ALLOWED_SORT_FIELDS.contains(property)) {
            throw new IllegalArgumentException("El campo de ordenamiento solicitado no está permitido: " + property);
        }
    }

    public static Sort toSpringSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }

        validateSortFields(sort);

        String[] parts = sort.split(",");
        String property = parts[0].trim();
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())) {
            direction = Sort.Direction.DESC;
        }
        return Sort.by(direction, property);
    }
}
