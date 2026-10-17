package com.auriolisboreal;

import com.auriolisboreal.blocks.ExampleBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static Block example_block;

    public static void init() {
        example_block = new ExampleBlock();
        GameRegistry.register(example_block);
        GameRegistry.register(new ItemBlock(example_block).setRegistryName(example_block.getRegistryName()));
    }
}
