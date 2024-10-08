package fuzs.bettertridents.capability;

import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;

public class TridentSlotCapability extends CapabilityComponent<ThrownTrident> {
    private static final String TAG_TRIDENT_SLOT = "trident_slot";

    private int slot = -1;

    public void setSlot(int slot) {
        if (this.slot != slot) {
            this.slot = slot;
            this.setChanged();
        }
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean addItemToInventory(Player player, ItemStack itemStack) {
        this.verifyEquippedItem(itemStack);
        Inventory inventory = player.getInventory();
        int slot = this.findSlotAtIndex(inventory, this.getSlot());
        if (slot != -1) {
            inventory.setItem(slot, itemStack.copy());
            inventory.getItem(slot).setPopTime(5);
            itemStack.setCount(0);
            return true;
        } else {
            return inventory.add(itemStack);
        }
    }

    private void verifyEquippedItem(ItemStack itemStack) {
        // copied from LivingEntity::verifyEquippedItem as the method is protected
        itemStack.getItem().verifyComponentsAfterLoad(itemStack);
    }

    private int findSlotAtIndex(Inventory inventory, int slot) {
        if (slot != -1 && inventory.getItem(slot).isEmpty()) {
            return slot;
        } else if (inventory.getSelected().isEmpty()) {
            // try to return to main hand as secondary option
            return inventory.selected;
        }
        return -1;
    }

    @Override
    public void write(CompoundTag compoundTag, HolderLookup.Provider registries) {
        if (this.slot != -1) {
            compoundTag.putInt(TAG_TRIDENT_SLOT, this.slot);
        }
    }

    @Override
    public void read(CompoundTag compoundTag, HolderLookup.Provider registries) {
        this.slot = compoundTag.getInt(TAG_TRIDENT_SLOT);
    }
}
