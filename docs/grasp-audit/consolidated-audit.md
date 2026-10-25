# Consolidated GRASP Audit

Consolidated by the Technical Lead from `leader-audit.md`,
`developer1-audit.md` and `developer2-audit.md`.

Audited code: `main` at commit `764d658` (same code as `develop` when
Requirement 7 started; the audit Pull Requests only add documents).

## Coverage

Every class of the system was audited exactly once, by the member who
developed it. Methods added to a class by another member were audited by
their author.

| Package       | Class                         | Audited by | Notes |
|---------------|-------------------------------|------------|-------|
| model         | `Person`, `Customer`, `Seller` | Developer 2 | |
| model         | `Product`, `VideoGame`, `Console` | Developer 1 | |
| model         | `Accessory`, `Controller`, `Cable`, `Memory` | Developer 1 | |
| model         | `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount` | Developer 1 | |
| model         | `Warranty`, `BasicWarranty`, `ExtendedWarranty` | Developer 1 | |
| model         | `Return`                      | Developer 1 | |
| model         | `Sale`                        | Leader | `canBeReturned()` audited by Developer 1 |
| persistence   | `ProductRepository`           | Developer 1 | |
| persistence   | `PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `WarrantyRepository`, `ReturnRepository` | Developer 2 | |
| persistence   | `SaleRepository`              | Leader | |
| service       | `ProductService`              | Developer 1 | `restoreStock()` audited by the Leader |
| service       | `PersonService`, `AccessoryService`, `PromotionService`, `WarrantyService`, `ReturnService` | Developer 2 | |
| service       | `SaleService`                 | Leader | use of validators and typed exceptions audited by Developer 2 |
| ui            | `ConsoleMenu`                 | Leader | |
| root          | `Main`                        | Leader | |
| exceptions    | `GameZoneException` and its 4 subclasses | Developer 1 | |
| validation    | `ProductValidator`, `PersonValidator`, `SaleValidator` | Developer 1 | |

Evidence check: every verdict of the three reports names a class and method
and, for violations, the exact fragment of code. No finding was returned to
its author.

## Team verdict per pattern

| Pattern               | Leader | Developer 1 | Developer 2 | Team verdict |
|-----------------------|--------|-------------|-------------|--------------|
| Information Expert    | violation | violation | violation | **violation** (V-02, V-03, V-04) |
| Creator               | correct | correct | correct | **correct** |
| Controller            | correct | correct | correct | **correct** |
| Low Coupling          | correct | correct | correct | **correct** |
| High Cohesion         | violation | correct | violation | **violation** (V-01, V-07) |
| Polymorphism          | correct | violation | violation | **violation** (V-05, V-06) |
| Pure Fabrication      | violation | violation | violation | **violation** (V-01, V-07) |
| Indirection           | correct | correct | correct | **correct** |
| Protected Variations  | violation | violation | correct | **violation** (V-01, V-05) |
