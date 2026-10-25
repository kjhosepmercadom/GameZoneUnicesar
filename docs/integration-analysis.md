# Integration Analysis - Requirement 5

The four extensions (accessories, promotions, warranties and returns) were
integrated in the order defined by the requirement. Each phase started only
after every branch of the previous phase was merged into `develop` and deleted.

| Phase | Module       | Module branch              | Adjustments |
|-------|--------------|----------------------------|-------------|
| 1     | Accessories  | `feature/accessory-module` | -           |
| 2     | Promotions   | `feature/promotion-module` | A1          |
| 3     | Warranties   | `feature/warranty-module`  | A2, A3      |
| 4     | Returns      | `feature/return-module`    | A4 - A7     |
| 5     | Closing      | -                          | A8, A9      |

## A1 - Category discount for accessories

- **Branch:** `feature/accessory-category-discount` - Developer 1.
- **Cause:** Requirement 2 limits `CategoryDiscount` to `VIDEOGAME` and
  `CONSOLE`; once accessories are sold, the store needs promotions on them.
- **Solution:** `CategoryDiscount` accepts `ACCESSORY` and recognizes every
  `Accessory` instance; `PromotionService.registerCategoryDiscount` validates
  the three allowed categories; the menu offers the accessory option; and
  `data/promotions.csv` includes an active accessory promotion (`PR004`).

## A2 - Circular dependency in the warranty module

- **Branch:** `fix/warranty-circular-dependency` - Developer 2.
- **Cause:** with Requirement 4, `SaleService -> WarrantyService ->
  WarrantyRepository -> SaleService`: the repository needed the sale service to
  resolve sales while loading. Constructor injection was impossible, so `Main`
  used a setter (`SaleService.setWarrantyService`) as a temporary workaround,
  and the persistence layer depended on the service layer.
- **Solution:** `WarrantyRepository` persists and loads only the identifiers
  of the sale and product and resolves them from the sales passed to
  `loadAll(List<Sale>)`, with no dependency on services.
  `WarrantyService.loadWarranties(List<Sale>)` loads the data once the sales
  exist; `SaleService` receives `WarrantyService` through its constructor and
  calls it after loading its sales. The setter was removed and `Main` builds
  `WarrantyService` before `SaleService`. The resolution uses the sales already
  loaded by `SaleService` instead of a second `SaleRepository` read, so every
  warranty points to the same `Sale` objects used by the rest of the system.

## A3 - Unified sale registration flow

- **Branch:** `refactor/unified-sale-registration` - Technical Lead.
- **Cause:** Requirements 1, 2 and 4 changed `registerSale` independently; the
  order of the operations (stock, promotion, warranties) was implicit.
- **Solution:** `registerSale` follows eight explicit steps: validate items,
  resolve products/accessories and validate stock, create the sale, apply the
  best promotion over the subtotal, assign basic and extended warranties,
  compute the final total (`subtotal - discount + extended warranties`),
  update the inventory by item type, and persist. The receipt shows the
  extended warranty cost, and the sales menu was split into item selection and
  warranty prompts.

## A4 - Accessory returns

- **Branch:** `fix/return-accessory-stock` - Developer 2.
- **Cause:** returns restored stock only through `ProductService.restoreStock`,
  which does not manage accessories, and loading a return with an accessory
  failed because accessories were not in the product list.
- **Solution:** `AccessoryService.restoreStock` was added; `ReturnService`
  receives `AccessoryService` and delegates by item type; `ReturnRepository`
  resolves returned items from the items of the original sale.

## A5 - Refund of discounted sales

- **Branch:** `fix/return-discounted-refund` - Developer 1.
- **Cause:** `Return.calculateRefundAmount` added list prices, refunding more
  than what the customer paid when the sale had a promotion.
- **Solution:** each item is refunded with `price x (1 - discount / subtotal)`.
  The return receipt shows list price, proportional discount and refund of
  each item.

## A6 - Monthly balance report

- **Branch:** `fix/monthly-balance-report` - Developer 2.
- **Cause:** the report must show sales, returns and net balance, but
  `generateMonthlyBalance` returned only the net balance and used the subtotal
  of the sales instead of their final total.
- **Solution:** `calculateMonthlySales` (final total of each sale) and
  `calculateMonthlyReturns` were added; `generateMonthlyBalance` keeps its
  signature and returns their difference; the menu shows the three values.

## A7 - Warranty cancellation on console returns

- **Branch:** `feature/return-warranty-cancellation` - Developer 2.
- **Cause:** a returned console kept an active warranty.
- **Solution:** `WarrantyService.cancelWarranties(productId, saleId)` removes
  the warranties of the product in the sale and returns the refundable cost
  (zero for basic, the additional cost for extended). `ReturnService` calls it
  for every returned console and `Return` adds that value to the refund and to
  the receipt; the value is persisted in `data/returns.csv`.

## Verification

After every phase the project was compiled and the following scenario was
executed from the console menu with the preloaded data:

1. Register customer `1001` and a sale with `CN001`, `VG001` and `AC001`,
   requesting the extended warranty for the console.
2. Expected receipt: subtotal $3.499.700, best promotion "Lleva 3 o mas y
   ahorra 15%" (-$524.955), extended warranty +$289.990, total $3.264.735.
3. Active warranties: basic `W0001` (6 months) and extended `W0002` (12 months).
4. Return `CN001` and `AC001` from `S0001`: refund $2.464.915 + $297.415 +
   $289.990 (warranty) = $3.052.320; stock of `CN001` back to 4 and `AC001` to
   12; no warranties left for the console.
5. Monthly balance of the current month: sales $3.264.735, returns
   $3.052.320, net balance $212.415.
