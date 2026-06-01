# CRUD Personas - Spring Boot

Proyecto backend desarrollado con Java y Spring Boot para la gestión de personas mediante una API REST, aplicando arquitectura por capas y buenas prácticas de desarrollo backend.

## Objetivo

Desarrollar una aplicación backend capaz de administrar información de personas, implementando una estructura organizada, mantenible y escalable mediante Spring Boot.

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Maven
- Lombok
- Git & GitHub
- PostgreSQL


## Arquitectura del proyecto

El proyecto está organizado siguiendo una arquitectura por capas:

```text
controller  -> Manejo de endpoints REST
service     -> Lógica de negocio
repository  -> Acceso a datos
domain      -> Modelos del dominio
dto         -> Transferencia de datos

## Configuración

La aplicación se ejecuta en:

```text
http://localhost:8080
```

## Estructura base del proyecto

```text
src
 └── main
      ├── java
      │    └── org.ceiba.hu03
      │         ├── controller
      │         ├── service
      │         ├── repository
      │         ├── domain
      │         └── dto
      │
      └── resources
           └── application.properties
```