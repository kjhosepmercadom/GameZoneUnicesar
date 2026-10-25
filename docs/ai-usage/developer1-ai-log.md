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

## Entry 5 - Return model

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 4 - `feature/return-module` |
| Objective       | Implement `Return` and `Sale.canBeReturned()` of Requirement 3. |
| Query           | "Implement Return with calculateRefundAmount and generateReturnReceipt, and canBeReturned in Sale with the thirty day limit." |
| Response        | Proposed `Return` with an immutable sale reference (no setter) and a list with one entry per returned unit; `canBeReturned()` compares today with `date.plusDays(30)`. |
| Decision        | Accepted. The refund starts with list prices, as Requirement 3 defines it; the discount problem was left for adjustment A5. |
| Related commit  | `feat: add Return class with sale reference and returned products` |

## Entry 6 - Refund of discounted sales (A5)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Phase 4 - `fix/return-discounted-refund` |
| Objective       | Refund what the customer actually paid when the sale had a promotion. |
| Query           | "Apply adjustment A5: refund each item with price x (1 - discount / subtotal) and show the breakdown in the return receipt." |
| Response        | Added the private helpers `discountRate()` and `refundOf(product)` and a receipt table with list price, proportional discount and refund. |
| Decision        | Accepted. Checked with a sale of $3.499.700 with a $524.955 discount: the console is refunded $2.464.915 instead of $2.899.900. |
| Related commit  | `fix: refund returned items proportionally to the sale discount` |

## Entry 7 - Exception and validation layers (Requirement 6)

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 6 - `feature/exception-handling` |
| Objective       | Create the custom exception hierarchy and the three validators. |
| Query           | "Implement GameZoneException and its four subclasses, and ProductValidator, PersonValidator and SaleValidator as specified in Requirement 6." |
| Response        | Proposed an abstract unchecked root with an error code, one subclass per error category with Spanish messages, and final validator classes with static methods and a private constructor. |
| Decision        | Accepted. `validateUniquePerson` receives `List<? extends Person>` so it accepts the list of customers (generics are invariant). A third, protected constructor with the cause was added to the root so `PersistenceException` keeps the original `IOException`. |
| Related commit  | `feat: add GameZoneException as root of the exception hierarchy` |

## Requirement 7 - GRASP audit and correction

### Phase 1 - Audit

#### Entry 8 - GRASP audit of the developer 1 code

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 7, Phase 1 - `feature/grasp-audit` |
| Objective       | Review the products, accessory, promotion, return, warranty, exception and validation code against the nine GRASP patterns. |
| Query           | "Audit the classes developed by Developer 1 against the nine GRASP patterns, with evidence from the code." |
| Response        | Pointed out three findings: the stock is increased outside `Product`, `CategoryDiscount` decides the category with `instanceof`, and `Return` calculates the discount rate of the sale. It also noted that the file handling of `ProductRepository` is repeated in the other repositories. |
| Decision        | Accepted the three findings after checking each fragment in the code. The duplicated file handling was reported to the Leader as a boundary finding instead of being audited here, because most repositories belong to Developer 2. |
| Related commit  | `docs: add promotion hierarchy findings to developer1 audit` |

### Phase 2 - Correction

#### Entry 9 - GRASP corrections V-02, V-03 and V-05

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 7, Phase 2 - `feature/grasp-refactor` |
| Objective       | Correct the violations assigned to Developer 1 without changing the behaviour. |
| Query           | "Implement Product.increaseStock, the polymorphic getCategory and the use of Sale.calculateDiscountRate in Return, keeping every message and file identical." |
| Response        | Added `increaseStock` with the same message used by the services, `getCategory()` abstract in `Product` with one implementation per type (`Accessory` answers for all accessories), and replaced `discountRate()` in `Return`. |
| Decision        | Accepted. Checked with the regression script that the output and the data files are identical to the reference run. |
| Related commit  | `refactor: declare the product category polymorphically (V-05)` |

## Requirement 8 - SOLID audit and correction

### Phase 1 - Audit

#### Entry 10 - SOLID audit of the developer 1 code

| Field           | Content |
|-----------------|---------|
| Date            | 2026-10-24 |
| Tool            | Claude (Anthropic) - Claude Code |
| Phase and branch| Requirement 8, Phase 1 - `feature/solid-audit` |
| Objective       | Review the same code, including the classes changed in R7, against the five SOLID principles. |
| Query           | "Audit the classes of Developer 1 against SRP, OCP, LSP, ISP and DIP, considering the GRASP corrections." |
| Response        | Found that `ProductRepository` is not closed for new product types (OCP) and confirmed that the hierarchies satisfy LSP. It noted that `CategoryDiscount` is now closed for new categories thanks to R7. |
| Decision        | Accepted the OCP finding. The use of `ProductService` as a concrete class by other services was reported to the Leader, because it is a dependency of their code. |
| Related commit  | `docs: add product module findings to developer1 SOLID audit` |
