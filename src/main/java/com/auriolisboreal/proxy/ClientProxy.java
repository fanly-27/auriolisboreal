package com.auriolisboreal.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.auriolisboreal.ModBlocks;
import com.auriolisboreal.ModItems;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void registerModels() {
        // Register block item model
        Item blockItem = Item.getItemFromBlock(ModBlocks.example_block);
        if (blockItem != null && blockItem.getRegistryName() != null) {
            ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(blockItem.getRegistryName(), "inventory"));
        }

        // Register example item model
        Item exampleItem = ModItems.example_item;
        if (exampleItem != null && exampleItem.getRegistryName() != null) {
            ModelLoader.setCustomModelResourceLocation(exampleItem, 0, new ModelResourceLocation(exampleItem.getRegistryName(), "inventory"));
        }
    }
}
