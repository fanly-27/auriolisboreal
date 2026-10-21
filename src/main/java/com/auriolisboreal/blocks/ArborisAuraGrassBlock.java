package com.auriolisboreal.blocks;

import com.auriolisboreal.ArborisAuraConfig;
import com.auriolisboreal.AuriolisBorealMod;
import com.auriolisboreal.MicroBiome;
import com.auriolisboreal.tileentity.TileEntityArborisAura;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;
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
        if (player.getHeldItem(hand).getItem() != Items.STICK) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }

        TileEntityArborisAura aura = MicroBiome.findTree(world, pos);
        if (aura == null) {
            player.sendMessage(new TextComponentString("No se encontr\u00f3 ning\u00fan Arboris Aura cerca."));
            return true;
        }

        double capacity = aura.getNutritionCapacity();
        String text;
        if (capacity <= 0.0) {
            text = "Fase " + aura.getPhase() + ": esta fase no usa nutrici\u00f3n.";
        } else {
            text = String.format(Locale.ROOT, "Nutrici\u00f3n: %.1f / %.1f (%s)",
                    aura.getNutrition(), capacity, aura.isMalnourished() ? "desnutrido" : "bien nutrido");
        }
        player.sendMessage(new TextComponentString(text));
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
