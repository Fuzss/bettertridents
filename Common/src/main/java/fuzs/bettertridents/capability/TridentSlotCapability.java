package fuzs.bettertridents.capability;

import fuzs.puzzleslib.capability.data.CapabilityComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface TridentSlotCapability extends CapabilityComponent {

    void setSlot(int slot);

    int getSlot();

    default boolean addItemToInventory(Player player, ItemStack stack) {
        int slot = this.findSlotAtIndex(player.getInventory(), this.getSlot());
        if (slot == 40) {
            player.setItemSlot(EquipmentSlot.OFFHAND, stack);
            // just some stuff Inventory::add also does (stack wasn't copied, so it's still the same)
            stack.setPopTime(5);
            return true;
        }
        return player.getInventory().add(slot, stack);
    }

    private int findSlotAtIndex(Inventory inventory, int slot) {
        if (slot != -1) {
            // try to return to main hand as secondary option
            int currentSlot = inventory.getSelected().isEmpty() ? inventory.selected : -1;
            if (inventory.getItem(slot).isEmpty()) {
                currentSlot = slot;
            }
            return currentSlot;
        }
        return -1;
    }
}
