# AI Usage Log - Technical Lead (Kevin Jhosep Mercado Moron)

Personal log of every interaction with an AI tool, as required by the AI
policy of Requirement 5. The requests were made in a Claude Code session that
the team used to prepare the implementation of each requirement; every
generated file was reviewed and executed before being committed from this
member's account.

## Entry 1 - Accessory integration

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 1 - `feature/accessory-module` |
| Objective       | Let sales include accessories and add the accessory submenu. |
| Query           | "Integrate the accessory module: SaleService must accept products and accessories, validate stock in a unified way and delegate the inventory update by item type; add the accessory submenu to ConsoleMenu." |
| Response        | Proposed resolving every item identifier first in `ProductService` and then in `AccessoryService`, validating stock with `Product.hasEnoughStock` (common to both), and delegating `updateStock` by type. The sales loading passes products and accessories to the repository. |
| Decision        | Accepted. The repeated parts of `registerSale` were extracted to private methods (`resolveItems`, `validateStock`, `updateInventory`) to keep the method readable. |
| Related commit  | `feat: accept products and accessories in SaleService.registerSale` |

## Entry 2 - Promotion integration

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 2 - `feature/promotion-module` |
| Objective       | Apply the best promotion when registering a sale and show the breakdown in the receipt. |
| Query           | "Add appliedPromotionName and discountAmount to Sale, update generateReceipt and apply findBestPromotionFor in registerSale." |
| Response        | Added the two attributes, `calculateFinalTotal()`, the receipt breakdown, the new columns in `data/sales.csv` (read only when present, so old sales still load) and the promotion submenu. |
| Decision        | Accepted. `calculateTotal()` keeps meaning "subtotal" so the promotions and the old code keep working; the final total is a separate method. Added `findSaleById` to show the detail of a sale. |
| Related commit  | `feat: apply the best active promotion in SaleService.registerSale` |

## Entry 3 - Warranty integration

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 3 - `feature/warranty-module` |
| Objective       | Generate warranties when registering a sale and add the warranty submenu. |
| Query           | "Integrate warranties: basic warranty for every console, optional extended warranty whose cost is added to the total, and the warranty submenu." |
| Response        | Explained that `WarrantyRepository` needs `SaleService` to load, so `WarrantyService` cannot be passed to the constructor of `SaleService`. Proposed a temporary setter (`setWarrantyService`) wired in `Main`. |
| Decision        | Accepted the setter only as a temporary workaround to keep the system working; it is removed by adjustment A2. The menu asks for the extended warranty once per console product. |
| Related commit  | `feat: generate basic and extended warranties when registering a sale` |

## Entry 4 - Unified sale registration (A3)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 3 - `refactor/unified-sale-registration` |
| Objective       | Reorganize `registerSale` in the eight steps of adjustment A3. |
| Query           | "Apply adjustment A3 without changing the results of the sales." |
| Response        | Reordered the method into the eight numbered steps with the private methods `applyBestPromotion` and `assignWarranties`, added the extended warranty line to the receipt and split the sales menu into `readSaleItems` and `askExtendedWarranties`. |
| Decision        | Accepted. Before and after the refactor the same sale (console + controller with extended warranty) gives the same total, $1.996.810. |
| Related commit  | `refactor: reorganize registerSale into the eight integration steps` |

## Entry 5 - Return integration

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 4 - `feature/return-module` |
| Objective       | Add `ProductService.restoreStock` and the returns submenu with the monthly balance. |
| Query           | "Add restoreStock to ProductService and the return management submenu of Requirement 3." |
| Response        | `restoreStock` increases the stock and saves through the repository; the submenu shows the original sale before asking which items are returned. |
| Decision        | Accepted. `restoreStock` was delivered in its own Pull Request before the return service, because `ReturnService` depends on it. |
| Related commit  | `feat: add restoreStock to ProductService` |

## Entry 6 - Integration documentation (A8)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 5 - `docs/integration-documentation` |
| Objective       | Document the adjustments A1 to A7 and the integrated design. |
| Query           | "Write integration-analysis.md, the integrated class diagram, and update the layers diagram, README and TEAM.md." |
| Response        | Drafted the cause and solution of every adjustment, a single Mermaid diagram by layer and the updated README with the demonstration scenario. |
| Decision        | Accepted after checking every number of the demonstration scenario against the program. The diagrams were rendered to confirm the Mermaid syntax. |
| Related commit  | `docs: describe the integration adjustments A1 to A7 in integration-analysis.md` |

## Entry 7 - Exception handling in the menu (Requirement 6)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 6 - `feature/exception-handling` |
| Objective       | Show a different message for every exception type. |
| Query           | "Refactor ConsoleMenu to catch the custom exceptions by type, with the messages of Requirement 6." |
| Response        | Proposed one `try` per submenu delegating to `showError`, which has one catch block per exception type, and handling `PersistenceException` at startup in `Main`. |
| Decision        | Accepted, and merged before the service refactor so the menu already understands the new exceptions when the services start throwing them. `IllegalArgumentException` is still handled because the model keeps some rules (for example, a sale without products). |
| Related commit  | `refactor: show differentiated messages by exception type in ConsoleMenu` |

## Requirement 7 - GRASP audit and correction

### Phase 1 - Audit

#### Entry 8 - GRASP audit of the leader code and consolidation

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 7, Phase 1 - `feature/grasp-audit` |
| Objective       | Audit `Sale`, `SaleRepository`, `SaleService`, `ConsoleMenu`, `Main` and `restoreStock`, and consolidate the three reports. |
| Query           | "Audit the leader code against the nine GRASP patterns and consolidate the three reports with unique identifiers and responsible members." |
| Response        | Found that `ConsoleMenu` concentrates every submenu, the input readers and the error messages (High Cohesion), and that `restoreStock` changes the stock outside `Product`. In the consolidation it merged the three reports of the stock problem and the three of the file handling into single violations. |
| Decision        | Accepted. The `instanceof` checks of `SaleService` were kept as correct for GRASP, with the reason written in the report. The out of scope defects were registered and not fixed. |
| Related commit  | `docs: consolidate GRASP audit findings from the three reports` |

### Phase 2 - Correction

#### Entry 9 - Preparation of the GRASP correction

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 7, Phase 2 - `feature/grasp-refactor` |
| Objective       | Define the non-regression strategy and add the methods of `Sale` that the developers need. |
| Query           | "Write a keyboard script that runs every menu operation, including the error cases, and add calculateDiscountRate, findProduct and countUnitsOf to Sale." |
| Response        | Wrote `docs/regression/menu-operations-input.txt` and the three methods of `Sale`. |
| Decision        | Accepted. The reference run was recorded before any correction (1376 lines of output). |
| Related commit  | `docs: define the GRASP non-regression strategy and baseline` |

#### Entry 10 - Split of the console menu

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 7, Phase 2 - `feature/grasp-refactor` |
| Objective       | Correct V-01 moving one submenu per commit, so the project compiles after every commit. |
| Query           | "Split ConsoleMenu into ConsoleInput, ErrorPrinter and one submenu class per module without changing any text shown to the user." |
| Response        | Proposed temporary private methods in `ConsoleMenu` that delegate to the new classes while the remaining submenus are moved, and removing them at the end. |
| Decision        | Accepted. After the last commit `ConsoleMenu` only has the main menu. The final regression run is identical to the reference. |
| Related commit  | `refactor: move the sales submenu to SaleMenu (V-01)` |
