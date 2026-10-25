# SOLID Refactor Log - Requirement 8

## Non-regression strategy

The same strategy of Requirement 7: the keyboard script
`docs/regression/menu-operations-input.txt` runs every operation of the seven
submenus, including every error case, with the versioned data files. The
console output and the resulting `data/*.csv` files of each run are compared
with the reference run. The commands are described in
`docs/grasp-refactor-log.md`.

## Reference run (before the first correction)

Executed on `develop` (with the GRASP correction already merged) before the
first commit of `feature/solid-refactor`: 1376 lines of output, no exception
trace, sale `S0001` total $2.739.780, refund $2.564.850, net balance $174.930,
the same values of the final run of Requirement 7.

## Changes

| Class | Change | Violation | Principle applied | Commit / PR | Responsible |
|-------|--------|-----------|-------------------|-------------|-------------|
| `InventoryService` | New interface with `findById`, `updateStock`, `restoreStock` | V-01 | DIP, OCP | `refactor: add the InventoryService abstraction (V-01)` / PR "prepare the SOLID corrections" | Leader |
| `ProductService` | Implements `InventoryService` (`@Override` on the three methods) | V-01 | DIP, LSP | `refactor: implement InventoryService in ProductService (V-01)` / PR "apply developer 1 SOLID corrections" | Developer 1 |
| `RecordMapper` | New interface: type code, `supports`, `toColumns`, `fromFields` | V-02 | OCP | `refactor: add the RecordMapper abstraction for typed CSV lines (V-02)` / same PR | Developer 1 |
| `VideoGameRecordMapper`, `ConsoleRecordMapper` | One mapper per product type, same columns as before | V-02 | OCP, SRP | `refactor: add video game and console record mappers (V-02)` / same PR | Developer 1 |
| `ProductRepository` | Iterates its mappers; the `if` / `instanceof` chains were removed | V-02 | OCP | `refactor: use record mappers in ProductRepository (V-02)` / same PR | Developer 1 |
| `AccessoryService` | Implements `InventoryService` | V-01 | DIP, LSP | `refactor: implement InventoryService in AccessoryService (V-01)` / PR "apply developer 2 SOLID corrections" | Developer 2 |
| `ReturnService` | Restores stock through the inventory that owns the item; `instanceof Accessory` removed | V-01 | DIP, OCP | `refactor: restore stock through InventoryService in ReturnService (V-01)` / same PR | Developer 2 |
| `AccessoryRecordMapper`, `ControllerRecordMapper`, `CableRecordMapper`, `MemoryRecordMapper`, `AccessoryRepository` | Mappers per accessory type; the `switch` and `instanceof` chain were removed | V-02 | OCP | `refactor: use record mappers in AccessoryRepository (V-02)` / same PR | Developer 2 |
| `PercentageDiscountRecordMapper`, `CategoryDiscountRecordMapper`, `BulkPurchaseDiscountRecordMapper`, `PromotionRepository` | Mappers per promotion type | V-02 | OCP | `refactor: use record mappers in PromotionRepository (V-02)` / same PR | Developer 2 |
| `WarrantyRecordMapper`, `BasicWarrantyRecordMapper`, `ExtendedWarrantyRecordMapper`, `WarrantyRepository` | Mappers per warranty type, created from the resolved product and sale | V-02 | OCP | `refactor: use record mappers in WarrantyRepository (V-02)` / same PR | Developer 2 |
| `MonthlyBalanceCalculator`, `ReturnService` | The report arithmetic moved to a new class; `ReturnService` delegates and keeps its public methods | V-03 | SRP | `refactor: extract MonthlyBalanceCalculator from ReturnService (V-03)` / same PR | Developer 2 |
| `SaleService` | Resolves and updates items through the list of inventories; `instanceof Accessory` removed | V-01 | DIP, OCP | `refactor: resolve and update sale items through InventoryService (V-01)` / PR "resolve sale items through InventoryService" | Leader |

Every violation V-01 to V-03 of the consolidated audit appears in this log;
none was moved to the out of scope findings. No GRASP violation of R7 was
reintroduced: the menu, the repositories and `CsvFile` were not modified, and
`Sale` keeps the methods added in R7.

Trade-off: each mapper uses `instanceof` once, in its own `supports` method.
The type check is now confined to the class of that type, which is what OCP
asks for; it is not the Polymorphism problem fixed in R7.

## Verification per Pull Request

| Pull Request | Affected operations | Result |
|--------------|---------------------|--------|
| prepare the SOLID corrections | none (new interface) | identical output and data |
| apply developer 1 SOLID corrections | products (load and save), sales | identical output and data |
| apply developer 2 SOLID corrections | accessories, promotions, warranties, returns, monthly balance | identical output and data |
| resolve sale items through InventoryService | sales | identical output and data |

## Final verification

The complete script was executed after the last correction and compared with
the reference run: the 1376 lines of output and every file of `data/` are
identical. The project compiles without errors after every commit of the phase.
