package arquitectura;

import arquitectura.condiciones.CondicionPatronPaqueteDominio;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class PuertoTest {

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
            .importPackages("ceiba.com.co");


    @Test
    @DisplayName("Valida que en el puerto solo existan interfaces")
    public void testElPaqueteContieneSoloInterfaces() {
        String patronDeseado = "ceiba.com.co.*.puerto.*";

        classes().that(new CondicionPatronPaqueteDominio(patronDeseado))
                .should().beInterfaces().check(classes);
    }

    @Test
    @DisplayName("Se valida que las clases que existan en el paquete dao, contengan el prefijo Dao")
    public void testNombresDeClasesPaqueteDao() {
        String patronDeseado = "ceiba.com.co.*.puerto.dao.*";

        classes().that(new CondicionPatronPaqueteDominio(patronDeseado))
                .should().haveSimpleNameStartingWith("Dao")
                .check(classes);
    }

    @Test
    @DisplayName("Se valida que las clases que existan en el paquete repositorio, contengan el prefijo Repositorio")
    public void testNombresDeClasesPaqueteRepositorio() {
        String patronDeseado = "ceiba.com.co.*.puerto.repositorio.*";

        classes().that(new CondicionPatronPaqueteDominio(patronDeseado))
                .should().haveSimpleNameStartingWith("Repositorio")
                .check(classes);
    }
    @Test
    @DisplayName("Se valida que las clases no dependa del servicio")
    public void testSinDependenciasExternas() {
        String patronDeseado = "ceiba.com.co.*.puerto.repositorio.*";

        noClasses().that(new CondicionPatronPaqueteDominio(patronDeseado))
                .should().dependOnClassesThat().resideInAnyPackage("..servicio..").check(classes);
    }
}
