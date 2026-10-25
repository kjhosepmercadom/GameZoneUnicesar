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

## Accessory hierarchy

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | correct | `Accessory.isCompatibleWith`, `addCompatibleConsole` and `removeCompatibleConsole` work with the `compatibleConsoleIds` list of the accessory. |
| Creator               | not applicable | The accessory classes do not create other objects (they only copy the identifier list they receive). |
| Controller            | not applicable | Model classes do not receive system events. |
| Low Coupling          | correct | The hierarchy depends only on `Product`; compatibility is kept by console identifier, so `Accessory` does not depend on `Console`. |
| High Cohesion         | correct | Each subclass only adds its own attributes and description. |
| Polymorphism          | correct | `getAccessoryType()` is abstract and implemented by each subclass, so `AccessoryService.listAccessoriesByType` filters without `instanceof`; `getDescription()` is extended with `super.getDescription()`. |
| Pure Fabrication      | not applicable | No responsibility outside the domain is placed in these classes. |
| Indirection           | not applicable | There are no two components that need a mediator inside the hierarchy. |
| Protected Variations  | correct | A new accessory type only needs a new subclass of `Accessory`; the services work with `Accessory`. |

## Promotion hierarchy

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | correct | `Promotion.isActive(LocalDate)` uses its own `startDate` and `endDate`. |
| Creator               | not applicable | The promotions do not create other objects. |
| Controller            | not applicable | Model classes do not receive system events. |
| Low Coupling          | correct | The promotions depend on `Sale` and `Product` only to read the items; `Sale` does not depend on `Promotion`. |
| High Cohesion         | correct | Each class contains only its calculation rule and its description. |
| Polymorphism          | **violation** | `CategoryDiscount.belongsToTargetCategory` (see V-02). |
| Pure Fabrication      | not applicable | No artificial class is needed in the hierarchy. |
| Indirection           | not applicable | No mediator is needed. |
| Protected Variations  | violation (same point) | A new product category forces a change in `CategoryDiscount` (see V-02). |

### V-02 - Category decided with a chain of type checks

| Field | Content |
|-------|---------|
| Pattern | Polymorphism (also Protected Variations) |
| Module | Promotions (R2) and adjustment A1 - `CategoryDiscount` |
| Evidence | `CategoryDiscount.belongsToTargetCategory(Product product)`: `if ("VIDEOGAME"...) return product instanceof VideoGame; if ("CONSOLE"...) return product instanceof Console; if ("ACCESSORY"...) return product instanceof Accessory; return false;` |
| Explanation | The behaviour "which category does this product belong to" varies with the product type, but it is resolved with conditionals on the type instead of letting each type answer. |
| Consequence | Adjustment A1 already had to modify this method to add accessories; every new product type will modify it again and the promotion depends on every concrete product class. |
| Proposed solution | Declare `getCategory()` abstract in `Product` and implement it in `VideoGame`, `Console` and `Accessory`; `CategoryDiscount` compares `product.getCategory()` with its target category (Polymorphism). |
| Code of another member | No. |

## Return model and `Sale.canBeReturned`

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | **violation** | `Return.discountRate()` (see V-03). `Sale.canBeReturned()` is correct: the sale owns its date. |
| Creator               | not applicable | `Return` only copies the list it receives. |
| Controller            | not applicable | Model classes. |
| Low Coupling          | correct | `Return` depends only on `Sale` and `Product`. |
| High Cohesion         | correct | `Return` holds the data of a return, its refund and its receipt. |
| Polymorphism          | not applicable | There are no type variants in this module. |
| Pure Fabrication      | not applicable | - |
| Indirection           | not applicable | - |
| Protected Variations  | correct | The refund rule is encapsulated in `calculateRefundAmount()`; adjustments A5 and A7 changed it without touching the callers. |

### V-03 - The return calculates information that belongs to the sale (boundary)

| Field | Content |
|-------|---------|
| Pattern | Information Expert |
| Module | Returns (R3, A5) - `Return`; uses `Sale` (Leader) |
| Evidence | `Return.discountRate()`: `double subtotal = sale.calculateTotal(); if (subtotal <= 0) return 0; return sale.getDiscountAmount() / subtotal;` |
| Explanation | The discount rate is derived only from data of the sale (subtotal and discount); the return reads both values to calculate it. |
| Consequence | If the way the discount is applied changes (for example, a discount over part of the items), the change must be made in `Return`, far from the class that owns the discount. |
| Proposed solution | `Sale.calculateDiscountRate()` (Leader) and `Return.refundOf` uses it (Developer 1). |
| Code of another member | Yes: a new method in `Sale` (Leader). Reported to the Leader. |

## Warranty hierarchy

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | correct | `Warranty.isActive` uses its own dates; `ExtendedWarranty.getAdditionalCost` uses the price of its product. |
| Creator               | not applicable | - |
| Controller            | not applicable | - |
| Low Coupling          | correct | Depends on `Product` and `Sale` only. |
| High Cohesion         | correct | Data, validity and certificate of one warranty. |
| Polymorphism          | correct | `getDurationInMonths`, `getWarrantyType` and `getAdditionalCost` are abstract; the end date is calculated once in the constructor with the duration of the subclass. |
| Pure Fabrication      | not applicable | - |
| Indirection           | not applicable | - |
| Protected Variations  | correct | A new warranty type is a new subclass; the calculation of dates does not change. |
