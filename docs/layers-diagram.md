# Layers Diagram - GameZone Unicesar

The system is organized in four layers. Arrows show the allowed dependency
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
    end

    subgraph persistence["Persistence layer - com.gamezone.persistence"]
        ProductRepository
        PersonRepository
        SaleRepository
    end

    subgraph model["Model layer - com.gamezone.model"]
        Person
        Customer
        Seller
        Product
        VideoGame
        Console
        Sale
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

| Layer       | Classes                                                                   | May depend on          |
|-------------|---------------------------------------------------------------------------|------------------------|
| UI          | `ConsoleMenu`                                                             | Service (and model objects returned by it) |
| Service     | `ProductService`, `PersonService`, `SaleService`                          | Persistence, Model     |
| Persistence | `ProductRepository`, `PersonRepository`, `SaleRepository`                 | Model                  |
| Model       | `Person`, `Customer`, `Seller`, `Product`, `VideoGame`, `Console`, `Sale` | None                   |

`ConsoleMenu` uses model objects only as values returned by the services
(for example, to print `getDescription()`); it never calls a repository.
