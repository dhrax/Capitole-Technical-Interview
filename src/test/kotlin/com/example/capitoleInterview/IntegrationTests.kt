package com.example.capitoleInterview

import com.example.capitoleInterview.controller.ProductController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
@SpringBootTest
class IntegrationTests {


    private lateinit var mockMvc: MockMvc

    @InjectMocks
    lateinit var productController: ProductController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build()
    }

    @Test
    fun `test get products with pagination`() {
        // Perform a GET request and verify the response
        mockMvc.get("/products?page=0&size=10")
            .andExpectAll {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.content.length()").value(2) //Verifying number of products in the response
                jsonPath("$.content[0].sku").value("SKU0009")
                jsonPath("$.content[1].sku").value("SKU0002")
            }
    }

    @Test
    fun `test get products with category filter`() {

        // Perform a GET request with category filter
        mockMvc.get("/products?page=0&size=10&category=Electronics")
            .andExpectAll {
                status().isBadRequest
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.content.length()").value(1)
                jsonPath("$.content[0].category").value("Electronics") //Verifying correct category
            }
    }

    @Test
    fun `test get products with sorting`() {

        // Perform a GET request with sorting
        mockMvc.get("/products?page=0&size=10&sortBy=price")
            .andExpectAll{
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.content[0].sku").value("SKU0001") //Verifying products are correctly sorted
                jsonPath("$.content[1].sku").value("SKU0002")
            }
    }
}