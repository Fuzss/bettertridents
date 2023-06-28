package fuzs.bettertridents.capability;

import net.minecraft.nbt.CompoundTag;

public class TridentSlotCapabilityImpl implements TridentSlotCapability {
    private int slot = -1;

    @Override
    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public void write(CompoundTag tag) {
        if (this.slot != -1) {
            tag.putInt("TridentSlot", this.slot);
        }
    }

    @Override
    public void read(CompoundTag tag) {
        this.slot = tag.getInt("TridentSlot");
    }
}
