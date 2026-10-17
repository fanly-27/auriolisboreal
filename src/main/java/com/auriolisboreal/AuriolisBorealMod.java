package com.auriolisboreal;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = AuriolisBorealMod.MODID, name = AuriolisBorealMod.NAME, version = AuriolisBorealMod.VERSION)
public class AuriolisBorealMod {
    public static final String MODID = "auriolisboreal";
    public static final String NAME = "Auriolis Boreal";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Registra bloques, items y configuración aquí
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Configura recetas y handlers aquí
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Compatibilidad con otros mods
    }
}
