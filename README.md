# Auriolis Boreal - Starter mod (extended)

He añadido una plantilla mínima lista para que empieces a programar funcionalidad del mod sin necesidad inmediata de herramientas de compilación.

Archivos añadidos:
- src/main/java/com/auriolisboreal/blocks/ExampleBlock.java — ejemplo de bloque.
- src/main/java/com/auriolisboreal/items/ExampleItem.java — ejemplo de item.
- src/main/java/com/auriolisboreal/ModBlocks.java — registro de bloques.
- src/main/java/com/auriolisboreal/ModItems.java — registro de items.
- src/main/java/com/auriolisboreal/proxy/CommonProxy.java — proxy común.
- src/main/java/com/auriolisboreal/proxy/ClientProxy.java — proxy cliente (registra modelos).
- src/main/java/com/auriolisboreal/AuriolisBorealMod.java — clase principal del mod (actualizada para inicializar registros y proxy).
- src/main/resources/assets/auriolisboreal/lang/en_US.lang — nombres básicos.
- src/main/resources/assets/auriolisboreal/blockstates/example_block.json
- src/main/resources/assets/auriolisboreal/models/block/example_block.json
- src/main/resources/assets/auriolisboreal/models/item/example_block.json
- src/main/resources/assets/auriolisboreal/models/item/example_item.json

Notas importantes:
- Las texturas (PNG) NO están incluidas. Pon las imágenes aquí cuando puedas:
  - src/main/resources/assets/auriolisboreal/textures/blocks/example_block.png
  - src/main/resources/assets/auriolisboreal/textures/items/example_item.png

Cómo continuar sin compilar:
- Puedes editar y añadir código en src/ normalmente desde la web o desde un editor. Cuando tengas una PC con Java/Gradle/Forge MDK, te puedo añadir build.gradle y el wrapper para compilar.

Si quieres que prepare también el build.gradle y GitHub Actions para compilar automáticamente (generar jar en cada push), dímelo y lo agrego.
