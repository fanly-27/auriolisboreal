package com.auriolisboreal;

import com.auriolisboreal.blocks.ArborisAuraBlock;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = AuriolisBorealMod.MODID)
public class ModBlocks {
    public static ArborisAuraBlock arboris_aura;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        arboris_aura = new ArborisAuraBlock();
        event.getRegistry().register(arboris_aura);
    }
}
