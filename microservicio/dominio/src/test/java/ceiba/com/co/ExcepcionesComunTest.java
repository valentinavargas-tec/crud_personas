package ceiba.com.co;

import ceiba.com.co.excepcion.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcepcionesComunTest {

    @Test
    void deberia_InstanciarExcepcionesComunes_Cuando_MensajeEsProporcionado() {

        // Arrange & Act & Assert
        ExcepcionLongitudValor exLongitud = new ExcepcionLongitudValor("Error de longitud");
        assertEquals("Error de longitud", exLongitud.getMessage());

        ExcepcionValorInvalido exInvalido = new ExcepcionValorInvalido("Valor invalido");
        assertEquals("Valor invalido", exInvalido.getMessage());

        ExcepcionValorObligatorio exObligatorio = new ExcepcionValorObligatorio("Valor obligatorio");
        assertEquals("Valor obligatorio", exObligatorio.getMessage());
    }

    @Test
    void deberia_CrearExcepcionDuplicidad_Cuando_MensajeEsProporcionado(){
        //Arrange & Act
        ExcepcionDuplicidad excepcion = new ExcepcionDuplicidad("La cedula ya existe");

        //Assert
        assertEquals("La cedula ya existe", excepcion.getMessage());
    }

    @Test
    void deberia_CrearExcepcionSinDatos_Cuando_MensajeEsProporcionado(){
        //Arrange & Act
        ExcepcionSinDatos excepcion = new ExcepcionSinDatos("No se encuentra datos");

        //Assert
        assertEquals("No se encuentra datos", excepcion.getMessage());
    }
}