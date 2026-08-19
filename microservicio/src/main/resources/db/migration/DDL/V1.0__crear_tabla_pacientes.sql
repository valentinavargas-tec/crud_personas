CREATE TABLE IF NOT EXISTS pacientes (
    numero_documento BIGINT NOT NULL,
    tipo_documento VARCHAR(10) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE NULL,
    telefono VARCHAR(20) NULL,
    correo_electronico VARCHAR(255) NOT NULL UNIQUE,
    eps VARCHAR(255) NULL,
    genero VARCHAR(20) NOT NULL,
    PRIMARY KEY (numero_documento)
);
