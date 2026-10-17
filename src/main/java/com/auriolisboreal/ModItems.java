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
        Block auraBlock = ModBlocks.arboris_aura;
        ItemBlock auraItemBlock = new ItemBlock(auraBlock);
        auraItemBlock.setRegistryName(auraBlock.getRegistryName());
        event.getRegistry().register(auraItemBlock);
    }
}
