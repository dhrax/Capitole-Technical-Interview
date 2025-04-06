package com.example.capitoleInterview.model

data class Product(
    val sku: String,
    val description: String,
    val category: Category,
    val price: Double
)