# GRASP Analysis - Requirement 7

Answers to the five guiding questions. Each answer was written by the member
indicated and discussed by the team.

## 1. Criteria for a violation, a correct pattern or "not applicable" (Developer 1)

A fragment is a **violation** when a responsibility is assigned to a class
that does not own the information or the role the pattern asks for, and the
team can point to a concrete consequence today or for a predictable change
(for example, "every new product type modifies this method"). A pattern is
**correct** when we can show the class and method where it is applied as the
pattern describes. It is **not applicable** when the audited code has no point
where the pattern's question arises (for example, Controller in model classes,
which never receive system events).

Minimum evidence accepted in the consolidation:

- class and method, and for violations the exact fragment of code in the
  audited commit;
- an explanation that names the pattern question that is not answered;
- the consequence, and a proposed solution that preserves the behaviour.

A verdict without the fragment, or without a consequence, was not accepted.

## 2. Boundary findings (Developer 2)

Three findings involve code of more than one member:

- **V-02** (stock increase): Developer 1 found that `Product` cannot add
  units; Developer 2 and the Leader found `setStock(getStock() + quantity)` in
  `AccessoryService.restoreStock` and `ProductService.restoreStock`. It was
  registered once; Developer 1 adds `Product.increaseStock` and each owner
  changes their own service.
- **V-04** (return service iterating the sale): found by Developer 2 in
  `ReturnService`, but the fix needs new methods in `Sale` (Leader). The Leader
  adds `findProduct` and `countUnitsOf`; Developer 2 removes the helpers.
- **V-07** (duplicated file handling): Developer 2 found it in five
  repositories and the other two members in theirs. Developer 2 creates
  `CsvFile`; each member migrates their repositories.

Each member reported the boundary findings to the Leader instead of auditing
the other member's code, and the consolidation names one responsible per part.

## 3. Fragments that break more than one pattern (Developer 1)

Yes. `CategoryDiscount.belongsToTargetCategory` breaks Polymorphism (type
checks instead of a polymorphic method) and Protected Variations (each new
product type changes it). `ConsoleMenu` breaks High Cohesion, Pure Fabrication
and Protected Variations at the same time. The duplicated file handling breaks
Pure Fabrication and High Cohesion.

To avoid duplicated findings, each fragment was registered **once**, under
the pattern whose solution fixes it, and the related patterns are listed in
the same row. The guiding pattern is the one that names the design change:
for `CategoryDiscount` it is Polymorphism (move the decision to the product
types), for `ConsoleMenu` it is High Cohesion (split the class), for the
repositories it is Pure Fabrication (create `CsvFile`).

## 4. How do we guarantee that every menu operation still works? (Developer 2)

The strategy is a **scripted regression run** of the whole menu:

1. `docs/regression/menu-operations-input.txt` contains the keyboard input of
   every menu operation, including error cases (duplicated product, negative
   price, invalid email, duplicated customer, invalid promotion dates,
   insufficient stock, sale without products, unknown customer, return of a
   product that is not in the sale, quantities above the units sold).
2. Before the first correction the script is run on `develop` with the
   original data files; the console output and the resulting `data/*.csv`
   files are the reference.
3. After each Pull Request the script is run again with the same initial data,
   and the output and the data files are compared with the reference
   (`diff` must be empty).

Evidence shown at the end: the comparison of the reference run with the final
run, recorded in `docs/grasp-refactor-log.md`.

## 5. Order of the corrections and coordination (Leader)

The corrections were ordered by dependency, so every Pull Request compiles and
keeps the behaviour:

1. **Leader**: the new methods of `Sale` (`calculateDiscountRate`,
   `findProduct`, `countUnitsOf`), because V-03 and V-04 of the developers need
   them. The before diagram and the regression baseline go in the same PR.
2. **Developer 1**: `Product.increaseStock` and `Product.getCategory` (V-02,
   V-05) and the change in `Return` (V-03).
3. **Developer 2**: `CsvFile` and five repositories (V-07), and the changes in
   `AccessoryService`, `ReturnService` and `WarrantyService` (V-02, V-04, V-06).
4. **Developer 1**: `ProductRepository` on `CsvFile` (V-07).
5. **Leader**: the split of `ConsoleMenu` (V-01), `ProductService.restoreStock`
   (V-02), `SaleRepository` on `CsvFile` (V-07), the after diagram and the
   final regression run.

All the work happened in `feature/grasp-refactor`, one member at a time: a
member starts only after the previous Pull Request is merged and the branch is
recreated from `develop`. No two members edit the same file in parallel, so
there are no conflicts.
