package arquitectura.condiciones;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public class CondicionCamposFinalesYPasadosComoParametroAlConstructor extends ArchCondition<JavaClass> {
    private final String prefijo;
    public CondicionCamposFinalesYPasadosComoParametroAlConstructor(String prefijo) {
        super("Valida nombres de paquetes con expresiones regulares");
        this.prefijo = prefijo;
    }

    @Override
    public void check(JavaClass javaClass, ConditionEvents events) {
        javaClass.getFields().forEach(javaField -> {
            if (!javaField.getRawType().getSimpleName().startsWith(prefijo)) {
                return;
            }

            boolean esFinal = javaField.getModifiers().contains(JavaModifier.FINAL);
            boolean existePatametroEnConstructor = javaField.getOwner().getConstructors().stream()
                    .anyMatch(constructor -> constructor.getRawParameterTypes().contains(javaField.getRawType()));
            if (!existePatametroEnConstructor || !esFinal) {
                String mensaje = String.format("%s: Debido a que el atributo %s contiene una clase de tipo %s debería ser ingresado como parametro en el constructor", javaClass.getSimpleName(), javaField.getName(), prefijo);
                events.add(new SimpleConditionEvent(javaField, false, mensaje));
            }
        });
    }
}
