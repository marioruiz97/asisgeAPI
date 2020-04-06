# asisgeAPI
API REST for Asisge Services

Proyecto de PPI para Asisge S.A.S.
El sistema del core consulting se encarga del seguimiento y gestión de proyectos de asesoría empresarial que realiza Asisge.

### Requisitos de usuario y módulos.
- RU-001	Administrar Usuarios
- RU-002	Administrar Clientes
- RU-003	Administrar Tipos de usuario (Perfiles)
- RU-004	Gestionar Proyectos
- RU-005	Gestionar Plan de trabajos, actividades y subactividades
- RU-006	Gestión de Módulos de Apoyo
- RU-007	Gestión de Informes

### Arquitectura y tecnologías utilizadas

El sistema es un sistema web realizado con **Spring boot, Spring Data (JPA y Hibernate ORM)** usando una arquitectura de
**Microservicios y MVC** (Modelo - Vista - Controlador).
El sistema funciona como una API REST (o Web service) bajo modelo cliente/servidor, el backend es encargado de recibir 
peticiones Http y devolver respuestas usando el mismo protocolo.

El sistema usa un sistema de **autenticación JWT (Json Web Token) y OAuth2** (framework de seguridad integrado con spring).
\Programa de ejemplo [JWT-Example-Repository](https://github.com/szerhusenBC/jwt-spring-security-demo)

### Estandares para la codificación

- Nombres de clases lo suficientemente claros
- Nombres de atributos en camelCase y equivalentes a su nombre en modelo de base de datos 

Ej.
> Clase   --      Base de datos:

> nombreUsuario -- nombre_usuario
      
- Los controladores siempre devuelven ResponseEntity para hacer estándar todos. Se puede usar un mapa response para 
enviar los objetos, mensajes o errores necesarios a traves de la red.
- Los modelos, servicios y controladores deben hacer uso de @Valid @Validated (_anotaciones de la api de Javax.Validation_)
- Documentar las clases en sus encabezados para no llenar las clases de comentarios. Documentar sobre atributos y métodos
- Documentar las interfaces ( las funciones), ***No*** las _implementaciones_
- Si se desea documentar funciones, se indican que parametros reciben, que valores retornan y que función cumple.
- Para el repositorio, no trabajar en la rama Master (rama default), crear una rama, hacer fetch de master (o pull), 
hacer checkout y verificar que se encuentre en la rama creada. 
  ***[Introducción a git y github](https://product.hubspot.com/blog/git-and-github-tutorial-for-beginners)***
