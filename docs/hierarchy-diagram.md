# Hierarchy Diagram — GameZone Unicesar

This diagram shows only the inheritance (generalization/specialization) relationships present in the model layer. Abstract classes are explicitly marked. No associations, attributes, or methods are included, per the design restrictions.

```mermaid
classDiagram
    direction TB

    class Person {
        <<abstract>>
    }
    class Client
    class Seller

    Person <|-- Client
    Person <|-- Seller

    class Product {
        <<abstract>>
    }
    class VideoGame
    class Console

    Product <|-- VideoGame
    Product <|-- Console

    classDef default fill:#ffffff,stroke:#000000,color:#000000,stroke-width:1px
    class Person,Client,Seller,Product,VideoGame,Console default
```

## Summary

- **Abstract base classes:** `Person`, `Product`.
- **Concrete derived classes:** `Client`, `Seller` (from `Person`); `VideoGame`, `Console` (from `Product`).
- **Number of hierarchies:** 2, satisfying the minimum required by the design restrictions (at least two identifiable hierarchies via inheritance relationships).
- **Restriction compliance:** this diagram intentionally omits attributes, methods, and associations — only inheritance relationships and the abstract/concrete distinction are shown, as required by the workshop specification.
