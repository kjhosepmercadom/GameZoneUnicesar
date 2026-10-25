# SOLID Class Diagrams - Requirement 8

## Before the correction

```mermaid
classDiagram
    direction TB

    class SaleService {
        -ProductService productService
        -AccessoryService accessoryService
        -resolveItems(List~String~ itemIds) List~Product~
        -updateInventory(List~Product~ items, Map quantities) void
    }
    note for SaleService "V-01: instanceof Accessory and concrete services"
    class ReturnService {
        -ProductService productService
        -AccessoryService accessoryService
        +registerReturn(String saleId, List~String~ productIds, String reason) Return
        +calculateMonthlySales(int month, int year) double
        +calculateMonthlyReturns(int month, int year) double
        +generateMonthlyBalance(int month, int year) double
        -isInMonth(LocalDate date, int month, int year) boolean
    }
    note for ReturnService "V-01: concrete services; V-03: report arithmetic"
    class ProductService {
        +findById(String id) Product
        +updateStock(String productId, int quantity) void
        +restoreStock(String productId, int quantity) void
    }
    class AccessoryService {
        +findById(String id) Accessory
        +updateStock(String accessoryId, int quantity) void
        +restoreStock(String accessoryId, int quantity) void
    }
    class ProductRepository {
        -fromLine(String line) Product
        -toLine(Product product) String
    }
    class AccessoryRepository {
        -fromLine(String line) Accessory
        -toLine(Accessory accessory) String
    }
    class PromotionRepository {
        -fromLine(String line) Promotion
        -toLine(Promotion promotion) String
    }
    class WarrantyRepository {
        +saveAll(List~Warranty~ warranties) void
        -create(String[] fields, Product product, Sale sale) Warranty
    }
    note for ProductRepository "V-02: if / switch / instanceof by subtype (also in the other three)"

    SaleService --> ProductService
    SaleService --> AccessoryService
    ReturnService --> ProductService
    ReturnService --> AccessoryService
```
