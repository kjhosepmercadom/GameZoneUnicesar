# Hierarchy Diagram - GameZone Unicesar

The diagram shows only the inheritance relationships of the model layer.
Abstract classes are marked with `<<abstract>>` and concrete classes with
`<<concrete>>`.

```mermaid
classDiagram
    direction TB

    class Person {
        <<abstract>>
    }
    class Customer {
        <<concrete>>
    }
    class Seller {
        <<concrete>>
    }

    class Product {
        <<abstract>>
    }
    class VideoGame {
        <<concrete>>
    }
    class Console {
        <<concrete>>
    }

    Person <|-- Customer
    Person <|-- Seller
    Product <|-- VideoGame
    Product <|-- Console
```

| Hierarchy | Base class (abstract) | Derived classes (concrete) |
|-----------|-----------------------|----------------------------|
| People    | `Person`              | `Customer`, `Seller`       |
| Products  | `Product`             | `VideoGame`, `Console`     |

`Sale` is a concrete model class that does not take part in any hierarchy.
