package com.example.capitoleInterview.controller

import com.example.capitoleInterview.dto.ProductDTO
import com.example.capitoleInterview.service.ProductService
import org.hibernate.annotations.Parameter
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
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false)  sortBy: String?
    ): Page<ProductDTO> {
        return productService.getProducts(page, size, category, sortBy)
    }
}