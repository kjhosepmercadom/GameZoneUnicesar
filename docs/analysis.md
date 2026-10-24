# Analysis - GameZone Unicesar

This document answers the eleven guiding questions of the analysis stage. The
decisions taken here are the basis of the hierarchy, class and layers
diagrams, and of the implementation in the `com.gamezone` package.

## About the people of the system

### 1. Common and specific attributes of people

Every person that interacts with the store has a **name**, an
**identification** number and a contact **phone**. These attributes are
common and are declared once in the base class `Person`.

The specific attributes depend on the role:

| Class      | Specific attributes                 |
|------------|-------------------------------------|
| `Customer` | `email`                             |
| `Seller`   | `employeeCode`, `shift`             |

This distinction is reflected in a hierarchy where `Person` is the
generalization and `Customer` and `Seller` are specializations
(`Customer extends Person`, `Seller extends Person`). The subclasses reuse the
common attributes through the constructor (`super(...)`) and the inherited
getters and setters.

The purchase history of a customer is not stored as an attribute of
`Customer`: it is derived from the registered sales
(`SaleService.findSalesByCustomer`). Storing it twice would duplicate
information and could make both copies inconsistent.

### 2. Should there be a "generic person" class?

There must be a class that groups the common information, but it must **not**
be instantiable. In the store nobody is "just a person": everyone is either a
customer or a seller. For this reason `Person` is declared `abstract`. The
implication is that `new Person(...)` is not allowed by the compiler; only
`Customer` and `Seller` objects can be created, while code can still work with
the general type `Person` when needed.

## About the products of the system

### 3. Common and specific characteristics of products

All products share an **id**, a **title**, a **price** and a **stock**
(quantity available in inventory). These are declared in the abstract class
`Product`, together with the behaviour that is the same for every product:
checking whether there is enough stock (`hasEnoughStock`) and deducting sold
units (`decreaseStock`).

Specific characteristics:

| Class       | Specific attributes                    |
|-------------|----------------------------------------|
| `VideoGame` | `platform`, `genre`, `ageRating`       |
| `Console`   | `brand`, `model`, `generation`         |

### 4. How to declare the description behaviour

The method is declared in `Product` as an **abstract method**:

```java
public abstract String getDescription();
```

An abstract method has no body in the base class, so every concrete subclass
is forced by the compiler to implement it (marked with `@Override`). The OOP
mechanisms involved are **abstraction** (abstract classes and methods) and
**polymorphism**: the user interface iterates over a `List<Product>` and calls
`getDescription()` without knowing whether each element is a `VideoGame` or a
`Console`; the right implementation is selected at runtime (dynamic binding).

## About sales and the relationships between entities

### 5. Relationships between `Sale` and the other classes

None of them is inheritance, because a sale *is not* a customer, a seller or a
product. They are associations:

- `Sale` -> `Customer`: **association**, multiplicity `0..*` sales to `1`
  customer. The customer exists independently of the sale.
- `Sale` -> `Seller`: **association**, multiplicity `0..*` sales to `1`
  seller. The seller exists independently of the sale.
- `Sale` -> `Product`: **aggregation**, multiplicity `1..*` products per sale.
  The sale groups products, but the products belong to the inventory and keep
  existing when a sale is created or removed, so it is not a composition.

### 6. Who calculates the total?

The sale calculates its own total (`Sale.calculateTotal()`). Following the
*Information Expert* principle, the class that owns the information needed
(the list of products bought and their prices) is the one responsible for the
calculation. Placing it elsewhere would force other classes to read the
internal list of the sale and would duplicate the logic.

## About the business restrictions

### 7. A sale must have at least one product

The rule is validated in two points:

1. `SaleService.registerSale` rejects the operation when the list of product
   identifiers is empty, before touching the inventory. This is the main
   business validation and gives a clear message to the user.
2. The constructor of `Sale` also rejects an empty product list, so an invalid
   `Sale` object can never exist in memory, even if a future class tries to
   create one without the service.

### 8. Automatic inventory update

When a sale is registered, `SaleService` first verifies that every product
exists and that the requested quantity is available
(`ProductService.hasEnoughStock`). Only when all the checks pass, it calls
`ProductService.updateStock` for each product, which uses
`Product.decreaseStock` and persists the new inventory through
`ProductRepository`. The classes involved are `SaleService`, `ProductService`,
`Product` and `ProductRepository`. Validating everything before deducting
guarantees that a rejected sale does not modify the inventory.

## About the layered organization

### 9. Classes of each layer

| Layer           | Package                   | Classes                                                         |
|-----------------|---------------------------|-----------------------------------------------------------------|
| Model           | `com.gamezone.model`      | `Person`, `Customer`, `Seller`, `Product`, `VideoGame`, `Console`, `Sale` |
| Persistence     | `com.gamezone.persistence`| `ProductRepository`, `PersonRepository`, `SaleRepository`       |
| Service         | `com.gamezone.service`    | `ProductService`, `PersonService`, `SaleService`                |
| User interface  | `com.gamezone.ui`         | `ConsoleMenu`                                                   |

`Main` lives in the root package `com.gamezone`: it is the composition root
that creates the objects of every layer and injects the dependencies.

The criterion is the **responsibility** of the class: if it represents a
business concept, it belongs to the model; if it reads or writes files, to
persistence; if it applies business rules and coordinates operations, to the
service layer; if it talks to the user, to the user interface.

### 10. Why file access must not be inside domain classes

Mixing both responsibilities violates the *Single Responsibility Principle*.
If `Product` knew how to write itself to a CSV file:

- any change in the file format would force changes in the domain classes;
- the domain classes could not be reused or tested without the file system;
- changing the storage (for example, to a database) would affect the whole
  model;
- the code would be harder to read because business rules and I/O details
  would be mixed.

Keeping the repositories separate lets the model stay simple and stable.

### 11. Allowed and forbidden dependencies

Allowed dependencies:

- `ui` -> `service`: the menu asks the services to execute operations.
- `service` -> `persistence` and `service` -> `model`: services apply the
  rules over domain objects and ask the repositories to store them.
- `persistence` -> `model`: repositories convert domain objects to file lines
  and back.

Forbidden dependencies:

- `model` -> any other layer: the domain is the most stable part of the system
  and must not change because of the UI or the storage.
- `ui` -> `persistence`: skipping the services would skip the business rules
  (for example, saving a sale without validating stock).
- Any dependency "upwards" (`persistence` -> `service`, `service` -> `ui`),
  which would create cycles and couple low-level details to high-level policy.

Dependencies point from the layers that change more (UI) to the layers that
change less (model), so a change in an outer layer never breaks an inner one.
