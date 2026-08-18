package org.ceiba.hu03.constants;

public final class AppConstants {
        private AppConstants(){
        }

        // Validaciones de CÉDULA
        public static final String CEDULA_OBLIGATORIA =
                "La cédula es obligatoria";

        public static final String CEDULA_INVALIDA =
                "La cédula debe ser un número válido y positivo";

        public static final String CEDULA_YA_REGISTRADA =
                "La cédula ya está registrada en el sistema";

        public static final String CEDULA_INMUTABLE =
                "La cédula no puede ser modificada. Es una propiedad inmutable del registro";

        // Validaciones de PERSONA
        public static final String PERSONA_NO_ENCONTRADA =
                "Persona no encontrada";

        public static final String CORREO_EN_USO =
                "El correo ya está en uso";

        public static final String PERSONA_ACTUALIZADA =
                "Persona actualizada correctamente";

        public static final String NOMBRE_OBLIGATORIO =
                "El nombre es obligatorio";

        public static final String APELLIDO_OBLIGATORIO =
                "El apellido es obligatorio";

        public static final String EMAIL_OBLIGATORIO =
                "El email es obligatorio";

        public static final String EMAIL_INVALIDO =
                "El formato de email no es válido";

        public static final String FECHA_INVALIDA =
                "La fecha de nacimiento no puede ser futura";

        public static final String ID_TIPO_INVALIDO =
                "El ID debe ser un número tipo Long válido";

        public static final String PERSONA_CREADA =
                "Persona creada correctamente";

        public static final String PERSONA_ELIMINADA =
                "Persona eliminada correctamente";
}
