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

## Entry 9 - Validators and typed exceptions in services and repositories (Requirement 6)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 6 - `feature/exception-handling` |
| Objective       | Replace the generic exceptions of every service and repository without changing the behaviour. |
| Query           | "Refactor the services to use the validators and typed exceptions, and the repositories to wrap file errors in PersistenceException." |
| Response        | Replaced each `IllegalArgumentException` with the matching category (`ResourceNotFoundException`, `BusinessRuleException` or `InvalidDataException`) and each `IllegalStateException` of the repositories with `PersistenceException`, keeping the file name and the operation in the message. |
| Decision        | Accepted. Searches such as `findById` still return `null` so the menu keeps its current messages; the validators are used when an operation requires the resource. Every condition that failed before still fails, now with a more precise type. |
| Related commit  | `refactor: use ProductValidator and typed exceptions in ProductService` |

## Requirement 7 - GRASP audit and correction

### Phase 1 - Audit

#### Entry 10 - GRASP audit of the developer 2 code

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 7, Phase 1 - `feature/grasp-audit` |
| Objective       | Review the person module and the repositories and services of accessories, promotions, returns and warranties against the nine GRASP patterns. |
| Query           | "Audit the classes developed by Developer 2 against the nine GRASP patterns, with evidence from the code." |
| Response        | Pointed out four findings: `WarrantyService` prefers the extended warranty with `instanceof`, `ReturnService` iterates the items of the sale, the file handling is duplicated in every repository, and `AccessoryService.restoreStock` changes the stock with the setter. |
| Decision        | Accepted the four findings after checking the code. The `instanceof` in the `toLine` methods of the repositories was judged acceptable for GRASP, because the model must not know the file format. |
| Related commit  | `docs: add repository findings to developer2 audit` |

### Phase 2 - Correction

#### Entry 11 - GRASP corrections V-02, V-04, V-06 and V-07

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 7, Phase 2 - `feature/grasp-refactor` |
| Objective       | Create `CsvFile`, migrate five repositories and correct the services without changing the behaviour. |
| Query           | "Create a CsvFile helper with readLines, writeLines and clean, use it in the repositories keeping every error message, and apply V-02, V-04 and V-06." |
| Response        | Proposed `CsvFile` receiving the beginning of each error message, so every message stays exactly the same, and the use of `Sale.findProduct`, `Sale.countUnitsOf`, `Product.increaseStock` and `getDurationInMonths()`. |
| Decision        | Accepted. `CsvFile` is package-private because only the repositories use it. Verified with the regression script: identical output and data. |
| Related commit  | `refactor: add CsvFile helper for the repositories (V-07)` |

## Requirement 8 - SOLID audit and correction

### Phase 1 - Audit

#### Entry 12 - SOLID audit of the developer 2 code

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 8, Phase 1 - `feature/solid-audit` |
| Objective       | Review the code of Developer 2, including `CsvFile` and the R7 changes, against the five SOLID principles. |
| Query           | "Audit the classes of Developer 2 against SRP, OCP, LSP, ISP and DIP." |
| Response        | Found that `ReturnService` also calculates the monthly report (SRP), that the accessory, promotion and warranty repositories use type switches (OCP), and that `ReturnService` depends on the concrete product and accessory services (DIP). |
| Decision        | Accepted the three findings; the DIP finding was reported to the Leader because `SaleService` has the same problem. |
| Related commit  | `docs: add return and warranty findings to developer2 SOLID audit` |
