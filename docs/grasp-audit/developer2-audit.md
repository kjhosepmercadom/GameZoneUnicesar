# GRASP Audit - Developer 2 (Dair Jose Quintero Ortiz)

Audited code: `main` at commit `764d658`, the same code that `develop`
had when Requirement 7 started.

## Scope

| Module (requirement)        | Classes and methods audited                                              |
|-----------------------------|--------------------------------------------------------------------------|
| People (Taller 1)           | `Person`, `Customer`, `Seller`, `PersonRepository`, `PersonService`      |
| Accessories (R1, A4)        | `AccessoryRepository`, `AccessoryService` (including `restoreStock`)     |
| Promotions (R2)             | `PromotionRepository`, `PromotionService` (including `findBestPromotionFor`) |
| Returns (R3, A4, A6, A7)    | `ReturnRepository`, `ReturnService` (including the monthly balance)      |
| Warranties (R4, A2, A7)     | `WarrantyRepository`, `WarrantyService`                                  |
| Exceptions and validation (R6) | use of validators and typed exceptions in every service and repository |

Verdict legend: **violation**, **correct**, **not applicable**.

## Person module

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | correct | `Customer.getDescription()` and `Seller.getDescription()` describe their own data; `PersonService.findCustomerById` searches the list the service owns. |
| Creator               | correct | `PersonService.registerCustomer` creates the `Customer`: it aggregates the customer list and has the initializing data. |
| Controller            | correct | `PersonService` receives the person operations from the menu. |
| Low Coupling          | correct | `PersonService` depends on `PersonRepository`, `PersonValidator` and the model only. |
| High Cohesion         | correct | Model, persistence and rules are separated in three classes. |
| Polymorphism          | correct | `Person.getDescription()` is abstract; customers and sellers are printed without type checks. |
| Pure Fabrication      | violation (boundary) | `PersonRepository.writeLines/readRecords` repeat the file handling of the other repositories (see V-03). |
| Indirection           | correct | `PersonService` isolates the menu from the CSV files. |
| Protected Variations  | correct | The two files (customers and sellers) are hidden behind the repository methods. |

## Accessory and promotion persistence and services

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | **violation** | `AccessoryService.restoreStock`: `accessory.setStock(accessory.getStock() + quantity)` (see V-04). `PromotionService.findBestPromotionFor` is correct: it asks each promotion for its own discount. |
| Creator               | correct | `AccessoryService.registerController/Cable/Memory` and `PromotionService.register*` create the objects of the lists they aggregate. |
| Controller            | correct | Both services receive the operations of their submenus. |
| Low Coupling          | correct | `AccessoryService` does not depend on `ProductService`; compatibility is stored by console identifier. |
| High Cohesion         | correct | Each service is limited to its module. |
| Polymorphism          | correct | `listAccessoriesByType` uses `getAccessoryType()`; `findBestPromotionFor` calls `calculateDiscount` polymorphically. In the repositories, `toLine` uses `instanceof` to choose the CSV columns; this is accepted for GRASP because the model must not know the file format (Pure Fabrication), and the type checks are confined to the persistence class. |
| Pure Fabrication      | violation (boundary) | File handling repeated in `AccessoryRepository` and `PromotionRepository` (see V-03). |
| Indirection           | correct | The services stand between the menu and the repositories. |
| Protected Variations  | correct | The CSV layout is hidden from the services. |

## Return and warranty persistence and services

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | **violation** | `ReturnService.findInSale` and `ReturnService.countInSale` (see V-02). `countReturned` is correct: the returns belong to the service. |
| Creator               | correct | `ReturnService.registerReturn` creates the `Return`; `WarrantyService.assignBasicWarranty/assignExtendedWarranty` create the warranties of the list they aggregate. |
| Controller            | correct | `ReturnService` coordinates validation, refund, warranty cancellation, stock restoration and persistence of a return. |
| Low Coupling          | correct | After adjustment A2, `WarrantyRepository` does not depend on any service; references are resolved from the sales passed to `loadAll`. |
| High Cohesion         | correct | Each service contains the rules of its module. |
| Polymorphism          | **violation** | `WarrantyService.findWarrantyByProduct` uses `instanceof ExtendedWarranty` (see V-01). |
| Pure Fabrication      | violation (boundary) | File handling repeated in `ReturnRepository` and `WarrantyRepository` (see V-03). |
| Indirection           | correct | `ReturnService` uses `ProductService`, `AccessoryService` and `WarrantyService` instead of touching their repositories. |
| Protected Variations  | correct | The thirty day rule is encapsulated in `Sale.canBeReturned()`; the refund rule in `Return.calculateRefundAmount()`. |

### V-01 - Warranty preference decided with a type check

| Field | Content |
|-------|---------|
| Pattern | Polymorphism |
| Module | Warranties (R4) - `WarrantyService.findWarrantyByProduct` |
| Evidence | `if (warranty instanceof ExtendedWarranty) { return warranty; } if (found == null) { found = warranty; }` |
| Explanation | The service prefers the extended warranty by asking for the concrete class. What it really needs is "the warranty with the widest coverage", which each warranty already knows through `getDurationInMonths()`. |
| Consequence | A new warranty type (for example, a 24 month premium warranty) would not be preferred unless this method is modified. |
| Proposed solution | Keep the warranty with the biggest `getDurationInMonths()`; it gives the same result for basic and extended warranties (Polymorphism). |
| Code of another member | No. |

### V-02 - The return service does the work of the sale (boundary)

| Field | Content |
|-------|---------|
| Pattern | Information Expert |
| Module | Returns (R3) - `ReturnService`; uses `Sale` (Leader) |
| Evidence | `private Product findInSale(Sale sale, String productId)` and `private int countInSale(Sale sale, String productId)` iterate `sale.getProducts()` to find a product and to count its units. |
| Explanation | The sale owns its list of products, so it is the expert to find a product of the sale and to count its units. |
| Consequence | Any other client that needs the same information (warranties, reports) has to copy the loop; the service depends on the internal representation of the sale (one entry per unit). |
| Proposed solution | `Sale.findProduct(productId)` and `Sale.countUnitsOf(productId)` (Leader); `ReturnService` uses them and drops its helpers (Developer 2). |
| Code of another member | Yes: new methods in `Sale` (Leader). Reported to the Leader. |

## Repositories

### V-03 - File handling duplicated in every repository (boundary)

| Field | Content |
|-------|---------|
| Pattern | Pure Fabrication (also High Cohesion) |
| Module | Persistence of every module (Taller 1, R1 to R4) |
| Evidence | The same blocks appear in `PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `WarrantyRepository` and `ReturnRepository` (and in `ProductRepository` and `SaleRepository`): `if (!Files.exists(filePath)) { return ...; }`, `Files.readAllLines(filePath, StandardCharsets.UTF_8)`, `Files.createDirectories(filePath.getParent())`, `Files.write(filePath, lines, StandardCharsets.UTF_8)`, the `catch (IOException e)` that throws `PersistenceException`, and the private method `clean(String)`. |
| Explanation | Reading and writing a CSV file is a technical responsibility that does not belong to any domain repository; it was copied seven times instead of being assigned to one artificial class. |
| Consequence | A change in the file handling (encoding, backup before writing, a different error message) must be repeated in seven classes and the copies can diverge. |
| Proposed solution | A pure fabrication `CsvFile` in the persistence package with `readLines`, `writeLines` and `clean`; every repository uses it. Developer 2 creates it and migrates five repositories; `ProductRepository` (Developer 1) and `SaleRepository` (Leader) are migrated by their owners. |
| Code of another member | Yes: `ProductRepository` (Developer 1) and `SaleRepository` (Leader). Reported to the Leader. |

## Use of exceptions and validators in services and repositories

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | **violation** (same point as V-04) | `AccessoryService.restoreStock` updates the stock outside the accessory. |
| Creator               | correct | Each service creates its own exceptions with the data of the failed operation. |
| Controller            | not applicable | The exception handling is not a system event. |
| Low Coupling          | correct | Services depend on the validators, not the other way around. |
| High Cohesion         | correct | Validation is delegated to `ProductValidator`, `PersonValidator` and `SaleValidator`. |
| Polymorphism          | correct | Services throw the specific subclass; the menu reacts to its type. |
| Pure Fabrication      | correct | The validators are the pure fabrication for validation rules. |
| Indirection           | correct | Repositories wrap `IOException` in `PersistenceException`, so the upper layers never handle `java.io`. |
| Protected Variations  | correct | A change of storage would change only the cause wrapped by `PersistenceException`. |

### V-04 - Stock restoration of accessories outside the expert (boundary)

| Field | Content |
|-------|---------|
| Pattern | Information Expert |
| Module | Accessories (A4) - `AccessoryService.restoreStock`; needs `Product` (Developer 1) |
| Evidence | `accessory.setStock(accessory.getStock() + quantity);` |
| Explanation | The accessory (a `Product`) owns its stock; the service adds units directly through the setter. |
| Consequence | Same as Developer 1 V-01: the stock rules are spread between the model and the services. |
| Proposed solution | Use `Product.increaseStock(int)` once Developer 1 adds it. |
| Code of another member | Yes: `Product` (Developer 1). Reported to the Leader as the same boundary finding of Developer 1 V-01. |

## Summary

| Pattern               | Verdict of this report |
|-----------------------|------------------------|
| Information Expert    | violation (V-02, V-04) |
| Creator               | correct |
| Controller            | correct |
| Low Coupling          | correct |
| High Cohesion         | violation (V-03, same point as Pure Fabrication) |
| Polymorphism          | violation (V-01) |
| Pure Fabrication      | violation (V-03) |
| Indirection           | correct |
| Protected Variations  | correct |
