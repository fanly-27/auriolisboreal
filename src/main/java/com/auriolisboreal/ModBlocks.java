package com.auriolisboreal;

import com.auriolisboreal.blocks.ArborisAuraBlock;
import com.auriolisboreal.blocks.ArborisAuraLeavesBlock;
import com.auriolisboreal.blocks.ArborisAuraTrunkBlock;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = AuriolisBorealMod.MODID)
public class ModBlocks {
    public static ArborisAuraBlock arboris_aura;
    public static ArborisAuraTrunkBlock arboris_aura_trunk;
    public static ArborisAuraLeavesBlock arboris_aura_leaves;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        arboris_aura = new ArborisAuraBlock();
        arboris_aura_trunk = new ArborisAuraTrunkBlock();
        arboris_aura_leaves = new ArborisAuraLeavesBlock();
        event.getRegistry().register(arboris_aura);
        event.getRegistry().register(arboris_aura_trunk);
        event.getRegistry().register(arboris_aura_leaves);
    }
}
