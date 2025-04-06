# Product Catalog API - Startup Guide

## Prerequisites

Before starting the application, ensure the following:

1. **JDK 17+**: The application is built using Java 17 or higher. Make sure JDK 11 or a higher version is installed on your machine.
    - You can check the installed Java version by running:
      ```bash
      java -version
      ```

---

## Cloning the Repository

1. Clone the repository to your local machine using:
   ```bash
   git clone https://github.com/your-repo-url.git
   cd your-repo-name
   
---

## API Access and Documentation

1. **API Access**
    - **Base URL**:
      ```
      http://localhost:8080/api/products
      ```
    - **Example API Requests**:
        - **Get paginated Products**:
          ```bash
          GET /products
          ```
        - **Get Products with custom pagination**:
          ```bash
          GET /products?page=0&size=10
          ```
        - **Filter products by category**:
          ```bash
          GET /products?category={category}
          ```
        - **Filter products by category**:
          ```bash
          GET /products?sortBy={sort_element}
          ```
        - You can also combine these requests to create more complex queries


2. **API Documentation**
    - The application uses **Swagger** for interactive API documentation. You can access it at:
      ```
      http://localhost:8080/swagger-ui.html
      ```
    - This UI will allow you to explore and test the API directly in your browser.