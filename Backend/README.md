# Microservicio de Gestión de Inventarios

Este microservicio permite gestionar productos, lotes (batches), inventarios, listas de precios y bodegas para un sistema de inventario farmacéutico.

## Orden de Creación de Entidades
El orden correcto para crear entidades depende de las relaciones entre ellas. Debes seguir este flujo:

1. **Producto (`ProductCreateDTO`)**
   - Es la entidad base. Debes crear el producto primero.
   - Ejemplo JSON:
   ```json
   {
     "code": "P001",
     "name": "Paracetamol",
     "description": "Analgésico",
     "category": "Medicamento",
     "presentation": "Caja",
     "laboratory": "LabChile",
     "defaultPricingMethod": "LAST_PURCHASE",
     "basePrice": 1000.00,
     "unit": "unidad",
     "active": true
   }
   ```

2. **Bodega (`WarehouseDTO`)**
   - Puedes crear bodegas en cualquier momento, pero si vas a asociar inventarios o listas de precios a una bodega, créala antes.
   - Ejemplo JSON:
   ```json
   {
     "name": "Bodega Central",
     "location": "Santiago",
     "capacity": 1000,
     "status": "ACTIVA"
   }
   ```

3. **Lote (`Batch`)**
   - Un lote siempre debe estar asociado a un producto (requiere el ID del producto).
   - Ejemplo JSON:
   ```json
   {
     "product": { "id": 1 },
     "batchNumber": "L001",
     "expirationDate": "2025-12-31",
     "quantity": 100,
     "status": "ACTIVE"
   }
   ```

4. **Lista de Precios (`PriceListDTO`)**
   - Puede requerir el ID de producto y/o bodega.
   - Ejemplo JSON:
   ```json
   {
     "name": "Lista General",
     "productId": 1,
     "warehouseId": 1,
     "salePrice": 1200.00,
     "costPrice": 1000.00,
     "marginPercentage": 20,
     "validFrom": "2025-07-01",
     "validTo": "2025-12-31",
     "active": true,
     "priceType": "GENERAL",
     "currency": "CLP",
     "pricingMethod": "LAST_PURCHASE"
   }
   ```

5. **Inventario (`InventoryDTO`)**
   - Requiere el ID de bodega y el ID de lote.
   - Ejemplo JSON:
   ```json
   {
     "warehouseId": 1,
     "batchId": 1,
     "quantity": 50,
     "inventoryType": "GENERAL"
   }
   ```

## ¿Cómo saber el orden?
- Si un DTO tiene un campo como `productId`, `warehouseId` o `batchId`, primero debes crear esa entidad base y luego usar su ID.
- Si el DTO solo tiene campos simples, puedes crearlo directamente.

## Resumen de los DTO principales
- **ProductCreateDTO**: Para crear productos. No depende de otras entidades.
- **WarehouseDTO**: Para crear bodegas. No depende de otras entidades.
- **Batch**: Para crear lotes. Depende de un producto.
- **PriceListDTO**: Para crear listas de precios. Depende de producto y/o bodega.
- **InventoryDTO**: Para crear inventarios. Depende de bodega y lote.

## Recomendaciones para pruebas en Postman
- Siempre usa el header `Content-Type: application/json`.
- Usa los ejemplos de JSON según el DTO correspondiente.
- Sigue el orden de creación recomendado para evitar errores de integridad referencial.

---

¿Dudas? Consulta los archivos DTO en el proyecto para ver los campos exactos que espera cada endpoint.
