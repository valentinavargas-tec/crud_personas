package arquitectura;

import arquitectura.condiciones.CondicionCamposFinalesYPasadosComoParametroAlConstructor;
import arquitectura.condiciones.CondicionPatronPaqueteDominio;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ServicioTest {

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
            .importPackages("ceiba.com.co");

    @Test
    @DisplayName("Se valida que las clases que existan en el paquete servicio, contengan el prefijo Servicio")
    public void testNombresDeClasesPaqueteServicio() {
        String patronDeseado = "ceiba.com.co.*.servicio.*";

        classes().that(new CondicionPatronPaqueteDominio(patronDeseado))
                .should().haveSimpleNameStartingWith("Servicio")
                .check(classes);
    }

    @Test
    @DisplayName("Se valida que los campos de tipo Repositorio sean final y existan dentro del constructor de la clase")
    public void testRepositoriosDeberianSerFinalesYPasadosPorConstructor() {
        String patronDeseado = "ceiba.com.co.*.servicio.*";

        classes().that(new CondicionPatronPaqueteDominio(patronDeseado))
                .should(new CondicionCamposFinalesYPasadosComoParametroAlConstructor("Repositorio"))
                .check(classes);
    }

    @Test
    @DisplayName("Se valida que las clases no dependa del controlador")
    public void testSinDependenciasExternas() {
        String patronDeseado = "ceiba.com.co.*.servicio.*";

        noClasses().that(new CondicionPatronPaqueteDominio(patronDeseado))
                .should().dependOnClassesThat().resideInAnyPackage("..controlador..").check(classes);
    }

}
