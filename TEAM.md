# Team - GameZone Unicesar

Programming III - Universidad Popular del Cesar.

## Members

| Full name                        | Student ID   | GitHub user                                               | Role                  | Module                      | Feature branch            |
|----------------------------------|--------------|-----------------------------------------------------------|-----------------------|-----------------------------|---------------------------|
| Kevin Jhosep Mercado Moron       | 1066871906   | [@kjhosepmercadom](https://github.com/kjhosepmercadom)    | Technical Lead        | Sales and integration       | `feature/sale-module`     |
| Diego Alejandro Amaya Verdecia   | 1121326454   | [@dalejandroamaya](https://github.com/dalejandroamaya)    | Developer 1           | Products                    | `feature/product-module`  |
| Dair Jose Quintero Ortiz         | 1124379152   | [@djosequintero](https://github.com/djosequintero)        | Developer 2           | People (customers, sellers) | `feature/person-module`   |

The Technical Lead also uses the branches `feature/project-setup`,
`feature/design-docs` and `feature/console-ui` for the repository setup, the
design documents and the user interface.

## Class distribution

The work is split with a vertical cut per module: each member implements a
module that crosses the model, persistence and service layers.

| Member         | Model                                  | Persistence         | Service          | Other                 |
|----------------|----------------------------------------|---------------------|------------------|-----------------------|
| Kevin Mercado  | `Sale`                                 | `SaleRepository`    | `SaleService`    | `ConsoleMenu`, `Main` |
| Diego Amaya    | `Product` (abstract), `VideoGame`, `Console` | `ProductRepository` | `ProductService` | -               |
| Dair Quintero  | `Person` (abstract), `Customer`, `Seller`    | `PersonRepository`  | `PersonService`  | -               |

## Committed activities

### Technical Lead - Kevin Jhosep Mercado Moron

1. Create the GitHub repository with the initial configuration (README, .gitignore, license).
2. Configure the `main` and `develop` branches and enable branch protection.
3. Configure the Maven project (`pom.xml`) and the package structure of the four layers.
4. Write this `TEAM.md` file.
5. Write the analysis and the hierarchy, class and layers diagrams in `docs/`.
6. Implement the `Sale` class with its attributes, constructor and basic methods.
7. Implement the total calculation of the sale.
8. Implement `SaleRepository`.
9. Implement `SaleService` with the validation rules (at least one product, stock verification, inventory update).
10. Implement the basic structure of `ConsoleMenu` (main menu).
11. Implement the submenus for products, people and sales.
12. Implement `Main` with the initial data loading and dependency injection.
13. Review and merge the Pull Requests of the developers into `develop`.
14. Write the final `README.md` with build and run instructions.

### Developer 1 - Diego Alejandro Amaya Verdecia

1. Create the `feature/product-module` branch.
2. Implement the abstract class `Product` with its common attributes, constructor and common methods.
3. Declare the abstract method `getDescription()`.
4. Implement `VideoGame` with its attributes and its `getDescription()` implementation.
5. Implement `Console` with its attributes and its `getDescription()` implementation.
6. Implement `ProductRepository` with save and load methods.
7. Implement `ProductService` with registration, listing and stock update methods.
8. Document every class of the module with JavaDoc in English.
9. Open the Pull Request of the module to the Technical Lead.

### Developer 2 - Dair Jose Quintero Ortiz

1. Create the `feature/person-module` branch.
2. Implement the abstract class `Person` with its common attributes, constructor and common methods.
3. Declare the abstract method `getDescription()`.
4. Implement `Customer` with its particular attributes.
5. Implement `Seller` with its particular attributes.
6. Implement `PersonRepository` with save and load methods.
7. Implement `PersonService` with registration and listing methods.
8. Preload the sellers file with the store employees.
9. Document every class of the module with JavaDoc in English.
10. Open the Pull Request of the module to the Technical Lead.

## Review policy

Every Pull Request targets `develop` (or `main` for releases) and must be
approved by a member different from its author before merging:

| Pull Request                         | Author | Reviewer |
|--------------------------------------|--------|----------|
| `feature/project-setup` -> `develop` | Kevin  | Diego    |
| `feature/design-docs` -> `develop`   | Kevin  | Dair     |
| `feature/product-module` -> `develop`| Diego  | Kevin    |
| `feature/person-module` -> `develop` | Dair   | Kevin    |
| `feature/sale-module` -> `develop`   | Kevin  | Diego    |
| `feature/console-ui` -> `develop`    | Kevin  | Dair     |
| `develop` -> `main` (release)        | Kevin  | Diego    |

## Integration responsibilities (Requirements 1 to 6)

Each member keeps the role of the Taller in every module: Developer 1 builds
the model hierarchies, Developer 2 the persistence and services, and the
Technical Lead the integration in sales, the console menu and the README.

| Module / adjustment                  | Kevin Mercado (Lead)                                 | Diego Amaya (Dev 1)                                   | Dair Quintero (Dev 2)                                |
|--------------------------------------|------------------------------------------------------|-------------------------------------------------------|------------------------------------------------------|
| Accessories (R1)                     | `SaleService` and `ConsoleMenu` integration          | `Accessory`, `Controller`, `Cable`, `Memory`          | `AccessoryRepository`, `AccessoryService`            |
| Promotions (R2)                      | Discount in `Sale` and `SaleService`, menu           | `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount` | `PromotionRepository`, `PromotionService` |
| Warranties (R4)                      | Warranties in `SaleService`, menu                    | `Warranty`, `BasicWarranty`, `ExtendedWarranty`       | `WarrantyRepository`, `WarrantyService`              |
| Returns (R3)                         | `ProductService.restoreStock`, menu                  | `Return`, `Sale.canBeReturned`                        | `ReturnRepository`, `ReturnService`                  |
| Integration adjustments (R5)         | A3, A8, A9                                           | A1, A5                                                | A2, A4, A6, A7                                       |
| Exceptions and validation (R6)       | Differentiated handling in `ConsoleMenu` and `Main`  | `exceptions` and `validation` packages                | Validators and typed exceptions in services and repositories |

Branches used: `feature/accessory-module`, `feature/promotion-module`,
`feature/warranty-module`, `feature/return-module`,
`feature/accessory-category-discount`, `fix/warranty-circular-dependency`,
`refactor/unified-sale-registration`, `fix/return-accessory-stock`,
`fix/return-discounted-refund`, `fix/monthly-balance-report`,
`feature/return-warranty-cancellation`, `docs/integration-documentation` and
`feature/exception-handling`.

Each member keeps a personal AI usage log in `docs/ai-usage/`
(`leader-ai-log.md`, `developer1-ai-log.md`, `developer2-ai-log.md`).
