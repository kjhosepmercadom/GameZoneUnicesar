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

## `Sale` and `SaleRepository`

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | correct | `Sale.calculateTotal()`, `calculateFinalTotal()` and `generateReceipt()` use only data of the sale. (The use of sale data by `Return` and `ReturnService` was reported by the developers: consolidated V-03 and V-04.) |
| Creator               | correct | `SaleRepository.loadAll` creates the `Sale` objects from the stored lines, receiving the customers, sellers and items it needs. |
| Controller            | not applicable | Model and repository do not receive system events. |
| Low Coupling          | correct | `SaleRepository` depends only on the model; references are resolved from the lists it receives. |
| High Cohesion         | correct | `Sale` keeps the data and totals of one sale. |
| Polymorphism          | correct | The receipt prints any item through `Product` without type checks. |
| Pure Fabrication      | violation (boundary) | `SaleRepository.saveAll/loadAll` repeat the file handling of the other repositories (consolidated V-07). |
| Indirection           | not applicable | - |
| Protected Variations  | correct | Old sale lines (without promotion or warranty columns) are still loaded: the format change is hidden in `fromLine`. |
