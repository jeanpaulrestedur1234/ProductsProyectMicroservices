
# Proyecto Microservicios: Productos & Inventario (Arquitectura Completa)

Este proyecto implementa una solución de **microservicios** para la gestión de productos e inventario, complementada con un **Frontend** y un **API Gateway (NGINX)** para enrutamiento, seguridad, y desacoplamiento. Los servicios backend están desarrollados en **Spring Boot** y siguen principios de **Arquitectura Limpia**.

## 💻 Arquitectura del Sistema

El sistema consta de cuatro componentes principales:

1.  **Productos-service**: Gestiona el catálogo de productos (**CRUD**).
2.  **Inventario-service**: Gestiona las cantidades de inventario. Consulta productos de `productos-service`.
3.  **API Gateway (NGINX)**: Punto de entrada unificado para todas las solicitudes externas, maneja el enrutamiento y la **seguridad perimetral**.
4.  **Frontend**: Interfaz de usuario que consume los servicios a través del API Gateway.

-----

## 💎 Principios de Diseño Backend

Ambos microservicios (`productos-service` e `inventario-service`) han sido diseñados bajo principios de **Arquitectura Limpia (Clean Architecture)**, promoviendo la **Separación de Intereses** y la **Fácil Mantenibilidad**.

### 1\. Arquitectura Limpia

La estructura de carpetas mostrada (`application`, `domain`, `infrastructure`) refleja el patrón de arquitectura limpia, asegurando que:

  * **Dominio (`domain`):** Contiene la lógica de negocio pura (modelos y contratos de repositorio). Es el **núcleo** y es independiente de la tecnología.
  * **Aplicación (`application`):** Contiene las implementaciones de los casos de uso o servicios (`ProductService`). Coordina el flujo de datos.
  * **Infraestructura (`infrastructure`):** Contiene adaptadores que conectan el dominio con el mundo exterior (controladores REST, implementaciones de base de datos, seguridad, etc.).

### 2\. Patrón Repository para Persistencia Flexible

  * **Uso del Patrón Repository:** Se implementa el patrón de diseño **Repository** (ejemplo: `ProductRepository.java` en `domain/repository`).
  * **Facilidad de Cambio de Base de Datos:** Este patrón desacopla la lógica de negocio (el `Service`) de la tecnología de persistencia subyacente. Al usar interfaces de repositorio, migrar de **H2 en memoria** a una base de datos relacional robusta (como PostgreSQL o MySQL) o incluso a NoSQL, requiere solo cambiar la implementación en la capa de **Infraestructura**, sin modificar el código de la lógica de negocio (`Service`).

### 3\. Base de Datos H2 en Memoria

  * **Persistencia Temporal:** Ambos servicios utilizan la base de datos **H2 en memoria** para la persistencia de datos.
  * **Objetivo:** Esto permite que el entorno de desarrollo y pruebas sea ligero y rápido de levantar, con la posibilidad de cargar datos iniciales (**data.sql**) automáticamente en cada arranque.

-----

## ⚙️ Componentes y Tecnologías

| Componente | Tecnología | Persistencia | Patrón de Diseño | Comunicación Inter-Servicio |
| :--- | :--- | :--- | :--- | :--- |
| `productos-service` | Spring Boot | **H2 en memoria** | **Repository Pattern** + Clean Arch. | **API Key** (para acceso interno) |
| `inventario-service` | Spring Boot | **H2 en memoria** | **Repository Pattern** + Clean Arch. | HTTP (a través de Docker DNS) |
| `api-gateway` | NGINX | N/A | N/A | HTTP |
| `frontend` | Dockerized Frontend | N/A | N/A | HTTP (vía API Gateway) |

-----

## 🚀 Gestión del Proyecto 

Para simplificar las operaciones comunes como levantar, detener o reiniciar los servicios, se ha incluido un `Makefile` en la raíz del proyecto.

| Comando | Descripción |
| :--- | :--- |
| `make up` | **Construye las imágenes** y levanta todos los microservicios y el frontend en modo *detached* (`-d`). |
| `make down` | Detiene y elimina todos los contenedores y redes creadas por `docker-compose`. |
| `make rebuild` | Ejecuta `down` seguido de `up`. Es útil para aplicar cambios en el código o en los `Dockerfile`. |
| `make logs` | Muestra los *logs* combinados de todos los servicios en tiempo real. |
| `make status` | Muestra el estado actual de los contenedores (`up`, `exited`, etc.). |
| `make clean` | Detiene y elimina todo, **incluyendo los volúmenes**, forzando la pérdida de los datos de H2. **Usar con precaución.** |
| `make help` | Muestra todos los comandos disponibles. |

**Ejemplo de uso:**

```bash
# Para levantar la aplicación por primera vez
make up

# Para reiniciar tras un cambio de código
make rebuild
```
### Accesos

| Componente | URL de Acceso |
| :--- | :--- |
| **Frontend** | `http://localhost:4200` |
| **API Gateway** | `http://localhost:80` |
| **Swagger UI** (Ejemplo) | `http://localhost:8081/swagger-ui.html` |

-----

## 🗺️ Endpoints y Responsabilidad Única

Cada servicio mantiene el principio de **Responsabilidad Única**.

### 1\. `productos-service` (Catálogo)

  * **Responsabilidad:** Gestiona las propiedades intrínsecas del producto (nombre, descripción, precio, etc.).
  * **Endpoints:** CRUD completo (`/products`, `/products/{id}`).
  * **Ruta Privada:** Incluye una ruta interna (`/private/products/{id}`) protegida por **API Key**, usada exclusivamente por `inventario-service` para obtener detalles del producto.

### 2\. `inventario-service` (Stock)

  * **Responsabilidad:** Gestiona la cantidad disponible (stock) de un producto.
  * **Endpoints:** Gestión de inventario (`/inventories`, `/inventories/{productId}`).
  * **Proceso:** Cuando se solicita el listado (`GET /inventories`), este servicio consulta su propia base de datos de stock y luego llama al `productos-service` (usando la API Key) para obtener la información de nombre/descripción y así **enriquecer** la respuesta final.