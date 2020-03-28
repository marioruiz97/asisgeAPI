INSERT INTO tipo_documento(nombre_tipo_documento) VALUES ('Cédula de Ciudadanía');
INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876765', 'Asisge', 'Asistencia Gerencial Estrategica SAS', 1);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Ruiz', 'Bedoya', '123456', 'marioarb.97@gmail.com', true, '1017251545', 'Mario Andres', '3015465076', 1);

INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de inicio de todo proyecto', null, 'Creado');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado siguiente al inicio del proyecto', 1, 'A punto de iniciar');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado a punto de inicio del proyecto, siguiente a creado', 2, 'Iniciado');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de prueba, siguiente al inicio del proyecto', 3, 'Prueba');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de un proyecto cuando es pospuesto', 1, 'Pospuesto');
