# Class Diagram - GameZone Unicesar

Complete class diagram of the system grouped by layer (namespaces `model`,
`persistence`, `service` and `ui`, plus the `Main` class of the root package).

Notation: `-` private, `+` public, `#` protected. Abstract classes are marked
with `<<abstract>>` and abstract methods end with `*`. Getters and setters of
every private attribute exist in the code; in the model they are listed
explicitly.

```mermaid
classDiagram
    direction TB

    namespace model {
        class Person {
            <<abstract>>
            -String name
            -String identification
            -String phone
            +Person(String name, String identification, String phone)
            +getName() String
            +setName(String name) void
            +getIdentification() String
            +setIdentification(String identification) void
            +getPhone() String
            +setPhone(String phone) void
            +getDescription()* String
        }
        class Customer {
            -String email
            +Customer(String name, String identification, String phone, String email)
            +getEmail() String
            +setEmail(String email) void
            +getDescription() String
        }
        class Seller {
            -String employeeCode
            -String shift
            +Seller(String name, String identification, String phone, String employeeCode, String shift)
            +getEmployeeCode() String
            +setEmployeeCode(String employeeCode) void
            +getShift() String
            +setShift(String shift) void
            +getDescription() String
        }
        class Product {
            <<abstract>>
            -String id
            -String title
            -double price
            -int stock
            +Product(String id, String title, double price, int stock)
            +getId() String
            +setId(String id) void
            +getTitle() String
            +setTitle(String title) void
            +getPrice() double
            +setPrice(double price) void
            +getStock() int
            +setStock(int stock) void
            +getDescription()* String
            +hasEnoughStock(int quantity) boolean
            +decreaseStock(int quantity) void
        }
        class VideoGame {
            -String platform
            -String genre
            -String ageRating
            +VideoGame(String id, String title, double price, int stock, String platform, String genre, String ageRating)
            +getPlatform() String
            +setPlatform(String platform) void
            +getGenre() String
            +setGenre(String genre) void
            +getAgeRating() String
            +setAgeRating(String ageRating) void
            +getDescription() String
        }
        class Console {
            -String brand
            -String model
            -int generation
            +Console(String id, String title, double price, int stock, String brand, String model, int generation)
            +getBrand() String
            +setBrand(String brand) void
            +getModel() String
            +setModel(String model) void
            +getGeneration() int
            +setGeneration(int generation) void
            +getDescription() String
        }
        class Sale {
            -String id
            -LocalDate date
            -Customer customer
            -Seller seller
            -List~Product~ products
            +Sale(String id, LocalDate date, Customer customer, Seller seller, List~Product~ products)
            +getId() String
            +getDate() LocalDate
            +getCustomer() Customer
            +getSeller() Seller
            +getProducts() List~Product~
            +calculateTotal() double
            +generateReceipt() String
        }
    }

    namespace persistence {
        class ProductRepository {
            -Path filePath
            +ProductRepository(String filePath)
            +saveAll(List~Product~ products) void
            +loadAll() List~Product~
            -fromLine(String line) Product
            -toLine(Product product) String
            -clean(String value) String
        }
        class PersonRepository {
            -Path customersFile
            -Path sellersFile
            +PersonRepository(String customersFile, String sellersFile)
            +saveCustomers(List~Customer~ customers) void
            +loadCustomers() List~Customer~
            +saveSellers(List~Seller~ sellers) void
            +loadSellers() List~Seller~
            -writeLines(Path file, List~String~ lines) void
            -readRecords(Path file, int expectedFields) List~String[]~
            -clean(String value) String
        }
        class SaleRepository {
            -Path filePath
            +SaleRepository(String filePath)
            +saveAll(List~Sale~ sales) void
            +loadAll(List~Customer~ customers, List~Seller~ sellers, List~Product~ products) List~Sale~
            -fromLine(String line, List~Customer~ customers, List~Seller~ sellers, List~Product~ products) Sale
        }
    }

    namespace service {
        class ProductService {
            -ProductRepository productRepository
            -List~Product~ products
            +ProductService(ProductRepository productRepository)
            +registerVideoGame(String id, String title, double price, int stock, String platform, String genre, String ageRating) VideoGame
            +registerConsole(String id, String title, double price, int stock, String brand, String model, int generation) Console
            +listAllProducts() List~Product~
            +findById(String id) Product
            +hasEnoughStock(String productId, int quantity) boolean
            +updateStock(String productId, int quantity) void
            -validateCommonData(String id, String title, double price, int stock) void
        }
        class PersonService {
            -PersonRepository personRepository
            -List~Customer~ customers
            -List~Seller~ sellers
            +PersonService(PersonRepository personRepository)
            +registerCustomer(String name, String identification, String phone, String email) Customer
            +listCustomers() List~Customer~
            +listSellers() List~Seller~
            +findCustomerById(String identification) Customer
            +findSellerByEmployeeCode(String employeeCode) Seller
            -isValidEmail(String email) boolean
        }
        class SaleService {
            -SaleRepository saleRepository
            -ProductService productService
            -PersonService personService
            -List~Sale~ sales
            +SaleService(SaleRepository saleRepository, ProductService productService, PersonService personService)
            +registerSale(String customerId, String sellerCode, List~String~ productIds) Sale
            +listAllSales() List~Sale~
            +findSalesByCustomer(String customerId) List~Sale~
            +findSalesBySeller(String sellerCode) List~Sale~
            -nextSaleId() String
        }
    }

    namespace ui {
        class ConsoleMenu {
            -ProductService productService
            -PersonService personService
            -SaleService saleService
            -Scanner scanner
            +ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService)
            +start() void
            -showProductMenu() void
            -showPersonMenu() void
            -showSaleMenu() void
            -registerSale() void
        }
    }

    class Main {
        +main(String[] args)$ void
    }

    %% Inheritance
    Person <|-- Customer
    Person <|-- Seller
    Product <|-- VideoGame
    Product <|-- Console

    %% Model associations
    Sale "0..*" --> "1" Customer : customer
    Sale "0..*" --> "1" Seller : seller
    Sale "0..*" o-- "1..*" Product : products

    %% Persistence depends on the model
    ProductRepository ..> Product : reads/writes
    PersonRepository ..> Customer : reads/writes
    PersonRepository ..> Seller : reads/writes
    SaleRepository ..> Sale : reads/writes

    %% Services
    ProductService "1" --> "1" ProductRepository : uses
    ProductService "1" o-- "0..*" Product : manages
    PersonService "1" --> "1" PersonRepository : uses
    PersonService "1" o-- "0..*" Person : manages
    SaleService "1" --> "1" SaleRepository : uses
    SaleService "1" --> "1" ProductService : uses
    SaleService "1" --> "1" PersonService : uses
    SaleService "1" o-- "0..*" Sale : manages

    %% User interface
    ConsoleMenu "1" --> "1" ProductService : uses
    ConsoleMenu "1" --> "1" PersonService : uses
    ConsoleMenu "1" --> "1" SaleService : uses

    %% Composition root
    Main ..> ConsoleMenu : creates
    Main ..> ProductService : creates
    Main ..> PersonService : creates
    Main ..> SaleService : creates
    Main ..> ProductRepository : creates
    Main ..> PersonRepository : creates
    Main ..> SaleRepository : creates
```

## Relationships summary

| Relationship                      | Type        | Multiplicity     |
|-----------------------------------|-------------|------------------|
| `Customer`, `Seller` -> `Person`  | Inheritance | -                |
| `VideoGame`, `Console` -> `Product` | Inheritance | -              |
| `Sale` -> `Customer`              | Association | `0..*` to `1`    |
| `Sale` -> `Seller`                | Association | `0..*` to `1`    |
| `Sale` -> `Product`               | Aggregation | `0..*` to `1..*` |
| Services -> Repositories          | Association | `1` to `1`       |
| `SaleService` -> `ProductService`, `PersonService` | Association | `1` to `1` |
| `ConsoleMenu` -> Services         | Association | `1` to `1`       |
| Repositories -> model classes     | Dependency  | -                |
| `Main` -> every layer             | Dependency  | -                |
