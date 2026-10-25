# Accessory Class Diagram - Requirement 1

```mermaid
classDiagram
    direction TB

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
    class Console {
        -String brand
        -String model
        -int generation
    }
    class Accessory {
        <<abstract>>
        -List~String~ compatibleConsoleIds
        +Accessory(String id, String title, double price, int stock, List~String~ compatibleConsoleIds)
        +getCompatibleConsoleIds() List~String~
        +setCompatibleConsoleIds(List~String~ ids) void
        +addCompatibleConsole(String consoleId) void
        +removeCompatibleConsole(String consoleId) void
        +isCompatibleWith(String consoleId) boolean
        +getAccessoryType()* String
        +getDescription() String
    }
    class Controller {
        -String connectionType
        +getConnectionType() String
        +setConnectionType(String connectionType) void
        +getAccessoryType() String
        +getDescription() String
    }
    class Cable {
        -double lengthInMeters
        -String connectorType
        +getLengthInMeters() double
        +setLengthInMeters(double lengthInMeters) void
        +getConnectorType() String
        +setConnectorType(String connectorType) void
        +getAccessoryType() String
        +getDescription() String
    }
    class Memory {
        -int capacityInGb
        -String memoryType
        +getCapacityInGb() int
        +setCapacityInGb(int capacityInGb) void
        +getMemoryType() String
        +setMemoryType(String memoryType) void
        +getAccessoryType() String
        +getDescription() String
    }
    class Sale {
        -List~Product~ products
        +calculateTotal() double
    }
    class AccessoryRepository {
        -Path filePath
        +saveAll(List~Accessory~ accessories) void
        +loadAll() List~Accessory~
    }
    class AccessoryService {
        -AccessoryRepository accessoryRepository
        -List~Accessory~ accessories
        +registerController(...) Controller
        +registerCable(...) Cable
        +registerMemory(...) Memory
        +listAllAccessories() List~Accessory~
        +listAccessoriesByType(String type) List~Accessory~
        +findAccessoriesCompatibleWith(String consoleId) List~Accessory~
        +findById(String id) Accessory
        +updateStock(String accessoryId, int quantity) void
    }
    class SaleService {
        -ProductService productService
        -AccessoryService accessoryService
        +registerSale(String customerId, String sellerCode, List~String~ itemIds) Sale
    }
    class ConsoleMenu {
        -AccessoryService accessoryService
        -showAccessoryMenu() void
    }

    Product <|-- Console
    Product <|-- Accessory
    Accessory <|-- Controller
    Accessory <|-- Cable
    Accessory <|-- Memory

    Sale "0..*" o-- "1..*" Product : products
    Accessory "0..*" ..> "0..*" Console : compatible by id
    AccessoryRepository ..> Accessory : reads/writes
    AccessoryService "1" --> "1" AccessoryRepository : uses
    AccessoryService "1" o-- "0..*" Accessory : manages
    SaleService "1" --> "1" AccessoryService : resolves items and updates stock
    ConsoleMenu "1" --> "1" AccessoryService : uses
```

The compatibility relationship is stored in `Accessory.compatibleConsoleIds`
as console identifiers, so it is drawn as a dependency by identifier and not
as an object reference.
