package com.auriolisboreal;

import com.auriolisboreal.tileentity.TileEntityArborisAura;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = AuriolisBorealMod.MODID, name = AuriolisBorealMod.NAME, version = AuriolisBorealMod.VERSION)
public class AuriolisBorealMod {
    public static final String MODID = "auriolisboreal";
    public static final String NAME = "Auriolis Boreal";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ArborisAuraConfig.load(event.getSuggestedConfigurationFile());
        GameRegistry.registerTileEntity(
                TileEntityArborisAura.class,
                new ResourceLocation(MODID, "arboris_aura")
        );
    }
}
