# Product Module Class Diagram

This document contains the class diagram for the Product Module (`feature/product-module`), detailing the domain hierarchy, custom exception, persistence layer, and service layer.

```mermaid
classDiagram
    direction TB

    %% CAPA MODELO
    namespace model {
        class Product {
            <<abstract>>
            -String id
            -String title
            -double price
            -int stock
            +Product(id: String, title: String, price: double, stock: int)
            +getId() String
            +getTitle() String
            +getPrice() double
            +getStock() int
            +setId(id: String) void
            +setTitle(title: String) void
            +setPrice(price: double) void
            +setStock(stock: int) void
            +getFullDescription()* String
        }

        class VideoGame {
            -String platform
            -String genre
            -String ageRating
            +VideoGame(id: String, title: String, price: double, stock: int, platform: String, genre: String, ageRating: String)
            +getPlatform() String
            +getGenre() String
            +getAgeRating() String
            +setPlatform(platform: String) void
            +setGenre(genre: String) void
            +setAgeRating(ageRating: String) void
            +getFullDescription() String
        }

        class Console {
            -String brand
            -String model
            -String generation
            +Console(id: String, title: String, price: double, stock: int, brand: String, model: String, generation: String)
            +getBrand() String
            +getModel() String
            +getGeneration() String
            +setBrand(brand: String) void
            +setModel(model: String) void
            +setGeneration(generation: String) void
            +getFullDescription() String
        }
    }

    %% CAPA EXCEPCIONES
    namespace exception {
        class ProductException {
            +ProductException(message: String)
            +ProductException(message: String, cause: Throwable)
        }
    }

    %% CAPA PERSISTENCIA
    namespace persistence {
        class ProductPersistence {
            -String filePath
            +ProductPersistence(filePath: String)
            +saveAll(products: List~Product~) void
            +loadAll() List~Product~
            +save(product: Product) void
        }
    }

    %% CAPA SERVICIOS
    namespace service {
        class ProductService {
            -ProductPersistence productPersistence
            -List~Product~ products
            +ProductService(productPersistence: ProductPersistence)
            +registerProduct(product: Product) void
            +getAllProducts() List~Product~
            +findProductById(id: String) Product
            +updateStock(productId: String, quantity: int) boolean
        }
    }

    %% RELACIONES DE HERENCIA
    Product <|-- VideoGame
    Product <|-- Console

    %% RELACIONES DE PERSISTENCIA Y MODELO
    ProductPersistence ..> Product : manages
    ProductPersistence ..> ProductException : throws

    %% RELACIONES DE SERVICIO
    ProductService --> ProductPersistence : uses
    ProductService ..> Product : manipulates
    ProductService ..> ProductException : throws