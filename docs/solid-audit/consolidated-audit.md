# Consolidated SOLID Audit

Consolidated by the Technical Lead from `leader-audit.md`,
`developer1-audit.md` and `developer2-audit.md`.

Audited code: `main` at commit `6d196b3` (same code as `develop` when
Requirement 8 started; the audit Pull Requests only add documents).

## Coverage

| Package       | Class | Audited by |
|---------------|-------|------------|
| model         | `Person`, `Customer`, `Seller` | Developer 2 |
| model         | `Product`, `VideoGame`, `Console`, `Accessory`, `Controller`, `Cable`, `Memory` | Developer 1 |
| model         | `Promotion` and its 3 subclasses, `Warranty` and its 2 subclasses, `Return` | Developer 1 |
| model         | `Sale` | Leader (`canBeReturned` by Developer 1) |
| persistence   | `ProductRepository` | Developer 1 |
| persistence   | `PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `WarrantyRepository`, `ReturnRepository`, `CsvFile` | Developer 2 |
| persistence   | `SaleRepository` | Leader |
| service       | `ProductService` | Developer 1 (`restoreStock` by the Leader) |
| service       | `PersonService`, `AccessoryService`, `PromotionService`, `WarrantyService`, `ReturnService` | Developer 2 |
| service       | `SaleService` | Leader |
| ui            | `ConsoleMenu`, `ConsoleInput`, `ErrorPrinter` and the seven submenus | Leader |
| root          | `Main` | Leader |
| exceptions    | `GameZoneException` and its 4 subclasses | Developer 1 |
| validation    | `ProductValidator`, `PersonValidator`, `SaleValidator` | Developer 1 |

Every class was audited once by its developer; the classes created in R7 were
audited by the member who created them, as recorded in
`docs/grasp-refactor-log.md`.

## Team verdict per principle

| Principle | Leader | Developer 1 | Developer 2 | Team verdict |
|-----------|--------|-------------|-------------|--------------|
| SRP | correct | correct | violation | **violation** (V-03) |
| OCP | violation | violation | violation | **violation** (V-01, V-02) |
| LSP | correct | correct | correct | **correct** |
| ISP | correct | correct | correct | **correct** |
| DIP | violation | correct | violation | **violation** (V-01) |
