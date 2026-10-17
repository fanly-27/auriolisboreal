package com.auriolisboreal;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import com.auriolisboreal.proxy.CommonProxy;

@Mod(modid = AuriolisBorealMod.MODID, name = AuriolisBorealMod.NAME, version = AuriolisBorealMod.VERSION)
public class AuriolisBorealMod {
    public static final String MODID = "auriolisboreal";
    public static final String NAME = "Auriolis Boreal";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.auriolisboreal.proxy.ClientProxy", serverSide = "com.auriolisboreal.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModBlocks.init();
        ModItems.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerModels();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // post init
    }
}
