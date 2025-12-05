# LevelUp-App-Movil

📄 README.md – LevelUp Gamer App

🎮 1. Nombre del Proyecto

LevelUp Gamer App – Aplicación Móvil + Microservicios + API Externa + Pruebas Unitarias

Proyecto desarrollado como parte de la evaluación final, integrando arquitectura MVVM, microservicios Spring Boot, persistencia local con DataStore, API externa (CheapShark) y pruebas unitarias con MockK/JUnit.

👥 2. Integrantes

Paloma Fuentes
Jorge Albornoz

⚙️ 3. Funcionalidades Principales
📌 Aplicación Móvil (Android – Kotlin + Compose)

Registro y login de usuarios (microservicio backend).

Perfil de usuario con actualización remota y local.

Catálogo de productos consumidos desde microservicio Spring Boot.

Carrito de compras con validación de stock y persistencia en DataStore.

Proceso de compra con registro remoto en backend.

Historial de compras por usuario.

Listado de sucursales conectadas a un microservicio.

Consumo de API externa CheapShark (ofertas gamer reales).

Pruebas unitarias con MockK y TestDispatcher.

Generación de APK firmado (.jks).

📌 Microservicios (Spring Boot)

Microservicio de Productos (GET, PUT)

Microservicio de Usuarios (GET, POST, PUT)

Microservicio de Compras (POST, GET)

Microservicio de Sucursales (GET)

🌐 4. Endpoints Utilizados
A. API Externa – CheapShark
Función	Método	Endpoint
Obtener ofertas gamer	GET	https://www.cheapshark.com/api/1.0/deals

Uso en Retrofit:

@GET("deals")
suspend fun getTopDeals(
    @Query("upperPrice") upperPrice: String = "25",
    @Query("sortBy") sortBy: String = "DealRating"
): List<GameDealDto>

B. Microservicios Propios (Spring Boot)
🛒 Productos
GET  /api/productos
PUT  /api/productos/{codigo}

👤 Usuarios
GET  /api/usuarios/email/{email}
POST /api/usuarios
PUT  /api/usuarios/{id}

🧾 Compras
POST /api/compras
GET  /api/compras/usuario/{email}

🏬 Sucursales
GET /api/sucursales

▶️ 5. Pasos para Ejecutar el Proyecto
A. Ejecutar los microservicios

Abrir cada microservicio en IntelliJ IDEA / STS.

Configurar la conexión a MySQL en application.properties.

Ejecutar cada proyecto con Spring Boot:

mvn spring-boot:run

B. Ejecutar la aplicación móvil

Abrir la carpeta mobile-app en Android Studio.

Asegurar dependencias sincronizadas (Gradle Sync).

Configurar IP local para llamadas a microservicios:

http://10.0.2.2:8081


Ejecutar en:

Emulador Android

Dispositivo físico (modo desarrollador activado)

C. Generar el APK Firmado

Ir a Build → Generate Signed APK

Crear o seleccionar tu archivo .jks

Completar los campos de clave

Seleccionar release

Generar APK final

El archivo quedará en:

/app/release/app-release.apk

📸 6. Captura del APK Firmado y Archivo .jks



(Puedes agregar estas imágenes desde tu carpeta images/ dentro del repositorio.)

🧪 Bonus: Pruebas Unitarias

Lógica del carrito

Cálculo del total

Interacción con repositorios mockeados

Uso de MainDispatcherRule

Cobertura superior al 80%

./gradlew test

📂 Estructura General del Proyecto
mobile-app/
 ├── ui/
 ├── viewmodel/
 ├── data/
 │    ├── repository/
 │    ├── remote/
 │    └── model/
 ├── tests/
backend/
 ├── productos-ms/
 ├── usuarios-ms/
 ├── compras-ms/
 └── sucursales-ms/
