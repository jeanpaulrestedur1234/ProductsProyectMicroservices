# Proyecto Microservicios: Productos & Inventario

Este proyecto consta de dos microservicios desarrollados en **Spring Boot**:

1. **Productos-service**: Gestiona productos (CRUD).  
2. **Inventario-service**: Gestiona inventario, consulta productos desde productos-service y actualiza cantidades.

Ambos servicios usan **H2 en memoria** para persistencia y tienen datos iniciales de prueba.

---

## Requisitos

- Docker y Docker Compose instalados
- Maven (opcional, solo para construir localmente)
- Puertos libres: 8081 para productos-service, 8082 para inventario-service

---

## Estructura del proyecto

```

.
├── productos-service
│   ├── src
│   ├── pom.xml
│   └── Dockerfile
├── inventario-service
│   ├── src
│   ├── pom.xml
│   └── Dockerfile
└── docker-compose.yml

````

---

## Levantar los microservicios

Desde la raíz del proyecto:

```bash
docker-compose up --build
````

* `productos-service` → [http://localhost:8081/products](http://localhost:8081/products)
* `inventario-service` → [http://localhost:8082/inventories](http://localhost:8082/inventories)

---

## Endpoints principales

### Productos-service

| Método | Endpoint       | Descripción                |
| ------ | -------------- | -------------------------- |
| GET    | /products      | Listar todos los productos |
| GET    | /products/{id} | Obtener producto por ID    |
| POST   | /products      | Crear un producto          |
| PUT    | /products/{id} | Actualizar un producto     |
| DELETE | /products/{id} | Eliminar un producto       |

### Inventario-service

| Método | Endpoint                 | Descripción                                  |
| ------ | ------------------------ | -------------------------------------------- |
| GET    | /inventories             | Lista todos los productos con sus cantidades |
| GET    | /inventories/{productId} | Obtener inventario por productId             |
| POST   | /inventories             | Crear inventario para un producto            |
| PUT    | /inventories/{productId} | Actualizar cantidad de inventario            |

---

## Manejo de errores

* **404 Not Found**: Producto o inventario no existe.
* **500 Internal Server Error**: Error interno inesperado.
* Logs estructurados con información de la operación y timestamp.

---

## Datos iniciales

* **Productos-service**:

  * Producto A (ID 1)
  * Producto B (ID 2)
  * Producto C (ID 3)

* **Inventario-service**:

  * Producto 1 → 50 unidades
  * Producto 2 → 30 unidades
  * Producto 3 → 20 unidades

---

## Notas

* H2 Console disponible si se habilita en `application.properties` de cada servicio (`spring.h2.console.enabled=true`).
* Para pruebas locales sin Docker, puedes ejecutar cada microservicio con `mvn spring-boot:run`.

---

