DELETE FROM doctores;

INSERT INTO doctores (numero_documento, nombre, apellido, tarjeta_profesional, especialidad, correo_institucional, habilitado)
VALUES ('DOC-001', 'Ana', 'Torres', 'TP-001', 'CARDIOLOGIA', 'ana.torres@hospital.com', true);

INSERT INTO doctores (numero_documento, nombre, apellido, tarjeta_profesional, especialidad, correo_institucional, habilitado)
VALUES ('DOC-002', 'Luis', 'Ramos', 'TP-002', 'PEDIATRIA', 'luis.ramos@hospital.com', true);

INSERT INTO doctores (numero_documento, nombre, apellido, tarjeta_profesional, especialidad, correo_institucional, habilitado)
VALUES ('DOC-003', 'Sofia', 'Mendez', 'TP-003', 'CARDIOLOGIA', 'sofia.mendez@hospital.com', false);
