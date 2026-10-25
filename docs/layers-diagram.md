# Layers Diagram - GameZone Unicesar

The integrated system is organized in four layers. Arrows show the allowed dependency
direction: `ui -> service -> persistence -> model`, and `service -> model`.
The model does not depend on any other layer.

```mermaid
flowchart TB
    main["Main (com.gamezone)<br/>Composition root: creates every layer and injects dependencies"]

    subgraph ui["UI layer - com.gamezone.ui"]
        ConsoleMenu
    end

    subgraph service["Service layer - com.gamezone.service"]
        ProductService
        PersonService
        SaleService
        AccessoryService
        PromotionService
        WarrantyService
        ReturnService
    end

    subgraph persistence["Persistence layer - com.gamezone.persistence"]
        ProductRepository
        PersonRepository
        SaleRepository
        AccessoryRepository
        PromotionRepository
        WarrantyRepository
        ReturnRepository
    end

    subgraph model["Model layer - com.gamezone.model"]
        direction LR
        People["Person, Customer, Seller"]
        Products["Product, VideoGame, Console"]
        Accessories["Accessory, Controller, Cable, Memory"]
        Promotions["Promotion, PercentageDiscount, CategoryDiscount, BulkPurchaseDiscount"]
        Warranties["Warranty, BasicWarranty, ExtendedWarranty"]
        Sales["Sale, Return"]
    end

    files[("data/*.csv files")]

    main --> ui
    ui --> service
    service --> persistence
    service --> model
    persistence --> model
    persistence --> files
    ui -. "only uses objects returned by services" .-> model
```

| Layer       | Classes                                                                                                   | May depend on                              |
|-------------|-----------------------------------------------------------------------------------------------------------|--------------------------------------------|
| UI          | `ConsoleMenu`                                                                                             | Service (and model objects returned by it) |
| Service     | `ProductService`, `PersonService`, `SaleService`, `AccessoryService`, `PromotionService`, `WarrantyService`, `ReturnService` | Persistence, Model                         |
| Persistence | `ProductRepository`, `PersonRepository`, `SaleRepository`, `AccessoryRepository`, `PromotionRepository`, `WarrantyRepository`, `ReturnRepository` | Model                                      |
| Model       | People, products, accessories, promotions, warranties, sales and returns                                  | None                                       |

After adjustment A2 no repository depends on a service: references between
files (sale of a warranty, sale of a return) are resolved from the objects
that the services pass to `loadAll`.
