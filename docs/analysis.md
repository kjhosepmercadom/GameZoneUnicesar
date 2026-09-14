# Analysis — GameZone Unicesar

## People in the system

**1. What attributes are common to all people who interact with the store, and which are specific to each type of person? How is this distinction reflected in a class hierarchy?**

All people share the same basic attributes: `id`, `name`, and `phone`. These are declared in a base class `Person`. Each specific role adds its own attributes on top of that base: `Client` adds `email` and `purchaseHistory`, while `Seller` adds `employeeCode` and `shift`. This distinction is reflected through inheritance: `Client` and `Seller` extend `Person`, inheriting the common attributes and adding their own specific ones.

**2. Should there be a class representing a "generic person" without specifying a role? Why or why not? What implication does this decision have on the possibility of instantiating that class?**

No, there should not be an instantiable "generic person" class. In the real business, every person who interacts with the store is either a client or a seller; a person with no defined role does not exist as a valid business entity. For this reason, `Person` is declared as an **abstract class**. Declaring it abstract prevents the creation of `Person` objects directly (`new Person()` is not allowed), while still allowing the class to hold shared attributes and behavior that `Client` and `Seller` inherit.

## Products in the system

**3. What characteristics do all products the store sells have in common, regardless of type? What characteristics are specific to each product type?**

All products share: `id`, `title`, `price`, and `stock` (available quantity). These are declared in the base class `Product`. `VideoGame` adds `platform`, `genre`, and `ageRating`. `Console` adds `brand`, `model`, and `generation`.

**4. Each product type must be able to present a description that integrates its particular characteristics. How should this behavior be declared in the base class to guarantee that all subclasses implement it in their own way? What object-oriented mechanism allows this?**

The behavior is declared as an **abstract method** `getDescription()` in the base class `Product`. Because the method has no implementation in the abstract class, every concrete subclass (`VideoGame`, `Console`) is forced to provide its own implementation, using the `@Override` annotation. The object-oriented mechanism that allows this is **polymorphism**, combined with the use of an abstract method in an abstract class.

## Sales and relationships between entities

**5. A sale involves a client, a seller, and one or more products. What kind of relationships exist between the class representing the sale and the other classes of the system? Are these relationships inheritance, association, composition, or another type? Justify.**

`Sale` has an **association** relationship with `Client` and with `Seller`: a sale references an already-existing client and an already-existing seller, but does not own or control their lifecycle. `Sale` has a **composition** relationship with its sold items (modeled as a `SaleItem` class, which associates a `Product` with a quantity): the items only exist as part of a specific sale and have no meaning on their own, and they are created and destroyed together with the sale that owns them. None of these relationships are inheritance, since a `Sale` is not a specialization of `Client`, `Seller`, or `Product`.

**6. Should the sale be responsible for calculating its own total, or should this responsibility fall to another class? Argue your decision.**

The sale should be responsible for calculating its own total through a method such as `calculateTotal()`, because the total is intrinsic information about the sale itself — it depends only on the items that belong to that particular `Sale` object. This keeps the responsibility cohesive within the class that owns the data it needs (the list of `SaleItem`), following the principle that a class should manage the data it is directly responsible for.

## Business rules

**7. How is it guaranteed in the design that a sale cannot be registered without at least one product? At what point in the system should this rule be validated?**

The rule is validated in the **service layer**, specifically in `SaleService`, before a `Sale` object is created and persisted. Additionally, the `Sale` constructor itself performs a basic structural check (rejecting an empty or null item list) as a safeguard, but the full business rule — including checking stock availability for each item — is the responsibility of `SaleService`, since business rules do not belong in the domain layer.

**8. How is the automatic inventory update reflected in the design when a sale is registered? Which classes are involved in this operation?**

When `SaleService.registerSale(...)` is called, it first verifies through `ProductService` that each requested product has sufficient stock. If all items pass validation, `SaleService` asks `ProductService` to discount the sold quantity from each product's stock before creating and saving the `Sale`. The classes involved are `SaleService` (orchestrates the operation), `ProductService` (executes the stock update), and `Product` (holds the stock attribute that gets modified).

## Layer organization

**9. The system must be organized into four layers: model, persistence, services, and user interface. What type of classes belong to each layer? What criterion determines which layer a class should belong to?**

- `model`: domain entities that represent real business concepts (`Person`, `Client`, `Seller`, `Product`, `VideoGame`, `Console`, `Sale`, `SaleItem`).
- `persistence`: classes responsible for reading and writing data to files (`PersonRepository`, `ProductRepository`, `SaleRepository`).
- `service`: classes containing business rules, validations, and orchestration logic (`PersonService`, `ProductService`, `SaleService`).
- `ui`: the console menu classes that interact with the user.

The criterion for placing a class in a layer is its **responsibility**: a class belongs to `model` if it represents a business concept; to `persistence` if its job is reading/writing data; to `service` if its job is enforcing business rules; and to `ui` if its job is interacting with the user.

**10. Why should the logic of saving and retrieving data from files not be inside the domain classes? What problems arise when these responsibilities are mixed?**

File access logic should not be inside domain classes because it would violate the single responsibility principle: a domain class would then have two reasons to change — a change in business rules, or a change in how data is stored (e.g., switching from plain text to CSV). Mixing these responsibilities also tightly couples the domain to a specific storage format, making the code harder to test, harder to reuse, and harder to maintain.

**11. What dependencies are allowed between layers, and which are forbidden? Justify the direction of the allowed dependencies.**

The allowed dependency direction is: `ui → service → persistence → model`. The `model` layer depends on nothing else, since it represents pure business concepts that should not know about how they are displayed or stored. `persistence` depends only on `model`, since it needs to read and write domain objects. `service` depends on both `model` and `persistence`, since it needs to apply business rules to domain objects and to invoke persistence operations. `ui` depends on `service`, since the interface should never bypass business rules by talking directly to persistence. Any dependency in the opposite direction (for example, `model` depending on `persistence`, or `ui` depending on `persistence` directly) is forbidden, since it would break the separation of concerns that the layered architecture is meant to enforce.
