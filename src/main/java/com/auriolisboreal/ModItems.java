package com.auriolisboreal;

import com.auriolisboreal.items.ExampleItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = AuriolisBorealMod.MODID)
public class ModItems {
    public static Item example_item;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Block block = ModBlocks.example_block;
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        event.getRegistry().register(itemBlock);

        example_item = new ExampleItem();
        event.getRegistry().register(example_item);
    }
}
