package com.example.demo.infrastructure.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

        // 404 Producto no encontrado
        @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(ProductNotFoundException ex) {
                ErrorResponse.ErrorItem item = new ErrorResponse.ErrorItem(
                                "404",
                                "Product not found",
                                ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse(List.of(item)));
        }

        // 400 Datos inválidos
        @ExceptionHandler(InvalidProductException.class)
        public ResponseEntity<Map<String, Object>> handleInvalidProduct(InvalidProductException ex) {
                Map<String, Object> error = Map.of(
                                "status", "400",
                                "title", "Invalid product data",
                                "detail", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("errors", List.of(error)));
        }

        // 500 Error interno
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleInternal(Exception ex, WebRequest request) {
                Map<String, Object> error = Map.of(
                                "status", "500",
                                "title", "Internal Server Error",
                                "detail", ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("errors", List.of(error)));
        }
}
