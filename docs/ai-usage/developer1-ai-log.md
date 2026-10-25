# AI Usage Log - Developer 1 (Diego Alejandro Amaya Verdecia)

Personal log of every interaction with an AI tool, as required by the AI
policy of Requirement 5. The requests were made in a Claude Code session that
the team used to prepare the implementation of each requirement; every
generated file was reviewed and executed before being committed from this
member's account.

## Entry 1 - Accessory model

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 1 - `feature/accessory-module` |
| Objective       | Implement the accessory hierarchy of Requirement 1 respecting the restrictions of the Taller. |
| Query           | "Implement Accessory, Controller, Cable and Memory as specified in Requirement 1, extending Product, with private attributes and JavaDoc in English." |
| Response        | Proposed `Accessory` as an abstract subclass of `Product` with the list of compatible console identifiers, compatibility methods, an abstract `getAccessoryType()` and an overridden `getDescription()`, plus one concrete subclass per accessory type. |
| Decision        | Accepted extending `Product`, because it reuses the stock logic and lets a sale mix all items. Modified: the type constants first proposed in the subclasses were removed because the Taller forbids public attributes; the type is returned by `getAccessoryType()`. Discarded: keeping `Console` objects inside the accessory, because it would couple the two data files. |
| Related commit  | `feat: add Accessory abstract class extending Product` |

## Entry 2 - Promotion model

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 2 - `feature/promotion-module` |
| Objective       | Implement `Promotion` and its three discount strategies of Requirement 2. |
| Query           | "Implement Promotion, PercentageDiscount, CategoryDiscount and BulkPurchaseDiscount as specified, with isActive and the abstract calculateDiscount(Sale)." |
| Response        | Proposed the abstract class with the validity period, the abstract `calculateDiscount` and `getDescription`, and a protected helper `describePeriod()` shared by the descriptions of the subclasses. |
| Decision        | Accepted. `getDescription()` was kept abstract so the menu can list any promotion polymorphically. The category check uses `instanceof`, as the requirement describes the category by product type. |
| Related commit  | `feat: declare abstract discount calculation and description in Promotion` |

## Entry 3 - Accessory category discount (A1)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 2 - `feature/accessory-category-discount` |
| Objective       | Allow category promotions on accessories. |
| Query           | "Apply adjustment A1: accept ACCESSORY in CategoryDiscount, validate the three categories in PromotionService, add the option to the menu and preload an accessory promotion." |
| Response        | Added the `ACCESSORY` branch to `belongsToTargetCategory`, the validation of the three values, the third menu option and promotion `PR004`. |
| Decision        | Accepted. Verified with a sale that only has accessories that the 25% accessory promotion is selected when it is the best one. |
| Related commit  | `feat: support ACCESSORY as target category in CategoryDiscount` |

## Entry 4 - Warranty model

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 3 - `feature/warranty-module` |
| Objective       | Implement `Warranty`, `BasicWarranty` and `ExtendedWarranty` of Requirement 4. |
| Query           | "Implement the warranty hierarchy; the end date must be calculated in the constructor using the abstract duration." |
| Response        | Proposed calculating `endDate = startDate.plusMonths(getDurationInMonths())` in the constructor of `Warranty`, with constant durations in the subclasses and the 10% cost in `ExtendedWarranty`. |
| Decision        | Accepted. Calling the abstract method from the constructor is safe here because the subclasses return constants and do not use their own fields; this was documented in the warranty analysis. |
| Related commit  | `feat: add Warranty abstract class with product, sale and validity dates` |
