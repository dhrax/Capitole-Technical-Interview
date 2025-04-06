# Capitole-Technical-Interview

# Product Catalog API

## Architectural Decisions

1. **Business Logic in `ProductService`**:  
   Encapsulates sorting, filtering, and discount logic to ensure separation of concerns and maintainability.

2. **DTO Pattern**:  
   Used to decouple internal models from API responses, allowing flexible transformations and versioning.

3. **Pagination with Spring Data**:  
   Enables efficient retrieval of large datasets with pagination using `PageRequest` and `PageImpl`.

4. **Sorting and Filtering**:  
   Handles sorting and filtering of products based on user-defined parameters (like category or price).

5. **Testing Strategy**:  
   Unit and integration tests ensure the correctness of the business logic and API endpoints.

6. **Swagger API Documentation**:  
   Provides interactive API documentation and easy onboarding for developers.

7. **Error Handling**:  
   Uses global exception handling to return appropriate HTTP status codes and messages for better error reporting.
