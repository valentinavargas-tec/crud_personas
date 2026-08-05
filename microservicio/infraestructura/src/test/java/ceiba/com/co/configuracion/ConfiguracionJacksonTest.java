package ceiba.com.co.configuracion;

import ceiba.com.co.infraestructura.configuracion.ConfiguracionJackson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfiguracionJacksonTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ConfiguracionJackson().objectMapper();
    }

    @Test
    void deberiaSerializarYDeserializarLocalDateCorrectamente() throws JsonProcessingException {
        // Arrange
        LocalDate fechaOriginal = LocalDate.of(1990, 5, 15);

        // Act
        String json = objectMapper.writeValueAsString(fechaOriginal);
        LocalDate fechaDeserializada = objectMapper.readValue(json, LocalDate.class);

        // Assert
        assertEquals("\"1990-05-15\"", json);
        assertEquals(fechaOriginal, fechaDeserializada);
    }

    @Test
    void deberiaSerializarYDeserializarLocalDateTimeCorrectamente() throws JsonProcessingException {
        // Arrange
        LocalDateTime fechaHoraOriginal = LocalDateTime.of(2026, 7, 23, 14, 30, 0);

        // Act
        String json = objectMapper.writeValueAsString(fechaHoraOriginal);
        LocalDateTime fechaHoraDeserializada = objectMapper.readValue(json, LocalDateTime.class);

        // Assert
        assertEquals("\"2026-07-23 14:30:00\"", json);
        assertEquals(fechaHoraOriginal, fechaHoraDeserializada);
    }
}