# SOLID Audit - Developer 2 (Dair Jose Quintero Ortiz)

Audited code: `main` at commit `6d196b3`, the same code that `develop`
had when Requirement 8 started (GRASP correction of Requirement 7 included).

## Scope

| Module (requirement)        | Classes and methods audited |
|-----------------------------|-----------------------------|
| People (Taller 1)           | `Person`, `Customer`, `Seller`, `PersonRepository`, `PersonService` |
| Accessories (R1, A4)        | `AccessoryRepository`, `AccessoryService` |
| Promotions (R2)             | `PromotionRepository`, `PromotionService` |
| Returns (R3, A4, A6, A7)    | `ReturnRepository`, `ReturnService` (including the monthly balance) |
| Warranties (R4, A2, A7)     | `WarrantyRepository`, `WarrantyService` |
| Exceptions (R6)             | use of validators and typed exceptions in services and repositories |
| GRASP correction (R7)       | `CsvFile`, and the changes in `AccessoryService`, `ReturnService` and `WarrantyService` |

Verdict legend: **violation**, **correct**, **not applicable**.

## Person module

| Principle | Verdict | Evidence |
|-----------|---------|----------|
| SRP | correct | `PersonService` only applies the person rules; `PersonRepository` only converts lines; file access is in `CsvFile`. |
| OCP | correct | Customers and sellers are kept in two files with a fixed layout each; no type switch exists. |
| LSP | correct | `Customer` and `Seller` can replace `Person`: both implement `getDescription()` and keep the common data. |
| ISP | not applicable | No interfaces. |
| DIP | correct | The repository is injected through the constructor of `PersonService`. |
