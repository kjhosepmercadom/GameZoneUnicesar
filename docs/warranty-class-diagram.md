# Warranty Class Diagram - Requirement 4

```mermaid
classDiagram
    direction TB

    class Warranty {
        <<abstract>>
        -String id
        -Product product
        -Sale sale
        -LocalDate startDate
        -LocalDate endDate
        +Warranty(String id, Product product, Sale sale, LocalDate startDate)
        +getId() String
        +getProduct() Product
        +getSale() Sale
        +getStartDate() LocalDate
        +getEndDate() LocalDate
        +getDurationInMonths()* int
        +getWarrantyType()* String
        +getAdditionalCost()* double
        +isActive(LocalDate date) boolean
        +generateWarrantyCertificate() String
    }
    class BasicWarranty {
        +getDurationInMonths() int
        +getWarrantyType() String
        +getAdditionalCost() double
    }
    class ExtendedWarranty {
        +getDurationInMonths() int
        +getWarrantyType() String
        +getAdditionalCost() double
    }
    class Product {
        <<abstract>>
    }
    class Console
    class Sale {
        -double extendedWarrantyCost
        +getExtendedWarrantyCost() double
        +setExtendedWarrantyCost(double cost) void
        +calculateFinalTotal() double
    }
    class WarrantyRepository {
        -Path filePath
        -SaleService saleService
        -ProductService productService
        +WarrantyRepository(String filePath, SaleService saleService, ProductService productService)
        +loadAll() List~Warranty~
        +saveAll(List~Warranty~ warranties) void
    }
    class WarrantyService {
        -WarrantyRepository warrantyRepository
        -List~Warranty~ warranties
        +assignBasicWarranty(Product product, Sale sale, LocalDate startDate) BasicWarranty
        +assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) ExtendedWarranty
        +findWarrantyByProduct(String productId, String saleId) Warranty
        +listAllWarranties() List~Warranty~
        +listActiveWarranties() List~Warranty~
        +listWarrantiesExpiringSoon(int daysAhead) List~Warranty~
    }
    class SaleService {
        +registerSale(String customerId, String sellerCode, List~String~ itemIds, List~String~ extendedWarrantyIds) Sale
        +findSaleById(String saleId) Sale
    }
    class ProductService {
        +findById(String id) Product
    }
    class ConsoleMenu {
        -WarrantyService warrantyService
        -showWarrantyMenu() void
    }

    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty
    Product <|-- Console
    Warranty "0..*" --> "1" Product : covers
    Warranty "0..*" --> "1" Sale : belongs to
    WarrantyRepository ..> Warranty : reads/writes
    WarrantyService "1" --> "1" WarrantyRepository : uses
    WarrantyService "1" o-- "0..*" Warranty : manages
    SaleService "1" --> "1" WarrantyService : assigns warranties
    ConsoleMenu "1" --> "1" WarrantyService : uses
    WarrantyRepository --> SaleService : resolves sales while loading
    WarrantyRepository --> ProductService : resolves products while loading
```

With this design `WarrantyRepository` needs `SaleService` to load its data,
while `SaleService` needs `WarrantyService` to assign warranties. This
circular dependency is resolved in the integration adjustment A2.
