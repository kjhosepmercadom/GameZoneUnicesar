# SOLID Audit - Technical Lead (Kevin Jhosep Mercado Moron)

Audited code: `main` at commit `6d196b3`, the same code that `develop`
had when Requirement 8 started (GRASP correction of Requirement 7 included).

## Scope

| Module (requirement)           | Classes and methods audited |
|--------------------------------|-----------------------------|
| Sales and startup (Taller 1)   | `Sale` (including `calculateDiscountRate`, `findProduct`, `countUnitsOf` added in R7), `SaleRepository` (on `CsvFile` since R7), `SaleService`, `Main` |
| Integrations (R1, R2, R4, A3)  | item resolution, promotion, warranties and inventory update in `SaleService.registerSale`; receipt of `Sale` |
| Returns (R3)                   | `ProductService.restoreStock` |
| User interface (R1-R7)         | `ConsoleMenu` and the classes created in R7: `ConsoleInput`, `ErrorPrinter`, `ProductMenu`, `PersonMenu`, `SaleMenu`, `AccessoryMenu`, `PromotionMenu`, `WarrantyMenu`, `ReturnMenu` |

Verdict legend: **violation**, **correct**, **not applicable**.
