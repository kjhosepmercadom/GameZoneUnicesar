# AI Usage Log - Developer 2 (Dair Jose Quintero Ortiz)

Personal log of every interaction with an AI tool, as required by the AI
policy of Requirement 5. The requests were made in a Claude Code session that
the team used to prepare the implementation of each requirement; every
generated file was reviewed and executed before being committed from this
member's account.

## Entry 1 - Accessory persistence and service

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 1 - `feature/accessory-module` |
| Objective       | Implement `AccessoryRepository` and `AccessoryService` of Requirement 1. |
| Query           | "Implement AccessoryRepository with data/accessories.csv and a type discriminator, and AccessoryService with the methods of the specification." |
| Response        | Proposed a CSV line `TYPE;id;title;price;stock;compatibleIds;field1;field2`, with the compatible console identifiers separated by commas, and a service that keeps the accessories in memory and saves after every change. |
| Decision        | Accepted. The filter by type uses `getAccessoryType()` instead of `instanceof`. Registration validates required fields, positive price, non negative stock and unique identifier, like the product module. |
| Related commit  | `feat: add AccessoryRepository with CSV save support` |

## Entry 2 - Promotion persistence and service

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 2 - `feature/promotion-module` |
| Objective       | Implement `PromotionRepository` and `PromotionService`, including the selection of the best promotion. |
| Query           | "Implement PromotionRepository with data/promotions.csv and PromotionService with findBestPromotionFor(Sale) returning null when the best discount is zero." |
| Response        | Proposed evaluating only the promotions active on the date of the sale and keeping the one with the biggest discount, starting from zero so a zero discount returns `null`. |
| Decision        | Accepted. The preloaded promotions are valid from 2026-09-01 to 2026-12-31 so they apply while the requirements are being tested. |
| Related commit  | `feat: select the best active promotion for a sale` |

## Entry 3 - Warranty persistence and service

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 3 - `feature/warranty-module` |
| Objective       | Implement `WarrantyRepository` and `WarrantyService` of Requirement 4. |
| Query           | "Implement WarrantyRepository with data/warranties.csv; its constructor injects what it needs to resolve the Sale and Product references while loading." |
| Response        | Followed the specification: the repository receives `SaleService` and `ProductService` to resolve the identifiers. It warned that this creates the cycle `SaleService -> WarrantyService -> WarrantyRepository -> SaleService`. |
| Decision        | Accepted as specified for this phase, knowing that adjustment A2 removes the cycle. The service lists warranties expiring soon as those active today whose end date is within the requested days. |
| Related commit  | `feat: load warranties resolving sale and product references` |

## Entry 4 - Circular dependency (A2)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 3 - `fix/warranty-circular-dependency` |
| Objective       | Remove the dependency of `WarrantyRepository` on `SaleService`. |
| Query           | "Apply adjustment A2 so every object can be built in Main with constructor injection." |
| Response        | Proposed persisting only identifiers, resolving them from the sales passed to `loadAll(List<Sale>)`, adding `WarrantyService.loadWarranties(sales)` called by `SaleService` after loading its sales, and removing the setter used in `Main`. |
| Decision        | Accepted. The requirement suggests injecting `SaleRepository` into `WarrantyService`; it was discarded because reading the sales file again would create a second copy of every `Sale` (and would need customers, sellers and products). Using the sales already loaded keeps a single object per sale. Documented in the integration analysis. |
| Related commit  | `fix: load warranties from identifiers without depending on SaleService` |

## Entry 5 - Return persistence and service

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 4 - `feature/return-module` |
| Objective       | Implement `ReturnRepository` and `ReturnService` with the thirty day and ownership rules and the monthly balance. |
| Query           | "Implement ReturnRepository and ReturnService as specified in Requirement 3." |
| Response        | Proposed `ReturnRepository.loadAll(sales, products)` receiving the lists instead of the services, so the persistence layer does not depend on the service layer, and a validation of the quantities returned per product. |
| Decision        | Accepted. Added the rule that a product cannot be returned more times than it was sold, counting previous returns of the same sale; without it a customer could return the same unit twice. |
| Related commit  | `feat: implement return registration with deadline and ownership rules` |

## Entry 6 - Accessory returns (A4)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 4 - `fix/return-accessory-stock` |
| Objective       | Restore the stock of returned accessories. |
| Query           | "Apply adjustment A4." |
| Response        | Added `AccessoryService.restoreStock`, the delegation by item type in `ReturnService` and the resolution of returned items from the items of the original sale. |
| Decision        | Accepted. Resolving from the sale also guarantees that a loaded return can only reference items that were really sold. |
| Related commit  | `fix: delegate stock restoration by item type in ReturnService` |

## Entry 7 - Monthly balance report (A6)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 4 - `fix/monthly-balance-report` |
| Objective       | Show sales, returns and net balance using the final total of each sale. |
| Query           | "Apply adjustment A6 keeping the signature of generateMonthlyBalance." |
| Response        | Added `calculateMonthlySales` (with `calculateFinalTotal()`) and `calculateMonthlyReturns`; `generateMonthlyBalance` returns their difference and the menu prints the three values. |
| Decision        | Accepted. Verified: sales $3.264.735, returns $3.052.320, net balance $212.415 in the demonstration scenario. |
| Related commit  | `fix: calculate monthly sales with the final total of each sale` |

## Entry 8 - Warranty cancellation (A7)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 4 - `feature/return-warranty-cancellation` |
| Objective       | Cancel the warranties of returned consoles and refund the extended warranty. |
| Query           | "Apply adjustment A7 after A5." |
| Response        | Added `WarrantyService.cancelWarranties(productId, saleId)`, the warranty refund in `Return` (calculation, receipt and file) and the call from `ReturnService` for every returned console. |
| Decision        | Accepted as specified: all the warranties of the product in the sale are cancelled. A partial return of only one of several identical consoles also cancels the warranties of the others; this limitation is noted for a future requirement. |
| Related commit  | `feat: cancel the warranties of returned consoles in ReturnService` |
