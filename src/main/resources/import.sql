INSERT INTO tipo_documento(nombre_tipo_documento) VALUES ('Cédula de Ciudadanía');
INSERT INTO tipo_documento(nombre_tipo_documento) VALUES ('NIT');

INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de inicio de todo proyecto', null, 'Creado');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado siguiente al inicio del proyecto', 1, 'A punto de iniciar');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado a punto de inicio del proyecto, siguiente a creado', 2, 'Iniciado');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de prueba, siguiente al inicio del proyecto', 3, 'Prueba');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de un proyecto cuando es pospuesto', 1, 'Pospuesto');

INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876765', 'Asisge', 'Asistencia Gerencial Estrategica SAS', 2);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Ruiz', 'Bedoya', '123456', 'marioarb.97@gmail.com', true, '1017251545', 'Mario Andres', '3015465076', 1);

INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876761', 'Dian', 'Direccion de impuestos SAS', 2);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Ruiz', 'Bedoya', '123456', 'carito.93@gmail.com', true, '1017214122', 'Carito', '3046307654', 1);

INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876762', 'S4DS', 'solutions for dealers SAS', 2);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Bedoya', 'Jaramillo', '123456', 'gloriae.61@gmail.com', true, '43032118', 'Gloria', '3007855211', 1);

INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876763', 'Joyeria inter', 'Joyeria intercontinental dali SAS', 2);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Ruiz', 'Giraldo', '123456', 'jotica.65@gmail.com', true, '71665023', 'Juan Mario', '3006521212', 1);

INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (1, 1);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (1, 2);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (1, 3);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (1, 4);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (2, 1);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (3, 2);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (4, 3);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (4, 4);