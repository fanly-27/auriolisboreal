package com.auriolisboreal.blocks;

import com.auriolisboreal.ArborisAuraConfig;
import com.auriolisboreal.AuriolisBorealMod;
import com.auriolisboreal.tileentity.TileEntityArborisAura;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Locale;

public class ArborisAuraBlock extends Block {
    public static final PropertyInteger PHASE = PropertyInteger.create("phase", 0, 10);

    public ArborisAuraBlock() {
        super(Material.WOOD);
        setTranslationKey("arboris_aura");
        setRegistryName(new ResourceLocation(AuriolisBorealMod.MODID, "arboris_aura"));
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);
        setDefaultState(blockState.getBaseState().withProperty(PHASE, 0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PHASE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(PHASE, Math.max(0, Math.min(10, meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PHASE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityArborisAura();
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
        ItemStack heldItem = player.getHeldItem(hand);
        boolean debugStick = heldItem.getItem() == Items.STICK;
        boolean boneMeal = heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15;
        if (!debugStick && !boneMeal) {
            return false;
        }

        if (world.isRemote) {
            return true;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityArborisAura)) {
            return false;
        }

        TileEntityArborisAura aura = (TileEntityArborisAura) tileEntity;
        if (debugStick) {
            int phase = aura.getPhase();
            long targetTicks = aura.getTicksForNextPhase();
            String progress = targetTicks == 0
                    ? "max"
                    : String.format(Locale.ROOT, "%d/%d ticks", aura.getProgressTicks(), targetTicks);
            player.sendMessage(new TextComponentTranslation(
                    "message.auriolisboreal.arboris_aura.debug",
                    phase,
                    String.format(Locale.ROOT, "%.1f", aura.getNutrition()),
                    String.format(Locale.ROOT, "%.1f", aura.getNutritionCapacity()),
                    progress,
                    aura.isMalnourished()
            ));
            return true;
        }

        if (aura.addNutrition(10.0)) {
            if (!player.capabilities.isCreativeMode) {
                heldItem.shrink(1);
            }
        }
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return ArborisAuraConfig.getFireFlammability();
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return ArborisAuraConfig.getFireEncouragement();
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return ArborisAuraConfig.getTrunkHardness();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return ArborisAuraConfig.getTrunkResistance();
    }
}
