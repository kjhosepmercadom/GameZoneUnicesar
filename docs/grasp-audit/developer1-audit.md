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
