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

## Sales module

| Principle | Verdict | Evidence |
|-----------|---------|----------|
| SRP | correct | `Sale` keeps the data and totals of a sale; `generateReceipt` is the presentation of those same data required by Requirement 2 inside `Sale`. `SaleRepository` only converts lines. `SaleService` coordinates one use case per public method. |
| OCP | **violation** | `SaleService.resolveItems` tries `productService.findById` and then `accessoryService.findById`; `updateInventory` chooses the service with `instanceof Accessory` (see V-01). |
| LSP | correct | `Sale` works with any `Product` subtype; the receipt only uses the methods of `Product`. |
| ISP | not applicable | No interfaces in this module. |
| DIP | **violation** | `SaleService` depends on the concrete `ProductService` and `AccessoryService` for the inventory operations (see V-01). |

### V-01 - The sale depends on concrete inventories and their types (boundary)

| Field | Content |
|-------|---------|
| Principle | OCP and DIP |
| Module | Accessories (R1) and A3 - `SaleService.resolveItems` and `updateInventory` |
| Evidence | `Product item = productService.findById(itemId); if (item == null) { item = accessoryService.findById(itemId); }` and `if (findItem(items, entry.getKey()) instanceof Accessory) { accessoryService.updateStock(...); } else { productService.updateStock(...); }` |
| Explanation | The high level policy (register a sale) depends on two concrete services and on the item types; it is not closed for a new kind of item. |
| Consequence | Adding another inventory (for example, digital games) modifies `SaleService` and also `ReturnService`, which has the same code (Developer 2 V-03). In R7 this type check was accepted for Polymorphism because it is not a behaviour of the item; the SOLID review shows the cost of the dependency itself. |
| Proposed solution | An `InventoryService` interface (`findById`, `updateStock`, `restoreStock`) implemented by `ProductService` and `AccessoryService`; `SaleService` and `ReturnService` iterate a list of inventories. The constructors keep their signatures. |
| Code of another member | Yes: `ProductService` (Developer 1), `AccessoryService` and `ReturnService` (Developer 2). |

`ProductService.restoreStock` (Leader since R3): **correct** for the five
principles; since R7 it delegates to `Product.increaseStock`.

## User interface and `Main`

| Principle | Verdict | Evidence |
|-----------|---------|----------|
| SRP | correct | Since R7 each submenu has one module; `ConsoleInput` only reads input; `ErrorPrinter` only prints error messages; `ConsoleMenu` only shows the main menu. `Main` only builds and connects the objects. |
| OCP | correct | A new module is a new submenu class and one option in `ConsoleMenu.start`; the existing submenus do not change. |
| LSP | not applicable | The user interface classes have no subclasses. |
| ISP | correct | Each submenu receives only the services it uses (for example, `PromotionMenu` only receives `PromotionService`). |
| DIP | correct | The submenus depend on the services, which are injected through the constructors; only `Main` knows the concrete repositories. |

## Summary

| Principle | Verdict of this report |
|-----------|------------------------|
| SRP | correct |
| OCP | violation (V-01) |
| LSP | correct |
| ISP | correct |
| DIP | violation (V-01) |
