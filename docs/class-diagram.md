classDiagram
    direction TB

    %% CAPA MODELO
    namespace model {
        class Person {
            <<abstract>>
            -String id
            -String name
            -String phone
            +Person(id: String, name: String, phone: String)
            +getId() String
            +getName() String
            +getPhone() String
            +setId(id: String) void
            +setName(name: String) void
            +setPhone(phone: String) void
            +getRoleDescription()* String
        }

        class Customer {
            -String email
            +Customer(id: String, name: String, phone: String, email: String)
            +getEmail() String
            +setEmail(email: String) void
            +getRoleDescription() String
        }

        class Seller {
            -String employeeCode
            -String shift
            +Seller(id: String, name: String, phone: String, employeeCode: String, shift: String)
            +getEmployeeCode() String
            +getShift() String
            +setEmployeeCode(employeeCode: String) void
            +setShift(shift: String) void
            +getRoleDescription() String
        }

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

        class Sale {
            -String id
            -String date
            -Customer customer
            -Seller seller
            -List~Product~ products
            +Sale(id: String, date: String, customer: Customer, seller: Seller, products: List~Product~)
            +getId() String
            +getDate() String
            +getCustomer() Customer
            +getSeller() Seller
            +getProducts() List~Product~
            +calculateTotal() double
        }
    }

    %% CAPA PERSISTENCIA
    namespace persistence {
        class ProductRepository {
            -String filePath
            +ProductRepository(filePath: String)
            +saveAll(products: List~Product~) void
            +loadAll() List~Product~
        }

        class PersonRepository {
            -String filePath
            +PersonRepository(filePath: String)
            +saveAll(people: List~Person~) void
            +loadAll() List~Person~
        }

        class SaleRepository {
            -String filePath
            +SaleRepository(filePath: String)
            +saveAll(sales: List~Sale~) void
            +loadAll() List~Sale~
        }
    }

    %% CAPA SERVICIOS
    namespace service {
        class ProductService {
            -ProductRepository productRepository
            -List~Product~ products
            +ProductService(productRepository: ProductRepository)
            +registerProduct(product: Product) void
            +getAllProducts() List~Product~
            +findProductById(id: String) Product
            +updateStock(productId: String, quantity: int) boolean
        }

        class PersonService {
            -PersonRepository personRepository
            -List~Person~ people
            +PersonService(personRepository: PersonRepository)
            +registerPerson(person: Person) void
            +getAllCustomers() List~Customer~
            +getAllSellers() List~Seller~
            +findCustomerById(id: String) Customer
            +findSellerById(id: String) Seller
        }

        class SaleService {
            -SaleRepository saleRepository
            -ProductService productService
            -PersonService personService
            -List~Sale~ sales
            +SaleService(saleRepository: SaleRepository, productService: ProductService, personService: PersonService)
            +registerSale(customerId: String, sellerId: String, productIds: List~String~) Sale
            +getAllSales() List~Sale~
            +getSalesByCustomer(customerId: String) List~Sale~
            +getSalesBySeller(sellerId: String) List~Sale~
        }
    }

    %% CAPA INTERFAZ Y PRINCIPAL
    namespace ui {
        class ConsoleUI {
            -ProductService productService
            -PersonService personService
            -SaleService saleService
            +ConsoleUI(productService: ProductService, personService: PersonService, saleService: SaleService)
            +start() void
            -showMenu() void
            -handleProductMenu() void
            -handlePersonMenu() void
            -handleSaleMenu() void
        }
    }

    class Main {
        +main(args: String[]) void
    }

    %% RELACIONES DE HERENCIA
    Person <|-- Customer
    Person <|-- Seller
    Product <|-- VideoGame
    Product <|-- Console

    %% RELACIONES DEL MODELO
    Sale "0..*" --> "1" Customer : purchasedBy
    Sale "0..*" --> "1" Seller : handledBy
    Sale "0..*" o-- "1..*" Product : contains

    %% RELACIONES DE PERSISTENCIA - MODELO
    ProductRepository ..> Product : manages
    PersonRepository ..> Person : manages
    SaleRepository ..> Sale : manages

    %% RELACIONES DE SERVICIO - PERSISTENCIA Y MODELO
    ProductService --> ProductRepository : uses
    ProductService ..> Product : manipulates
    PersonService --> PersonRepository : uses
    PersonService ..> Person : manipulates
    SaleService --> SaleRepository : uses
    SaleService --> ProductService : uses
    SaleService --> PersonService : uses
    SaleService ..> Sale : manages

    %% RELACIONES DE UI Y MAIN
    ConsoleUI --> ProductService : uses
    ConsoleUI --> PersonService : uses
    ConsoleUI --> SaleService : uses
    Main ..> ConsoleUI : boots