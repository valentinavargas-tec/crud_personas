package org.ceiba.hu03.application.usecase;

import org.ceiba.hu03.domain.exception.InvalidSearchCriteriaException;
import org.ceiba.hu03.domain.model.PaginatedResult;
import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.port.in.BuscarPersonaAvanzadaUseCase;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;

import java.util.Set;

public class BuscarPersonaAvanzadaUseCaseImpl implements BuscarPersonaAvanzadaUseCase {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "cedula", "nombre", "apellido", "email", "fechaNacimiento"
    );

    private final PersonaRepositoryPort personaRepositoryPort;

    public BuscarPersonaAvanzadaUseCaseImpl(PersonaRepositoryPort personaRepositoryPort) {
        this.personaRepositoryPort = personaRepositoryPort;
    }

    @Override
    public PaginatedResult<Persona> buscar(
            String nombre,
            String apellido,
            Integer edadMinima,
            Integer edadMaxima,
            int page,
            int size,
            String sort
    ) {
        // 1. Limitar 'size': Si es > 100, fíjalo en 100. Si es <= 0, fíjalo en 10.
        int validatedSize = size;
        if (size > 100) {
            validatedSize = 100;
        } else if (size <= 0) {
            validatedSize = 10;
        }

        // 2. Limitar 'page': Si es < 0, fíjalo en 0.
        int validatedPage = (page < 0) ? 0 : page;

        // 3. Validación de 'sort' (Lista blanca)
        if (sort != null && !sort.isBlank()) {
            String[] sortParts = sort.split(",");
            String property = sortParts[0].trim();
            if (!ALLOWED_SORT_FIELDS.contains(property)) {
                throw new InvalidSearchCriteriaException("El campo de ordenamiento solicitado no está permitido: " + property);
            }
        }

        // 4. Validación de rango de edad
        if (edadMinima != null && edadMinima < 0) {
            throw new InvalidSearchCriteriaException("La edad mínima no puede ser negativa: " + edadMinima);
        }
        if (edadMaxima != null && edadMaxima < 0) {
            throw new InvalidSearchCriteriaException("La edad máxima no puede ser negativa: " + edadMaxima);
        }
        if (edadMinima != null && edadMaxima != null && edadMinima > edadMaxima) {
            throw new InvalidSearchCriteriaException("La edad mínima no puede ser mayor que la edad máxima: " + edadMinima + " > " + edadMaxima);
        }

        String nombreSanitizado = (nombre != null) ? nombre.trim() : null;
        return personaRepositoryPort.findByCriteria(
                nombreSanitizado,
                apellido,
                edadMinima,
                edadMaxima,
                validatedPage,
                validatedSize,
                sort
        );
    }
}
