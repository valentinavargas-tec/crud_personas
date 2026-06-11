package org.ceiba.hu03.infrastructure.config;

import org.ceiba.hu03.application.usecase.*;
import org.ceiba.hu03.domain.port.in.*;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonaBeanConfig {

    @Bean
    public CrearPersonaUseCase crearPersonaUseCase(PersonaRepositoryPort personaRepositoryPort) {
        return new CrearPersonaUseCaseImpl(personaRepositoryPort);
    }


    @Bean
    public BuscarPersonaUseCase buscarPersonaUseCase(PersonaRepositoryPort personaRepositoryPort) {
        return new BuscarPersonaUseCaseImpl(personaRepositoryPort);
    }

    @Bean
    public ActualizarPersonaUseCase actualizarPersonaUseCase(PersonaRepositoryPort personaRepositoryPort) {
        return new ActualizarPersonaUseCaseImpl(personaRepositoryPort);
    }

    @Bean
    public EliminarPersonaUseCase eliminarPersonaUseCase(PersonaRepositoryPort personaRepositoryPort) {
        return new EliminarPersonaUseCaseImpl(personaRepositoryPort);
    }

    @Bean
    public BuscarPersonaAvanzadaUseCase buscarPersonaAvanzadaUseCase(PersonaRepositoryPort personaRepositoryPort) {
        return new BuscarPersonaAvanzadaUseCaseImpl(personaRepositoryPort);
    }
}
