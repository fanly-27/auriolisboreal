package com.auriolisboreal.proxy;

import com.auriolisboreal.AuriolisBorealMod;
import com.auriolisboreal.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = AuriolisBorealMod.MODID)
public class ClientProxy {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerItemModel(ModBlocks.arboris_aura);
        registerItemModel(ModBlocks.arboris_aura_trunk);
        registerItemModel(ModBlocks.arboris_aura_leaves);
        registerItemModel(ModBlocks.arboris_aura_sapling);
        registerItemModel(ModBlocks.arboris_aura_grass);
        registerItemModel(ModBlocks.arboris_aura_tallgrass);
    }

    private static void registerItemModel(Block block) {
        Item item = Item.getItemFromBlock(block);
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
