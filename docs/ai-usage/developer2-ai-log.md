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
