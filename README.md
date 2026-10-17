# Auriolis Boreal

Plantilla de mod para **Minecraft 1.12.2** y **Forge 14.23.5.2847**. Aún no
añade contenido propio al juego; incluye ejemplos básicos de un bloque y un
objeto.

## Compilar automáticamente

Al subir cambios a GitHub, la acción **Build Minecraft mod** compila el proyecto
con Java 8. También se puede iniciar manualmente desde la pestaña **Actions**,
eligiendo el workflow y pulsando **Run workflow**.

Para descargar el mod compilado:

1. Abre **Actions** en el repositorio.
2. Selecciona una ejecución que haya terminado correctamente.
3. Descarga el artefacto `auriolisboreal-...` al final de la página.
4. Extrae el `.jar` y colócalo en la carpeta `mods` de una instalación de
   Minecraft 1.12.2 con Forge.

## Compilar en tu computadora

Se necesita **JDK 8**. El wrapper incluido descarga y usa Gradle 4.10.3; no
instales ni configures Gradle por separado.

En Linux o macOS, desde la carpeta del proyecto:

```sh
./gradlew build
```

En Windows:

```bat
gradlew.bat build
```

El archivo compilado queda en `build/libs/`. Para probar el mod desde el entorno
de desarrollo, usa `./gradlew runClient` (o `gradlew.bat runClient` en Windows).

ForgeGradle 2.3 para Minecraft 1.12.2 no funciona correctamente con Java moderno:
usa Java 8 tanto para compilar como para ejecutar el cliente.

## Mixin (pausado)

Mixin y MixinGradle están desactivados y no forman parte de la compilación actual.
Se podrán volver a integrar cuando el mod necesite mixins; habrá que añadir la
dependencia y el plugin, crear su configuración y declarar el tweaker en el
manifiesto del JAR.

Para bloques, objetos, recetas y eventos normales de Forge, generalmente no hace
falta escribir mixins.

## Archivos y ejemplos

- Código Java: `src/main/java/com/auriolisboreal/`
- Modelos, blockstates e idioma: `src/main/resources/assets/auriolisboreal/`

Las texturas de ejemplo aún no están incluidas. Se esperan en:

- `src/main/resources/assets/auriolisboreal/textures/blocks/example_block.png`
- `src/main/resources/assets/auriolisboreal/textures/items/example_item.png`
