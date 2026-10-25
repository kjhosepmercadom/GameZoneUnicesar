# Integrated Class Diagram - Requirement 5

Single class diagram of the integrated system, grouped by layer. Attributes
are private (`-`) and accessed through getters and setters, which are omitted
here to keep the diagram readable; see the module diagrams for the details.

```mermaid
classDiagram
    direction TB

    namespace model {
        class Person {
            <<abstract>>
            -String name
            -String identification
            -String phone
            +getDescription()* String
        }
        class Customer {
            -String email
        }
        class Seller {
            -String employeeCode
            -String shift
        }
        class Product {
            <<abstract>>
            -String id
            -String title
            -double price
            -int stock
            +getDescription()* String
            +hasEnoughStock(int quantity) boolean
            +decreaseStock(int quantity) void
        }
        class VideoGame {
            -String platform
            -String genre
            -String ageRating
        }
        class Console {
            -String brand
            -String model
            -int generation
        }
        class Accessory {
            <<abstract>>
            -List~String~ compatibleConsoleIds
            +isCompatibleWith(String consoleId) boolean
            +getAccessoryType()* String
        }
        class Controller {
            -String connectionType
        }
        class Cable {
            -double lengthInMeters
            -String connectorType
        }
        class Memory {
            -int capacityInGb
            -String memoryType
        }
        class Sale {
            -String id
            -LocalDate date
            -List~Product~ products
            -String appliedPromotionName
            -double discountAmount
            -double extendedWarrantyCost
            +calculateTotal() double
            +calculateFinalTotal() double
            +canBeReturned() boolean
            +generateReceipt() String
        }
        class Promotion {
            <<abstract>>
            -String id
            -String name
            -LocalDate startDate
            -LocalDate endDate
            +isActive(LocalDate date) boolean
            +calculateDiscount(Sale sale)* double
        }
        class PercentageDiscount
        class CategoryDiscount
        class BulkPurchaseDiscount
        class Warranty {
            <<abstract>>
            -String id
            -LocalDate startDate
            -LocalDate endDate
            +getDurationInMonths()* int
            +getAdditionalCost()* double
            +isActive(LocalDate date) boolean
        }
        class BasicWarranty
        class ExtendedWarranty
        class Return {
            -String id
            -LocalDate date
            -List~Product~ returnedProducts
            -String reason
            -double refundAmount
            -double warrantyRefundAmount
            +calculateRefundAmount() double
            +generateReturnReceipt() String
        }
    }

    namespace persistence {
        class ProductRepository
        class PersonRepository
        class SaleRepository
        class AccessoryRepository
        class PromotionRepository
        class WarrantyRepository
        class ReturnRepository
    }

    namespace service {
        class ProductService {
            +updateStock(String productId, int quantity) void
            +restoreStock(String productId, int quantity) void
        }
        class PersonService
        class AccessoryService {
            +updateStock(String accessoryId, int quantity) void
            +restoreStock(String accessoryId, int quantity) void
        }
        class PromotionService {
            +findBestPromotionFor(Sale sale) Promotion
        }
        class WarrantyService {
            +loadWarranties(List~Sale~ sales) void
            +cancelWarranties(String productId, String saleId) double
        }
        class SaleService {
            +registerSale(String customerId, String sellerCode, List~String~ itemIds, List~String~ extendedWarrantyIds) Sale
            +findSaleById(String saleId) Sale
        }
        class ReturnService {
            +registerReturn(String saleId, List~String~ productIds, String reason) Return
            +calculateMonthlySales(int month, int year) double
            +calculateMonthlyReturns(int month, int year) double
            +generateMonthlyBalance(int month, int year) double
        }
    }

    namespace ui {
        class ConsoleMenu {
            +start() void
        }
    }

    class Main {
        +main(String[] args)$ void
    }

    Person <|-- Customer
    Person <|-- Seller
    Product <|-- VideoGame
    Product <|-- Console
    Product <|-- Accessory
    Accessory <|-- Controller
    Accessory <|-- Cable
    Accessory <|-- Memory
    Promotion <|-- PercentageDiscount
    Promotion <|-- CategoryDiscount
    Promotion <|-- BulkPurchaseDiscount
    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty

    Sale "0..*" --> "1" Customer
    Sale "0..*" --> "1" Seller
    Sale "0..*" o-- "1..*" Product : items
    Promotion ..> Sale : calculates discount
    Warranty "0..*" --> "1" Product : covers
    Warranty "0..*" --> "1" Sale
    Return "0..*" --> "1" Sale : original sale
    Return o-- Product : returned units

    ProductService --> ProductRepository
    PersonService --> PersonRepository
    AccessoryService --> AccessoryRepository
    PromotionService --> PromotionRepository
    WarrantyService --> WarrantyRepository
    SaleService --> SaleRepository
    SaleService --> ProductService
    SaleService --> PersonService
    SaleService --> AccessoryService
    SaleService --> PromotionService
    SaleService --> WarrantyService
    ReturnService --> ReturnRepository
    ReturnService --> SaleService
    ReturnService --> ProductService
    ReturnService --> AccessoryService
    ReturnService --> WarrantyService

    ConsoleMenu --> ProductService
    ConsoleMenu --> PersonService
    ConsoleMenu --> SaleService
    ConsoleMenu --> AccessoryService
    ConsoleMenu --> PromotionService
    ConsoleMenu --> WarrantyService
    ConsoleMenu --> ReturnService
    Main ..> ConsoleMenu : creates and starts
```
