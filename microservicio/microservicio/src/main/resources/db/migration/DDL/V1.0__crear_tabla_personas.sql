CREATE TABLE IF NOT EXISTS personas (
    cedula BIGINT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    fecha_nacimiento DATE NULL,
    PRIMARY KEY (cedula)
);
