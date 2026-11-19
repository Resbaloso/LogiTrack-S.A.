# LogiTracksA API - Sistema de Gestión de Inventario y Logística 📦🚚

Tecnologías y Plataformas: Java 21, Spring Boot 4.1.2, Spring Security, JWT, MySQL, JPA/Hibernate, Swagger/OpenAPI, Lombok 🚀🔥

## Descripción 😉

**LogiTracksA API** es una solución backend RESTful diseñada para gestionar de manera integral las operaciones logísticas de una empresa con múltiples bodegas. El sistema cubre la administración de productos, control de inventario por bodega, registro de usuarios y roles, gestión de credenciales de acceso, seguimiento de movimientos de stock (entradas, salidas y transferencias entre bodegas) con sus respectivos detalles, y un módulo de auditoría para trazabilidad de operaciones.

La API implementa seguridad basada en JWT con Spring Security, diferenciando permisos entre el rol `admin_maestro` y los `encargados` de bodega. Expone su documentación interactiva a través de Swagger UI.

## ¿Qué soluciona? 🤨

El sistema resuelve problemas clave en la operación logística de una organización multi-bodega:

- **Control Centralizado de Inventario:** Registra y actualiza el stock de cada producto en cada bodega de forma individual, con alertas de stock bajo configurable por umbral.
- **Trazabilidad Completa de Movimientos:** Cada entrada, salida o transferencia queda registrada con usuario responsable, bodegas de origen/destino, fecha y detalle de productos involucrados.
- **Gestión de Acceso por Roles:** El `admin_maestro` administra usuarios, roles y credenciales; los encargados de bodega operan el inventario y los movimientos dentro de su alcance.
- **Auditoría de Operaciones:** Mantiene un registro histórico de operaciones del sistema (INSERT, UPDATE, DELETE) por usuario, entidad afectada y rango de fechas para control de calidad.
- **Autenticación Stateless con JWT:** Acceso seguro mediante tokens firmados, sin manejo de sesiones del lado del servidor, listo para integrarse con frontends y aplicaciones móviles.

## Funcionamiento y estructura 😃

La arquitectura sigue el patrón en capas estándar de Spring Boot: `Controller → Service → Repository → Entity`, con DTOs de entrada/salida separados y mappers dedicados por entidad.

### 🔐 Autenticación (JWT)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/auth/login` | Autentica al usuario y retorna un token JWT |

**Body de ejemplo:**
```json
{
  "username": "admin",
  "password": "1234"
}
```
**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

> Todos los endpoints protegidos requieren el header: `Authorization: Bearer <token>`

---

### 👤 Usuarios — `/api/usuarios` *(solo ADMIN_MAESTRO)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/usuarios` | Crea un nuevo usuario |
| GET | `http://localhost:8080/api/usuarios` | Lista todos los usuarios |
| GET | `http://localhost:8080/api/usuarios/1` | Busca un usuario por ID |
| GET | `http://localhost:8080/api/usuarios/filtroEmail?email=juan@empresa.com` | Busca usuarios por email |
| GET | `http://localhost:8080/api/usuarios/filtroActivo?activo=true` | Filtra usuarios por estado activo |
| PUT | `http://localhost:8080/api/usuarios/1` | Actualiza un usuario por ID |
| DELETE | `http://localhost:8080/api/usuarios/1` | Elimina un usuario por ID |

---

### 🎭 Roles — `/api/roles` *(solo ADMIN_MAESTRO)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/roles` | Crea un nuevo rol |
| GET | `http://localhost:8080/api/roles` | Lista todos los roles |
| GET | `http://localhost:8080/api/roles/1` | Busca un rol por ID |
| GET | `http://localhost:8080/api/roles/filtroNombre?nombre=admin_maestro` | Busca roles por nombre |
| PUT | `http://localhost:8080/api/roles/1` | Actualiza un rol por ID |
| DELETE | `http://localhost:8080/api/roles/1` | Elimina un rol por ID |

---

### 🔑 Credenciales — `/api/credenciales` *(solo ADMIN_MAESTRO)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/credenciales` | Crea credenciales para un usuario |
| GET | `http://localhost:8080/api/credenciales` | Lista todas las credenciales |
| GET | `http://localhost:8080/api/credenciales/1` | Busca credenciales por ID |
| GET | `http://localhost:8080/api/credenciales/filtroUsername?username=juan.perez` | Busca por username |
| GET | `http://localhost:8080/api/credenciales/usuario/2` | Busca credenciales por ID de usuario |
| GET | `http://localhost:8080/api/credenciales/rol/2` | Busca credenciales por ID de rol |
| PUT | `http://localhost:8080/api/credenciales/1` | Actualiza credenciales por ID |
| DELETE | `http://localhost:8080/api/credenciales/1` | Elimina credenciales por ID |

---

### 🏭 Bodegas — `/api/bodegas` *(autenticado)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/bodegas` | Crea una nueva bodega |
| GET | `http://localhost:8080/api/bodegas` | Lista todas las bodegas |
| GET | `http://localhost:8080/api/bodegas/1` | Busca una bodega por ID |
| GET | `http://localhost:8080/api/bodegas/filtroNombre?nombre=Norte` | Filtra bodegas por nombre |
| GET | `http://localhost:8080/api/bodegas/filtroUbicacion?ubicacion=Bucaramanga` | Filtra por ubicación |
| GET | `http://localhost:8080/api/bodegas/encargado/2` | Busca bodegas por encargado |
| GET | `http://localhost:8080/api/bodegas/filtroCapacidad?capacidad=1000` | Filtra por capacidad mínima |
| PUT | `http://localhost:8080/api/bodegas/1` | Actualiza una bodega por ID |
| DELETE | `http://localhost:8080/api/bodegas/1` | Elimina una bodega por ID |

---

### 📦 Productos — `/api/productos` *(autenticado)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/productos` | Crea un nuevo producto |
| GET | `http://localhost:8080/api/productos` | Lista todos los productos |
| GET | `http://localhost:8080/api/productos/1` | Busca un producto por ID |
| GET | `http://localhost:8080/api/productos/filtroCategoria?categoria=Materiales` | Filtra por categoría |
| GET | `http://localhost:8080/api/productos/filtroNombre?nombre=Pallet` | Filtra por nombre |
| GET | `http://localhost:8080/api/productos/filtroPrecioMaximo?precio=50000` | Filtra por precio máximo |
| GET | `http://localhost:8080/api/productos/filtroPrecioMinimo?precio=10000` | Filtra por precio mínimo |
| PUT | `http://localhost:8080/api/productos/1` | Actualiza un producto por ID |
| DELETE | `http://localhost:8080/api/productos/1` | Elimina un producto por ID |

---

### 🗃️ Inventario — `/api/inventario` *(autenticado)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/inventario` | Registra stock de un producto en una bodega |
| GET | `http://localhost:8080/api/inventario` | Lista todo el inventario |
| GET | `http://localhost:8080/api/inventario/1` | Busca un registro de inventario por ID |
| GET | `http://localhost:8080/api/inventario/bodega/1` | Inventario de una bodega específica |
| GET | `http://localhost:8080/api/inventario/producto/1` | Bodegas que contienen un producto |
| GET | `http://localhost:8080/api/inventario/bodega/1/producto/2` | Stock de un producto en una bodega |
| GET | `http://localhost:8080/api/inventario/stockBajo?umbral=10` | Productos con stock por debajo del umbral |
| PUT | `http://localhost:8080/api/inventario/1` | Actualiza un registro de inventario |
| DELETE | `http://localhost:8080/api/inventario/1` | Elimina un registro de inventario |

---

### 🔄 Movimientos — `/api/movimientos` *(autenticado)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/movimientos` | Registra un movimiento de inventario |
| GET | `http://localhost:8080/api/movimientos` | Lista todos los movimientos |
| GET | `http://localhost:8080/api/movimientos/1` | Busca un movimiento por ID |
| GET | `http://localhost:8080/api/movimientos/usuario/2` | Movimientos realizados por un usuario |
| GET | `http://localhost:8080/api/movimientos/filtroTipo?tipoMovimiento=ENTRADA` | Filtra por tipo (ENTRADA / SALIDA / TRANSFERENCIA) |
| GET | `http://localhost:8080/api/movimientos/filtroFechas?fechaInicio=2026-01-01&fechaFin=2026-11-15` | Filtra por rango de fechas |
| GET | `http://localhost:8080/api/movimientos/bodegaOrigen/1` | Movimientos desde una bodega origen |
| GET | `http://localhost:8080/api/movimientos/bodegaDestino/2` | Movimientos hacia una bodega destino |
| GET | `http://localhost:8080/api/movimientos/filtroUsuarioTipo?usuarioId=2&tipoMovimiento=SALIDA` | Filtra por usuario y tipo |
| PUT | `http://localhost:8080/api/movimientos/1` | Actualiza un movimiento por ID |
| DELETE | `http://localhost:8080/api/movimientos/1` | Elimina un movimiento por ID |

---

### 📋 Detalle de Movimientos — `/api/detallemovimiento` *(autenticado)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/detallemovimiento` | Registra un producto dentro de un movimiento |
| GET | `http://localhost:8080/api/detallemovimiento` | Lista todos los detalles |
| GET | `http://localhost:8080/api/detallemovimiento/1` | Busca un detalle por ID |
| GET | `http://localhost:8080/api/detallemovimiento/movimiento/3` | Detalles asociados a un movimiento |
| GET | `http://localhost:8080/api/detallemovimiento/producto/2` | Detalles donde aparece un producto |
| GET | `http://localhost:8080/api/detallemovimiento/movimiento/3/producto/2` | Detalle por movimiento y producto |
| GET | `http://localhost:8080/api/detallemovimiento/filtroCantidad?cantidad=5` | Filtra por cantidad mínima |
| PUT | `http://localhost:8080/api/detallemovimiento/1` | Actualiza un detalle por ID |
| DELETE | `http://localhost:8080/api/detallemovimiento/1` | Elimina un detalle por ID |

---

### 🔍 Auditoría — `/api/auditoria` *(autenticado)*

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `http://localhost:8080/api/auditoria` | Registra una operación auditada |
| GET | `http://localhost:8080/api/auditoria` | Lista todos los registros de auditoría |
| GET | `http://localhost:8080/api/auditoria/1` | Busca una auditoría por ID |
| GET | `http://localhost:8080/api/auditoria/usuario/2` | Auditorías por usuario |
| GET | `http://localhost:8080/api/auditoria/filtroTipo?tipoOperacion=INSERT` | Filtra por tipo (INSERT / UPDATE / DELETE) |
| GET | `http://localhost:8080/api/auditoria/filtroEntidad?entidadAfectada=Producto` | Filtra por entidad afectada |
| GET | `http://localhost:8080/api/auditoria/filtroFechas?fechaInicio=2026-01-01&fechaFin=2026-11-15` | Filtra por rango de fechas |
| GET | `http://localhost:8080/api/auditoria/filtroUsuarioTipo?usuarioId=2&tipoOperacion=UPDATE` | Filtra por usuario y tipo de operación |
| PUT | `http://localhost:8080/api/auditoria/1` | Actualiza una auditoría por ID |
| DELETE | `http://localhost:8080/api/auditoria/1` | Elimina una auditoría por ID |

---

### 🛡️ Seguridad y Roles

| Rol | Acceso |
|-----|--------|
| `ADMIN_MAESTRO` | Todos los endpoints, incluyendo usuarios, roles y credenciales |
| `ENCARGADO` | Bodegas, productos, inventario, movimientos, detalles y auditoría |
| Público | `POST /auth/login` y Swagger UI |

La API utiliza sesiones **stateless** (sin cookies ni estado del lado del servidor). CORS habilitado para `http://localhost:5500` y `http://127.0.0.1:5500`.

---

### 📖 Documentación Swagger

Con la aplicación levantada, accede a la documentación interactiva en:

```
http://localhost:8080/swagger-ui/index.html
```

---

### 🗄️ Modelo de Datos

Las entidades principales del sistema y sus relaciones:

- **`usuario`** → Persona con nombre, apellidos, email y estado activo.
- **`rol`** → Define el nivel de acceso (`admin_maestro`, `encargado`).
- **`credenciales`** → Vincula un `usuario` con un `rol`, almacenando username y password.
- **`bodega`** → Almacén físico con nombre, ubicación, capacidad y usuario encargado.
- **`producto`** → Artículo con nombre, descripción, categoría y precio.
- **`inventario`** → Relación N:M entre `bodega` y `producto` con cantidad en stock.
- **`movimiento`** → Registro de movimiento de stock con tipo, fecha, usuario y bodegas origen/destino.
- **`detalle_movimiento`** → Productos y cantidades incluidas en cada `movimiento`.
- **`auditoria`** → Bitácora de operaciones (INSERT/UPDATE/DELETE) por usuario y entidad.

---

### ⚙️ Stack Tecnológico

| Tecnología | Versión |
|------------|---------|
| Java | 21 |
| Spring Boot | 4.1.2 |
| Spring Security | Incluido en Boot |
| JWT (jjwt) | 0.11.5 |
| Spring Data JPA | Incluido en Boot |
| MySQL Connector | Runtime |
| Springdoc OpenAPI | 3.0.1 |
| Lombok | Incluido en Boot |
| Maven | Wrapper incluido |

---

### 🚀 Cómo ejecutar el proyecto

**1. Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd logitracksaapi
```

**2. Configurar la base de datos**

Crear la base de datos en MySQL y ejecutar el script `scriptsql.sql` incluido en la raíz del proyecto.

```sql
CREATE DATABASE logitrack_database;
```

**3. Configurar `application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/logitrack_database
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```

**4. Compilar y ejecutar**
```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

---

## Información de contacto 🧑🏻‍🦱💫✨

**Autor:**

Juan Sebastian Toscano Duarte

**Contacto:**

juan.toscano@gmail.com


                                                *++==+                                              
                                        #*===----=+==++*#                                           
                                   *+------:----:-===++++++**    #  *++                             
                               #*=::::------::::::-=+====-----++==+++****                           
                            +-::---::::::::--::::::-==-===------===-++****                          
                          *-:::::::-:::--:::-----::=--------==-----=++++***#                        
                    #*  *=:.........::::::::::------=------=------:=-+++***+*#                      
                  *+=--:::.::.....:::::::::::-:----------==--------=++++++++++**#                   
              *++*:.::.::::::..:::..:::--::::-:-------==--==----=-=====+++++++++*#*                 
             #+::-:......:::..::::::::-----:------:--:::-======-======++++++++=++++*                
             +:.:::....::.:::.::::.::----------:::::-----==++++========+++++=====++**#              
           *=-:::...............::..:::-----:--:-----===-:-=====--=======+++=====++++**             
           =::............::....::::::::::::--:------===---====-=:=-==-=========++++++***           
        **=:......................::.:::::::-::--=---===----------------------====++++++++***       
         *:....................:::.:.::::---------=---#%+==-------------------=====++++++++**       
        *......................::::.::::::-----=+%%=--=+%%%%#+===-=--=-----------==+++++++++*       
       +.......................:.::::::-:-:---*%%%%%%+-+%%=#%%%%#+===------------===+++++++++*      
      +:....................::::.::------=*#%%%+=-+%%%%%*---=+#%%%%%#==---------=====++++++++#     
      =.......................::.:::-=+##%%%#==------#%%%=-----====*#%%%%%#+=-----=====+++++++*     
     +........................:::-=##%%%*-------------+%%+---=**=----=--=##%%%%+=======+++++++*#    
     =.....................::::+#%%%*=-------------++=--+--=+%++##---=-------=#%%*=+===+++++++*#    
    *:.....................:+#%%%*---------------=#*++#+---=#%==#%%:-----------+#%%+===++++++++*    
    +:...................-*#%%=-:::::-:---------=+%==*%%=--=#%%%%%%=:-----------+#%#++++++++++**    
    =..................:+#%#-::::::::::::-------=*%%%%%%%:-=*%@%%%%#:----=--=====+%%*++++++++++*    
    =................:+#%#::::::::::::::::------=+%%%%%%%:--=%%%%%%=:-----========*%%++++++++++*    
    +................-#%+::::::::::::::::::------=#%%%%%%:---=%%%%%:------=========%%+++++++++**    
    +................*%#::::::::::::::::-:::-----==#%%%%--------=-------===========#%*+++++++***    
    *:..............:#%*::::::::::::::::::-:--------=+=-----------------==========+%%++++++****     
    *=.............:.*%+:::::::::::::::::-----------------------------============#%#++++++****     
     *-............:.*%#:::::::::::::::::::::------------------==--==============*%%+++++++***#     
      *-...........:::#%*:::::::::::::::-:::-:------------------===========+++++#%#+==++++****      
       #-.......:::::.=#%+:::::::::::::::::-------------------=--=========+++++%@*+++=+++****       
        *........:::::.=#%+--::::::::::----:---::--------===------=======+++#%@#++*+++=+****        
         -....:::-:-:..:=#%+::::::--==++++===---:-------=#%%#+==========*#%%@#*++***+++****         
         #:...::::-=-::.=#%%%%%%%%%%%%%%%%%%%%%%%#**+=*#%%+##%%%%%%%%%%%@%%*+++*******+***          
          *::.:::--===-::-------::::-=====+-====##%%%%%%+==-=-==+=+=+++++++++++*********            
           *-:::::--===-::::::::::---=+%%%#+=-=-----=---------===-=-=*#+=+++++*******               
             =::::--=====-::::::-----+++*#*--+#%#*=----=-=====++###*---+++++++*******               
              *=::---==++==--::::=====++=--*#+====#*+#%%%%%%#**+++%-:-=====+*******                 
                 +=--====++==-:----===+**===*####%%*++++*#%%####*#::======+*****#                   
                    #*==-+++==:::----=+*##+=-=====-===***==+++++=---=+===*####                      
                          **++----==--=++*##+--=+====++++++++=---==+==+*##*                         
                                #*+++++===**#*====+==**+=-::-=======*###                            
                                          ****##  * *+*+++++++=***                  
