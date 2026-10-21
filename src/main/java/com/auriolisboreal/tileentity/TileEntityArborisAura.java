package com.auriolisboreal.tileentity;

import com.auriolisboreal.ArborisAuraConfig;
import com.auriolisboreal.MicroBiome;
import com.auriolisboreal.ModBlocks;
import com.auriolisboreal.blocks.ArborisAuraBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityArborisAura extends TileEntity implements ITickable {
    private static final String TAG_PHASE = "phase";
    private static final String TAG_NUTRITION = "nutrition";
    private static final String TAG_PROGRESS_TICKS = "progressTicks";
    private static final String TAG_MALNOURISHED = "malnourished";
    private static final String TAG_OWNER_UUID = "ownerUuid";
    private static final String TAG_OWNER_NAME = "ownerName";
    private static final String TAG_BIOME_RADIUS = "biomeRadius";
    private static final String TAG_BIOME_INDEX = "biomeIndex";

    // Altura del tronco por fase (índice = fase)
    private static final int[] HEIGHTS = {1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14};
    // Radio de la copa por fase
    private static final int[] RADII = {0, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4};

    private int phase;
    private double nutrition;
    private long progressTicks;
    private boolean malnourished;
    private String ownerUuid = "";
    private String ownerName = "";
    private int biomeRadius;
    private int biomeIndex;

    @Override
    public void update() {
        if (world == null || world.isRemote || world.getTotalWorldTime() % 20L != 0L) {
            return;
        }

        processMicroBiome();

        if (phase >= 10) {
            return;
        }

        if (phase > 0 && nutrition <= 0.0) {
            nutrition = 0.0;
            malnourished = true;
            markDirty();
            return;
        }

        if (phase > 0) {
            double consumed = ArborisAuraConfig.getConsumptionPerDay(phase) * 20.0 / 24.0;
            nutrition = Math.max(0.0, nutrition - consumed);
            malnourished = nutrition <= 0.0;
        }

        progressTicks += 20L;
        if (progressTicks >= getTicksForNextPhase()) {
            advancePhase();
        }
        markDirty();
    }

    public boolean addNutrition(double amount) {
        if (phase == 0 || amount <= 0.0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
            return false;
        }

        double capacity = getNutritionCapacity();
        if (nutrition >= capacity) {
            return false;
        }

        nutrition = Math.min(capacity, nutrition + amount);
        malnourished = nutrition <= 0.0;
        markDirty();
        return true;
    }

    public int getPhase() {
        return phase;
    }

    public double getNutrition() {
        return nutrition;
    }

    public double getNutritionCapacity() {
        return phase >= 10 ? 0L : 50L * (phase + 1);
    }

    public long getProgressTicks() {
        return progressTicks;
    }

    public boolean isMalnourished() {
        return malnourished;
    }

    public long getTicksForNextPhase() {
        return phase >= 10 ? 0L : 24L * (phase + 1);
    }

    // ---- Dueño ----

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwner(EntityPlayer player) {
        ownerUuid = player.getUniqueID().toString();
        ownerName = player.getName();
        markDirty();
    }

    /** Lo llama el brote al convertirse en árbol: guarda el dueño y pasa de la fase 0 a la 1. */
    public void startFromSapling(String uuid, String name) {
        ownerUuid = uuid == null ? "" : uuid;
        ownerName = name == null ? "" : name;
        if (phase == 0) {
            advancePhase();
        }
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger(TAG_PHASE, phase);
        compound.setDouble(TAG_NUTRITION, nutrition);
        compound.setLong(TAG_PROGRESS_TICKS, progressTicks);
        compound.setBoolean(TAG_MALNOURISHED, malnourished);
        compound.setString(TAG_OWNER_UUID, ownerUuid);
        compound.setString(TAG_OWNER_NAME, ownerName);
        compound.setInteger(TAG_BIOME_RADIUS, biomeRadius);
        compound.setInteger(TAG_BIOME_INDEX, biomeIndex);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        phase = Math.max(0, Math.min(10, compound.getInteger(TAG_PHASE)));
        nutrition = compound.getDouble(TAG_NUTRITION);
        if (Double.isNaN(nutrition) || Double.isInfinite(nutrition)) {
            nutrition = 0.0;
        }
        nutrition = Math.max(0.0, Math.min(getNutritionCapacity(), nutrition));
        if (phase == 0) {
            nutrition = 0.0;
        }
        progressTicks = Math.max(0L, compound.getLong(TAG_PROGRESS_TICKS));
        malnourished = compound.getBoolean(TAG_MALNOURISHED);
        ownerUuid = compound.getString(TAG_OWNER_UUID);
        ownerName = compound.getString(TAG_OWNER_NAME);
        biomeRadius = Math.max(0, compound.getInteger(TAG_BIOME_RADIUS));
        biomeIndex = Math.max(0, compound.getInteger(TAG_BIOME_INDEX));
    }

    private void advancePhase() {
        phase++;
        progressTicks = 0L;
        malnourished = phase > 0 && nutrition <= 0.0;

        if (world != null) {
            BlockPos pos = getPos();
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof ArborisAuraBlock) {
                world.setBlockState(pos, state.withProperty(ArborisAuraBlock.PHASE, phase), 3);
            }
            growShape();
            biomeRadius = ArborisAuraConfig.getBiomeRadius(phase);
            biomeIndex = 0;
        }
    }

    // ---- Micro-bioma ----

    /** Convierte el suelo poco a poco (columnas por segundo según la config) para no trabar el juego. */
    private void processMicroBiome() {
        if (biomeRadius <= 0) {
            return;
        }
        int side = biomeRadius * 2 + 1;
        int total = side * side;
        if (biomeIndex >= total) {
            return;
        }

        int budget = ArborisAuraConfig.getColumnsPerStep();
        int r2 = biomeRadius * biomeRadius;
        BlockPos base = getPos();
        while (budget > 0 && biomeIndex < total) {
            int dx = biomeIndex % side - biomeRadius;
            int dz = biomeIndex / side - biomeRadius;
            if (dx * dx + dz * dz > r2) {
                biomeIndex++;
                continue;
            }
            BlockPos column = new BlockPos(base.getX() + dx, base.getY(), base.getZ() + dz);
            if (!world.isBlockLoaded(column)) {
                break;
            }
            MicroBiome.convertColumn(world, column.getX(), base.getY(), column.getZ());
            biomeIndex++;
            budget--;
        }
        markDirty();
    }

    // ---- Forma del árbol ----

    private boolean canReplace(BlockPos p) {
        IBlockState s = world.getBlockState(p);
        return world.isAirBlock(p)
                || s.getMaterial() == Material.LEAVES
                || s.getBlock().isReplaceable(world, p);
    }

    private void placeTrunk(BlockPos p) {
        if (canReplace(p)) {
            world.setBlockState(p, ModBlocks.arboris_aura_trunk.getDefaultState(), 3);
        }
    }

    private void placeLeaf(BlockPos p) {
        if (world.isAirBlock(p) || world.getBlockState(p).getBlock().isReplaceable(world, p)) {
            world.setBlockState(p, ModBlocks.arboris_aura_leaves.getDefaultState(), 3);
        }
    }

    /** Borra las hojas de la copa anterior para que no queden flotando al crecer. */
    private void clearOldLeaves() {
        BlockPos base = getPos();
        int reach = 7;
        int top = HEIGHTS[10] + 4;
        for (int dx = -reach; dx <= reach; dx++) {
            for (int dz = -reach; dz <= reach; dz++) {
                for (int y = 0; y <= top; y++) {
                    BlockPos p = base.add(dx, y, dz);
                    if (world.getBlockState(p).getBlock() == ModBlocks.arboris_aura_leaves) {
                        world.setBlockToAir(p);
                    }
                }
            }
        }
    }

    private void growShape() {
        int h = HEIGHTS[phase];
        int r = RADII[phase];
        BlockPos base = getPos();

        clearOldLeaves();

        // Tronco central (el bloque base queda en y+0)
        for (int y = 1; y < h; y++) {
            placeTrunk(base.up(y));
        }

        // Tronco 2x2 en fases 9 y 10
        if (phase >= 9) {
            for (int y = 0; y < h; y++) {
                placeTrunk(base.add(1, y, 0));
                placeTrunk(base.add(0, y, 1));
                placeTrunk(base.add(1, y, 1));
            }
        }

        // Copa estilo roble oscuro: ancha y achatada
        int cx = base.getX();
        int cz = base.getZ();
        int extra = phase >= 9 ? 1 : 0; // la copa se centra en el 2x2
        for (int dy = -1; dy <= 2; dy++) {
            int layerR = (dy == 2) ? Math.max(1, r - 2) : (dy == 1 ? r - 1 : r);
            for (int dx = -layerR; dx <= layerR + extra; dx++) {
                for (int dz = -layerR; dz <= layerR + extra; dz++) {
                    // Recortar esquinas
                    if (Math.abs(dx) == layerR && Math.abs(dz) == layerR && layerR > 1) {
                        continue;
                    }
                    placeLeaf(new BlockPos(cx + dx, base.getY() + h + dy, cz + dz));
                }
            }
        }
    }

    @Override
    public boolean shouldRefresh(net.minecraft.world.World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
