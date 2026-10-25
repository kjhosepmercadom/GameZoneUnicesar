# SOLID Analysis - Requirement 8

Answers to the five guiding questions. Each answer was written by the member
indicated and discussed by the team.

## 1. Criteria for a violation, a fulfilled principle or "not applicable" (Developer 1)

- **Violation:** we can show a concrete fragment where the principle's
  question gets a "no" (for OCP: a new variant requires editing working code;
  for DIP: a high level rule depends on a concrete low level class that is
  expected to vary; for SRP: the class has two independent reasons to change),
  together with the consequence for a predictable change.
- **Correct:** we can show where and how the principle is satisfied (the
  class and method, and why the question gets a "yes").
- **Not applicable:** the code has no point where the question arises, for
  example ISP in a module without interfaces, or LSP in classes without
  subclasses.

Minimum evidence: class, method and fragment for every violation; class and
method for every correct verdict; a short justification for "not applicable".
A verdict based only on opinion was not accepted.

## 2. Boundary findings (Developer 2)

Two violations involve more than one member:

- **V-01 (OCP, DIP):** the Leader found it in `SaleService` and Developer 2 in
  `ReturnService`; the fix touches `ProductService` (Developer 1). It is
  registered once; the Leader creates `InventoryService` and each member makes
  their class implement or use it.
- **V-02 (OCP):** found by Developer 1 in `ProductRepository` and by
  Developer 2 in three repositories. Developer 1 creates `RecordMapper`, and
  Developer 2 reuses it.

Each member reported to the Leader the parts that belong to someone else, and
the consolidated audit assigns one responsible per part.

## 3. Relation with the GRASP audit (Developer 1)

- **SOLID violation in code already corrected by GRASP:** `ReturnService` was
  corrected in R7 (V-04 Information Expert) but keeps V-01 (DIP) and V-03
  (SRP). The `instanceof Accessory` of `SaleService` was accepted in R7 for
  Polymorphism and is now a violation of OCP and DIP: the two catalogs look at
  the same code from different angles.
- **GRASP correction that solved a SOLID problem:** `Product.getCategory()`
  (R7 V-05) made `CategoryDiscount` closed for new categories, so OCP is
  fulfilled in the promotion hierarchy; the split of `ConsoleMenu` (R7 V-01)
  fulfils SRP in the user interface; `CsvFile` (R7 V-07) took the file
  handling out of the repositories (SRP).

## 4. How do we guarantee that every menu operation still works? (Developer 2)

The same regression script of Requirement 7,
`docs/regression/menu-operations-input.txt`, covers every operation of the
seven submenus and every error message. It was executed on `develop` before
the first correction (reference) and after every Pull Request of Phase 2, with
the same initial data files. The output and the resulting `data/*.csv` files
must be identical to the reference. The evidence is recorded in
`docs/solid-refactor-log.md`.
