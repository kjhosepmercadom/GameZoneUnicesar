# Promotion Module Analysis - Requirement 2

## 1. How does the hierarchy reflect common data and different calculation rules?

All promotions share an identifier, a name and a validity period (start and
end dates), and all of them answer the same question: *how much discount
would you give to this sale?* That common part lives in the abstract class
`Promotion`. Each way of calculating the discount is a concrete subclass:

| Class                  | Specific attributes                 | Rule                                                    |
|------------------------|-------------------------------------|---------------------------------------------------------|
| `PercentageDiscount`   | `percentage`                        | percentage of the subtotal                              |
| `CategoryDiscount`     | `percentage`, `targetCategory`      | percentage of the products of one category only         |
| `BulkPurchaseDiscount` | `minimumQuantity`, `percentage`     | percentage of the subtotal if the sale reaches the minimum |

The mechanism is **polymorphism** with dynamic binding: `PromotionService`
iterates over a `List<Promotion>` and calls `calculateDiscount(sale)` on each
element without knowing its concrete type. Every subclass is a different
strategy behind the same interface, so a new promotion type can be added
without changing the code that selects the best one.

## 2. How is the discount method declared in the base class?

It is declared as an abstract method:

```java
public abstract double calculateDiscount(Sale sale);
```

`Promotion` is abstract too. The declaration guarantees that every concrete
subclass implements its own calculation (marked with `@Override`); otherwise
the class does not compile. It also prevents creating a "generic" promotion
that has no rule. `getDescription()` is declared abstract for the same reason,
so every promotion can be listed with its specific rule.

## 3. Where is the logic that selects the best promotion?

In `PromotionService.findBestPromotionFor(Sale sale)`. It filters the
promotions that are active on the date of the sale, asks each one for its
discount and returns the one with the biggest amount (or `null` if none gives
a discount). This is coherent with the layered architecture because:

- it is a **business rule** that coordinates several promotion objects, which
  is the responsibility of the service layer;
- `PromotionService` already owns the list of promotions loaded from the
  repository.

It must not be in `Sale` because a sale does not know the promotion catalog;
putting it there would make the model depend on the stored promotions. It
must not be in `ConsoleMenu` because the user interface only reads input and
shows results; a rule in the menu would be skipped by any other client of
the services.

## 4. Changes in `Sale` and `generateReceipt`

`Sale` receives two private attributes with getters and setters:
`appliedPromotionName` and `discountAmount`, plus `calculateFinalTotal()`
(subtotal minus discount). `generateReceipt` now shows the subtotal, the
discount with the name of the promotion (or "no aplica") and the final total.

The existing behaviour is not broken: `calculateTotal()` keeps returning the
subtotal, the new attributes default to no promotion and zero discount, and
sales stored before this change are loaded with those default values because
the repository only reads the new columns when they exist.

## 5. Where are the validity dates checked?

In both places, each one with its own responsibility:

- `Promotion.isActive(LocalDate date)` answers whether a given date is inside
  the validity period. It is the **Information Expert**: the promotion owns its
  start and end dates.
- `PromotionService` decides **which date** to use: the current date for
  `listActivePromotions()` and the sale date for `findBestPromotionFor(sale)`.
  It applies the rule over the whole catalog.
