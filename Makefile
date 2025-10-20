# =========================================================================
# Makefile para Microservicios: Productos & Inventario
# Uso: make [comando]
# =========================================================================

# Variables
PROJECT_NAME := microservices-project
DOCKER_COMPOSE_FILE := docker-compose.yml
FRONTEND_PORT := 4200
API_GATEWAY_PORT := 80
PRODUCTS_PORT := 8081
INVENTORY_PORT := 8082

# Target principal para levantar y construir la aplicación
.PHONY: up
up:
	@echo "🏗️  Construyendo y levantando todos los servicios..."
	docker-compose -f $(DOCKER_COMPOSE_FILE) up --build -d
	@echo "✅ Aplicación lista y corriendo en modo detached."
	@echo "🔗 Acceso Frontend: http://localhost:$(FRONTEND_PORT)"
	@echo "🔗 Acceso API Gateway: http://localhost:$(API_GATEWAY_PORT)"

# Target para detener y eliminar los contenedores
.PHONY: down
down:
	@echo "🛑 Deteniendo y eliminando contenedores..."
	docker-compose -f $(DOCKER_COMPOSE_FILE) down
	@echo "✅ Contenedores detenidos y eliminados."

# Target para reconstruir y reiniciar todo (útil tras cambios en código/Dockerfile)
.PHONY: rebuild
rebuild: down up
	@echo "♻️  Reconstrucción y reinicio completados."

# Target para ver el estado de los contenedores
.PHONY: status
status:
	@echo "📊 Estado de los Contenedores:"
	docker-compose -f $(DOCKER_COMPOSE_FILE) ps

# Target para ver los logs de todos los servicios
.PHONY: logs
logs:
	@echo "📄 Viendo logs de todos los servicios (Ctrl+C para salir)..."
	docker-compose -f $(DOCKER_COMPOSE_FILE) logs -f

# Target para limpiar volúmenes (usar con precaución, borrará datos de H2)
.PHONY: clean
clean: down
	@echo "🧹 Limpiando volúmenes (puede eliminar datos persistentes de H2)..."
	docker-compose -f $(DOCKER_COMPOSE_FILE) rm -f -v
	@echo "✅ Limpieza completada."

# Ayuda
.PHONY: help
help:
	@echo "Uso: make <comando>"
	@echo ""
	@echo "Comandos disponibles:"
	@echo "  up       : Construye las imágenes y levanta los contenedores en modo detached."
	@echo "  down     : Detiene y elimina los contenedores, redes y volúmenes anónimos."
	@echo "  rebuild  : Ejecuta 'down' seguido de 'up' (reconstruye todo)."
	@echo "  status   : Muestra el estado actual de los contenedores."
	@echo "  logs     : Muestra los logs en tiempo real de todos los servicios."
	@echo "  clean    : Detiene y elimina todo, incluyendo volúmenes (¡borra datos!)."
	@echo "  help     : Muestra este mensaje de ayuda."