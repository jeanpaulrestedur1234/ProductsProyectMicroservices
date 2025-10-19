# Etapa 1: Construir la app Angular
FROM node:20-alpine AS build-step

RUN mkdir -p /app
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . /app
RUN npm run build --prod

# Etapa 2: Servir la app con Nginx
FROM nginx:1.27-alpine

COPY --from=build-step /app/dist/Frontend /usr/share/nginx/html
