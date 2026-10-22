package com.auriolisboreal;

import com.auriolisboreal.tileentity.TileEntityArborisAura;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
