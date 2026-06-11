package org.ceiba.hu03.infrastructure.persistence;

import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class PersonaSpecifications {

    public static Specification<PersonaEntity> nombreContiene(String nombre) {
        return (root, query, criteriaBuilder) -> {
            if (nombre == null || nombre.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nombre")),
                    "%" + nombre.toLowerCase() + "%"
            );
        };
    }

    public static Specification<PersonaEntity> apellidoContiene(String apellido) {
        return (root, query, criteriaBuilder) -> {
            if (apellido == null || apellido.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("apellido")),
                    "%" + apellido.toLowerCase() + "%"
            );
        };
    }

    public static Specification<PersonaEntity> edadMinima(Integer edadMinima) {
        return (root, query, criteriaBuilder) -> {
            if (edadMinima == null) {
                return criteriaBuilder.conjunction();
            }
            LocalDate fechaLimite = LocalDate.now().minusYears(edadMinima);
            return criteriaBuilder.lessThanOrEqualTo(root.get("fechaNacimiento"), fechaLimite);
        };
    }

    public static Specification<PersonaEntity> edadMaxima(Integer edadMaxima) {
        return (root, query, criteriaBuilder) -> {
            if (edadMaxima == null) {
                return criteriaBuilder.conjunction();
            }
            LocalDate fechaLimite = LocalDate.now().minusYears(edadMaxima);
            return criteriaBuilder.greaterThanOrEqualTo(root.get("fechaNacimiento"), fechaLimite);
        };
    }
}
