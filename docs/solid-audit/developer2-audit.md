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

## Accessory and promotion persistence and services

| Principle | Verdict | Evidence |
|-----------|---------|----------|
| SRP | correct | Each service and repository works on one module. |
| OCP | **violation** | `AccessoryRepository.fromLine` (`switch (type) { case "CONTROLLER": ... case "CABLE": ... case "MEMORY": ... }`) and `toLine` (`instanceof Controller / Cable / Memory`); `PromotionRepository.fromLine` (`switch` on `PERCENTAGE`, `CATEGORY`, `BULK`) and `toLine` (`instanceof` chain). Same violation as Developer 1 V-01 (see V-02 below). |
| LSP | correct | The services work with `Accessory` and `Promotion` without assumptions about the subtype. |
| ISP | correct | `AccessoryService` and `PromotionService` expose only the operations their submenus use. |
| DIP | correct | Repositories are injected through the constructors. |

## Return and warranty persistence and services

| Principle | Verdict | Evidence |
|-----------|---------|----------|
| SRP | **violation** | `ReturnService` (see V-01). |
| OCP | **violation** | `ReturnService.registerReturn`: `if (sale.findProduct(id) instanceof Accessory) { accessoryService.restoreStock(...); } else { productService.restoreStock(...); }`; `WarrantyRepository.saveAll` (`instanceof ExtendedWarranty ? "EXTENDED" : "BASIC"`) and `create` (`if ("EXTENDED".equals(fields[0]))`). |
| LSP | correct | `WarrantyService` works with any `Warranty` since R7 (coverage compared by duration). |
| ISP | correct | - |
| DIP | **violation** | `ReturnService` depends on the concrete `ProductService` and `AccessoryService` to restore stock (see V-03). |

### V-01 - The return service also builds the monthly report

| Field | Content |
|-------|---------|
| Principle | SRP |
| Module | Returns (R3, A6) - `ReturnService` |
| Evidence | Besides `registerReturn` and the return queries, the class contains the report arithmetic: `calculateMonthlySales`, `calculateMonthlyReturns`, `generateMonthlyBalance` and the helper `isInMonth`. |
| Explanation | The class has two reasons to change: the rules of a return, and the way the monthly report is calculated (A6 already changed the report alone). |
| Consequence | A new report figure (for example, warranties sold in the month) would modify the class that registers returns. |
| Proposed solution | Move the arithmetic to a `MonthlyBalanceCalculator` in the service package; `ReturnService` keeps its public methods and delegates (the menu does not change). |
| Code of another member | No. |

### V-02 - Type switches in the repositories (boundary)

| Field | Content |
|-------|---------|
| Principle | OCP |
| Module | `AccessoryRepository`, `PromotionRepository`, `WarrantyRepository` |
| Evidence | The switches and `instanceof` chains listed above. |
| Explanation | Supporting a new subtype requires modifying the repository. |
| Consequence | Each new accessory, promotion or warranty type modifies the code that loads all the existing ones. |
| Proposed solution | The same mapper abstraction proposed by Developer 1: one mapper per subtype. |
| Code of another member | Yes: same finding as Developer 1 V-01 (`ProductRepository`). |

### V-03 - Stock restoration depends on concrete services (boundary)

| Field | Content |
|-------|---------|
| Principle | DIP (also OCP) |
| Module | Returns (R3, A4) - `ReturnService.registerReturn` |
| Evidence | Fields `productService` and `accessoryService` used only to restore stock, chosen with `instanceof Accessory`. |
| Explanation | The high level rule (restore the stock of the returned items) depends on two concrete low level services and on the item types. |
| Consequence | A new kind of item modifies `ReturnService`; the same code exists in `SaleService` (Leader). |
| Proposed solution | An inventory abstraction implemented by `ProductService` and `AccessoryService`; `ReturnService` asks the inventory that owns the item. |
| Code of another member | Yes: `SaleService` (Leader) and `ProductService` (Developer 1). Reported to the Leader. |

## `CsvFile`, exceptions and validators

| Principle | Verdict | Evidence |
|-----------|---------|----------|
| SRP | correct | `CsvFile` only reads and writes one file. The validators only validate. |
| OCP | correct | The repositories use `CsvFile` without knowing how the file is written. |
| LSP | not applicable | `CsvFile` has no subclasses. |
| ISP | correct | `CsvFile` exposes three operations, all used by the repositories. |
| DIP | correct | The repositories depend on `CsvFile`, a package-private stable helper; the services never see it. |

## Summary

| Principle | Verdict of this report |
|-----------|------------------------|
| SRP | violation (V-01) |
| OCP | violation (V-02, V-03) |
| LSP | correct |
| ISP | correct |
| DIP | violation (V-03) |
