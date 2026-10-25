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
