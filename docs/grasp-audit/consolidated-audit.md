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

## Violations

| ID   | Pattern | Evidence | Explanation | Consequence | Agreed solution | Origin | Responsible |
|------|---------|----------|-------------|-------------|-----------------|--------|-------------|
| V-01 | High Cohesion (Pure Fabrication, Protected Variations) | `ConsoleMenu`: 811 lines, seven submenus, input readers and `showError` | One class with the user interface of every module | Every requirement modifies the same class | `ConsoleInput`, `ErrorPrinter` and one submenu class per module; `ConsoleMenu` delegates | leader-audit V-01 | Leader |
| V-02 | Information Expert | `ProductService.restoreStock` and `AccessoryService.restoreStock`: `setStock(getStock() + quantity)` | The product owns its stock but only knows how to decrease it | Stock rules spread over the model and two services | `Product.increaseStock(int)`; both services call it | developer1-audit V-01, developer2-audit V-04, leader-audit V-02 | Developer 1 (`Product`), Leader (`ProductService`), Developer 2 (`AccessoryService`) |
| V-03 | Information Expert | `Return.discountRate()` reads `sale.calculateTotal()` and `sale.getDiscountAmount()` | The discount rate is information of the sale | Changes in the discount logic would land in `Return` | `Sale.calculateDiscountRate()`; `Return` uses it | developer1-audit V-03 | Leader (`Sale`), Developer 1 (`Return`) |
| V-04 | Information Expert | `ReturnService.findInSale` and `countInSale` iterate `sale.getProducts()` | The sale is the expert of its items | Duplicated loops, dependency on the internal list of the sale | `Sale.findProduct(id)` and `Sale.countUnitsOf(id)`; `ReturnService` uses them | developer2-audit V-02 | Leader (`Sale`), Developer 2 (`ReturnService`) |
| V-05 | Polymorphism (Protected Variations) | `CategoryDiscount.belongsToTargetCategory`: chain of `instanceof` by category | The category varies with the product type | Each new product type modifies the promotion | `Product.getCategory()` abstract, implemented by `VideoGame`, `Console` and `Accessory` | developer1-audit V-02 | Developer 1 |
| V-06 | Polymorphism | `WarrantyService.findWarrantyByProduct`: `instanceof ExtendedWarranty` | The preference depends on the coverage, which each warranty knows | A new warranty type would not be preferred | Keep the warranty with the longest `getDurationInMonths()` | developer2-audit V-01 | Developer 2 |
| V-07 | Pure Fabrication (High Cohesion) | File handling (`Files.exists`, `readAllLines`, `createDirectories`, `write`, `IOException` wrapping, `clean`) copied in the seven repositories | A technical responsibility copied instead of being assigned to one class | Seven places to change for any file handling change | Package-private helper `CsvFile`; every repository uses it | developer2-audit V-03, developer1-audit (Pure Fabrication), leader-audit V-03 | Developer 2 (`CsvFile` and five repositories), Developer 1 (`ProductRepository`), Leader (`SaleRepository`) |

## Boundary findings

- **V-02** was reported from three sides. Developer 1 adds `Product.increaseStock`
  first; then Developer 2 and the Leader change their `restoreStock` methods.
- **V-03** and **V-04** need new methods in `Sale` (Leader). The Leader adds
  them in the first Pull Request of Phase 2, and the developers use them in
  their own Pull Requests.
- **V-07** is one finding for the seven repositories. Developer 2 creates
  `CsvFile` because that member owns five of them; Developer 1 and the Leader migrate
  `ProductRepository` and `SaleRepository` after `CsvFile` is merged.

## Out of scope findings

These are functional defects. Fixing them would change what the user sees, so
they are not corrected in this requirement.

| ID    | Evidence | Defect |
|-------|----------|--------|
| OS-01 | `WarrantyService.cancelWarranties(productId, saleId)` removes every warranty of the product in the sale | If a sale has two units of the same console and only one is returned, the warranties of both units are cancelled and refunded. |
| OS-02 | `SaleService.resolveItems` looks for an identifier first among products and then among accessories; no service checks identifiers across both inventories | An accessory registered with the identifier of an existing product can never be sold. |
| OS-03 | `WarrantyService.nextWarrantyId` uses the biggest current number plus one | After cancelling the newest warranty, its identifier is reused by the next warranty. |
