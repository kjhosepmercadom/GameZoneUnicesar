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
