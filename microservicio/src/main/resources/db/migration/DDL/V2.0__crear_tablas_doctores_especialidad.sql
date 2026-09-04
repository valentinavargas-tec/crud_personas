CREATE TABLE IF NOT EXISTS especialidades (
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (nombre)
);

CREATE TABLE IF NOT EXISTS doctores (
    numero_documento VARCHAR(20) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    tarjeta_profesional VARCHAR(50) NOT NULL UNIQUE,
    especialidad VARCHAR(100) NOT NULL,
    correo_institucional VARCHAR(255) NOT NULL UNIQUE,
    habilitado BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (numero_documento),
    CONSTRAINT fk_especialidad FOREIGN KEY (especialidad) REFERENCES especialidades(nombre)
);

INSERT INTO especialidades (nombre) VALUES ('CARDIOLOGIA'), ('PEDIATRIA'), ('MEDICINA_GENERAL'), ('DERMATOLOGIA'), ('GINECOLOGIA');
