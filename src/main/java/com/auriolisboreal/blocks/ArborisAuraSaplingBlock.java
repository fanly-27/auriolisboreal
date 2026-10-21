package com.auriolisboreal.blocks;

import com.auriolisboreal.ArborisAuraConfig;
import com.auriolisboreal.AuriolisBorealMod;
import com.auriolisboreal.ModBlocks;
import com.auriolisboreal.tileentity.TileEntityArborisSapling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ArborisAuraSaplingBlock extends BlockBush {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);

    public ArborisAuraSaplingBlock() {
        super(Material.PLANTS);
        setTranslationKey("arboris_aura_sapling");
        setRegistryName(new ResourceLocation(AuriolisBorealMod.MODID, "arboris_aura_sapling"));
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return ArborisAuraConfig.getSaplingLight();
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        Block block = state.getBlock();
        return block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.FARMLAND
                || block == ModBlocks.arboris_aura_grass;
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return canSustainBush(world.getBlockState(pos.down()));
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos)
                && canSustainBush(world.getBlockState(pos.down()));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityArborisSapling();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!world.isRemote && placer instanceof EntityPlayer) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityArborisSapling) {
                ((TileEntityArborisSapling) tileEntity).setOwner((EntityPlayer) placer);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
