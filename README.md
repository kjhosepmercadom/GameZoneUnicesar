# GameZoneUnicesar
## Módulo de Productos (Developer 1)

### Arquitectura de 4 Capas
- **Dominio (`com.gamezone.model`)**: Clases `Product`, `VideoGame` y `Console`.
- **Persistencia (`com.gamezone.persistence`)**: Manejo de lectura/escritura en archivo plano `data/products.txt`.
- **Servicio (`com.gamezone.service`)**: Reglas de negocio, validaciones de stock/precio y búsquedas.
- **Excepciones (`com.gamezone.exception`)**: Manejo de errores de dominio con `ProductException`.

### Ejecución de Pruebas Unitarias
Para ejecutar las pruebas del servicio de productos de forma independiente:
1. Ir a `src/test/java/com/gamezone/service/ProductServiceTest.java`.
2. Ejecutar la clase principal `ProductServiceTest`.