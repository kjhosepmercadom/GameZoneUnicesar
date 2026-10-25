# Return Module Analysis - Requirement 3

## 1. What kind of relationship exists between `Return` and `Sale`?

It is an **association** (`Return` -> `Sale`, multiplicity `0..*` returns to
`1` sale). A return *references* a sale that already exists; it is not a kind
of sale, so it is not inheritance. It is not composition either, because the
sale exists before the return and keeps existing independently of it. The
reference is immutable: `Return` offers `getSale()` but no setter.

## 2. How is a partial return represented?

`Return` keeps its own list `returnedProducts` (`List<Product>`), separate
from the product list of the sale. It stores only the units that the
customer gives back, one entry per unit, and each entry is the same
`Product` object that appears in the original sale. If a customer bought a
console, a game and a controller and returns only the console, the list has
just the console. `ReturnService` checks that every returned unit belongs to
the sale and that a product is not returned more times than it was sold
(counting previous returns of the same sale).

## 3. Where is the thirty day rule validated?

In the service layer: `ReturnService.registerReturn` rejects the operation
when the sale is out of the return period. The date calculation itself lives
in the model, in `Sale.canBeReturned()`, because the sale owns its date
(Information Expert); the service applies the rule and produces the error
message for the user.

The Java mechanism is the `java.time` API: `date.plusDays(30)` gives the last
day of the period and `LocalDate.now().isAfter(...)` compares it with today.
(`ChronoUnit.DAYS.between(start, end)` is an equivalent alternative.)

## 4. Which existing method increases the stock?

`ProductService.restoreStock(String productId, int quantity)`, added to the
product service of the Taller as an additive change. It is invoked from
`ReturnService.registerReturn` for every returned product, and it persists
the new stock in `data/products.csv` through `ProductRepository`.

Reusing the service instead of writing the stock directly from the return
module keeps one single place that changes the inventory and saves it. If the
logic were duplicated, a future change (for example, a new validation or a
different file) would have to be repeated, and the two copies could behave
differently.

## 5. Where is the monthly balance and what does it need?

In `ReturnService.generateMonthlyBalance(int month, int year)`. The balance
combines two modules: the sales of the month (obtained through
`SaleService.listAllSales()`) and the returns of the month (managed by
`ReturnService` itself). Placing it in the return service is coherent with
the layered architecture because it is a business calculation that
coordinates information through services, without the menu or the model
reading files. Its dependencies are `SaleService` (sales) and its own return
list, loaded through `ReturnRepository`.
