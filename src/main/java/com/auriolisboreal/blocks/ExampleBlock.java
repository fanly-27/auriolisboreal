package com.auriolisboreal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import com.auriolisboreal.AuriolisBorealMod;

public class ExampleBlock extends Block {
    public ExampleBlock() {
        super(Material.WOOD);
        setUnlocalizedName("example_block");
        setRegistryName(AuriolisBorealMod.MODID, "example_block");
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.WOOD);
    }
}
