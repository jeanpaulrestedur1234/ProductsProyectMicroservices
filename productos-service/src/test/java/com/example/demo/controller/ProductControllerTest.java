package com.example.demo.controller;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    // --- CREAR ---
    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setName("Producto Ctrl");
        product.setPrice(50.0);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Producto Ctrl"));
    }

    @Test
    void testCreateInvalidProduct() throws Exception {
        Product product = new Product(); // sin name y price

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].title").value("Invalid product data"));
    }



    // --- ACTUALIZAR ---
    @Test
    void testUpdateNonExistingProduct() throws Exception {
        Product product = new Product();
        product.setName("Test");
        product.setPrice(10.0);

        mockMvc.perform(put("/products/888")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].title").value("Product not found"));
    }

    // --- BORRAR ---
    @Test
    void testDeleteNonExistingProduct() throws Exception {
        mockMvc.perform(delete("/products/777"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].title").value("Product not found"));
    }

    // --- LISTAR ---
    @Test
    void testListProductsEmpty() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
