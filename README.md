# Mis tareas

Prototipo Android en Kotlin para anotar pendientes sin cuenta ni conexión a internet.

## Funciones

- Lista principal con descripción, estado y fecha de creación (`ListView` + `TaskAdapter`).
- Pantalla para agregar tareas con validación de descripción.
- Pantalla de detalle que recibe el ID de la tarea mediante `Intent`.
- Casilla para completar o reabrir una tarea; las completadas se muestran tachadas.
- Eliminación desde el detalle con confirmación.
- Persistencia local con `SQLiteOpenHelper` en `tasks.db`.

## Ejecutar

1. Abre esta carpeta en Android Studio.
2. Espera a que termine la sincronización de Gradle.
3. Ejecuta la configuración `app` en un teléfono Android (API 24 o superior) o un emulador.

La app no solicita permiso de internet ni usa backend. Los datos quedan en el dispositivo hasta desinstalar la app o borrar sus datos.

## Guion para el video de entrega

Graba la pantalla del teléfono o emulador mientras haces lo siguiente:

1. Abre la app y muestra la lista vacía.
2. Agrega una tarea y enseña que aparece en la lista.
3. Toca la tarea y muestra descripción, estado y fecha en el detalle.
4. Márcala como completada y vuelve a la lista para enseñar el tachado.
5. Cierra y vuelve a abrir la app para demostrar persistencia.
6. Entra al detalle, elimina la tarea y confirma que ya no aparece.
