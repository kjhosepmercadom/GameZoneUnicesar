# Exception and Validation Analysis - Requirement 6

## 1. Checked or unchecked root exception?

`GameZoneException` extends `RuntimeException` (unchecked). The errors it
represents (missing resource, broken business rule, invalid data, file
problem) can happen in almost every operation and are handled in a single
place, the user interface. With a checked exception every method of every
service and repository, and every caller, would need a `throws` clause or a
try/catch that only re-throws, which adds noise without adding handling.

Consequence on the signatures: services and repositories keep their
signatures without `throws` clauses; the possible exceptions are documented
with `@throws` in the JavaDoc, and `ConsoleMenu` catches them by type.

## 2. Why a custom hierarchy with a common root?

- **Error differentiation:** with a different class per category the menu can
  show a specific message for "not found", "business rule", "invalid data"
  and "persistence" errors, instead of one generic text.
- **Single catch point when needed:** `catch (GameZoneException e)` captures
  every system error, while `catch (BusinessRuleException e)` captures just
  one category.
- **Information hiding:** low level `IOException`s are wrapped in
  `PersistenceException`; the user never sees a technical message or trace.
- Design principles applied: *Single Responsibility* (validators validate,
  services coordinate), *Open/Closed* (a new category is a new subclass) and
  *Liskov Substitution* (every subclass can be handled as a `GameZoneException`).

## 3. Where are the validators invoked?

The validators of `com.gamezone.validation` (`ProductValidator`,
`PersonValidator`, `SaleValidator`) are invoked by the services at the
beginning of each operation, before any business logic or persistence call.
This is coherent with the layered architecture: the validation layer sits
logically between services and model, it works only with input values and
model objects, and it is reused by every service of its module.

Validators must **not** access the persistence package: data that depends on
the stored state (for example, the list of existing customers for the
uniqueness check) is passed by the service as a parameter. Otherwise the
validation layer would duplicate the responsibilities of the services and
break the dependency rule.

## 4. How do we guarantee that the ten operations keep working?

The refactor only changes the exception type: the same conditions that caused
an error before still cause it. The strategy was:

1. Write the exception and validation classes first, without using them.
2. Make the menu able to show every exception type **before** changing the
   services, so the system works at every commit.
3. Refactor one service or repository per commit and run the menu after each one.

Evidence at the end: every operation of the menu (products, people, sales,
accessories, promotions, warranties and returns) was executed with the
preloaded data, producing the same results as before, and each error case
was triggered to show its new message (unknown product, insufficient stock,
duplicated customer, invalid email, invalid promotion dates, return of a
product that is not in the sale).

## 5. One try with several catch blocks, cascade or a general catch?

Each submenu keeps one `try` around the selected operation and delegates the
handling to the private method `showError`, which contains **several
specific catch blocks** ordered from the most specific to the most general:
`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`,
`PersistenceException`, then `IllegalArgumentException` (rules still enforced
by the model, such as an empty sale) and finally `RuntimeException`.

A single `catch (GameZoneException e)` would lose the message
differentiation, and repeating the six catch blocks in seven submenus would
duplicate code. Centralizing them keeps every message in one place and makes
the order of the catch blocks explicit.

| Exception                   | Message shown to the user                                                     |
|-----------------------------|-------------------------------------------------------------------------------|
| `ResourceNotFoundException` | No se encontro el recurso solicitado: *detail*                                |
| `BusinessRuleException`     | No se puede completar la operacion: *detail*                                  |
| `InvalidDataException`      | Los datos ingresados no son validos: *detail*                                 |
| `PersistenceException`      | Ocurrio un problema al acceder a los datos del sistema. Contacte al administrador. |
