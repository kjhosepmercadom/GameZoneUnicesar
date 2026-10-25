# SOLID Audit - Developer 1 (Diego Alejandro Amaya Verdecia)

Audited code: `main` at commit `6d196b3`, the same code that `develop`
had when Requirement 8 started (GRASP correction of Requirement 7 included).

## Scope

| Module (requirement)        | Classes and methods audited |
|-----------------------------|-----------------------------|
| Products (Taller 1)         | `Product` (including `increaseStock` and `getCategory` added in R7), `VideoGame`, `Console`, `ProductRepository` (on `CsvFile` since R7), `ProductService` (except `restoreStock`) |
| Accessories (R1)            | `Accessory` (including `getCategory`), `Controller`, `Cable`, `Memory` |
| Promotions (R2, A1)         | `Promotion`, `PercentageDiscount`, `CategoryDiscount` (on `getCategory` since R7), `BulkPurchaseDiscount` |
| Returns (R3, A5)            | `Return` (on `Sale.calculateDiscountRate` since R7), `Sale.canBeReturned()` |
| Warranties (R4)             | `Warranty`, `BasicWarranty`, `ExtendedWarranty` |
| Exceptions and validation (R6) | packages `exceptions` and `validation` |

The responsibility of the GRASP changes is taken from `docs/grasp-refactor-log.md`.
Verdict legend: **violation**, **correct**, **not applicable**.

## Product module

| Principle | Verdict | Evidence |
|-----------|---------|----------|
| SRP | correct | `Product` keeps data and stock rules; `ProductRepository` only converts lines (file access is in `CsvFile`); `ProductService` only applies rules. Each class has one reason to change. |
| OCP | **violation** | `ProductRepository.fromLine` and `toLine` (see V-01). The model is open: a new product type is a new subclass of `Product`. |
| LSP | correct | `VideoGame` and `Console` can replace `Product` everywhere: they inherit stock behaviour unchanged and implement `getDescription()` and `getCategory()` without preconditions. |
| ISP | not applicable | The module declares no interface; `Product` only declares abstract methods that every subclass needs. |
| DIP | correct in this code | `ProductService` receives `ProductRepository` through its constructor. The service itself is used as a concrete class by `SaleService` and `ReturnService` to update stock; that dependency belongs to the code of those clients and was reported to the Leader. |

### V-01 - Product types decided inside the repository (boundary)

| Field | Content |
|-------|---------|
| Principle | OCP |
| Module | Products (Taller 1) - `ProductRepository` |
| Evidence | `fromLine`: `if (VIDEO_GAME_TYPE.equals(type)) { return new VideoGame(...); } if (CONSOLE_TYPE.equals(type)) { return new Console(...); }`; `toLine`: `if (product instanceof VideoGame) { ... } if (product instanceof Console) { ... }` |
| Explanation | The repository is not closed for modification: supporting a new product type means editing both methods. |
| Consequence | Adding a new product (for example, a collector edition with extra columns) changes and risks the loading of the existing products. The same pattern exists in the accessory, promotion and warranty repositories (Developer 2). |
| Proposed solution | A `RecordMapper<T>` abstraction with one mapper per type; the repository keeps a list of mappers and only iterates it. |
| Code of another member | The same violation exists in code of Developer 2; reported to the Leader as a boundary finding. |

## Accessory and promotion hierarchies

| Principle | Verdict | Evidence |
|-----------|---------|----------|
| SRP | correct | Each class keeps the data and the rule of one accessory or promotion type. |
| OCP | correct | Since R7, `CategoryDiscount` uses `product.getCategory()`: a new product category does not modify the promotion. A new promotion is a new subclass of `Promotion`; `PromotionService.findBestPromotionFor` does not change. |
| LSP | correct | Every `Accessory` can be used as a `Product` (stock, price, description). Every `Promotion` subclass returns a non negative discount and never throws for a sale without its category (it returns 0), as the base contract says. |
| ISP | correct | `Promotion` declares only `calculateDiscount` and `getDescription`, both used by every client. |
| DIP | correct | The promotions depend on the abstraction `Product`, not on concrete products. |
