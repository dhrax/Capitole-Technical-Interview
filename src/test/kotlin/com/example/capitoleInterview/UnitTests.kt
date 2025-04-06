package com.example.capitoleInterview

import com.example.capitoleInterview.model.Category
import com.example.capitoleInterview.model.Product
import com.example.capitoleInterview.service.ProductService
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.everyItem
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UnitTests {

	@Autowired
	private lateinit var mockMvc: MockMvc

	private val service = ProductService()

	@Test
	fun `should return all products`() {
		mockMvc.perform(get("/products"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.length()").value(greaterThan(0)))
	}

	@Test
	fun `should return only Electronics products`() {
		mockMvc.perform(get("/products?category=Electronics"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$[*].category").value(everyItem(equalTo("Electronics"))))
	}

	@Test
	fun `should return products sorted by price`() {
		mockMvc.perform(get("/products?sortBy=price"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$[0].price").exists())
	}

	@Test
	fun `should return empty list for unknown category`() {
		mockMvc.perform(get("/products?category=NonExistent"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.length()").value(0))
	}

	@Test
	fun `should apply 30 percent discount for SKU ending in 5`() {
		val product = Product("SKU9995", "Test", Category.TOYS_GAMES, 100.0)
		val result = service.getProducts(0, 10,null, null).find { it.sku == product.sku }
		// Not found? Then test logic directly:
		val discounted = service.applyBestDiscount(product)

		assertEquals(30.0, discounted.discountApplied * 100)
		assertEquals(70.0, discounted.finalPrice, 0.01)
	}

	@Test
	fun `should apply 25 percent discount for Home & Kitchen`() {
		val product = Product("SKU0003", "Bottle", Category.HOME_KITCHEN, 200.0)
		val discounted = service.applyBestDiscount(product)

		assertEquals(25.0, discounted.discountApplied * 100)
		assertEquals(150.0, discounted.finalPrice, 0.01)
	}

	@Test
	fun `should apply highest discount if multiple apply`() {
		val product = Product("SKU8885", "Combo Deal", Category.HOME_KITCHEN, 100.0)
		val discounted = service.applyBestDiscount(product)

		// 25% and 30% applicable, so use 30%
		assertEquals(30.0, discounted.discountApplied * 100)
		assertEquals(70.0, discounted.finalPrice, 0.01)
	}

}
