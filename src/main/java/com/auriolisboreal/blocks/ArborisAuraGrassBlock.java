package com.auriolisboreal.blocks;

import com.auriolisboreal.ArborisAuraConfig;
import com.auriolisboreal.AuriolisBorealMod;
import com.auriolisboreal.MicroBiome;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class ArborisAuraGrassBlock extends Block {
    public ArborisAuraGrassBlock() {
        super(Material.GRASS);
        setTranslationKey("arboris_aura_grass");
        setRegistryName(new ResourceLocation(AuriolisBorealMod.MODID, "arboris_aura_grass"));
        setHardness(0.6F);
        setSoundType(SoundType.PLANT);
        setHarvestLevel("shovel", 0);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.DIRT);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return ArborisAuraConfig.getGrassBlockLight();
    }

    @Override
    public boolean onBlockActivated(
            World world,
            BlockPos pos,
            IBlockState state,
            EntityPlayer player,
            EnumHand hand,
            EnumFacing facing,
            float hitX,
            float hitY,
            float hitZ
    ) {
        return MicroBiome.interact(world, pos, player, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
