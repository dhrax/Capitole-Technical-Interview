package com.example.capitoleInterview.controller

import com.example.capitoleInterview.dto.ProductDTO
import com.example.capitoleInterview.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun listProducts(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) sortBy: String?
    ): List<ProductDTO> {
        return productService.getProducts(category, sortBy)
    }
}