# Accessory Module Analysis - Requirement 1

## 1. Should accessories extend `Product` or form an independent hierarchy?

Accessories extend `Product` through the abstract class `Accessory`. An
accessory *is a* sellable product: it has an identifier, a title, a price and
a stock, it can be bought in a sale, its stock must be validated and deducted,
and the sale total adds its price. All of that is already implemented in
`Product` (`hasEnoughStock`, `decreaseStock`, getters and setters) and in
`Sale` (which works with `List<Product>`).

Making `Accessory` a subclass of `Product`:

- reuses the common attributes and behaviour without duplicating code;
- lets a single `Sale` mix video games, consoles and accessories, because the
  sale keeps a `List<Product>`;
- keeps the model coherent: every item that can be sold is a `Product`.

An independent hierarchy would force us to duplicate the stock logic and to
change `Sale` so it could hold two unrelated types of items.

## 2. Common and specific attributes of the accessories

| Class        | Attributes                                                      |
|--------------|-----------------------------------------------------------------|
| `Product`    | `id`, `title`, `price`, `stock` (inherited)                     |
| `Accessory`  | `compatibleConsoleIds` (common to every accessory)              |
| `Controller` | `connectionType` (wireless or wired)                            |
| `Cable`      | `lengthInMeters`, `connectorType`                               |
| `Memory`     | `capacityInGb`, `memoryType`                                    |

`Accessory` is abstract because no accessory is sold without a specific type.
It declares the abstract method `getAccessoryType()`, which lets the service
filter accessories by type without asking for the concrete class, and it
overrides `getDescription()` with the common part of the description
(identifier, title, price, stock and compatible consoles). Each subclass
overrides `getDescription()` again, calling `super.getDescription()` and adding
its own attributes.

## 3. How is the compatibility between accessories and consoles represented?

Compatibility is an attribute of the **accessory**: `Accessory` keeps the list
`compatibleConsoleIds` and offers `addCompatibleConsole`,
`removeCompatibleConsole` and `isCompatibleWith`. The console does not need to
know its accessories, so `Console` was not modified.

The relationship is stored by identifier, not by object reference, because:

- accessories and products are persisted in different files;
- it avoids a dependency from the accessory file to the product file at load time.

In the persistence, the sixth column of `data/accessories.csv` keeps the
console identifiers separated by commas (for example `CN001,CN003`).

## 4. Changes needed in `SaleService`

The changes are additive:

- the constructor receives `AccessoryService`, and the stored sales are loaded
  resolving both products and accessories;
- `registerSale` resolves each item identifier first as a product and, if it
  does not exist, as an accessory;
- stock validation is unified, because both types are `Product` and share
  `hasEnoughStock`;
- the inventory update is delegated to `ProductService.updateStock` or to
  `AccessoryService.updateStock` depending on the type of each item.

Sales with only video games and consoles behave exactly as before.

## 5. In which layers are the new classes placed?

| Class                                       | Layer       | Reason                                         |
|---------------------------------------------|-------------|------------------------------------------------|
| `Accessory`, `Controller`, `Cable`, `Memory` | model       | Business concepts, no file access              |
| `AccessoryRepository`                       | persistence | Reads and writes `data/accessories.csv`        |
| `AccessoryService`                          | service     | Registration rules, queries and stock updates  |
| `ConsoleMenu` (extended)                    | ui          | New submenu; it only talks to the services     |

The dependency direction `ui -> service -> persistence -> model` is preserved.
