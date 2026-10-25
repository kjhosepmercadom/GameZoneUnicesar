# GRASP Refactor Log - Requirement 7

## Non-regression strategy

Every operation of the menu is verified with the same keyboard script,
`docs/regression/menu-operations-input.txt`, which covers:

| Menu | Operations and data |
|------|---------------------|
| 1 Products | register video game `VG010` (price typed as `189.900`) and console `CN010`; list; duplicated `VG001` (error); console with price `-5` (error) |
| 2 People | register customer `1001`; invalid email (error); duplicated customer `1001` (error); list customers and sellers |
| 4 Accessories | register controller `AC010`, cable `AC011`, memory `AC012`; list all; list by each type; compatible with `CN001` |
| 5 Promotions | register percentage `PR010`, category `PR011`, bulk `PR012` (expired); invalid dates (error); list all and active |
| 3 Sales | sale `CN001` + `VG001` + `AC001` with extended warranty; insufficient stock (error); sale without items (error); unknown customer (error); full history; by customer; by seller; detail of `S0001` and of a missing sale |
| 6 Warranties | warranty of `CN001` and of `VG001` in `S0001`; list all, active and expiring in 200 days |
| 7 Returns | return `CN001` + `AC001`; quantity above the units sold (error); product not in the sale (error); missing sale; list all, by customer, by sale; balance of month 13 (error) and of the current month |
| End | inventory and accessories listed again to check the restored stock |

How to run it (from the repository root, with the versioned data files):

```powershell
git checkout -- data; Remove-Item data\customers.csv, data\sales.csv, data\warranties.csv, data\returns.csv -ErrorAction SilentlyContinue
javac -encoding UTF-8 -d target\classes (Get-ChildItem -Recurse src -Filter *.java).FullName
Get-Content docs\regression\menu-operations-input.txt | java -cp target\classes com.gamezone.Main > regression-output.txt
```

The output and the files of `data/` are compared with the reference run.
Lines with the current date change from one day to another, so reference and
verification are executed on the same day.

## Reference run (before the first correction)

Executed on `develop` before the first commit of `feature/grasp-refactor`:

- 1376 lines of console output, no exception trace;
- expected key values: sale `S0001` total $2.739.780 (promotion "Black Friday
  30%" registered by the script), refund $2.564.850, net balance $174.930;
- every error case shows its differentiated message.

## Changes

| Class | Change | Violation | Pattern applied | Commit / PR | Responsible |
|-------|--------|-----------|-----------------|-------------|-------------|
| `Sale` | New `calculateDiscountRate()` | V-03 | Information Expert | `refactor: add calculateDiscountRate to Sale (V-03)` / PR "prepare the GRASP corrections" | Leader |
| `Sale` | New `findProduct(String)` and `countUnitsOf(String)` | V-04 | Information Expert | `refactor: add findProduct and countUnitsOf to Sale (V-04)` / PR "prepare the GRASP corrections" | Leader |
| `Product` | New `increaseStock(int)` | V-02 | Information Expert | `refactor: add increaseStock to Product (V-02)` / PR "apply developer 1 GRASP corrections" | Developer 1 |
| `Product`, `VideoGame`, `Console`, `Accessory` | New abstract `getCategory()` and its implementations | V-05 | Polymorphism | `refactor: declare the product category polymorphically (V-05)` / same PR | Developer 1 |
| `CategoryDiscount` | `belongsToTargetCategory` compares `product.getCategory()`; the `instanceof` chain was removed | V-05 | Polymorphism, Protected Variations | `refactor: use the product category in CategoryDiscount (V-05)` / same PR | Developer 1 |
| `Return` | `refundOf` uses `sale.calculateDiscountRate()`; `discountRate()` was removed | V-03 | Information Expert | `refactor: ask the sale for its discount rate in Return (V-03)` / same PR | Developer 1 |
| `CsvFile` | New package-private helper: `readLines`, `writeLines`, `clean` | V-07 | Pure Fabrication | `refactor: add CsvFile helper for the repositories (V-07)` / PR "apply developer 2 GRASP corrections" | Developer 2 |
| `PersonRepository` | Uses `CsvFile` for both files | V-07 | Pure Fabrication, High Cohesion | `refactor: use CsvFile in PersonRepository (V-07)` / same PR | Developer 2 |
| `AccessoryRepository`, `PromotionRepository` | Use `CsvFile` | V-07 | Pure Fabrication, High Cohesion | `refactor: use CsvFile in the accessory and promotion repositories (V-07)` / same PR | Developer 2 |
| `WarrantyRepository`, `ReturnRepository` | Use `CsvFile` | V-07 | Pure Fabrication, High Cohesion | `refactor: use CsvFile in the warranty and return repositories (V-07)` / same PR | Developer 2 |
| `AccessoryService` | `restoreStock` calls `accessory.increaseStock(quantity)` | V-02 | Information Expert | `refactor: use Product.increaseStock in AccessoryService (V-02)` / same PR | Developer 2 |
| `ReturnService` | Uses `sale.findProduct` and `sale.countUnitsOf`; `findInSale` and `countInSale` were removed | V-04 | Information Expert | `refactor: ask the sale for its products in ReturnService (V-04)` / same PR | Developer 2 |
| `WarrantyService` | `findWarrantyByProduct` keeps the warranty with the longest `getDurationInMonths()` | V-06 | Polymorphism | `refactor: choose the warranty with the longest coverage without type checks (V-06)` / same PR | Developer 2 |
| `ProductRepository` | Uses `CsvFile` | V-07 | Pure Fabrication, High Cohesion | `refactor: use CsvFile in ProductRepository (V-07)` / PR "use CsvFile in ProductRepository" | Developer 1 |
