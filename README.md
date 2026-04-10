# 🧳 Compartrip

> Aplicación Android nativa para compartir viajes entre particulares — transporte, alojamiento o ambos.

**Alumna:** Nora Bedoya García  
**Ciclo:** Desarrollo de Aplicaciones Multiplataforma (DAM) — 2º Curso  
**Centro:** IES Isidra de Guzmán  
**Curso:** 2025 / 2026

---

## 📱 ¿Qué es Compartrip?

Compartrip conecta viajeros que quieren compartir su viaje, ya sea el transporte, el alojamiento, o ambos. A diferencia de plataformas como BlaBlaCar (solo trayecto) o Airbnb (solo alojamiento), Compartrip integra ambas dimensiones en una sola app orientada a viajes particulares entre personas.

**Flujo básico:**
1. Un usuario publica su viaje indicando qué quiere compartir (transporte / alojamiento / completo)
2. Otros usuarios buscan con filtros y solicitan unirse
3. El publicante acepta o rechaza la solicitud
4. Ambos se comunican por el chat interno en tiempo real

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Kotlin | 2.0.21 | Lenguaje principal |
| Android Studio Narwhal | 2025.1.4 | IDE de desarrollo |
| Jetpack Compose | BOM 2025.05.00 | UI declarativa (sin XML) |
| Material Design 3 | — | Sistema de diseño |
| Navigation Compose | 2.9.0 | Navegación entre pantallas |
| Room ORM | 2.7.1 | Base de datos local |
| Retrofit2 | 2.11.0 | Consumo de API REST |
| OkHttp | 4.12.0 | Cliente HTTP y WebSockets |
| Auth0 Android | 2.10.2 | Autenticación de usuarios |
| Hilt | 2.56.2 | Inyección de dependencias |
| Coroutines | 1.9.0 | Programación asíncrona |
| Coil | 2.7.0 | Carga de imágenes |
| KSP | 2.0.21-1.0.28 | Procesador de código (Room + Hilt) |

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVVM** con separación clara de capas:

```
com.bedoya.compartrip/
├── data/
│   ├── local/
│   │   ├── entity/        ← Entidades Room (tablas BD)
│   │   ├── dao/           ← Interfaces DAO (consultas)
│   │   └── database/      ← CompartripBaseDatos
│   ├── remote/
│   │   ├── api/           ← ServicioApi (Retrofit)
│   │   └── dto/           ← DTOs de la API
│   └── repository/        ← Repositorios (fuente única de datos)
├── domain/
│   ├── model/             ← Modelos de dominio + mapeadores
│   └── usecase/           ← Casos de uso
├── ui/
│   ├── screens/           ← Pantallas Compose + ViewModels
│   ├── components/        ← Componentes reutilizables
│   ├── navigation/        ← GrafoNavegacion + Destinos
│   └── theme/             ← Color, Typography, Theme
└── di/                    ← Módulos Hilt
```

**Flujo de datos:**
```
Pantalla → ViewModel → Caso de uso → Repositorio → Room / Retrofit
```

---

## 🗄️ Base de datos

La app usa **Room ORM** con 5 entidades:

| Entidad | Tabla | Descripción |
|---|---|---|
| `EntidadUsuario` | `usuarios` | Perfiles de usuarios |
| `EntidadViaje` | `viajes` | Viajes publicados |
| `EntidadReserva` | `reservas` | Solicitudes de unirse |
| `EntidadResena` | `resenas` | Valoraciones entre usuarios |
| `EntidadMensaje` | `mensajes` | Mensajes del chat |

---

## 📲 Pantallas

| Pantalla | Descripción |
|---|---|
| Login | Autenticación con Auth0 |
| Home | Lista de viajes con búsqueda y filtros |
| Detalle | Vista completa de un viaje |
| Publicar viaje | Formulario para publicar un viaje |
| Perfil | Información del usuario autenticado |
| Mensajes | Chat en tiempo real (WebSockets) |

---

## 🚀 Instalación y ejecución

### Requisitos
- Android Studio Narwhal (2025.1.4) o superior
- JDK 11
- Android SDK API 26+
- Conexión a internet (para Auth0 y API)

### Pasos
1. Clona el repositorio:
```bash
git clone https://github.com/nbg94/ComparTrip.git
```
2. Abre el proyecto en Android Studio
3. Espera a que Gradle sincronice las dependencias
4. Ejecuta en un emulador o dispositivo físico con **Shift + F10**

---

## 🔐 Credenciales de prueba

### Auth0
Puedes crear una cuenta nueva desde la pantalla de login o usar estas credenciales de prueba:

| Campo | Valor |
|---|---|
| Email | `test@compartrip.com` |
| Contraseña | `Test1234!` |

### API REST
La app usa **JSONPlaceholder** (`https://jsonplaceholder.typicode.com`) como API de pruebas.  
No requiere credenciales.

---

## 📋 Requisitos técnicos implementados

### PMDM
- ✅ UI completa en Jetpack Compose (sin XML)
- ✅ Navegación con Navigation Compose
- ✅ Mínimo 4 pantallas diferentes
- ✅ Estados reactivos con StateFlow
- ✅ Componentes reutilizables (TarjetaViaje, BarraFiltros)
- ✅ Tema personalizado Material Design 3
- ✅ Listas con LazyColumn
- ✅ Formularios con validación
- ✅ Arquitectura MVVM con separación de capas
- ✅ ViewModels con StateFlow
- ✅ Repository pattern
- ✅ Coroutines para operaciones asíncronas
- ✅ Room con 5 entidades y DAOs CRUD
- ✅ Flow para observar cambios en BD

### PSP
- ✅ Autenticación con Auth0
- ✅ Manejo de tokens JWT
- ✅ Logout funcional
- ✅ Integración con API REST (Retrofit2)
- ✅ Mínimo 2 endpoints (GET /posts, GET /posts/{id})
- ✅ Serialización JSON con Gson
- ✅ Manejo de errores de red

---

## 📅 Entregas

| Entrega | Fecha | Estado |
|---|---|---|
| Entrega 1 — Diseño y análisis | 13/03/2026 | ✅ Entregada |
| Entrega 2 — Versión funcional inicial | 10/04/2026 | ✅ Entregada  |
| Entrega 3 — Versión intermedia | 24/04/2026 | ⏳ Pendiente |
| Entrega 4 — Entrega completa | 08/05/2026 | ⏳ Pendiente |
| Depósito final | 15/05/2026 | ⏳ Pendiente |

---

## 📚 Bibliografía

- [Jetpack Compose — Google Developers](https://developer.android.com/compose)
- [Room ORM — Google Developers](https://developer.android.com/training/data-storage/room)
- [Navigation Compose](https://developer.android.com/guide/navigation/navigation-compose)
- [Retrofit2 — Square](https://square.github.io/retrofit/)
- [Auth0 Android SDK](https://auth0.com/docs/quickstart/native/android)
- [Hilt — Inyección de dependencias](https://developer.android.com/training/dependency-injection/hilt-android)
- [Material Design 3](https://m3.material.io/)
- [OkHttp — Square](https://square.github.io/okhttp/)
- [Coil — Carga de imágenes](https://coil-kt.github.io/coil/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
