package com.example.capitoleInterview.controller

import com.example.capitoleInterview.dto.ProductDTO
import com.example.capitoleInterview.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.data.web.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {
    @Operation(
        summary = "Get a list of products",
        description = "Returns a list of products filtered by category and sorted by the given criteria"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful retrieval of product list"),
            ApiResponse(responseCode = "400", description = "Invalid category or sort parameter")
        ]
    )
    @GetMapping
    fun listProducts(
        @RequestParam(defaultValue = "0") @Parameter(description = "Page number to retrieve (zero-based)") page: Int,
        @RequestParam(defaultValue = "5") @Parameter(description = "Number of products per page") size: Int,
        @RequestParam(required = false) @Parameter(description = "Category to filter products by") category: String?,
        @RequestParam(required = false) @Parameter(description = "Sort the products by the given field") sortBy: String?
    ): PagedModel<ProductDTO> {
        return PagedModel(productService.getProducts(page, size, category, sortBy))
    }
}