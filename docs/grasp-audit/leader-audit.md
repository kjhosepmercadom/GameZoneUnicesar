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

## `SaleService` (with the accessory, promotion and warranty integrations)

| Pattern               | Verdict | Evidence |
|-----------------------|---------|----------|
| Information Expert    | correct | The discount is asked to the promotion (`promotion.calculateDiscount(sale)`), the warranty cost to the warranty (`getAdditionalCost()`) and the stock check to the item (`item.hasEnoughStock`). |
| Creator               | correct | `registerSale` creates the `Sale`: the service aggregates the sales and has every initializing datum (customer, seller, items, date). |
| Controller            | correct | `registerSale` is the controller of the "register sale" use case; it follows the eight steps of adjustment A3 delegating each one. |
| Low Coupling          | correct | It depends on six collaborators, all of them services or the sale repository; it never uses the repositories of other modules. This is the expected coupling of the controller of the main use case. |
| High Cohesion         | correct | After A3 each step is a private method (`resolveItems`, `validateStock`, `applyBestPromotion`, `assignWarranties`, `updateInventory`). |
| Polymorphism          | correct | `item instanceof Console` in `assignWarranties` applies the business rule "only consoles get a warranty", which Requirement 4 defines by product type. `instanceof Accessory` in `updateInventory` selects which service owns the item; it is not a behaviour of the item, so Polymorphism over `Product` does not apply (moving the stock update into the model would make the model call services). |
| Pure Fabrication      | correct | The validators are used for the rules; no domain class does service work. |
| Indirection           | correct | The service reaches the inventories through `ProductService` and `AccessoryService`, never through their repositories. |
| Protected Variations  | correct | The promotion rules are hidden behind `PromotionService.findBestPromotionFor`; the warranty types behind `WarrantyService`. |
