# Auriolis Boreal

Plantilla de mod para Minecraft 1.12.2 con Forge 14.23.5.2847 y soporte de
Mixin 0.8.2.

## Requisitos

- JDK 8 (no basta con tener solo un JRE).
- Gradle 4.10.3; se recomienda usar el Gradle Wrapper incluido para no depender
  de la versión global instalada.

ForgeGradle 2.3 y Minecraft 1.12.2 no son compatibles con las versiones modernas
de Java y Gradle. Si el build falla con otra versión, comprueba primero `java
-version` y `./gradlew --version`.

## Preparar y ejecutar

Desde la raíz del proyecto:

```sh
./gradlew setupDecompWorkspace
./gradlew runClient
```

En Windows, usa `gradlew.bat` en lugar de `./gradlew`. Para compilar el mod:

```sh
./gradlew build
```

El JAR compilado se genera en `build/libs/`.

## Mixin

MixinGradle genera el refmap al compilar. La configuración declarada en
`src/main/resources/mixins.auriolisboreal.json` se carga desde el manifiesto del
JAR. La configuración no contiene mixins todavía; cuando se añadan, deben
ubicarse bajo `com.auriolisboreal.mixins` y declararse en el arreglo adecuado
(`mixins`, `client` o `server`). No elimines la configuración de Mixin si el mod
necesita transformar clases de Minecraft o Forge.

Para contenido normal de Forge (bloques, ítems, recetas, eventos), normalmente
no se necesitan mixins.

## Estructura de ejemplo

- `src/main/java/com/auriolisboreal/`: código Java del mod.
- `src/main/resources/assets/auriolisboreal/`: modelos, blockstates y nombres.
- `src/main/resources/mixins.auriolisboreal.json`: configuración de Mixin.

Los modelos del ejemplo esperan texturas que aún no están incluidas:

- `src/main/resources/assets/auriolisboreal/textures/blocks/example_block.png`
- `src/main/resources/assets/auriolisboreal/textures/items/example_item.png`

Claude puede generar código o recursos para añadir a estas rutas, pero sus
respuestas no se aplican automáticamente al repositorio: hay que guardar cada
cambio en el archivo indicado y luego compilar con el wrapper.
