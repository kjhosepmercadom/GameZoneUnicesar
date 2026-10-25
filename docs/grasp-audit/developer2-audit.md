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
