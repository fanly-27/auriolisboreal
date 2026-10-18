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
- El terminal del entorno usa Java 8 (`1.8.0_504`). El log de la extensión
  Gradle de VS Code mostró un proceso iniciado con su JRE 25; con Gradle 4.10.3
  falló el acceso a `URLClassLoader.addURL`. Para compilar, usar el wrapper desde
  el terminal con JDK 8.
- `gradle.properties` establece `org.gradle.jvmargs=-Xmx2G`, lo cual causa que
  `--no-daemon` use un proceso temporal. Para evitarlo sin cambiar ese archivo:

  ```sh
  export JAVA_HOME=/ruta/al/jdk8
  export PATH="$JAVA_HOME/bin:$PATH"
  ./gradlew --no-daemon -Dorg.gradle.jvmargs= compileJava
  ```

- El comando anterior terminó con `BUILD SUCCESSFUL`. Se verificó `compileJava`,
  no el build completo ni el JAR sombreado. Revisar una ejecución de Actions
  para confirmar el artefacto.
- Las texturas PNG de los ejemplos no están incluidas.

## Al continuar

Definir qué contenido debe añadir el mod. Después, implementar los cambios en
`src/main/java/` y/o `src/main/resources/`, compilar con Java 8 o revisar el
artefacto de GitHub Actions y probarlo en Minecraft 1.12.2 con Forge.
