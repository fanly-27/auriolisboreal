# Notas del proyecto

## Objetivo actual

Preparar una plantilla base para un mod de Minecraft 1.12.2 con Forge. El
contenido y las mecánicas del mod todavía están por decidir; por ahora se
conservan el bloque y el objeto de ejemplo. No hay una integración que aplique
cambios de forma automática.

## Herramientas y estado

- Minecraft 1.12.2, Forge 14.23.5.2847 y Java 8.
- ForgeGradle 2.3 y Gradle Wrapper 4.10.3 (`gradlew` / `gradlew.bat`).
- Mixin y MixinGradle están pausados: no se incluyen como dependencias, plugins
  ni configuración del manifiesto. Reintegrarlos cuando el mod necesite mixins.
- GitHub Actions está configurado en `.github/workflows/build.yml` para compilar
  con Java 8 y publicar el JAR como artefacto de la ejecución.
- Las instrucciones de compilación y descarga están en `README.md`.

## Compilación y límites verificados

- La compilación actual no carga ni depende de Mixin.
- No se pudo verificar el build completo en el entorno de desarrollo actual:
  tiene Java 25, y Forge 1.12.2 necesita Java 8. El build local falló al usar la
  API `Pack200`, retirada de Java moderno.
- El workflow de GitHub usa Java 8; hay que subir los cambios al repositorio y
  comprobar una ejecución de Actions para confirmar el JAR.
- Las texturas PNG de los ejemplos no están incluidas.

## Al continuar

Definir qué contenido debe añadir el mod. Después, implementar los cambios en
`src/main/java/` y/o `src/main/resources/`, compilar con Java 8 o revisar el
artefacto de GitHub Actions y probarlo en Minecraft 1.12.2 con Forge.
