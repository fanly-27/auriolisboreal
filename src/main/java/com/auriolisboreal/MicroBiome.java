package com.auriolisboreal;

import com.auriolisboreal.tileentity.TileEntityArborisAura;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Locale;

public final class MicroBiome {
    private MicroBiome() {
    }

    /** Convierte una columna (x, z) del micro-bioma: hierba/tierra -> hierba Aura y, opcionalmente, pasto en cruz. */
    public static void convertColumn(World world, int x, int baseY, int z) {
        BlockPos ground = null;
        boolean justConverted = false;

        for (int y = baseY + 2; y >= baseY - 3; y--) {
            BlockPos p = new BlockPos(x, y, z);
            IBlockState s = world.getBlockState(p);
            Block b = s.getBlock();
            if (b == ModBlocks.arboris_aura_grass) {
                ground = p;
                break;
            }
            boolean soil = b == Blocks.GRASS
                    || (b == Blocks.DIRT && s.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT);
            if (soil && !world.getBlockState(p.up()).isOpaqueCube()) {
                world.setBlockState(p, ModBlocks.arboris_aura_grass.getDefaultState(), 2);
                ground = p;
                justConverted = true;
                break;
            }
        }

        if (ground == null || !justConverted) {
            return;
        }

        BlockPos above = ground.up();
        IBlockState aboveState = world.getBlockState(above);
        Block aboveBlock = aboveState.getBlock();
        Material material = aboveState.getMaterial();
        boolean isPlant = (material == Material.PLANTS || material == Material.VINE)
                && aboveBlock != ModBlocks.arboris_aura_tallgrass
                && aboveBlock != ModBlocks.arboris_aura_sapling;
        if (isPlant && ArborisAuraConfig.isRemovePlants()) {
            world.setBlockToAir(above);
        }

        if (world.isAirBlock(above) && world.rand.nextInt(100) < ArborisAuraConfig.getPlantChancePercent()) {
            world.setBlockState(above, ModBlocks.arboris_aura_tallgrass.getDefaultState(), 2);
        }
    }

    /** Devuelve el suelo a hierba normal y quita el pasto Aura alrededor de un árbol que desaparece. */
    public static void revert(World world, BlockPos center) {
        int r = ArborisAuraConfig.getBiomeRadius(10);
        if (r <= 0) {
            return;
        }
        int r2 = r * r;
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (dx * dx + dz * dz > r2) {
                    continue;
                }
                for (int y = center.getY() + 4; y >= center.getY() - 4; y--) {
                    BlockPos p = new BlockPos(center.getX() + dx, y, center.getZ() + dz);
                    if (!world.isBlockLoaded(p)) {
                        break;
                    }
                    Block b = world.getBlockState(p).getBlock();
                    if (b == ModBlocks.arboris_aura_grass) {
                        world.setBlockState(p, Blocks.GRASS.getDefaultState(), 2);
                    } else if (b == ModBlocks.arboris_aura_tallgrass) {
                        world.setBlockToAir(p);
                    }
                }
            }
        }
    }

    /**
     * Clic derecho sobre el suelo o el pasto del micro-bioma:
     * palo = revisar la nutrición, polvo de hueso = nutrir al árbol.
     */
    public static boolean interact(World world, BlockPos pos, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        boolean stick = held.getItem() == Items.STICK;
        boolean boneMeal = held.getItem() == Items.DYE && held.getMetadata() == 15;
        if (!stick && !boneMeal) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }

        TileEntityArborisAura aura = findTree(world, pos);
        if (aura == null) {
            player.sendMessage(new TextComponentString("No se encontr\u00f3 ning\u00fan Arboris Aura cerca."));
            return true;
        }

        double capacity = aura.getNutritionCapacity();
        if (aura.getPhase() == 0 || capacity <= 0.0) {
            player.sendMessage(new TextComponentString(
                    "Fase " + aura.getPhase() + ": esta fase no usa nutrici\u00f3n."));
            return true;
        }

        if (boneMeal) {
            if (aura.addNutrition(10.0)) {
                if (!player.capabilities.isCreativeMode) {
                    held.shrink(1);
                }
            } else {
                player.sendMessage(new TextComponentString(
                        "El \u00e1rbol no puede recibir m\u00e1s nutrici\u00f3n ahora."));
                return true;
            }
        }

        player.sendMessage(new TextComponentString(String.format(Locale.ROOT,
                "Nutrici\u00f3n: %.1f / %.1f (%s)",
                aura.getNutrition(), capacity, aura.isMalnourished() ? "desnutrido" : "bien nutrido")));
        return true;
    }

    /** Busca el Arboris Aura más cercano a una posición (para leer la nutrición con el palo). */
    public static TileEntityArborisAura findTree(World world, BlockPos from) {
        int r = ArborisAuraConfig.getBiomeRadius(10);
        TileEntityArborisAura best = null;
        double bestDistance = Double.MAX_VALUE;
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                double distance = dx * dx + dz * dz;
                if (distance >= bestDistance) {
                    continue;
                }
                for (int y = from.getY() - 4; y <= from.getY() + 4; y++) {
                    BlockPos p = new BlockPos(from.getX() + dx, y, from.getZ() + dz);
                    if (!world.isBlockLoaded(p)) {
                        break;
                    }
                    if (world.getBlockState(p).getBlock() == ModBlocks.arboris_aura) {
                        TileEntity te = world.getTileEntity(p);
                        if (te instanceof TileEntityArborisAura) {
                            best = (TileEntityArborisAura) te;
                            bestDistance = distance;
                            break;
                        }
                    }
                }
            }
        }
        return best;
    }
}
