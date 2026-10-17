package com.auriolisboreal.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import com.auriolisboreal.ModBlocks;
import com.auriolisboreal.ModItems;
import com.auriolisboreal.AuriolisBorealMod;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = AuriolisBorealMod.MODID)
public class ClientProxy {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        Item blockItem = Item.getItemFromBlock(ModBlocks.example_block);
        ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(blockItem.getRegistryName(), "inventory"));

        Item exampleItem = ModItems.example_item;
        ModelLoader.setCustomModelResourceLocation(exampleItem, 0, new ModelResourceLocation(exampleItem.getRegistryName(), "inventory"));
    }
}
