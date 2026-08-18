package arquitectura.condiciones;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

public class CondicionPatronPaqueteDominio extends DescribedPredicate<JavaClass> {
    private final String patronPaqueteDeseado;

    public CondicionPatronPaqueteDominio(String patronPaqueteDeseado) {
        super("Valida nombres de paquetes con expresiones regulares");
        this.patronPaqueteDeseado = patronPaqueteDeseado;
    }

    @Override
    public boolean test(JavaClass javaClass) {
        String nombrePaquete = javaClass.getPackageName();
        return nombrePaquete.matches(patronPaqueteDeseado);
    }
}
