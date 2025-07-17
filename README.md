# 🧠 Arquitectura del Proyecto — Domain-Driven Design (DDD) + SOLID

Este proyecto está diseñado siguiendo los principios de **Domain-Driven Design (DDD)** y los principios **SOLID**, con el objetivo de lograr una arquitectura **modular, escalable y de alta cohesión**.

---

## 📐 Capas de Arquitectura

La arquitectura se divide en cinco capas principales, cada una con responsabilidades claras y desacopladas.

---

### 1. `Interface`
Responsabilidad: Entrada del sistema (también conocida como capa de orquestación o adaptadores primarios).

- Recibe peticiones externas (HTTP, CLI, etc).
- Orquesta la ejecución de los casos de uso.
- Interpreta y traduce inputs/outputs (DTOs).
- Aplica validaciones iniciales y seguridad (auth, permisos).

**Incluye:**
- `Rest Controllers`
- `CLI Commands`
- `Security`
- `Request/Response Patterns`

---

### 2. `Domain`
Responsabilidad: Contiene la lógica de negocio pura, independiente de frameworks y detalles técnicos.

- Modela el comportamiento central del sistema.
- Define reglas, validaciones, políticas y flujos de negocio.
- No debe tener dependencias de infraestructura.

**Incluye:**
- `Entities`
- `Aggregates`
- `Value Objects`
- `Domain Services`
- `Repository Interfaces`
- `Use Cases`
- `Ports (Interfaces)`
- `Domain Patterns`

---

### 3. `Infrastructure`
Responsabilidad: Implementar los detalles técnicos y externos al dominio.

- Comunicación con bases de datos, colas de mensajería, APIs externas.
- Implementa los contratos definidos por el dominio.

**Incluye:**
- `Database Repositories`
- `Kafka Listeners / Consumers`
- `Service Clients`
- `Infrastructure Patterns`

---

### 4. `Shared`
Responsabilidad: Compartir código genérico, reusable y transversal que **no pertenece a un solo bounded context**.

- Evita duplicación.
- Mejora la consistencia entre contextos.
- Estándar para aspectos comunes.

**Incluye:**
- `Page Results`
- `Shared Objects`
- `Common Mappers`
- `Helpers` (logs, fechas, tokens, etc.)

---

## 📚 Principios SOLID aplicados

Esta arquitectura refuerza los principios SOLID en todo el diseño:

| Principio | Aplicación |
|----------|------------|
| **S - Single Responsibility** | Cada clase o componente tiene una única responsabilidad (ej. UseCases vs Controllers vs Repositories). |
| **O - Open/Closed** | Las reglas de negocio están abiertas a extensión (nuevos casos) pero cerradas a modificación directa. |
| **L - Liskov Substitution** | Las implementaciones de interfaces del dominio pueden sustituirse sin romper la lógica (ej. repositorios). |
| **I - Interface Segregation** | Las interfaces están enfocadas (ej. repositorios no mezclan responsabilidades de mensajería). |
| **D - Dependency Inversion** | El dominio depende de abstracciones, no de detalles técnicos (ej. interfaces de puertos). |

---

## 🧬 Beneficios de esta arquitectura

- ✅ Alta cohesión y bajo acoplamiento
- ✅ Fácil de testear, extender y mantener
- ✅ Independencia de frameworks
- ✅ Código alineado con el **ubiquitous language**
- ✅ Escalable para sistemas distribuidos y microservicios

---

## 🗂️ Diagrama General

![Arquitectura DDD](./arquitectura.png)

---

¿Preguntas o contribuciones? ¡Sientete libre de abrir un issue o PR!