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
