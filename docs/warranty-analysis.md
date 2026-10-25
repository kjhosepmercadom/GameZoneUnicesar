# Warranty Module Analysis - Requirement 4

## 1. How does the hierarchy reflect common and different warranty data?

Both warranty types share an identifier, the covered product, the sale, a
start date and an end date, plus the same behaviour: checking whether they
are active on a date and generating a certificate. That common part is the
abstract class `Warranty`. What changes between them is the duration, the
coverage name and the cost, so those are abstract methods implemented by
`BasicWarranty` and `ExtendedWarranty`:

| Method                   | `BasicWarranty`   | `ExtendedWarranty`         |
|--------------------------|-------------------|----------------------------|
| `getDurationInMonths()`  | 6                 | 12                         |
| `getWarrantyType()`      | Garantia Basica   | Garantia Extendida         |
| `getAdditionalCost()`    | 0                 | 10% of the product price   |

The mechanism is **inheritance plus polymorphism** (a template method): the
constructor of `Warranty` calculates the end date with
`startDate.plusMonths(getDurationInMonths())`, and the call is resolved at
runtime by the concrete subclass. The calculation is written once and each
type only says how long it lasts.

## 2. Where is the decision "only consoles get a basic warranty"?

In the service layer, inside `SaleService.registerSale`, because it is a
business rule of the sale process and not a property of a single object. The
real type of each item is checked with the `instanceof` operator
(`item instanceof Console`): the items of a sale are kept as `Product`, and
`instanceof` tells whether the object is actually a console at runtime.

## 3. How is the end date calculated in each subclass?

The subclasses do not calculate it: they only return their duration. The
calculation is done once, in the constructor of `Warranty`, so a warranty can
never exist without an end date and the two dates are always consistent.
Doing it in the constructor is safe here because `getDurationInMonths()` only
returns a constant and does not use attributes of the subclass. A separate
method would allow objects with an empty end date and would repeat the
calculation in every place that creates a warranty.

## 4. Where is the extended warranty cost applied?

In `SaleService.registerSale`, after the sale is created and the stock is
validated. For every console of the sale the service assigns the basic
warranty and, if the console identifier is in the new parameter
`extendedWarrantyIds`, it assigns an extended warranty and adds its
`getAdditionalCost()` to `Sale.extendedWarrantyCost`. The final total is
`subtotal - discount + extendedWarrantyCost`.

Changes to `registerSale`: a new parameter `List<String> extendedWarrantyIds`
(empty or null means no extended warranty), the warranty assignment loop and
the extended warranty cost stored in the sale. `ConsoleMenu` asks the user for
each console of the sale.

## 5. Where is "warranties about to expire" and what does it need?

In `WarrantyService.listWarrantiesExpiringSoon(int daysAhead)`. It needs only
the list of warranties it already manages (loaded through
`WarrantyRepository`) and the current date. It is a query with a business
rule (active today and ending before *today + daysAhead*), so it belongs to
the service layer: the menu only asks how many days to look ahead and prints
the result, and the model only provides `isActive` and `getEndDate`.
