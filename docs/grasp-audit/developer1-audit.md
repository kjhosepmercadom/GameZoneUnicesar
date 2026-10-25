# GRASP Audit - Developer 1 (Diego Alejandro Amaya Verdecia)

Audited code: `main` at commit `764d658`, the same code that `develop`
had when Requirement 7 started.

## Scope

| Module (requirement)        | Classes and methods audited                                              |
|-----------------------------|--------------------------------------------------------------------------|
| Products (Taller 1)         | `Product`, `VideoGame`, `Console`, `ProductRepository`, `ProductService` (all methods except `restoreStock`) |
| Accessories (R1)            | `Accessory`, `Controller`, `Cable`, `Memory`                             |
| Promotions (R2, A1)         | `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount` |
| Returns (R3, A5)            | `Return`, `Sale.canBeReturned()`                                         |
| Warranties (R4)             | `Warranty`, `BasicWarranty`, `ExtendedWarranty`                          |
| Exceptions and validation (R6) | package `exceptions` (5 classes), package `validation` (3 classes)    |

Verdict legend: **violation**, **correct**, **not applicable**.

## Product module

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | correct | `Product.hasEnoughStock` and `Product.decreaseStock` work with the `stock` attribute of the product itself. |
| Creator               | correct | `ProductService.registerVideoGame/registerConsole` create the products: the service keeps the product list (it aggregates them) and receives the initializing data. |
| Controller            | correct | `ProductService` receives the product operations from the menu and coordinates validation, creation and persistence. |
| Low Coupling          | correct | `ProductService` depends only on `ProductRepository`, the model and `ProductValidator`; `ProductRepository` depends only on the model. |
| High Cohesion         | correct | The model keeps data and stock rules; the repository only converts to CSV; the service only applies rules. |
| Polymorphism          | correct | `getDescription()` is abstract in `Product` and implemented with `@Override` by `VideoGame` and `Console`; the menu prints any product without type checks. |
| Pure Fabrication      | violation (boundary) | `ProductRepository.saveAll/loadAll` repeat the file handling also written in the other six repositories; it was reported to the Leader as a boundary finding. |
| Indirection           | correct | `ProductService` stands between the menu and `ProductRepository`. |
| Protected Variations  | correct | The CSV format is hidden behind `saveAll/loadAll`; the service does not know the file layout. |

### V-01 - Stock increase is not done by the expert (boundary)

| Field | Content |
|-------|---------|
| Pattern | Information Expert |
| Module | Products (Taller 1) - `Product`; used by Returns (R3) and A4 |
| Evidence | `Product` offers `decreaseStock(int)` but no operation to add units. `ProductService.restoreStock` (Leader, R3) and `AccessoryService.restoreStock` (Developer 2, A4) do `product.setStock(product.getStock() + quantity)` and `accessory.setStock(accessory.getStock() + quantity)`. |
| Explanation | The product owns its stock and already knows how to decrease it; the arithmetic of adding units was placed outside the expert, in two services. |
| Consequence | A new rule about stock (for example, a maximum stock) would have to be written in `Product` and in two services; the services can leave a product in an inconsistent state. |
| Proposed solution | Add `Product.increaseStock(int)` (Information Expert) and make both services call it. |
| Code of another member | Yes: `ProductService.restoreStock` (Leader) and `AccessoryService.restoreStock` (Developer 2). Reported to the Leader. |
