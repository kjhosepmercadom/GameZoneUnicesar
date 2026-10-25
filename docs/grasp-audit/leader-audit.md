# GRASP Audit - Technical Lead (Kevin Jhosep Mercado Moron)

Audited code: `main` at commit `764d658`, the same code that `develop`
had when Requirement 7 started.

## Scope

| Module (requirement)           | Classes and methods audited                                            |
|--------------------------------|------------------------------------------------------------------------|
| Sales, UI and startup (Taller 1) | `Sale`, `SaleRepository`, `SaleService`, `ConsoleMenu`, `Main`       |
| Accessories (R1)               | integration in `SaleService` (item resolution, inventory update) and the accessory submenu |
| Promotions (R2)                | discount in `SaleService.registerSale`, `Sale` (applied promotion and receipt), promotion submenu |
| Returns (R3)                   | `ProductService.restoreStock` and the returns submenu                  |
| Warranties (R4, A3)            | warranty assignment and extended warranty cost in `SaleService`, warranty submenu |
| Exceptions (R6)                | differentiated handling in `ConsoleMenu.showError` and `Main`          |

Verdict legend: **violation**, **correct**, **not applicable**.
