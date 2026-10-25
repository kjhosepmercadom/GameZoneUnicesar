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
