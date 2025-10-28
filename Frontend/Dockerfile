# --- Etapa 1: Construcción (Build) ---
# Usamos una imagen completa de Node.js para compilar la app de Angular.
FROM node:20 as build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar archivos de configuración e instalar dependencias
COPY package.json package-lock.json ./
RUN npm install

# Copiar el resto del código fuente de la aplicación
COPY . .

RUN npm run build --configuration=production

RUN echo "=== Contenido del directorio dist después del build ===" && \
    ls -R /app/dist

FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=build /app/dist/Frontend/browser /usr/share/nginx/html
RUN mv /usr/share/nginx/html/index.csr.html /usr/share/nginx/html/index.html



EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
