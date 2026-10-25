# GameZone Unicesar

Information system for **GameZone Unicesar**, a video game store located in
the university area of Valledupar. The application manages products (video
games and consoles), people (customers and sellers) and sales, and keeps all
the information in files between executions.

Programming III - Universidad Popular del Cesar.

## Features

The console menu (in Spanish) offers ten operations:

| Module   | Operation                                                         |
|----------|-------------------------------------------------------------------|
| Products | 1. Register a video game                                          |
|          | 2. Register a console                                             |
|          | 3. List the product inventory                                     |
| People   | 4. Register a customer                                            |
|          | 5. List customers                                                 |
|          | 6. List sellers                                                   |
| Sales    | 7. Register a sale (customer, seller and one or more products)    |
|          | 8. Show the complete sales history                                |
|          | 9. Show the purchase history of a customer                        |
|          | 10. Show the sales attended by a seller                           |

Business rules:

- A sale must contain at least one product.
- A product cannot be sold when its stock is insufficient.
- Registering a sale deducts the sold quantities from the inventory.
- The sale total is the sum of the prices of the products bought.
- Data is loaded automatically at startup and saved after every operation.

## Architecture

The code lives in the `com.gamezone` package and is organized in four layers:

```
src/main/java/com/gamezone
├── model         Domain classes: Person, Customer, Seller, Product, VideoGame, Console, Sale
├── persistence   CSV repositories: ProductRepository, PersonRepository, SaleRepository
├── service       Business rules: ProductService, PersonService, SaleService
├── ui            Console menu: ConsoleMenu
└── Main.java     Builds the layers and starts the application
```

Allowed dependencies: `ui -> service -> persistence -> model`. See the design
documents for details:

- [Analysis](docs/analysis.md)
- [Hierarchy diagram](docs/hierarchy-diagram.md)
- [Class diagram](docs/class-diagram.md)
- [Layers diagram](docs/layers-diagram.md)

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

Alternatively, compile and run in one step:

```bash
mvn compile exec:java
```

In NetBeans: *File > Open Project*, select the repository folder and press
*Run Project* (F6).

## Data files

| File                  | Content                                   | Versioned |
|-----------------------|-------------------------------------------|-----------|
| `data/sellers.csv`    | Sellers of the store (3 preloaded)        | Yes       |
| `data/products.csv`   | Product inventory (preloaded examples)    | Yes       |
| `data/customers.csv`  | Customers registered from the menu        | No        |
| `data/sales.csv`      | Sales registered from the menu            | No        |

Files use `;` as separator. Products include a type discriminator
(`VIDEOGAME` or `CONSOLE`) in the first column. Missing files are treated as
empty and are created on the first save.

## Git workflow

The team follows a simplified Git Flow:

- `main`: stable version, updated only through Pull Requests from `develop`.
- `develop`: integration branch, updated only through approved Pull Requests.
- `feature/*`: one branch per functionality, created from `develop` and
  deleted after merging.

Commit messages follow Conventional Commits (`feat:`, `fix:`, `docs:`,
`refactor:`, `chore:`). Team roles and responsibilities are described in
[TEAM.md](TEAM.md).

## License

[MIT](LICENSE)

## Accessory module (Requirement 1)

The store sells three types of accessories, all of them `Product`s:
controllers (connection type), cables (length and connector) and memories
(capacity and memory type). Every accessory keeps the identifiers of the
consoles it is compatible with.

Menu *4. Gestion de accesorios*: register a controller, a cable or a memory,
list every accessory, list accessories by type and list the accessories
compatible with a console. Sales can mix products and accessories; stock
validation and inventory update work the same for both. Preloaded data:
`data/accessories.csv`. Design: [analysis](docs/accessory-analysis.md) and
[class diagram](docs/accessory-class-diagram.md).

## Promotion module (Requirement 2)

Three promotion types with a validity period: percentage over the subtotal,
percentage over one category, and percentage when the sale reaches a minimum
number of products. When a sale is registered the system applies the active
promotion that gives the biggest discount (promotions are not cumulative), and
the receipt shows subtotal, discount and final total.

Menu *5. Gestion de promociones*: register each promotion type, list every
promotion and list the active ones. Menu *3. Gestion de ventas* adds the sale
detail query. Preloaded data: `data/promotions.csv`. Design:
[analysis](docs/promotion-analysis.md) and [class diagram](docs/promotion-class-diagram.md).

## Warranty module (Requirement 4)

Every console sold receives a six month basic warranty. The seller can add a
twelve month extended warranty to each console, which costs 10% of its price
and is added to the sale total.

Menu *6. Gestion de garantias*: query the warranty of a product in a sale,
list every warranty, list the active ones and list the warranties that expire
within a number of days. Data: `data/warranties.csv` (generated by the sales).
Design: [analysis](docs/warranty-analysis.md) and [class diagram](docs/warranty-class-diagram.md).

## Return module (Requirement 3)

A customer can return some or all of the products of a sale within thirty
days. The returned products must belong to the sale, their stock goes back to
the inventory and the refund is calculated automatically.

Menu *7. Gestion de devoluciones*: register a return, list every return, list
the returns of a customer or of a sale, and show the monthly balance (sales
minus returns). Data: `data/returns.csv`. Design: [analysis](docs/return-analysis.md)
and [class diagram](docs/return-class-diagram.md).
