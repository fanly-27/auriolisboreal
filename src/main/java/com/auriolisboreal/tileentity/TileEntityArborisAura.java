package com.auriolisboreal.tileentity;

import com.auriolisboreal.ArborisAuraConfig;
import com.auriolisboreal.blocks.ArborisAuraBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityArborisAura extends TileEntity implements ITickable {
    private static final String TAG_PHASE = "phase";
    private static final String TAG_NUTRITION = "nutrition";
    private static final String TAG_PROGRESS_TICKS = "progressTicks";
    private static final String TAG_MALNOURISHED = "malnourished";

    private int phase;
    private double nutrition;
    private long progressTicks;
    private boolean malnourished;

    @Override
    public void update() {
        if (world == null || world.isRemote || world.getTotalWorldTime() % 20L != 0L || phase >= 10) {
            return;
        }

        if (phase > 0 && nutrition <= 0.0) {
            nutrition = 0.0;
            malnourished = true;
            markDirty();
            return;
        }

        if (phase > 0) {
            double consumed = ArborisAuraConfig.getConsumptionPerDay(phase) * 20.0 / 24000.0;
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
        return ArborisAuraConfig.getCapacity(phase);
    }

    public long getProgressTicks() {
        return progressTicks;
    }

    public boolean isMalnourished() {
        return malnourished;
    }

    public long getTicksForNextPhase() {
        return phase >= 10 ? 0L : 24000L * (phase + 1);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger(TAG_PHASE, phase);
        compound.setDouble(TAG_NUTRITION, nutrition);
        compound.setLong(TAG_PROGRESS_TICKS, progressTicks);
        compound.setBoolean(TAG_MALNOURISHED, malnourished);
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
        }
    }

        @Override
    public boolean shouldRefresh(net.minecraft.world.World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
