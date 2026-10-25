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
