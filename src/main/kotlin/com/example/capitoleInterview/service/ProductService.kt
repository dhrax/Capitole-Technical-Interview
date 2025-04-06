package com.example.capitoleInterview.service

import com.example.capitoleInterview.dto.ProductDTO
import com.example.capitoleInterview.model.Category
import com.example.capitoleInterview.model.Field
import com.example.capitoleInterview.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ProductService {

    // Example list of products
    private val products = listOf(
        Product("SKU0001", "Wireless Mouse with ergonomic design", Category.ELECTRONICS, 19.99),
        Product("SKU0002", "4K Ultra HD Smart TV, 55 inches", Category.ELECTRONICS, 499.00),
        Product("SKU0003", "Stainless Steel Water Bottle, 1L", Category.HOME_KITCHEN, 29.50),
        Product("SKU0004", "Cotton T-Shirt, Unisex, Size M", Category.CLOTHING, 15.00),
        Product("SKU0005", "Noise-Cancelling Over-Ear Headphones", Category.ELECTRONICS, 120.00),
        Product("SKU0006", "USB-C to USB Adapter", Category.ELECTRONICS, 9.99),
        Product("SKU0007", "Leather Wallet with RFID Protection", Category.ACCESSORIES, 75.00),
        Product("SKU0008", "Yoga Mat with Non-Slip Surface", Category.SPORTS, 35.00),
        Product("SKU0009", "Smartwatch with Heart Rate Monitor", Category.ELECTRONICS, 220.00),
        Product("SKU0010", "Ceramic Coffee Mug, 350ml", Category.HOME_KITCHEN, 12.50),
        Product("SKU0011", "Bluetooth Portable Speaker", Category.ELECTRONICS, 60.00),
        Product("SKU0012", "Backpack with Laptop Compartment", Category.ACCESSORIES, 85.00),
        Product("SKU0013", "Stainless Steel Cutlery Set, 24 Pieces", Category.HOME_KITCHEN, 18.00),
        Product("SKU0014", "Electric Guitar Starter Pack", Category.MUSICAL_INSTRUMENTS, 250.00),
        Product("SKU0015", "Running Shoes, Men's Size 42", Category.FOOTWEAR, 42.00),
        Product("SKU0016", "Digital Bathroom Scale with Body Fat Analyzer", Category.HOME_APPLIANCES, 27.99),
        Product("SKU0017", "Set of 6 Organic Cotton Socks", Category.CLOTHING, 14.99),
        Product("SKU0018", "DSLR Camera with 18-55mm Lens", Category.ELECTRONICS, 300.00),
        Product("SKU0019", "Hardcover Notebook, A5, 200 Pages", Category.STATIONERY, 8.99),
        Product("SKU0020", "Microwave Oven, 20L Capacity", Category.HOME_APPLIANCES, 65.00),
        Product("SKU0021", "LED Desk Lamp with Adjustable Brightness", Category.HOME_KITCHEN, 23.50),
        Product("SKU0022", "Wireless Charger Pad for Smartphones", Category.ELECTRONICS, 19.00),
        Product("SKU0023", "Men's Quartz Analog Watch with Leather Strap", Category.ACCESSORIES, 55.00),
        Product("SKU0024", "Wooden Chess Set with Folding Board", Category.TOYS_GAMES, 30.00),
        Product("SKU0025", "Home Security Camera with Night Vision", Category.ELECTRONICS, 99.00),
        Product("SKU0026", "Aromatherapy Essential Oil Diffuser", Category.HOME_KITCHEN, 16.50),
        Product("SKU0027", "Professional Blender with 2L Jar", Category.HOME_APPLIANCES, 40.00),
        Product("SKU0028", "Kids' Educational Tablet Toy", Category.TOYS_GAMES, 22.00),
        Product("SKU0029", "Mechanical Gaming Keyboard with RGB Lighting", Category.ELECTRONICS, 110.00),
        Product("SKU0030", "Pack of 10 Ballpoint Pens, Blue Ink", Category.STATIONERY, 7.50),
    )

    /**
     * Retrieves a paginated list of products, optionally filtered by category and sorted by a specified field.
     *
     * This method filters products by category (if provided), sorts the products based on the specified field
     * (if given), and applies the best discount to each product. It then returns a paginated response containing
     * the filtered, sorted, and discounted products.
     *
     * The method supports pagination through `page` and `size` parameters and supports sorting by fields such as
     * SKU, price, description, or category.
     *
     * @param page The page number to retrieve, starting from 0.
     * @param size The number of products to return per page.
     * @param category An optional category to filter products by. If null or empty, no filtering is applied.
     * @param sortBy An optional field to sort the products by. Supported values: SKU, PRICE, DESCRIPTION, CATEGORY.
     * @return A `Page` of `ProductDTO` containing the filtered, sorted, and discounted products for the current page.
     *
     * @see ProductDTO
     * @see Field
     * @see applyBestDiscount
     */
    fun getProducts(page: Int, size: Int, category: String?, sortBy: String?): Page<ProductDTO> {
        // Filter products by category if provided
        val filtered = if (!category.isNullOrBlank()) {
            products.filter { it.category.label.equals(category, ignoreCase = true) }
        }else{
            products
        }

        // Sort the filtered products if a sorting field is provided
        val sorted = when (sortBy?.lowercase()) {
            Field.SKU.label -> filtered.sortedBy { it.sku }
            Field.PRICE.label-> filtered.sortedBy { it.price }
            Field.DESCRIPTION.label -> filtered.sortedBy { it.description }
            Field.CATEGORY.label -> filtered.sortedBy { it.category }
            else -> filtered
        }

        val pageRequest = PageRequest.of(page, size)

        // Get the sublist of products for the current page
        val start = (page * size).coerceAtMost(sorted.size)
        val end = ((page + 1) * size).coerceAtMost(sorted.size)

        // Apply discount to each product
        val productsWithDiscount = sorted.subList(start, end).map { applyBestDiscount(it) }

        return PageImpl(productsWithDiscount, pageRequest, sorted.size.toLong())
    }

    /**
     * Calculates the best discount applicable for a given product based on its category and SKU.
     *
     * The highest discount is applied to the product price, and the final price after discount is calculated.
     *
     * @param product The product for which the discount is to be applied. It contains the product's category, SKU, and price.
     * @return A `ProductDTO` object that contains:
     * - The original product information.
     * - The applied discount (as a percentage).
     * - The final price after applying the discount.
     *
     * @see ProductDTO
     * @see Category
     */
    fun applyBestDiscount(product: Product): ProductDTO {
        val discounts = mutableListOf<Pair<String, Double>>()

        if (product.category.label.equals(Category.ELECTRONICS.label, ignoreCase = true)) discounts += Category.ELECTRONICS.label to 0.15
        if (product.category.label.equals(Category.HOME_KITCHEN.label, ignoreCase = true)) discounts += Category.HOME_KITCHEN.label to 0.25
        if (product.sku.endsWith("5")) discounts += Category.SPECIAL_SKU.label to 0.30

        val bestDiscount = discounts.maxByOrNull { it.second }
        val discountAmount = bestDiscount?.second?.let { product.price * it } ?: 0.0
        val finalPrice = product.price - discountAmount

        return ProductDTO(product, bestDiscount?.second ?: 0.0, finalPrice)
    }
}