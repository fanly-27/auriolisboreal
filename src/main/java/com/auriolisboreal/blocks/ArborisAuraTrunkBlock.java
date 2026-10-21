package com.auriolisboreal.blocks;

import com.auriolisboreal.AuriolisBorealMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ArborisAuraTrunkBlock extends Block {
    public ArborisAuraTrunkBlock() {
        super(Material.WOOD);
        setTranslationKey("arboris_aura_trunk");
        setRegistryName(new ResourceLocation(AuriolisBorealMod.MODID, "arboris_aura_trunk"));
        setHardness(ArborisAuraConfig.getTrunkHardness());
        setResistance(ArborisAuraConfig.getTrunkResistance());
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 5;
    }
}
