package com.auriolisboreal.tileentity;

import com.auriolisboreal.ArborisAuraConfig;
import com.auriolisboreal.ModBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityArborisSapling extends TileEntity implements ITickable {
    private static final String TAG_GROWTH = "growthTicks";
    private static final String TAG_OWNER_UUID = "ownerUuid";
    private static final String TAG_OWNER_NAME = "ownerName";

    private long growthTicks;
    private String ownerUuid = "";
    private String ownerName = "";

    @Override
    public void update() {
        if (world == null || world.isRemote || world.getTotalWorldTime() % 20L != 0L) {
            return;
        }

        growthTicks += 20L;
        if (growthTicks >= ArborisAuraConfig.getSaplingGrowthTicks()) {
            grow();
        } else {
            markDirty();
        }
    }

    public void setOwner(EntityPlayer player) {
        ownerUuid = player.getUniqueID().toString();
        ownerName = player.getName();
        markDirty();
    }

    private void grow() {
        BlockPos pos = getPos();
        String uuid = ownerUuid;
        String name = ownerName;

        world.setBlockState(pos, ModBlocks.arboris_aura.getDefaultState(), 3);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityArborisAura) {
            ((TileEntityArborisAura) te).startFromSapling(uuid, name);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setLong(TAG_GROWTH, growthTicks);
        compound.setString(TAG_OWNER_UUID, ownerUuid);
        compound.setString(TAG_OWNER_NAME, ownerName);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        growthTicks = Math.max(0L, compound.getLong(TAG_GROWTH));
        ownerUuid = compound.getString(TAG_OWNER_UUID);
        ownerName = compound.getString(TAG_OWNER_NAME);
    }
}
