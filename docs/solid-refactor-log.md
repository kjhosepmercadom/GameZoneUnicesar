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
