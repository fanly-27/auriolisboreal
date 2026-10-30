package com.auriolisboreal;

import com.auriolisboreal.tileentity.TileEntityArborisAura;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFlower;
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

    private static boolean isSoil(IBlockState s) {
        Block b = s.getBlock();
        return b == ModBlocks.arboris_aura_grass
                || b == Blocks.GRASS
                || (b == Blocks.DIRT && s.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT);
    }

    private static boolean isOwnBlock(Block b) {
        return b == ModBlocks.arboris_aura
                || b == ModBlocks.arboris_aura_trunk
                || b == ModBlocks.arboris_aura_leaves
                || b == ModBlocks.arboris_aura_sapling
                || b == ModBlocks.arboris_aura_grass
                || b == ModBlocks.arboris_aura_tallgrass;
    }

    /** Nutrición que da una vegetación que no es del árbol (0 = no es vegetación). */
    private static double nutritionOf(World world, BlockPos p, IBlockState s) {
        Block b = s.getBlock();
        Material m = s.getMaterial();
        if (m == Material.LEAVES) {
            return ArborisAuraConfig.getNutritionLeaves();
        }
        if (b.isWood(world, p)) {
            return ArborisAuraConfig.getNutritionLog();
        }
        if (b instanceof BlockCrops) {
            return ArborisAuraConfig.getNutritionCrop();
        }
        if (b instanceof BlockFlower) {
            return ArborisAuraConfig.getNutritionFlower();
        }
        if (m == Material.PLANTS || m == Material.VINE || m == Material.CACTUS || m == Material.GOURD) {
            return ArborisAuraConfig.getNutritionPlant();
        }
        return 0.0;
    }

    /** Quita la vegetación ajena sobre el suelo de la columna y devuelve la nutrición ganada. */
    private static double absorbVegetation(World world, BlockPos ground) {
        double total = 0.0;
        int top = Math.min(255, ground.getY() + ArborisAuraConfig.getAbsorbHeight());
        for (int y = ground.getY() + 1; y <= top; y++) {
            BlockPos p = new BlockPos(ground.getX(), y, ground.getZ());
            IBlockState s = world.getBlockState(p);
            Block b = s.getBlock();
            if (b == Blocks.AIR || isOwnBlock(b)) {
                continue;
            }
            double value = nutritionOf(world, p, s);
            if (value > 0.0) {
                world.setBlockState(p, Blocks.AIR.getDefaultState(), 2);
                total += value;
            }
        }
        return total;
    }

    /**
     * Procesa una columna (x, z) del micro-bioma: absorbe la vegetación ajena (da nutrición al árbol),
     * convierte pasto/tierra normal en hierba Aura y, al convertir, a veces pone pasto en cruz.
     */
    public static void processColumn(World world, TileEntityArborisAura aura, int x, int baseY, int z) {
        int groundY = Integer.MIN_VALUE;
        for (int y = baseY + 2; y >= baseY - 3; y--) {
            if (isSoil(world.getBlockState(new BlockPos(x, y, z)))) {
                groundY = y;
                break;
            }
        }
        if (groundY == Integer.MIN_VALUE) {
            return;
        }
        BlockPos ground = new BlockPos(x, groundY, z);

        if (ArborisAuraConfig.isRemovePlants()) {
            double gained = absorbVegetation(world, ground);
            if (gained > 0.0) {
                aura.absorbNutrition(gained);
            }
        }

        boolean justConverted = false;
        IBlockState groundState = world.getBlockState(ground);
        if (groundState.getBlock() != ModBlocks.arboris_aura_grass
                && !world.getBlockState(ground.up()).isOpaqueCube()) {
            world.setBlockState(ground, ModBlocks.arboris_aura_grass.getDefaultState(), 2);
            justConverted = true;
        }

        BlockPos above = ground.up();
        if (justConverted && world.isAirBlock(above)
                && world.rand.nextInt(100) < ArborisAuraConfig.getPlantChancePercent()) {
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
