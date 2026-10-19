package com.auriolisboreal;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = AuriolisBorealMod.MODID)
public class ModItems {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        registerBlockItem(event, ModBlocks.arboris_aura);
        registerBlockItem(event, ModBlocks.arboris_aura_trunk);
        registerBlockItem(event, ModBlocks.arboris_aura_leaves);
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        event.getRegistry().register(itemBlock);
    }
}
