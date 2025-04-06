package com.example.capitoleInterview.dto

import com.example.capitoleInterview.model.Category
import com.example.capitoleInterview.model.Product

data class ProductDTO(
    val sku: String,
    val description: String,
    val category: Category,
    val originalPrice: Double,
    val discountApplied: Double,
    val finalPrice: Double
) {
    constructor(product: Product, discountRate: Double, finalPrice: Double) : this(
        sku = product.sku,
        description = product.description,
        category = product.category,
        originalPrice = product.price,
        discountApplied = discountRate,
        finalPrice = finalPrice
    )
}