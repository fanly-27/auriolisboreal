package com.auriolisboreal;

import com.auriolisboreal.blocks.ExampleBlock;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = AuriolisBorealMod.MODID)
public class ModBlocks {
    public static Block example_block;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        example_block = new ExampleBlock();
        event.getRegistry().register(example_block);
    }
}
