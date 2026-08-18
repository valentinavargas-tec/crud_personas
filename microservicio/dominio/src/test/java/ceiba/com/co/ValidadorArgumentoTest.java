package ceiba.com.co;

import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorArgumentoTest {

    @Test
    void deberia_NoLanzarExcepcion_Cuando_ValidarObligatorioConValorValido() {
        // Arrange
        String valorValido = "Texto valido";

        // Act & Assert
        assertDoesNotThrow(() -> ValidadorArgumento.validarObligatorio(valorValido, "Error"));
    }

    @Test
    void deberia_LanzarExcepcionValorObligatorio_Cuando_ValidarObligatorioConValorNulo() {
        // Arrange
        Object valorNulo = null;

        // Act & Assert
        ExcepcionValorObligatorio excepcion = assertThrows(
                ExcepcionValorObligatorio.class,
                () -> ValidadorArgumento.validarObligatorio(valorNulo, "El campo es obligatorio")
        );
        assertEquals("El campo es obligatorio", excepcion.getMessage());
    }

    @Test
    void deberia_NoLanzarExcepcion_Cuando_ValidarObligatorioConObjetoNoString() {
        // Arrange — cubre validarObligatorio con objeto no-String (Long, Integer, etc.)
        Long cedula = 123456789L;

        // Act & Assert
        assertDoesNotThrow(() -> ValidadorArgumento.validarObligatorio(cedula, "La cédula es obligatoria"));
    }

    @Test
    void deberia_NoLanzarExcepcion_Cuando_ValidarRegexConPatronValido() {
        // Arrange
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        String emailValido = "correo@test.com";

        // Act & Assert
        assertDoesNotThrow(() -> ValidadorArgumento.validarRegex(emailValido, regexEmail, "Email invalido"));
    }

    @Test
    void deberia_LanzarExcepcionValorInvalido_Cuando_ValidarRegexConPatronInvalido() {
        // Arrange
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        String emailInvalido = "correo-invalido";

        // Act & Assert
        ExcepcionValorInvalido excepcion = assertThrows(
                ExcepcionValorInvalido.class,
                () -> ValidadorArgumento.validarRegex(emailInvalido, regexEmail, "Formato invalido")
        );
        assertEquals("Formato invalido", excepcion.getMessage());
    }
}