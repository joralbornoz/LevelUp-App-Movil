# 🎮 LevelUp App – Tienda Gamer

**LevelUp** es una aplicación móvil desarrollada en **Kotlin + Jetpack Compose**, inspirada en una tienda gamer moderna.  
Permite gestionar productos, carrito de compras, perfil de usuario y visualizar sucursales en un mapa.

---

## 👥 Equipo de Desarrollo

- **Jorge Albornoz** – Desarrollador
- **Paloma Fuente** – Desarrollador
    
- Proyecto académico desarrollado para **Duoc UC**, en la asignatura de **Desarrollo de Aplicaciones Móviles**.

---

## 💡 Descripción del Proyecto

LevelUp App busca ofrecer una experiencia de compra gamer sencilla, visualmente atractiva y adaptada a dispositivos Android.  
Incluye pantallas conectadas por navegación y almacenamiento local de datos del usuario mediante **DataStore**.

---

## 🚀 Funcionalidades Principales

🔐 Login validado	Acceso con validación de datos.

🏠 Menú principal	Navegación entre todas las secciones.

🕹️ Gestión de productos	Muestra catálogo con nombre, precio y descripción, permitiendo agregar al carrito.

🛒 Gestión de carrito	Agrega, limpia y calcula el total + IVA.

🧍‍♂️ Perfil de usuario	Guarda nombre, email y dirección con persistencia local (DataStore).

🏬 Sucursales	Muestra dirección, teléfono y acceso nativo a mapa y llamada.

🎨 Diseño Material 3	Interfaz moderna con tema oscuro y acentos en verde neón.

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin  
- **Framework:** Jetpack Compose (Material 3)  
- **Arquitectura:** MVVM (ViewModel + DataStore)  
- **Navegación:** Navigation Compose  
- **Almacenamiento local:** DataStore
- **IDE:** Android Studio  
- **Versión mínima de SDK:** 26 (Android 8.0 Oreo)

---

## 📂 Estructura del Proyecto

<img width="355" height="745" alt="image" src="https://github.com/user-attachments/assets/8e24d1dc-5f86-4026-ae95-1d0a4e3eb74b" />

---

💻 1. Clonar el repositorio
git clone https://github.com/joralbornoz/LevelUpApp.git
cd LevelUpApp

🧩 2. Abrir en Android Studio

Abre Android Studio.

Ve a Archivo → Open Project...

Selecciona la carpeta del proyecto (LevelUpApp).

Espera a que Gradle sincronice todas las dependencias necesarias.

📱 3. Ejecutar en un emulador o dispositivo físico

Conecta tu teléfono en modo depuración USB,
o crea un emulador Android desde el AVD Manager.

En la barra superior de Android Studio, selecciona el dispositivo donde correrá la app.

Haz clic en Run ▶️ para compilar y ejecutar la aplicación.
