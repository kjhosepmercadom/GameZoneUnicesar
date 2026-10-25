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
