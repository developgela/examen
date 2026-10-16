🎬 TV Show Management API (Examen)

Esta es una API REST desarrollada con **Java 21** y **Spring Boot** orientada al consumo, almacenamiento en caché y enriquecimiento de datos de series de televisión utilizando la API externa de **TVMaze** acoplada a una persistencia distribuida en **MongoDB Atlas**.

---

## 🚀 Requisitos del Entorno
* **Entorno de Ejecución:** Java 21 o superior.
* **Gestor de Dependencias:** Maven 3.x.
* **Base de Datos:** Cuenta e instancia activa en la nube de MongoDB Atlas.

---

## ⚙️ Configuración del Sistema
La conexión a la base de datos distribuida en la nube se realiza dentro del archivo de propiedades (`application.yml` o `application.properties`). De acuerdo con las instrucciones de la evaluación, la instancia de MongoDB Atlas fue configurada con **acceso libre de IP (`0.0.0.0/0`)** para garantizar el correcto despliegue y pruebas sin restricciones de red.

---

## 📐 Desarrollo de la Solución (Punto por Punto)

### 🔹 Fase 1: Endpoint Search (`GET /api/v1/shows/search`) esta construido
* **Objetivo:** Permitir la localización de shows mediante un criterio de búsqueda textual.
* **Flujo Interno:**
    1. Recibe el parámetro del cliente.
    2. Consulta el catálogo de la API externa de TVMaze (`/search/shows?q={query}`).
    3. Procesa y transforma el JSON recibido para extraer de manera inteligente y prioritaria el canal de transmisión (`network_name` o `webchannel_name`).
* **Información que retorna:** Un arreglo estructurado con los atributos básicos del show (`id`, `name`, `channel`, `summary`, `genres`).

### 🔹 Fase 2: Endpoint Show por ID con Estrategia de Caché (`GET /api/v1/shows/{show_id}`) solo la primera parte, segunda no esta constrtuido del cache
* **Objetivo:** Consultar la información detallada de una serie optimizando las peticiones externas.
* **Estrategia Cache-Aside implementada:**
    * **Paso A (Validación):** Previo a consumir cualquier servicio externo, el sistema inspecciona la colección de caché dentro de MongoDB Atlas usando el identificador enviado.
    * **Paso B (Caché Hit):** Si el registro ya existe en MongoDB, se recupera el objeto local de forma inmediata, evitando llamadas HTTP innecesarias.
    * **Paso C (Caché Miss):** Si el ID no existe en la base de datos, se consume la API original de TVMaze (`/shows/{show_id}`), se registra la respuesta completa de manera persistente en MongoDB para futuras consultas y finalmente se entrega la información.

### 🔹 Fase 3: Endpoint Comments (`POST /api/v1/shows/comments`)  construido rango numérico de **0 a 5**
* **Objetivo:** Permitir a los usuarios calificar y dejar sus opiniones sobre los shows.
* **Flujo Interno:**
    1. Recibe un objeto con los datos de auditoría.
    2. Valida mediante filtros del framework que la calificación (`rating`) se encuentre estrictamente en el rango numérico de **0 a 5** y que el comentario no esté vacío.
    3. Almacena de manera permanente la opinión ligándola de forma indexada al identificador numérico de la serie.
* **Información que retorna:** El código de estado HTTP correspondiente y un objeto con el estatus de éxito de la petición.

### 🔹 Fase 4: Enriquecimiento de Datos Post-Consulta
Para consolidar las vistas de cara al usuario final, se modificaron los contratos de los endpoints principales bajo la siguiente lógica de acoplamiento:
* **Modificación en Search:** Posterior a la consulta del API externa, el servicio ejecuta un proceso de cruce de datos (*Join* en memoria) con MongoDB Atlas para inyectar a cada show del listado un arreglo interno llamado `comments` con todas sus calificaciones históricas.
* **Modificación en Show por ID:** Justo antes de despachar la respuesta al cliente (ya sea que provenga de la API o del caché), el sistema adjunta de manera dinámica el bloque de opiniones correspondientes al final del objeto del show completo.

Para mas detalle se dejo un comentarios en los flujos para cada caso
