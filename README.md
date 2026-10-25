# GameZone Unicesar

Information system for **GameZone Unicesar**, a video game store located in
the university area of Valledupar. The integrated system manages products
(video games and consoles), accessories, people (customers and sellers),
sales with automatic promotions and warranties, and partial returns. All the
information is kept in files between executions.

Programming III - Universidad Popular del Cesar.

## Features

The console menu (in Spanish) is organized in seven submenus:

| Menu                        | Operations                                                                                     |
|-----------------------------|------------------------------------------------------------------------------------------------|
| 1. Gestion de productos     | Register a video game, register a console, list the inventory                                 |
| 2. Gestion de personas      | Register a customer, list customers, list sellers                                             |
| 3. Gestion de ventas        | Register a sale, full history, history of a customer, sales of a seller, detail of a sale     |
| 4. Gestion de accesorios    | Register a controller, a cable or a memory, list all, list by type, compatible with a console |
| 5. Gestion de promociones   | Register percentage, category and bulk promotions, list all, list active                      |
| 6. Gestion de garantias     | Warranty of a product in a sale, list all, list active, list expiring soon                    |
| 7. Gestion de devoluciones  | Register a return, list all, by customer, by sale, monthly balance                            |

Business rules of the integrated sale (see the [integration analysis](docs/integration-analysis.md)):

1. A sale must contain at least one item (product or accessory).
2. Every item must exist and have enough stock.
3. The best active promotion is applied over the subtotal of the items.
4. Every console gets a six month basic warranty; an extended twelve month
   warranty can be added for 10% of the console price.
5. Final total = subtotal - discount + extended warranties.
6. The stock of every item is deducted through the service of its type.
7. Returns are accepted within thirty days and only for items of the sale; the
   refund is proportional to the discount of the sale and includes the cost of
   the cancelled extended warranties, and the stock is restored.

## Architecture

```
src/main/java/com/gamezone
├── model         Person, Customer, Seller, Product, VideoGame, Console, Accessory, Controller,
│                 Cable, Memory, Sale, Promotion (3 types), Warranty (2 types), Return
├── persistence   One CSV repository per module
├── service       Business rules: one service per module
├── ui            ConsoleMenu
└── Main.java     Builds the layers and starts the application
```

Allowed dependencies: `ui -> service -> persistence -> model`. Design documents:

- Taller: [analysis](docs/analysis.md), [hierarchy](docs/hierarchy-diagram.md),
  [class diagram](docs/class-diagram.md), [layers](docs/layers-diagram.md)
- Modules: [accessories](docs/accessory-analysis.md) ([diagram](docs/accessory-class-diagram.md)),
  [promotions](docs/promotion-analysis.md) ([diagram](docs/promotion-class-diagram.md)),
  [warranties](docs/warranty-analysis.md) ([diagram](docs/warranty-class-diagram.md)),
  [returns](docs/return-analysis.md) ([diagram](docs/return-class-diagram.md))
- Integration: [analysis](docs/integration-analysis.md), [integrated class diagram](docs/integrated-class-diagram.md)
- AI usage logs: [docs/ai-usage](docs/ai-usage)

### Exceptions and validation (Requirement 6)

Two cross-cutting packages separate validation and error handling from the
business logic:

- `com.gamezone.exceptions`: `GameZoneException` (abstract, unchecked) and its
  subclasses `ResourceNotFoundException`, `BusinessRuleException`,
  `InvalidDataException` and `PersistenceException`.
- `com.gamezone.validation`: `ProductValidator`, `PersonValidator` and
  `SaleValidator`, invoked by the services before every operation.

Repositories wrap file errors in `PersistenceException` with the file and the
operation, and `ConsoleMenu` shows a different message for each exception
type; the user never sees a technical trace. Design:
[analysis](docs/exception-analysis.md) and [class diagram](docs/exception-class-diagram.md).

## Requirements

- JDK 17 or newer
- Apache Maven 3.8 or newer (or an IDE with Maven support such as NetBeans or IntelliJ IDEA)

## Build and run

Run every command from the root folder of the repository, so the application
finds the `data/` folder.

```bash
mvn clean package
java -jar target/gamezone-unicesar-1.0.0.jar
```

Alternatively, `mvn compile exec:java`. In NetBeans: *File > Open Project*,
select the repository folder and press *Run Project* (F6).

## Data files

| File                    | Content                                         | Versioned |
|-------------------------|-------------------------------------------------|-----------|
| `data/sellers.csv`      | Sellers of the store (3 preloaded)              | Yes       |
| `data/products.csv`     | Video games and consoles (preloaded examples)   | Yes       |
| `data/accessories.csv`  | Accessories (one of each type preloaded)        | Yes       |
| `data/promotions.csv`   | Promotions (one of each type, plus accessories) | Yes       |
| `data/customers.csv`    | Customers registered from the menu              | No        |
| `data/sales.csv`        | Sales with promotion and warranty cost          | No        |
| `data/warranties.csv`   | Warranties generated by the sales               | No        |
| `data/returns.csv`      | Returns                                         | No        |

## Demonstration scenario

1. Register a customer and a sale with a console, a video game and an
   accessory, adding the extended warranty to the console.
2. Check the receipt: best promotion, extended warranty cost and final total.
3. List the active warranties (basic and extended).
4. Return the console and the accessory: stock restored, warranty cancelled,
   proportional refund.
5. Show the monthly balance: sales, returns and net balance.

## Git workflow

Simplified Git Flow: `main` (stable), `develop` (integration) and work
branches created from `develop` with the prefixes `feature/`, `fix/`,
`refactor/` and `docs/`, integrated through reviewed Pull Requests and deleted
after merging. Commits follow Conventional Commits. Team: [TEAM.md](TEAM.md).

## Design review: GRASP (Requirement 7)

The code was audited against the nine GRASP patterns by the member
responsible for each part (`docs/grasp-audit/`), and the seven violations of
the [consolidated audit](docs/grasp-audit/consolidated-audit.md) were fixed
without changing the behaviour:

| ID   | Change |
|------|--------|
| V-01 | `ConsoleMenu` split into `ConsoleInput`, `ErrorPrinter` and one submenu per module (`ProductMenu`, `PersonMenu`, `SaleMenu`, `AccessoryMenu`, `PromotionMenu`, `WarrantyMenu`, `ReturnMenu`) |
| V-02 | `Product.increaseStock` used by `ProductService` and `AccessoryService` |
| V-03 | `Sale.calculateDiscountRate` used by `Return` |
| V-04 | `Sale.findProduct` and `Sale.countUnitsOf` used by `ReturnService` |
| V-05 | `Product.getCategory` replaces the `instanceof` chain of `CategoryDiscount` |
| V-06 | `WarrantyService` prefers the warranty with the longest coverage |
| V-07 | `CsvFile` concentrates the file handling of the seven repositories |

Documents: [analysis](docs/grasp-analysis.md), [before/after diagram](docs/grasp-class-diagram.md),
[refactor log and non-regression evidence](docs/grasp-refactor-log.md). The
regression script is `docs/regression/menu-operations-input.txt`.

## Design review: SOLID (Requirement 8)

After the GRASP correction the code was audited against the five SOLID
principles (`docs/solid-audit/`) and the three violations of the
[consolidated audit](docs/solid-audit/consolidated-audit.md) were fixed:

| ID   | Change |
|------|--------|
| V-01 | `InventoryService` interface implemented by `ProductService` and `AccessoryService`; `SaleService` and `ReturnService` no longer check item types |
| V-02 | `RecordMapper` and `WarrantyRecordMapper`: one mapper per subtype in the product, accessory, promotion and warranty repositories |
| V-03 | `MonthlyBalanceCalculator` takes the report arithmetic out of `ReturnService` |

Documents: [analysis](docs/solid-analysis.md), [before/after diagram](docs/solid-class-diagram.md),
[refactor log and non-regression evidence](docs/solid-refactor-log.md).

## License

[MIT](LICENSE)
